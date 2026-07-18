# Módulo 7 — Ferramentas de IA para Gestão de Projetos

> IA aplicada ao "como o trabalho é planejado e entregue" — requisitos,
> priorização, estimativas, riscos, relatórios. Novo em relação ao curso de
> Prompt Engineering.

## Unidade 1 — Planejamento e Escopo (Requirements Copilot)

IA transformando transcrições de reuniões e descrições em **épicos, histórias de
usuário** ("Como [persona], quero [ação] para [benefício]") e requisitos;
decompondo tarefas e gerando **critérios de aceite** (Given/When/Then). O PM revisa
e valida — a IA acelera o rascunho, não substitui o julgamento.

## Unidade 2 — Priorização Inteligente de Backlog

Frameworks clássicos, agora com IA sugerindo pontuações:

- **RICE**: (Reach × Impact × Confidence) ÷ Effort — priorização quantitativa;
- **WSJF** (SAFe): Custo do Atraso ÷ Duração — mais valor por unidade de tempo;
- **MoSCoW**: Must / Should / Could / Won't — classificação de escopo.

IA simula impacto de funcionalidades, gera roadmap e aplica pontuação **consistente**
(humanos são inconsistentes ao pontuar 200 itens; a IA não).

## Unidade 3 — Cronograma, Capacidade e Alocação

- Cronogramas com **identificação automática de dependências**;
- Cenários **"what-if"**: "e se perdermos um dev em março?" — recalcula prazos e gargalos;
- Balanceamento de capacidade e ajustes conforme previsões de carga.

## Unidade 4 — Estimativas e Previsões

- Previsão de prazos/custos/esforço baseada em **dados históricos** (velocity real, não otimismo);
- **Simulação de Monte Carlo**: em vez de uma data única, roda milhares de simulações com as incertezas de cada tarefa e entrega probabilidades ("85% de chance de concluir até 15/09") — muito mais honesto que estimativa pontual.

## Unidade 5 — Riscos e Mitigações (AIOps de Projeto)

- Detecção de riscos de escopo/cronograma/recursos analisando padrões nos dados;
- Identificação de **anomalias** (queda de velocity, acúmulo de bugs, tarefas travadas) que preveem falhas;
- Geração de **planos de mitigação** baseados em histórico.

## Unidade 6 — Reuniões Turbinadas

- Transcrição e **resumo automático**;
- Extração de **decisões, pendências e action items**;
- Integração com boards: o action item vira card automaticamente, com dono e prazo.

## Unidade 7 — Status Reports e Executive Summaries

- Relatórios de sprint/projeto/portfólio a partir dos dados reais (board, commits, métricas);
- **Adaptação por público**: o mesmo dado vira relatório técnico (time), executivo (diretoria) e tático (gestor);
- Consolidação de desempenho em linguagem natural.

## Unidade 8 — Governança, Compliance e Qualidade

- Verificação de conformidade e **rastreabilidade** (requisito → código → teste → entrega);
- Checklists automatizados e trilhas de decisão para auditorias;
- Geração e controle de documentação de qualidade.

## Unidade 9 — Automação em Jira/Asana/Trello/Notion/Slack

- Automações integradas às plataformas (via APIs e MCPs);
- **Bots em JavaScript** para criar, atualizar e priorizar cards;
- **NL → workflow**: descrever o fluxo em linguagem natural e a IA gera a automação;
- Regras de negócio automatizadas: lembretes, notificações, aprovações.

## Unidade 10 — Portfólio e OKRs

- Alinhamento automático projeto ↔ **OKRs** (Objectives & Key Results);
- Análise de **outcome** (resultado real) vs. **output** (entregas) para priorizar;
- Avaliação de impacto e propostas de ajuste baseadas em dados.

---

## ❓ Perguntas para fixar — Módulo 7

**1. Como se calcula o score RICE?**
(Reach × Impact × Confidence) ÷ Effort. Maior alcance, impacto e confiança, e menor esforço → maior prioridade.

**2. O que o WSJF prioriza?**
O maior valor por unidade de tempo: Custo do Atraso ÷ Duração — itens valiosos e rápidos sobem.

**3. Por que Monte Carlo é melhor que uma estimativa de data única?**
Porque incorpora a incerteza: simula milhares de cenários e entrega probabilidades por data, permitindo compromissos realistas ("85% até dia X") em vez de um chute.

**4. Diferença entre outcome e output?**
Output é o que foi entregue (features, tarefas); outcome é o resultado gerado (retenção, receita, satisfação). OKRs e portfólio priorizam outcome.

**5. Exemplo de anomalia de projeto detectável precocemente?**
Queda sustentada de velocity, aumento anormal de bugs reabertos ou tarefas paradas além do padrão histórico — sinais antecedentes de atraso.

**6. O que significa NL → workflow?**
Descrever uma automação em linguagem natural ("quando o card for para 'Review', notifique o líder e crie checklist X") e a IA gerar o workflow executável na ferramenta.

**Próximo:** [Módulo 8 — Arquitetura de sistemas com IA →](./08-arquitetura-de-sistemas-com-ia.md)
