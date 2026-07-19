# Módulo 06 — Do Zero ao Projeto Real

> Juntando tudo: montar o **repositório-base** e rodar o **ciclo completo** numa
> feature de verdade, do `git init` ao PR revisável e rastreável.

## Objetivos

- Montar um projeto com o [`repo-base/`](../repo-base/README.md) do kit.
- Rodar o **workflow ponta a ponta** numa feature real.
- Ter um checklist de "pronto para produção".

---

## 1. Montando o repositório-base

O [`repo-base/`](../repo-base/README.md) já traz tudo ligado. Passos:

```
1. Copie o repo-base para o seu projeto (ou aplique as partes ao existente).
2. Preencha o AGENTS.md/.cursorrules: stack, arquitetura, convenções, proibições.
3. Escolha a arquitetura (Módulo 03) e crie o esqueleto de pastas.
4. Ligue os templates SDD (spec/plano/tarefas/ADR/matriz) em docs/specs/.
5. Aponte as skills (Módulo 04) que seu time vai usar.
6. Configure o CI mínimo: testes + lint rodando em todo PR.
```

Pronto: a IA já "conhece" o projeto, sabe onde pôr o código e como entregar.

---

## 2. O ciclo completo numa feature real

Vamos aplicar tudo numa feature exemplo: **"aplicar cupom de desconto no checkout"**.

### Passo 1 — Spec (Módulo 02)
Escreva a spec com o template. Requisitos numerados, critérios de aceite
Given/When/Then, fora de escopo, bordas. Peça à IA (`afiar-spec`) para criticar.

```
R1. O sistema deve aceitar um código de cupom no checkout.
R2. Cupom inválido/expirado deve ser rejeitado com erro CUPOM_INVALIDO.
CA1. Dado cupom válido de 10%, quando aplicar, então total reduz 10%.
CA2. Dado cupom expirado, quando aplicar, então erro CUPOM_INVALIDO.
Fora de escopo: cupons cumulativos.
```

### Passo 2 — Plano + Tarefas
`propor-abordagem` → decide: Value Object `Cupom`, caso de uso `AplicarCupom`,
port `CupomRepository`. `decompor-tarefas` → lista pequena, cada tarefa ligada a
um requisito. Se houver decisão relevante, `gerar-adr`.

### Passo 3 — Implementação (uma tarefa por vez)
`criar-command` (CQRS) ou `criar-caso-de-uso` (Clean) para `AplicarCupom`,
ancorado na arquitetura declarada. A IA gera o código **no lugar certo** + o teste.

### Passo 4 — Testes (dos critérios de aceite)
`gerar-testes-ca` transforma CA1/CA2 em testes que citam os critérios.
`gerar-testes-borda` cobre vazio, expirado, formato inválido.

### Passo 5 — Review + rastreabilidade (Módulo 05)
`revisar-contra-spec` (a IA checa requisito a requisito) →
**você revisa com a rubrica** → `gerar-matriz-rastreabilidade` →
`mensagem-commit-rastreavel` citando R1/R2 e CA1/CA2 → PR pequeno e revisável.

Resultado: uma feature **previsível** (saiu no padrão), **revisável** (PR
pequeno contra a spec) e **rastreável** (matriz requisito↔código↔teste).

---

## 3. Checklist "pronto para produção"

Antes de abrir o PR de uma feature gerada com IA:

```
[ ] Spec escrita e criticada (requisitos numerados + critérios de aceite)
[ ] Plano/ADR para decisões relevantes
[ ] Código na camada certa (regra de dependência respeitada)
[ ] Testes cobrem TODOS os critérios de aceite + bordas
[ ] Revisado contra a spec com a rubrica (Módulo 05)
[ ] Sem libs novas sem justificativa; sem código órfão
[ ] Matriz de rastreabilidade atualizada
[ ] PR pequeno, com mensagem que cita os requisitos
[ ] CI verde (testes + lint)
```

Se todos marcados, você tem engenharia com IA — não geração de dívida.

---

## 4. Escalando para o time

- **Regras no repo** (`AGENTS.md`) versionadas → todo mundo (e toda IA) segue o
  mesmo padrão.
- **Skills compartilhadas** em `skills/` → o time inteiro executa etapas igual.
- **Templates SDD** → specs consistentes entre features e pessoas.
- **Revisão contra spec** → onboarding mais fácil (o alvo é explícito).
- **ADRs** → o "porquê" das decisões fica no repo, não na cabeça de alguém.

O kit vira o **contrato social** do time com a IA: previsível para todos.

---

## Exercícios / Projeto final

> Este é o capstone do kit.

1. **Monte o repo-base** num projeto (novo ou existente): preencha o `AGENTS.md`,
   escolha a arquitetura, ligue os templates SDD.

2. **Rode o ciclo completo** numa feature real e pequena sua, do jeito do §2:
   spec → plano/tarefas → código (uma tarefa por vez) → testes de CA → review com
   rubrica → matriz → PR.

3. **Meça honestamente:** o código saiu no padrão? o PR foi revisável? você
   consegue, pela matriz, ir de um requisito ao teste em segundos?

4. **Refine uma skill** que você sentiu falta e adicione ao `skills/` do seu repo.

**Entregável:** a feature no ar (ou em PR), com spec, testes, matriz e ADR — um
portfólio de *engenharia de software com IA*, não só de "usei IA".

---

## Resumo

- O `repo-base` liga tudo: regras + arquitetura + templates SDD + skills + CI.
- O **ciclo completo**: spec → plano/tarefas → código (1 tarefa/vez) → testes de
  CA → review com rubrica → matriz → PR pequeno.
- Use o **checklist** antes de todo PR gerado com IA.
- No time, o kit vira o **contrato social** com a IA: previsível para todos.

---

## 🎉 Fim do kit

Você agora tem o **workflow completo**: mentalidade (M1), spec-driven (M2),
arquiteturas para IA (M3), skills (M4), guardrails (M5) e o projeto real (M6).

Próximos passos práticos:
- Explore a [biblioteca de 40+ skills](../skills/README.md) e adote as suas.
- Copie o [`repo-base/`](../repo-base/README.md) e aplique num projeto de verdade.
- Combine com o [Prompt Engineering](../../curso-prompt-engineering-para-devs/README.md)
  para escrever skills melhores.

**Spec manda, arquitetura restringe, skill executa, humano revisa.**
