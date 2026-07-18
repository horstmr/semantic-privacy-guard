# Módulo 07 — Engenharia de contexto

> Prompt engineering é escrever a instrução. **Context engineering** é decidir
> o que colocar na janela do modelo — e o que deixar de fora. Em sistemas
> reais, é aqui que a maior parte da qualidade se ganha ou se perde.

## Objetivos de aprendizagem

- Entender a **janela de contexto** como um recurso escasso a ser gerido.
- Aplicar **RAG** (retrieval-augmented generation) na prática: buscar o dado
  certo e injetar no prompt.
- Combater o "**perdido no meio**" e a poluição de contexto.
- Usar **prompt caching** para cortar custo e latência.
- Decidir **o que entra e o que fica de fora** da janela.

---

## 1. A janela de contexto é um orçamento

Tudo que o modelo "sabe" no momento de responder está na **janela de contexto**:
seu prompt, o histórico, os documentos que você colou, a resposta que está
gerando. Ela tem um teto (em tokens) e cada token custa dinheiro e latência.

Dois erros opostos:

- **Contexto de menos** → o modelo não tem a informação e **alucina** para
  preencher (Módulo 00). Ex: perguntar sobre a política interna da empresa sem
  colar a política.
- **Contexto de mais** → você enche a janela de coisa irrelevante, o custo
  explode, e o sinal importante se **dilui** no meio do ruído. Mais contexto não
  é melhor contexto.

O trabalho de context engineering é colocar **exatamente o que importa,
o mínimo necessário, na posição certa.**

---

## 2. "Perdido no meio" (lost in the middle)

Modelos tendem a prestar mais atenção ao **começo** e ao **fim** da janela do
que ao **meio**. Se você enterra a informação crucial no parágrafo 40 de 80, o
modelo pode simplesmente "não ver".

Contramedidas:

- Coloque a **instrução** e a **informação mais crítica** no começo e/ou repita
  no fim.
- **Ordene** os documentos recuperados por relevância — os mais relevantes nas
  pontas.
- **Corte** o irrelevante em vez de confiar que o modelo vai filtrar. Ele filtra
  mal.
- Em contexto muito longo, **re-declare a pergunta depois do material**: "Com
  base nos documentos acima, responda: {{pergunta}}".

---

## 3. RAG na prática

**RAG** (geração aumentada por recuperação) é o padrão para dar ao modelo
conhecimento que ele não tem: em vez de colocar *tudo* no prompt, você **busca**
os pedaços relevantes e injeta só eles. É como dar ao modelo as páginas certas
do manual, não o manual inteiro.

Fluxo típico:

```
1. INDEXAÇÃO (uma vez, offline)
   documentos → quebra em pedaços (chunks) → gera embeddings → guarda em
   um vector store

2. CONSULTA (a cada pergunta)
   pergunta do usuário → embedding da pergunta → busca os k chunks mais
   parecidos → injeta esses chunks no prompt → modelo responde usando eles
```

O prompt final fica algo assim:

```
Responda à pergunta usando APENAS os trechos fornecidos em <contexto>. Se a
resposta não estiver nos trechos, diga "não encontrei essa informação nos
documentos". Cite qual trecho usou.

<contexto>
{{chunks_recuperados}}
</contexto>

<pergunta>{{pergunta}}</pergunta>
```

### Os pontos onde RAG dá errado (e o que fazer)

- **Chunking ruim.** Pedaços grandes demais trazem ruído; pequenos demais
  perdem contexto. Chunks que cortam no meio de uma frase/tabela destroem
  sentido. Ajuste tamanho e sobreposição; respeite fronteiras naturais
  (parágrafos, seções).
- **Recuperação errada.** Se a busca traz os chunks errados, o melhor prompt do
  mundo responde com base em lixo. A qualidade do RAG é limitada pela qualidade
  da recuperação — invista nela (melhores embeddings, filtros por metadados,
  re-ranqueamento).
- **Modelo ignora o contexto e "sabe de cor".** Force com a instrução: "use
  APENAS <contexto>". Peça citação do trecho — isso reduz alucinação e te dá
  como auditar.
- **Sem saída de escape.** Sempre inclua "se não estiver nos trechos, diga que
  não encontrou". Sem isso, ele inventa.

> RAG é um mundo em si (embeddings, vector stores, re-ranking, avaliação de
> recuperação). Aqui você aprende o **papel dele na engenharia de contexto**:
> é como você coloca o dado certo na janela. Para ir fundo, veja as referências
> no glossário.

