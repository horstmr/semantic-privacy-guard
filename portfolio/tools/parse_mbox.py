#!/usr/bin/env python3
"""
Parser de mbox (Thunderbird) para o portfólio de traduções.

Uso:
    python3 parse_mbox.py <arquivo.mbox> [outro.mbox ...] --out DIR

Produz em DIR:
    mensagens.jsonl      uma linha por mensagem: data, de, para, assunto, corpo em texto
    anexos/              anexos extraídos (PDF etc.), nomeados <msgid>__<arquivo>
    jobs.csv             trabalhos detectados por agência, com volume quando disponível
    valores.csv          valores monetários detectados (vouchers, remessas, créditos)
    resumo.txt           contagens por agência e por ano

Não depende de rede. Aceita mbox puro ou .gz.
"""

import argparse
import csv
import email
import email.policy
import gzip
import json
import mailbox
import os
import re
import sys
from collections import Counter, defaultdict
from datetime import datetime, timezone
from email.utils import parsedate_to_datetime
from html.parser import HTMLParser


# ---------------------------------------------------------------- utilidades

class _Stripper(HTMLParser):
    def __init__(self):
        super().__init__()
        self.parts = []
        self._skip = 0

    def handle_starttag(self, tag, attrs):
        if tag in ("script", "style"):
            self._skip += 1

    def handle_endtag(self, tag):
        if tag in ("script", "style") and self._skip:
            self._skip -= 1

    def handle_data(self, data):
        if not self._skip:
            self.parts.append(data)


def html_to_text(html):
    s = _Stripper()
    try:
        s.feed(html)
    except Exception:
        return re.sub(r"<[^>]+>", " ", html)
    return re.sub(r"[ \t\r\f\v]+", " ", " ".join(s.parts))


def body_text(msg):
    """Extrai o corpo em texto, preferindo text/plain e caindo para text/html."""
    plain, html = [], []
    for part in msg.walk():
        if part.get_content_maintype() == "multipart":
            continue
        if part.get_filename():
            continue
        ctype = part.get_content_type()
        try:
            payload = part.get_payload(decode=True)
            if payload is None:
                continue
            charset = part.get_content_charset() or "utf-8"
            text = payload.decode(charset, errors="replace")
        except Exception:
            continue
        if ctype == "text/plain":
            plain.append(text)
        elif ctype == "text/html":
            html.append(text)
    if plain:
        return "\n".join(plain)
    if html:
        return html_to_text("\n".join(html))
    return ""


def msg_date(msg):
    raw = msg.get("Date")
    if not raw:
        return None
    try:
        dt = parsedate_to_datetime(raw)
        if dt.tzinfo is None:
            dt = dt.replace(tzinfo=timezone.utc)
        return dt.astimezone(timezone.utc)
    except Exception:
        return None


def addrs(msg, header):
    v = msg.get(header, "") or ""
    return re.findall(r"[\w.+-]+@[\w.-]+\.\w+", v.lower())


def open_mbox(path):
    """Aceita mbox puro ou .gz (descompacta para um temporário ao lado)."""
    if path.endswith(".gz"):
        plain = path[:-3]
        if not os.path.exists(plain):
            with gzip.open(path, "rb") as fi, open(plain, "wb") as fo:
                while True:
                    chunk = fi.read(1 << 20)
                    if not chunk:
                        break
                    fo.write(chunk)
        path = plain
    return mailbox.mbox(path, factory=None)


# ------------------------------------------------------- sinais por agência
# Cada entrada: (agência, regex no remetente, regex de "trabalho concluído")
# Derivado de portfolio/CONTEXTO.md — ver a tabela de sinais de conclusão.

AGENCIES = [
    ("LanguageWire",    r"languagewire\.com"),
    ("Alconost",        r"alconost\.com"),
    ("Flexword",        r"flexword\.de"),
    ("TextUnited",      r"textunited\.com"),
    ("TextMaster",      r"textmaster\.com"),
    ("TranslateMedia",  r"translatemedia\.com"),
    ("Toppan",          r"toppandigital\.com"),
    ("MyLanguageConn",  r"mylanguageconnection\.com"),
    ("e2f",             r"e2f\.com"),
    ("GameTransLab",    r"gametranslab\.com"),
    ("FutureTrans",     r"future-trans\.com"),
]

