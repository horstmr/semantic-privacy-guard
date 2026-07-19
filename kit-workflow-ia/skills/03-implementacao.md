# Skills — Implementação

> Geram código **na camada certa**, ancorado na arquitetura. Uma tarefa por vez
> (Módulo 05). Todas assumem a arquitetura declarada no `AGENTS.md`.

---

## criar-caso-de-uso
**Quando usar:** implementar uma operação de aplicação (Clean Architecture).
**Entradas:** requisito + critérios de aceite.
**Saída:** o caso de uso + o teste, na camada de aplicação.

**Prompt:**
```
Crie um caso de uso para o requisito <req> seguindo a Clean Architecture do projeto:
- Local: src/application/use-cases/{{contexto}}/
- Recebe Input DTO, retorna Output DTO ou um Result de erro (não lance exceção p/ fluxo esperado).
- Depende de PORTS (interfaces em src/application/ports), nunca de infra concreta.
- Regras de negócio ficam no domínio; aqui só orquestração.
- Trate as bordas da spec; erros explícitos com códigos.
Gere também o teste cobrindo os critérios <cas>, cada teste citando o CA.
Saída: (1) arquivo do caso de uso, (2) arquivo de teste.
<req>{{requisito}}</req> <cas>{{criterios}}</cas>
```

---

## criar-command
**Quando usar:** operação de escrita em CQRS.
**Entradas:** requisito + regra de negócio.
**Saída:** Command + Handler + teste, no molde CQRS.

**Prompt:**
```
Crie um Command CQRS para <req>. Molde:
- Command: objeto imutável com os dados de entrada (validado).
- Handler: valida → carrega aggregate → aplica regra de domínio → persiste via
  repositório (port) → emite Domain Event se aplicável.
- Não retorna dados de leitura (só sucesso/erro/id). Leitura é Query.
- Erros de domínio explícitos (ex.: CupomInvalido), não exceções genéricas.
Gere Command, Handler e teste (cobrindo {{cas}}).
<req>{{requisito}}</req> <cas>{{criterios}}</cas>
```

---

## criar-query
**Quando usar:** operação de leitura em CQRS.
**Entradas:** o dado a ler + o formato de retorno.
**Saída:** Query + Handler retornando DTO de leitura.

**Prompt:**
```
Crie uma Query CQRS para ler <o_que>. Molde:
- Query: parâmetros de busca/paginação.
- Handler: lê de uma projeção/read model otimizada (NÃO reusa o aggregate de escrita).
- Retorna um DTO de leitura enxuto (só os campos que a UI precisa), nunca a entidade.
- Sem efeitos colaterais (leitura pura).
Gere Query, Handler e teste. Trate resultado vazio com valor explícito.
<o_que>{{consulta}}</o_que>
```

---

## criar-entidade
**Quando usar:** modelar uma entidade de domínio com suas invariantes.
**Entradas:** o conceito de negócio + suas regras.
**Saída:** entidade pura, com invariantes protegidas, sem dependência de infra.

**Prompt:**
```
Crie a entidade de domínio <nome> (camada src/domain). Regras:
- Encapsula estado; invariantes protegidas no construtor e nos métodos (não deixe
  criar estado inválido).
- Métodos expressam ações do negócio na linguagem ubíqua (ex.: pedido.confirmar()).
- ZERO imports de framework/infra/ORM. Domínio é puro.
- Erros de invariante são explícitos (ex.: lançar DomainError com código).
Gere a entidade + testes das invariantes (incluindo tentativas inválidas).
<nome>{{entidade_e_regras}}</nome>
```

---

## criar-value-object
**Quando usar:** um conceito sem identidade, imutável e validado (Email, CPF, Dinheiro, Cupom).
**Entradas:** o conceito + regras de validação.
**Saída:** value object imutável, autovalidado, com igualdade por valor.

**Prompt:**
```
Crie um Value Object <nome> (src/domain): imutável, validado na construção,
igualdade por VALOR (não por referência). Rejeite entradas inválidas na criação
com erro explícito. Sem dependências de infra. Inclua os métodos de negócio
pertinentes (ex.: Dinheiro.somar, Cupom.estaValido(data)).
Gere o VO + testes (válidos e inválidos, igualdade).
<nome>{{conceito_e_regras}}</nome>
```

---

## criar-adapter
**Quando usar:** implementar uma port (DB, HTTP, fila) na camada de infra.
**Entradas:** a interface (port) + a tecnologia.
**Saída:** o adapter concreto implementando a port, isolado na infra.

**Prompt:**
```
Implemente um adapter para a port <port> usando <tecnologia> (ex.: Postgres,
Redis, um SDK). Regras:
- Local: src/infra/{{tipo}}/. Implementa EXATAMENTE a interface da port.
- Traduz erros da tecnologia para erros do domínio/aplicação (não vaze exceções do driver).
- Não contém regra de negócio — só o acesso ao recurso externo.
- Mapeia entre modelo de persistência e entidade/DTO (não vaze o schema do DB).
Gere o adapter + teste de integração (pode usar o recurso real ou um duplo).
<port>{{interface}}</port>
```

---

## criar-endpoint
**Quando usar:** expor um caso de uso/command/query via HTTP.
**Entradas:** o caso de uso/command + a rota.
**Saída:** o controller/handler HTTP fino, validando entrada e chamando a aplicação.

**Prompt:**
```
Crie o endpoint HTTP para <operacao> (camada de infra/adapters). Regras:
- Controller FINO: valida/parseia a request, chama o caso de uso/command/query,
  mapeia o Result para status HTTP (200/201/400/404/409...).
- Sem regra de negócio no controller. Erros de domínio → status + corpo de erro padronizado.
- Valide a entrada (schema) e trate corpo inválido com 400 explícito.
Gere o endpoint + teste (caminho feliz + erros mapeados).
<operacao>{{caso_de_uso}}</operacao>
```

---

## implementar-tarefa
**Quando usar:** skill genérica para uma tarefa da decomposição SDD, quando não há molde específico.
**Entradas:** a tarefa + requisitos que atende.
**Saída:** a implementação + teste, na camada correta, citando os requisitos.

**Prompt:**
```
Implemente APENAS a tarefa <tarefa> (não faça além dela). Regras:
- Respeite a arquitetura declarada; ponha o código na camada certa.
- Atenda exatamente os requisitos <reqs>; trate as bordas relevantes.
- Gere o teste correspondente citando os critérios de aceite.
- Se faltar informação para decidir algo, PARE e pergunte — não invente.
- Comente no código qual requisito cada parte atende (// Rn).
Saída: diff pequeno (arquivos alterados) + testes.
<tarefa>{{tarefa}}</tarefa> <reqs>{{requisitos}}</reqs>
```

---

← [Arquitetura & Design](./02-arquitetura-e-design.md) · [Catálogo](./README.md) · [Testes & Qualidade →](./04-testes-e-qualidade.md)
