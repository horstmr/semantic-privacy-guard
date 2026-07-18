# Módulo 04 — Pilar 4: Formato de saída

> A saída do seu prompt vai ser consumida por **outro código**, não por um
> humano generoso que "entende o que você quis dizer". Trate-a como uma API.

## Objetivos de aprendizagem

- Especificar formatos de saída **parseáveis e estáveis** (JSON, enum, etc.).
- Obter **JSON confiável** de um LLM — e lidar com o que dá errado.
- Usar **schema / structured outputs** quando o provedor oferece.
- Projetar a saída pensando em quem vai consumi-la (validação, erros, versão).

---

## 1. "Responda de forma organizada" é uma promessa vazia

Se outro código vai ler a resposta, a saída precisa ser **um contrato**, tão
rígido quanto o retorno de uma função. Comparações:

**Ruim (formato à mercê do modelo):**

```
Analise o sentimento e me diga o resultado.
```

Saída possível hoje: *"O sentimento do texto é claramente positivo, pois..."*
Saída possível amanhã: *"Positivo 😊"*. Seu `if resposta == "positivo"` quebra.

**Bom (contrato de saída):**

```
Classifique o sentimento. Responda com exatamente uma palavra, minúscula, sem
pontuação: positivo, negativo ou neutro. Nada além disso.
```

Melhor ainda quando há mais de um campo: **peça JSON**.

```
Responda apenas com um objeto JSON, sem texto antes ou depois, no formato:
{
  "sentimento": "positivo" | "negativo" | "neutro",
  "confianca": number,   // 0 a 1
  "trecho_chave": string // a frase que mais indicou o sentimento
}
```

---

## 2. Como conseguir JSON confiável

