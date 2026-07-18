# Módulo 05 — Raciocínio (reasoning)

> Para problemas com passos, deixar o modelo "pensar antes de responder" muda o
> jogo. Mas raciocínio no lugar errado só gasta tokens e polui sua saída.

## Objetivos de aprendizagem

- Entender **por que** pedir raciocínio (chain-of-thought) melhora tarefas com
  múltiplos passos.
- Aplicar **decomposição** de problemas complexos.
- Separar o **raciocínio** (rascunho) da **resposta final** (o que seu código
  consome).
- Saber quando **não** usar raciocínio, e como isso muda com **modelos de
  raciocínio** dedicados.

---

## 1. Por que "pense passo a passo" funciona

Lembre do Módulo 00: cada token depende dos anteriores. Se você força o modelo
a **jogar direto para a resposta**, ele precisa acertar de uma vez, sem
"espaço" para calcular. Se você o deixa **escrever o raciocínio primeiro**, cada
passo intermediário vira contexto que melhora o passo seguinte — como fazer uma
conta no papel em vez de de cabeça.

**Ruim (resposta direta, erra em problemas com passos):**

```
Um produto custa R$ 80. Tem 15% de desconto e depois 10% de imposto sobre o
valor com desconto. Qual o preço final? Responda só o número.
```

O modelo pode chutar um número plausível e errar a ordem das operações.

**Bom (raciocínio antes):**

```
Um produto custa R$ 80. Tem 15% de desconto e depois 10% de imposto sobre o
valor já com desconto. Calcule o preço final.

Pense passo a passo: primeiro o desconto, depois o imposto sobre o valor com
desconto. Mostre cada conta e só então dê o resultado.
```

Agora ele calcula `80 × 0,85 = 68`, depois `68 × 1,10 = 74,80`, e chega certo —
porque cada passo ficou explícito no contexto.

Isso vale para: matemática, lógica, análise de código, decisões com muitos
critérios, qualquer coisa em que a resposta dependa de uma cadeia de deduções.

---

## 2. Decomposição: quebre o problema

Para tarefas grandes, em vez de um prompt gigante que pede tudo, **guie o
modelo por etapas** — ou até use várias chamadas encadeadas (*prompt chaining*).

**Guiando por etapas em um só prompt:**

```
Vou te dar um relato de bug. Faça, nesta ordem:

1. Resuma o comportamento observado em 1 frase.
2. Liste as causas prováveis, da mais provável para a menos.
3. Para a causa mais provável, descreva como confirmá-la.
4. Só então proponha a correção.

Não pule etapas. <bug>{{relato}}</bug>
```

**Encadeando (prompt chaining) — melhor quando cada etapa é pesada:**

```
Chamada 1: extrai fatos estruturados do relato → JSON
Chamada 2: recebe o JSON, gera hipóteses → lista
Chamada 3: recebe a hipótese escolhida, gera a correção → diff
```

Encadear te dá pontos de controle: você valida a saída de cada etapa, pode
trocar de modelo por etapa, e depura qual passo falhou. Custa mais chamadas e
latência — vale quando a tarefa é complexa e a confiabilidade importa.

---

## 3. Separe o rascunho da resposta final

Problema prático: se o modelo "pensa em voz alta", esse raciocínio **vem junto**
na saída — e seu parser não quer isso. Duas soluções:

**(a) Peça o raciocínio em um campo separado:**

```
Responda em JSON:
{
  "raciocinio": string,  // seu passo a passo aqui
  "resposta": string     // apenas a conclusão final
}
```

Seu código lê `.resposta` e ignora (ou loga) `.raciocinio`. Bônus: o campo
`raciocinio` é ouro para depurar por que o modelo decidiu o que decidiu.

**(b) Use delimitadores e extraia:**

```
Pense dentro de <rascunho>...</rascunho>. Depois dê a resposta final dentro de
<final>...</final>. Só o conteúdo de <final> será usado.
```

