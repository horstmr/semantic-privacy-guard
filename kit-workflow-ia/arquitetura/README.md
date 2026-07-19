# 🏛️ Arquiteturas Prontas para IA

> Estruturas arquiteturais adaptadas para funcionar **com IA sem virar código
> caótico**. Cada guia traz: a ideia, a estrutura de pastas, e o **bloco de
> regras** para colar no `AGENTS.md`/`.cursorrules` do seu projeto.

A tese (ver [Módulo 03](../modulos/03-arquiteturas-prontas-para-ia.md)):
**arquitetura é restrição**, e restrição é o que a IA precisa para gerar código
que encaixa em vez de virar sopa.

| Guia | Quando |
|------|--------|
| [Clean Architecture para IA](./clean-architecture-ia.md) | Ordem sem cerimônia; dependências para dentro |
| [DDD para IA](./ddd-ia.md) | Domínio rico; linguagem do negócio + fronteiras |
| [CQRS para IA](./cqrs-ia.md) | Leitura e escrita bem diferentes; escala |

## Como aplicar

1. Escolha o nível certo (não empilhe por moda — [Módulo 03 §5](../modulos/03-arquiteturas-prontas-para-ia.md#5-qual-escolher)).
2. Copie a **estrutura de pastas** do guia para o seu projeto.
3. Cole o **bloco de regras** do guia no seu `AGENTS.md` (ver [repo-base](../repo-base/README.md)).
4. Use as skills de [implementação](../skills/03-implementacao.md), que já
   respeitam essas fronteiras.

> Combinam-se em camadas: **Clean** é a base; **DDD** enriquece o domínio;
> **CQRS** separa leitura/escrita por cima. Use só o que remove ambiguidade real.
