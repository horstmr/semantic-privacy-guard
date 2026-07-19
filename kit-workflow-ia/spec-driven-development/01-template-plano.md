<!-- TEMPLATE DE PLANO TÉCNICO — docs/specs/<feature>/plano.md
     A IA propõe (skill `propor-abordagem`); VOCÊ aprova antes de codar. -->

# Plano técnico: <nome da feature>

- **Spec de referência:** `spec.md` (na mesma pasta) · **Status:** proposto | aprovado

## 1. Abordagem escolhida
<Descreva a solução técnica em alto nível. Qual estratégia, por quê.
Se houve alternativas relevantes, registre a decisão em um [ADR](./03-template-adr.md).>

## 2. Componentes afetados
> Onde o código vai morar (respeitando a arquitetura declarada).

| Componente | Camada | Novo/alterado | Requisitos |
|------------|--------|---------------|-----------|
| <Cupom> (Value Object) | domain | novo | R2 |
| <AplicarCupom> (Command/UseCase) | application | novo | R1, R2 |
| <CupomRepository> (Port) | application/ports | novo | R2 |
| <PostgresCupomRepo> (Adapter) | infra | novo | R2 |
| <POST /checkout/cupom> (Endpoint) | infra/http | novo | R1 |

## 3. Contratos (DTOs / interfaces)
```
Input:  { pedidoId, codigoCupom }
Output: { total, descontoAplicado } | Erro(CUPOM_INVALIDO)
Port:   CupomRepository.buscarPorCodigo(codigo): Cupom | null
```

## 4. Riscos e mitigações
| Risco | Prob./Impacto | Mitigação |
|-------|---------------|-----------|
| <cupom aplicado 2x em concorrência> | médio | <trava otimista / idempotência> |

## 5. Impacto e migração
- **Dados:** <nova tabela? migração? backfill?>
- **Compatibilidade:** <breaking change? versionamento?>
- **Observabilidade:** <o que logar/medir>

## 6. Estratégia de testes
- Testes de CA: CA1..CAn (skill `gerar-testes-ca`).
- Testes de borda: <quais> (skill `gerar-testes-borda`).
- Integração: <adapters/endpoints>.

---
> Aprovado o plano, gere as [tarefas](./02-template-tarefas.md). Não comece a
> codar antes de aprovar o plano — é aqui que se evita a IA escolher arquitetura
> no meio do código.
