#!/usr/bin/env python3
"""Extrai da tabela do SC Saúde (rol de procedimentos) os valores dos exames
do check-up e calcula a coparticipação de 30%.

Uso:
    python3 tuss/extrair_valores.py <planilha.xls|.xlsx|.csv>

A busca é feita pelo código TUSS E pelo nome do exame; divergências entre o
código informado e o encontrado pelo nome são sinalizadas (a planilha é a
fonte definitiva). Saída: tabela markdown no stdout e tuss/valores-extraidos.csv.
"""

import re
import sys
import unicodedata
from pathlib import Path

import pandas as pd

# (nome, codigo TUSS informado, padrões de nome, padrões de exclusão)
EXAMES = [
    ("Hemograma completo", "40304361", [r"\bHEMOGRAMA\b"], [r"RECONTAGEM"]),
    ("Glicose", "40302040", [r"\bGLICOSE\b"],
     [r"6\s*FOSFATO", r"POS[\s-]*(DEXTROSOL|PRANDIAL|SOBRECARGA)", r"CURVA", r"LIQUOR", r"TOLERANCIA"]),
    ("Colesterol total", "40301508", [r"\bCOLESTEROL\b"], [r"\bHDL\b", r"\bLDL\b", r"\bVLDL\b", r"ESTER"]),
    ("Colesterol HDL", None, [r"\bHDL\b"], []),
    ("Colesterol LDL", "40301656", [r"\bLDL\b"], []),
    ("Triglicerideos", "40302504", [r"TRIGLICER"], []),
    ("Creatinina", "40301630", [r"\bCREATININA\b"], [r"CLEARANCE", r"DEPURACAO", r"URINA"]),
    ("TGO (AST)", "40302377", [r"OXALACETICA", r"\bTGO\b", r"\bAST\b", r"ASPARTATO"], []),
    ("TGP (ALT)", "40302385", [r"PIRUVICA", r"\bTGP\b", r"\bALT\b", r"ALANINA"], []),
    ("TSH", "40316530", [r"\bTSH\b", r"TIREOESTIMULANTE", r"TIREOTROFICO"], [r"RECEPTOR", r"\bANTI\b"]),
    ("T4 livre", "40316521", [r"T4\s*LIVRE", r"TIROXINA\s*LIVRE"], []),
    ("Testosterona total", "40316513", [r"\bTESTOSTERONA\b"], [r"LIVRE", r"DIIDRO"]),
    ("Testosterona livre", "40316505", [r"\bTESTOSTERONA\s+LIVRE\b"], []),
    ("PSA total", "40316297", [r"\bPSA\b", r"ANTIGENO\s+PROSTATICO"], [r"LIVRE"]),
    ("Vitamina D (25-OH)", "40302733", [r"25.{0,3}HIDROXI", r"VITAMINA\s*D\b", r"25\s*OH"], [r"1[,.]25", r"DI.?HIDROXI"]),
]

COPARTICIPACAO = 0.30


def normalizar(texto):
    texto = unicodedata.normalize("NFKD", str(texto))
    texto = "".join(c for c in texto if not unicodedata.combining(c))
    return re.sub(r"\s+", " ", texto).upper().strip()


def eh_codigo_tuss(celula):
    return bool(re.fullmatch(r"4\d{7}", re.sub(r"\.0$", "", str(celula).strip())))


def como_codigo(celula):
    return re.sub(r"\.0$", "", str(celula).strip())


def como_valor(celula):
    """Converte célula em valor monetário (aceita '12,34', 'R$ 12,34', 12.34)."""
    if celula is None or (isinstance(celula, float) and pd.isna(celula)):
        return None
    if isinstance(celula, (int, float)):
        return float(celula)
    s = str(celula).strip().replace("R$", "").strip()
    if re.fullmatch(r"\d{1,3}(\.\d{3})*,\d{2}", s):
        return float(s.replace(".", "").replace(",", "."))
    if re.fullmatch(r"\d+,\d+", s):
        return float(s.replace(",", "."))
    if re.fullmatch(r"\d+(\.\d+)?", s):
        return float(s)
    return None


