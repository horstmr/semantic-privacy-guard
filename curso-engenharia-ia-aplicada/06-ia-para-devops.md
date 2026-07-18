# Módulo 6 — Ferramentas de IA para DevOps

> IA aplicada a infraestrutura, do IaC ao incidente. Domínio inteiro, novo em
> relação ao curso de Prompt Engineering.

## Unidade I — Fundamentos de IA Generativa para Infra

**APIs** (OpenAI, Anthropic, **AWS Bedrock** — acesso a vários modelos dentro da
AWS com IAM/VPC), **frameworks de agentes** (LangChain, CrewAI, AutoGen) e **RAG
sobre documentação técnica** (runbooks, wikis, READMEs) para respostas fundamentadas
no *seu* ambiente.

**Prompting para infra**: forneça versões exatas (Terraform x.y, K8s 1.xx), o
estado atual (trecho de manifest/plan) e exija saída validável. **Limitação
crítica**: LLMs alucinam flags e recursos inexistentes → **validação obrigatória**
(planos, dry-runs, linters) antes de aplicar qualquer coisa gerada por IA.

## Unidade II — IaC Copilot

- **NL → IaC**: descrever a infra em linguagem natural e gerar **Terraform, Pulumi, Helm Charts**;
- **Validação automatizada**: `terraform validate` (sintaxe) e `terraform plan` (o que muda) antes de aplicar;
- **Policy-as-Code**: **OPA (Open Policy Agent)** e **Sentinel** codificam regras ("nenhum bucket público") que barram IaC fora de conformidade — humano ou gerado por IA;
- **Workflow PR-first**: a IA nunca aplica direto; abre **Pull Request**, com revisão assistida por IA + aprovação humana;
- **Drift detection**: IA compara estado real vs. declarado e sugere correções.

## Unidade III — Agentes para Kubernetes

- Geração/ajuste de **manifests YAML** por linguagem natural;
- **HPA/VPA inteligentes**: HPA escala nº de pods; VPA ajusta CPU/memória por pod — IA sugere config baseada no workload real;
- Gerência de **Ingress, Services e Network Policies**;
- **Rollout/rollback guiados**: *canary* (liberar para % pequeno e observar), *blue-green* (dois ambientes, troca instantânea), *progressive delivery*;
- **GitOps**: Argo CD e Flux — o Git é a fonte de verdade; a IA propõe mudanças via commit/PR, o operador GitOps aplica.

## Unidade IV — Troubleshooting Assistido

- **ReAct para troubleshooting**: raciocina ("pod em CrashLoopBackOff → ver logs") → age (`kubectl logs`) → observa → próxima hipótese — diagnóstico estruturado, não chute.
- **Erros comuns de K8s**: `CrashLoopBackOff` (app quebra ao iniciar), `ImagePullBackOff` (imagem inacessível/tag errada), `OOMKilled` (estourou memória);
- **Logs e traces**: análise de logs distribuídos e stack traces; correlação de **traces (Jaeger, Zipkin)** com métricas para *root cause analysis*.

## Unidade V — AIOps e Observabilidade

- **NL → queries**: gerar **PromQL** (métricas Prometheus) e **LogQL** (logs Loki) a partir de linguagem natural;
- Dashboards **Grafana** contextualizados automaticamente;
- **Detecção de anomalias com ML**: Prophet (séries temporais/sazonalidade) e Isolation Forest (outliers) sem thresholds fixos;
- **Correlação** métricas + logs + traces (os três pilares da observabilidade);
- **Alertas preditivos** e **forecasting de recursos** (prever quando o disco enche).

## Unidade VI — ChatOps com Aprovação Humana

- Bots para **Slack/Discord/Teams**: `/deploy`, `/scale`, `/rollback`, `/investigar`;
- **Comandos seguros**: o bot mostra o **diff** e a análise de impacto antes de executar;
- **Workflows de aprovação** com guardrails e circuit breakers;
- **RBAC**: cada usuário só executa o que seu papel permite;
- **Auditoria** completa: quem pediu, quem aprovou, o que rodou, resultado.

## Unidade VII — Segurança e Compliance com IA

- **Scans** de IaC e containers: Snyk, Trivy, Checkov;
- **"AI fix"**: correção automática sugerida para vulnerabilidades;
- **Secrets**: detecção de vazamento (chaves commitadas, tokens em logs);
- **Explicação de políticas OPA** em linguagem natural;
- **Remediação guiada de CVEs**; relatórios de compliance (SOC2, ISO 27001, PCI-DSS).

