# Módulo 4 — Criação de Agentes Autônomos (avançado)

> O [Bônus B4](../curso-prompt-engineering-para-devs/bonus/b4-agentes.md) ensina o
> **loop de agente, os padrões e a contenção** de forma introdutória. Este módulo
> aprofunda: padrões de raciocínio nomeados, taxonomia de memória, gerência de
> contexto, LangGraph e sistemas multiagente.

## Unidade 1 — Arquitetura de Agents

**Agente** = LLM + capacidade de agir em loop até atingir um objetivo.
**Agent loop**: **percepção → raciocínio → ação → feedback** — observa o estado,
decide o próximo passo, executa uma ferramenta, observa o resultado e repete.

**Estrutura interna:**
- **Planner**: decompõe o objetivo em passos;
- **Executor**: executa cada passo (chamadas de tools/APIs);
- **Memory store**: guarda o que já foi feito e aprendido;
- **Toolbox**: conjunto de ferramentas disponíveis.

**Tipos de agentes:** *Task-based* (executa e para), *Interactive* (colabora em
turnos), *Goal-oriented* (recebe objetivo e decide os passos), *Autonomous* (opera
continuamente com mínima supervisão).

> **↩︎ Já coberto no B4** — o loop, a anatomia de componentes e "quando usar
> agente vs. prompt/cadeia" estão no
> [Bônus B4](../curso-prompt-engineering-para-devs/bonus/b4-agentes.md).

## Unidade 2 — Padrões de Raciocínio e Execução

- **ReAct** (Reason + Act): alterna *pensamento* e *ação*, uma etapa por vez:
  "penso → ajo → observo → penso de novo". Ótimo para tarefas exploratórias, onde
  o próximo passo depende do resultado do anterior.
- **Plan-and-Execute**: gera um plano completo (lista de subtarefas), depois
  executa cada uma. Melhor para tarefas previsíveis e longas; mais barato (menos
  chamadas de raciocínio), porém menos adaptável a surpresas.
- **Reflection**: o agente avalia a própria saída e se corrige.
  ([↩︎ é o Pipeline Autor-Revisor da Aula 9](../curso-prompt-engineering-para-devs/aulas/aula-09-auto-refinamento-autor-revisor.md).)
- **Comparativo**: ReAct adapta-se melhor mas gasta mais tokens e pode divagar;
  Plan-Execute é eficiente mas quebra se o plano estava errado. Sistemas reais
  combinam: planejar, executar com ReAct dentro de cada passo, refletir ao final.
- **Frameworks**: LangChain (componentes), **LangGraph** (grafos de estados),
  CrewAI, AutoGen.

## Unidade 3 — Function Calling e Tool Use

> **↩︎ Já coberto no B3** — o mecanismo (o LLM devolve um JSON com a chamada, seu
> código executa) e o design de schemas estão no
> [Bônus B3](../curso-prompt-engineering-para-devs/bonus/b3-tool-calling.md).

O que agrega aqui: com function calling, agentes operam APIs REST, bancos,
automações e **servidores MCP** — o MCP é, na prática, uma padronização do tool use
([Módulo 3](./03-mcp.md)).

## Unidade 4 — Memória e Reflexão

**Tipos de memória:**
- *Curta*: a janela de contexto da conversa atual;
- *Longa*: persistida entre sessões (banco/arquivo);
- *Episódica*: registros de eventos/execuções passadas ("o que aconteceu");
- *Contextual*: conhecimento relevante recuperado dinamicamente para a tarefa.

**Armazenamento via embeddings**: memórias são vetorizadas e recuperadas por
similaridade quando relevantes (RAG aplicado à própria memória do agente).

**Reflection loops**: o agente revisa periodicamente suas execuções e extrai lições
("da última vez, a API X falhou com payloads grandes → dividir em lotes"),
melhorando a própria lógica.

> **↩︎ Relacionado** — a ideia de manter **estado enxuto** (checkpoints, resets,
> re-anchoring) está na
> [Aula 3 — Engenharia de Estado](../curso-prompt-engineering-para-devs/aulas/aula-03-engenharia-de-estado.md).
> Aqui formalizamos os *tipos* de memória.

## Unidade 5 — Gerenciamento de Contextos

- **Contexto ativo**: o que está na janela agora — instruções, histórico,
  resultados de tools. Estruturá-lo bem (seções, prioridades) melhora o desempenho.
- **Context pruning**: remover do contexto o que deixou de ser relevante
  (resultados antigos, tentativas falhas) para economizar tokens e evitar confusão.
- **Context stitching**: costurar informações de fontes diferentes (memória, RAG,
  estado da tarefa) num contexto coerente.
- **Contextos compartilhados** (multiagente): equilibrar **memória coletiva**
  (estado global) e **memória individual** — compartilhar tudo é caro e ruidoso;
  compartilhar de menos causa retrabalho.

