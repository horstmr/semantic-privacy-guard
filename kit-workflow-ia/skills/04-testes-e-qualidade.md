# Skills — Testes & Qualidade

> Derivam testes da **spec** (não do código, para não "testar o bug junto") e
> cobrem o que a IA tende a esquecer.

---

## gerar-testes-ca
**Quando usar:** transformar critérios de aceite em testes (o coração da rastreabilidade).
**Entradas:** critérios de aceite (CA1...).
**Saída:** testes que citam cada CA, no framework do projeto.

**Prompt:**
```
Escreva testes automatizados para os critérios de aceite <cas>, no framework de
testes do projeto. Regras:
- Um teste (ou grupo) por critério; o NOME do teste cita o CA (ex.: 'CA2: cupom expirado rejeita').
- Derive o teste do CRITÉRIO (Given/When/Then), não do código existente.
- Cubra o resultado esperado E o erro esperado de cada CA.
- Use arrange/act/assert claros; sem lógica condicional no teste.
Saída: o arquivo de teste.
<cas>{{criterios}}</cas>
```

---

## gerar-testes-borda
**Quando usar:** cobrir os casos que quebram em produção e a IA esquece.
**Entradas:** a função/caso de uso.
**Saída:** testes de borda por categoria.

**Prompt:**
```
Gere testes de borda para <alvo>: entrada vazia/nula, limites (0, 1, máximo),
formato/tipo inválido, concorrência (se aplicável), falha de dependência externa
(timeout/erro), e entrada maliciosa. Para cada, afirme o comportamento seguro
esperado (erro explícito, não crash silencioso).
Saída: um teste por caso, nomeado pelo caso.
<alvo>{{funcao_ou_caso_de_uso}}</alvo>
```

---

## gerar-testes-propriedade
**Quando usar:** funções com muitas entradas onde exemplos não bastam (parsers, cálculos, serialização).
**Entradas:** a função + suas invariantes.
**Saída:** testes de propriedade (property-based) com as invariantes.

**Prompt:**
```
Escreva testes de PROPRIEDADE (property-based) para <funcao>, usando a lib de
property testing do ecossistema. Identifique invariantes que valem para QUALQUER
entrada válida, por exemplo: round-trip (parse(format(x)) == x), idempotência,
comutatividade, limites preservados. Gere geradores de entrada adequados.
Saída: os testes de propriedade + a lista de invariantes testadas.
<funcao>{{funcao_e_invariantes}}</funcao>
```

---

## cobrir-lacunas-de-teste
**Quando usar:** achar o que a suíte atual NÃO testa.
**Entradas:** o código + os testes existentes.
**Saída:** lacunas de cobertura (caminhos, ramos, casos) e os testes que faltam.

**Prompt:**
```
Compare o código <codigo> com os testes <testes> e liste as LACUNAS: ramos/caminhos
não exercitados, casos de erro não verificados, requisitos sem teste. Priorize por
risco (o que dói mais se quebrar). Para cada lacuna, descreva o teste que falta.
Não conte cobertura de linha como garantia — foque em comportamento não verificado.
Saída: tabela lacuna | risco | teste faltante.
<codigo>{{codigo}}</codigo> <testes>{{testes}}</testes>
```

---

## gerar-fixtures
**Quando usar:** criar dados de teste realistas e reutilizáveis.
**Entradas:** as entidades/DTOs envolvidos.
**Saída:** builders/factories de fixtures com defaults válidos e variações.

**Prompt:**
```
Crie fixtures/factories para <entidades> com o padrão builder: um default VÁLIDO
e métodos para variar campos (incl. estados inválidos para testes negativos).
Evite acoplar fixture a detalhes de persistência. Mantенha os dados coerentes com
as invariantes do domínio.
Saída: os builders + exemplos de uso.
<entidades>{{entidades_ou_dtos}}</entidades>
```

---

## revisar-testes
**Quando usar:** garantir que os testes (gerados por IA) são bons de verdade.
**Entradas:** os testes.
**Saída:** problemas nos testes (frágeis, tautológicos, sem asserção real) e correções.

**Prompt:**
```
Revise os testes em <testes> com olhar crítico. Aponte: testes tautológicos (que
sempre passam), asserções fracas/ausentes, testes acoplados a implementação (quebram
em refactor válido), falta de casos de erro, e testes que "testam o mock". Para cada
problema, sugira a correção.
Um teste bom falha quando o comportamento quebra — verifique se estes fazem isso.
<testes>{{testes}}</testes>
```

---

← [Implementação](./03-implementacao.md) · [Catálogo](./README.md) · [Review & Refatoração →](./05-review-e-refatoracao.md)
