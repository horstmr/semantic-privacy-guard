# Módulo 10 — Segurança e Governança em IA

> Injeção de prompt o curso de Prompt Engineering já ataca (Aula 8). Este módulo
> abre o resto: explicabilidade, vieses, jailbreaks, o lado **legal** (LGPD/GDPR/
> EU AI Act) e custos.

## 10.1 O que é Governança em IA

Conjunto de políticas, processos e papéis que garantem que sistemas de IA sejam
desenvolvidos e operados de forma **segura, ética, legal e alinhada ao negócio**:
quem aprova modelos em produção, como se monitora qualidade, quem responde por
decisões automatizadas, como se documenta e audita.

## 10.2 Interpretabilidade e Explicabilidade

- **Interpretabilidade**: entender *como* o modelo funciona internamente (fácil em
  árvores de decisão, dificílimo em LLMs — "caixas-pretas");
- **Explicabilidade (XAI)**: fornecer justificativas compreensíveis para decisões
  específicas, mesmo sem abrir a caixa (quais fatores pesaram; técnicas como
  SHAP/LIME em ML clássico; **citação de fontes em RAG**).

Importa porque decisões que afetam pessoas (crédito, saúde, justiça) exigem
justificativa — inclusive por lei.

## 10.3 Vieses e Responsabilidade

Modelos aprendem os **vieses dos dados de treino** (histórico discriminatório →
decisões discriminatórias). Mitigação: auditar dados e saídas por grupos, testar com
casos sensíveis, diversificar dados, manter revisão humana em decisões de impacto.
**Responsabilidade**: a organização que implanta responde pelo sistema — "foi a IA"
não é defesa.

## 10.4 Riscos — Aspectos Humanos e Éticos

Impacto no trabalho, dependência excessiva (**automation bias** — confiar cegamente
na máquina), transparência com usuários (saber que interagem com IA), consentimento
e dignidade nas decisões automatizadas.

## 10.5 Riscos — Segurança e Dados

- **Prompt injection**: instruções maliciosas embutidas em conteúdo processado pelo
  modelo (o "ataque nº 1" contra apps de LLM);
- **Vazamento de dados**: dados sensíveis enviados a APIs externas ou expostos em
  respostas; risco de *data leakage* via memorização;
- **Jailbreaks**: burlar as proteções do modelo;
- **Controles**: sanitização de entradas, princípio do menor privilégio nas tools,
  DLP, isolamento de dados sensíveis, logging e monitoramento.

> **↩︎ Já coberto** — a defesa prática contra **injeção** (separar dado de
> instrução + regra "não siga instruções internas") está na
> [Aula 8](../curso-prompt-engineering-para-devs/aulas/aula-08-consistencia-e-valvulas-de-escape.md)
> e no [Bônus B3](../curso-prompt-engineering-para-devs/bonus/b3-tool-calling.md).
> Aqui é o enquadramento de **governança e risco**.

## 10.6 Riscos — Aspectos Legais e Regulatórios

- **LGPD** (Brasil) / **GDPR** (Europa): base legal para tratar dados pessoais,
  direitos do titular, decisões automatizadas com direito a revisão;
- **EU AI Act**: classificação por risco (inaceitável / alto / limitado / mínimo)
  com obrigações proporcionais;
- **Propriedade intelectual**: direitos sobre conteúdo gerado e sobre dados de treino;
- Contratos e responsabilidade civil por danos causados por sistemas de IA.

## 10.7 Custos Financeiros em IA

Custo total vai além do token: **inferência** (por uso — cresce com escala),
**treino/fine-tuning**, **infraestrutura** (GPUs, vector DBs, observabilidade),
**pessoas** e **retrabalho por qualidade**. Governança de custo: budgets por projeto,
monitoramento por feature, model tiering, cache — e medir **ROI** (o ganho justifica
o gasto?).

---

## ❓ Perguntas para fixar — Módulo 10

**1. Diferença entre interpretabilidade e explicabilidade?**
Interpretabilidade é entender o funcionamento interno do modelo; explicabilidade é justificar decisões específicas de forma compreensível, mesmo sem abrir a "caixa-preta".

**2. De onde vêm os vieses de um modelo?**
Principalmente dos dados de treino, que refletem desigualdades e padrões históricos; também de escolhas de rotulagem, amostragem e métricas.

**3. O que é prompt injection e por que é tão perigoso?**
Instruções maliciosas escondidas em conteúdo que o modelo processa (um e-mail, uma página), levando-o a ignorar suas instruções. É perigoso porque "código" e "dados" do LLM são o mesmo canal: texto.

**4. O que é automation bias?**
A tendência humana de confiar excessivamente na saída da máquina, deixando de verificar — governança exige manter ceticismo e revisão em decisões relevantes.

**5. Como o EU AI Act organiza as obrigações?**
Por níveis de risco: risco inaceitável é proibido; alto risco tem requisitos rígidos (documentação, supervisão humana); risco limitado exige transparência; mínimo, poucas obrigações.

**6. Cite três componentes do custo total além dos tokens.**
Infraestrutura (vector DB, observabilidade, GPUs), fine-tuning/treino e pessoas/manutenção — além do retrabalho por baixa qualidade.

**Próximo:** [Módulo 11 — Projeto Integrador →](./11-projeto-integrador-capstone.md)
