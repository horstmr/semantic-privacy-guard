# Prompt Engineering para Devs — *System Architect*

> Curso de auto-estudo, gratuito e prático, que **espelha a estrutura** do curso
> *"Prompt Engineering para Devs"* da Beer and Code (Prof. Lucas Souza / Virgu)
> — as mesmas **11 aulas** e os mesmos **6 mecanismos**, reconstruídos como
> material aberto para você aprender por conta.

Este não é um curso de "melhores prompts para o dia a dia". É sobre entender a
**mecânica real dentro do modelo** — tokens, atenção, contexto, estado,
raciocínio — para você **arquitetar** com IA, e não depender da sorte. O lema:
**formar arquitetos, não apertadores de botões.**

> **Transparência.** A **estrutura** (as 11 aulas, os títulos, os 6 mecanismos,
> o framing) vem da grade oficial do curso da Beer and Code, registrada
> fielmente em [`recursos/estrutura-original-do-curso.md`](./recursos/estrutura-original-do-curso.md).
> Todo o **texto, exemplos e exercícios** deste repositório são autorais,
> escritos para estudo próprio. Se você quer o curso original (vídeos,
> certificado, campo de dúvidas, comunidade), apoie o trabalho deles:
> a Beer and Code cobra R$99 com 1 ano de acesso.

---

## As 3 fases da jornada

O curso te move por três estágios. Saber em qual você está ajuda a calibrar o
estudo:

- **Fase 01 — Instável.** Você usa IA como ferramenta, arrisca prompts, gasta
  tokens testando. Funciona às vezes. Depende da sorte.
- **Fase 02 — Em transição.** Você entende a base: o que é um token, como a
  resposta é gerada, por que o contexto degrada.
- **Fase 03 — Maestria.** Você domina o contexto e o estado, arquiteta com IA,
  e extrai o melhor dos modelos de forma consistente.

Este material te leva da Fase 01 à Fase 03.

---

## Os 6 mecanismos que você vai dominar

Estes são os "instrumentos" do curso — cada aula desenvolve um ou mais deles:

1. **Arquitetura Cognitiva** — o template R.O.C.C.O.: *Role, Objective,
   Constraints, Context, Output spec.*
2. **Token Ops & Re-anchoring** — operar tokens e re-ancorar contexto para
   evitar o *lost in the middle*.
3. **Playbook de Operação** — checkpoints e resets sem perder o contexto
   essencial de uma sessão longa.
4. **Guardrails & Fallbacks** — regras anti-contradição e tratamento de dados
   ausentes.
5. **Pipeline Autor-Revisor** — o modelo revisando o próprio trabalho, para
   qualidade superior sem pedir dez vezes.
6. **Personas Modulares** — Tech Lead, SRE, Security Reviewer como *interfaces
   de competência* que você pluga conforme a tarefa.

---

## As 11 aulas

Estude **em ordem** — cada aula constrói sobre a anterior.

### Fundamentos: a mecânica

| # | Aula | O que você domina |
|---|------|-------------------|
| 01 | [O Fim dos "Pedidinhos"](./aulas/aula-01-o-fim-dos-pedidinhos.md) | Prompt como **configuração**, não pergunta; ilusão da plausibilidade; template R.O.C.C.O. |
| 02 | [A Caixa Preta da IA](./aulas/aula-02-a-caixa-preta-da-ia.md) | Tokens, contexto e **atenção**: como o modelo realmente lê você |
| 03 | [Engenharia de Estado](./aulas/aula-03-engenharia-de-estado.md) | Por que o chat degrada; re-anchoring; checkpoints e resets |

### O motor: raciocínio

| # | Aula | O que você domina |
|---|------|-------------------|
| 04 | [O Motor da Inteligência](./aulas/aula-04-o-motor-da-inteligencia.md) | Modelos reativos vs. de raciocínio profundo; qual usar quando |
| 05 | [O Código do Raciocínio](./aulas/aula-05-o-codigo-do-raciocinio.md) | Chain of Thought: quando destrava, quando atrapalha |
| 06 | [O Antídoto para a "IA Júnior"](./aulas/aula-06-o-antidoto-scaffolding.md) | Scaffolding: andaimes que elevam o nível da resposta |
| 07 | [Múltiplas Realidades](./aulas/aula-07-multiplas-realidades-tree-of-thoughts.md) | Planejamento não linear e Tree of Thoughts |

