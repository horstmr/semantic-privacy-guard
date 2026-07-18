# Módulo 09 — Agentes

> Um agente é um LLM em **loop**, com **ferramentas** e um **objetivo**, que
> decide sozinho os próximos passos até terminar. É onde tudo do curso se junta
> — e onde "aguentar produção" vira o desafio central.

## Objetivos de aprendizagem

- Entender a **anatomia de um agente**: objetivo, loop, ferramentas, memória,
  critério de parada.
- Enxergar o agente como **composição** dos módulos anteriores.
- Reconhecer os modos de falha de agentes e como contê-los.
- Decidir **quando um agente é a ferramenta certa** — e quando é overkill.

---

## 1. Do tool calling ao agente

No Módulo 08 você viu o loop: modelo pede ferramenta → código executa →
resultado volta → repete. Um **agente** é esse loop com três acréscimos:

1. Um **objetivo** de alto nível ("resolva o ticket do cliente"), não uma única
   pergunta.
2. **Autonomia para múltiplos passos**: o modelo planeja, age, observa o
   resultado, re-planeja — quantas vezes precisar.
3. Um **critério de parada**: como o agente sabe que terminou (ou que deve
   desistir).

O laço mental de um agente costuma ser: **pensar → agir → observar → repetir.**
Ele raciocina sobre o que fazer (Módulo 05), escolhe uma ferramenta (Módulo 08),
lê o resultado, e decide o próximo passo — até atingir o objetivo.

```
objetivo → [ pensar → escolher ação → executar → observar ] ⟳ → resposta final
                              ↑___________________________|
                          (repete até "pronto" ou limite)
```

---

## 2. Anatomia de um agente

Um agente de produção tem estes componentes — e cada um usa algo que você já
aprendeu:

| Componente | O que é | Módulo que sustenta |
|-----------|---------|---------------------|
| **System prompt / papel** | Quem o agente é, seu objetivo, suas regras e limites | 01, 02 |
| **Ferramentas** | O que ele pode fazer no mundo | 08 |
| **Loop de controle** | O código que roda pensar→agir→observar | 08 |
| **Memória / estado** | O que ele lembra entre passos e entre sessões | 07 |
| **Critério de parada** | Quando parar (sucesso, falha, limite de passos) | este |
| **Formato das ações** | Saída estruturada para cada decisão | 04 |
| **Avaliação** | Como você sabe que o agente funciona | 06 |

Se um desses está frouxo, o agente falha em produção. Um agente é, literalmente,
os oito módulos anteriores costurados por um loop.

---

## 3. O system prompt de um agente

É o contrato mais importante do sistema. Ele precisa deixar claro:

```
<papel>
Você é um agente de suporte que resolve pedidos de reembolso.
</papel>

<objetivo>
Dado o pedido de um cliente, decidir e executar o reembolso quando elegível,
ou explicar por que não é elegível.
</objetivo>

<ferramentas>
Você tem: buscar_pedido, verificar_politica, emitir_reembolso, escalar_humano.
</ferramentas>

<processo>
1. Busque o pedido e verifique a política aplicável antes de decidir.
2. Só emita reembolso se a política permitir. Em dúvida, use escalar_humano.
3. Nunca emita reembolso acima de R$ 500 sem escalar para humano.
</processo>

<limites>
- Nunca invente números de pedido ou valores. Use as ferramentas.
- Se faltar informação do cliente, pergunte antes de agir.
- Pare quando o reembolso for emitido, negado com justificativa, ou escalado.
</limites>
```

Repare como isso combina tudo: papel e regras (Mód. 02), processo em etapas
(Mód. 05), ferramentas com limites (Mód. 08), e critério de parada explícito.

---

## 4. Por que agentes quebram em produção (e como conter)

Autonomia é poderosa e perigosa. Os modos de falha típicos e suas contenções:

- **Loop infinito / não sabe parar.** O agente fica repetindo ações sem
  convergir. → **Sempre** imponha um teto de iterações e/ou de custo. Defina
  critérios de parada explícitos.
- **Acúmulo de erro.** Um passo errado no início contamina todos os seguintes,
  e o agente "confia" no próprio erro. → Faça-o **verificar resultados**
  (checar a saída de uma ferramenta antes de seguir), e valide estados críticos
  no seu código.
- **Explosão de contexto.** Depois de muitos passos, o histórico estoura a
  janela e o custo dispara. → Aplique o Módulo 07: resuma, use estado
  estruturado, memória externa.
- **Ação errada com efeito real.** O agente faz algo caro/irreversível baseado
  numa decisão ruim. → Módulo 08: confirmação humana para ações sensíveis,
  permissões mínimas, limites de valor (como o "R$ 500" acima).
- **Injeção via conteúdo.** Um dado processado tenta sequestrar o agente. →
  Módulo 01 (separar dado de instrução) + Módulo 08 (validar toda ação).
- **Impossível de depurar.** "Deu errado" mas você não sabe onde. → **Logue cada
  passo**: pensamento, ferramenta escolhida, argumentos, resultado. O rastro é
  seu debugger.

> **"Aguentar produção" = observabilidade + limites + verificação.** Um agente
> sem logs, sem teto de passos e sem confirmação para ações sensíveis não é um
> produto; é um incidente esperando acontecer.

---

