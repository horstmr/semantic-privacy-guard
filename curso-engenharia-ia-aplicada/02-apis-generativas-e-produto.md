# Módulo 2 — APIs de IA Generativa e Produto

> Sair do "prompt que funciona" para o **produto que roda em produção**: mercado,
> provedores, custo, integração no back-end e multimodalidade.

## 2.1 Panorama do mercado de IA como serviço

Os **"ChatGPT wrappers"** — produtos que embalam uma API de LLM numa interface de
nicho — levantaram milhões (copywriting, jurídico, estudo). A lição: **o valor não
está no modelo, está no problema resolvido** — UX, dados proprietários, integração
ao fluxo do cliente e distribuição.

**Onde estão as oportunidades reais**: verticalização (nichos com dor específica),
dados exclusivos (RAG sobre bases privadas), automação de fluxo completo (não só
chat) e integração profunda (o wrapper raso é facilmente copiado).

## 2.2 Principais provedores

| Provedor | Destaques |
|---|---|
| **OpenAI** | GPT-4o/o-series, Whisper (áudio), DALL·E, ecossistema maduro |
| **Anthropic** | Claude — forte em raciocínio, código, contextos longos e segurança; criadora do MCP |
| **Google** | Gemini — multimodal nativo, contexto enorme, integração GCP |
| **Hugging Face** | Hub de modelos open-source, Inference API, biblioteca `transformers` |

As APIs diferem em formato de request, preços por token (entrada ≠ saída), limites
de contexto e recursos (tool use, vision, cache).

## 2.3 Engenharia de prompts avançada

> **↩︎ Já coberto no Prompt Engineering** — *prompt chaining* está na
> [Aula 5](../curso-prompt-engineering-para-devs/aulas/aula-05-o-codigo-do-raciocinio.md),
> *prompt templates/versionados* no
> [Bônus B1](../curso-prompt-engineering-para-devs/bonus/b1-prompt-como-codigo-testes.md),
> *redução de alucinação e válvulas de escape* na
> [Aula 8](../curso-prompt-engineering-para-devs/aulas/aula-08-consistencia-e-valvulas-de-escape.md),
> e *validar saída com segunda chamada* na
> [Aula 9](../curso-prompt-engineering-para-devs/aulas/aula-09-auto-refinamento-autor-revisor.md).
> Não repetimos aqui.

## 2.4 Custo e eficiência (novo)

O que o curso de prompt não cobre: **a conta**.

- **Custo**: preço = tokens de entrada + tokens de saída (saída costuma custar
  2–5× mais). Projete prompts enxutos, limite `max_tokens`, escolha o **menor
  modelo que resolve** (*model tiering*).
- **Cache**: *prompt caching* (reusar prefixos longos com desconto), cache de
  respostas para perguntas repetidas, e **semantic cache** — perguntas *parecidas*
  (por embedding) retornam resposta cacheada sem chamar o LLM.
- **Redução de tokens**: resumir histórico em vez de reenviar tudo; enviar só os
  chunks relevantes (RAG) em vez do documento inteiro.
- **Previsibilidade**: `temperature` baixa para tarefas determinísticas; saída
  estruturada (JSON mode/schemas); *seeds* quando disponíveis; validação da saída
  com retry automático se o JSON vier inválido.

## 2.5 RAG avançado na prática

- **Orquestração**: frameworks como **LangChain** encadeiam etapas — loaders,
  splitters, retrievers, LLMs — com componentes prontos.
- **Erros e re-tentativas**: toda chamada pode falhar (rate limit, timeout, saída
  malformada). Padrões: retry com *exponential backoff*, fallback para outro
  modelo, validação de schema com nova tentativa.
- **Observabilidade**: logar cada chamada (prompt, resposta, tokens, latência,
  custo), rastrear o pipeline ponta a ponta (LangSmith/Langfuse), monitorar
  qualidade em produção. Sem logs, depurar IA é impossível — o comportamento é não
  determinístico.

## 2.6 Integrando IA ao back-end

Fluxo típico de integração de um LLM a um back-end existente:

1. **Chave de API em variável de ambiente** (nunca no front-end nem no repositório);
2. **Endpoint próprio** no back-end que recebe a requisição do cliente, monta o prompt e chama a API do provedor;
3. **Autenticação e rate limiting** próprios (evitar que usuários abusem do seu custo);
4. **Versionamento**: fixar a versão do modelo (`gpt-4o-2024-xx`) para comportamento estável; tratar upgrades como deploys;
5. **Testes**: testes de contrato (a saída respeita o schema?) e avaliações com casos de referência (*evals*).

## 2.7 Modelos multimodais

- **Texto**: geração, resumo, classificação.
- **Visão** (GPT-4o Vision, Claude, Gemini): entender imagens — OCR inteligente, análise de screenshots, laudos visuais.
- **Áudio** (Whisper): transcrição e tradução de fala.
- **Vídeo** (Gemini multimodal): análise de cenas e conteúdo.

**Quando usar**: escolha o modal pela natureza do dado de entrada, não por moda —
texto é mais barato e rápido; use visão/áudio só quando o dado é imagem/som.

## 2.8 Aplicações práticas com APIs

- **OCR inteligente**: em vez de OCR clássico (só extrai caracteres), um modelo de
  visão **entende** o documento — extrai campos estruturados de notas fiscais,
  contratos, formulários, mesmo com layouts variados.
- **Análise de mídia**: classificar imagens/vídeos, gerar descrições, detectar
  conteúdo, extrair insights para automações.
- **Bots multimodais**: assistentes que recebem texto + foto + áudio na mesma
  conversa (ex.: suporte técnico que analisa a foto do erro).

---

## ❓ Perguntas para fixar — Módulo 2

**1. Por que "ChatGPT wrappers" geram valor se qualquer um acessa a mesma API?**
Porque o valor está na resolução do problema específico: UX para o nicho, dados proprietários, integração no fluxo do cliente e distribuição. O modelo é commodity; o produto não.

**2. Por que tokens de saída merecem atenção especial no custo?**
Porque geralmente custam mais que os de entrada (2–5×) e são menos controláveis; limitar `max_tokens` e pedir respostas concisas reduz custo diretamente.

**3. O que é semantic cache?**
Cache que compara o embedding da nova pergunta com perguntas já respondidas: se a similaridade é alta, retorna a resposta cacheada sem chamar o LLM — economiza custo e latência.

**4. O que é model tiering?**
Rotear cada tarefa ao menor/mais barato modelo que a resolve, reservando os modelos de ponta só para o que realmente exige — otimiza custo sem perder qualidade onde importa.

**5. Por que a chave de API nunca deve ficar no front-end?**
Qualquer usuário inspeciona o código do navegador e rouba a chave, gerando custo ilimitado na sua conta. A chamada ao provedor deve sempre passar pelo seu back-end.

**6. O que são "evals" e por que importam?**
Casos de teste com respostas esperadas para medir a qualidade do sistema a cada mudança (prompt, modelo, pipeline) — o equivalente aos testes automatizados no mundo não determinístico. (Detalhe no [Bônus B1](../curso-prompt-engineering-para-devs/bonus/b1-prompt-como-codigo-testes.md).)

**7. Diferença entre OCR clássico e OCR com modelo de visão?**
O clássico só transcreve caracteres; o modelo de visão entende o documento — identifica campos, contexto e estrutura, com layouts variados sem regras manuais.

**Próximo:** [Módulo 3 — MCP →](./03-mcp.md)