> **Cuidado:** ordem importa. O raciocínio precisa vir **antes** da resposta
> para ajudar — pedir "dê a resposta e depois explique" perde o benefício,
> porque a resposta já foi comprometida antes do raciocínio existir.

---

## 4. Modelos de raciocínio mudam a receita

Existe uma geração de **modelos de raciocínio** (reasoning models) que fazem a
cadeia de pensamento **internamente**, antes de responder, sem você pedir.
Com eles, a prática muda:

- **Não precisa (e às vezes não deve) pedir "pense passo a passo"** — eles já
  fazem, e instruir explicitamente pode atrapalhar. Você dá o problema e o
  critério de sucesso; eles cuidam do "como pensar".
- **Menos few-shot.** Como vimos no Módulo 03, exemplos demais podem interferir
  na cadeia deles. Prefira instrução clara e poucos exemplos.
- **Você paga pelo pensamento.** Esse raciocínio interno consome tokens (às
  vezes muitos). Ótimo para problemas difíceis; exagero para uma classificação
  trivial.

Com **modelos padrão** (não-raciocínio), o "pense passo a passo" e a
decomposição explícita continuam sendo suas ferramentas principais.

> **Regra prática:** tarefa difícil e com passos → modelo de raciocínio *ou*
> chain-of-thought explícito. Tarefa simples e de alto volume → modelo rápido,
> resposta direta, sem raciocínio (mais barato, mais rápido).

---

## 5. Anti-padrões de raciocínio

- **Raciocínio onde não precisa.** Pedir "pense passo a passo" para "classifique
  como spam/não-spam" só gasta tokens e latência.
- **Resposta antes do raciocínio.** Perde todo o benefício. Raciocínio vem
  primeiro.
- **Raciocínio vazando na saída de produção.** O usuário final (ou seu parser)
  recebe três parágrafos de "vamos analisar..." Separe em campo/delimitador.
- **Pedir CoT explícito para um modelo de raciocínio.** Redundante e às vezes
  contraproducente.
- **Confiar no raciocínio como prova.** O passo a passo é plausível, não
  garantido. Um modelo pode "raciocinar" lindamente e ainda concluir errado —
  valide a *resposta*, não confie na narrativa.

---

## Exercícios

1. **Meça o efeito do CoT.** Escolha 5 problemas com passos (contas
   percentuais, lógica, "quem é mais velho se..."). Rode cada um em dois
   prompts: resposta direta vs. "mostre o passo a passo antes". Conte acertos.
   Qual a diferença?

2. **Separe rascunho de resposta.** Pegue um prompt de raciocínio e refaça-o em
   JSON com `{ "raciocinio": ..., "resposta": ... }`. Confirme que seu código
   consegue usar só `.resposta`. Leia alguns `.raciocinio` — eles te ajudam a
   entender erros?

3. **Decomponha.** Pegue uma tarefa complexa sua (ex: "gere um plano de testes
   a partir desta especificação") e quebre em 3 chamadas encadeadas. Compare
   qualidade e depurabilidade contra fazer tudo em um prompt só.

4. **Onde raciocínio atrapalha.** Force "pense passo a passo" numa tarefa
   trivial de classificação e meça tokens/latência a mais versus a versão
   direta. Confirme que a qualidade **não** melhorou — só o custo.

Soluções em [`exercicios/solucoes.md`](./exercicios/solucoes.md).

---

## Resumo

- Para tarefas **com passos**, deixar o modelo **raciocinar antes** melhora a
  resposta — cada passo intermediário vira contexto útil.
- **Decomponha** problemas grandes por etapas ou por **chaining** (mais controle,
  mais custo).
- **Separe o raciocínio da resposta final** (campo JSON ou delimitador) para não
  poluir o que o código consome — e sempre com **raciocínio antes**.
- **Modelos de raciocínio** já pensam sozinhos: menos CoT explícito, menos
  few-shot, mais custo. Case a ferramenta com a dificuldade da tarefa.

**Próximo:** [Módulo 06 — Prompt como código: testes e versão →](./06-prompt-como-codigo-testes.md)
