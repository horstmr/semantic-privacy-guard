# Valores TUSS — check-up (SC Saúde)

Objetivo: confirmar, na tabela oficial do SC Saúde, o valor de cada exame do
check-up e a coparticipação de 30%.

## Por que a planilha não está aqui

Os servidores `*.sc.gov.br` (WAF estadual/CIASC) retornam **403 para qualquer
acesso automatizado** — terminal, fetchers e espelhos de arquivo. O download
funciona normalmente em navegador comum.

## Links oficiais (baixar no navegador)

- Página do rol: <https://scsaude.sea.sc.gov.br/rol-de-procedimentos/>
  (baixar o anexo de **rol de procedimentos e serviços auxiliares** vigente —
  é o que contém os exames laboratoriais)
- Últimas versões indexadas pelo Google:
  - Rol de procedimentos (última AN_ROL indexada, vigência 01.12.2022):
    <https://scsaude.sea.sc.gov.br/wp-content/uploads/2023/01/AN_ROL_VIG_01.12.2022.xls>
  - Honorário hospitalar 01.10.2025:
    <https://scsaude.sea.sc.gov.br/wp-content/uploads/2025/10/Tabela_de_Honorarios_01.10.2025.xlsx>
  - Diárias, taxas e gases 01.01.2026:
    <https://scsaude.sea.sc.gov.br/wp-content/uploads/2026/01/AN_11.3_DIARIAS-E-TAXAS_01.01.2026-3.xlsx>
- Programa de check-up do plano (pode alterar a coparticipação desses exames):
  <https://scsaude.sea.sc.gov.br/check-up-saude-anual/>

## Como usar

1. Baixe a planilha do rol no navegador e salve nesta pasta (`tuss/`).
2. Rode:

   ```bash
   pip install pandas openpyxl xlrd   # se necessário
   python3 tuss/extrair_valores.py tuss/<arquivo-baixado>
   ```

3. Saída: tabela com valor de tabela + coparticipação de 30% por exame
   (`tuss/valores-extraidos.csv`).

O extrator procura por **código TUSS e por nome** e sinaliza divergências —
na lista original, `40301630` aparece duplicado (HDL × creatinina), então a
resolução pelo nome da planilha é a que vale. Os códigos da lista estão em
`exames-checkup.csv` com o grau de confiança de cada um.
