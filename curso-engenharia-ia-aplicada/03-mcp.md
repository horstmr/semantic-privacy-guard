# Módulo 3 — MCP (Model Context Protocol)

> O tema que o curso de Prompt Engineering não toca. Tool calling (Bônus B3)
> ensina o modelo a chamar **suas** funções; MCP padroniza isso num protocolo
> aberto que qualquer cliente e qualquer serviço falam.

## 3.1 Visão geral

**MCP** é um protocolo aberto que padroniza a conexão entre LLMs e o mundo externo
(APIs, bancos, arquivos, serviços). Analogia consagrada: **"o USB-C da IA"** — um
conector universal.

**O problema que resolve**: antes, cada aplicação de IA integrava cada serviço com
código customizado (problema **N×M**: N apps × M serviços = N×M integrações). Com
MCP, cada serviço expõe **um** servidor MCP e qualquer cliente compatível (Claude,
IDEs, agentes) o consome — vira **N+M**.

**Por que ganhou tração**: adoção pelos grandes players, ecossistema explosivo de
servidores prontos (GitHub, Slack, Postgres, navegador...) e a ascensão dos
agentes, que precisam de ferramentas padronizadas.

## 3.2 Conceitos e integração padronizada

Um servidor MCP expõe **três primitivas**:

- **Tools**: funções que o modelo pode executar (ex.: `criar_issue`, `consultar_banco`);
- **Resources**: dados/conteúdo que o cliente pode ler (arquivos, registros);
- **Prompts**: templates reutilizáveis oferecidos pelo servidor.

A padronização de entrada/saída (JSON-RPC) garante que o mesmo servidor funcione
com modelos e ferramentas diferentes, sem retrabalho.

## 3.3 MCP vs. tools tradicionais

| | Tools/plugins clássicos | MCP |
|---|---|---|
| Escopo | Amarrado a um provedor/app | Padrão aberto, interoperável |
| Reuso | Reescrever por plataforma | Um servidor, vários clientes |
| Descoberta | Manual | Cliente lista tools do servidor dinamicamente |

**Quando usar MCP**: quando a integração deve servir a múltiplos clientes/modelos
ou entrar num ecossistema. **Quando usar tool nativa**: integração pontual e
simples dentro de um único app (function calling direto pode bastar).

> **↩︎ Base de function calling** está no
> [Bônus B3 — Tool calling](../curso-prompt-engineering-para-devs/bonus/b3-tool-calling.md).
> MCP é a **padronização** desse mesmo mecanismo.

## 3.4 MCP em JavaScript

O SDK oficial (`@modelcontextprotocol/sdk`) permite criar servidores em
TypeScript: define-se nome, schema de entrada (com Zod, por exemplo) e handler de
cada tool. **Boas práticas**: uma tool = uma responsabilidade clara; **descrições
ricas** (o LLM escolhe a tool pela descrição!); validação de entrada; separar
lógica de negócio do transporte (stdio ou HTTP).

## 3.5 Transformando sua empresa em um "MCP"

Ideia estratégica: expor serviços internos (CRM, billing, suporte, dados de
produto) como servidores MCP cria uma **camada AI-ready** sobre a stack da
empresa. Qualquer agente ou copilot autorizado passa a operar esses sistemas de
forma padronizada — sem integração customizada por projeto.

## 3.6 Segurança e governança

- **Autenticação**: service tokens/OAuth por cliente; nunca expor um MCP sensível sem credencial;
- **Autorização**: cada tool com permissões mínimas (princípio do menor privilégio);
- **Rate limiting**: evita abuso e custo descontrolado (agentes podem entrar em loop!);
- **WAF e rede**: proteger servidores MCP expostos como qualquer API — validação de payload, filtragem, monitoramento;
- **Auditoria**: logar quem (qual agente/usuário) chamou o quê e quando.

## 3.7 Casos de uso

- Conectar IA a **sistemas legados** (o MCP vira "tradutor" moderno de um ERP antigo);
- **Copilots internos**: assistente que consulta CRM + suporte + analytics via MCPs;
- **IDEs**: copilots customizados com acesso às ferramentas da empresa;
- **Automações**: agentes que executam fluxos completos entre sistemas.

## 3.8 Hands-on: do zero à produção

(1) criar o servidor MCP com 2–3 tools; (2) testar localmente com um cliente
(Claude Desktop / MCP Inspector); (3) integrar ao front e back da aplicação;
(4) testar em diferentes LLM clients; (5) compor com outros MCPs (o agente usa
vários servidores na mesma tarefa); (6) publicar com autenticação, rate limit e logs.

---

## ❓ Perguntas para fixar — Módulo 3

**1. Que problema o MCP resolve, em uma frase?**
Elimina o caos N×M de integrações customizadas entre apps de IA e serviços, padronizando tudo num protocolo único (N+M).

**2. Quais são as três primitivas de um servidor MCP?**
Tools (funções executáveis), Resources (dados legíveis) e Prompts (templates reutilizáveis).

**3. Por que a descrição de uma tool MCP é tão importante?**
Porque é por ela que o LLM decide quando e como usar a tool. Descrição vaga = uso errado ou nunca usado. (Mesmo princípio da [definição de ferramenta no B3](../curso-prompt-engineering-para-devs/bonus/b3-tool-calling.md).)

**4. MCP substitui function calling nativo?**
Não necessariamente. Para integração pontual num único app, function calling direto basta. MCP compensa quando há múltiplos clientes, reuso e ecossistema.

**5. O que significa "transformar a empresa em um MCP"?**
Expor os serviços internos (CRM, billing, dados) como servidores MCP, criando uma camada padronizada AI-ready que qualquer agente autorizado consegue operar.

**6. Cite três controles de segurança essenciais ao expor um MCP.**
Autenticação (tokens), rate limiting (contra loops e abuso) e auditoria/logs; além de permissões mínimas por tool e proteção de rede (WAF).

**7. Por que rate limiting é especialmente crítico com agentes?**
Porque agentes autônomos podem entrar em loops chamando tools repetidamente, gerando custo e carga inesperados em minutos.

**Próximo:** [Módulo 4 — Agentes avançados →](./04-agentes-avancados.md)
