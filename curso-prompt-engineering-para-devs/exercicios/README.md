# Exercícios

Os exercícios vivem **no fim de cada módulo** — é lá que eles fazem mais
sentido, logo depois da teoria. Este arquivo é só um **índice** e um guia de
como tirar proveito deles.

## Como fazer os exercícios valerem

1. **Tenha um LLM aberto.** Não dá para aprender prompt engineering só lendo,
   assim como não dá para aprender a programar só assistindo. Rode tudo.
2. **Anote o que você observou**, não só a resposta final. "Rodei 4 vezes e 2
   deram formato diferente" é o aprendizado, não a resposta em si.
3. **Fixe `temperature = 0`** quando o exercício fala de consistência/formato, e
   experimente `temperature` alta quando fala de criatividade/variedade.
4. **Compare sempre "antes → depois".** Quase todo exercício pede que você rode
   uma versão ruim e uma boa. A diferença *é* a lição.
5. Só depois de tentar, confira as [soluções comentadas](./solucoes.md). Elas
   não são "a resposta certa" (prompt engineering tem muitos caminhos) — são o
   raciocínio esperado e as armadilhas comuns.

## Índice por módulo

| Módulo | Foco dos exercícios |
|--------|---------------------|
| [00 — Introdução](../00-introducao-prompt-e-codigo.md#exercícios) | Tokens, instabilidade, alucinação, conversa→contrato |
| [01 — Estrutura](../01-pilar-estrutura.md#exercícios) | Anti-injeção, anatomia, cortar excesso, delimitadores |
| [02 — Instrução](../02-pilar-instrucao.md#exercícios) | Caçar lacunas, fechar bordas, positiva×negativa, persona |
| [03 — Exemplos](../03-pilar-exemplos-few-shot.md#exercícios) | Zero→few, ensinar borda, viés, cortar excesso |
| [04 — Formato de saída](../04-pilar-formato-de-saida.md#exercícios) | Texto→JSON, parse defensivo, enum fechado, erro no formato |
| [05 — Raciocínio](../05-raciocinio-reasoning.md#exercícios) | Efeito do CoT, separar rascunho, decompor, onde atrapalha |
| [06 — Testes e versão](../06-prompt-como-codigo-testes.md#exercícios) | Bateria, eval mínimo, regressão, instabilidade, LLM-juiz |
| [07 — Contexto](../07-engenharia-de-contexto.md#exercícios) | Contexto de mais/menos, perdido no meio, mini-RAG, cache |
| [08 — Tool calling](../08-tool-calling.md#exercícios) | Definir ferramenta, rodar loop, args hostis, segurança |
| [09 — Agentes](../09-agentes.md#exercícios) | Anatomizar, system prompt, simular loop, escada, contenção |

## Projeto final sugerido

Depois de terminar os módulos, consolide tudo em **um projeto seu**:

> Escolha uma tarefa real do seu trabalho que hoje você faria "na mão" com um
> LLM (classificar tickets, extrair dados de documentos, gerar rascunhos,
> revisar código...). Construa a solução completa aplicando o curso:
>
> 1. Escreva o prompt com os **quatro pilares** (Mód. 01–04).
> 2. Adicione **raciocínio** se a tarefa precisar (Mód. 05).
> 3. Monte uma **bateria de 20+ casos** e rode um **eval** (Mód. 06).
> 4. **Versione** o prompt com changelog.
> 5. Se precisar de dado externo, esboce a **engenharia de contexto / RAG**
>    (Mód. 07) ou uma **ferramenta** (Mód. 08).
> 6. Decida honestamente: isso precisa de um **agente** ou um prompt resolve?
>    (Mód. 09)
>
> Entregável: o prompt versionado + a bateria de testes + um README curto
> explicando as decisões. Isso é um portfólio de engenharia de prompt.
