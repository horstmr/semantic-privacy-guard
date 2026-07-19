# 📦 Repositório-base

> O ponto de partida **pronto para aplicar no seu projeto real**. Copie estes
> arquivos, preencha os `<...>`, e a IA já "conhece" seu projeto: arquitetura,
> convenções, onde o código mora e como entregar.

## O que tem aqui

| Arquivo | Papel |
|---------|-------|
| [`AGENTS.md`](./AGENTS.md) | As **regras persistentes** que a IA lê a cada interação (stack, arquitetura, convenções, proibições) |
| [`.cursorrules`](./.cursorrules) | O mesmo, no formato do Cursor (aponta para o AGENTS.md) |
| [`estrutura.md`](./estrutura.md) | A **estrutura de pastas** de referência (Clean + DDD + CQRS) e como ligar tudo |

## Como aplicar (5 min)

```
1. Copie AGENTS.md e .cursorrules para a raiz do seu projeto.
   (Copilot: renomeie para .github/copilot-instructions.md; Claude Code: CLAUDE.md.)
2. Preencha os <placeholders>: stack, versões, arquitetura escolhida, glossário.
3. Crie o esqueleto de pastas (ver estrutura.md), no nível de arquitetura que
   você escolheu (Módulo 03 — não empilhe padrões por moda).
4. Crie docs/specs/ e cole os templates de SDD (spec/plano/tarefas/adr/matriz).
5. Adote as skills (skills/) que seu time vai usar.
6. Configure o CI mínimo (skill `gerar-pipeline-ci`): testes + lint em todo PR.
```

Pronto. A partir daqui, toda geração da IA nasce dentro dos seus trilhos.

## Como isso liga com o resto do kit

```
AGENTS.md ──declara──► Arquitetura (Módulo 03 / pasta arquitetura/)
   │                         │
   │                         └──restringe──► onde o código mora
   ▼
docs/specs/ ──SDD──► Spec → Plano → Tarefas (Módulo 02 / spec-driven-development/)
   │
   ▼
skills/ ──executam──► cada etapa no padrão (Módulo 04 / skills/)
   │
   ▼
Review + Matriz ──garantem──► previsível, revisável, rastreável (Módulo 05)
```

> Comece pelo [Módulo 06](../modulos/06-do-zero-ao-projeto-real.md), que roda o
> ciclo completo usando exatamente estes arquivos.
