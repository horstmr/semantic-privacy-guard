<!-- TEMPLATE DE TAREFAS — docs/specs/<feature>/tarefas.md
     A IA decompõe (skill `decompor-tarefas`); cada tarefa vira um PR pequeno. -->

# Tarefas: <nome da feature>

- **Spec:** `spec.md` · **Plano:** `plano.md`

> Regra: cada tarefa é **pequena e revisável** (um PR curto), cita o(s)
> requisito(s) que atende, e é implementada **uma por vez** (skill `implementar-tarefa`).

## Lista (ordenada por dependência)

- [ ] **T1** — Criar Value Object `Cupom` com validade — (R2) — depende de: —
- [ ] **T2** — Definir port `CupomRepository` — (R2) — depende de: —
- [ ] **T3** — Implementar caso de uso/Command `AplicarCupom` — (R1, R2) — depende de: T1, T2
- [ ] **T4** — Adapter `PostgresCupomRepository` — (R2) — depende de: T2
- [ ] **T5** — Endpoint `POST /checkout/cupom` — (R1) — depende de: T3
- [ ] **T6** — Testes de CA (CA1..CAn) e bordas — (R1, R2) — depende de: T3
- [ ] **T7** — Matriz de rastreabilidade + docs — (todos) — depende de: T6

## Convenções

- Uma tarefa **não mistura camadas** sem necessidade (não faça "entidade + endpoint" juntos).
- Tarefa que exige **decisão de arquitetura** → registre um [ADR](./03-template-adr.md) antes.
- Ao concluir, marque `[x]` e referencie o PR.

## Rastreabilidade rápida (preencha ao avançar)

| Tarefa | Requisitos | PR | Status |
|--------|-----------|----|--------|
| T1 | R2 | #— | ⬜ |
| T3 | R1,R2 | #— | ⬜ |

---
> Feche o ciclo com a [matriz de rastreabilidade](./04-matriz-rastreabilidade.md)
> e o [checklist de pronto para produção](../modulos/06-do-zero-ao-projeto-real.md#3-checklist-pronto-para-produção).
