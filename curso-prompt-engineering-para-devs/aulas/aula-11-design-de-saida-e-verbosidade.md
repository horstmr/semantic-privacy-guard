# Aula 11 — Design de saída e controle de verbosidade

> "Refém da verbosidade": você escreve parágrafos e a IA devolve o básico
> embrulhado em 350 tokens de preâmbulo. Esta aula fecha o curso desenhando a
> **saída como uma API** — enxuta, estrita e parseável.

## Objetivos

- Tratar a saída do prompt como uma **API** que outro código consome.
- **Cortar a verbosidade**: eliminar preâmbulo e enchimento.
- Obter **JSON confiável** e usar **formato estrito** (structured outputs).

---

## 1. A saída é uma API, não uma redação

Se a resposta vai ser consumida por **outro código** (ou por você, com pressa),
ela precisa ser um **contrato**, tão rígido quanto o retorno de uma função.
"Responda de forma organizada" é uma promessa vazia.

**Refém da verbosidade:**

```
Analise o sentimento e me diga o resultado.
```

Saída hoje: *"O sentimento do texto é claramente positivo, pois o autor..."*.
Saída amanhã: *"Positivo 😊"*. Seu `if resposta == "positivo"` quebra.

**Saída como contrato:**

```
Classifique o sentimento. Responda com exatamente uma palavra, minúscula, sem
pontuação: positivo, negativo ou neutro. Nada além disso.
```

Para múltiplos campos, **peça JSON**:

```
Responda apenas com JSON, sem markdown, sem texto antes ou depois:
{
  "sentimento": "positivo" | "negativo" | "neutro",
  "confianca": number,   // 0 a 1
  "trecho_chave": string
}
```

---

## 2. Controle de verbosidade: mate o preâmbulo

A verbosidade tem duas fontes, e você fecha as duas:

**Preâmbulo e cortesia** ("Claro! Aqui está...", "Espero ter ajudado!"). Corte
com instrução direta:

```
Responda apenas com {{o artefato}}. Sem introdução, sem conclusão, sem
comentários. Não explique a menos que solicitado.
```

**Explicação não pedida** (o modelo ensina quando você só queria o resultado).
Se você quer o raciocínio, peça-o **separado** (Aula 5); se não, proíba:

```
Entregue somente o código final. Nenhuma explicação, nenhum comentário no código
além dos estritamente necessários.
```

Cada token de preâmbulo é dinheiro e latência. Em produção de volume, cortar
verbosidade é economia direta — e o mecanismo *raw_code_only* que o curso cita
nada mais é que isto: formato estrito, só o artefato.

> **Cuidado com o exagero oposto:** cortar verbosidade **não** é cortar
> raciocínio quando ele é necessário (Aula 5). A regra é: **sem enchimento na
> saída final**; o raciocínio, quando útil, vai para um campo separado, não para
> a resposta que o usuário/código recebe.

---

## 3. JSON confiável

