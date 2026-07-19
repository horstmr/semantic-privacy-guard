# Aula 3 — Engenharia de Estado
## Como Salvar um Chat da Degradação

> "Chat termina um lixo": o contexto se perde, a alucinação toma conta conforme
> a thread cresce, e você gasta mais tempo corrigindo que criando. Esta aula é o
> antídoto.

## Objetivos

- Entender **por que** sessões longas degradam — e prever quando vai acontecer.
- Aplicar **Token Ops & Re-anchoring**: re-ancorar o contexto para vencer o
  *lost in the middle*.
- Operar o **Playbook**: checkpoints e resets sem perder o contexto essencial.
- Tratar a conversa como **estado gerenciado**, não como histórico que só cresce.

---

## 1. Por que o chat degrada

Lembre da Aula 2: a janela de contexto é **finita** e a atenção favorece as
pontas. Numa conversa longa, três coisas acontecem ao mesmo tempo:

1. **A janela enche.** Cada turno soma tokens. Em algum momento o começo
   (onde estavam suas instruções e restrições) fica tão distante que a atenção
   quase não o alcança — ou é truncado de vez.
2. **O sinal se dilui.** Instruções importantes ficam enterradas no meio de
   dezenas de mensagens. *Lost in the middle* em escala.
3. **O erro se acumula.** Uma resposta imprecisa vira contexto para a próxima, e
   o modelo passa a "confiar" no próprio deslize. A alucinação **compõe**.

O resultado é o "chat que termina um lixo": ele começou ótimo e foi apodrecendo.
Não é azar — é a mecânica da caixa preta agindo sobre um estado mal gerenciado.

---

## 2. Estado, não histórico

A virada desta aula: pare de pensar na conversa como um **histórico** que se
empilha, e comece a tratá-la como um **estado** que você mantém enxuto e
deliberado. Você é o engenheiro de estado da sessão.

**Histórico (cresce até degradar):**

```
[msg 1][msg 2][msg 3]...[msg 40] → tudo reenviado, tudo competindo por atenção
```

**Estado gerenciado (constante e limpo):**

```
[ estado essencial: objetivo, decisões, fatos, restrições ]
                    + [ últimos K turnos relevantes ]
```

Em vez de arrastar 40 mensagens, você mantém um bloco de **estado essencial**
compacto e só os turnos recentes que importam.

---

## 3. Re-anchoring (re-ancoragem)

**Re-anchoring** é reintroduzir, no ponto certo da conversa, as âncoras que a
atenção já está perdendo: o objetivo, as restrições, o formato, os fatos-chave.
É o segundo mecanismo do curso (*Token Ops & Re-anchoring*) na prática.

Técnicas:

- **Re-declare a instrução depois do material longo.** Em vez de confiar que o
  modelo lembra o pedido do topo, repita-o logo antes de ele responder:
  *"Lembrando o objetivo e as restrições: [...]. Agora, com base no acima,
  faça X."*
- **Fixe as âncoras nas pontas.** Objetivo e restrições no começo **e** um
  resumo delas no fim — as duas posições que a atenção privilegia.
- **Recarregue o schema.** Se a saída precisa de um formato, recarregue o
  formato a cada pedido crítico, não só na primeira mensagem.

```
<ancoras>
Objetivo desta sessão: refatorar o módulo de pagamentos para PHP 8.2 estrito.
Restrições que continuam valendo: sem libs externas; manter a interface pública;
todo método com tipagem estrita e tratamento de erro.
Formato: apenas diff unificado, sem explicação.
</ancoras>

Com base nas âncoras acima, refatore o arquivo a seguir: {{arquivo}}
```

Re-ancorar custa alguns tokens e **economiza** a sessão inteira de degradar.

---

## 4. O Playbook: checkpoints e resets

O terceiro mecanismo do curso é o **Playbook de Operação** — como conduzir uma
sessão longa sem perder o essencial.

### Checkpoint (salvar o estado)

A cada marco da conversa, peça (ou construa) um **resumo estruturado** do que
importa. Esse resumo é seu "save game":

