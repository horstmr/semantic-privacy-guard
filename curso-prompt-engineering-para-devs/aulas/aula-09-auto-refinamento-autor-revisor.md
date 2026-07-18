# Aula 9 — Auto-refinamento
## O modelo como autor e revisor

> "Qualidade superior sem precisar pedir 10 vezes." Em vez de você corrigir a
> resposta na base da tentativa e erro, faça o **modelo revisar o próprio
> trabalho**. Este é o mecanismo **Pipeline Autor-Revisor**.

## Objetivos

- Montar o **pipeline autor-revisor**: gerar, criticar, corrigir.
- Escrever uma **rubrica de revisão** que dá ao revisor critérios objetivos.
- Saber quando o auto-refinamento ajuda e quando vira loop inútil.

---

## 1. Por que separar autor de revisor

Quando você pede uma coisa só ("escreva e já entregue perfeito"), o modelo faz as
duas tarefas — criar e avaliar — ao mesmo tempo, e mal. Escrever e revisar exigem
**posturas diferentes**: o autor gera; o revisor duvida. Separar os dois papéis
em passos distintos deixa cada um render melhor — a mesma razão pela qual, em
times, quem escreve o código não é o melhor juiz único do próprio PR.

O pipeline básico tem três passos:

```
1. AUTOR   → gera um primeiro rascunho
2. REVISOR → critica o rascunho contra uma rubrica (acha problemas)
3. AUTOR   → reescreve corrigindo as críticas
```

Você troca "pedir 10 vezes e torcer" por um **processo** que melhora a saída de
forma dirigida.

---

## 2. O pipeline na prática

### Em passos encadeados (mais controle)

```
# Passo 1 — Autor
"Escreva {{artefato}} seguindo {{requisitos}}."

# Passo 2 — Revisor (nova chamada, com o rascunho como entrada)
"Você é um revisor crítico. Avalie o rascunho em <rascunho> contra a rubrica.
Liste TODOS os problemas encontrados, específicos e acionáveis. Se não houver,
diga 'APROVADO'.
<rubrica>{{critérios}}</rubrica>
<rascunho>{{saída do passo 1}}</rascunho>"

# Passo 3 — Autor de novo (com rascunho + críticas)
"Reescreva o artefato corrigindo cada problema listado em <criticas>. Não
introduza novos problemas.
<criticas>{{saída do passo 2}}</criticas>
<rascunho>{{saída do passo 1}}</rascunho>"
```

### Em um único prompt (mais simples)

```
Tarefa: {{tarefa}}.
1. Escreva um primeiro rascunho.
2. Assuma o papel de um revisor crítico e liste os defeitos do seu rascunho
   contra estes critérios: {{rubrica}}.
3. Reescreva o resultado final corrigindo os defeitos que você mesmo apontou.
Entregue apenas o resultado final (passo 3).
```

Encadear dá pontos de inspeção (você lê as críticas); um prompt só é mais barato.
Escolha pelo valor da tarefa.

---

## 3. A rubrica: sem ela, o revisor é decorativo

Um revisor sem critérios só produz elogios vagos ("ficou bom!"). A **rubrica** dá
ao revisor o que procurar — e é o que faz o refinamento morder:

```
<rubrica>
Reprove o rascunho se qualquer item falhar:
- [ ] Trata o caso de entrada vazia?
- [ ] Todos os caminhos de erro retornam um valor definido?
- [ ] O código tem tipagem estrita e nenhuma variável não usada?
- [ ] A resposta responde à pergunta SEM preâmbulo genérico?
- [ ] Nenhuma afirmação sem suporte no contexto?
</rubrica>
```

Note que a rubrica é, muitas vezes, a lista de **guardrails** da Aula 8 e os
**critérios de qualidade** da Aula 6 — reaproveitados agora como checklist de
revisão. É o mesmo padrão fechando o ciclo.

---

## 4. Reflexão e crítica adversarial

Duas variações potentes:

- **Reflexão.** Antes de finalizar, o modelo se pergunta: *"isto realmente
  atende ao objetivo? o que eu esqueci? onde um cético atacaria?"* — e corrige.
  Barato e pega erros óbvios.
- **Crítica adversarial.** O revisor é instruído a **tentar derrubar** o
  rascunho, não a validá-lo: *"aja como um revisor hostil cujo trabalho é achar
  a falha que quebra isto em produção."* Postura adversarial acha o que a
  simpática não acha.

```
Aja como um revisor adversarial. Seu objetivo é encontrar o cenário de entrada
que quebra este código. Descreva o input exato e o que acontece. Só depois,
proponha a correção.
```

---

## 5. Quando parar (não vire loop infinito)

Auto-refinamento tem retorno decrescente e pode degenerar em loop (o modelo
"melhorando" para sempre, ou reintroduzindo problemas). Contenções:

- **Limite de rodadas.** Uma ou duas passadas de revisão costumam capturar a
  maior parte do ganho. Raramente vale mais que isso.
- **Critério de parada objetivo.** Pare quando o revisor responder "APROVADO"
  contra a rubrica — não "quando ficar perfeito".
- **Cuidado com a regressão.** Ao corrigir, o autor pode quebrar o que estava
  bom. Instrua "não introduza novos problemas" e, em tarefa crítica, rode a
  bateria de testes (bônus B1).

> **Regra prática:** 1 rodada autor→revisor→autor entrega a maior parte do valor.
> Só encadeie mais se cada rodada estiver medindo um ganho real.

---

## 6. Anti-padrões

- **Revisor sem rubrica.** Vira gerador de elogios; não melhora nada.
- **Refinar para sempre.** Sem limite de rodadas nem critério de parada.
- **Autor cego às próprias críticas.** Não passar as críticas de volta para a
  reescrita.
- **Ignorar regressão.** Cada reescrita pode quebrar o que funcionava.
- **Auto-refinamento em tarefa trivial.** Overhead sem ganho — uma classificação
  não precisa de revisor.

---

## Exercícios

1. **Monte o pipeline.** Escolha uma tarefa (gerar uma função, escrever um
   texto). Rode em uma passada só e depois no pipeline autor→revisor→autor com
   rubrica. Compare a qualidade final.

2. **Rubrica importa.** Rode o revisor **sem** rubrica e **com** rubrica. Sem
   ela, ele achou defeitos reais ou só elogiou? Com ela, o que mudou?

3. **Adversarial vs. simpático.** Peça uma revisão "valide isto" e outra
   "tente quebrar isto em produção". Qual encontrou problemas que a outra não
   viu?

4. **Retorno decrescente.** Rode 3 rodadas de refinamento na mesma saída. Da
   rodada 1 para a 2, e da 2 para a 3, quanto melhorou? Onde parou de valer a
   pena? Alguma rodada **piorou** (regressão)?

Soluções em [`../exercicios/solucoes.md`](../exercicios/solucoes.md).

---

## Resumo

- Separe **autor** (gera) de **revisor** (duvida): cada postura rende melhor
  isolada. Pipeline: **gerar → criticar → corrigir**.
- A **rubrica** é o que faz o revisor morder — geralmente os guardrails (Aula 8)
  e critérios (Aula 6) reaproveitados como checklist.
- **Reflexão** e **crítica adversarial** encontram o que a validação simpática
  não acha.
- **Limite as rodadas** (1–2 bastam), pare por critério objetivo e cuide da
  **regressão**.

**Próximo:** [Aula 10 — Personas, papéis e especialização →](./aula-10-personas-papeis-especializacao.md)
