# 📚 Apostila Macetosa — Engenharia de IA & Prompt Engineering

Apostila PDF **visual e macetosa** (estilo caderno de estudos criativo / NotebookLM)
que condensa as duas trilhas do repositório em **19 capítulos de ~30 minutos**.

**[⬇️ Apostila-Macetosa-IA-e-Prompt-Engineering.pdf](./Apostila-Macetosa-IA-e-Prompt-Engineering.pdf)** — 22 páginas, A4.

## O que cada capítulo tem

- 🎯 **Cabeçalho colorido** com número e ⏱ 30 min
- 💡 **Macete** — o mnemônico/atalho mental (post-it)
- 🎨 **Imagem-âncora absurda** — uma cena bizarra de propósito (técnica de memória:
  imagens absurdas grudam mais que definições)
- 🗺️ **Esquema colorido** — o conceito em diagrama (fluxo, ciclo, escada, comparação)
- 🧩 **Cards de conceito** — 3 ideias-chave
- ✅ **Cola rápida** — o TL;DR do capítulo

## Como foi feito (reproduzível)

O PDF é gerado por código, não à mão:

```bash
python3 gerador.py            # produz apostila.html (A4 print-ready)
# converte para PDF com Chromium headless:
chromium --headless --print-to-pdf=apostila.pdf --no-pdf-header-footer apostila.html
```

- `gerador.py` — define a paleta (validada, colorblind-safe), os builders de SVG
  (fluxo/ciclo/escada/comparação) e o conteúdo de cada capítulo como dados.
- `apostila.html` — saída intermediária (HTML+CSS+SVG inline, sem dependências externas).

Para editar um capítulo, mude os dados em `gerador.py` e rode de novo.

## Trilhas cobertas

- **Parte 1 — Prompt Engineering** (caps. 1–10): baseada em
  [`../curso-prompt-engineering-para-devs`](../curso-prompt-engineering-para-devs/README.md)
- **Parte 2 — Engenharia de IA Aplicada** (caps. 11–19): baseada em
  [`../curso-engenharia-ia-aplicada`](../curso-engenharia-ia-aplicada/README.md)

> Dica de estudo: leia o macete, **feche os olhos e reconstrua a imagem-âncora**.
> Depois tape a cola e tente recitá-la. Revise após 1 dia e 1 semana.
