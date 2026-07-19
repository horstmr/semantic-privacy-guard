<!-- TEMPLATE DE MATRIZ DE RASTREABILIDADE — docs/specs/<feature>/rastreabilidade.md
     Mantida pela IA com a skill `gerar-matriz-rastreabilidade`. -->

# Matriz de rastreabilidade: <nome da feature>

- **Spec:** `spec.md`

> Liga **requisito ↔ implementação ↔ teste**. É o que transforma "a IA gerou
> código" em "a feature cumpre o contrato, e eu provo em segundos".

## Requisitos → Código → Teste

| Req | Descrição | Implementação (arquivo:função) | Teste (arquivo:caso) | Status |
|-----|-----------|--------------------------------|----------------------|--------|
| R1 | Aceitar cupom no checkout | `application/commands/aplicar-cupom.handler.ts` | `aplicar-cupom.spec:CA1` | ✅ |
| R2 | Rejeitar cupom inválido/expirado | `domain/value-objects/cupom.ts:estaValido()` | `cupom.spec:CA2` | ✅ |
| R3 | <...> | <...> | ⚠️ SEM TESTE | ⛔ |

## Critérios de aceite → Teste

| CA | Requisito | Teste | Passa? |
|----|-----------|-------|--------|
| CA1 | R1 | `aplicar-cupom.spec: 'CA1: cupom 10% reduz total'` | ✅ |
| CA2 | R2 | `cupom.spec: 'CA2: cupom expirado rejeita'` | ✅ |

## Pendências (o que a matriz revelou)

- ⛔ **R3 sem teste** — criar teste antes de fechar a feature.
- ⚠️ **Código órfão** — `<arquivo>` não mapeia a nenhum requisito. Investigar
  (escopo extra? remover? falta requisito?).

## Como ler numa investigação

```
Bug em produção → qual comportamento? → teste correspondente (coluna Teste)
   → requisito (coluna Req) → decisão (ADR, se houver) → contexto completo.
```

---
> Requisito sem teste ou código órfão são **sinais**: o primeiro é risco não
> coberto; o segundo é escopo que ninguém pediu. Resolva ambos antes do
> [checklist de produção](../modulos/06-do-zero-ao-projeto-real.md#3-checklist-pronto-para-produção).
