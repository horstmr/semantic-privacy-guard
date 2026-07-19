<!-- TEMPLATE DE ADR (Architecture Decision Record) — docs/specs/<feature>/adr-XXX.md
     ou docs/adr/. Gere com a skill `gerar-adr`. Um ADR por decisão relevante. -->

# ADR-XXX: <título curto da decisão>

- **Status:** proposto | aceito | substituído por ADR-YYY | descontinuado
- **Data:** <AAAA-MM-DD> · **Decisores:** <quem>

## Contexto
<Qual problema/força motivou a decisão. Restrições em jogo (técnicas, de negócio,
de prazo). Fatos, não opinião. Por que precisamos decidir isto AGORA.>

## Decisão
<O que foi decidido, no imperativo. "Vamos usar X para Y."
Seja específico o suficiente para guiar a implementação e a IA.>

## Alternativas consideradas
| Alternativa | Prós | Contras | Por que não |
|-------------|------|---------|-------------|
| <A escolhida> | ... | ... | (escolhida) |
| <B> | ... | ... | <motivo do descarte> |
| <C> | ... | ... | <motivo do descarte> |

## Consequências
- **Positivas:** <o que ganhamos>.
- **Negativas / custo:** <o que aceitamos de ruim>.
- **Nova restrição:** <o que passa a ser regra — isto entra no AGENTS.md se afeta
  como a IA deve gerar código daqui pra frente>.

## Referências
- Spec: <link> · Requisitos afetados: <R...>

---
> ADRs são a memória do "porquê". Num bug ou numa mudança futura, você (ou seu
> "eu de daqui a 6 meses") lê o ADR e entende a decisão sem arqueologia.
> Decisões que viram restrição permanente devem ser refletidas no `AGENTS.md`.
