# Aula 2 — A Caixa Preta da IA
## Dominando Tokens, Contexto e Atenção

> Ninguém te explicou o que acontece lá dentro quando você aperta Enter. E é
> exatamente isso que separa quem depende da sorte de quem extrai o modelo.

## Objetivos

- Abrir a caixa preta o suficiente para tomar boas decisões: **tokens**,
  **janela de contexto** e **atenção**.
- Entender por que o modelo **inventa** (alucina) e por que ele **desvia**.
- Usar o parâmetro **temperature** com intenção.

---

## 1. Tokens: o modelo não vê palavras

O LLM não lê texto — lê **tokens**, pedaços de palavra. "ordenação" pode virar
`orden` + `ação`. Três consequências práticas:

- **Custo e limite.** APIs cobram por token e têm um teto (a janela de
  contexto). Texto longo custa mais e pode estourar o limite.
- **Contagem.** Em português, conte ~1,3 token por palavra (pior que o inglês,
  por causa de acentos e palavras longas). "Encher linguiça" tem preço real.
- **Formatação conta.** Cada quebra de linha, `#` ou aspas é token. Formatar bem
  vale a pena; encher, não.

## 2. Previsão do próximo token

No fundo, o modelo faz uma coisa só: dado tudo que veio antes, prever o
**próximo token mais provável** — de novo e de novo. Ele não consulta um banco
de fatos nem "executa" sua ordem como uma CPU. Ele **continua o texto** de forma
estatisticamente plausível.

Daí saem os dois comportamentos que mais te atrapalham:

- **Alucinação.** Se a informação não está no contexto (nem foi bem aprendida no
  treino), ele **inventa algo plausível** para continuar — não existe "não sei"
  espontâneo. A cura não é pedir "não invente"; é *fornecer o dado* (Aula 3 e
  bônus de contexto) ou dar uma **válvula de escape** (Aula 8).
- **Desvio.** Como cada token depende dos anteriores, um começo ruim puxa uma
  continuação ruim. Por isso raciocínio no início melhora a conclusão no fim
  (Aula 5), e por isso o modelo "perde o fio" quando o contexto fica poluído
  (Aula 3).

## 3. Atenção: começo e fim pesam mais que o meio

O mecanismo de **atenção** é como o modelo decide, a cada token, quais partes do
contexto olhar. Na prática, ele tende a atender melhor ao **começo** e ao **fim**
da janela do que ao **meio** — o fenômeno *lost in the middle*. Se você enterra a
informação crucial no parágrafo 40 de 80, o modelo pode simplesmente não "vê-la".

Isso tem implicações diretas que você vai explorar na próxima aula:

- Instrução e informação crítica → **começo** e/ou repetidas no **fim**.
- Formato de saída → **perto do fim** (última coisa lida antes de responder).
- Contexto irrelevante → **cortado**, não empurrado para o meio na esperança de
  que o modelo filtre. Ele filtra mal.

## 4. Temperature: o botão do acaso

A maioria das APIs expõe `temperature`. Grosso modo:

- **`0` (ou ~0,02, "estrito")** → o modelo quase sempre pega o token mais
  provável. Saída **determinística e repetível**. Use para código, extração,
  classificação — tudo que alimenta outro sistema.
- **Alta** → mais variedade e "criatividade", menos previsível. Use para
  brainstorming e geração de opções.

> **Regra prática:** se a saída vai para outro código, comece em `temperature`
> baixíssima. Você quer engenharia, não loteria. (Mesmo em 0 pode haver leve
> variação entre chamadas — não conte com bit-a-bit idêntico.)

---

## 5. Por que isto importa para a arquitetura

Cada decisão dos próximos capítulos deriva desta mecânica:

| Fenômeno da caixa preta | Consequência na arquitetura |
|-------------------------|-----------------------------|
| Próximo token / plausibilidade | Feche bordas e dê válvulas de escape (Aula 8) |
| Atenção começo/fim | Posicione instrução e formato nas pontas (Aula 3) |
| Janela finita | Gerencie estado, re-ancore, resete (Aula 3) |
| Determinismo via temperature | Baixe a temperatura para consistência |
| Dependência do começo | Faça o modelo raciocinar antes de concluir (Aula 5) |

Entender a caixa preta não é curiosidade acadêmica — é o que te deixa **prever** o
comportamento do modelo em vez de reagir a ele.

---

## Exercícios

1. **Meça tokens.** Cole um parágrafo seu (~80 palavras) num tokenizer do
   provedor que você usa. Divida tokens por palavras. Anote sua razão em PT.
   Repita com um trecho de código — a razão muda?

2. **Provoque a alucinação.** Pergunte algo específico e verificável sobre um
   nicho obscuro (ex: detalhe de uma lib pouco conhecida). Ele inventou com
   confiança? Agora cole a doc real e pergunte de novo. O que mudou?

3. **Sinta a atenção.** Cole 10 fatos numerados e faça uma pergunta cuja
   resposta está no fato nº 5 (meio). Depois mova esse fato para o topo e
   refaça. O modelo acertou mais em alguma posição?

4. **Temperature.** Rode o mesmo prompt criativo em temperature baixa e alta, 3
   vezes cada. Depois um prompt de extração de JSON nas duas. Onde a variação
   ajuda e onde atrapalha?

Soluções em [`../exercicios/solucoes.md`](../exercicios/solucoes.md).

---

## Resumo

- O modelo lê **tokens**, prevê o **próximo token** e continua texto plausível —
  não consulta fatos nem executa ordens.
- Isso causa **alucinação** (inventa para continuar) e **desvio** (o começo puxa
  o resto).
- A **atenção** favorece começo e fim; o meio se perde (*lost in the middle*).
- **Temperature** baixa = determinismo, para tudo que alimenta código.
- Toda a arquitetura das próximas aulas deriva desta mecânica.

**Próximo:** [Aula 3 — Engenharia de Estado →](./aula-03-engenharia-de-estado.md)
