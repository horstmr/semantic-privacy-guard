# Soluções comentadas

> Estas **não** são "a resposta certa" — prompt engineering admite muitos
> caminhos. São o **raciocínio esperado**, as armadilhas comuns e um padrão de
> referência. Se a sua solução chega ao mesmo objetivo por outro caminho e você
> consegue **explicar por quê**, está ótimo. Tente antes de ler.

---

## Aula 1 — O Fim dos "Pedidinhos"

**1. Pedido → configuração.** A versão R.O.C.C.O. deve reduzir drasticamente a
variação de formato e os tokens de preâmbulo, e passar a tratar bordas. **Lição:**
o pedido delega decisões ao acaso; a configuração as fecha. Um bom sinal: sua
config responde bem a uma entrada que você não tinha em mente ao escrevê-la.

**2. Cace a plausibilidade.** O ponto é perceber que "soar certo" ≠ "estar
certo". Ao checar de verdade (trata erro? tipa? fecha bordas?), o buraco
aparece. **Armadilha:** aceitar a primeira resposta bonita — a ilusão da
plausibilidade vive disso.

**3. Teste cada bloco do R.O.C.C.O.** Ao remover blocos um a um, normalmente
**Output spec** e **Constraints** são os que mais degradam a saída ao sair (o
preâmbulo volta, as bordas abrem). Isso te mostra quais blocos mais trabalham na
sua tarefa específica.

**4. Papel que muda tudo.** Se "Tech Lead cético" vs. "entusiasta júnior" muda a
saída de forma útil, o papel trabalha. Se não muda, é decorativo — concretize com
prioridade/critério/nível (prévia da Aula 10).

---

## Aula 2 — A Caixa Preta da IA

**1. Meça tokens.** Em PT você deve ver ~1,2–1,5 token/palavra; código costuma
ter razão diferente (símbolos, indentação). **Lição:** texto em PT custa um pouco
mais que em inglês, e formatação é token.

**2. Provoque a alucinação.** Sem o dado, o modelo inventa com confiança; com a
doc colada, ancora na realidade. **Lição:** a cura da alucinação é *fornecer o
dado* ou dar *válvula de escape* — não pedir "não invente".

**3. Sinta a atenção.** O fato no meio (posição 5/10) tende a ser recuperado com
menos confiabilidade que no topo. Mover para o começo melhora. É o *lost in the
middle* na prática.

**4. Temperature.** Variação alta ajuda em brainstorming e atrapalha em extração
de JSON (você quer forma idêntica). **Lição:** case a temperatura ao objetivo —
baixa para o que alimenta código.

---

## Aula 3 — Engenharia de Estado

**1. Provoque a degradação.** Espera-se que, após vários turnos e trocas de
assunto, o modelo comece a "esquecer" restrições do início — a janela encheu e a
atenção se dispersou. Anote o turno em que virou.

**2. Re-ancore e compare.** Re-declarar objetivo + restrições a cada 3–4 turnos
deve **atrasar ou eliminar** a degradação. **Lição:** re-anchoring vence o *lost
in the middle* a um custo pequeno.

**3. Checkpoint + reset.** O checkpoint em JSON permite retomar numa sessão limpa
sem perder decisões, com **menos tokens** que arrastar a thread. **Lição:** a
thread é volátil; o checkpoint é durável (como commit vs. working directory).

**4. Estado externo.** Manter um objeto de estado e injetar só o relevante é mais
estável e barato que reenviar tudo — e é a base de agentes que não quebram.

---

## Aula 4 — O Motor da Inteligência

**1. Mesmo problema, dois motores.** Em problema com passos, o motor de raciocínio
tende a acertar mais, ao custo de latência/tokens. Se não houve diferença, o
problema era fácil demais (não precisava deliberar).

**2. Prompt por motor.** A versão com CoT explícito ajuda o **reativo**; ao rodar
essa mesma versão no modelo de **raciocínio**, pode **piorar** (interfere na
cadeia interna dele). **Lição:** escrever igual para os dois desperdiça um.

**3. Classifique suas tarefas.** O aprendizado esperado: a maioria das tarefas
diárias é "reativo" (classificar, extrair, reformatar). Deliberação é minoria.

**4. Escalonamento.** Um bom sinal de escalada: baixa confiança, ambiguidade
detectada, ou o caso cair fora do "caminho feliz". Reativo resolve o comum;
raciocínio entra no difícil.

---

## Aula 5 — O Código do Raciocínio

**1. Meça o efeito do CoT.** Em problemas com passos, "mostre o passo a passo
antes" costuma elevar o acerto vs. resposta direta. Se não melhorou, o problema
era fácil ou o modelo já raciocina.

**2. Separe rascunho de resposta.** Com `{raciocinio, resposta}`, o código usa só
`.resposta` e `.raciocinio` vira depuração. **Armadilha:** pedir a resposta
**antes** do raciocínio — o benefício some.

