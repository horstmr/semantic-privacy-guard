# Módulo 03 — Pilar 3: Exemplos (few-shot)

> Um exemplo canônico vale mais que três parágrafos de descrição. Mostrar é
> mais barato e mais preciso que descrever.

## Objetivos de aprendizagem

- Diferenciar **zero-shot, one-shot e few-shot** e escolher o certo.
- Escrever **exemplos canônicos** que ensinam o comportamento (formato, tom,
  limite de escopo).
- Usar exemplos para ensinar **os casos difíceis**, não só o caso feliz.
- Saber **quando exemplos atrapalham** e como evitar viés de exemplo.

---

## 1. O espectro shot

"Shot" = quantos exemplos resolvidos você inclui no prompt antes de pedir a
tarefa real.

- **Zero-shot** — nenhum exemplo, só a instrução. *"Classifique o sentimento
  deste texto."* Ótimo quando a tarefa é comum e o formato é óbvio.
- **One-shot** — um exemplo. Suficiente para fixar formato quando o resto é
  claro.
- **Few-shot** — vários exemplos (tipicamente 2 a 5). Necessário quando a
  tarefa é sutil, o formato é específico, ou existem casos limítrofes que a
  descrição não captura bem.

**Regra de bolso:** comece zero-shot. Se a saída sai inconsistente em formato ou
erra os casos sutis, adicione exemplos — um de cada vez — até estabilizar. Não
comece com dez exemplos "por garantia": cada um custa tokens e pode enviesar.

---

## 2. Por que exemplos funcionam tão bem

Descrever comportamento em linguagem natural é impreciso. Um exemplo é uma
**especificação executável**: mostra exatamente o formato de entrada, o formato
de saída, e o mapeamento entre eles — sem espaço para interpretação.

Compare.

**Só descrição (impreciso):**

```
Extraia as habilidades técnicas mencionadas e retorne de forma organizada.
```

"De forma organizada" como? Lista? JSON? Inclui "trabalho em equipe"? Agrupa?

**Com um exemplo (preciso):**

```
Extraia as habilidades técnicas (linguagens, frameworks, ferramentas) do texto.
Ignore habilidades comportamentais.

Exemplo:
Entrada: "Tenho 5 anos com Python e Django, sou proativo e conheço Docker."
Saída: ["Python", "Django", "Docker"]

Agora processe:
Entrada: "{{texto}}"
Saída:
```

O exemplo respondeu, de uma vez: formato (array JSON de strings), granularidade
(tecnologias, não anos), e a regra de escopo ("proativo" ficou de fora). Três
parágrafos de instrução não teriam sido tão claros.

---

## 3. Como escolher bons exemplos

### Exemplos canônicos, não aleatórios

Um exemplo *canônico* é o representante ideal de uma categoria de entrada.
Escolha exemplos que, juntos, **cobrem a variedade** que o modelo vai encontrar
— não três variações do mesmo caso fácil.

### Ensine os casos difíceis

Os exemplos mais valiosos são os que mostram **o que fazer quando é ambíguo**.
Se você tem uma regra de borda que é difícil de descrever, *demonstre-a*:

```
<exemplos>
<!-- caso normal -->
Entrada: "Reunião amanhã às 15h com o time de backend."
Saída: { "tipo": "reuniao", "hora": "15:00", "participantes": "time de backend" }

<!-- caso sem hora explícita: regra = hora null, não invente -->
Entrada: "Preciso falar com você sobre o deploy."
Saída: { "tipo": "conversa", "hora": null, "participantes": "voce" }

<!-- caso fora de escopo: não é um evento -->
Entrada: "Bom dia, tudo bem?"
Saída: { "tipo": "nenhum", "hora": null, "participantes": null }
</exemplos>
```

Esses três exemplos ensinam mais sobre os limites da tarefa do que um parágrafo
de regras.

### Consistência é tudo

Todos os exemplos devem seguir **exatamente** o mesmo formato que você quer na
saída. O modelo imita o padrão que vê — inclusive erros. Se um exemplo usa
aspas simples e outro aspas duplas, você acabou de ensinar inconsistência.

### Cuidado com o viés de distribuição

Se seus 4 exemplos são todos de sentimento "positivo", o modelo aprende que
positivo é o esperado e passa a puxar para positivo. **Equilibre as classes**
nos exemplos (alguns positivos, alguns negativos, alguns neutros) e **varie a
ordem** — não deixe todos os positivos juntos no topo.

