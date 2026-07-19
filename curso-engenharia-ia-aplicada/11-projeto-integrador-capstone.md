# Módulo 11 — Projeto Integrador (Capstone)

> Consolida tudo: construir um **Micro-SaaS com IA** de ponta a ponta. É onde os
> dois cursos (Prompt Engineering + esta trilha) se encontram na prática.

## Unidade 1 — Ideação e Arquitetura

- Definir **problema e escopo** (nicho claro, dor real);
- **Viabilidade**: público-alvo e proposta de valor;
- **Arquitetura da solução**: front-end + back-end + componentes de IA (RAG, agentes, orquestração);
- **Planejamento**: milestones, backlog inicial e stack.

## Unidade 2 — Fundamentos: RAG e Agentes

- Implementar o **core de inteligência**: sistema RAG (ingestão → embeddings → vector DB → retrieval);
- **Selecionar e configurar o Vector Database**;
- Desenvolver o **agente principal** que usa o RAG para responder queries complexas;
- **Entrega 1**: protótipo funcional do RAG com interface **CLI** — validar a lógica
  antes de investir em UI.

## Unidade 3 — Orquestração, Back-end e Ponte IA-UI

- Criar as **APIs do back-end**;
- **Habilitar MCP na aplicação**: expor as capacidades do serviço de forma padronizada para agentes (a "ponte IA-UI");
- Orquestrar múltiplos agentes/LLMs com um **ADK (Agent Development Kit)**;
- **Entrega 2**: API documentada e funcional + front-end com MCP habilitado.

## Unidade 4 — Front-End, Integração e Implantação

- UI do Micro-SaaS (ex.: **Angular**), estruturada com o MCP;
- Integração front ↔ APIs de IA do back-end;
- **Validação do MCP**: confirmar que agentes conseguem interpretar e operar o front-end pelo protocolo;
- **CI/CD** e implantação;
- **Entrega 3**: solução completa no ar, com demonstração de um agente interagindo
  com o front-end via MCP.

## Unidade 5 — Apresentação e Defesa Técnica

- Apresentação com **storytelling, demo ao vivo e resultados**;
- **Defesa técnica**: justificar decisões de arquitetura, tecnologia e implementação
  (o "porquê" vale tanto quanto o "o quê");
- Documentação final: README claro, diagrama de arquitetura, instruções de uso.

> **Dica de integração com o outro curso:** aplique aqui o
> [projeto final do curso de Prompt Engineering](../curso-prompt-engineering-para-devs/exercicios/README.md#projeto-final-sugerido)
> — prompts versionados + bateria de evals — como o núcleo de qualidade do seu
> Micro-SaaS.

---

## ❓ Perguntas para fixar — Módulo 11

**1. Por que a Entrega 1 é um protótipo em CLI e não já com interface gráfica?**
Para validar a lógica central (RAG + agente) com o mínimo de esforço — se o core não funciona, a UI é irrelevante. Reduz risco e acelera iteração.

**2. Qual o papel do MCP no capstone?**
Ser a ponte padronizada entre a aplicação e os agentes: o serviço expõe suas capacidades via MCP, permitindo que qualquer agente interaja programaticamente com o produto, inclusive com o front-end.

**3. O que se espera numa defesa técnica além do sistema funcionando?**
Justificar as decisões: por que essa arquitetura, esse vector DB, esse padrão de agente; que alternativas foram consideradas e quais trade-offs foram aceitos.

**4. Quais são as três entregas incrementais?**
(1) RAG funcional com CLI; (2) API do back-end documentada + front com MCP habilitado; (3) solução completa implantada com demonstração de agente operando via MCP.

**Próximo:** [Módulo 12 — Carreira e entrevistas →](./12-carreira-e-entrevistas.md)
