# Clean Architecture para IA

> Dependências apontam **para dentro**; o núcleo (domínio) não conhece
> frameworks. Dá à IA um mapa de onde cada coisa mora.

## A regra que resolve 80% do caos

**Regra de dependência:** o código de dentro nunca conhece o de fora.

```
Infra  →  Aplicação  →  Domínio          (setas = "depende de")
(DB/HTTP)  (casos de uso)  (regras puras)
```

- **Domínio** não importa nada de framework/infra. É puro e testável isolado.
- **Aplicação** (casos de uso) depende de **interfaces (ports)**, não de implementações.
- **Infra** implementa as ports (adapters de DB, HTTP, filas, libs externas).

Quando você declara isso, a IA para de pôr SQL no controller e regra de negócio
na entidade errada.

## Estrutura de pastas

```
src/
├── domain/                 # puro: sem imports de framework
│   ├── entities/
│   ├── value-objects/
│   ├── events/
│   └── errors/             # DomainError com códigos
├── application/
│   ├── use-cases/          # orquestração; 1 caso de uso = 1 arquivo
│   ├── ports/              # interfaces (repositórios, serviços externos)
│   └── dtos/               # Input/Output DTOs
├── infra/
│   ├── db/                 # adapters de persistência (implementam ports)
│   ├── http/               # controllers/rotas (finos)
│   ├── external/           # SDKs, APIs de terceiros
│   └── config/
└── shared/                 # utilitários sem regra de negócio
```

## Fluxo de uma requisição

```
HTTP request → Controller (infra, fino) → Caso de uso (application)
   → usa Entidade/regras (domain) e Repositório via Port (application)
   → Adapter concreto (infra) fala com o DB
   → Output DTO volta → Controller mapeia para HTTP response
```

## Bloco de regras para o AGENTS.md

```
## Arquitetura: Clean Architecture
- Regra de dependência: dependências apontam SEMPRE para dentro
  (infra → application → domain). NUNCA domain → infra.
- domain/: entidades, value objects, regras. ZERO imports de framework/ORM/HTTP.
- application/use-cases/: um caso de uso por arquivo; recebe Input DTO, retorna
  Output DTO ou Result de erro. Depende de ports (interfaces), não de infra.
- application/ports/: interfaces que a infra implementa.
- infra/: adapters concretos (DB, HTTP, libs). Traduz erros externos para erros
  do domínio/aplicação. Não contém regra de negócio.
- Controllers são FINOS: parseiam, chamam o caso de uso, mapeiam o Result para HTTP.
- Ao gerar código, coloque cada peça na camada certa e cite o requisito atendido.
```

## Erros comuns que a IA comete (e a regra que bloqueia)

| Erro | Regra que bloqueia |
|------|--------------------|
| SQL/query no controller | "DB só na infra/db, via port" |
| Regra de negócio no caso de uso | "regras ficam no domínio" |
| Entidade importando o ORM | "domain sem imports de framework" |
| Controller com lógica de negócio | "controllers finos" |
| Vazar entidade na resposta HTTP | "retorne DTOs, não entidades" |

## Skills relacionadas

`criar-caso-de-uso` · `criar-entidade` · `criar-value-object` · `criar-adapter` ·
`criar-endpoint` · `revisar-aderencia-arquitetural` (em [skills/](../skills/README.md)).
