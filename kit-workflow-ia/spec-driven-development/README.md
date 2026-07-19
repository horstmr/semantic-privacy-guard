# 📝 Spec-Driven Development — Templates

> Templates que tornam o código gerado por IA **previsível, revisável e
> rastreável**. Copie, preencha, versione em `docs/specs/` do seu projeto.

Fluxo (ver [Módulo 02](../modulos/02-spec-driven-development.md)):
**Spec → Plano → Tarefas → Código → Verificação**, com aprovação humana em cada seta.

| Ordem | Template | Papel |
|-------|----------|-------|
| 1 | [Spec](./00-template-spec.md) | O quê e por quê (requisitos + critérios de aceite) |
| 2 | [Plano técnico](./01-template-plano.md) | Como (abordagem, componentes, riscos) |
| 3 | [Tarefas](./02-template-tarefas.md) | Decomposição em passos pequenos ligados a requisitos |
| 4 | [ADR](./03-template-adr.md) | Registro de decisão de arquitetura |
| 5 | [Matriz de rastreabilidade](./04-matriz-rastreabilidade.md) | Requisito ↔ código ↔ teste |

## Como usar no ciclo

```
docs/specs/<feature>/
├── spec.md          ← template 00 (humano escreve; IA critica com afiar-spec)
├── plano.md         ← template 01 (IA propõe com propor-abordagem; humano aprova)
├── tarefas.md       ← template 02 (IA decompõe com decompor-tarefas)
├── adr-XXX.md        ← template 03 (para decisões relevantes)
└── rastreabilidade.md← template 04 (IA mantém com gerar-matriz-rastreabilidade)
```

## Regra que amarra tudo

**Requisitos numerados (R1...) e critérios de aceite (CA1...) são a espinha da
rastreabilidade.** Código cita requisitos (`// R3`); testes citam critérios
(`test('CA2: ...')`); a matriz liga os três. É isso que transforma "a IA gerou
código" em "a feature cumpre o contrato, e eu provo".