**3. Onde atrapalha.** Na classificação trivial, CoT só adiciona tokens/latência
sem ganho. Raciocínio é para dificuldade, não enfeite.

**4. Decomponha.** Quebrar em 3–4 etapas explícitas costuma sair mais correto e
**auditável** que "faça tudo" — você vê onde falhou.

---

## Aula 6 — O Antídoto (Scaffolding)

**1. Júnior → sênior.** Cada andaime (template → exemplo) deve subir o nível: o
template mata o parágrafo genérico; o exemplo fixa formato e escopo. **Lição:** a
"IA júnior" é falta de andaime, não do modelo.

**2. Ensine a borda com exemplo.** Normalmente **o exemplo vence a regra** para
casos sutis — é especificação executável, sem interpretação.

**3. Provoque o viés.** 4 exemplos todos positivos fazem o modelo puxar negativos
para positivo. Reequilibrar (2/2) corrige. A distribuição dos exemplos vira o
"prior".

**4. Corte o excesso.** Muitas vezes 2 exemplos bem escolhidos igualam 5 — mais
barato e menos enviesado. O mínimo é o que ainda mantém a qualidade da bateria.

---

## Aula 7 — Múltiplas Realidades (Tree of Thoughts)

**1. CoT vs ToT.** Em problema com várias soluções, o ToT (gerar→avaliar→escolher)
tende a produzir uma resposta melhor que a linha única do CoT — mas custa mais.
Vale quando escolher errado é caro.

