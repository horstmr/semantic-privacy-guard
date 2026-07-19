# Módulo 9 — Processamento de Dados e Fine-Tuning de Modelos

> Quando prompt e RAG não bastam e você precisa mudar o **comportamento** do
> modelo. Tema totalmente novo em relação ao curso de Prompt Engineering.

## Unidade 1 — Quando fazer fine-tuning (decision framework)

Ordem de tentativa (do mais barato ao mais caro):

1. **Prompt engineering** — resolve a maioria dos casos;
2. **RAG** — quando o problema é *conhecimento* (dados privados/atualizados);
3. **Fine-tuning** — quando o problema é *comportamento*: formato/estilo muito
   específico, jargão de domínio, tarefa repetitiva em altíssimo volume (um modelo
   menor fine-tunado fica mais barato e rápido que um modelo grande com prompt gigante).

> **Regra de bolso:** **RAG ensina o que o modelo deve *saber*; fine-tuning ensina
> como ele deve *se comportar*.** Fine-tuning NÃO é a melhor forma de injetar
> conhecimento factual novo.

## Unidade 2 — Preparação de Datasets

- Coletar exemplos reais da tarefa (centenas a milhares de pares entrada→saída ideais);
- **Limpeza, balanceamento e padronização**: remover duplicatas/erros, equilibrar categorias, uniformizar formato;
- **Formato JSONL**: um JSON por linha, tipicamente
  `{"messages": [{"role":"system",...},{"role":"user",...},{"role":"assistant",...}]}`;
- **Qualidade > quantidade**: 500 exemplos excelentes e diversos superam 5.000
  medianos — o modelo aprende exatamente o que os dados mostram, **inclusive os erros**.

## Unidade 3 — Fine-tuning via API (OpenAI, Gemini)

Processo: (1) upload do dataset JSONL; (2) criar o job de fine-tuning;
(3) configurar hiperparâmetros básicos (épocas, learning rate); (4) monitorar
métricas de treino/validação; (5) usar o modelo pelo ID próprio.

**Boas práticas**: versionar datasets e modelos, documentar o que cada versão muda,
manter um conjunto de avaliação **fixo** para comparar versões de forma justa.

## Unidade 4 — LoRA e PEFT

- **PEFT** (Parameter-Efficient Fine-Tuning): família de técnicas que ajusta só uma
  pequena fração dos parâmetros, em vez do modelo inteiro;
- **LoRA** (Low-Rank Adaptation): congela os pesos originais e treina apenas pequenas
  **matrizes adaptadoras de baixo posto** inseridas nas camadas. Resultado: treino
  com fração da memória/custo, adaptadores de poucos MB que podem ser trocados sobre
  o mesmo modelo base;
- **Trade-offs**: LoRA é muito mais barato e rápido; full fine-tuning pode render um
  pouco mais em mudanças profundas de comportamento. Para a maioria dos casos, LoRA basta.

## Unidade 5 — Avaliar modelos fine-tunados

- **Quantitativas** (acurácia, exact match, similaridade) e **qualitativas**
  (avaliação humana ou LLM-as-judge com rubrica);
- Medir precisão, consistência e adequação em cenários variados — inclusive **fora
  da distribuição** de treino;
- **Testes A/B**: modelo customizado vs. genérico com casos reais;
- **Riscos**: *overfitting* (decora os exemplos) e **esquecimento catastrófico**
  (fica ótimo na tarefa e pior em todo o resto). Mitigação: dataset diverso, poucas
  épocas, avaliação também em tarefas gerais.

> **↩︎ Já coberto** — a disciplina de **evals, bateria de casos e LLM-as-judge**
> está no [Bônus B1](../curso-prompt-engineering-para-devs/bonus/b1-prompt-como-codigo-testes.md).
> Aqui aplicamos aos riscos específicos de fine-tuning.

## Unidade 6 — Projeto Final

Modelo fine-tunado para um domínio (atendimento, suporte, jurídico, financeiro):
preparar dados → treinar → avaliar → integrar num fluxo real (chatbot/agente) →
documentar e justificar as escolhas.

---

## ❓ Perguntas para fixar — Módulo 9

**1. RAG ou fine-tuning: como decidir?**
RAG quando o problema é conhecimento (dados privados/atualizados/factuais). Fine-tuning quando é comportamento (formato, estilo, jargão, volume). E tente prompt engineering primeiro.

**2. Por que fine-tuning não é bom para injetar fatos novos?**
Porque o modelo aprende padrões de comportamento, não memoriza fatos de forma confiável — pode misturar e alucinar. Fatos devem vir por RAG/contexto, verificáveis e atualizáveis.

**3. O que é o formato JSONL e como é um exemplo de treino?**
Um objeto JSON por linha. Cada linha traz uma conversa exemplo: mensagens de system, user e a resposta ideal do assistant que o modelo deve aprender a produzir.

**4. Por que qualidade supera quantidade no dataset?**
O modelo aprende exatamente o que os exemplos mostram — inclusive erros. Poucos exemplos excelentes e diversos moldam o comportamento melhor que muitos medianos.

**5. Explique LoRA em duas frases.**
Congela os pesos do modelo base e treina apenas pequenas matrizes adaptadoras de baixo posto nas camadas. Isso reduz drasticamente custo e memória, gerando adaptadores minúsculos e intercambiáveis.

**6. O que é esquecimento catastrófico e como mitigar?**
O modelo fine-tunado melhora na tarefa alvo mas degrada em capacidades gerais. Mitiga-se com dataset diverso, poucas épocas e avaliação contínua também em tarefas gerais.

**7. Por que manter um conjunto de avaliação fixo entre versões?**
Para comparar versões de forma justa: se o benchmark muda a cada versão, é impossível saber se o modelo melhorou ou se o teste ficou mais fácil.

**Próximo:** [Módulo 10 — Segurança e Governança →](./10-seguranca-e-governanca.md)