def carregar_abas(caminho):
    caminho = Path(caminho)
    if caminho.suffix.lower() == ".csv":
        for sep in (";", ","):
            try:
                df = pd.read_csv(caminho, sep=sep, header=None, dtype=str, encoding_errors="replace")
                if df.shape[1] > 1:
                    return {"csv": df}
            except Exception:
                continue
        raise SystemExit(f"Não consegui ler o CSV: {caminho}")
    engine = "xlrd" if caminho.suffix.lower() == ".xls" else None
    return pd.read_excel(caminho, sheet_name=None, header=None, dtype=object, engine=engine)


def extrair_linhas(abas):
    """Percorre todas as abas e devolve linhas (aba, codigo, descricao, valores)."""
    linhas = []
    for aba, df in abas.items():
        for _, row in df.iterrows():
            celulas = list(row)
            codigo = next((como_codigo(c) for c in celulas if eh_codigo_tuss(c)), None)
            if not codigo:
                continue
            textos = [str(c) for c in celulas if isinstance(c, str) and len(str(c).strip()) > 3
                      and not eh_codigo_tuss(c) and como_valor(c) is None]
            descricao = max(textos, key=len) if textos else ""
            idx_codigo = next(i for i, c in enumerate(celulas) if eh_codigo_tuss(c))
            valores = [v for c in celulas[idx_codigo + 1:] if (v := como_valor(c)) is not None and v > 0]
            linhas.append({"aba": aba, "codigo": codigo, "descricao": descricao, "valores": valores})
    return linhas


def procurar(linhas):
    resultados = []
    for nome, codigo_informado, padroes, exclusoes in EXAMES:
        candidatos = []
        for linha in linhas:
            desc = normalizar(linha["descricao"])
            bate_codigo = bool(codigo_informado) and linha["codigo"] == codigo_informado
            bate_nome = any(re.search(p, desc) for p in padroes) and not any(re.search(e, desc) for e in exclusoes)
            if bate_codigo or bate_nome:
                candidatos.append({**linha, "bate_codigo": bate_codigo, "bate_nome": bate_nome})
        resultados.append((nome, codigo_informado, candidatos))
    return resultados


def main():
    if len(sys.argv) != 2:
        raise SystemExit(__doc__)
    abas = carregar_abas(sys.argv[1])
    linhas = extrair_linhas(abas)
    if not linhas:
        raise SystemExit("Nenhuma linha com código TUSS (8 dígitos iniciando em 4) encontrada. "
                         "Confira se o arquivo é o rol de procedimentos.")
    print(f"Linhas com código TUSS encontradas: {len(linhas)}\n")

    saida = []
    print("| Exame | Código na planilha | Descrição na planilha | Valor tabela | Copart. 30% | Código bateu? |")
    print("|---|---|---|---|---|---|")
    for nome, codigo_informado, candidatos in procurar(linhas):
        if not candidatos:
            print(f"| {nome} | — | NÃO ENCONTRADO | — | — | informado: {codigo_informado or '?'} |")
            saida.append({"exame": nome, "codigo_informado": codigo_informado, "status": "nao encontrado"})
            continue
        for c in candidatos:
            # valor monetário costuma ser a coluna numérica mais à direita (CH/porte vêm antes)
            valor = c["valores"][-1] if c["valores"] else None
            copart = round(valor * COPARTICIPACAO, 2) if valor is not None else None
            flag = "✓" if c["bate_codigo"] else ("⚠ difere do informado" if codigo_informado else "código obtido pelo nome")
            fmt = lambda v: f"R$ {v:.2f}".replace(".", ",") if v is not None else "—"
            print(f"| {nome} | {c['codigo']} | {c['descricao'][:60]} | {fmt(valor)} | {fmt(copart)} | {flag} |")
            saida.append({"exame": nome, "codigo_informado": codigo_informado, "codigo_planilha": c["codigo"],
                          "descricao_planilha": c["descricao"], "valor_tabela": valor,
                          "coparticipacao_30": copart, "codigo_bateu": c["bate_codigo"], "aba": c["aba"]})

    destino = Path(__file__).parent / "valores-extraidos.csv"
    pd.DataFrame(saida).to_csv(destino, index=False)
    print(f"\nResultado gravado em {destino}")


if __name__ == "__main__":
    main()
