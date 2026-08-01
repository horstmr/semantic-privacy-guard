# Contexto do projeto de portfólio de traduções

**Documento de transferência entre sessões.** Se você é uma sessão do Claude
retomando este trabalho, leia este arquivo primeiro — ele contém o estado, os
métodos já descobertos e o que falta.

Última atualização: 01/08/2026 · Branch: `claude/translation-portfolio-setup-pzpfrs`

---

## Objetivo

Montar um portfólio dos trabalhos de tradução do Martin Horst a partir do e-mail,
agrupado por agência, com uma seção dedicada a **maquinário industrial e setor
elétrico**, e um arquivo separado com **totais de palavras e valores pagos**.

Regra definida pelo usuário: determinar se um dado trabalho foi realizado ou não
**a partir das respostas dele** na correspondência — não presumir.

## Arquivos produzidos

| Arquivo | Conteúdo |
|---|---|
| `PORTFOLIO.md` | Portfólio por agência + seção industrial/elétrica. Escopo 2023–2026 |
| `TOTAIS-PALAVRAS-E-VALORES.md` | Volumes, tarifas, valores pagos, lacunas |
| `ANALISE-PERIODO-COMPLETO.md` | Varredura 2019–2026; corrige os dois acima |

Os dois primeiros trazem aviso de escopo no topo. **O terceiro é o quadro correto.**

---

## Contas de e-mail

| Conta | Papel | Status |
|---|---|---|
| `martinhorsttranslates@gmail.com` | Profissional — fonte principal | Analisada |
| `horst.mr@gmail.com` | Pessoal — recebeu correspondência profissional encaminhada | Parcial |

**Limitação do conector:** o Gmail do Claude aceita **uma conta por vez**. Para
alternar é preciso desconectar e reconectar em Settings → Connectors. Não existe
multi-conta (pedido de recurso: anthropics/claude-code#27567). Ao trocar, sempre
revalidar a conta ativa antes de extrair (busca `in:sent` e conferir o remetente).

**O conector não baixa anexos.** Valores em PDF são inacessíveis por aqui.

---

## Sinais de conclusão por agência (conhecimento mais valioso deste projeto)

Cada agência marca "trabalho feito" de um jeito. Estas queries do Gmail isolam
os trabalhos realizados:

| Agência | Query | Significado |
|---|---|---|
| LanguageWire | `from:noreply-info@languagewire.com subject:"is in progress"` | Realizado. Corpo traz **Work area** e **Word count** |
| LanguageWire | `subject:"Tender closed"` | **Não** ganho |
| Alconost | `from:alpha@alconost.com "is delivered, and will be paid"` | Realizado; snippet traz volume |
| Alconost/Nitro | `from:nitro@alconost.com "sent your payment"` | Pagamento efetuado (US$) |
| TranslateMedia/Toppan | `subject:"Successful Upload"` | Entregue |
| TranslateMedia/Toppan | `subject:"Account crediting in process"` | Entregue **e precificado** (£ por job) |
| TranslateMedia/Toppan | `subject:"Remittance"` | Pagamento efetivamente transferido |
| Flexword | `subject:"Purchase order O-"` | Job atribuído |
| TextMaster | `subject:"aprovada"` | Aprovado pelo cliente final |
| TextMaster | `subject:"words"` | Convite com **contagem no assunto** |

### Truque de classificação sem abrir e-mails

A busca do Gmail varre o corpo. Para agrupar jobs da LanguageWire por setor sem
abrir cada mensagem:

```
from:noreply-info@languagewire.com "is in progress" "Work area: Construction"
```

Funciona com negação para achar o resto:
```
... -"Work area: IT" -"Work area: Marketing" -"Work area: Medical" -"Work area: Construction"
```

### Limitação técnica central

`search_threads` **nunca retorna o corpo** da mensagem — só assunto e snippet.
Contagem de palavras exige `get_message` com `FULL_CONTENT`, um e-mail por vez
(~7 mil tokens cada, HTML pesado). É isso que torna caro completar os volumes.

**Contorno útil:** quando o resultado de uma busca estoura o limite de tokens, a
ferramenta salva em arquivo e informa o caminho. Processar esse arquivo com
`jq`/`python` extrai os dados **sem carregar no contexto**. Vale forçar buscas
grandes (`pageSize: 50`) de propósito por causa disso.

---

## Estado dos dados

- **8 agências**: LanguageWire, Alconost, Flexword, TextUnited, TextMaster,
  TranslateMedia, Toppan Digital Language, MyLanguageConnection
- **Período**: 2º semestre de 2019 → julho de 2026
- **Palavras documentadas**: 125.413 (piso)
- **Valores confirmados**: £ 15.769–15.898 (TranslateMedia/Toppan, 59 remessas
  2020–2025) + US$ 106,78 (Alconost Nitro)
- **Maior trabalho**: Danfoss VLT AutomationDrive FC 360 Programming Guide,
  70.581 palavras, jun/2026
- **Virada de perfil**: marketing de alto giro (2019–2023) → documentação técnica
  industrial (2024–2026)

## Pendências, em ordem de retorno

1. **Créditos por job da Toppan anteriores a ago/2025** — paginar
   `Account crediting in process`. Rende receita e contagem de jobs juntas.
2. **Volumes TextMaster 2019–2026** — parsear a contagem de palavras do assunto
   dos convites; a série é longa.
3. **Contagem dos 58 jobs LanguageWire não industriais** — 58 chamadas de
   `get_message`. Caro; melhor em sessão dedicada.
4. **Valores em PDF** (LanguageWire 27 vouchers, Flexword 7+, Alconost 2+) —
   inacessíveis pelo conector. Baixar manualmente ou usar os portais:
   LanguageWire → perfil → *Payment Information*; Flexword → Plunet;
   TextUnited → fatura mensal na plataforma.

---

## Como unir o contexto de outra conversa

Sessões do Claude são isoladas — nenhuma sessão lê o histórico de outra. Para
consolidar:

1. **Via repositório (recomendado).** Tudo que estiver commitado aqui é visível
   a qualquer sessão futura neste repo. Aponte a outra conversa para
   `portfolio/CONTEXTO.md`.
2. **Via colagem.** Peça um resumo à outra conversa e cole na sessão que vai
   continuar; o conteúdo é incorporado a estes arquivos.
3. **Via `CLAUDE.md` na raiz.** Carregado automaticamente no início de toda
   sessão neste repositório. Ainda não existe aqui — note que este repo é um
   projeto Java (`semantic-privacy-guard`) e um `CLAUDE.md` sobre tradução
   afetaria também as sessões de código.

---

## Observações registradas

- Há um e-mail de 20/03/2025, assunto "abelha", com uma **chave de API da OpenAI
  em texto puro** trafegando entre as duas contas. Deve ser considerada
  comprometida e revogada.
- Este repositório é um projeto Java de segurança; o diretório `portfolio/` não
  tem relação com o código-fonte dele. Se atrapalhar, mover para repo próprio.
