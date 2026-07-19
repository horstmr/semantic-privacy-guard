# DDD (Domain-Driven Design) para IA

> O código fala a **linguagem do negócio** e é organizado por **fronteiras de
> significado**. Para a IA, DDD entrega um glossário e fronteiras — ouro contra a
> geração inconsistente.

## Os dois presentes do DDD para a IA

1. **Ubiquitous Language (linguagem ubíqua)** — um glossário do domínio que vai no
   `AGENTS.md`. A IA usa os mesmos termos do negócio ("Pedido", "Fatura",
   "Reserva"), não inventa sinônimos ("Order" aqui, "Compra" ali).
2. **Bounded Contexts (contextos delimitados)** — fronteiras onde um termo tem UM
   significado. "Cliente" em Vendas ≠ "Cliente" em Suporte; a IA não mistura.

## Blocos de construção (tático)

| Bloco | O que é | Regra para a IA |
|-------|---------|-----------------|
| **Entity** | tem identidade e ciclo de vida | invariantes protegidas; métodos na linguagem do negócio |
| **Value Object** | sem identidade, imutável, igualdade por valor | validado na criação; ex.: Dinheiro, Email, Cupom |
| **Aggregate** | grupo consistente com uma **raiz** | só a raiz é acessada de fora; fronteira de transação |
| **Domain Event** | algo relevante que aconteceu | passado ("PedidoConfirmado"); dispara reações |
| **Repository** | coleção de aggregates (via port) | um por aggregate; interface na aplicação |
| **Domain Service** | regra que não pertence a uma entidade | puro; sem infra |

## Estrutura de pastas (sobre Clean)

```
src/domain/
├── <contexto>/                  # um bounded context por pasta
│   ├── entities/
│   ├── value-objects/
│   ├── aggregates/              # raiz + membros
│   ├── events/
│   ├── services/                # domain services
│   └── <contexto>.glossario.md  # a linguagem ubíqua deste contexto
```

## Bloco de regras para o AGENTS.md

```
## Domínio: DDD
- Bounded contexts: <liste os seus, ex.: Vendas, Estoque, Faturamento>.
  Um termo pode significar coisas diferentes em contextos diferentes — não misture.
- Ubiquitous Language (use EXATAMENTE estes termos no código e nas conversas):
  - Pedido: <definição de 1 linha>
  - Item: <...>
  - Fatura: <...>
  - Reserva: <...>
- Aggregates: acesse só pela raiz; a raiz garante a consistência do conjunto.
- Value Objects para conceitos sem identidade (Dinheiro, Email, CPF, Cupom):
  imutáveis, validados na criação, igualdade por valor.
- Domain Events no passado (algo que aconteceu); emitidos pela raiz do aggregate.
- Regras de negócio vivem no domínio, expressas na linguagem ubíqua.
```

## O glossário é o artefato mais valioso

Antes de codar um domínio, gere o glossário (skill `modelar-contexto-ddd`) e
**cole-o no AGENTS.md**. A partir daí, toda geração da IA fala a língua do
negócio. Um glossário de 10 termos elimina dezenas de inconsistências futuras.

## Anti-padrões

- **Anemic domain** — entidades só com getters/setters e a regra espalhada nos
  serviços. A regra deve estar no domínio.
- **Um aggregate gigante** — tudo numa raiz só. Aggregates pequenos, fronteiras claras.
- **Vazar o modelo entre contextos** — use DTOs/eventos e (quando preciso) uma
  anti-corruption layer.
- **Termos técnicos onde deveria ser negócio** — "UserRecord" em vez de "Cliente".

## Skills relacionadas

`modelar-contexto-ddd` · `desenhar-fronteiras` · `criar-entidade` ·
`criar-value-object` (em [skills/](../skills/README.md)).
