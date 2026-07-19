# Skills — Planejamento & Spec

> Transformam uma ideia solta numa **spec executável e revisável**. Base do
> [SDD (Módulo 02)](../modulos/02-spec-driven-development.md).

---

## afiar-spec
**Quando usar:** você tem um rascunho de spec/feature e quer remover ambiguidade antes de gerar código.
**Entradas:** o rascunho da spec.
**Saída:** lista de ambiguidades, requisitos contraditórios, casos de borda faltando, e perguntas abertas.

**Prompt:**
```
Você é um engenheiro sênior revisando uma especificação ANTES da implementação.
Sua tarefa é CRITICAR, não implementar. Analise a spec em <spec> e aponte:
1. Ambiguidades (frases que podem ser lidas de 2 formas).
2. Requisitos contraditórios ou que se sobrepõem.
3. Casos de borda ausentes (vazio, erro, concorrência, limites).
4. Critérios de aceite que não são testáveis.
5. Perguntas que precisam de resposta antes de codar.
Não invente requisitos novos; aponte o que falta decidir.
Saída: uma lista por categoria; se uma categoria estiver ok, escreva "ok".
<spec>{{rascunho}}</spec>
```

---

## gerar-criterios-aceite
**Quando usar:** os requisitos existem mas faltam critérios de aceite testáveis.
**Entradas:** requisitos numerados.
**Saída:** critérios no formato Given/When/Then, numerados (CA1...), ligados a requisitos.

**Prompt:**
```
Dado os requisitos em <reqs>, escreva critérios de aceite testáveis no formato
Given/When/Then (Dado/Quando/Então). Regras:
- Numere CA1, CA2... e indique qual requisito (Rn) cada um cobre.
- Cubra o caminho feliz E os erros/bordas de cada requisito.
- Cada critério deve ser verificável por um teste automatizado (evite subjetivo).
Saída: lista de CAs, cada um com (requisito coberto).
<reqs>{{requisitos}}</reqs>
```

---

## descobrir-casos-de-borda
**Quando usar:** antes de implementar, para não esquecer os casos que quebram em produção.
**Entradas:** descrição da feature ou função.
**Saída:** lista de casos de borda por categoria, com o comportamento esperado sugerido.

**Prompt:**
```
Liste os casos de borda para <feature>, agrupados em: entrada vazia/nula,
tipos/formatos inválidos, limites (0, 1, máximo, overflow), concorrência,
falhas externas (timeout, indisponível), e segurança (entrada maliciosa).
Para cada caso, sugira o comportamento esperado (com válvula de escape, ex.:
retornar erro X, não lançar exceção silenciosa).
Saída: tabela caso | comportamento esperado.
<feature>{{descricao}}</feature>
```

---

## decompor-tarefas
**Quando usar:** a spec está pronta e você quer quebrá-la em tarefas pequenas e revisáveis.
**Entradas:** spec com requisitos numerados.
**Saída:** lista ordenada de tarefas pequenas, cada uma ligada a requisito(s), com dependências.

**Prompt:**
```
Decomponha a implementação da spec <spec> em tarefas PEQUENAS e independentes
(cada uma revisável em um PR curto). Regras:
- Cada tarefa cita o(s) requisito(s) que atende (Rn).
- Uma tarefa não deve misturar camadas de arquitetura diferentes sem necessidade.
- Ordene por dependência (o que precisa vir antes).
- Marque tarefas que exigem decisão de arquitetura (candidatas a ADR).
Saída: lista ordenada: [Tarefa] — (Rn) — depende de [.] — [precisa ADR? s/n].
<spec>{{spec}}</spec>
```

---

## estimar-risco
**Quando usar:** priorizar o que revisar com mais cuidado.
**Entradas:** lista de tarefas ou a spec.
**Saída:** cada item classificado por risco (baixo/médio/alto) com justificativa.

**Prompt:**
```
Classifique cada item de <itens> por risco de implementação com IA: baixo, médio
ou alto. Considere: ambiguidade, impacto em dados/segurança, irreversibilidade,
e quão fácil é VERIFICAR o resultado. Para os de alto risco, sugira a mitigação
(mais spec, revisão especialista, teste extra, HITL).
Saída: tabela item | risco | motivo | mitigação.
<itens>{{tarefas_ou_spec}}</itens>
```

---

## gerar-perguntas-de-esclarecimento
**Quando usar:** a demanda chegou vaga (ticket, mensagem) e você precisa desambiguar com o solicitante.
**Entradas:** o pedido original.
**Saída:** as 5–8 perguntas mais importantes, ordenadas por impacto na implementação.

**Prompt:**
```
Recebi este pedido: <pedido>. Antes de especificar, liste as perguntas que EU
preciso fazer ao solicitante para remover ambiguidade — ordenadas pelo impacto
que a resposta tem na implementação. Máximo 8. Para cada, explique em 1 frase por
que a resposta muda o código. Não invente respostas.
<pedido>{{pedido}}</pedido>
```

---

## converter-bug-em-spec
**Quando usar:** um bug reportado — transforme em spec de correção + teste de regressão.
**Entradas:** o relato do bug.
**Saída:** causa provável, requisito da correção, e um critério de aceite que vira teste de regressão.

**Prompt:**
```
Dado o bug em <bug>, produza: (1) resumo do comportamento observado vs. esperado;
(2) causas prováveis ordenadas; (3) o requisito da correção (Rn); (4) um critério
de aceite Given/When/Then que, virando teste, PREVINE a regressão.
Não proponha a correção ainda — só a spec do conserto e o teste que a prova.
<bug>{{relato}}</bug>
```

---

← [Voltar ao catálogo](./README.md) · Próxima fase: [Arquitetura & Design →](./02-arquitetura-e-design.md)
