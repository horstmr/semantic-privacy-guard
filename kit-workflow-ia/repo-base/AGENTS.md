<!-- AGENTS.md — regras persistentes do repositório para assistentes de IA.
     Cole na raiz do projeto. Para Cursor use .cursorrules; para Claude Code, CLAUDE.md;
     para Copilot, .github/copilot-instructions.md. Preencha os <placeholders>. -->

# Regras do projeto para IA

Você é um par de programação neste repositório. Siga estas regras em TODA geração.
Quando faltar informação para decidir algo, **pare e pergunte** — não invente.

## Stack
- Linguagem/versão: <ex.: TypeScript 5.x / Node 20>
- Framework: <ex.: NestJS / Express / Fastify>
- Persistência: <ex.: PostgreSQL + Prisma>
- Testes: <ex.: Vitest / Jest>
- Lint/format: <ex.: ESLint + Prettier> (siga a config do repo; não a altere)

## Arquitetura
<Escolha uma e cole o bloco de regras do guia correspondente:
 arquitetura/clean-architecture-ia.md, ddd-ia.md, cqrs-ia.md.>

- Padrão: <ex.: Clean Architecture + DDD (+ CQRS onde a leitura difere da escrita)>
- Regra de dependência: SEMPRE para dentro (infra → application → domain).
  NUNCA domain → infra. Domínio é puro (zero imports de framework/ORM/HTTP).
- Onde mora cada coisa:
  - `src/domain/`: entidades, value objects, regras, eventos, erros de domínio.
  - `src/application/`: casos de uso / commands / queries; ports (interfaces); DTOs.
  - `src/infra/`: adapters (DB, HTTP, libs), controllers finos, config.
- Controllers são finos: parseiam, chamam a aplicação, mapeiam o Result para HTTP.

## Linguagem ubíqua (glossário do domínio)
> Use EXATAMENTE estes termos no código e nas mensagens. Não invente sinônimos.
- <Pedido>: <definição de 1 linha>
- <Fatura>: <...>
- <...>

## Convenções
- Nomes: <ex.: arquivos kebab-case; classes PascalCase; casos de uso VerboSubstantivo>.
- Erros: explícitos, com código (ex.: `DomainError('CUPOM_INVALIDO')`). Nunca
  engula exceção nem use fluxo por exceção para casos esperados (use Result).
- Saída de funções que podem falhar: `Result<Ok, Erro>` (ou o padrão do projeto).
- Sem `any` / tipos frouxos sem justificativa.
- Comentários: só onde a intenção não é óbvia; cite o requisito (`// R3`).

## Fluxo de trabalho (SDD)
- Trabalhe orientado a spec: implemente contra `docs/specs/<feature>/spec.md`.
- **Uma tarefa por vez** (PR pequeno e revisável). Não gere a feature inteira de uma vez.
- Todo código novo vem com **teste** cobrindo os critérios de aceite (cite o CA).
- Cite os requisitos atendidos no código e na mensagem de commit.

## Proibições (NUNCA faça)
- Acessar DB/HTTP fora da camada de infra.
- Pôr regra de negócio no controller ou no adapter.
- Introduzir uma dependência/lib nova sem justificar (e perguntar).
- Vazar entidade de domínio ou schema do banco na resposta HTTP (use DTOs).
- Gerar código sem teste, ou "consertar" testes só para passar.
- Alucinar APIs: se não tem certeza que uma função/flag existe, verifique ou pergunte.

## Como entregar
- Em **diffs pequenos**, com os testes junto.
- Explique brevemente o que mudou e por quê, citando requisitos.
- Se o pedido está ambíguo, faça as perguntas antes de codar.
