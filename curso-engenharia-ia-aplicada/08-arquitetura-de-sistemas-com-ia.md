# Módulo 8 — Arquitetura de Sistemas com IA

> Como desenhar sistemas AI-first: decisões de arquitetura, padrões de RAG e
> agentes, e a stack enterprise.

## Unidade 1 — Fundamentos de Arquitetura AI-First

**Tradicional vs. AI-driven**: sistemas tradicionais são **determinísticos** (mesma
entrada → mesma saída); sistemas com IA são **probabilísticos** — exigem validação
de saída, monitoramento de qualidade e fallbacks *por design*.

**Cinco padrões de design em sistemas inteligentes**: *Prompting* (como instruir),
*Responsible AI* (segurança/vieses), *UX* (comunicar incerteza ao usuário),
*AI-Ops* (operar modelos em produção) e *Optimization* (custo/latência).

**Decision framework — IA vs. regras determinísticas**: use **regras** quando a
lógica é bem definida, precisa ser 100% auditável e o erro é inaceitável (cálculo
de imposto); use **IA** quando há linguagem natural, ambiguidade ou padrões difíceis
de codificar (classificação de texto livre). Muitos sistemas combinam: **IA propõe,
regra valida**.

**Trade-offs permanentes**: latência × precisão × custo × performance — modelo maior
é mais preciso, mas mais caro e lento. Arquitetar é escolher o ponto certo por caso
de uso.

## Unidade 2 — Arquiteturas Single-Agent

Um único agente em fluxo linear de decisão. **Padrões**: *Reactive* (responde direto,
sem memória), *Memory-Enhanced* (usa memória), *Tool-Using* (executa ferramentas),
*ReAct* (raciocínio + ação), *Self-Reflection* (avalia e corrige a própria saída).

**Contextos ideais**: tarefas curtas, orçamento limitado, baixa latência — um agente
simples é mais barato, rápido e fácil de depurar que um multiagente.

> **↩︎ Já coberto** — o loop e a decisão "agente vs. prompt/cadeia" no
> [Bônus B4](../curso-prompt-engineering-para-devs/bonus/b4-agentes.md); os padrões
> ReAct/Plan-Execute/Reflection no [Módulo 4](./04-agentes-avancados.md).

## Unidade 3 — Arquiteturas Multi-Agent

**Padrões de orquestração**: *Sequential* (pipeline A→B→C), *Parallel* (simultâneos,
resultados combinados), *Supervisor* (coordenador central), *Hierarchical* (árvore de
supervisores), *Group Chat* (canal comum), *Handoff* (um agente transfere o controle
a outro).

**Desafios distribuídos**: falhas de nó, coordenação, controle de estado e
sincronização — os problemas de sistemas distribuídos clássicos, agravados pelo não
determinismo. Implementação em JS: comunicação via filas de mensagens e chamadas
assíncronas.

## Unidade 4 — Padrões de Design AI-Específicos

**Variações de RAG:**
- *Basic*: busca vetorial simples → prompt;
- *Hybrid Search*: busca vetorial + keyword (BM25) — melhor recall (pega termos raros/exatos);
- *Multi-Index*: índices separados por tipo/fonte de dado;
- *Agentic RAG*: um agente decide o que buscar, onde, e se precisa buscar de novo.

**Roteamento inteligente** (*Model Router* / *Intent-Based Routing*): classificar a
requisição e enviá-la ao modelo certo (simples → barato; complexa → top). Otimiza
custo e latência sem sacrificar qualidade.

**Caching**: *Semantic Cache*, *Prompt Cache*, *Response Streaming* (percepção de
latência menor).

**HITL**: *Approval Gates* (aprovação humana em pontos críticos), *Confidence
Thresholds* (abaixo de X% de confiança, escala para humano), *Audit Trails*.

> **↩︎ Já coberto** — o *papel* do RAG na engenharia de contexto está no
> [Bônus B2](../curso-prompt-engineering-para-devs/bonus/b2-engenharia-de-contexto-rag.md);
> as **variações** (hybrid, multi-index, agentic) são o que agrega aqui.

## Unidade 5 — Arquitetura Enterprise

- **Stack completo**: API Gateway → Orquestração (Kubernetes) → Serviços Compartilhados → Observabilidade;
- **Princípios**: *Loose Coupling* (componentes independentes), *Clear Interfaces* (contratos explícitos), *Policy-Driven Control* (políticas centralizadas governam o comportamento);
- **Observabilidade em IA**: rastrear prompts, logs e métricas de ponta a ponta;
- **Implantação híbrida**: Kubernetes (controle), Serverless (elasticidade), Edge (latência);
- **Model Tiering**: roteamento entre camadas de modelos por custo/qualidade.

---

## ❓ Perguntas para fixar — Módulo 8

**1. Diferença arquitetural fundamental entre sistema tradicional e AI-driven?**
O tradicional é determinístico; o AI-driven é probabilístico — a mesma entrada pode gerar saídas diferentes, exigindo validação de saída, monitoramento e fallbacks como parte da arquitetura.

**2. Quando usar regras determinísticas em vez de IA?**
Quando a lógica é bem definida, o resultado precisa ser 100% previsível e auditável e erros são inaceitáveis (cálculos financeiros/fiscais). IA entra onde há ambiguidade e linguagem natural.

**3. O que é Hybrid Search e por que supera a busca vetorial pura?**
Combina similaridade vetorial (significado) com keywords (BM25 — termos exatos, códigos, nomes). Vetorial pura falha com termos raros; a combinação melhora o recall.

**4. O que é Agentic RAG?**
RAG comandado por um agente: ele decide se busca, o que busca, em qual índice, avalia se o resultado basta e busca de novo se necessário — em vez de um pipeline fixo.

**5. Explique Model Router / Intent-Based Routing.**
Classificar cada requisição e direcioná-la ao modelo adequado: simples → modelos baratos/rápidos; complexa → modelos de ponta. Otimiza custo e latência.

**6. Cite os três mecanismos de HITL do módulo.**
Approval Gates (aprovação em ações críticas), Confidence Thresholds (escalar abaixo de certa confiança) e Audit Trails (rastreabilidade).

**7. O que é Policy-Driven Control numa arquitetura enterprise?**
Políticas centralizadas (segurança, custo, compliance) que governam automaticamente todos os componentes, em vez de regras duplicadas em cada serviço.

**8. Sequential vs. Handoff em multiagentes?**
Sequential é pipeline fixo (A sempre vai para B). Handoff é dinâmico: um agente decide transferir o controle a outro conforme a tarefa.

**Próximo:** [Módulo 9 — Fine-tuning →](./09-fine-tuning.md)
