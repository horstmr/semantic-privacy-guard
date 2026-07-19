# Módulo 02 — Spec-Driven Development (SDD)

> A virada que torna o código gerado por IA **previsível, revisável e
> rastreável**: você não pede código, você **especifica** — e a IA gera contra a
> spec. O código vira consequência de um contrato, não de um chute.

## Objetivos

- Entender o fluxo SDD: **Spec → Plano → Tarefas → Código → Verificação**.
- Escrever uma **spec** que a IA consegue executar e um humano consegue revisar.
- Manter **rastreabilidade** entre requisito, código e teste.

---

## 1. Por que spec antes de código

Sem spec, revisar código de IA é revisar contra o seu humor: "acho que é isso".
Com spec, você revisa contra um **artefato explícito e acordado**. Isso muda tudo:

- **Previsível** — a IA sabe exatamente o alvo; menos "interpretação criativa".
- **Revisável** — o PR se lê contra a spec: cada item foi atendido? sim/não.
- **Rastreável** — cada linha de código aponta para um requisito, e cada
  requisito para um teste.

> É o mesmo princípio do Prompt Engineering (prompt é contrato) elevado à
> **feature**: a feature é um contrato, e o código é a implementação verificável
> dele.

---

## 2. O fluxo SDD

```
1. SPEC ──────── o quê e por quê: contexto, requisitos, critérios de aceite,
   │             fora-de-escopo, restrições. (Humano escreve; IA ajuda a afiar.)
   ↓
2. PLANO ─────── como: abordagem técnica, arquitetura afetada, riscos,
   │             decisões (ADR se for relevante). (IA propõe; humano aprova.)
   ↓
3. TAREFAS ───── a decomposição em passos pequenos e revisáveis, cada um
   │             ligado a um item da spec. (IA decompõe; humano ordena.)
   ↓
4. CÓDIGO ────── a IA implementa UMA tarefa por vez, contra a spec + arquitetura.
   ↓
5. VERIFICAÇÃO ─ testes derivados dos critérios de aceite; revisão contra a spec;
                 rastreabilidade registrada.
```

Cada seta é um **ponto de controle humano** — você aprova antes de avançar. É o
oposto de "gere a feature inteira e torça".

Templates prontos de cada artefato em
[`spec-driven-development/`](../spec-driven-development/README.md).

---

## 3. Anatomia de uma boa spec

Uma spec executável por IA e revisável por humano tem:

```markdown
# Feature: <nome>

## Contexto / Problema
Por que isto existe, para quem, e qual dor resolve.

## Requisitos (numerados — viram rastreabilidade)
R1. O sistema deve ...
R2. Quando <condição>, o sistema deve ...

## Critérios de aceite (viram testes — Given/When/Then)
CA1. Dado ..., quando ..., então ...

## Fora de escopo
O que explicitamente NÃO faz parte desta feature.

## Restrições
Arquitetura, performance, segurança, compatibilidade.

## Casos de borda
Vazio, erro, concorrência, dados inválidos.
```

Os **requisitos numerados** (R1, R2...) e os **critérios de aceite** (CA1...) são
a espinha da rastreabilidade: código e testes vão referenciá-los.

---

## 4. Requisitos → testes (rastreabilidade)

O truque que fecha o ciclo: **cada critério de aceite vira um teste**, e cada
teste cita o critério. Assim você prova que a spec foi cumprida.

```
CA1 "Dado carrinho vazio, quando finalizar, então erro CARRINHO_VAZIO"
        ↓ vira
test('CA1: carrinho vazio bloqueia checkout', ...)   // referência explícita
```

Uma **matriz de rastreabilidade** (template no SDD) mapeia:
`Requisito → arquivo/função → teste`. Numa auditoria (ou num bug), você anda pelo
mapa em segundos.

---

## 5. O papel da IA em cada etapa

- **Spec**: a IA **afia** — aponta ambiguidades, casos de borda esquecidos,
  requisitos contraditórios. Não deixe ela *decidir* o produto; deixe ela *criticar*.
- **Plano**: a IA **propõe** a abordagem técnica e os riscos; você escolhe.
- **Tarefas**: a IA **decompõe** em passos pequenos ligados à spec.
- **Código**: a IA **implementa uma tarefa por vez**, citando os requisitos.
- **Verificação**: a IA **gera testes** dos critérios de aceite e **revisa** o
  próprio código contra a spec (pipeline autor-revisor).

---

## 6. Anti-padrões

- **Spec vaga** ("fazer o login funcionar") → a IA inventa o resto. Numere requisitos.
- **Pular o plano** → a IA escolhe arquitetura no meio do código → inconsistência.
- **Gerar tudo de uma vez** → PR irrevisável. Uma tarefa por vez.
- **Critérios sem teste** → você "acha" que cumpriu. Todo CA vira teste.
- **Spec que a IA escreveu sozinha e ninguém leu** → contrato fantasma.

---

## Exercícios

1. **Escreva uma spec real.** Pegue uma feature pequena e escreva a spec com o
   template (requisitos numerados + critérios de aceite Given/When/Then + fora de
   escopo + bordas). Peça à IA para **criticar** sua spec — o que ela achou de
   ambíguo?

2. **Do CA ao teste.** Converta 3 critérios de aceite em 3 testes que citam o CA.
   Rode. Eles falham no código ainda inexistente (red)?

3. **Decomponha.** Peça à IA para quebrar a feature em tarefas pequenas ligadas
   aos requisitos. Alguma tarefa mistura 2 requisitos? Separe.

4. **Matriz.** Preencha uma linha da matriz de rastreabilidade
   (requisito → arquivo → teste) para a feature.

---

## Resumo

- **Spec antes de código** torna o resultado previsível, revisável e rastreável.
- Fluxo: **Spec → Plano → Tarefas → Código → Verificação**, com aprovação humana
  em cada seta.
- **Requisitos numerados** + **critérios de aceite** = espinha da rastreabilidade;
  cada CA vira teste.
- A IA **afia, propõe, decompõe, implementa e revisa** — mas você aprova cada etapa.

**Próximo:** [Módulo 03 — Arquiteturas prontas para IA →](./03-arquiteturas-prontas-para-ia.md)