---

## 4. Gerência de contexto em conversas longas

Em chats/agentes que rodam por muitos turnos, o histórico cresce e estoura a
janela. Estratégias:

- **Resumir o histórico antigo.** A cada N turnos, substitua as mensagens
  antigas por um resumo compacto ("o usuário já informou X, Y, Z"). Preserva o
  essencial, libera tokens.
- **Janela deslizante.** Mantenha só os últimos K turnos + um resumo do resto.
- **Memória externa.** Guarde fatos duráveis (preferências, decisões) fora da
  janela, em um store, e recupere só quando relevante (é RAG aplicado ao
  histórico).
- **Estado estruturado.** Em vez de reenviar a conversa inteira, mantenha um
  objeto de estado (`{cliente: ..., pedido: ..., etapa: ...}`) e injete-o.
  Muito mais barato e estável que texto corrido.

---

## 5. Prompt caching: corte custo e latência

Se boa parte do seu prompt é **fixa** (instruções longas, exemplos, um documento
grande de referência) e só o fim muda, o **prompt caching** deixa o provedor
reaproveitar o processamento da parte fixa entre chamadas. Resultado: mais
barato e mais rápido.

Como tirar proveito:

- **Ponha o conteúdo estável no começo** (system prompt, instruções, exemplos,
  documento de referência) e o **variável no fim** (a pergunta do usuário
  daquela chamada). O cache cobre o prefixo comum.
- **Não mude o prefixo à toa.** Qualquer alteração no começo invalida o cache
  dali para frente.
- É um recurso do provedor (nem todos oferecem, regras e TTL variam). Consulte a
  doc do que você usa.

Isso reforça a estrutura que você já aprendeu: **estável primeiro, variável por
último** — bom para atenção (seção 2) *e* para cache.

---

## 6. Anti-padrões de contexto

- **Despejar tudo "por garantia".** Cola o documento de 50 páginas inteiro
  quando 2 parágrafos bastavam. Caro, lento e dilui o sinal.
- **Confiar que o modelo filtra o ruído.** Ele filtra mal; filtre você.
- **Informação crucial no meio** de um contexto enorme.
- **RAG sem saída de escape** → alucinação quando a busca falha.
- **Reenviar a conversa inteira** a cada turno em vez de resumir/estruturar.
- **Variar o prefixo** e perder o cache sem necessidade.

---

## Exercícios

1. **Contexto de menos vs. de mais.** Pergunte ao modelo algo sobre um documento
   seu (a) sem colar nada, (b) colando só a seção relevante, (c) colando o
   documento inteiro. Compare precisão, custo (tokens) e se houve alucinação.

2. **Perdido no meio.** Cole 10 fatos numerados e faça uma pergunta cuja
   resposta está no fato nº 5 (meio). Depois mova esse fato para o começo e
   refaça. O modelo acertou mais numa posição que na outra?

3. **Mini-RAG manual.** Simule RAG sem ferramenta: dado um "banco" de 6
   parágrafos, escolha à mão os 2 mais relevantes para uma pergunta, injete só
   eles com a instrução "use apenas <contexto>". Depois teste uma pergunta cuja
   resposta **não** está nos parágrafos — o modelo usou a saída de escape ou
   inventou?

4. **Resuma o histórico.** Pegue uma conversa longa (10+ turnos) e escreva um
   prompt que a resume em um estado estruturado (`{fatos: [...], pendencias:
   [...]}`). Confirme que, dando só o resumo, o modelo continua a conversa sem
   perder o essencial.

5. **Desenhe para cache.** Reorganize um prompt seu para pôr todo o conteúdo
   fixo no início e só a variável no fim. Isso mudou a estrutura? Ficou também
   mais legível?

Soluções em [`exercicios/solucoes.md`](./exercicios/solucoes.md).

---

## Resumo

- A **janela de contexto é um orçamento**: coloque o mínimo necessário, não o
  máximo possível. Contexto de menos → alucina; de mais → dilui e encarece.
- Combata o "**perdido no meio**": crítico nas pontas, corte o irrelevante,
  re-declare a pergunta após material longo.
- **RAG** injeta o dado certo via busca; sua qualidade depende de **chunking** e
  **recuperação**, e sempre precisa de **saída de escape**.
- Em conversas longas, **resuma / estruture / use memória externa** em vez de
  reenviar tudo.
- **Prompt caching** premia a estrutura "estável primeiro, variável por último".

**Próximo:** [Módulo 08 — Tool calling →](./08-tool-calling.md)
