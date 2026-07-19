# Skills — Docs & DevOps

> A "papelada" que humanos evitam e a IA mantém em dia — o que sustenta a
> **rastreabilidade** ([Módulo 05](../modulos/05-codigo-previsivel-revisavel-rastreavel.md)).

---

## gerar-readme
**Quando usar:** documentar um módulo/projeto para quem chega novo.
**Entradas:** o código/estrutura do módulo.
**Saída:** README com propósito, uso, arquitetura e como rodar.

**Prompt:**
```
Gere um README para <alvo> com: propósito (o problema que resolve, em 2 frases),
como instalar/rodar, exemplo de uso mínimo, a arquitetura (camadas/pastas), como
rodar os testes, e como contribuir. Escreva para quem NUNCA viu o projeto. Não
invente comandos/scripts — se não souber, marque {{PREENCHER}}. Seja conciso.
<alvo>{{modulo}}</alvo>
```

---

## gerar-matriz-rastreabilidade
**Quando usar:** fechar o ciclo SDD — provar que a spec foi cumprida.
**Entradas:** a spec (requisitos/CAs) + o código + os testes.
**Saída:** tabela requisito → implementação → teste.

**Prompt:**
```
Monte a matriz de rastreabilidade a partir de <spec>, <codigo> e <testes>:
| Req | Descrição | Implementação (arquivo:função) | Teste (arquivo:caso) |
Preencha cada requisito. Marque em VERMELHO (texto "⚠️ SEM TESTE" / "⚠️ SEM CÓDIGO")
qualquer requisito sem implementação ou sem teste, e liste código órfão (sem
requisito). Não invente vínculos — se não achar, marque a lacuna.
<spec>{{spec}}</spec> <codigo>{{codigo}}</codigo> <testes>{{testes}}</testes>
```

---

## mensagem-commit-rastreavel
**Quando usar:** commitar de forma que o histórico conte a história e cite a spec.
**Entradas:** o diff + os requisitos atendidos.
**Saída:** mensagem no padrão Conventional Commits citando requisitos.

**Prompt:**
```
Escreva a mensagem de commit para o diff <diff> no padrão Conventional Commits
(tipo(escopo): descrição no imperativo, ≤72 chars). No corpo: o que mudou e por quê,
e cite os requisitos atendidos (Rn) e CAs. Se o diff mistura assuntos, sugira quebrar
em commits separados. Não descreva o óbvio linha a linha; explique a intenção.
<diff>{{diff}}</diff> <reqs>{{requisitos}}</reqs>
```

---

## descrever-pr
**Quando usar:** abrir um PR revisável.
**Entradas:** o diff + a spec.
**Saída:** descrição de PR com contexto, mudanças, como testar e checklist.

**Prompt:**
```
Escreva a descrição do PR para <diff>, baseada na spec <spec>. Seções:
- Contexto/objetivo (link para a spec/requisitos R1..Rn cobertos);
- O que mudou (resumo por área, não linha a linha);
- Como testar/validar (passos + quais CAs);
- Fora de escopo / follow-ups;
- Checklist: [testes] [aderência arquitetural] [matriz atualizada] [sem lib nova injustificada].
Mantенha revisável: se o PR está grande demais, sinalize para quebrar.
<diff>{{diff}}</diff> <spec>{{spec}}</spec>
```

---

## gerar-changelog
**Quando usar:** consolidar mudanças para uma release.
**Entradas:** a lista de commits/PRs desde a última versão.
**Saída:** changelog agrupado (Added/Changed/Fixed/...) em linguagem de usuário.

**Prompt:**
```
Gere um CHANGELOG a partir de <commits> no estilo Keep a Changelog, agrupando em
Added / Changed / Deprecated / Removed / Fixed / Security. Escreva na linguagem do
USUÁRIO (o que muda para quem usa), não em jargão de commit. Destaque breaking
changes no topo. Ignore commits internos (chore/refactor) que não afetam o usuário.
<commits>{{commits_ou_prs}}</commits>
```

---

## gerar-pipeline-ci
**Quando usar:** montar o CI mínimo que sustenta o workflow.
**Entradas:** stack + o que rodar (testes, lint, build).
**Saída:** o arquivo de pipeline (GitHub Actions/GitLab CI) com os gates.

**Prompt:**
```
Gere um pipeline de CI para <stack> na plataforma <plataforma> com os gates:
instalar deps → lint → testes (com relatório) → build. Regras:
- Falhar o PR se lint/teste falhar (gates obrigatórios).
- Cache de dependências para velocidade.
- Rodar em todo PR e no merge para a branch principal.
- Não incluir deploy (fora de escopo aqui) nem secrets hardcoded.
Saída: o arquivo de pipeline comentado.
<stack>{{stack}}</stack> <plataforma>{{ci}}</plataforma>
```

---

## documentar-api
**Quando usar:** documentar endpoints/contratos para consumidores.
**Entradas:** os endpoints/handlers.
**Saída:** doc de API (OpenAPI ou markdown) com request/response/erros.

**Prompt:**
```
Documente a API a partir de <endpoints>. Para cada rota: método, caminho,
parâmetros, corpo da request (com schema), respostas de sucesso E de erro (status +
corpo), e um exemplo. Formato: <OpenAPI 3 | markdown, escolha o do projeto>.
Deixe explícitos os códigos de erro de negócio (ex.: CUPOM_INVALIDO → 409).
Não invente campos que não existem no código; marque dúvidas com {{PREENCHER}}.
<endpoints>{{endpoints}}</endpoints>
```

---

← [Review & Refatoração](./05-review-e-refatoracao.md) · [Voltar ao catálogo](./README.md)

---

> **Total do catálogo:** 42 skills prontas (7+7+8+6+7+7) cobrindo o workflow do
> planejamento à entrega. Adicione as suas seguindo a
> [receita do Módulo 04](../modulos/04-skills-dentro-da-arquitetura.md#5-como-criar-a-sua-skill).
