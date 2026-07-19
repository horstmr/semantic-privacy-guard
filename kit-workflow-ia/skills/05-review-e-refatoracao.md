# Skills — Review & Refatoração

> A rede de segurança do workflow. Lembre: a IA acelera, mas a **palavra final é
> humana** ([Módulo 05](../modulos/05-codigo-previsivel-revisavel-rastreavel.md)).

---

## revisar-contra-spec
**Quando usar:** o passo de review mais importante — checar código contra a spec, requisito a requisito.
**Entradas:** a spec (requisitos + CAs) + o código.
**Saída:** para cada requisito, atendido/não/parcial, com evidência no código.

**Prompt:**
```
Revise o código <codigo> contra a spec <spec>. Para CADA requisito (Rn) e critério
de aceite (CAn), diga: ATENDIDO / PARCIAL / NÃO ATENDIDO, apontando o trecho de
código que o cumpre (ou o que falta). Depois liste: requisitos sem cobertura,
código que NÃO corresponde a nenhum requisito (órfão/escopo extra), e bordas da
spec não tratadas. Não elogie; seja um checklist objetivo.
<spec>{{spec}}</spec> <codigo>{{codigo}}</codigo>
```

---

## caca-bugs-adversarial
**Quando usar:** encontrar o cenário que quebra o código em produção.
**Entradas:** o código.
**Saída:** bugs concretos com o input exato que os dispara.

**Prompt:**
```
Aja como um revisor ADVERSARIAL cujo trabalho é QUEBRAR este código em produção.
Para cada bug encontrado em <codigo>, dê: (1) o input/estado exato que dispara,
(2) o que acontece de errado, (3) a severidade, (4) a correção. Procure por:
null/undefined, off-by-one, condição invertida, concorrência/race, recurso vazando,
erro engolido, e alucinação de API (função/flag inexistente). Se não achar bug
numa categoria, diga "ok". Não invente bugs — mostre o input que prova cada um.
<codigo>{{codigo}}</codigo>
```

---

## revisar-seguranca
**Quando usar:** antes de subir código que lida com entrada externa, auth ou dados sensíveis.
**Entradas:** o código.
**Saída:** vulnerabilidades por severidade, com vetor e correção.

**Prompt:**
```
Revise <codigo> sob a ótica de segurança. Aponte apenas vulnerabilidades EXPLORÁVEIS,
por severidade: injeção (SQL/comando/prompt), validação de entrada ausente,
autorização/autenticação furada, dados sensíveis em log/resposta, secrets no código,
SSRF/path traversal, e dependência insegura. Para cada: o vetor de ataque em 1 frase
e a correção. Ignore estilo. Se não houver, diga "sem achados".
<codigo>{{codigo}}</codigo>
```

---

## refatorar-com-seguranca
**Quando usar:** melhorar código sem mudar comportamento.
**Entradas:** o código + o objetivo da refatoração.
**Saída:** o código refatorado + garantia de que os testes continuam válidos.

**Prompt:**
```
Refatore <codigo> com o objetivo <objetivo> SEM mudar o comportamento observável.
Regras: não altere a interface pública sem avisar; preserve os testes existentes
(eles devem continuar passando); faça em passos pequenos e descreva cada mudança e
por quê. Se um teste precisar mudar, isso é sinal de mudança de comportamento —
PARE e avise. Não introduza libs novas sem justificar.
Saída: código refatorado + lista de mudanças + confirmação de que a suíte cobre isto.
<codigo>{{codigo}}</codigo> <objetivo>{{objetivo}}</objetivo>
```

---

## reduzir-complexidade
**Quando usar:** uma função/classe ficou difícil de entender ou testar.
**Entradas:** o trecho complexo.
**Saída:** versão mais simples, com o raciocínio da simplificação.

**Prompt:**
```
Reduza a complexidade de <codigo> mantendo o comportamento. Ataque: aninhamento
profundo (early return/guard clauses), condições longas (extrair para funções/
predicados nomeados), responsabilidades misturadas (separar), e nomes obscuros.
Não "espalhe" complexidade — reduza de fato (menos caminhos, mais legível).
Explique cada simplificação. Confirme que os testes existentes cobrem a mudança.
<codigo>{{codigo}}</codigo>
```

---

## revisar-performance
**Quando usar:** suspeita de gargalo, mas só depois de estar correto.
**Entradas:** o código + o contexto de uso (volume, frequência).
**Saída:** gargalos reais com impacto estimado e correção.

**Prompt:**
```
Analise <codigo> para performance no contexto <contexto> (volume/frequência).
Aponte apenas gargalos REAIS: N+1 queries, laços aninhados sobre dados grandes,
alocação/serialização desnecessária, falta de índice/paginação, chamada síncrona
que poderia ser lote. Para cada: impacto estimado e a correção. NÃO micro-otimize o
que não é gargalo (não sacrifique legibilidade por ganho irrelevante).
<codigo>{{codigo}}</codigo> <contexto>{{uso}}</contexto>
```

---

## explicar-codigo-legado
**Quando usar:** entender código herdado antes de mexer.
**Entradas:** o código legado.
**Saída:** o que ele faz, suas suposições, riscos e onde tocar com cuidado.

**Prompt:**
```
Explique o código legado <codigo>: (1) o que faz, em alto nível; (2) as suposições
implícitas e efeitos colaterais; (3) as partes frágeis/perigosas de mexer; (4) o
que testar ANTES de alterar (caracterização). Marque o que você NÃO tem certeza
(não invente intenção). Saída em seções.
<codigo>{{codigo}}</codigo>
```

---

← [Testes & Qualidade](./04-testes-e-qualidade.md) · [Catálogo](./README.md) · [Docs & DevOps →](./06-docs-e-devops.md)
