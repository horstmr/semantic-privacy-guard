# Módulo 1 — Fundamentos de IA e LLMs para Programadores

> A base conceitual que o curso de Prompt Engineering assume que você já tem (ou
> não precisa). Aqui a gente abre a caixa: história, o que é ML/DL, e como um
> LLM funciona por dentro.

## 1.1 História e contexto da IA

A ideia de "máquina inteligente" é antiga: autômatos gregos, o Golem, e
formalmente o **Teste de Turing (1950)**, que propôs avaliar inteligência pela
capacidade de uma máquina se passar por humano em conversa. O termo *Artificial
Intelligence* nasce na **Conferência de Dartmouth (1956)**.

| Época | Marco |
|---|---|
| 1950–1970 | IA simbólica (regras, lógica) — sistemas especialistas |
| 1980–1990 | "Invernos da IA" — expectativas frustradas, cortes de investimento |
| 1997 | Deep Blue vence Kasparov no xadrez |
| 2012 | AlexNet — Deep Learning explode com GPUs (ImageNet) |
| 2017 | Paper *"Attention Is All You Need"* — nasce o **Transformer** |
| 2018–2020 | BERT, GPT-2, GPT-3 — escala de parâmetros |
| 2022+ | ChatGPT populariza LLMs; era das ferramentas de IA para devs |

## 1.2 Introdução a LLMs

**Hierarquia:** IA ⊃ Machine Learning ⊃ Deep Learning ⊃ LLMs.

- **IA**: qualquer sistema que simula comportamento inteligente (inclui regras fixas).
- **ML**: sistemas que **aprendem padrões a partir de dados**, sem serem programados explicitamente.
- **DL**: ML com redes neurais profundas (muitas camadas).
- **LLM**: modelo de DL treinado em enormes volumes de texto para prever o próximo token.

**Os 3 pilares de como um LLM funciona:**

1. **Embeddings**: tokens viram vetores numéricos que capturam significado.
   Palavras semelhantes ficam próximas no espaço vetorial ("rei" e "rainha"
   próximos; "rei" e "banana" distantes).
2. **Attention (atenção)**: mecanismo que pesa quais tokens do contexto são
   relevantes para prever o próximo. Resolve ambiguidade ("banco" de sentar vs.
   banco financeiro depende do contexto).
3. **Transformer**: arquitetura que processa toda a sequência em paralelo usando
   *self-attention*, ao contrário das RNNs (token a token). Isso permitiu
   escalar o treino massivamente.

**Exemplos**: ChatGPT (OpenAI), Claude (Anthropic), Gemini (Google), Llama (Meta, open-source).

> **↩︎ Já coberto no Prompt Engineering** — o comportamento prático de *tokens* e
> *atenção* (e o "lost in the middle") está na
> [Aula 2 — A Caixa Preta da IA](../curso-prompt-engineering-para-devs/aulas/aula-02-a-caixa-preta-da-ia.md).
> Aqui é a mecânica interna; lá é como usar isso a seu favor.

## 1.3 Fundamentos de Web Machine Learning

- **Tensor**: estrutura de dados fundamental — generalização de escalar (0D),
  vetor (1D), matriz (2D) para N dimensões. Todo dado (texto, imagem, áudio) vira
  tensor. GPUs são rápidas em operações matriciais sobre tensores.
- **Rede neural**: camadas de neurônios artificiais. Cada neurônio faz
  `saída = ativação(pesos · entradas + bias)`.
- **Como ela aprende**:
  1. *Forward pass* — dado entra, previsão sai;
  2. *Loss* — mede o erro entre previsão e resposta correta;
  3. *Backpropagation* — calcula quanto cada peso contribuiu para o erro;
  4. *Gradient descent* — ajusta os pesos na direção que reduz o erro. Repete milhares de vezes.

## 1.4 Criando sua primeira IA do zero

O ciclo completo de qualquer projeto de ML: **Dados → Treino → Validação → Inferência**

- **Dados**: coletar, limpar e dividir (tipicamente 70–80% treino, 10–15% validação, 10–15% teste).
- **Treino**: o modelo ajusta pesos nos dados de treino.
- **Validação**: mede desempenho em dados que o modelo **nunca viu** — detecta
  *overfitting* (decorar em vez de aprender).
- **Inferência**: usar o modelo treinado em produção para prever dados novos.

Em JavaScript, a biblioteca de referência é o **TensorFlow.js**, que roda no
browser (via WebGL/WebGPU) ou Node.js.

## 1.5 Projetos práticos com ML na Web

- **Vídeo no browser**: detecção de objetos (COCO-SSD), pose (PoseNet/MoveNet),
  face/mãos (MediaPipe) — tudo local, sem enviar dados ao servidor (privacidade +
  latência baixa).
- **Jogos**: bots que aprendem, NPCs adaptativos, efeitos que reagem ao jogador.
- **Vantagem do ML no cliente**: zero custo de servidor de inferência,
  privacidade, tempo real.

## 1.6 Prompt Engineering na prática

> **↩︎ Já coberto no Prompt Engineering** — os princípios (papel, tarefa clara,
> few-shot, formato de saída, decomposição) são **o curso inteiro** de Prompt
> Engineering. Comece pela
> [Aula 1 — O Fim dos "Pedidinhos"](../curso-prompt-engineering-para-devs/aulas/aula-01-o-fim-dos-pedidinhos.md).

O que agrega aqui são os **padrões por intenção do dev** (use como cola):

- **Codar**: forneça stack, versão, trecho de código existente e o comportamento esperado.
- **Debugar**: cole o erro completo + código + o que já tentou.
- **Documentar**: peça doc no padrão do projeto (JSDoc, docstring) com exemplos de uso.
- **Aprender**: peça explicação com analogia + exemplo mínimo executável.

