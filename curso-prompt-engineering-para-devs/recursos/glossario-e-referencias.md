# Glossário e referências

## Glossário

Termos do curso, em ordem alfabética, com definição curta e a aula onde
aparecem.

**Agente** — LLM em loop (pensar→agir→observar) com ferramentas, um objetivo e
um critério de parada, capaz de decidir os próprios passos. (Bônus B4)

**Alucinação** — quando o modelo produz informação plausível porém falsa,
tipicamente para preencher uma lacuna do contexto. Combate-se com dado no
contexto e saídas de escape. (Aula 2 · Bônus B2)

**Casos de borda (edge cases)** — entradas atípicas (vazia, tipo errado, dado
ausente, ambígua, maliciosa) que precisam de regras explícitas. (Aula 8)

**Chain-of-thought (CoT) / cadeia de raciocínio** — pedir que o modelo escreva
o raciocínio passo a passo antes da resposta, melhorando tarefas com múltiplos
passos. (Aula 5)

**Chunk / chunking** — pedaço em que um documento é dividido para indexação em
RAG; o tamanho e as fronteiras dos chunks afetam muito a qualidade. (Bônus B2)

**Contexto (janela de)** — o total de tokens que o modelo "enxerga" ao
responder (prompt + histórico + dados + resposta). Recurso escasso. (Bônus B2)

**Context engineering / engenharia de contexto** — a disciplina de decidir o
que colocar (e o que cortar) da janela de contexto. (Bônus B2)

**Contrato (mentalidade de)** — tratar o prompt como especificação com entrada,
saída, bordas, teste e versão — não como conversa. Tese central do curso.
(Aula 1)

**Delimitador** — marcação (tags XML, cercas, aspas) que separa instrução de
dado. Recomendado: tags XML nomeadas. (Aula 1)

**Determinismo / temperature** — `temperature` controla a aleatoriedade da
saída; 0 = mais determinístico e repetível, usado para tarefas que alimentam
código. (Aula 2)

**Eval (avaliação)** — rodar o prompt contra uma bateria de casos e medir o
resultado, para saber se está bom e detectar regressão. (Bônus B1)

**Few-shot / one-shot / zero-shot** — quantos exemplos resolvidos você inclui no
prompt (vários / um / nenhum). (Aula 6)

**Function calling / tool calling** — dar ao modelo funções que ele pode pedir
para chamar; o modelo decide a chamada, seu código executa. (Bônus B3)

**Injeção de prompt (prompt injection)** — quando o dado processado contém
instruções que tentam sequestrar o comportamento do modelo. Defesa: separar dado
de instrução e validar ações. (Aula 1 · Bônus B3)

**JSON mode / structured outputs** — recurso do provedor que força a saída a
obedecer um schema, garantindo formato parseável. (Aula 11)

**LLM-as-judge (LLM como juiz)** — usar um LLM para avaliar saídas segundo uma
rubrica; escala bem mas tem vieses e precisa de calibração. (Bônus B1)

**Lost in the middle (perdido no meio)** — tendência do modelo a prestar menos
atenção à informação no meio de um contexto longo. (Bônus B2)

**Persona / papel (role)** — atribuir um ponto de vista ao modelo; útil quando
muda a resposta (perspectiva/prioridade/público), inútil como elogio. (Aula 8)

**Prompt caching** — reaproveitamento, pelo provedor, do processamento de um
prefixo fixo do prompt entre chamadas, cortando custo e latência. (Bônus B2)

**Prompt chaining (encadeamento)** — quebrar uma tarefa em várias chamadas
encadeadas, com pontos de verificação entre elas. (Aula 5)

**RAG (retrieval-augmented generation)** — buscar os trechos relevantes de uma
base e injetá-los no prompt, dando ao modelo conhecimento que ele não tem.
(Bônus B2)

**Regressão** — quando uma mudança melhora um caso e quebra outro sem que você
perceba; detectada comparando a bateria caso a caso. (Bônus B1)

**Regra positiva vs. negativa** — dizer o que fazer (positiva, guia melhor) vs.
o que não fazer (negativa, guarda-corpo). (Aula 8)

**Saída de escape** — valor explícito para o caso "não sei / não se aplica"
(`null`, "INVALIDO", "não encontrado"), que evita que o modelo invente. (Aula 8 · Aula 11 · Bônus B2)

**Token** — a unidade em que o modelo processa texto (pedaços de palavra). Base
do custo, do limite de contexto e da contagem. (Aula 2)

**Vector store / embedding** — representação numérica de texto (embedding) e o
banco que a armazena, usados para busca por similaridade em RAG. (Bônus B2)

---

## Como continuar aprendendo

O curso te deu a base e o vocabulário. Para aprofundar, procure (nas fontes
oficiais e atualizadas de cada tema):

### Guias de engenharia de prompt dos provedores

Os provedores de LLM mantêm guias de boas práticas, e são a fonte mais confiável
porque refletem o comportamento **atual** dos modelos deles:

- **Guias de prompt engineering** da documentação do modelo que você usa
  (OpenAI, Anthropic/Claude, Google/Gemini, etc.). Procure por "prompt
  engineering guide" + o provedor. Leia especialmente as seções de structured
  outputs, function calling e caching.
- **Guia de structured outputs / JSON mode** do seu provedor — para a Aula 11 na
  prática.
- **Guia de function/tool calling** do seu provedor — para o Bônus B3.

### Recursos abertos e comunitários

- **Learn Prompting** (learnprompting.org) — curso aberto e abrangente de
  prompting, do básico ao avançado.
- **Prompt Engineering Guide** (promptingguide.ai) — compêndio de técnicas com
  referências a papers.
- **DAIR.AI** e newsletters de engenharia de IA — para acompanhar técnicas novas
  (o campo muda rápido).

### Para aprofundar por tema

- **RAG (Bônus B2):** estude embeddings, estratégias de chunking, re-ranking e
  avaliação de recuperação. Frameworks como LangChain e LlamaIndex têm
  documentação didática (mas aprenda o conceito antes da ferramenta).
- **Avaliação/evals (Bônus B1):** procure frameworks de eval e o padrão
  "LLM-as-a-judge". O conceito (bateria + métrica + regressão) importa mais que
  a ferramenta específica.
- **Agentes (Bônus B4):** estude padrões de agente (planejar-executar, reflexão,
  multi-agente) e, sobretudo, **observabilidade** e **limites** — o que faz um
  agente sobreviver à produção.

### O melhor recurso: seu próprio eval

Nenhuma leitura substitui **medir no seu caso**. O campo muda rápido, modelos
mudam de comportamento, e o que funciona para um caso falha em outro. A
habilidade durável que este curso te deu não é uma lista de truques — é o
**método**: escrever como contrato, medir com uma bateria, versionar, iterar.

Isso não sai de moda quando o próximo modelo é lançado.

---

## Uma última nota sobre modelos que mudam

Técnicas específicas envelhecem: um truque que "destrava" o modelo de hoje pode
ser desnecessário no de amanhã (ou já embutido nele). Por isso o curso enfatizou
**princípios** — clareza, contrato, medição — em vez de fórmulas mágicas.

Quando um modelo novo sair, não jogue fora o que aprendeu. Rode sua **bateria de
testes** (Bônus B1) no modelo novo, veja o que mudou, e ajuste. Quem tem eval
adapta-se em uma tarde. Quem só tem "truques" recomeça do zero a cada
lançamento.

Bons prompts. 🍺
