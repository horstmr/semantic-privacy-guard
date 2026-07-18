# Aula 4 — O Motor da Inteligência
## Modelos Reativos vs. Raciocínio Profundo

> Nem todo modelo "pensa" do mesmo jeito. Escolher o motor errado para a tarefa
> é como usar uma chave de fenda como martelo: às vezes funciona, sempre
> desperdiça.

## Objetivos

- Distinguir **modelos reativos** de **modelos de raciocínio profundo**.
- Saber **qual usar quando** — por dificuldade, custo e latência.
- Entender como essa escolha muda a forma de escrever o prompt.

---

## 1. Dois tipos de motor

Simplificando uma paisagem que muda rápido, há dois comportamentos:

**Modelo reativo (rápido).** Responde "de primeira", puxando direto para a saída
mais provável. É veloz e barato. Excelente para tarefas diretas: classificar,
extrair, reformatar, responder algo contido no contexto. A força dele é
latência e custo.

**Modelo de raciocínio profundo.** Antes de responder, ele **pensa
internamente** — gera uma cadeia de raciocínio, explora passos, revisa — e só
então conclui. É mais lento e mais caro (você paga pelos tokens de pensamento),
mas resolve problemas com múltiplos passos, lógica e planejamento que derrubam um
modelo reativo.

A analogia do curso: reativo é o **reflexo**; raciocínio profundo é a
**deliberação**. Você não delibera para desviar de uma bola, e não desvia por
reflexo de uma decisão de arquitetura.

---

## 2. Qual usar quando

| Situação | Motor indicado | Por quê |
|----------|----------------|---------|
| Classificar, extrair, reformatar | **Reativo** | Direto; deliberar é desperdício |
| Alto volume, baixa latência | **Reativo** | Custo e velocidade dominam |
| Matemática, lógica, múltiplos passos | **Raciocínio** | O reflexo erra a cadeia |
| Planejamento, arquitetura, trade-offs | **Raciocínio** | Precisa explorar alternativas |
| Depuração de causa-raiz | **Raciocínio** | Encadear hipóteses e evidências |
| Escrita simples, resposta contida no contexto | **Reativo** | Não há o que deliberar |

> **Regra prática:** case o motor com a **dificuldade real** da tarefa. Usar
> raciocínio profundo para classificar spam é queimar tempo e dinheiro; usar um
> reativo para desenhar uma migração de banco é pedir uma resposta plausível e
> errada (a ilusão da Aula 1).

---

## 3. A escolha muda o prompt

Este é o ponto que a maioria ignora: **a forma de escrever o prompt depende do
motor.**

**Com modelo reativo:** você faz o trabalho cognitivo *por ele*. Guie o
raciocínio explicitamente (Chain of Thought, Aula 5), decomponha em passos, dê
scaffolding (Aula 6). O reativo não pensa sozinho — você empresta a estrutura.

**Com modelo de raciocínio:** ele já pensa. Então:
- **Não peça "pense passo a passo"** — é redundante e às vezes atrapalha a
  cadeia interna dele.
- **Use menos exemplos.** Few-shot em excesso pode interferir no raciocínio
  dele; prefira instrução clara e objetivo bem definido.
- **Dê o problema e o critério de sucesso**, não o método. Deixe-o achar o
  caminho.

Ou seja: com o reativo você é o arquiteto do raciocínio; com o de raciocínio
você é o arquiteto do **problema**. Escrever igual para os dois desperdiça um
deles.

---

## 4. Custo, latência e o pensamento como recurso

O raciocínio profundo consome tokens de pensamento — às vezes muitos, invisíveis
na resposta final mas presentes na conta. Isso significa:

- Em **produção de alto volume**, o motor de raciocínio pode ser caro demais
  para cada requisição. Padrão comum: usar um reativo para o caminho feliz e
  **escalar** para raciocínio só nos casos difíceis.
- Em **latência sensível** (algo que o usuário espera na tela), o tempo de
  deliberação pode ser inaceitável. Reserve raciocínio para o que roda em
  segundo plano ou tolera espera.

Pensar é um recurso. Gaste onde há dificuldade; economize onde há rotina.

---

## 5. Anti-padrões

- **Um motor para tudo.** Usar o mais poderoso (e caro) em toda tarefa, ou o
  mais barato em problemas que exigem deliberação.
- **Pedir CoT explícito a um modelo de raciocínio.** Redundante e
  contraproducente.
- **Encher de few-shot um modelo de raciocínio.** Pode atrapalhar a cadeia dele.
- **Ignorar o custo do pensamento.** Colocar raciocínio profundo num endpoint de
  alto volume sem medir a conta.
- **Esperar deliberação de um reativo.** Jogar um problema de arquitetura num
  modelo rápido e aceitar a primeira resposta plausível.

---

## Exercícios

1. **Mesmo problema, dois motores.** Se você tem acesso a um modelo reativo e um
   de raciocínio, rode um problema de lógica com passos nos dois. Compare
   acerto, latência e (se visível) tokens. Onde o raciocínio valeu a pena?

2. **Prompt por motor.** Pegue uma tarefa difícil e escreva duas versões: uma
   com CoT explícito e decomposição (para reativo), outra só com problema +
   critério (para raciocínio). Rode cada uma no motor certo. Depois **troque** —
   rode a versão de reativo no modelo de raciocínio. Piorou?

3. **Classifique suas tarefas.** Liste 8 tarefas suas do dia a dia e marque cada
   uma como "reativo" ou "raciocínio". Quantas realmente precisam deliberar?
   (Provavelmente menos do que você imagina.)

4. **Escalonamento.** Desenhe (no papel) uma estratégia "reativo no caminho
   feliz, raciocínio no caso difícil" para uma tarefa sua. Qual sinal dispara a
   escalada?

Soluções em [`../exercicios/solucoes.md`](../exercicios/solucoes.md).

---

## Resumo

- Há motores **reativos** (rápidos, baratos, respondem de primeira) e de
  **raciocínio profundo** (deliberam antes, caros, resolvem problemas com
  passos).
- **Case o motor com a dificuldade real** da tarefa; escalone do reativo para o
  raciocínio só quando preciso.
- A **forma do prompt muda**: com reativo você empresta a estrutura de
  raciocínio; com o de raciocínio você só define bem o problema.
- **Pensar é um recurso** — gaste em dificuldade, economize em rotina.

**Próximo:** [Aula 5 — O Código do Raciocínio →](./aula-05-o-codigo-do-raciocinio.md)
