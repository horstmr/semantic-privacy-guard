# Aula 7 — Múltiplas Realidades
## Planejamento Não Linear e Tree of Thoughts

> Chain of Thought segue **uma** linha de raciocínio. Mas os problemas difíceis
> têm bifurcações — e a primeira linha nem sempre é a melhor. Esta aula abre
> **várias realidades** de raciocínio e escolhe a vencedora.

## Objetivos

- Entender o limite do raciocínio **linear** (uma cadeia só).
- Aplicar **Tree of Thoughts (ToT)**: ramificar, avaliar e podar caminhos.
- Usar **planejamento não linear** para problemas de decisão e design.

---

## 1. O limite da linha única

O Chain of Thought da Aula 5 é poderoso, mas **linear**: o modelo segue um
caminho de raciocínio do início ao fim. Se ele erra o primeiro passo, todo o
resto herda o erro (o acúmulo da Aula 3) — e ele não volta atrás. Para problemas
com **uma** resposta correta e passos claros, isso basta.

Mas há problemas onde:
- existem **vários caminhos plausíveis** e não se sabe de antemão qual é o bom;
- a qualidade da resposta depende de **comparar alternativas**, não de seguir uma;
- um passo em falso no começo compromete tudo, então vale **explorar antes de
  comprometer**.

Design de arquitetura, estratégias de solução, quebra-cabeças, decisões com
trade-offs — todos pedem mais que uma linha.

---

## 2. Tree of Thoughts: ramificar, avaliar, podar

**Tree of Thoughts (ToT)** trata o raciocínio como uma **árvore**, não uma reta:
o modelo gera **múltiplos caminhos** (ramos), **avalia** cada um, **poda** os
ruins e aprofunda os promissores. Em vez de "pense passo a passo", é "pense em
várias possibilidades, julgue-as, e siga a melhor".

O ciclo:

```
        ┌── caminho A ──▶ avalia ──▶ (promissor) ──▶ aprofunda
objetivo┼── caminho B ──▶ avalia ──▶ (fraco) ─────▶ poda
        └── caminho C ──▶ avalia ──▶ (promissor) ──▶ aprofunda ──▶ melhor resposta
```

Você pode orquestrar isso **num único prompt** (mais simples) ou em **etapas
encadeadas** (mais controle, cada ramo em sua chamada).

### Versão em um prompt

```
Problema: {{problema}}

1. Gere 3 abordagens DIFERENTES para resolver, cada uma com uma estratégia
   distinta. Rotule A, B, C.
2. Para cada abordagem, liste prós, contras e principais riscos.
3. Avalie e dê nota de 0 a 10 para cada, justificando.
4. Escolha a de maior nota e desenvolva a solução completa só dela.
5. Se duas empatarem, explicite o critério de desempate.
```

### Versão encadeada (para problemas pesados)

```
Chamada 1: gerar N abordagens candidatas → lista
Chamada 2: para cada candidata, avaliar contra critérios → notas
Chamada 3: pegar a vencedora e desenvolvê-la a fundo → solução
```

Encadear te dá pontos de verificação: você inspeciona os ramos antes de gastar
tokens desenvolvendo o vencedor.

---

## 3. Planejamento não linear

Além de ramificar soluções, ToT habilita **planejar antes de executar** e
**revisar o plano**. Em vez de o modelo sair fazendo (e herdar o primeiro erro),
ele:

1. **Explora** o espaço do problema (quais são as partes, as incógnitas).
2. **Esboça** caminhos alternativos de solução.
3. **Avalia** qual caminho ataca melhor o objetivo.
4. **Só então executa** o escolhido — e pode voltar se travar.

```
Antes de resolver, PLANEJE:
- Liste as sub-partes do problema.
- Para as 2 partes mais críticas, proponha 2 abordagens cada.
- Escolha a combinação mais promissora e justifique.
Só depois de eu aprovar o plano, execute.  <!-- ponto de controle humano -->
```

O "só depois de eu aprovar" é um **checkpoint humano** barato que evita o modelo
desenvolver o caminho errado por 2000 tokens. É a ponte para os agentes (bônus
B4), onde planejar-antes-de-executar é um padrão central.

---

## 4. Quando usar (e quando é exagero)

ToT é mais caro que CoT (gera e avalia vários caminhos). Use com critério:

| Tarefa | Técnica |
|--------|---------|
| Resposta única, passos claros (uma conta, uma extração) | **CoT linear** (Aula 5) |
| Várias soluções possíveis, escolher a melhor | **Tree of Thoughts** |
| Decisão de design/arquitetura com trade-offs | **ToT + planejamento** |
| Tarefa trivial | **Nenhum** — resposta direta |

> **Regra prática:** ToT paga o custo extra quando o **espaço de soluções é
> largo** e escolher errado é caro. Para o resto, CoT linear já resolve — não
> transforme "classifique este e-mail" numa árvore de decisões.

---

## 5. Anti-padrões

- **ToT para tudo.** Ramificar uma tarefa de resposta única é queimar tokens.
- **Gerar ramos sem avaliar.** Três abordagens e nenhum critério de escolha =
  três respostas soltas, não uma decisão.
- **Não podar.** Desenvolver todos os caminhos a fundo em vez de só o vencedor.
- **Pular o ponto de controle.** Deixar o modelo executar o plano inteiro sem
  você inspecionar os ramos — perde a vantagem do não linear.
- **Ramos que não são diferentes.** Três variações da mesma ideia não exploram
  nada; force estratégias distintas.

---

## Exercícios

1. **CoT vs ToT.** Pegue um problema com várias soluções possíveis (ex:
   "como estruturar o cache deste serviço?"). Resolva com CoT linear e com ToT
   (3 abordagens → avalia → escolhe). A resposta do ToT foi melhor? Valeu o
   custo extra?

2. **Force a diversidade.** Rode um prompt ToT e verifique se as 3 abordagens são
   realmente distintas. Se forem variações da mesma, reescreva exigindo
   estratégias diferentes (ex: "uma simples, uma robusta, uma barata").

3. **Plano com checkpoint.** Para uma tarefa de design, peça o **plano** primeiro
   (sub-partes + abordagens + escolha justificada) e só aprove/execute depois.
   Comparado a mandar fazer direto, você pegou algum caminho ruim antes de gastar
   tokens?

4. **Poda.** Num prompt ToT, confirme que o modelo desenvolve **só** o vencedor.
   Se ele desenvolve todos, ajuste a instrução para podar.

Soluções em [`../exercicios/solucoes.md`](../exercicios/solucoes.md).

---

## Resumo

- CoT é **linear**; problemas com múltiplos caminhos pedem raciocínio em
  **árvore**.
- **Tree of Thoughts:** gerar vários caminhos → **avaliar** → **podar** os
  fracos → aprofundar o melhor. Em um prompt ou encadeado.
- **Planejamento não linear:** explorar e escolher o caminho **antes** de
  executar, com **checkpoints** — ponte para agentes.
- ToT paga o custo extra quando o **espaço de soluções é largo**; para resposta
  única, CoT linear basta.

**Próximo:** [Aula 8 — Consistência interna e válvulas de escape →](./aula-08-consistencia-e-valvulas-de-escape.md)
