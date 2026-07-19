# Módulo 04 — Skills que Trabalham Dentro da Sua Arquitetura

> Uma **skill** é uma receita de prompt/fluxo **versionada** que executa uma etapa
> do workflow sempre do mesmo jeito — no seu padrão, na sua arquitetura. É prompt
> engineering transformado em ferramenta reutilizável.

## Objetivos

- Entender o que é uma skill e por que ela vence o "prompt improvisado".
- Conhecer a **anatomia** de uma skill e como escrever a sua.
- Usar a [biblioteca de 40+ skills](../skills/README.md) prontas.

---

## 1. Prompt improvisado vs. skill

Todo dia você repete tarefas: gerar um caso de uso, escrever testes, revisar um
PR, criar um endpoint. Se você reescreve o prompt toda vez, a qualidade oscila e
o resultado sai fora do padrão da arquitetura.

Uma **skill** empacota o prompt certo — com o contexto da sua arquitetura, o
formato de saída e os casos de borda — para que a etapa saia **igual e correta
toda vez**. É a diferença entre "cozinhar de cabeça" e seguir uma receita testada.

| Prompt improvisado | Skill |
|--------------------|-------|
| Qualidade oscila | Consistente |
| Ignora a arquitetura | Já embute as fronteiras |
| Formato imprevisível | Saída fixa e revisável |
| Some quando você fecha o chat | Versionada no repo |

---

## 2. Anatomia de uma skill

Cada skill do kit tem esta estrutura (veja o
[catálogo](../skills/README.md)):

```markdown
## <nome-da-skill>
**Quando usar:** <o gatilho — em que etapa/situação>
**Entradas:** <o que você fornece: spec, arquivo, requisito...>
**Saída:** <o formato exato do que ela produz>

**Prompt:**
<o prompt pronto, com placeholders {{...}}, já embutindo:
 - papel/objetivo (R.O.C.C.O.)
 - a arquitetura/convenções do projeto
 - o formato de saída
 - as regras de borda e o "não faça">
```

Repare que a skill é a **Aula 1 (R.O.C.C.O.) + Aula 8 (guardrails) + Aula 11
(saída como API)** do Prompt Engineering, congeladas para reuso.

---

## 3. Skills ancoradas na arquitetura

O segredo do "trabalhar dentro da sua arquitetura": a skill **referencia as
fronteiras** que você declarou (Módulo 03). Exemplo de uma skill `criar-caso-de-uso`:

```
Crie um caso de uso seguindo a Clean Architecture deste projeto:
- Local: src/application/use-cases/{{contexto}}/
- Depende de interfaces (ports) declaradas em src/application/ports/, nunca de infra.
- Recebe um Input DTO, retorna um Output DTO (ou um Result de erro).
- Regras de negócio ficam no domínio (src/domain), não aqui.
- Gere também o teste do caso de uso cobrindo os critérios de aceite {{CAs}}.
Entrada: requisito {{Rn}} e critérios {{CAs}} da spec.
Saída: (1) o arquivo do caso de uso, (2) o arquivo de teste, citando os CAs.
```

Como a skill já sabe onde as coisas moram, o código sai encaixado — não importa
quem (ou qual modelo) a executa.

---

## 4. Skills compõem o workflow inteiro

O [catálogo](../skills/README.md) cobre todas as etapas do loop (Módulo 01),
organizadas por fase:

- **Planejamento & Spec** — afiar spec, gerar critérios de aceite, decompor tarefas.
- **Arquitetura & Design** — propor abordagem, criar ADR, desenhar contexto DDD.
- **Implementação** — caso de uso, command/query (CQRS), entidade, endpoint, adapter.
- **Testes & Qualidade** — testes de CA, testes de borda, property-based, mutação.
- **Review & Refatoração** — revisar contra spec, achar bugs, refatorar com segurança.
- **Docs & DevOps** — README, ADR, changelog, pipeline CI, mensagem de commit/PR.

Encadeadas, elas **são** o SDD executável: `afiar-spec` → `decompor-tarefas` →
`criar-caso-de-uso` → `gerar-testes-ca` → `revisar-contra-spec`.

---

## 5. Como criar a sua skill

1. Pegue uma tarefa que você repete e **capture o melhor prompt** que já usou.
2. Enxerte a **arquitetura** (onde mora, quais fronteiras) e o **formato de saída**.
3. Feche as **bordas** (o que fazer quando falta info, o "não faça").
4. Troque o específico por **placeholders** `{{...}}`.
5. **Versione** no repo (em `skills/`), com um exemplo de uso.
6. **Meça** (Bônus B1 do Prompt Eng.): rode em 3-4 casos e ajuste até estabilizar.

---

## 6. Anti-padrões

- **Skill sem arquitetura embutida** → gera código fora do padrão.
- **Skill que faz tudo** → vira um mega-prompt frágil. Uma skill = uma etapa.
- **Saída não fixada** → o resultado não é revisável nem componível.
- **Nunca versionar** → volta a ser prompt improvisado.
- **Skill que ninguém testou** → propaga um erro em escala.

---

## Exercícios

1. **Skill a partir de um prompt seu.** Pegue um prompt que você repete e
   transforme em skill (quando usar, entradas, saída, prompt com placeholders +
   arquitetura). Rode em 3 casos.

2. **Ancore na arquitetura.** Adicione à sua skill a referência às pastas/fronteiras
   do seu projeto. O código gerado passou a nascer no lugar certo?

3. **Encadeie.** Escolha 3 skills do catálogo e execute-as em sequência numa
   feature pequena (spec → tarefas → código). O output de uma alimentou a próxima?

4. **Meça.** Rode uma skill em 4 entradas variadas. A saída saiu no mesmo formato?
   Se oscilou, aperte o prompt (Aula 11: saída como API).

---

## Resumo

- **Skill = prompt/fluxo versionado** que executa uma etapa igual toda vez.
- Ela embute **R.O.C.C.O. + arquitetura + formato + bordas** — congelados para reuso.
- Ancorada nas **fronteiras** da arquitetura, o código sai encaixado.
- Encadeadas, as skills **são** o SDD executável.
- Uma skill = uma etapa; versionada e medida.

**Próximo:** [Módulo 05 — Previsível, revisável, rastreável →](./05-codigo-previsivel-revisavel-rastreavel.md)