DONE_SIGNALS = [
    ("LanguageWire",   re.compile(r"job is in progress", re.I)),
    ("Alconost",       re.compile(r"is delivered, and will be paid", re.I)),
    ("Toppan",         re.compile(r"Account crediting in process|Successful Upload", re.I)),
    ("TranslateMedia", re.compile(r"Successful Upload", re.I)),
    ("TextMaster",     re.compile(r"tarefa foi aprovada|has been approved", re.I)),
    ("Flexword",       re.compile(r"Purchase order O-", re.I)),
]

NOT_DONE = re.compile(r"Tender closed|has been cancelled|cancellation", re.I)

# Campos estruturados
RE_WORDCOUNT = re.compile(r"Word count:\s*([\d.,]+)", re.I)
RE_WORKAREA  = re.compile(r"Work area:\s*([^\n<]+)", re.I)
RE_JOBID     = re.compile(r"Job ID:\s*(\d+)", re.I)
RE_JOBNAME   = re.compile(r"Job name:\s*([^\n<]+)", re.I)
RE_ENTITY    = re.compile(r"Entity:\s*([^\n<]+)", re.I)
RE_ALCONOST  = re.compile(r"Volume \(total (words|chars with spaces)\):\s*([\d.,]+)", re.I)
RE_SUBJ_WORDS = re.compile(r"/\s*([\d.,]+)\s+words", re.I)

# Valores monetários
RE_MONEY = re.compile(
    r"(?:(?:credited with the sum of|a payment of|Amount:|Fee)\s*)"
    r"([£$€]|EUR|USD|GBP|R\$)\s?([\d]{1,3}(?:[.,]\d{3})*(?:[.,]\d{1,2})?)",
    re.I,
)


def agency_of(froms, tos):
    pool = " ".join(froms + tos)
    for name, pat in AGENCIES:
        if re.search(pat, pool):
            return name
    return None


def num(s):
    """Converte '1.234,56' ou '1,234.56' ou '17599' em float."""
    s = s.strip()
    if "," in s and "." in s:
        s = s.replace(".", "").replace(",", ".") if s.rfind(",") > s.rfind(".") else s.replace(",", "")
    elif "," in s:
        s = s.replace(",", ".") if len(s.split(",")[-1]) <= 2 else s.replace(",", "")
    return float(s)


# ------------------------------------------------------------------- main