## 5. Padrões úteis

- **Planejar-depois-executar.** O agente primeiro produz um plano (lista de
  passos) e só então executa. Fica mais fácil de auditar e corrigir antes de
  agir.
- **Decompor em subagentes.** Um agente "orquestrador" delega subtarefas a
  agentes especializados (um que pesquisa, um que escreve). Cada um tem prompt e
  ferramentas focados — mais confiável que um agente "faz-tudo".
- **Humano no loop (human-in-the-loop).** Para decisões de alto risco, o agente
  para e pede aprovação. Barato de implementar, salva de desastres.
- **Reflexão / auto-crítica.** O agente revisa a própria saída ("isso responde
  ao objetivo? há erro?") antes de finalizar. Pega erros óbvios.
- **Ferramentas > liberdade.** Um agente com ferramentas bem definidas e um
  processo claro é mais confiável que um agente "livre" e criativo. Restrição é
  feature.

---

## 6. Quando (não) usar um agente

Agentes são o topo da escada de complexidade. Suba só quando precisa.

**Use agente quando:**
- a tarefa exige **múltiplos passos cujo número você não sabe de antemão**;
- o caminho depende de **resultados intermediários** (precisa buscar algo para
  decidir o próximo passo);
- várias ferramentas precisam ser **orquestradas dinamicamente**.

**Não use agente quando:**
- um **prompt único** resolve (a maioria dos casos!);
- uma **cadeia fixa** de 2–3 chamadas resolve (mais simples, mais previsível,
  mais barato) — Módulo 05, prompt chaining;
- a confiabilidade é crítica e você não pode arcar com a imprevisibilidade da
  autonomia.

> **Regra prática:** comece com o mais simples que funciona. Prompt único →
> cadeia fixa → agente. Cada degrau adiciona poder e imprevisibilidade. Não
> construa um agente onde um prompt resolve; a maioria dos problemas reais não
> precisa de autonomia.

---

## 7. Anti-padrões de agentes

- **Sem teto de iterações.** Loop infinito, conta de API assustadora.
- **Sem logs.** Impossível entender por que falhou.
- **Ações irreversíveis sem confirmação.** Um bug vira prejuízo real.
- **Agente onde bastava um prompt.** Complexidade e custo sem motivo.
- **System prompt sem critério de parada.** O agente não sabe quando terminou.
- **Confiar no plano do agente sem verificar os resultados** de cada passo.
- **Um agente gigante faz-tudo** em vez de decompor em partes focadas.

---

## Exercícios

> Você pode fazer no papel/simulando, mesmo sem construir um agente real.

1. **Anatomize.** Pegue um "agente" que você conhece (um assistente de código,
   um bot de suporte) e mapeie os 7 componentes da seção 2. Qual componente
   parece mais frágil?

2. **Escreva o system prompt.** Projete um agente simples (ex: "agenda reuniões
   olhando a agenda e enviando convites"). Escreva o system prompt com papel,
   objetivo, ferramentas, processo, limites e **critério de parada**. Marque
   qual módulo do curso sustenta cada parte.

3. **Simule o loop.** No papel, execute 3–4 iterações "pensar→agir→observar"
   desse agente para um caso concreto. Onde ele poderia entrar em loop? Onde um
   erro se propagaria? Adicione uma contenção para cada risco.

4. **Escada de complexidade.** Pegue 3 tarefas suas e classifique cada uma:
   prompt único, cadeia fixa, ou agente? Justifique. (Provavelmente a maioria
   **não** precisa de agente — esse é o aprendizado.)

5. **Contenção.** Liste os 6 modos de falha da seção 4 e, para um agente que
   emite reembolsos, escreva uma proteção concreta para cada um.

Soluções em [`exercicios/solucoes.md`](./exercicios/solucoes.md).

---

## Resumo

- Um **agente** é um LLM em **loop** (pensar→agir→observar) com **ferramentas**,
  **objetivo** e **critério de parada** — a composição de todos os módulos.
- O **system prompt** é o contrato central: papel, objetivo, ferramentas,
  processo, limites, parada.
- Agentes quebram em produção por **loop infinito, acúmulo de erro, explosão de
  contexto, ação errada, injeção e falta de observabilidade**. Contenha com
  **limites + verificação + logs + humano no loop**.
- Suba a **escada de complexidade** só quando precisar: prompt único → cadeia
  fixa → agente. Restrição é feature.

---

## Você chegou ao fim da trilha 🎉

Recapitulando o caminho:

1. **Mentalidade** — prompt é código (Mód. 00).
2. **Os quatro pilares** — estrutura, instrução, exemplos, formato (Mód. 01–04).
3. **Técnica** — raciocínio (Mód. 05) e a disciplina de testar e versionar
   (Mód. 06).
4. **Produção** — contexto (Mód. 07), ferramentas (Mód. 08) e agentes (Mód. 09).

O próximo passo é **praticar no seu trabalho real**. Use a
[biblioteca de prompts](./templates/biblioteca-de-prompts.md) como cola, os
[exercícios](./exercicios/README.md) para treinar, e o
[glossário](./recursos/glossario-e-referencias.md) para aprofundar.

E lembre do mantra: **se você não consegue explicar o resultado, você não fez
engenharia de prompt — você teve sorte.** Agora você sabe fazer engenharia.
