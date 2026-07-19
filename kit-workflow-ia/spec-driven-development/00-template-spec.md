<!-- TEMPLATE DE SPEC — copie para docs/specs/<feature>/spec.md e preencha.
     Depois rode a skill `afiar-spec` para a IA criticar antes de codar. -->

# Spec: <nome da feature>

- **Autor:** <você>  · **Data:** <AAAA-MM-DD>  · **Status:** rascunho | aprovada
- **Contexto de arquitetura:** <bounded context / módulo afetado>

## 1. Contexto / Problema
<Por que esta feature existe, para quem, e qual dor resolve. 2–4 frases.
Sem solução ainda — só o problema.>

## 2. Objetivo
<O resultado esperado em uma frase. "Ao final, um usuário consegue ___".>

## 3. Requisitos
> Numerados — viram a espinha da rastreabilidade. Um requisito = uma capacidade.

- **R1.** O sistema deve <...>.
- **R2.** Quando <condição>, o sistema deve <...>.
- **R3.** <...>

## 4. Critérios de aceite
> Given/When/Then. Cada um vira um teste (skill `gerar-testes-ca`). Cite o requisito.

- **CA1.** (R1) Dado <estado>, quando <ação>, então <resultado observável>.
- **CA2.** (R2) Dado <...>, quando <...>, então <erro esperado + código>.
- **CA3.** <...>

## 5. Fora de escopo
> O que esta feature explicitamente NÃO faz (evita que a IA "amplie" sozinha).

- <...>

## 6. Restrições
- **Arquitetura:** <ex.: seguir Clean + CQRS; caso de uso em application/>.
- **Performance:** <ex.: listar em < 300ms para 10k itens>.
- **Segurança:** <ex.: só o dono do pedido pode aplicar cupom>.
- **Compatibilidade:** <ex.: não quebrar a API v1>.

## 7. Casos de borda
> O que quebra em produção (skill `descobrir-casos-de-borda`). Comportamento esperado.

| Caso | Comportamento esperado |
|------|------------------------|
| Entrada vazia / nula | <...> |
| Dado inválido / expirado | <erro explícito, código> |
| Concorrência | <...> |
| Dependência externa falha | <...> |

## 8. Perguntas em aberto
> Decida antes de codar (skill `gerar-perguntas-de-esclarecimento`).

- [ ] <pergunta>

---
> **Checklist da spec:** requisitos numerados? critérios testáveis? fora de escopo
> explícito? bordas cobertas? Rode `afiar-spec` e resolva as ambiguidades antes de
> avançar para o [plano](./01-template-plano.md).
