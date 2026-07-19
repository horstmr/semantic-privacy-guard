# 📋 Revisão Geral — 20 Perguntas-Síntese

> Auto-teste do curso inteiro. Tente responder cada uma **sem olhar**; revise as
> que errar. Repita após 1 dia e após 1 semana (repetição espaçada).

**1.** Complete: IA ⊃ ___ ⊃ ___ ⊃ LLMs. → *Machine Learning ⊃ Deep Learning.*

**2.** Qual arquitetura de 2017 viabilizou os LLMs modernos? → *O Transformer (self-attention, "Attention Is All You Need").*

**3.** Pipeline do RAG em 4 passos? → *Chunking → embeddings no vector DB → similarity search da pergunta → prompt com os trechos recuperados.*

**4.** Escala de soluções, da mais barata à mais cara? → *Prompt engineering → RAG → fine-tuning.*

**5.** MCP em uma frase? → *Protocolo aberto que padroniza a conexão entre LLMs e ferramentas/dados — o "USB-C da IA".*

**6.** As 3 primitivas MCP? → *Tools, Resources, Prompts.*

**7.** O agent loop? → *Percepção → raciocínio → ação → feedback, em iteração até o objetivo.*

**8.** ReAct vs. Plan-and-Execute? → *ReAct intercala pensamento e ação (adaptável); Plan-Execute planeja tudo e executa (eficiente).*

**9.** Quem executa a função no function calling? → *O código da aplicação; o LLM só retorna o JSON da chamada.*

**10.** Para que serve o LangGraph? → *Modelar agentes como grafos de estados com roteamento, retries e checkpoints — controle de produção.*

**11.** Fluxo seguro de IaC com IA? → *Gerar → validar (plan/policy) → PR → revisão → merge → GitOps aplica.*

**12.** Os 3 pilares da observabilidade? → *Métricas, logs e traces.*

**13.** Fluxo de auto-remediação segura? → *Alerta → verificação → mitigação → validação (com HITL para ações críticas).*

**14.** Fórmula RICE? → *(Reach × Impact × Confidence) ÷ Effort.*

**15.** Por que Monte Carlo em estimativas? → *Transforma incerteza em probabilidades por data, em vez de um chute único.*

**16.** Regra RAG vs. fine-tuning? → *RAG = o que o modelo deve saber; fine-tuning = como deve se comportar.*

**17.** O que é LoRA? → *Fine-tuning eficiente: congela o modelo base e treina só pequenas matrizes adaptadoras.*

**18.** O principal ataque a apps de LLM? → *Prompt injection — instruções maliciosas embutidas no conteúdo processado.*

**19.** Model tiering/routing? → *Rotear cada requisição ao modelo adequado por complexidade, otimizando custo e latência.*

**20.** As 3 entregas do capstone? → *RAG + CLI; API + front com MCP; solução implantada com agente operando via MCP.*

---

## E agora?

Você cobriu **os dois cursos**:

- **[Prompt Engineering para Devs](../curso-prompt-engineering-para-devs/README.md)** — a fundo em como instruir modelos (os 4 mecanismos, raciocínio, estado, tools, agentes).
- **Engenharia de IA Aplicada** (esta trilha) — o entorno completo: fundamentos, MCP, agentes avançados, UX/UI, DevOps, gestão, arquitetura, fine-tuning, governança e carreira.

O próximo passo é sempre o mesmo: **construir e medir**. Pegue o
[capstone](./11-projeto-integrador-capstone.md), aplique os prompts versionados e a
bateria de evals do outro curso, e coloque algo no ar. Portfólio deployado > curso concluído.

*Bons estudos! 🍺*