### Confiabilidade: produção

| # | Aula | O que você domina |
|---|------|-------------------|
| 08 | [Consistência interna e válvulas de escape](./aulas/aula-08-consistencia-e-valvulas-de-escape.md) | Self-consistency; guardrails; fallbacks para dados ausentes |
| 09 | [Auto-refinamento: autor e revisor](./aulas/aula-09-auto-refinamento-autor-revisor.md) | Pipeline autor-revisor; reflexão; crítica automática |
| 10 | [Personas, papéis e especialização](./aulas/aula-10-personas-papeis-especializacao.md) | Personas modulares como interfaces de competência |
| 11 | [Design de saída e controle de verbosidade](./aulas/aula-11-design-de-saida-e-verbosidade.md) | Saída como API; cortar verbosidade; formato estrito |

---

## Trilha complementar (bônus)

Temas **além das 11 aulas oficiais**, que aprofundam a promessa do curso sobre
"agentes que não quebram". Estude depois de terminar as aulas.

| # | Bônus | Assunto |
|---|-------|---------|
| B1 | [Prompt como código: testes e versão](./bonus/b1-prompt-como-codigo-testes.md) | Evals, casos de teste, versionamento, regressão |
| B2 | [Engenharia de contexto e RAG](./bonus/b2-engenharia-de-contexto-rag.md) | Janela de contexto, RAG na prática, caching |
| B3 | [Tool calling](./bonus/b3-tool-calling.md) | Dar ferramentas ao modelo; contratos de função; segurança |
| B4 | [Agentes](./bonus/b4-agentes.md) | Loop de agente; estado; agentes que sobrevivem à produção |

---

## Apoio

- [Exercícios (índice)](./exercicios/README.md) · [Soluções comentadas](./exercicios/solucoes.md)
- [Biblioteca de prompts / templates](./templates/biblioteca-de-prompts.md)
- [Glossário e referências](./recursos/glossario-e-referencias.md)
- [Estrutura original do curso (referência)](./recursos/estrutura-original-do-curso.md)

## Quer ir além de prompt engineering?

Há uma **trilha complementar** neste repositório —
[Engenharia de Software em IA Aplicada](../curso-engenharia-ia-aplicada/README.md)
— que cobre o que fica *em volta* de escrever bons prompts: fundamentos de IA/ML,
MCP, agentes avançados, e IA aplicada a UX/UI, DevOps, gestão de projetos,
arquitetura, fine-tuning, governança e carreira. Ela **não repete** os assuntos
deste curso — aponta de volta para cá quando o tema já foi coberto.

---

## Como estudar

1. **Ordem importa.** Faça as 11 aulas em sequência; depois os bônus.
2. **Tenha um LLM aberto** (ChatGPT, Claude, Gemini ou a API que você usa) e
   **teste cada exemplo**. Prompt engineering é habilidade empírica — você
   aprende medindo o que sai.
3. Em cada aula: leia o conceito → estude o par **ruim → bom** → faça os
   **exercícios** do fim. Sem fazer, não gruda.
4. Use `temperature = 0` (ou próximo) quando o assunto é consistência/formato.
5. Use a [biblioteca de prompts](./templates/biblioteca-de-prompts.md) como cola
   no trabalho real.

**Tempo estimado:** ~14–18 horas de estudo ativo (11 aulas + bônus + exercícios).

**Pré-requisitos:** saber ler código (exemplos em pseudo-código, Python e JS) e
ter noção de API/JSON. Não precisa saber nada de ML.

---

## A filosofia, em uma frase

> Quando você entende a mecânica profunda por baixo dos panos, tudo muda: você
> sabe **por que** o contexto degrada, **quando** o Chain of Thought ajuda ou
> atrapalha, e **como** construir agentes que não quebram — porque você entende
> o estado que eles manipulam.

Ou, no espírito do curso: **se você não consegue explicar o resultado, você não
fez engenharia — teve sorte.**
