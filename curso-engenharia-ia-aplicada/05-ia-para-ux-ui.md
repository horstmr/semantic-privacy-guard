# Módulo 5 — Ferramentas de IA para UX & UI

> Domínio inteiro que o curso de Prompt Engineering não cobre: IA no processo de
> produto, do wireframe ao componente em produção.

## Unidade 1 — A revolução da IA no design

**AI-driven UX/UI**: IA presente em todas as etapas do produto:
- *Pesquisa*: sintetizar entrevistas e feedback;
- *Ideação*: gerar variações de conceito rapidamente;
- *Prototipação*: transformar descrição em interface;
- *Desenvolvimento*: gerar componentes de código;
- *Teste*: avaliar usabilidade e gerar casos de teste.

Efeito prático: o ciclo ideia → protótipo → validação caiu de semanas para horas,
permitindo testar muito mais hipóteses.

## Unidade 2 — Prototipação e geração de UI

- **Text-to-UI**: descrever a interface em linguagem natural e receber
  wireframes/mockups ou código (v0, Figma AI, Uizard).
- **Prototipação assistida**: gera variações de layout, aplica design systems, sugere componentes.
- **Figma → código**: ferramentas como o **Firebase Studio** convertem designs do
  Figma em código front-end funcional; o dev passa a revisar e refinar.
- **Validação de usabilidade com IA**: modelos analisam fluxos e apontam fricções,
  inconsistências e problemas de acessibilidade antes do teste com usuários reais.

## Unidade 3 — Agentes de IA e CLI no fluxo front-end

- **Agentes de codificação**: operam no ambiente de dev — leem o repositório, criam/editam arquivos, rodam comandos e testes.
- **Gemini CLI** (e similares): scaffolding de projetos, geração de componentes e refatoração direto do terminal.
- **Fluxo com agentes**: ideia → prompt estruturado → componente gerado → testes gerados → revisão humana → merge. O dev vira orquestrador e revisor.
- **Prompt engineering para código de UI**: especifique stack e versões, design
  system/tokens, estados do componente (hover, erro, loading), acessibilidade e
  exemplos de código do próprio projeto como referência de estilo.
  ([↩︎ princípios gerais no curso de Prompt Engineering](../curso-prompt-engineering-para-devs/README.md).)

## Unidade 4 — Automação e interação inteligente com a UI

- **Testes E2E com agentes**: agentes autônomos que **consomem o MCP da aplicação**
  para interagir com a UI de forma semântica (em vez de seletores CSS frágeis),
  executando fluxos completos de teste.
- **mcpui.dev** (e similares): visualizar e interagir com o contexto que a aplicação
  expõe via MCP — facilita depurar e desenvolver agentes que operam a interface.

## Unidade 5 — Lógica de IA no cliente e no servidor

- **IA direto do front-end**: possível, mas com risco de segurança — a chave de API
  não pode ficar exposta; usam-se proxies/backends ou tokens efêmeros com escopo limitado.
- **Firebase AI Logic**: criar, gerenciar e implantar backends que embutem lógica de IA para apps front-end, cuidando de autenticação e cotas.
- **Features inteligentes**: busca semântica no produto, chatbots de suporte, personalização de conteúdo em tempo real.
- **Regra prática**: no cliente ficam interações leves e não sensíveis; chaves, prompts proprietários e dados sensíveis ficam sempre no servidor.

---

## ❓ Perguntas para fixar — Módulo 5

**1. O que muda no papel do designer/dev com AI-driven UX/UI?**
Menos produção manual, mais orquestração: definir intenção, restrições e critérios, revisar e refinar o que a IA gera — com ciclos de validação muito mais curtos.

**2. O que é Text-to-UI?**
Gerar wireframes, mockups ou código de interface a partir de descrições em linguagem natural.

**3. Por que testes E2E via MCP são mais robustos que via seletores CSS?**
Porque o agente interage com capacidades semânticas expostas pela aplicação (intenções/ações), e não com detalhes do DOM que quebram a cada mudança de layout.

**4. Quais os riscos de consumir APIs de IA direto do front-end?**
Exposição da chave (roubo e custo), falta de controle de rate limit e de validação. Mitiga-se com backend proxy ou tokens efêmeros de escopo restrito.

**5. O que incluir num prompt de geração de componente de UI?**
Stack e versões, design system/tokens, estados do componente, requisitos de acessibilidade e exemplos do código do projeto como referência de estilo.

**Próximo:** [Módulo 6 — IA para DevOps →](./06-ia-para-devops.md)