LLMs adoram ser prestativos e "explicar" antes do JSON, ou embrulhar em
```` ```json ````. Isso quebra o parser. Defesas, da mais forte para a mais
fraca:

1. **Use structured outputs / JSON mode se o provedor oferece** (ver seção 3).
   É a defesa definitiva: o formato é garantido pela API, não pedido por
   gentileza.
2. **Peça explicitamente "apenas o JSON, sem texto antes ou depois, sem
   markdown".** Repita isso no fim do prompt.
3. **Forneça o schema no prompt** com os tipos e valores permitidos.
4. **Pré-preencha o início da resposta** com `{` quando a API permite (isso
   força o modelo a começar já no JSON).
5. **Parse defensivo no seu código**: mesmo com tudo acima, trate a resposta
   como entrada não confiável — extraia o bloco JSON, `try/parse`, valide o
   schema, e tenha um plano B (retry, valor default, erro controlado).

Exemplo de parse defensivo (JS):

```js
function parseModelJson(raw) {
  // 1. tira cercas de markdown se vierem
  const cleaned = raw.trim().replace(/^```(?:json)?/i, "").replace(/```$/, "").trim();
  // 2. tenta parsear
  let obj;
  try {
    obj = JSON.parse(cleaned);
  } catch {
    // 3. plano B: extrai o primeiro {...} equilibrado
    const match = cleaned.match(/\{[\s\S]*\}/);
    if (!match) throw new Error("resposta sem JSON");
    obj = JSON.parse(match[0]);
  }
  // 4. valida o contrato (aqui, à mão; em produção use zod/pydantic)
  if (!["positivo", "negativo", "neutro"].includes(obj.sentimento)) {
    throw new Error("sentimento inválido: " + obj.sentimento);
  }
  return obj;
}
```

> **Princípio:** nunca confie cegamente na saída de um LLM, mesmo formatada.
> Ele é um gerador probabilístico, não um serializador. Valide como você
> validaria a resposta de um serviço externo instável.

---

## 3. Structured outputs / JSON mode (a defesa definitiva)

Vários provedores permitem **forçar** a saída a obedecer um schema
(JSON Schema / função tipada). Quando disponível, **use** — elimina a classe
inteira de bugs "o modelo pôs texto antes do JSON".

Exemplo conceitual (API estilo OpenAI, com JSON Schema):

```python
schema = {
    "type": "object",
    "properties": {
        "sentimento": {"type": "string", "enum": ["positivo", "negativo", "neutro"]},
        "confianca": {"type": "number", "minimum": 0, "maximum": 1},
    },
    "required": ["sentimento", "confianca"],
    "additionalProperties": False,
}

resp = client.chat.completions.create(
    model="...",
    messages=[...],
    response_format={"type": "json_schema", "json_schema": {"name": "analise", "schema": schema}},
)
# resp agora é garantidamente um JSON que bate com o schema
```

Ferramentas que ajudam a definir e validar schema no seu código:

- **Python:** `pydantic` (define o modelo como classe, valida e serializa).
- **TypeScript/JS:** `zod` (define o schema, faz `parse` que lança em dado
  inválido).

Mesmo com structured outputs, mantenha validação no seu lado — schemas cobrem
forma, não semântica ("confiança 0.99" pode estar formalmente válida e ainda
assim errada).

---

## 4. Escolha o formato certo para o consumidor

Nem tudo precisa ser JSON. Combine o formato com quem consome:

| Consumidor | Formato ideal |
|------------|---------------|
| Código que faz `switch`/`if` numa categoria | **enum** (uma palavra de um conjunto fixo) |
| Código que precisa de vários campos | **JSON** com schema |
| Outro sistema que espera CSV/planilha | **CSV** com cabeçalho fixo (cuidado com vírgulas no conteúdo) |
| Humano lendo na tela | **markdown** (títulos, listas, tabela) |
| Pipeline que processa item a item | **JSON Lines** (um objeto por linha) |

E deixe explícito **o que fazer no caso de erro dentro do próprio formato**.
Ex.: em vez de o modelo escrever "não consegui", faça-o retornar
`{ "erro": "documento_ilegivel", "dados": null }` — assim seu código trata erro
e sucesso pela mesma porta.

---

## 5. Estabilidade ao longo do tempo

Sua saída é uma **interface**. Trate mudanças como você trataria mudança de API:

- **Nomeie os campos** e não mude nomes à toa (quebra quem consome).
- **Fixe os valores de enum** e documente-os. Se adicionar um valor novo, saiba
  que o código a jusante precisa lidar com ele.
- **Versione** o formato quando fizer mudança incompatível (ver Módulo 06).
- Prefira campos **explícitos** a implícitos: `"tem_desconto": false` é melhor
  que "omitir o campo quando não tem" — omissão é ambígua.

---

## 6. Anti-padrões de saída

- **"Responda de forma organizada/clara/bonita."** Não é contrato. É desejo.
- **Pedir JSON e aceitar o embrulho markdown** sem tratar no código. Vai quebrar.
- **Enum aberto.** "Diga a categoria" sem listar as categorias → o modelo
  inventa uma sexta categoria e seu `switch` cai no `default`.
- **Misturar prosa e dado.** "O total é R$ 340, calculado somando..." quando
  você só queria o número. Peça **só** o dado.
- **Confiar sem validar.** Tratar a string do LLM como se fosse retorno tipado
  de uma função sua.
- **Formato humano onde precisa de máquina** (e vice-versa): mandar JSON cru pro
  usuário final, ou markdown floreado pro parser.

---

## Exercícios

1. **Do texto ao contrato.** Pegue um prompt seu que hoje responde em prosa e
   redefina a saída como JSON com schema (campos, tipos, enums). Rode 5 vezes.
   O JSON saiu idêntico em estrutura toda vez?

2. **Quebre o parser de propósito.** Peça JSON sem dizer "sem markdown". Veja se
   o modelo embrulha em ```` ```json ````. Agora escreva a função de parse
   defensivo (como a da seção 2) que sobrevive a isso. Teste com: resposta
   limpa, resposta embrulhada, resposta com texto antes.

3. **Enum fechado.** Faça um roteador de intenção que classifica a mensagem do
   usuário em uma de 4 intenções. Force-o a sair **exatamente** uma das quatro
   palavras. Teste com uma mensagem que não encaixa em nenhuma — ele inventou
   uma quinta? Adicione a intenção `"outros"` e feche a borda.

4. **Erro pela mesma porta.** Redesenhe uma saída para que o caso de erro venha
   **dentro** do JSON (`{ "erro": ..., "dados": null }`) em vez de o modelo
   escrever uma frase de desculpa. Ajuste seu código para tratar erro e sucesso
   no mesmo caminho.

Soluções em [`exercicios/solucoes.md`](./exercicios/solucoes.md).

---

## Resumo

- A saída é uma **API**: especifique-a como contrato (enum, JSON com schema),
  nunca "de forma organizada".
- Para **JSON confiável**: use structured outputs quando houver; senão peça
  "apenas JSON, sem markdown", forneça o schema, e **faça parse defensivo**.
- **Valide sempre** — o LLM é gerador probabilístico, não serializador.
- Combine o **formato ao consumidor** e leve o **caso de erro para dentro do
  formato**.
- Trate mudanças de formato como **mudanças de interface**: nomeie, fixe enums,
  versione.

**Fim dos quatro pilares.** Você já tem a base para escrever prompts de
produção. Os próximos módulos aprofundam a técnica e levam à produção de verdade.

**Próximo:** [Módulo 05 — Raciocínio (reasoning) →](./05-raciocinio-reasoning.md)
