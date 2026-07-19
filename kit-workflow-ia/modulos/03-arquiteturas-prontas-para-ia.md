# Módulo 03 — Arquiteturas Prontas para IA

> Clean Architecture, DDD e CQRS não são só "boas práticas" — no workflow com IA
> elas viram **trilhos que a IA respeita**. Fronteiras claras = o código gerado
> encaixa em vez de virar sopa.

## Objetivos

- Entender por que **arquitetura é restrição** (e por que isso ajuda a IA).
- Conhecer Clean, DDD e CQRS na versão **adaptada para IA**.
- Escolher qual usar e como declará-la para o assistente.

Guias completos e estruturas de pasta em
[`arquitetura/`](../arquitetura/README.md).

---

## 1. Por que arquitetura ajuda a IA

A IA gera o token mais provável dado o contexto. Se o contexto não diz **onde
cada coisa mora**, ela põe a regra de negócio no controller, a query no meio da
UI, e a chamada de API dentro da entidade — tudo "funciona" e tudo é dívida.

Uma arquitetura com **fronteiras explícitas** dá à IA um mapa: "isto é um caso de
uso → vai na camada de aplicação; isto é acesso a dados → vai no repositório".
O que era ambíguo vira determinístico.

> **Macete:** arquitetura é uma **válvula de escape** (Aula 8 do Prompt Eng.) no
> nível do projeto: sem ela, a IA "inventa" onde pôr o código.

---

## 2. Clean Architecture para IA

**Ideia:** dependências apontam **para dentro**; o núcleo (domínio) não conhece
frameworks. Camadas:

```
  ┌─────────────────────────────────────────┐
  │  Infra (DB, HTTP, libs)  ── adapters     │  ← a IA pluga o mundo externo aqui
  │   ┌───────────────────────────────────┐  │
  │   │  Aplicação (casos de uso)          │  │  ← orquestra; a IA implementa aqui
  │   │   ┌─────────────────────────────┐  │  │
  │   │   │  Domínio (entidades, regras) │  │  │  ← puro; a IA muda só com spec
  │   │   └─────────────────────────────┘  │  │
  │   └───────────────────────────────────┘  │
  └─────────────────────────────────────────┘
```

**Regra que você dá à IA:** "domínio não importa nada de fora; casos de uso
dependem de interfaces, não de implementações; DB/HTTP só na infra". Isso sozinho
elimina 80% do código-espaguete gerado.

---

## 3. DDD (Domain-Driven Design) para IA

**Ideia:** o código fala a **linguagem do negócio** (Ubiquitous Language) e é
organizado por **bounded contexts** (fronteiras de significado).

Para a IA, DDD dá duas coisas de ouro:

- **Glossário** — um vocabulário do domínio que vai no arquivo de regras. A IA
  usa os mesmos termos que o negócio ("Pedido", "Fatura", "Reserva"), não
  inventa sinônimos.
- **Fronteiras de contexto** — a IA sabe que "Cliente" no contexto de Vendas ≠
  "Cliente" no de Suporte, e não mistura os dois.

Blocos que você declara: *Entities, Value Objects, Aggregates, Domain Events,
Repositories, Domain Services*. (Detalhe em [`arquitetura/ddd-ia.md`](../arquitetura/ddd-ia.md).)

---

## 4. CQRS para IA

**Ideia:** separar **Commands** (escrita, mudam estado) de **Queries** (leitura,
não mudam nada). Dois caminhos, dois modelos.

Para a IA isso é maravilhoso porque cada operação tem um **molde fixo**:

- **Command** → valida → aplica regra de domínio → persiste → emite evento.
- **Query** → lê (pode ser de uma projeção otimizada) → retorna DTO.

Você dá o molde uma vez (uma skill!) e toda operação nova sai no mesmo formato —
previsível e revisável. CQRS combina naturalmente com as
[skills](../skills/README.md) `criar-command` e `criar-query`.

---

## 5. Qual escolher

| Contexto | Comece com |
|----------|-----------|
| App de porte médio, quer ordem sem cerimônia | **Clean Architecture** |
| Domínio rico e complexo, muitas regras de negócio | **Clean + DDD** |
| Leitura e escrita com necessidades bem diferentes, escala | **+ CQRS** (sobre Clean/DDD) |
| Protótipo/CRUD simples | Nenhuma cerimônia — camadas leves bastam |

Não empilhe padrões por moda. Cada um é uma restrição; use a que **remove
ambiguidade real** no seu caso. Excesso de arquitetura também vira caos — só que
caos cerimonioso.

---

## 6. Como declarar a arquitetura para a IA

No arquivo de regras do repo (`AGENTS.md`/`.cursorrules`), declare:

```
Arquitetura: Clean Architecture + DDD.
- Domínio (src/domain): entidades e regras puras. Sem imports de framework/infra.
- Aplicação (src/application): casos de uso; dependem de interfaces (ports).
- Infra (src/infra): adapters de DB/HTTP/libs implementando as ports.
- Regra de dependência: sempre para dentro. Nunca domínio → infra.
Vocabulário do domínio: Pedido, Item, Fatura, Reserva (use estes termos).
```

Com isso no contexto, cada geração da IA já nasce no lugar certo.

---

## Exercícios

1. **Mapeie seu projeto.** Desenhe as camadas do seu projeto atual. Onde há
   vazamento (regra de negócio no controller, SQL na UI)? Esses são os pontos que
   a IA vai piorar se você não declarar fronteiras.

2. **Escreva o glossário.** Liste 8 termos do seu domínio com definição de 1
   linha. Esse é o começo da Ubiquitous Language que vai no arquivo de regras.

3. **Molde CQRS.** Escreva o "molde" de um Command e de uma Query no seu projeto.
   Depois peça à IA para criar uma operação nova seguindo o molde. Saiu no padrão?

4. **Declare para a IA.** Adicione o bloco de arquitetura ao seu `AGENTS.md` e
   peça um caso de uso novo. A IA respeitou a regra de dependência?

---

## Resumo

- **Arquitetura é restrição** — e restrição é o que a IA precisa para gerar código
  que encaixa.
- **Clean**: dependências para dentro; domínio puro.
- **DDD**: linguagem do negócio + fronteiras de contexto (glossário para a IA).
- **CQRS**: separa escrita (Command) de leitura (Query) — moldes fixos = skills.
- **Declare a arquitetura** no arquivo de regras; use só o que remove ambiguidade.

**Próximo:** [Módulo 04 — Skills dentro da sua arquitetura →](./04-skills-dentro-da-arquitetura.md)
