# Aula 5 — O Código do Raciocínio
## Decifrando o Chain of Thought

> Deixar o modelo "pensar antes de responder" muda o jogo em problemas com
> passos. Mas Chain of Thought no lugar errado só gasta tokens e polui a saída.
> Esta aula ensina **quando** ele destrava e **quando** atrapalha.

## Objetivos

- Entender **por que** o Chain of Thought (CoT) funciona, à luz da caixa preta.
- Aplicar CoT e **decomposição** em problemas com múltiplos passos.
- Separar o **raciocínio** (rascunho) da **resposta final** que o código
  consome.
- Saber quando **não** usar CoT.

---

## 1. Por que "pensar passo a passo" funciona

Lembre da Aula 2: cada token depende dos anteriores. Se você força o modelo a
**pular direto para a resposta**, ele precisa acertar de uma vez, sem espaço para
calcular. Se você o deixa **escrever o raciocínio primeiro**, cada passo
intermediário vira contexto que melhora o passo seguinte — como fazer a conta no
papel em vez de de cabeça.

**Reativo, direto (erra em problemas com passos):**

```
Um produto custa R$ 80. Tem 15% de desconto e depois 10% de imposto sobre o
valor já com desconto. Preço final? Só o número.
```

Aqui o modelo pode chutar um número plausível e errar a ordem das operações.

**Com CoT (raciocínio antes):**

```
Um produto custa R$ 80. Tem 15% de desconto e depois 10% de imposto sobre o
valor já com desconto. Calcule o preço final.
Pense passo a passo: primeiro o desconto, depois o imposto sobre o valor com
desconto. Mostre cada conta e só então dê o resultado.
```

Agora ele computa `80 × 0,85 = 68`, depois `68 × 1,10 = 74,80`, e acerta —
porque cada passo ficou explícito no contexto.

Vale para: matemática, lógica, análise de código, decisões multicritério —
qualquer tarefa em que a resposta depende de uma cadeia de deduções.

---

## 2. Decomposição: quebre o problema

Para tarefas grandes, em vez de um prompt gigante pedindo tudo, **guie o modelo
por etapas**:

```
Vou te dar um relato de bug. Faça, nesta ordem:
1. Resuma o comportamento observado em 1 frase.
2. Liste as causas prováveis, da mais provável para a menos.
3. Para a mais provável, descreva como confirmá-la.
4. Só então proponha a correção.
Não pule etapas. <bug>{{relato}}</bug>
```

Você guiou a **atenção** do modelo e lhe deu um método, não um desejo. Quando a
tarefa é pesada, dá para ir além e **encadear chamadas** (prompt chaining) — a
saída de uma etapa alimenta a próxima — ganhando pontos de verificação a cada
passo (aprofundado no bônus B1 e na Aula 9).

---

## 3. Separe o rascunho da resposta final

Problema prático: se o modelo "pensa em voz alta", esse raciocínio **vem junto**
na saída — e seu parser não quer isso. Duas soluções.

**(a) Campo separado no JSON:**

```
Responda em JSON:
{
  "raciocinio": string,  // seu passo a passo aqui
  "resposta": string     // apenas a conclusão final
}
```

Seu código lê `.resposta` e ignora (ou loga) `.raciocinio`. Bônus: o
`raciocinio` é ouro para depurar **por que** o modelo decidiu o que decidiu.

**(b) Delimitadores:**

```
Pense dentro de <rascunho>...</rascunho>. Depois dê a resposta final em
<final>...</final>. Só o conteúdo de <final> será usado.
```

> **Cuidado com a ordem.** O raciocínio precisa vir **antes** da resposta para
> ajudar. Pedir "dê a resposta e depois explique" perde o benefício — a
> conclusão já foi comprometida antes de o raciocínio existir.

---

## 4. Quando o CoT atrapalha

CoT não é sempre a resposta. Cuidado quando:

- **A tarefa é trivial.** Pedir "pense passo a passo" para classificar spam só
  gasta tokens e latência, sem ganhar acurácia.
- **O modelo é de raciocínio profundo** (Aula 4). Ele já pensa internamente;
  CoT explícito é redundante e pode atrapalhar a cadeia dele.
- **Você precisa de saída limpa e curta.** O raciocínio pode vazar para o
  usuário final. Se usar CoT, **separe** (seção 3).

> **Regra prática:** CoT é ferramenta para **dificuldade com passos**, não
> enfeite universal. Use em modelo reativo diante de problema complexo; dispense
> em tarefa trivial ou em modelo que já raciocina.

---

## 5. Anti-padrões

- **Raciocínio onde não precisa.** CoT em classificação binária.
- **Resposta antes do raciocínio.** Perde todo o benefício.
- **Raciocínio vazando na saída de produção.** Três parágrafos de "vamos
  analisar..." indo para o parser ou para o usuário.
- **CoT explícito em modelo de raciocínio.** Redundante e contraproducente.
- **Confiar no raciocínio como prova.** O passo a passo é plausível, não
  garantido — valide a *resposta*, não a narrativa.

---

## Exercícios

1. **Meça o efeito do CoT.** Escolha 5 problemas com passos (contas percentuais,
   lógica, "quem é mais velho se..."). Rode cada um em dois prompts: resposta
   direta vs. "mostre o passo a passo antes". Conte acertos. Qual a diferença?

2. **Separe rascunho de resposta.** Refaça um prompt de raciocínio em JSON com
   `{ "raciocinio", "resposta" }`. Confirme que seu código usa só `.resposta`.
   Leia alguns `.raciocinio` — ajudaram a entender erros?

3. **Onde atrapalha.** Force "pense passo a passo" numa classificação trivial e
   meça tokens/latência a mais versus a versão direta. A qualidade melhorou? (Não
   deve.)

4. **Decomponha.** Pegue uma tarefa complexa sua e quebre em 3–4 etapas
   explícitas num só prompt. Comparada ao prompt "faça tudo", a saída ficou mais
   correta e mais fácil de auditar?

Soluções em [`../exercicios/solucoes.md`](../exercicios/solucoes.md).

---

## Resumo

- CoT funciona porque cada **passo intermediário vira contexto** que melhora o
  seguinte — o modelo "calcula no papel".
- **Decomponha** tarefas grandes em etapas (ou em chamadas encadeadas).
- **Separe raciocínio da resposta** (campo JSON ou delimitador), sempre com
  **raciocínio antes**.
- CoT é para **dificuldade com passos**; dispense em tarefa trivial e em modelo
  de raciocínio profundo.

**Próximo:** [Aula 6 — O Antídoto para a "IA Júnior" →](./aula-06-o-antidoto-scaffolding.md)
