# Prompt Engineering para Devs

> Um curso de auto-estudo, gratuito e prático, para desenvolvedores que
> querem parar de "conversar com a IA na sorte" e começar a **tratar prompt
> como código**: com contrato de entrada/saída, testes e versionamento.

Este material foi construído para ser estudado **por conta própria**, no seu
ritmo. Ele copia a estrutura de um curso de referência (os *quatro pilares* +
o caminho para agentes de produção) e expande cada tópico com teoria enxuta,
exemplos "ruim → bom", anti-padrões, exercícios e templates prontos.

---

## A ideia central: prompt é código

Se você é dev, já sabe a diferença entre um script que "funcionou naquela vez"
e um software confiável. A maioria das pessoas usa IA no primeiro modo:
digita algo, torce, copia o resultado. Este curso te leva para o segundo modo.

Um prompt de produção tem:

- **Contrato de entrada** — o que entra, em que formato, com quais garantias.
- **Contrato de saída** — o que sai, em que formato, que outro código vai consumir.
- **Casos de borda fechados** — o que fazer quando a entrada é vazia, ambígua ou maliciosa.
- **Testes** — você consegue provar que ele funciona, e detectar quando quebra.
- **Versão** — você sabe qual versão está em produção e o que mudou.

Se isso soa como engenharia de software, é porque é. Prompt é a nova
superfície de código da sua aplicação.

---

## Para quem é este curso

- Desenvolvedores(as) de qualquer stack que usam LLMs no dia a dia (ChatGPT,
  Claude, Copilot, Cursor) e querem resultados **consistentes**, não sortudos.
- Quem vai **colocar IA dentro de um produto** (chamada de API, feature de
  app, automação) e precisa que aquilo seja confiável.
- Quem já sabe programar. Não ensinamos lógica de programação aqui — ensinamos
  a instruir modelos.

**Pré-requisitos:** saber ler código (exemplos em pseudo-código, Python e
JavaScript), noção do que é uma API/JSON. Não precisa saber nada de ML.

---

## Como estudar

1. Siga os módulos **em ordem** na primeira passada. Eles constroem uns sobre
   os outros.
2. Em cada módulo: leia o conceito, estude o par "ruim → bom", faça os
   **exercícios** no fim. Sem fazer, não gruda.
3. Tenha um LLM aberto do lado (ChatGPT, Claude, Gemini, ou a API que você
   usa) e **teste cada exemplo você mesmo**. Prompt engineering é uma
   habilidade empírica: você aprende medindo o que sai.
4. Use a `templates/biblioteca-de-prompts.md` como cola no trabalho real.

**Tempo estimado:** ~12–16 horas de estudo ativo (leitura + exercícios),
distribuídas como você quiser. Dá para fazer um módulo por dia em duas semanas.

---

## Trilha do curso

### Fundamentos

| # | Módulo | O que você sai sabendo |
|---|--------|------------------------|
| 00 | [Introdução: prompt é código](./00-introducao-prompt-e-codigo.md) | Como um LLM "lê" seu prompt; mentalidade de contrato; por que determinismo importa |
| 01 | [Pilar 1 — Estrutura](./01-pilar-estrutura.md) | Separar instrução de dado; delimitadores; anatomia de um prompt |
| 02 | [Pilar 2 — Instrução](./02-pilar-instrucao.md) | Escrever instrução como contrato; casos de borda; regras negativas; persona |
| 03 | [Pilar 3 — Exemplos (few-shot)](./03-pilar-exemplos-few-shot.md) | Zero/one/few-shot; exemplos canônicos; quando usar e quando evita |
| 04 | [Pilar 4 — Formato de saída](./04-pilar-formato-de-saida.md) | Saída como API; JSON confiável; schema; structured outputs |

### Técnica

| # | Módulo | O que você sai sabendo |
|---|--------|------------------------|
| 05 | [Raciocínio (reasoning)](./05-raciocinio-reasoning.md) | Chain-of-thought; decomposição; quando o modelo deve "pensar" |
| 06 | [Prompt como código: testes e versão](./06-prompt-como-codigo-testes.md) | Evals; casos de teste; versionamento; detectar regressão |

### Produção

| # | Módulo | O que você sai sabendo |
|---|--------|------------------------|
| 07 | [Engenharia de contexto](./07-engenharia-de-contexto.md) | Janela de contexto; RAG na prática; caching; o que colocar e o que cortar |
| 08 | [Tool calling / function calling](./08-tool-calling.md) | Dar ferramentas ao modelo; definir contratos de função; segurança |
| 09 | [Agentes](./09-agentes.md) | Loop de agente; memória; agentes que sobrevivem à produção |

### Apoio

- [Exercícios (todos os módulos)](./exercicios/README.md) · [Soluções comentadas](./exercicios/solucoes.md)
- [Biblioteca de prompts / templates](./templates/biblioteca-de-prompts.md)
- [Glossário e referências](./recursos/glossario-e-referencias.md)

---

## Os quatro pilares (resumo de bolso)

Todo bom prompt para tarefa séria tem estes quatro elementos. Guarde este
quadro; ele é o coração do curso.

1. **Estrutura** — a instrução está claramente separada dos dados. O modelo
   nunca confunde "o que fazer" com "sobre o quê".
2. **Instrução** — escrita como contrato, sem ambiguidade, com os casos de
   borda fechados. Se dá para interpretar de dois jeitos, um deles vai sair.
3. **Exemplos** — um ou dois exemplos canônicos valem mais que três parágrafos
   descrevendo o comportamento. Mostre, não só descreva.
4. **Formato de saída** — explícito e tratado como uma API que outro código
   vai consumir. Nada de "responda de forma organizada".

E o mantra por trás de tudo: **prompt é código.** Ele tem contrato, tem teste,
tem versão.

---

## Licença e origem

Material educacional autoral, escrito para estudo próprio. A **estrutura**
(quatro pilares → contexto → tools → agentes) é inspirada em abordagens
públicas de ensino de engenharia de prompt para desenvolvedores; todo o texto,
exemplos e exercícios aqui são originais. Use, adapte e compartilhe à vontade.