def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("mbox", nargs="+")
    ap.add_argument("--out", default="saida_mbox")
    ap.add_argument("--attachments", action="store_true",
                    help="extrair anexos (PDF etc.) para <out>/anexos")
    args = ap.parse_args()

    os.makedirs(args.out, exist_ok=True)
    att_dir = os.path.join(args.out, "anexos")
    if args.attachments:
        os.makedirs(att_dir, exist_ok=True)

    seen_msgids = set()
    per_agency = Counter()
    per_agency_year = defaultdict(Counter)
    done_rows, money_rows = [], []
    total = dupes = 0

    jsonl = open(os.path.join(args.out, "mensagens.jsonl"), "w", encoding="utf-8")

    for path in args.mbox:
        print(f"[+] lendo {path} ...", file=sys.stderr)
        box = open_mbox(path)
        for key in box.keys():
            try:
                raw = box.get_bytes(key)
                msg = email.message_from_bytes(raw, policy=email.policy.default)
            except Exception as e:
                print(f"    ! erro na mensagem {key}: {e}", file=sys.stderr)
                continue

            total += 1
            mid = (msg.get("Message-ID") or f"__nomsgid_{path}_{key}").strip()
            if mid in seen_msgids:
                dupes += 1          # deduplicação entre as duas contas
                continue
            seen_msgids.add(mid)

            froms = addrs(msg, "From")
            tos = addrs(msg, "To") + addrs(msg, "Cc") + addrs(msg, "Delivered-To")
            subj = (msg.get("Subject") or "").replace("\n", " ").strip()
            dt = msg_date(msg)
            year = dt.year if dt else None
            text = body_text(msg)
            ag = agency_of(froms, tos)

            if ag:
                per_agency[ag] += 1
                if year:
                    per_agency_year[ag][year] += 1

            jsonl.write(json.dumps({
                "msgid": mid, "date": dt.isoformat() if dt else None,
                "from": froms, "to": tos, "subject": subj,
                "agency": ag, "body": text[:20000],
            }, ensure_ascii=False) + "\n")

            hay = subj + "\n" + text

            # --- trabalhos concluídos -------------------------------------
            if not NOT_DONE.search(hay):
                for name, pat in DONE_SIGNALS:
                    if pat.search(hay) and (ag == name or ag is None):
                        wc = RE_WORDCOUNT.search(text) or RE_ALCONOST.search(text)
                        words = ""
                        if wc:
                            words = wc.group(wc.lastindex)
                        elif RE_SUBJ_WORDS.search(subj):
                            words = RE_SUBJ_WORDS.search(subj).group(1)
                        done_rows.append({
                            "agencia": name,
                            "data": dt.date().isoformat() if dt else "",
                            "job_id": (RE_JOBID.search(text).group(1) if RE_JOBID.search(text) else ""),
                            "job_name": (RE_JOBNAME.search(text).group(1).strip() if RE_JOBNAME.search(text) else subj),
                            "cliente": (RE_ENTITY.search(text).group(1).strip() if RE_ENTITY.search(text) else ""),
                            "area": (RE_WORKAREA.search(text).group(1).strip() if RE_WORKAREA.search(text) else ""),
                            "palavras": words,
                            "assunto": subj,
                        })
                        break

            # --- valores ---------------------------------------------------
            for m in RE_MONEY.finditer(hay):
                try:
                    val = num(m.group(2))
                except Exception:
                    continue
                money_rows.append({
                    "agencia": ag or "", "data": dt.date().isoformat() if dt else "",
                    "moeda": m.group(1), "valor": val, "assunto": subj,
                })

            # --- anexos ----------------------------------------------------
            if args.attachments:
                for part in msg.walk():
                    fn = part.get_filename()
                    if not fn:
                        continue
                    safe = re.sub(r"[^\w.\-]", "_", fn)[:120]
                    stem = re.sub(r"[^\w]", "", mid)[:40]
                    try:
                        data = part.get_payload(decode=True)
                        if data:
                            with open(os.path.join(att_dir, f"{stem}__{safe}"), "wb") as f:
                                f.write(data)
                    except Exception:
                        pass

    jsonl.close()

    with open(os.path.join(args.out, "jobs.csv"), "w", newline="", encoding="utf-8") as f:
        w = csv.DictWriter(f, fieldnames=["agencia", "data", "job_id", "job_name",
                                          "cliente", "area", "palavras", "assunto"])
        w.writeheader()
        w.writerows(done_rows)

    with open(os.path.join(args.out, "valores.csv"), "w", newline="", encoding="utf-8") as f:
        w = csv.DictWriter(f, fieldnames=["agencia", "data", "moeda", "valor", "assunto"])
        w.writeheader()
        w.writerows(money_rows)

    lines = [f"mensagens lidas: {total}",
             f"duplicatas entre contas (mesmo Message-ID): {dupes}",
             f"mensagens únicas: {len(seen_msgids)}",
             f"trabalhos concluídos detectados: {len(done_rows)}",
             f"valores detectados: {len(money_rows)}", "",
             "Mensagens por agência:"]
    for ag, n in per_agency.most_common():
        anos = " ".join(f"{y}:{c}" for y, c in sorted(per_agency_year[ag].items()))
        lines.append(f"  {ag:16s} {n:6d}   {anos}")

    somas = defaultdict(float)
    for r in money_rows:
        somas[r["moeda"]] += r["valor"]
    lines += ["", "Soma bruta por moeda (revisar duplicatas antes de usar):"]
    for cur, v in somas.items():
        lines.append(f"  {cur} {v:,.2f}")

    out = "\n".join(lines)
    with open(os.path.join(args.out, "resumo.txt"), "w", encoding="utf-8") as f:
        f.write(out + "\n")
    print(out)


if __name__ == "__main__":
    main()