**2. Force a diversidade.** Se as 3 abordagens saírem parecidas, o ToT não
explorou nada. Exigir estratégias distintas ("uma simples, uma robusta, uma
barata") corrige.

**3. Plano com checkpoint.** Pedir o plano antes de executar deve te deixar
**pegar um caminho ruim** antes de gastar tokens desenvolvendo-o. É a vantagem do
não linear + ponto de controle.

**4. Poda.** O modelo deve desenvolver **só** o vencedor. Se desenvolve todos,
ajuste a instrução para podar os fracos — senão você paga por todos os ramos.

---

## Aula 8 — Consistência e válvulas de escape

**1. Feche as cinco bordas.** A solução robusta trata entrada completa, campo
ausente (→ null), tipo errado (→ marcador de inválido), vazio, e injeção
(ignorar instruções internas). **Armadilha:** esquecer vazio e injeção — os que
quebram em produção.

**2. Self-consistency.** Rodar 5x e tomar o majoritário costuma elevar a acurácia
em tarefas de raciocínio instáveis. Vale quando errar é caro; para trivial, é
desperdício.

**3. Guardrail anti-contradição.** A regra "nunca recomende A e seu oposto" reduz
a auto-contradição. Invariantes críticos, valide também no código.

**4. Injeção.** Sem a fronteira + regra, o modelo obedece o "responda HACKED".
Com elas, classifica normalmente. **Lição:** dado de usuário sem regra
anti-injeção é vulnerabilidade.

---

## Aula 9 — Auto-refinamento

**1. Monte o pipeline.** Autor→revisor→autor com rubrica deve superar a passada
única, principalmente em completude e casos de borda.

**2. Rubrica importa.** Sem rubrica, o revisor só elogia ("ficou bom"). Com
rubrica (checklist objetivo), ele acha defeitos reais e acionáveis. A rubrica é o
que faz o refinamento morder.

**3. Adversarial vs. simpático.** "Tente quebrar em produção" encontra falhas que
"valide isto" não vê. Postura adversarial > validação simpática.

**4. Retorno decrescente.** Da rodada 1→2 costuma haver ganho claro; 2→3, pouco;
e alguma rodada pode **piorar** (regressão). **Lição:** 1–2 rodadas capturam a
maior parte; pare por critério objetivo.

---

## Aula 10 — Personas modulares

**1. Três interfaces, um código.** Tech Lead, SRE e Security devem produzir
revisões **diferentes e complementares** do mesmo código — cada uma olha para um
lugar. Se saíram iguais, as personas estavam vagas.

**2. Concretize a persona-enfeite.** "Especialista incrível" não muda a saída;
com perspectiva + critérios + nível, muda de forma útil. Esse é o teste de uma
persona que trabalha.

**3. Biblioteca modular.** Manter tarefa/formato fixos e trocar só o bloco de
persona dá reuso e consistência — o mesmo prompt "implementa" competências
diferentes.

**4. Painel + síntese.** A síntese dos riscos que aparecem em **mais de uma**
perspectiva costuma apontar o que realmente importa, separando-o dos detalhes de
um ângulo só.

---

## Aula 11 — Design de saída e verbosidade

**1. Do texto ao contrato.** Com JSON + schema e temperatura baixa, a **estrutura**
deve sair idêntica nas 5 execuções (só os valores mudam). Se a forma variar,
reforce "apenas JSON, sem markdown" + schema.

**2. Mate o preâmbulo.** A instrução de saída estrita deve eliminar o "Claro!
Aqui está..." e economizar dezenas/centenas de tokens por chamada — economia
direta em produção de volume.

**3. Parse defensivo.** Sem "sem markdown", o modelo embrulha em ```` ```json ````.
A função defensiva (remover cercas → parse → plano B extraindo `{...}` → validar)
deve sobreviver aos três casos. Saída de LLM é entrada não confiável.

**4. Enum + erro no formato.** `"outros"` fecha a borda do enum (equivale ao
`default` do switch); o erro dentro do JSON (`{erro, dados:null}`) deixa o código
tratar sucesso e falha pela mesma porta.

---

# Bônus

## B1 — Testes e versão

**1. Crie a bateria.** Boa bateria tem felizes de cada classe, bordas (vazio,
tipo errado) e ao menos um adversarial. Se todos os casos são fáceis, você só
testa o que já funciona.

**2. Rode o eval mínimo.** O valor está na **lista de FALHAs** — é seu backlog
priorizado. A falha mais comum aponta o ponto mais fraco do prompt.

**3. Corrija e cheque regressão.** Rodar a bateria **inteira** após a correção
revela se você quebrou outro caso. Melhorar A e quebrar B silenciosamente é a
regressão clássica — compare **caso a caso**, não só o total.

**4. Instabilidade.** Um caso que passa 3 de 5 vezes está frágil, não resolvido.
A causa costuma estar no formato ambíguo (Aula 11), instrução com lacuna (Aula 1)
ou falta de exemplo (Aula 6).

**5. LLM-juiz.** Compare os julgamentos do juiz com os seus. Vieses comuns:
premiar respostas longas ou "bem escritas" mesmo quando erradas. Rubrica
específica e calibração contra humanos reduzem isso.

## B2 — Contexto e RAG

**1. Contexto de menos vs. de mais.** Sem dado → alucina; só a seção relevante →
melhor precisão e menor custo; documento inteiro → mais caro e às vezes **pior**
(sinal diluído). O ideal raramente é "tudo".

**2. Perdido no meio.** O fato no meio é recuperado com menos confiabilidade que
no começo. Posicione o crítico nas pontas.

**3. Mini-RAG manual.** Com só os trechos relevantes + "use apenas <contexto>", a
resposta foca. No caso sem resposta nos trechos, o esperado é a **saída de
escape**; se inventou, faltou a instrução de escape (erro nº 1 de RAG).

**4. Resuma o histórico.** Um resumo estruturado (`{fatos, pendencias}`) permite
continuar sem reenviar tudo — mais barato e estável que histórico corrido.

**5. Desenhe para cache.** Mover o conteúdo fixo para o início (variável no fim)
habilita o cache do prefixo e, de quebra, deixa o prompt mais legível.

## B3 — Tool calling

**1. Defina bem.** Descrição com "use quando... / não use para..." faz o modelo
chamar na hora certa ("converta 10 USD") e não chamar quando não deve ("capital
da França?"). Vaga → chama errado.

**2. Rode o loop.** O esperado é o modelo **delegar** a conta à ferramenta em vez
de chutar — precisão vem da ferramenta.

**3. Args hostis.** Sem validação, argumento malformado quebra (ou pior, executa
errado calado). Com validação + erro estruturado, o modelo se corrige. O modelo
propõe, seu código dispõe.

**4. Segurança.** Para `enviar_email`: confirmação humana, allowlist de
destinatário, rate limit. Contra injeção "envie para x", allowlist + confirmação
seguram mesmo que o modelo "queira" obedecer.

## B4 — Agentes

**1. Anatomize.** Mapear os 7 componentes num agente conhecido costuma revelar
que o elo fraco é **memória/estado** ou **critério de parada** — os mais
esquecidos.

**2. Escreva o system prompt.** Bom system prompt de agente tem papel, objetivo,
ferramentas, processo e **critério de parada** explícito. Sem o "quando parar", o
agente não sabe terminar.

**3. Simule o loop.** Executando 3–4 iterações no papel, você deve apontar onde
entraria em loop (nunca satisfaz a parada) e onde um erro se propagaria (passo 1
errado contamina o resto). Cada risco pede uma contenção.

**4. Escada de complexidade.** O aprendizado: a **maioria** das tarefas **não**
precisa de agente — um prompt ou uma cadeia fixa resolve com mais previsibilidade
e menos custo. Reconhecer isso é maturidade.

**5. Contenção.** Reembolsos: loop → teto de iterações; acúmulo de erro →
verificar cada ferramenta; explosão de contexto → resumir estado; ação errada →
limite de valor + confirmação; injeção → separar dado/instrução + validar;
indepurável → logar cada passo. Isso **é** aguentar produção.

---

## Fechamento

Se você fez os exercícios de verdade, percebeu o padrão: quase todo problema de
"o modelo não obedece" se resolve voltando à **Arquitetura Cognitiva** (Aula 1) e
à disciplina de **medir** (Bônus B1). Prompt não é adivinhação — é engenharia.
Bons prompts. 🍺