```
Resuma o estado atual desta sessão em JSON, para eu reiniciar sem perder nada:
{
  "objetivo": string,
  "decisoes_tomadas": string[],
  "fatos_confirmados": string[],
  "restricoes_ativas": string[],
  "pendencias": string[]
}
```

Guarde esse JSON. Ele é denso, barato e não degrada — ao contrário das 40
mensagens que o geraram.

### Reset (recomeçar limpo)

Quando a sessão começar a degradar (respostas piorando, modelo "esquecendo"),
**não insista na thread poluída**. Abra uma sessão nova e injete o checkpoint:

```
<estado_da_sessao>
{{cole aqui o JSON do último checkpoint}}
</estado_da_sessao>

Retome a partir deste estado. Próximo passo: {{o que fazer agora}}
```

Você recomeça com a janela limpa, a atenção focada e **zero acúmulo de erro** —
mas sem perder as decisões e fatos que levaram horas para estabelecer. Isso é
resetar sem perder contexto essencial.

> **Regra prática:** trate checkpoints como commits e resets como um `git
> checkout` para um estado limpo. A thread é volátil; o checkpoint é durável.

---

## 5. Memória externa e estado estruturado

Levando a ideia ao limite: em vez de manter o estado *dentro* da conversa,
mantenha-o **fora** — num objeto no seu código — e injete só o pedaço relevante
a cada chamada.

```
estado = {
  "usuario": {...},
  "decisoes": [...],
  "etapa_atual": "..."
}
# a cada turno, você monta o prompt com estado + a nova entrada,
# em vez de reenviar a conversa inteira
```

Isso é mais barato, mais estável e mais fácil de depurar do que texto corrido —
e é a ponte direta para **agentes que não quebram** (bônus B4): um agente é, no
fundo, um loop que gerencia estado com disciplina.

---

## 6. Anti-padrões

- **Arrastar a thread inteira** turno após turno, esperando que o modelo filtre
  o que importa.
- **Insistir na sessão degradada.** Se piorou, resete — não brigue com um
  contexto poluído.
- **Nunca re-ancorar.** Confiar que o objetivo do topo ainda "vale" na mensagem
  30.
- **Checkpoint em prosa.** "Resuma a conversa" gera texto solto; peça **estado
  estruturado** (JSON) para ser denso e reutilizável.
- **Deixar o erro compor.** Não corrigir um deslize cedo — ele vira base para os
  próximos.

---

## Exercícios

1. **Provoque a degradação.** Conduza uma tarefa por 15+ turnos numa única
   thread, mudando de assunto algumas vezes. Anote quando o modelo começa a
   "esquecer" restrições do início. Em qual turno degradou?

2. **Re-ancore e compare.** Refaça a mesma tarefa, mas re-ancorando objetivo +
   restrições a cada 3–4 turnos. A degradação atrasou ou sumiu?

3. **Checkpoint + reset.** Numa conversa longa, peça o checkpoint em JSON. Abra
   uma sessão nova, injete só o JSON e continue. O modelo retomou sem perder as
   decisões? Compare o custo (tokens) com continuar a thread velha.

4. **Estado externo.** Modele uma tarefa multi-turno como um objeto de estado
   que você mantém e injeta. Rode 5 turnos assim vs. reenviando tudo. Qual ficou
   mais estável e barato?

Soluções em [`../exercicios/solucoes.md`](../exercicios/solucoes.md).

---

## Resumo

- Sessões longas **degradam** porque a janela enche, o sinal se dilui e o **erro
  se acumula** — mecânica da Aula 2 sobre estado mal gerido.
- Trate a conversa como **estado gerenciado**, não histórico que só cresce.
- **Re-anchoring:** re-declare objetivo, restrições e formato nas pontas e antes
  de pedidos críticos.
- **Playbook:** faça **checkpoints** (estado em JSON) e **resets** (sessão limpa
  + checkpoint) — como commits e checkouts.
- **Estado externo/estruturado** é a base de agentes que não quebram.

**Próximo:** [Aula 4 — O Motor da Inteligência →](./aula-04-o-motor-da-inteligencia.md)
