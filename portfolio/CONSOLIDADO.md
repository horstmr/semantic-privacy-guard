# Portfólio Consolidado — as duas contas de e-mail

Documento de união. O levantamento foi feito em duas frentes, em conversas
separadas, uma por conta de e-mail. Aqui elas convergem.

**Status:** conta profissional completa · conta pessoal **parcial** (ver seção 3)

---

## 1. Por que são duas contas

| Conta | Papel | Levantamento |
|---|---|---|
| `martinhorsttranslates@gmail.com` | Profissional, criada para o trabalho de tradução | ✅ Completo (2019–2026) |
| `horst.mr@gmail.com` | Pessoal; recebeu correspondência profissional encaminhada e algumas agências escreveram direto para ela | ⚠️ Parcial |

O conector Gmail do Claude aceita **uma conta por vez**. As duas frentes nunca
puderam ser lidas na mesma sessão — daí o levantamento em paralelo.

Há trânsito comprovado entre as contas: mensagens de agências encaminhadas de
uma para a outra (e2f, Future Trans), o que significa que **um mesmo trabalho
pode aparecer nas duas** e exige deduplicação por identificador de job.

---

## 2. Conta profissional — consolidado

Detalhamento em `ANALISE-PERIODO-COMPLETO.md`, `PORTFOLIO.md` e
`TOTAIS-PALAVRAS-E-VALORES.md`.

| Indicador | Valor |
|---|---|
| Período | 2º semestre de 2019 → julho de 2026 |
| Agências | 8 |
| Palavras documentadas | 125.413 (piso) |
| Valores confirmados | £ 15.769–15.898 + US$ 106,78 |
| Maior trabalho | Danfoss VLT AutomationDrive FC 360 — 70.581 palavras |

**Agências:** LanguageWire · Alconost · Flexword · TextUnited · TextMaster ·
TranslateMedia · Toppan Digital Language · MyLanguageConnection

---

## 3. Conta pessoal — o que já está verificado

⚠️ **Atenção:** o que segue foi verificado diretamente por esta sessão, numa
janela em que o conector ainda apontava para a conta pessoal. **Não é o
levantamento completo** — é o piso confirmado. A varredura sistemática da conta
pessoal, com a metodologia consolidada em `CONTEXTO.md`, ainda não foi feita.

### 3.1 e2f

Agência norte-americana (e2f.com). Contatos: `production@e2f.com`, `it@e2f.com`,
`tdc@e2f.com`, `vendors@e2f.com`.

| Item | Dado |
|---|---|
| Projeto | *Very Important Project — Datadog AI Trial* (20251004-BR-AMPRO) |
| Cliente final | **Datadog** |
| Tipo | AI + Human Post-editing |
| Par | EN-US → PT-BR |
| Tarifa oferecida | **0,02 EUR / palavra-fonte** (Teslime Duygu Çakır, 12/08/2025) |
| Job atribuído | `EN-US ▸ PT-BR, EP, 20251004/#9`, prazo 15/08/2025 |
| Plataformas | Basecamp + Transifex |
| Evidência de aceite | Resposta própria confirmando disponibilidade e pedindo confirmação de honorário |

### 3.2 GameTransLab

Localização de jogos. Contato: `Maxim.Tarasevsky@gametranslab.com`.
Padrão de e-mail traz **palavras e honorário explícitos por job** — a agência
com o dado financeiro mais limpo de todas.

| Projeto | Palavras | Valor | Data |
|---|---:|---:|---|
| Mahjong Magic Islands — Batch #40 | 169 | US$ 8,45 | 18/11/2024 |
| Jetpack Jump | 70 | US$ 3,50 | 26/09/2024 |
| Snake Cube Hunt | 24 | US$ 1,20 | 19/09/2024 |
| **Subtotal verificado** | **263** | **US$ 13,15** | |

Tarifa derivada consistente: **US$ 0,05 / palavra**.

> Os três acima são apenas os que apareceram na amostra inicial. O padrão
> "Words N / Fee X $" é buscável e deve render a série completa.

### 3.3 Future Trans / GoTransparent

Contato: `mennatallah.mahmoud@future-trans.com` (Localization Talent Management
Team Lead). Agosto/2025 — recrutamento para novas linhas
**EN & DE → PT-BR e PT-PT**. Houve manifestação de interesse e apresentação de
perfil (15 anos de experiência, nativo brasileiro). **Sem trabalho registrado**
até o ponto verificado.

### 3.4 Ruído a filtrar

A conta pessoal concentra também a atividade não relacionada a tradução — em
especial os digests diários do **Diário Oficial da Polícia Científica de Santa
Catarina** enviados a `diariooficial@googlegroups.com`. Qualquer varredura deve
excluir esse tráfego.

---

## 4. Como completar a união

Duas rotas. A primeira dá resultado melhor.

### Rota A — reexecutar a varredura na conta pessoal (recomendada)

1. Em claude.ai → Settings → Connectors → Gmail → **Desconectar**
2. **Conectar** novamente, escolhendo `horst.mr@gmail.com`
3. Avisar nesta conversa

A metodologia amadureceu muito desde o levantamento original: as queries por
sinal de conclusão de cada agência e o truque de classificação por corpo sem
abrir e-mails (ambos em `CONTEXTO.md`) não existiam no começo. Reexecutar produz
dados **consistentes e comparáveis** com os da conta profissional, em vez de
duas metodologias diferentes coladas.

### Rota B — colar o resultado da outra conversa

Pedir à outra conversa um resumo do que ela apurou e colar aqui. Mais rápido,
porém herda a metodologia antiga e provavelmente subestima o período, como
ocorreu na primeira passada da conta profissional (que indicava 2023 quando o
histórico real começa em 2019).

### Deduplicação obrigatória

Ao unir, cruzar por identificador de job antes de somar qualquer total. Casos
conhecidos de trânsito entre contas:

- **e2f** — job `20251004/#9` encaminhado da conta pessoal para a profissional
- **Future Trans** — thread de recrutamento encaminhada entre as duas
- **GameTransLab** — Maxim enviava para a conta profissional com **cópia** para a
  pessoal; os mesmos jobs existem nas duas caixas

---

## 5. Totais unificados (provisórios)

Somando apenas o que está documentalmente confirmado nas duas contas:

| Indicador | Profissional | Pessoal (verificado) | Total |
|---|---:|---:|---:|
| Palavras | 125.413 | 263 | **125.676** |
| Valor em £ | 15.769–15.898 | — | **£ 15.769–15.898** |
| Valor em US$ | 106,78 | 13,15 | **US$ 119,93** |
| Agências | 8 | 3 | **11** |

⚠️ O lado pessoal está subamostrado. Estes números **vão crescer** — trate-os
como piso, não como resultado.