---

## 4. Formato dos exemplos

Deixe visualmente claro o que é entrada e o que é saída. Padrões comuns:

**Estilo `Entrada:/Saída:`** (simples, ótimo para pares curtos):

```
Entrada: <x>
Saída: <y>
```

**Estilo XML** (bom para exemplos com conteúdo multi-linha):

```
<exemplo>
  <entrada>...</entrada>
  <saida>...</saida>
</exemplo>
```

**Estilo conversa** (quando você simula um diálogo com papéis usuário/assistente
— comum em APIs de chat, onde cada exemplo vira um par de mensagens
`user`/`assistant`).

O importante: seja **consistente** e deixe a fronteira entrada↔saída óbvia.

---

## 5. Quando exemplos atrapalham

Few-shot não é sempre a resposta. Cuidado quando:

- **A tarefa é criativa/aberta.** Exemplos podem "ancorar" o modelo e reduzir a
  diversidade — todas as saídas ficam parecidas com seus exemplos. Para
  brainstorming, menos exemplos (ou zero) pode dar mais variedade.
- **Os exemplos custam caro.** Em prompts que rodam muito, cada exemplo é token
  a cada chamada. Se um exemplo já estabiliza, não coloque cinco.
- **Modelos de raciocínio.** Modelos focados em raciocínio às vezes vão melhor
  com uma instrução clara e **poucos ou nenhum** exemplo — exemplos demais podem
  atrapalhar a cadeia de raciocínio deles. Teste os dois.
- **Você enviesou sem querer.** Se todas as saídas parecem "grudadas" em um
  padrão específico dos exemplos (mesmo tamanho, mesma estrutura, mesma classe),
  revise a variedade dos exemplos.

> **Regra prática:** exemplos são a ferramenta mais poderosa para *formato* e
> *casos sutis*, e a mais perigosa para *criatividade*. Use conforme a tarefa.

---

## 6. Anti-padrões de few-shot

- **Exemplos inconsistentes entre si.** Formatos que divergem ensinam divergência.
- **Só o caso feliz.** Nenhum exemplo de borda → o modelo não sabe o que fazer
  quando a entrada é estranha.
- **Todos da mesma classe.** Enviesa a saída para aquela classe.
- **Exemplo maior que a tarefa.** Se o exemplo é mais complexo que o caso real,
  você confundiu em vez de ensinar.
- **Exemplo errado.** Um exemplo com a saída "quase certa" ensina o "quase". Os
  exemplos são a verdade; revise-os como revisaria um teste.

---

## Exercícios

1. **Zero → few, medindo.** Escreva um classificador de prioridade de tickets
   (`baixa`/`media`/`alta`) em zero-shot. Rode em 6 tickets variados. Anote
   erros. Agora adicione 3 exemplos canônicos (um de cada classe, incluindo um
   caso ambíguo) e rode nos mesmos 6. Melhorou? Quanto?

2. **Ensine a borda.** Crie uma tarefa de extração onde exista um caso chato
   (ex: "quando houver dois preços, pegue o com desconto"). Tente ensinar isso
   **só com regra em texto**. Depois **só com um exemplo**. Qual foi mais
   confiável em 4 entradas de teste?

3. **Provoque o viés.** Faça um classificador de sentimento com 4 exemplos,
   **todos positivos**. Rode em textos claramente negativos. O modelo puxou pra
   positivo? Reequilibre os exemplos (2 pos / 2 neg) e repita.

4. **Corte o excesso.** Pegue um prompt seu com 5+ exemplos. Remova um por vez e
   rode a bateria de teste a cada remoção. Qual é o número mínimo de exemplos
   que ainda mantém a qualidade? (Menos exemplos = mais barato e rápido.)

Soluções em [`exercicios/solucoes.md`](./exercicios/solucoes.md).

---

## Resumo

- **Comece zero-shot; adicione exemplos até estabilizar.** Não empilhe por
  garantia.
- Um exemplo é uma **especificação executável**: fixa formato, granularidade e
  escopo melhor que texto.
- Escolha exemplos **canônicos** que cobrem a variedade, **inclua casos de
  borda**, mantenha **consistência absoluta** de formato e **equilibre as
  classes**.
- Exemplos brilham para **formato e casos sutis**; podem **atrapalhar
  criatividade** e **enviesar** — use com intenção.

**Próximo:** [Módulo 04 — Pilar 4: Formato de saída →](./04-pilar-formato-de-saida.md)
