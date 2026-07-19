# Módulo 01 — Fundamentos do Workflow com IA

> Antes das ferramentas, o **fluxo**. Este módulo instala a mentalidade que faz
> a IA acelerar você sem transformar seu projeto em código caótico.

## Objetivos

- Adotar o loop de trabalho **Spec → Gerar → Revisar → Integrar**.
- Saber **quando usar IA e quando não**.
- Configurar o mínimo para a IA "conhecer" seu projeto (regras persistentes).

---

## 1. O problema que o workflow resolve

A IA é um dev genial e **amnésico**: escreve ótimos trechos, mas não conhece sua
arquitetura, seus padrões nem o que já foi decidido. Sem trilhos, ela produz
código que *parece* certo e vai apodrecendo o projeto — cada geração num estilo,
cada uma reinventando o que já existia.

O workflow com IA não é "pedir código"; é **dar trilhos** para que o que ela gera
encaixe. Três trilhos (aprofundados nos próximos módulos):

1. **Spec antes de código** (Módulo 02) — especifique; a IA gera contra a spec.
2. **Arquitetura como restrição** (Módulo 03) — fronteiras claras de onde tudo mora.
3. **Skills reutilizáveis** (Módulo 04) — receitas que executam cada etapa igual.

---

## 2. O loop de trabalho

Todo trabalho não trivial com IA segue este ciclo:

```
   ┌── SPEC ───────── o que fazer e por quê (contrato revisável)
   │      ↓
   │   GERAR ──────── a IA produz contra a spec + a arquitetura
   │      ↓
   │   REVISAR ────── você (e a IA-revisora) checam contra a spec
   │      ↓
   └── INTEGRAR ───── testes passam, rastreabilidade registrada
```

A diferença para "vibe coding puro": **a revisão é contra um artefato explícito
(a spec)**, não contra o seu humor. Isso torna o resultado previsível e auditável.

### Anti-loop (o que não fazer)

- Pedir código sem spec → revisar "no olho" → aceitar porque compila → dívida.
- Deixar a IA decidir arquitetura no meio da geração → inconsistência.
- Gerar features inteiras de uma vez → PR gigante, irrevisável.

---

## 3. Quando usar IA (e quando não)

| Use IA | Pense duas vezes |
|--------|------------------|
| Boilerplate, scaffolding, CRUD | Decisões de arquitetura de alto risco |
| Testes a partir de spec/código | Segurança/cripto sem revisão especialista |
| Refatoração mecânica em massa | Lógica de negócio ambígua sem spec |
| Tradução entre linguagens/formatos | Algo que você não sabe verificar |
| Documentação, changelogs, ADRs | Código que ninguém vai revisar |

> **Regra de ouro:** só gere o que você **consegue revisar**. IA que produz mais
> do que o time consegue revisar não é produtividade — é dívida acelerada.

---

## 4. Faça a IA "conhecer" seu projeto

O ganho mais barato do workflow: um arquivo de **regras persistentes** na raiz do
repo que a IA lê a cada interação (o "system prompt do repositório"). Dependendo
da ferramenta: `.cursorrules`, `AGENTS.md`, `CLAUDE.md`, `.github/copilot-instructions.md`.

O que colocar (modelo completo no [`repo-base/`](../repo-base/README.md)):

```
- Stack e versões (linguagem, framework, libs principais).
- Arquitetura adotada (ex.: Clean Architecture — ver arquitetura/).
- Convenções: nomes, estrutura de pastas, estilo, tratamento de erro.
- O que NUNCA fazer (ex.: não acessar o banco fora da camada de dados;
  não introduzir libs sem justificar; não gerar sem teste).
- Como entregar: em diffs pequenos, com testes, seguindo a spec.
```

Isso é a Aula 1 do Prompt Engineering (R.O.C.C.O.) aplicada ao repositório
inteiro: você **configura** o comportamento uma vez, e ele vale em toda geração.

---

## 5. As três posturas do dev no workflow

Você deixa de ser só "quem digita o código" e assume três papéis:

- **Arquiteto** — define spec, fronteiras e restrições (a IA obedece).
- **Revisor** — julga o que a IA gerou contra a spec e a arquitetura.
- **Integrador** — garante testes, rastreabilidade e que o todo permanece coerente.

A IA faz a digitação; **você faz a engenharia.**

---

## Exercícios

1. **Escreva o loop no seu contexto.** Pegue uma feature pequena real e escreva,
   em 4 bullets, como seria cada etapa (spec/gerar/revisar/integrar). Onde hoje
   você pula etapas?

2. **Crie o arquivo de regras.** Faça um `.cursorrules`/`AGENTS.md` mínimo para
   um projeto seu (stack + arquitetura + 5 convenções + 3 proibições). Teste:
   peça algo à IA e veja se ela respeitou.

3. **Classifique 8 tarefas.** Liste 8 tarefas suas e marque "usar IA" ou "pensar
   duas vezes". Quantas você conseguiria de fato **revisar** com confiança?

---

## Resumo

- A IA é genial e amnésica: sem trilhos, gera caos plausível.
- O workflow é o loop **Spec → Gerar → Revisar → Integrar**, com revisão contra
  um artefato explícito.
- Só gere o que você **consegue revisar**.
- Instale **regras persistentes** na raiz (o system prompt do repo).
- Você vira **arquiteto + revisor + integrador**; a IA digita.

**Próximo:** [Módulo 02 — Spec-Driven Development →](./02-spec-driven-development.md)
