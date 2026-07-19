# Módulo 05 — Código Previsível, Revisável e Rastreável

> As três propriedades que separam "IA acelera o time" de "IA enterra o time em
> dívida". Este módulo é sobre os **guardrails** que garantem as três.

## Objetivos

- Tornar a geração **previsível** (mesma entrada → mesmo padrão).
- Tornar o código **revisável** (PR pequeno, contra a spec).
- Tornar tudo **rastreável** (requisito ↔ código ↔ teste).

---

## 1. Previsível

Previsibilidade vem de **remover graus de liberdade** da IA:

- **`temperature` baixa** para geração de código (Aula 2 do Prompt Eng.).
- **Arquitetura declarada** (Módulo 03) — onde tudo mora.
- **Skills com saída fixa** (Módulo 04) — formato constante.
- **Spec como alvo** (Módulo 02) — o quê exato.

Teste de previsibilidade: rode a mesma skill/spec duas vezes. A **estrutura** do
resultado deve ser idêntica (nomes de arquivo, camadas, formato). Se variar, algo
está solto — aperte a spec ou a skill.

---

## 2. Revisável

O maior inimigo da revisão é o **PR gigante**. IA gera rápido demais; se você
deixar, ela produz 2.000 linhas que ninguém revisa de verdade — e "aprovado
porque compila" é dívida disfarçada.

Guardrails de revisibilidade:

- **Uma tarefa por vez** (Módulo 02) → PRs pequenos e focados.
- **Revisão contra a spec**, não "no olho": cada requisito foi atendido? use a
  skill `revisar-contra-spec`.
- **Pipeline autor-revisor** (Aula 9 do Prompt Eng.): a IA revisa o próprio
  código com uma rubrica antes de você olhar — mas **você tem a palavra final**.
- **Rubrica de review de código de IA** (abaixo).

### Rubrica: revisando código gerado por IA

```
[ ] Atende TODOS os requisitos da spec? (liste R1..Rn e marque)
[ ] Respeita a arquitetura? (camada certa, regra de dependência)
[ ] Casos de borda da spec tratados? (vazio, erro, concorrência)
[ ] Testes cobrem os critérios de aceite? (CA1..CAn → testes)
[ ] Sem "código morto" ou libs novas injetadas sem justificar?
[ ] Sem alucinação de API? (funções/flags que não existem)
[ ] Erros tratados de forma explícita (não engolidos)?
[ ] Legível para quem não viu o prompt?
```

> **Regra:** só gere o que você consegue revisar. Se o PR passou de revisável,
> quebre em tarefas menores (volte ao Módulo 02).

---

## 3. Rastreável

Rastreabilidade é conseguir andar, nos dois sentidos, entre **requisito ↔ código
↔ teste**. Ela paga em três momentos: no review, no bug, e na auditoria.

Ferramentas do kit:

- **Requisitos numerados** (R1...) e **critérios de aceite** (CA1...) na spec.
- **Referências no código e nos testes**: `// implementa R3` / `test('CA2: ...')`.
- **Matriz de rastreabilidade** (template no
  [SDD](../spec-driven-development/README.md)): tabela `Requisito → arquivo/função → teste`.
- **ADRs** para decisões: *por que* foi feito assim fica registrado.
- **Mensagens de commit/PR** que citam a spec e os requisitos.

```
| Req | Descrição              | Implementação                  | Teste            |
|-----|------------------------|--------------------------------|------------------|
| R1  | Bloquear carrinho vazio| checkout.usecase.ts:validate() | checkout.spec:CA1|
| R2  | Aplicar cupom          | apply-coupon.usecase.ts        | coupon.spec:CA3  |
```

Num bug de produção, você vai do sintoma → teste → requisito → decisão (ADR) em
minutos. Sem isso, é arqueologia.

---

## 4. Onde a IA ajuda a manter as três

- **Previsível**: skills e arquitetura declarada.
- **Revisável**: `revisar-contra-spec`, `gerar-rubrica-review`, autor-revisor.
- **Rastreável**: `gerar-matriz-rastreabilidade`, `gerar-adr`,
  `mensagem-commit-rastreavel` (todas no [catálogo](../skills/README.md)).

A IA mantém a papelada de rastreabilidade em dia — o que humanos odeiam fazer e
por isso não fazem. Aqui a IA vira aliada da governança, não fonte de caos.

---

## 5. Anti-padrões

- **PR gigante gerado de uma vez** → revisão teatral, dívida real.
- **"Compilou, aprovei"** → sem checar contra a spec.
- **Código sem referência a requisito** → rastreabilidade zero.
- **Decisão importante sem ADR** → daqui a 3 meses ninguém sabe o porquê.
- **Confiar no autorreview da IA sem revisão humana** → a IA aprova a si mesma.

---

## Exercícios

1. **Teste de previsibilidade.** Rode a mesma skill duas vezes. A estrutura saiu
   idêntica? Onde variou, o que estava solto?

2. **Revise com rubrica.** Pegue um trecho gerado por IA e passe a rubrica da
   seção 2, item a item. Quantos itens falharam? (Quase sempre pega algo.)

3. **Monte a matriz.** Para uma feature, preencha a matriz
   requisito → código → teste. Algum requisito sem teste? Algum código sem
   requisito (código órfão = suspeito)?

4. **Escreva um ADR.** Registre uma decisão técnica recente no template de ADR.
   Daqui a um tempo, seu "eu futuro" agradece.

---

## Resumo

- **Previsível**: temperature baixa + arquitetura declarada + skills + spec.
- **Revisável**: uma tarefa por vez + revisão contra a spec + rubrica + autor-revisor
  com palavra final humana.
- **Rastreável**: requisitos numerados + referências no código/teste + matriz + ADRs.
- Só gere o que você consegue revisar. A IA mantém a rastreabilidade que humanos
  evitam.

**Próximo:** [Módulo 06 — Do zero ao projeto real →](./06-do-zero-ao-projeto-real.md)