O modelo adora ser prestativo e embrulhar o JSON em ```` ```json ```` ou explicar
antes. Defesas, da mais forte para a mais fraca:

1. **Structured outputs / JSON mode**, se o provedor oferece — o formato é
   **garantido** pela API, não pedido por gentileza. Use sempre que puder.
2. **Peça explicitamente** "apenas JSON, sem markdown, sem texto antes ou depois",
   e repita no fim.
3. **Forneça o schema** no prompt (tipos e enums).
4. **Parse defensivo no código** — trate a saída como entrada não confiável.

```js
function parseModelJson(raw) {
  const cleaned = raw.trim().replace(/^```(?:json)?/i, "").replace(/```$/, "").trim();
  let obj;
  try { obj = JSON.parse(cleaned); }
  catch {
    const m = cleaned.match(/\{[\s\S]*\}/);   // plano B: extrai o objeto
    if (!m) throw new Error("resposta sem JSON");
    obj = JSON.parse(m[0]);
  }
  if (!["positivo","negativo","neutro"].includes(obj.sentimento))
    throw new Error("sentimento inválido: " + obj.sentimento);
  return obj;
}
```

> **Princípio:** nunca confie cegamente na saída, mesmo formatada. O modelo é um
> gerador probabilístico, não um serializador. Valide como você validaria a
> resposta de um serviço externo instável.

---

## 4. Formato estrito e enums fechados

Quanto mais estrito o formato, menos espaço para variação:

- **Enum fechado.** "Diga a categoria" sem listar as categorias → o modelo
  inventa uma sexta. Liste-as e feche com um escape (`"outros"`).
- **Erro dentro do formato.** Em vez de o modelo escrever "não consegui", faça-o
  retornar `{ "erro": "documento_ilegivel", "dados": null }` — seu código trata
  erro e sucesso pela mesma porta (Aula 8).
- **Campos explícitos.** `"tem_desconto": false` é melhor que omitir o campo —
  omissão é ambígua.
- **Formato casado com o consumidor:** enum para um `switch`; JSON para vários
  campos; JSON Lines para pipeline item a item; markdown só quando é humano
  lendo.

A saída é uma **interface**: nomeie campos, fixe enums, e trate mudança de
formato como mudança de API (versione — bônus B1).

---

## 5. Anti-padrões

- **"Responda de forma organizada/clara."** Desejo, não contrato.
- **Aceitar o embrulho markdown** sem parse defensivo. Vai quebrar.
- **Enum aberto.** O `switch` cai no `default`.
- **Cortar raciocínio junto com a verbosidade.** Perde qualidade em tarefa
  difícil; separe raciocínio em vez de matá-lo.
- **Confiar sem validar.** Tratar a string do LLM como retorno tipado.
- **Formato humano onde precisa de máquina** (e vice-versa).

---

## Exercícios

1. **Do texto ao contrato.** Pegue um prompt que responde em prosa e redefina a
   saída como JSON com schema. Rode 5 vezes (temperature baixa). A estrutura saiu
   idêntica toda vez?

2. **Mate o preâmbulo.** Meça quantos tokens de "Claro! Aqui está..." um prompt
   frouxo gera. Adicione a instrução de saída estrita e meça de novo. Quanto você
   economizou por chamada?

3. **Parse defensivo.** Peça JSON **sem** dizer "sem markdown" e veja o embrulho
   aparecer. Escreva a função de parse que sobrevive a: resposta limpa, embrulhada
   e com texto antes.

4. **Enum + erro no formato.** Faça um roteador de intenção com 4 categorias +
   `"outros"`, e leve o caso de erro para dentro do JSON. Teste com uma mensagem
   que não encaixa e uma entrada inválida. Seu código tratou tudo pela mesma
   porta?

Soluções em [`../exercicios/solucoes.md`](../exercicios/solucoes.md).

---

## Resumo

- A saída é uma **API**: contrato estrito (enum/JSON com schema), nunca "de forma
  organizada".
- **Controle de verbosidade:** mate preâmbulo e explicação não pedida; o
  raciocínio útil vai para um **campo separado**, não para a resposta final.
- **JSON confiável:** structured outputs quando houver; senão "apenas JSON, sem
  markdown" + schema + **parse defensivo**. Sempre **valide**.
- **Formato estrito:** enums fechados com escape, erro dentro do formato, campos
  explícitos, formato casado com o consumidor.

---

## 🎉 Você concluiu as 11 aulas

Recapitulando a jornada — da Fase 01 (instável) à Fase 03 (maestria):

- **Mecânica** (Aulas 1–3): prompt como configuração, a caixa preta, engenharia
  de estado.
- **Raciocínio** (Aulas 4–7): motores, Chain of Thought, scaffolding, Tree of
  Thoughts.
- **Confiabilidade** (Aulas 8–11): consistência e válvulas de escape,
  auto-refinamento, personas modulares, design de saída.

Agora avance para a [**trilha complementar (bônus)**](../README.md#trilha-complementar-bônus)
— testes/evals, RAG, tool calling e agentes — para cumprir a promessa de
construir **agentes que não quebram**. E leve tudo para o
[**projeto final**](../exercicios/README.md#projeto-final-sugerido).

No espírito do curso: **você deixou de apertar botões e virou arquiteto.**
