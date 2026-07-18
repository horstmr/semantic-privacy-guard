# Módulo 00 — Introdução: prompt é código

> "Se você não consegue explicar o resultado, você não fez engenharia de
> prompt. Você teve sorte."

## Objetivos de aprendizagem

Ao final deste módulo você será capaz de:

- Explicar, em alto nível, **como um LLM transforma seu prompt em resposta**
  (tokens, contexto, probabilidade) — o suficiente para tomar boas decisões.
- Entender por que a mesma pergunta pode dar respostas diferentes, e o que
  você controla nisso.
- Adotar a **mentalidade de contrato**: prompt não é conversa, é especificação.
- Reconhecer os quatro sinais de que um prompt está pronto para produção.

---

## 1. O que acontece quando você manda um prompt

Você não precisa de um doutorado em ML, mas precisa de um modelo mental
correto. Sem ele, você vai brigar com o LLM sem entender por que ele "não
obedece".

### Tokens, não palavras

O modelo não vê texto — vê **tokens**, que são pedaços de palavras. "programação"
pode virar `program` + `ação`. Isso importa por três motivos práticos:

- **Custo e limite**: APIs cobram por token e têm um teto (a *janela de
  contexto*). Textos longos custam mais e podem estourar o limite.
- **Contagem aproximada**: em inglês, ~1 token ≈ 4 caracteres ≈ ¾ de palavra.
  Em português, um pouco pior (mais tokens por palavra). Regra de bolso: conte
  ~1,3 token por palavra em PT.
- **Formatação conta**: cada quebra de linha, cada `#`, cada aspa é token. Isso
  não te impede de formatar bem — só significa que "encher linguiça" tem preço.

### Previsão de próximo token

Um LLM faz, no fundo, uma coisa só: dado tudo que veio antes, prever o
**próximo token mais provável**, de novo e de novo. Ele não "consulta um banco
de fatos" nem "executa sua instrução" como uma CPU. Ele continua o texto de um
jeito estatisticamente plausível.

Consequências diretas para você:

- **Ele não sabe o que não está no texto.** Se a informação não está no prompt
  (nem no treino), ele **inventa algo plausível** — a famosa alucinação. A cura
  não é pedir "não invente"; é *fornecer* o dado ou dar uma saída de escape
  (ver Módulo 02).
- **O começo influencia o resto.** Como cada token depende dos anteriores, um
  bom começo de resposta puxa uma boa continuação. É por isso que "pensar passo
  a passo" (Módulo 05) funciona: o raciocínio no início melhora a conclusão no
  fim.
- **Ambiguidade vira aleatoriedade.** Se seu prompt permite duas leituras, o
  modelo vai escolher uma pela probabilidade. Em outra chamada, pode escolher a
  outra. Você percebe isso como "instabilidade" — mas a causa é o seu prompt.

### Temperatura: o botão do acaso

A maioria das APIs tem um parâmetro `temperature` (0 a ~1-2). Grosso modo:

- `temperature = 0` → o modelo quase sempre escolhe o token mais provável.
  Saída mais **determinística e repetível**. Use para extração, classificação,
  código, qualquer coisa que precise ser confiável.
- `temperature` alta → mais variedade e "criatividade", menos previsível. Use
  para brainstorming, geração de opções, texto criativo.

> **Regra prática:** se o resultado alimenta outro código, comece em
> `temperature = 0`. Você quer engenharia, não loteria. (Mesmo em 0 pode haver
> pequena variação entre chamadas — não conte com bit-a-bit idêntico.)

---

## 2. A mudança de mentalidade: conversa → contrato

A pessoa comum trata o LLM como um chat: pergunta, reage, refina, pergunta de
novo. Funciona para uso pessoal. **Não funciona quando o prompt roda 10 mil
vezes por dia dentro do seu produto**, com entradas que você nunca viu.

O dev trata o prompt como **contrato / especificação**:

| Mentalidade de conversa | Mentalidade de contrato (dev) |
|--------------------------|-------------------------------|
| "Vou refinando até sair bom" | "Especifico antes; funciona de primeira" |
| Entrada é o que eu digitei agora | Entrada é *qualquer* coisa que o usuário mandar |
| Saída eu leio e interpreto | Saída é consumida por outro código, sem humano no meio |
| Erro? Eu corrijo na hora | Erro? O sistema tem que se virar sozinho |
| Guardo na cabeça | Guardo em arquivo, versionado, testado |

Essa é a tese do curso inteiro: **prompt é código**. E código de produção tem
contrato, teste e versão.

