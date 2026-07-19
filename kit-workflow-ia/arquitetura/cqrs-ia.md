# CQRS para IA

> Separar **Commands** (escrita, mudam estado) de **Queries** (leitura, não mudam
> nada). Cada operação ganha um **molde fixo** — e molde fixo é exatamente o que
> torna a geração por IA previsível.

## A ideia

```
        ┌── COMMAND ──► valida ► regra de domínio ► persiste ► emite evento
Request ┤   (escrita)                                          (sucesso/erro/id)
        └── QUERY ────► lê (read model otimizado) ► retorna DTO de leitura
            (leitura)
```

- **Command** muda estado e **não retorna dados de leitura** (só sucesso/erro/id).
- **Query** lê e **não tem efeito colateral**; pode usar uma projeção otimizada
  para leitura (read model), separada do modelo de escrita.

## Por que isso ajuda a IA

Cada tipo de operação tem **um roteiro único**. Você dá o molde uma vez (vira as
skills `criar-command` e `criar-query`) e **toda operação nova sai idêntica em
estrutura** — previsível para gerar, trivial para revisar.

| | Command | Query |
|---|---------|-------|
| Muda estado? | Sim | Não |
| Retorna | sucesso / erro / id | DTO de leitura |
| Modelo | de escrita (aggregate) | de leitura (projeção/DTO) |
| Skill | `criar-command` | `criar-query` |

## Estrutura de pastas (sobre Clean/DDD)

```
src/application/
├── commands/
│   └── <contexto>/
│       ├── aplicar-cupom.command.ts
│       └── aplicar-cupom.handler.ts
├── queries/
│   └── <contexto>/
│       ├── listar-pedidos.query.ts
│       └── listar-pedidos.handler.ts
└── read-models/            # projeções otimizadas para leitura
```

## Bloco de regras para o AGENTS.md

```
## Padrão: CQRS
- Operações de ESCRITA são Commands: <Nome>Command (dados imutáveis, validados) +
  <Nome>Handler (valida → carrega aggregate → aplica regra → persiste → emite evento).
  Command NÃO retorna dados de leitura (só sucesso/erro/id).
- Operações de LEITURA são Queries: <Nome>Query (filtros/paginação) + Handler que
  lê de um read model e retorna um DTO enxuto. SEM efeito colateral.
- Nunca reuse o aggregate de escrita para responder leitura (use projeção/DTO).
- Um Command/Query por arquivo; nome no domínio do negócio.
```

## Quando NÃO usar CQRS

CQRS adiciona cerimônia (dois caminhos, às vezes dois modelos e sincronização).
Use quando **leitura e escrita têm necessidades genuinamente diferentes** (escala
de leitura, consultas complexas, muitos eventos). Para CRUD simples, é
over-engineering — camadas leves bastam ([Módulo 03 §5](../modulos/03-arquiteturas-prontas-para-ia.md#5-qual-escolher)).

> **Cuidado com Event Sourcing:** CQRS **não** exige event sourcing. Comece só com
> a separação command/query; só adote event sourcing se o domínio realmente pedir
> (auditoria total, replay). Empilhar os dois "porque sim" é receita de caos
> cerimonioso.

## Skills relacionadas

`criar-command` · `criar-query` · `criar-endpoint` (em [skills/](../skills/README.md)).
