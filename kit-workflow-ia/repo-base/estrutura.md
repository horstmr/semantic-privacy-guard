# Estrutura de pastas de referência

> Esqueleto que junta Clean + DDD + CQRS + SDD. Adote no nível que você escolheu
> (não crie cerimônia sem necessidade — [Módulo 03 §5](../modulos/03-arquiteturas-prontas-para-ia.md#5-qual-escolher)).

```
meu-projeto/
├── AGENTS.md                     # regras persistentes para a IA
├── .cursorrules                  # idem, formato Cursor
├── README.md
├── docs/
│   ├── specs/                    # SDD, uma pasta por feature
│   │   └── <feature>/
│   │       ├── spec.md
│   │       ├── plano.md
│   │       ├── tarefas.md
│   │       └── rastreabilidade.md
│   └── adr/                      # decisões de arquitetura (ADR-001, 002...)
├── src/
│   ├── domain/                   # PURO (sem framework)
│   │   └── <contexto>/           # um bounded context por pasta (DDD)
│   │       ├── entities/
│   │       ├── value-objects/
│   │       ├── aggregates/
│   │       ├── events/
│   │       ├── errors/
│   │       └── <contexto>.glossario.md
│   ├── application/
│   │   ├── use-cases/            # Clean: um caso de uso por arquivo
│   │   ├── commands/             # CQRS: escrita (Command + Handler)
│   │   ├── queries/              # CQRS: leitura (Query + Handler)
│   │   ├── ports/                # interfaces implementadas pela infra
│   │   ├── read-models/          # projeções otimizadas p/ leitura
│   │   └── dtos/
│   ├── infra/
│   │   ├── db/                   # adapters de persistência (implementam ports)
│   │   ├── http/                 # controllers/rotas finas
│   │   ├── external/             # SDKs, APIs de terceiros
│   │   └── config/
│   └── shared/                   # utilitários sem regra de negócio
├── tests/                        # ou testes ao lado do código (.spec)
└── .github/workflows/ci.yml      # gates: lint + testes em todo PR
```

## Mapa: onde cada skill gera código

| Skill | Gera em |
|-------|---------|
| `criar-entidade`, `criar-value-object` | `src/domain/<contexto>/` |
| `criar-caso-de-uso` | `src/application/use-cases/` |
| `criar-command` / `criar-query` | `src/application/commands` / `queries/` |
| `criar-adapter` | `src/infra/db` (ou `external/`) |
| `criar-endpoint` | `src/infra/http/` |
| `gerar-testes-ca` / `-borda` | `tests/` (ou ao lado, `.spec`) |
| `gerar-adr` | `docs/adr/` |
| `gerar-matriz-rastreabilidade` | `docs/specs/<feature>/rastreabilidade.md` |

## Níveis (escolha o seu)

- **CRUD simples:** `domain` leve + `application` + `infra`. Pule CQRS e aggregates.
- **Domínio rico:** adote DDD dentro de `domain/<contexto>/`.
- **Escala leitura/escrita:** ative `commands/` + `queries/` + `read-models/` (CQRS).

> A estrutura é um trilho para a IA — mas trilho de menos é caos e trilho demais é
> cerimônia. Comece simples; suba de nível quando a dor aparecer.
