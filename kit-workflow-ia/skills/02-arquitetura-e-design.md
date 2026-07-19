# Skills — Arquitetura & Design

> Decidem **como** e onde o código vai morar, respeitando a
> [arquitetura declarada (Módulo 03)](../modulos/03-arquiteturas-prontas-para-ia.md).

---

## propor-abordagem
**Quando usar:** spec pronta, antes de codar — para decidir a abordagem técnica.
**Entradas:** a spec + a arquitetura do projeto.
**Saída:** 2–3 abordagens com prós/contras e uma recomendação justificada.

**Prompt:**
```
Dada a spec <spec> e a arquitetura declarada do projeto (Clean/DDD/CQRS conforme
AGENTS.md), proponha 2–3 abordagens técnicas DISTINTAS de implementação.
Para cada: componentes afetados (entidade, caso de uso, port, adapter),
prós, contras e riscos. Depois recomende uma, justificando pelo custo/risco.
NÃO escreva código ainda. Respeite a regra de dependência (nunca domínio→infra).
Saída: abordagens A/B/C + recomendação.
<spec>{{spec}}</spec>
```

---

## gerar-adr
**Quando usar:** registrar uma decisão de arquitetura relevante.
**Entradas:** a decisão + alternativas consideradas.
**Saída:** um ADR no template padrão (contexto, decisão, alternativas, consequências).

**Prompt:**
```
Escreva um ADR (Architecture Decision Record) para a decisão em <decisao>.
Use o template:
# ADR-{{n}}: <título>
## Status: proposto
## Contexto: qual problema/força motivou a decisão
## Decisão: o que foi decidido (imperativo)
## Alternativas consideradas: com por que foram descartadas
## Consequências: positivas, negativas e o que passa a ser restrição
Seja conciso e factual; registre trade-offs, não venda a decisão.
<decisao>{{decisao_e_alternativas}}</decisao>
```

---

## modelar-contexto-ddd
**Quando usar:** organizar um domínio complexo em blocos DDD.
**Entradas:** descrição do domínio/feature.
**Saída:** entidades, value objects, aggregates, eventos de domínio e a linguagem ubíqua.

**Prompt:**
```
Modele o domínio de <dominio> em blocos DDD. Identifique:
- Entities (com identidade) e Value Objects (sem identidade, imutáveis);
- Aggregates e a raiz de cada um (fronteira de consistência);
- Domain Events relevantes;
- Repositories necessários (por aggregate);
- A Ubiquitous Language: glossário de termos do negócio (para o AGENTS.md).
Não misture termos técnicos com os do negócio; use a linguagem do domínio.
Saída: seções por bloco + glossário final.
<dominio>{{descricao}}</dominio>
```

---

## desenhar-fronteiras
**Quando usar:** definir onde termina um módulo/contexto e começa outro.
**Entradas:** os módulos/contextos candidatos.
**Saída:** fronteiras (bounded contexts), o que cada um possui, e os contratos entre eles.

**Prompt:**
```
Dado os módulos/contextos em <contextos>, defina as fronteiras entre eles:
- O que cada contexto POSSUI (dados, regras) e o que é dele exclusivo;
- Onde o mesmo termo significa coisas diferentes (ex.: "Cliente" em Vendas vs Suporte);
- Os contratos de comunicação entre contextos (eventos, DTOs, anti-corruption layer);
- O que NÃO deve cruzar a fronteira.
Saída: um contexto por seção + tabela de contratos entre eles.
<contextos>{{contextos}}</contextos>
```

---

## revisar-aderencia-arquitetural
**Quando usar:** checar se um código (gerado ou não) respeita a arquitetura.
**Entradas:** o código + a arquitetura declarada.
**Saída:** violações da regra de dependência e do posicionamento por camada.

**Prompt:**
```
Verifique se o código em <codigo> adere à arquitetura declarada (Clean/DDD/CQRS).
Aponte violações: regra de negócio fora do domínio, acesso a DB/HTTP fora da
infra, domínio importando framework, camada dependendo "para fora", DTO vazando
entidade. Para cada violação: onde está e como corrigir (mover para onde).
Se estiver aderente, diga "aderente".
<codigo>{{codigo}}</codigo>
```

---

## escolher-padrao
**Quando usar:** decidir se vale aplicar Clean/DDD/CQRS (ou nada) numa parte do sistema.
**Entradas:** o contexto/tamanho do módulo.
**Saída:** recomendação com justificativa e o custo de cada opção.

**Prompt:**
```
Para o módulo descrito em <modulo>, recomende o nível de arquitetura adequado:
(a) camadas leves/CRUD simples, (b) Clean Architecture, (c) Clean + DDD,
(d) + CQRS. Justifique pela complexidade REAL do domínio e pela escala.
Alerte se houver over-engineering (cerimônia sem benefício). 
Saída: recomendação + por quê + o que seria exagero aqui.
<modulo>{{contexto}}</modulo>
```

---

## mapear-dependencias
**Quando usar:** entender/registrar as dependências de um módulo antes de mexer.
**Entradas:** o módulo/arquivo.
**Saída:** dependências de entrada e saída, e riscos de acoplamento.

**Prompt:**
```
Mapeie as dependências de <alvo>: o que ele importa/usa (saída) e quem depende
dele (entrada). Aponte acoplamentos perigosos (domínio dependendo de infra,
dependência circular, dependência de detalhe em vez de interface).
Saída: lista "depende de" / "é usado por" + riscos.
<alvo>{{modulo}}</alvo>
```

---

← [Planejamento & Spec](./01-planejamento-e-spec.md) · [Catálogo](./README.md) · [Implementação →](./03-implementacao.md)
