# Estrutura original do curso (referência fiel)

> Este documento registra, de forma fiel, a **grade real** do curso
> *"Prompt Engineering para Devs"* da Beer and Code, extraída da landing page
> oficial (incluindo o conteúdo dos acordeões recolhíveis). Serve de
> **referência** para o material de estudo deste repositório, que segue esta
> estrutura aula a aula.
>
> O texto bruto extraído está em [`_fonte-extraida-landing.txt`](./_fonte-extraida-landing.txt).
> Direitos do curso original: Beer and Code. Este repositório é material de
> estudo autoral que **espelha a estrutura** para aprendizado próprio.

## Ficha

- **Nome:** Prompt Engineering para Devs — *System Architect v2.0*
- **Professor:** Lucas Souza (Virgu) — AI Engineer, Co-Founder Beer and Code
- **Inclui:** 1 ano de acesso · certificado de conclusão · campo de dúvidas em
  cada aula
- **Proposta:** entender a mecânica real por trás dos LLMs (tokens, atenção,
  contexto, raciocínio) e manter a qualidade de agentes mesmo em sessões longas.

## Posicionamento (o que o curso NÃO é)

- Não é curso de "melhores prompts para o dia a dia".
- Não tem template de prompt para copiar e colar sem entender.
- Não tem lista rasa de "comandos mágicos" ou truques.
- Não ensina a apenas "pedir passivamente" para a IA.
- Objetivo declarado: **formar arquitetos, não apertadores de botões.**

## A jornada em 3 fases (framing do curso)

- **Fase 01 — Instável:** usa IA como ferramenta, arrisca integrações, consome
  tokens testando ideias. Depende da sorte.
- **Fase 02 — Em transição:** entende a base do LLM — o que é um token, como as
  respostas são geradas.
- **Fase 03 — Maestria:** domina o contexto, arquiteta com IA, extrai o melhor
  dos modelos sem "chat aleatório".

## Os 6 mecanismos ("O que você vai dominar")

| # | Mecanismo | Descrição original |
|---|-----------|--------------------|
| 01 | **Arquitetura Cognitiva** | Template: Role, Objective, Constraints, Context, Output spec |
| 02 | **Token Ops & Re-anchoring** | Métodos práticos para evitar o *lost in the middle* |
| 03 | **Playbook de Operação** | Checkpoints e resets sem perda de contexto essencial |
| 04 | **Guardrails & Fallbacks** | Regras anti-contradição e tratamento de dados ausentes |
| 05 | **Pipeline Autor-Revisor** | Qualidade superior sem precisar pedir 10 vezes |
| 06 | **Personas Modulares** | Tech Lead, SRE, Security Reviewer como interfaces de competência |

## As dores que o curso ataca

- **Chat termina um lixo:** o contexto se perde e a alucinação toma conta
  conforme a thread cresce.
- **Refém da verbosidade:** gasto desnecessário de tokens; você escreve
  parágrafos e a IA entrega o básico.
- **Inconsistência de saída:** o prompt funciona uma vez e falha na segunda —
  impossível integrar em produção.

## As 11 aulas (grade oficial)

1. **Aula 1 — O Fim dos "Pedidinhos" (A Verdadeira Arquitetura Cognitiva)**
   O erro conceitual de tratar a IA como mecanismo de busca. Um prompt não é uma
   pergunta, é a **configuração de como o modelo processa a informação**. O
   perigo da *ilusão da plausibilidade* (a IA soa confiante mas entrega genérico
   e quebra o sistema). Criar uma Arquitetura Cognitiva: personas, contextos,
   restrições e objetivos precisos.
2. **Aula 2 — A Caixa Preta da IA (Dominando Tokens, Contexto e Atenção)**
3. **Aula 3 — Engenharia de Estado (Como Salvar um Chat da Degradação)**
4. **Aula 4 — O Motor da Inteligência (Modelos Reativos vs. Raciocínio Profundo)**
5. **Aula 5 — O Código do Raciocínio (Decifrando o Chain of Thought)**
6. **Aula 6 — O Antídoto para a "IA Júnior" (Construindo o Scaffolding)**
7. **Aula 7 — Múltiplas Realidades (Planejamento Não Linear e Tree of Thoughts)**
8. **Aula 8 — Consistência interna e válvulas de escape**
9. **Aula 9 — Auto-refinamento: o modelo como autor e revisor**
10. **Aula 10 — Personas, papéis e especialização situacional**
11. **Aula 11 — Design de saída e controle de verbosidade**

## A filosofia (o que o curso promete que você vai saber)

- Por que o contexto longo degrada a resposta.
- Quando usar Chain of Thought e quando ele atrapalha.
- Como estruturar raciocínio para o modelo não desviar no meio.
- Por que o modelo "perde o fio" — e como evitar.
- Como construir agentes que não quebram, porque você entende o **estado** que
  eles manipulam.

---

**Como este repositório se relaciona com o original:** as
[11 aulas em `../aulas/`](../aulas/) seguem esta grade, título a título, com
teoria, exemplos, exercícios e checklists autorais. A pasta
[`../bonus/`](../bonus/) traz temas complementares (testes/evals, RAG, tool
calling e agentes) que aprofundam a promessa do curso sobre "agentes que não
quebram", mas que **não** fazem parte das 11 aulas oficiais.