## 1.7 Ferramentas de IA para devs

- **Cursor**: fork do VSCode com IA nativa — chat com contexto do repositório,
  edição multi-arquivo, modo agente.
- **Comparativo**: VSCode (+ Copilot) é o padrão consolidado; Windsurf e Cursor
  competem em "IA nativa" com agentes que editam vários arquivos.
- **`.cursorrules`**: arquivo na raiz do projeto com instruções permanentes para a
  IA (estilo de código, stack, convenções). É um "system prompt do repositório".
- **Vibe Coding**: programar descrevendo intenções em linguagem natural e deixando
  a IA gerar/iterar. Case clássico: **levelsio** (Pieter Levels), microSaaS de
  +100k/mês quase sozinho usando IA intensivamente.

## 1.8 MCPs e automação (visão introdutória)

**MCP (Model Context Protocol)** é um protocolo aberto (criado pela Anthropic) que
padroniza como LLMs se conectam a ferramentas, APIs e fontes de dados. Em vez de
cada app criar integração própria, todos falam a mesma "língua". Usos: gerar
testes, consultar documentação atualizada, navegar em sites e extrair dados.
Ferramentas como **n8n** (com MCP) orquestram automação com IA sem código extenso.

> Aprofundado no [Módulo 3 — MCP](./03-mcp.md).

## 1.9 RAG, embeddings e busca semântica

**RAG (Retrieval-Augmented Generation)**: em vez de esperar que o LLM "saiba
tudo", você **busca** os documentos relevantes numa base própria e os injeta no
prompt. Resolve as duas maiores fraquezas dos LLMs: conhecimento desatualizado e
alucinação sobre dados privados.

**Pipeline básico:**
1. Dividir documentos em pedaços (*chunks*);
2. Gerar **embedding** de cada chunk e salvar num **Vector Database** (Pinecone, pgvector no Postgres, Chroma...);
3. Na pergunta: embedding da pergunta → **similarity search** (proximidade vetorial = busca por *significado*, não texto exato) → top-k chunks;
4. Montar prompt: "Com base nestes trechos: [...], responda: [pergunta]".

Com JavaScript + Postgres, a extensão **pgvector** faz tudo num banco relacional comum.

> **↩︎ Parcialmente coberto** — o *papel* do RAG na engenharia de contexto e a
> saída de escape anti-alucinação estão no
> [Bônus B2](../curso-prompt-engineering-para-devs/bonus/b2-engenharia-de-contexto-rag.md).
> Aqui entram os **vector DBs, embeddings e busca semântica**; as variações
> avançadas (hybrid, agentic) estão no [Módulo 8](./08-arquitetura-de-sistemas-com-ia.md).

## 1.10 Open-source vs. proprietários

| | Proprietários (GPT, Claude, Gemini) | Open-source (Llama, Mistral, Qwen) |
|---|---|---|
| Qualidade de ponta | ✅ Geralmente superior | Alcançando rápido |
| Custo | Pago por token | "Grátis" (você paga a infra) |
| Privacidade | Dados vão à API | Roda 100% local |
| Customização | Limitada | Total (fine-tuning livre) |

- **OpenRouter**: API única que roteia para dezenas de modelos — troque de modelo mudando uma string.
- **Ollama**: roda modelos open-source localmente com um comando (`ollama run llama3`).
- **Agentes de IA**: o LLM decide em etapas — planeja, executa ferramentas, avalia
  e continua até concluir. Detalhado no [Módulo 4](./04-agentes-avancados.md).

---

## ❓ Perguntas para fixar — Módulo 1

**1. Qual a diferença entre IA, ML e DL?**
IA é o campo amplo (qualquer simulação de inteligência, inclusive por regras). ML é o subcampo em que o sistema aprende padrões a partir de dados. DL é ML com redes neurais profundas. LLMs são um caso de DL aplicado a texto.

**2. O que é o mecanismo de attention e por que foi revolucionário?**
Permite pesar a relevância de cada token do contexto ao gerar o próximo. Revolucionou porque viabilizou processar sequências em paralelo (Transformer) e capturar dependências de longo alcance, permitindo a escala dos LLMs.

**3. O que é um embedding?**
Representação vetorial de um texto que captura seu significado. Textos parecidos geram vetores próximos, o que permite busca por significado (similarity search).

**4. Descreva o ciclo dados → treino → validação → inferência.**
Prepara-se os dados; treina-se ajustando pesos para minimizar o erro; valida-se em dados nunca vistos para detectar overfitting; usa-se o modelo em produção (inferência).

**5. O que é overfitting?**
Quando o modelo "decora" o treino em vez de aprender padrões gerais — ótimo no treino, ruim em dados novos. Detecta-se comparando métricas de treino vs. validação.

**6. O que é RAG e que problema resolve?**
Buscar documentos relevantes numa base própria e injetá-los no prompt antes de responder. Resolve conhecimento desatualizado e falta de acesso a dados privados, reduzindo alucinações.

**7. Diferença entre busca semântica e busca por texto exato?**
Exata (keyword) só acha correspondência literal. Semântica compara embeddings e acha o mesmo *significado*, mesmo com palavras diferentes ("carro quebrou" ≈ "veículo com defeito").

**8. Quando usar open-source com Ollama em vez de API proprietária?**
Quando privacidade é crítica, quando se quer custo previsível/zero por token, rodar offline, ou fazer fine-tuning livre. Trade-off: qualidade de ponta e infra por conta própria.

**9. Para que serve o `.cursorrules`?**
Instruções permanentes para a IA do editor (stack, convenções, estilo) — um system prompt fixo do repositório que evita repetir contexto a cada prompt.

**Próximo:** [Módulo 2 — APIs de IA generativa e produto →](./02-apis-generativas-e-produto.md)
