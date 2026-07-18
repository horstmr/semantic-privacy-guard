# Exercícios

Os exercícios vivem **no fim de cada aula** — é lá que fazem mais sentido, logo
depois da teoria. Este arquivo é só um **índice** e um guia de como tirar
proveito deles.

## Como fazer os exercícios valerem

1. **Tenha um LLM aberto.** Não dá para aprender prompt engineering só lendo,
   assim como não se aprende a programar só assistindo. Rode tudo.
2. **Anote o que você observou**, não só a resposta final. "Rodei 4 vezes e 2
   deram formato diferente" é o aprendizado.
3. **Use `temperature` baixa** quando o exercício fala de consistência/formato, e
   experimente `temperature` alta quando fala de criatividade/variedade.
4. **Compare sempre "antes → depois".** Quase todo exercício pede uma versão ruim
   e uma boa. A diferença *é* a lição.
5. Só depois de tentar, confira as [soluções comentadas](./solucoes.md). Elas não
   são "a resposta certa" (prompt engineering tem muitos caminhos) — são o
   raciocínio esperado e as armadilhas comuns.

## Índice — as 11 aulas

| Aula | Foco dos exercícios |
|------|---------------------|
| [01 — O Fim dos "Pedidinhos"](../aulas/aula-01-o-fim-dos-pedidinhos.md#exercícios) | Pedido→configuração, caçar plausibilidade, testar o R.O.C.C.O., papel que muda tudo |
| [02 — A Caixa Preta da IA](../aulas/aula-02-a-caixa-preta-da-ia.md#exercícios) | Medir tokens, provocar alucinação, sentir a atenção, temperature |
| [03 — Engenharia de Estado](../aulas/aula-03-engenharia-de-estado.md#exercícios) | Provocar degradação, re-ancorar, checkpoint+reset, estado externo |
| [04 — O Motor da Inteligência](../aulas/aula-04-o-motor-da-inteligencia.md#exercícios) | Reativo vs. raciocínio, prompt por motor, classificar tarefas, escalonamento |
| [05 — O Código do Raciocínio](../aulas/aula-05-o-codigo-do-raciocinio.md#exercícios) | Efeito do CoT, separar rascunho, onde atrapalha, decompor |
| [06 — O Antídoto (Scaffolding)](../aulas/aula-06-o-antidoto-scaffolding.md#exercícios) | Júnior→sênior, ensinar borda, viés, cortar excesso |
| [07 — Múltiplas Realidades (ToT)](../aulas/aula-07-multiplas-realidades-tree-of-thoughts.md#exercícios) | CoT vs ToT, forçar diversidade, plano com checkpoint, poda |
| [08 — Consistência e válvulas de escape](../aulas/aula-08-consistencia-e-valvulas-de-escape.md#exercícios) | Fechar 5 bordas, self-consistency, guardrail, injeção |
| [09 — Auto-refinamento](../aulas/aula-09-auto-refinamento-autor-revisor.md#exercícios) | Montar o pipeline, rubrica importa, adversarial, retorno decrescente |
| [10 — Personas modulares](../aulas/aula-10-personas-papeis-especializacao.md#exercícios) | Três interfaces, concretizar enfeite, biblioteca modular, painel+síntese |
| [11 — Design de saída e verbosidade](../aulas/aula-11-design-de-saida-e-verbosidade.md#exercícios) | Texto→contrato, matar preâmbulo, parse defensivo, enum+erro |

## Índice — bônus (trilha complementar)

| Bônus | Foco dos exercícios |
|-------|---------------------|
| [B1 — Testes e versão](../bonus/b1-prompt-como-codigo-testes.md#exercícios) | Criar bateria, eval mínimo, regressão, instabilidade, LLM-juiz |
| [B2 — Contexto e RAG](../bonus/b2-engenharia-de-contexto-rag.md#exercícios) | Contexto de mais/menos, perdido no meio, mini-RAG, cache |
| [B3 — Tool calling](../bonus/b3-tool-calling.md#exercícios) | Definir ferramenta, rodar loop, args hostis, segurança |
| [B4 — Agentes](../bonus/b4-agentes.md#exercícios) | Anatomizar, system prompt, simular loop, escada, contenção |

## Projeto final sugerido

Depois de terminar as aulas (e, idealmente, os bônus), consolide tudo em **um
projeto seu**:

> Escolha uma tarefa real do seu trabalho que hoje você faria "na mão" com um LLM
> (classificar tickets, extrair dados de documentos, gerar rascunhos, revisar
> código...). Construa a solução completa aplicando o curso:
>
> 1. Escreva o prompt como **Arquitetura Cognitiva** (R.O.C.C.O. — Aula 1).
> 2. Adicione **raciocínio** se a tarefa precisar (Aulas 5 e 7) e **scaffolding**
>    (Aula 6).
> 3. Instale **guardrails e válvulas de escape** (Aula 8) e um **pipeline
>    autor-revisor** se a qualidade importar (Aula 9).
> 4. Desenhe a **saída como API**, enxuta e estrita (Aula 11).
> 5. Monte uma **bateria de 20+ casos** e rode um **eval** (Bônus B1);
>    **versione** o prompt.
> 6. Se precisar de dado externo ou ação, esboce **RAG** (B2) ou uma
>    **ferramenta** (B3). Decida honestamente: isso precisa de um **agente** (B4)
>    ou um prompt resolve?
>
> Entregável: o prompt versionado + a bateria de testes + um README curto
> explicando as decisões. Isso é um portfólio de engenharia de prompt.