### O prompt de uma linha vs. o prompt de produção

Veja a mesma tarefa nos dois mundos.

**Uso casual (ok para você, péssimo para produção):**

```
resuma esse email pra mim: <cola o email>
```

**Prompt de produção (mesma tarefa, tratada como código):**

```
Você é um assistente que resume e-mails corporativos.

<tarefa>
Resuma o e-mail delimitado em <email> em no máximo 3 frases, em português,
focando em: (1) o que estão pedindo, (2) o prazo, (3) quem precisa agir.
</tarefa>

<regras>
- Se não houver prazo no e-mail, escreva "prazo: não informado".
- Se o e-mail estiver vazio ou não for um e-mail, responda exatamente: "SEM_CONTEUDO".
- Não invente informação que não está no texto.
</regras>

<email>
{{corpo_do_email}}
</email>

Responda apenas com o resumo, sem preâmbulo.
```

O segundo é maior — e é justamente isso que o torna confiável. Ele fecha as
brechas por onde a ambiguidade (e a alucinação) escapa. Nos próximos módulos
você vai aprender a construir cada parte dele.

---

## 3. Os quatro sinais de um prompt pronto

Antes de mandar um prompt para produção, confira se ele tem os quatro pilares.
Este é o checklist que amarra o curso:

1. **Estrutura** — a instrução está separada do dado? (Módulo 01)
2. **Instrução** — está escrita como contrato, sem ambiguidade, com casos de
   borda fechados? (Módulo 02)
3. **Exemplos** — há exemplos canônicos mostrando o comportamento esperado?
   (Módulo 03)
4. **Formato de saída** — a saída é explícita e parseável por código? (Módulo 04)

Se algum está faltando, você tem um rascunho, não um contrato.

---

## 4. Anti-padrões que começam aqui

- **"Ser educado ajuda o modelo."** Não. "Por favor" e "obrigado" não melhoram
  a qualidade técnica — clareza melhora. (Custa alguns tokens; use se quiser,
  mas não confunda gentileza com engenharia.)
- **"Quanto mais eu peço, melhor fica."** Instrução longa e vaga é pior que
  instrução curta e precisa. Volume não é clareza.
- **"Vou dizer o que NÃO fazer e pronto."** Regras negativas ajudam, mas o
  modelo segue muito melhor quando você diz **o que fazer** e dá um **exemplo**.
- **"Se deu certo uma vez, está pronto."** Uma execução não é um teste.
  Você vai aprender a avaliar de verdade no Módulo 06.

---

## Exercícios

> Faça no seu LLM favorito. Guarde suas respostas; você vai revisitar.

1. **Meça tokens.** Pegue um parágrafo seu (~80 palavras) e cole em um contador
   de tokens (procure "tokenizer" do provedor que você usa). Quantos tokens
   deu? Divida por número de palavras. Anote sua razão token/palavra em PT.

2. **Provoque a instabilidade.** Escreva um prompt propositalmente ambíguo, ex:
   *"me dê um exemplo de função"*. Rode 4 vezes. Descreva as diferenças. Agora
   reescreva fechando a ambiguidade (linguagem, o que a função faz, formato) e
   rode 4 vezes. Compare a variação.

3. **Provoque uma alucinação.** Pergunte algo específico e verificável sobre um
   assunto de nicho que o modelo provavelmente não domina (ex: detalhes de uma
   lib obscura). Ele inventou? Agora cole a documentação real no prompt e
   pergunte de novo. O que mudou?

4. **Conversa → contrato.** Pegue um prompt casual que você usa no dia a dia e
   reescreva-o no formato de contrato (tarefa, regras, casos de borda, formato
   de saída). Não precisa estar perfeito — só sinta a diferença.

Soluções e comentários em [`exercicios/solucoes.md`](./exercicios/solucoes.md).

---

## Resumo

- LLM prevê o **próximo token** com base no contexto; ele não executa nem
  consulta fatos — continua texto plausível.
- O que **você controla**: o texto do prompt e a `temperature`. Ambiguidade no
  prompt vira aleatoriedade na saída.
- Para tarefas que alimentam código, use `temperature = 0`.
- Troque a mentalidade de **conversa** pela de **contrato**.
- Um prompt de produção tem os **quatro pilares**: estrutura, instrução,
  exemplos, formato de saída — e é tratado como **código** (contrato, teste,
  versão).

**Próximo:** [Módulo 01 — Pilar 1: Estrutura →](./01-pilar-estrutura.md)
