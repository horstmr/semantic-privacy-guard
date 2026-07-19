# 🧰 Biblioteca de Skills (40+)

> Skills são **prompts/fluxos prontos e versionados** que executam uma etapa do
> workflow sempre do mesmo jeito — no seu padrão, na sua arquitetura. Copie,
> troque os `{{placeholders}}`, use.

Cada skill segue a [anatomia do Módulo 04](../modulos/04-skills-dentro-da-arquitetura.md):
**quando usar · entradas · saída · prompt** (com R.O.C.C.O. + arquitetura +
formato + bordas embutidos).

> **Como usar:** cole o prompt no seu assistente (Cursor, Claude Code, Copilot
> Chat, ou API), preenchendo os `{{...}}`. Muitas assumem que a **arquitetura**
> está declarada no seu `AGENTS.md`/`.cursorrules` (ver [repo-base](../repo-base/README.md)).

---

## Catálogo por fase do workflow

### 1. [Planejamento & Spec](./01-planejamento-e-spec.md)
`afiar-spec` · `gerar-criterios-aceite` · `descobrir-casos-de-borda` ·
`decompor-tarefas` · `estimar-risco` · `gerar-perguntas-de-esclarecimento` ·
`converter-bug-em-spec`

### 2. [Arquitetura & Design](./02-arquitetura-e-design.md)
`propor-abordagem` · `gerar-adr` · `modelar-contexto-ddd` · `desenhar-fronteiras` ·
`revisar-aderencia-arquitetural` · `escolher-padrao` · `mapear-dependencias`

### 3. [Implementação](./03-implementacao.md)
`criar-caso-de-uso` · `criar-command` · `criar-query` · `criar-entidade` ·
`criar-value-object` · `criar-adapter` · `criar-endpoint` · `implementar-tarefa`

### 4. [Testes & Qualidade](./04-testes-e-qualidade.md)
`gerar-testes-ca` · `gerar-testes-borda` · `gerar-testes-propriedade` ·
`cobrir-lacunas-de-teste` · `gerar-fixtures` · `revisar-testes`

### 5. [Review & Refatoração](./05-review-e-refatoracao.md)
`revisar-contra-spec` · `caca-bugs-adversarial` · `revisar-seguranca` ·
`refatorar-com-seguranca` · `reduzir-complexidade` · `revisar-performance` ·
`explicar-codigo-legado`

### 6. [Docs & DevOps](./06-docs-e-devops.md)
`gerar-readme` · `gerar-matriz-rastreabilidade` · `mensagem-commit-rastreavel` ·
`descrever-pr` · `gerar-changelog` · `gerar-pipeline-ci` · `documentar-api`

---

## Regras de ouro ao usar skills

1. **Uma skill = uma etapa.** Encadeie skills; não crie uma que faz tudo.
2. **Ancore na arquitetura.** A skill deve saber onde o código mora.
3. **Saída fixa.** O resultado precisa ser revisável e componível.
4. **Você revisa.** A skill acelera; a decisão final é humana (Módulo 05).
5. **Versione e meça.** Skill que oscila entre execuções precisa de ajuste
   (Aula 11 do [Prompt Engineering](../../curso-prompt-engineering-para-devs/aulas/aula-11-design-de-saida-e-verbosidade.md)).

## Criando suas próprias skills

Siga a [receita do Módulo 04 §5](../modulos/04-skills-dentro-da-arquitetura.md#5-como-criar-a-sua-skill):
capture o melhor prompt → enxerte arquitetura + formato → feche bordas →
placeholders → versione → meça. Guarde as suas junto a estas.
