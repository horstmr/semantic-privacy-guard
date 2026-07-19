# Kit: Workflow Completo com IA

> Do zero ao domínio do **workflow de desenvolvimento assistido por IA** — com
> **skills prontas**, **arquiteturas adaptadas para IA** (Clean · DDD · CQRS) e
> **spec-driven development**, para que o código gerado seja **previsível,
> revisável e rastreável** em vez de virar caos.

Este kit é a parte **"mão na massa"** do repositório: enquanto o
[curso de Prompt Engineering](../curso-prompt-engineering-para-devs/README.md)
ensina a instruir modelos e o
[curso de IA Aplicada](../curso-engenharia-ia-aplicada/README.md) dá o panorama,
aqui você monta o **fluxo de trabalho real** de construir software com IA sem
perder controle da arquitetura.

---

## O que tem aqui (os 5 pilares)

| Pilar | Onde |
|-------|------|
| 🧭 **6 módulos** do zero ao domínio do workflow | [`modulos/`](./modulos/) |
| 🧰 **40+ skills prontas** que fazem a IA trabalhar dentro da sua arquitetura | [`skills/`](./skills/README.md) |
| 🏛️ **Arquiteturas prontas** (Clean, DDD, CQRS) adaptadas para IA | [`arquitetura/`](./arquitetura/README.md) |
| 📝 **Templates spec-driven-development** (código previsível/revisável/rastreável) | [`spec-driven-development/`](./spec-driven-development/README.md) |
| 📦 **Repositório-base** pronto para aplicar no seu projeto real | [`repo-base/`](./repo-base/README.md) |

---

## A ideia central: a IA é um dev genial e amnésico

Um modelo escreve código excelente por trecho, mas **não conhece a sua
arquitetura, seus padrões nem o seu histórico** — e, sem trilhos, gera código
que *parece* certo e apodrece o projeto (a "ilusão da plausibilidade"). O
workflow deste kit resolve isso com três trilhos:

1. **Spec antes de código** (SDD) — você especifica; a IA gera contra a spec.
   O código vira consequência de um contrato revisável, não de um chute.
2. **Arquitetura como restrição** — Clean/DDD/CQRS dão à IA fronteiras claras
   (onde cada coisa mora), então o que ela gera encaixa em vez de virar sopa.
3. **Skills reutilizáveis** — receitas de prompt/fluxo versionadas que executam
   cada etapa do jeito certo, toda vez.

> **Macete:** *Spec manda, arquitetura restringe, skill executa, humano revisa.*

---

## Os 6 módulos

| # | Módulo | O que você domina |
|---|--------|-------------------|
| 01 | [Fundamentos do workflow com IA](./modulos/01-fundamentos-workflow-ia.md) | Mentalidade, setup, quando (não) usar IA, o loop de trabalho |
| 02 | [Spec-Driven Development](./modulos/02-spec-driven-development.md) | Especificar antes de gerar; spec → plano → tarefas → código |
| 03 | [Arquiteturas prontas para IA](./modulos/03-arquiteturas-prontas-para-ia.md) | Clean, DDD e CQRS como trilhos que a IA respeita |
| 04 | [Skills dentro da sua arquitetura](./modulos/04-skills-dentro-da-arquitetura.md) | Criar e usar skills que geram código no seu padrão |
| 05 | [Previsível, revisável, rastreável](./modulos/05-codigo-previsivel-revisavel-rastreavel.md) | Guardrails, review de código de IA, rastreabilidade spec↔código |
| 06 | [Do zero ao projeto real](./modulos/06-do-zero-ao-projeto-real.md) | Montar o repo-base e rodar o ciclo completo numa feature real |

---

## Como usar

1. **Leia os 6 módulos em ordem** — eles montam o workflow peça por peça.
2. **Copie o [`repo-base/`](./repo-base/README.md)** para o seu projeto (ou use
   de referência): ele já traz `AGENTS.md`/`.cursorrules`, estrutura de pastas e
   os templates ligados.
3. **Use as [skills](./skills/README.md) como comandos** no seu dia a dia — cada
   uma é um prompt/fluxo pronto para uma etapa (scaffold, testar, revisar,
   refatorar, documentar...).
4. **Aplique o SDD** em toda feature não trivial: spec → plano → tarefas → gerar
   → revisar contra a spec.

**Pré-requisitos:** saber programar e ter um assistente de IA no fluxo (Cursor,
Claude Code, Copilot, ou a API que você usa). Nenhum framework específico é
obrigatório — os padrões são agnósticos de linguagem.

---

## Relação com o resto do repositório

- Usa a base de **[Prompt Engineering](../curso-prompt-engineering-para-devs/README.md)**
  (contrato, R.O.C.C.O., válvulas de escape) para escrever as skills.
- Usa conceitos de **[IA Aplicada](../curso-engenharia-ia-aplicada/README.md)**
  (MCP, agentes, `.cursorrules`, tool use) para conectar a IA ao seu ambiente.
- Tem um resumo visual na **[Apostila Macetosa](../apostila/README.md)**.

> **Aviso honesto:** este kit é material autoral inspirado na *estrutura*
> anunciada de kits de "workflow com IA". Todo o conteúdo, skills e templates são
> originais e prontos para adaptar ao seu contexto. As "40+ skills" são um
> catálogo real e extensível — comece por elas e crie as suas.
