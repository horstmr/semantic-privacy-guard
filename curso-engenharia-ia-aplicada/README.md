# Engenharia de Software em IA Aplicada — trilha complementar

> Material de estudo que **complementa** o
> [curso de Prompt Engineering para Devs](../curso-prompt-engineering-para-devs/README.md)
> com tudo que fica **em volta** de escrever bons prompts: os fundamentos de
> IA/ML, MCP, agentes avançados, e a IA aplicada a UX/UI, DevOps, gestão de
> projetos, arquitetura, fine-tuning, governança e carreira.

Cada módulo traz os conceitos essenciais de forma direta, seguidos de
**perguntas para fixar** (responda sem olhar; revise as que errar).

---

## Como esta trilha se relaciona com o curso de Prompt Engineering

O curso de Prompt Engineering ensina **a fundo como instruir modelos** (os
quatro pilares, raciocínio, contexto, tools, agentes). Esta trilha **não repete**
esses assuntos — quando um tópico já foi coberto lá, você verá um callout assim:

> **↩︎ Já coberto no Prompt Engineering** — este assunto está detalhado em
> [Aula X / Bônus BY]. Aqui fica só o que agrega.

Assim você estuda os dois sem redundância. Comece pelo curso de Prompt
Engineering se ainda não fez; use esta trilha para abrir o leque para
**engenharia de IA aplicada** de ponta a ponta.

**O que foi de-duplicado** (vive no outro curso, aqui vira ponteiro):
princípios de prompt · consistência/anti-alucinação/válvulas de escape ·
function calling · loop básico de agente e reflexão · RAG básico ·
evals/LLM-juiz · injeção de prompt (básico).

---

## Trilha (12 módulos)

| # | Módulo | Foco | Novo vs. Prompt Eng. |
|---|--------|------|----------------------|
| 01 | [Fundamentos de IA e LLMs](./01-fundamentos-ia-e-llms.md) | História, IA/ML/DL, embeddings/attention/transformer, redes neurais, ML na web, ferramentas p/ devs | 🆕 quase tudo |
| 02 | [APIs de IA generativa e produto](./02-apis-generativas-e-produto.md) | Mercado, provedores, custo/caching, integração no back-end, multimodal, OCR | 🆕 (prompt avançado → ponteiro) |
| 03 | [MCP (Model Context Protocol)](./03-mcp.md) | O "USB-C da IA": primitivas, MCP vs tools, JS SDK, segurança, casos | 🆕 tudo |
| 04 | [Agentes avançados](./04-agentes-avancados.md) | ReAct/Plan-Execute, memória, context pruning/stitching, LangGraph, multiagente | 🆕 (básico → ponteiro B3/B4) |
| 05 | [IA para UX & UI](./05-ia-para-ux-ui.md) | Text-to-UI, prototipação, agentes de código, testes E2E via MCP | 🆕 tudo |
| 06 | [IA para DevOps](./06-ia-para-devops.md) | IaC copilot, K8s, troubleshooting, AIOps, ChatOps, CI/CD, FinOps, auto-remediação | 🆕 tudo |
| 07 | [IA para Gestão de Projetos](./07-ia-para-gestao-de-projetos.md) | Requisitos, RICE/WSJF/MoSCoW, Monte Carlo, riscos, reuniões, OKRs | 🆕 tudo |
| 08 | [Arquitetura de sistemas com IA](./08-arquitetura-de-sistemas-com-ia.md) | AI-first, single/multi-agent, variações de RAG, routing, HITL, enterprise | 🆕 (parte de agentes → ponteiro) |
| 09 | [Fine-tuning de modelos](./09-fine-tuning.md) | Quando fine-tunar, datasets, API, LoRA/PEFT, avaliação | 🆕 (evals → ponteiro B1) |
| 10 | [Segurança e Governança](./10-seguranca-e-governanca.md) | Explicabilidade, vieses, injeção/jailbreak, LGPD/GDPR/EU AI Act, custos | 🆕 (injeção básica → ponteiro Aula 8) |
| 11 | [Projeto integrador (Capstone)](./11-projeto-integrador-capstone.md) | Micro-SaaS com IA de ponta a ponta | 🆕 tudo |
| 12 | [Carreira e entrevistas](./12-carreira-e-entrevistas.md) | Portfólio, níveis, entrevistas, negociação | 🆕 tudo |
| — | [Revisão geral (20 perguntas-síntese)](./revisao-geral.md) | Auto-teste do curso inteiro | — |

---

## Como estudar

1. Leia o módulo, **tente responder as "perguntas para fixar" sem olhar**, revise
   as que errar.
2. Use **repetição espaçada**: revisite as perguntas após 1 dia e após 1 semana —
   consolida muito melhor a retenção.
3. Nos callouts ↩︎, pule para o curso de Prompt Engineering para o assunto a fundo.
4. Feche com a [revisão geral](./revisao-geral.md).

---

## Origem e escopo

Esta trilha foi montada a partir de um material de estudo de um curso de
**Engenharia de Software em IA Aplicada** (12 módulos, JavaScript-first). O
conteúdo foi organizado no repositório e **de-duplicado** contra o curso de
Prompt Engineering, conforme pedido, para você aprender os dois sem repetição.