## Unidade 6 — LangGraph e Workflows Complexos

**LangGraph** modela o fluxo do agente como um **grafo de estados**: nós = etapas
(chamadas de LLM ou funções), arestas = transições (podem ser condicionais).

Recursos-chave: controle de dependências, **roteamento de tarefas** (arestas
condicionais decidem o caminho), **fallback handlers** e **retry logic** nativos,
checkpoints de estado (retomar de onde parou), monitoramento/depuração.

Vantagem sobre um loop "solto": fluxo **explícito, auditável e testável** —
essencial para produção.

## Unidade 7 — Observabilidade e Limites de Autonomia

- **Métricas e logs**: cada passo logado (prompt, tool, resultado, tokens, latência, custo);
- **Auditoria**: reconstruir *por que* o agente tomou cada decisão;
- **Guardrails**: validação de entradas/saídas, lista de ações permitidas, tetos de custo e de iterações;
- **Human-in-the-loop (HITL)**: ações críticas (deletar, pagar, publicar) exigem aprovação humana;
- **Ética e escopo**: delimitar explicitamente o que o agente pode e não pode fazer;
- **Runaway loops**: limite máximo de iterações, timeout global, detecção de repetição e circuit breakers de API.

> **↩︎ Já coberto no B4** — os modos de falha e a filosofia "limites + verificação
> + logs + humano no loop" estão no
> [Bônus B4](../curso-prompt-engineering-para-devs/bonus/b4-agentes.md). Aqui só
> nomeamos os mecanismos.

## Unidade 8 — Projeto Prático

Agente autônomo completo com: **planejamento adaptativo** (replaneja quando algo
falha), **execução via APIs externas**, **logs e feedback loops**, **contexto
controlado e memória persistente**; orquestração de subtarefas com LangGraph;
avaliação por **eficácia** (concluiu?), **autonomia** (quantas intervenções
humanas?) e **resiliência** (recuperou-se de falhas?).

## Unidade 9 — Multi-Agent Systems

**Colaboração**: *orquestração* (um coordenador distribui trabalho), *coordenação*
(agentes sincronizam ações) e *negociação* (objetivos parcialmente conflitantes
chegam a acordo).

**Padrões:**
- **Supervisor**: um agente central roteia tarefas para especialistas;
- **Hierarchical**: supervisores de supervisores (árvore);
- **Group Chat**: agentes conversam num canal comum e se auto-organizam;
- **Delegation**: um agente delega subtarefas e cobra resultados;
- **Consensus**: múltiplos agentes propõem e votam/convergem.

**Comunicação assíncrona**: troca de mensagens via filas — agentes trabalham em
paralelo sem bloqueio. **Exemplo clássico**: analista → planejador → executor.
Referências: equipes de agentes Claude, AutoGPTs, CrewAI, Agentarium.

---

## ❓ Perguntas para fixar — Módulo 4

**1. O que diferencia um agente de uma simples chamada de LLM?**
O loop: percebe, raciocina, age (executa ferramentas) e usa o feedback para decidir o próximo passo, iterando até o objetivo — em vez de gerar uma única resposta.

**2. ReAct vs. Plan-and-Execute: quando usar cada um?**
ReAct para tarefas exploratórias em que cada passo depende do anterior (mais adaptável, mais caro). Plan-and-Execute para tarefas previsíveis e longas (mais eficiente, menos flexível). Na prática, combinam-se.

**3. No function calling, quem executa a função?**
Sempre o seu código. O LLM apenas retorna JSON estruturado indicando função e argumentos; a aplicação executa e devolve o resultado.

**4. Cite os quatro tipos de memória de um agente.**
Curta (contexto da sessão), longa (persistida entre sessões), episódica (registro de eventos passados) e contextual (conhecimento recuperado dinamicamente por relevância).

**5. O que é context pruning e por que fazê-lo?**
Remover do contexto ativo informações que deixaram de ser úteis. Economiza tokens, reduz custo e evita que ruído degrade as decisões do modelo.

**6. Que problema o LangGraph resolve vs. um agent loop simples?**
Torna o fluxo explícito como grafo de estados — com roteamento condicional, retries, fallbacks e checkpoints — dando controle, auditabilidade e robustez de produção.

**7. Como prevenir runaway loops?**
Limite máximo de iterações, timeout global, teto de custo, detecção de ações repetidas e circuit breakers nas chamadas de API.

**8. O que é o padrão Supervisor em multiagentes?**
Um agente coordenador central que recebe a tarefa, delega partes a especialistas e consolida os resultados.

**9. Quando human-in-the-loop é obrigatório?**
Em ações críticas ou irreversíveis: exclusões, transações financeiras, publicações externas, mudanças de produção — o agente propõe, o humano aprova.

**Próximo:** [Módulo 5 — IA para UX & UI →](./05-ia-para-ux-ui.md)
