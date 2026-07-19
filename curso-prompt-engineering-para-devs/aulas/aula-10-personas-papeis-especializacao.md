# Aula 10 — Personas, papéis e especialização situacional

> Personas não são fantasia ("finja ser um pirata"). São **interfaces de
> competência** que você pluga conforme a tarefa — Tech Lead, SRE, Security
> Reviewer. Este é o mecanismo **Personas Modulares**.

## Objetivos

- Entender persona como **interface de competência**, não enfeite.
- Compor **personas modulares** e trocá-las conforme a situação.
- Combinar múltiplas perspectivas (ex: painel de revisores) sem confundir o
  modelo.

---

## 1. Persona como interface de competência

Da Aula 1: papel só vale quando **muda a resposta**. Aqui aprofundamos: uma
persona bem definida funciona como uma **interface** no sentido de programação —
ela expõe um conjunto de prioridades, vocabulário e critérios, e você a "instancia"
quando precisa daquela competência.

Compare o mesmo pedido sob três interfaces:

```
"Revise este trecho de código."
```

- **Como Tech Lead:** foca em legibilidade, manutenção, aderência a padrões do
  time, e se a abordagem escala. Ignora microdetalhes.
- **Como SRE:** foca em falha, observabilidade, timeouts, retries, o que acontece
  às 3h da manhã em produção.
- **Como Security Reviewer:** foca em vetores de ataque, validação de entrada,
  vazamento de dados. Ignora estilo.

Mesmo código, três revisões diferentes e **complementares**. A persona não muda
quem o modelo "é" — muda **para onde ele olha**.

---

## 2. O que faz uma persona funcionar

Uma persona útil carrega **três coisas concretas** (e nenhum elogio):

1. **Perspectiva/prioridade** — o que importa e o que ignorar.
2. **Critérios** — como essa competência julga "bom".
3. **Vocabulário/nível** — o registro técnico e a profundidade esperados.

```
<persona>
Você é um Site Reliability Engineer (SRE) sênior.
Prioridade: confiabilidade em produção acima de elegância. Você assume que tudo
falha.
Critérios: todo I/O tem timeout e retry com backoff; todo erro é observável
(log/métrica); nenhum caminho deixa recurso vazando.
Ignore: estilo de código e preferências estéticas.
</persona>
```

Compare com a persona-enfeite: *"Você é um engenheiro genial e incrível."* — zero
informação sobre perspectiva, critério ou nível. Não faz trabalho.

> **Teste rápido:** troque a persona e veja se a saída muda **de forma útil**. Se
> não mudar, ela é decorativa — concretize (prioridade, critério, nível) ou
> corte.

---

## 3. Modularidade: troque a persona, não o resto do prompt

O poder do mecanismo é **modular**: você mantém a tarefa, o contexto e o formato
fixos, e **pluga** a persona conforme a situação. Isso mantém consistência e te
dá reuso.

```
<persona>{{PERSONA_ATIVA}}</persona>   ← o único bloco que troca

<tarefa>Revise o código em <codigo> e liste achados.</tarefa>
<formato>Lista: [severidade] linha N — problema. Correção: ...</formato>
<codigo>{{codigo}}</codigo>
```

Você tem uma biblioteca de personas (Tech Lead, SRE, Security, DBA, UX Writer...)
e injeta a certa. É o mesmo prompt "implementando" competências diferentes —
personas modulares como interfaces plugáveis.

---

## 4. Painel de perspectivas

Para decisões importantes, rode **várias personas** sobre o mesmo material e
combine — como um painel de revisores. Duas formas:

**Sequencial (uma persona por chamada):** você coleta a revisão do Tech Lead, do
SRE e do Security separadamente e consolida. Cada uma é focada e não se
contamina.

**Em um prompt (o modelo simula o painel):**

```
Avalie a proposta em <proposta> sob TRÊS perspectivas, uma seção cada:
### Tech Lead (manutenção e escala)
### SRE (confiabilidade em produção)
### Security (vetores de ataque)
Depois, uma seção "### Síntese" com os riscos que aparecem em mais de uma
perspectiva — esses são os prioritários.
```

A síntese que cruza perspectivas costuma revelar os problemas que **importam de
verdade** (aparecem em vários ângulos), separando-os dos detalhes de um ângulo
só. É parente do painel de julgamento que você viu no auto-refinamento (Aula 9).

> **Cuidado:** muitas personas de uma vez num único prompt confundem o modelo e
> diluem cada perspectiva. Para análise séria, prefira sequencial (uma por
> chamada) ou poucas personas bem separadas por seção.

---

## 5. Anti-padrões

- **Persona-enfeite.** "Você é brilhante." Não muda a saída.
- **Persona de fantasia.** "Finja ser o Steve Jobs" — carrega ruído estético, não
  competência. Prefira o papel funcional (Tech Lead, SRE).
- **Trocar tudo junto com a persona.** Perde reuso e consistência; troque só o
  bloco de persona.
- **Personas demais num prompt.** Diluem-se; o modelo mistura as vozes.
- **Persona sem critério.** "Você é um revisor de segurança" sem dizer o que ele
  procura — vago demais para render.

---

## Exercícios

1. **Três interfaces, um código.** Pegue um trecho de código e revise-o sob
   Tech Lead, SRE e Security (personas concretas, com prioridade e critérios).
   As três revisões foram diferentes e complementares?

2. **Concretize uma persona-enfeite.** Pegue "você é um especialista incrível em
   X" e reescreva com perspectiva + critérios + nível. Rode as duas. A concreta
   mudou a saída de forma útil?

3. **Biblioteca modular.** Escreva 3 personas reutilizáveis e um prompt-base onde
   só o bloco de persona troca. Rode a mesma tarefa plugando cada uma. Ganhou
   reuso e consistência?

4. **Painel + síntese.** Rode um painel de 3 perspectivas sobre uma decisão de
   design e peça a síntese dos riscos que aparecem em mais de uma. A síntese
   apontou o que realmente importa?

Soluções em [`../exercicios/solucoes.md`](../exercicios/solucoes.md).

---

## Resumo

- Persona é **interface de competência**: perspectiva + critérios + nível — nunca
  elogio nem fantasia.
- **Modularidade:** mantenha tarefa/contexto/formato fixos e **pluge** a persona
  conforme a situação; tenha uma biblioteca delas.
- **Painel de perspectivas** (Tech Lead + SRE + Security) com **síntese** revela
  os riscos que importam — os que cruzam vários ângulos.
- Poucas personas bem definidas por vez; muitas juntas se diluem.

**Próximo:** [Aula 11 — Design de saída e controle de verbosidade →](./aula-11-design-de-saida-e-verbosidade.md)
