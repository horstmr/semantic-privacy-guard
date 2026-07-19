# Aula 6 — O Antídoto para a "IA Júnior"
## Construindo o Scaffolding

> Quando o modelo entrega respostas de "estagiário" — genéricas, incompletas,
> sem os detalhes que importam — o problema raramente é o modelo. É a falta de
> **andaimes** (scaffolding) que elevam o nível do que ele produz.

## Objetivos

- Entender **scaffolding**: os andaimes que estruturam a resposta antes de o
  modelo escrevê-la.
- Usar **exemplos canônicos** (few-shot) como a forma mais forte de andaime.
- Guiar por **template, processo e critérios** para transformar "IA júnior" em
  "IA sênior".

---

## 1. O que é scaffolding

*Scaffolding* (andaime) é toda estrutura que você fornece para o modelo
**apoiar** a resposta enquanto a constrói — e que remove o espaço para ele
entregar o mínimo. Assim como um andaime sustenta o pedreiro até a parede se
sustentar sozinha, o scaffolding sustenta o raciocínio do modelo até a resposta
ficar de pé.

A "IA júnior" aparece quando você deixa o modelo se virar sozinho: ele preenche
as lacunas com o mais genérico e plausível (a ilusão da Aula 1). O scaffolding
**preenche as lacunas com a sua estrutura**, e o nível da resposta sobe junto.

Formas de andaime, da mais leve à mais forte:

1. **Template de saída** — os campos/seções que a resposta deve ter.
2. **Processo** — os passos a seguir (parente do CoT da Aula 5).
3. **Critérios de qualidade** — o que faz a resposta ser boa.
4. **Exemplos canônicos** — demonstrações resolvidas (o andaime mais forte).

---

## 2. Exemplos canônicos: o andaime mais forte

Descrever comportamento em linguagem natural é impreciso. Um exemplo é uma
**especificação executável**: mostra o formato de entrada, o de saída e o
mapeamento entre eles, sem espaço para interpretação.

**Só descrição (vira júnior):**

```
Extraia as habilidades técnicas e retorne de forma organizada.
```

**Com exemplo (vira sênior):**

```
Extraia habilidades técnicas (linguagens, frameworks, ferramentas). Ignore
habilidades comportamentais.

Exemplo:
Entrada: "5 anos com Python e Django, sou proativo e conheço Docker."
Saída: ["Python", "Django", "Docker"]

Agora processe:
Entrada: "{{texto}}"
Saída:
```

O exemplo respondeu de uma vez: formato (array JSON), granularidade
(tecnologias, não anos) e escopo ("proativo" ficou de fora).

### Ensine os casos difíceis

Os exemplos mais valiosos mostram **o que fazer quando é ambíguo**. Se uma regra
de borda é difícil de descrever, *demonstre-a*:

```
<exemplos>
<!-- normal -->
Entrada: "Reunião amanhã 15h com o backend."
Saída: {"tipo":"reuniao","hora":"15:00","quem":"time de backend"}
<!-- sem hora: regra = null, não invente -->
Entrada: "Preciso falar com você sobre o deploy."
Saída: {"tipo":"conversa","hora":null,"quem":"voce"}
<!-- fora de escopo: não é evento -->
Entrada: "Bom dia, tudo bem?"
Saída: {"tipo":"nenhum","hora":null,"quem":null}
</exemplos>
```

Três exemplos ensinam os limites da tarefa melhor que um parágrafo de regras.

### Zero → few, na medida certa

- **Comece zero-shot.** Se o formato sai inconsistente ou os casos sutis erram,
  **adicione exemplos um a um** até estabilizar.
- **Não empilhe dez "por garantia".** Cada exemplo custa tokens e pode enviesar.
- **Equilibre as classes.** Quatro exemplos todos "positivos" ensinam o modelo a
  puxar para positivo. Varie e balanceie.
- **Consistência absoluta.** Todos os exemplos no formato exato que você quer — o
  modelo imita o padrão, inclusive erros.

---

## 3. Template e processo como andaime

Quando exemplos são caros ou a tarefa é aberta, os andaimes de **template** e
**processo** já elevam muito:

```
Escreva a análise seguindo EXATAMENTE esta estrutura:

## Diagnóstico
(1 frase: qual o problema central)

## Causa provável
(a causa mais provável e por quê)

## Correção recomendada
(passos concretos, numerados)

## Riscos da correção
(o que pode dar errado)
```

O template proíbe a resposta-parágrafo-genérico. O modelo tem que preencher
cada seção — e seções vazias expõem o que ele não sabe (melhor do que esconder
numa prosa fluida).

---

## 4. Critérios de qualidade explícitos

O modelo não adivinha o seu padrão de "bom". Escreva-o:

```
Uma boa resposta aqui: cita a linha exata do código; propõe correção testável;
considera o caso de entrada vazia; não sugere reescrever o que já funciona.
```

Dar os critérios é montar o andaime da avaliação **dentro** do prompt — e prepara
o terreno para o auto-refinamento da Aula 9, onde o modelo usa esses mesmos
critérios para revisar a si mesmo.

---

## 5. Anti-padrões

- **Deixar o modelo se virar** e reclamar que a resposta é júnior. Sem andaime,
  júnior é o esperado.
- **Exemplos inconsistentes** entre si — ensinam inconsistência.
- **Só o caso feliz** nos exemplos — o modelo não sabe o que fazer na borda.
- **Exemplos enviesados** (todos da mesma classe) — puxam a saída para ela.
- **Template ausente** em tarefa aberta — convida o parágrafo genérico.
- **Andaime demais numa tarefa trivial** — cinco exemplos para "positivo/negativo"
  é custo sem ganho.

---

## Exercícios

1. **Júnior → sênior.** Pegue uma resposta genérica que o modelo te deu. Adicione
   um **template** de saída e rode de novo. Depois adicione **um exemplo
   canônico**. A cada andaime, quanto o nível subiu?

2. **Ensine a borda com exemplo.** Crie uma tarefa com um caso chato (ex: "quando
   houver dois preços, use o com desconto"). Ensine isso **só com regra** e
   depois **só com um exemplo**. Qual foi mais confiável em 4 entradas?

3. **Provoque o viés.** Classificador com 4 exemplos **todos positivos**. Rode em
   textos negativos — puxou para positivo? Reequilibre (2/2) e repita.

4. **Corte o excesso.** Pegue um prompt com 5+ exemplos. Remova um por vez,
   rodando a bateria a cada remoção. Qual o número mínimo que mantém a qualidade?

Soluções em [`../exercicios/solucoes.md`](../exercicios/solucoes.md).

---

## Resumo

- A "**IA júnior**" é sintoma de **falta de scaffolding**, não do modelo.
- **Andaimes**, da mais leve à mais forte: template → processo → critérios →
  **exemplos canônicos**.
- Exemplos são **especificações executáveis**: fixam formato, granularidade e
  escopo, e ensinam **casos de borda** melhor que regras.
- Comece zero-shot, adicione exemplos até estabilizar, **equilibre classes** e
  mantenha **consistência absoluta** — sem exagerar na dose.

**Próximo:** [Aula 7 — Múltiplas Realidades →](./aula-07-multiplas-realidades-tree-of-thoughts.md)