## Unidade VIII — CI/CD Copilot

- Geração de **pipelines** (GitLab CI, GitHub Actions, Jenkins);
- **Gates inteligentes**: testes, **SAST** (estática do código), **DAST** (app rodando), dependency scanning;
- **Análise de impacto** de PRs; recomendação de estratégia de deploy (canary vs. blue-green) conforme risco;
- **Otimização de build** (cache, paralelização); **rollback automático** por métricas pós-deploy.

## Unidade IX — FinOps com IA

- **Infracost**: estimativa de custo no PR ("+$340/mês");
- **Rightsizing**: ajustar recursos ao uso histórico real;
- Recomendação de **spot** (baratas, interrompíveis) e **reservadas** (desconto por compromisso);
- Detecção de **zombie resources** (volumes órfãos, IPs parados, ambientes esquecidos);
- **Forecasting de custos** com ML.

## Unidade X — RAG de Runbooks e Post-mortem

- **RAG sobre doc interna**: indexar playbooks/READMEs/wikis; no incidente, o agente recupera o runbook certo;
- **Agente que executa runbooks** (com guardrails);
- **Timelines de incidente** geradas de logs/alertas/ações;
- **Post-mortem blameless**: foco no processo, não em culpar pessoas.

## Unidade XI — Auto-Remediação Segura

Fluxo padrão: **alerta → verificação → mitigação → validação**.
Playbooks acionados por alertas; **canary automático com rollback inteligente**;
**circuit breakers e rate limiting** nas ações; **dry-run** antes de produção;
**HITL para ações críticas** — auto-remediação total só para ações reversíveis e de baixo risco.

## Unidade XII — Projeto Integrador

Copilot completo integrando **IaC + Kubernetes + Observabilidade**, com guardrails,
políticas de segurança, testes de validação, simulação de cenários e **análise de ROI**.

---

## ❓ Perguntas para fixar — Módulo 6

**1. Por que validação (plan, dry-run, linter) é obrigatória em IaC gerado por IA?**
Porque LLMs alucinam flags e recursos inexistentes com aparência plausível. Aplicar IaC não validado pode destruir infra real; o plan mostra exatamente o que mudaria antes de aplicar.

**2. O que é o workflow PR-first?**
A IA nunca aplica direto: abre um PR, que passa por revisão (IA + humana) e pelos gates do pipeline antes do merge/apply.

**3. Diferença entre HPA e VPA?**
HPA escala horizontalmente (mais/menos pods); VPA escala verticalmente (mais/menos CPU e memória por pod).

**4. Explique CrashLoopBackOff, ImagePullBackOff e OOMKilled.**
CrashLoopBackOff: o container inicia e quebra repetidamente. ImagePullBackOff: não baixou a imagem (tag errada, registry sem acesso). OOMKilled: excedeu o limite de memória e foi morto pelo kernel.

**5. Canary vs. blue-green?**
Canary: libera a versão nova para uma fração do tráfego e observa antes de expandir. Blue-green: dois ambientes completos e troca o tráfego de uma vez (rollback instantâneo).

**6. Papel do GitOps (Argo CD/Flux) num fluxo com IA?**
O Git é a única fonte de verdade: a IA propõe por commit/PR e o operador GitOps sincroniza o cluster com o repositório — nada é aplicado "por fora".

**7. Os três pilares da observabilidade?**
Métricas, logs e traces — e o valor da IA está em correlacioná-los para achar a causa raiz.

**8. O que são zombie resources?**
Recursos provisionados e esquecidos que geram custo sem uso: volumes órfãos, IPs parados, ambientes abandonados.

**9. SAST vs. DAST?**
SAST analisa o código-fonte estaticamente (sem executar); DAST testa a aplicação em execução, atacando-a de fora.

**10. O que é um post-mortem blameless?**
Análise de incidente focada em causas sistêmicas e melhoria de processo, sem culpar indivíduos — incentiva transparência e aprendizado.

**11. Por que existe validação após a mitigação na auto-remediação?**
Para confirmar que a ação resolveu (e não mascarou ou piorou); se a validação falha, aciona rollback ou escalonamento para humano.

**Próximo:** [Módulo 7 — IA para Gestão de Projetos →](./07-ia-para-gestao-de-projetos.md)
