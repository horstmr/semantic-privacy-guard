# Painel de Casos — página inteira, responsiva, endereço próprio

Substitui a incorporação no Google Sites. O painel passa a ser servido como
página de topo pelo Apps Script, o que remove de uma vez as limitações do
bloco de incorporação: altura fixa em px, rolagem dupla, `100vh` inoperante,
`<meta viewport>` ignorado e conta errada no `authuser`.

## Arquivos

| Arquivo | Onde vai |
|---|---|
| `Codigo.gs` | Projeto do Apps Script, arquivo de script |
| `index.html` | Projeto do Apps Script, arquivo HTML chamado `index` |
| `PEDIDO-TI.md` | Texto para solicitar o subdomínio ao TI |

## Publicação

1. **Planilha.** A aba de casos precisa ter uma linha de cabeçalho. O padrão
   esperado é `Protocolo | Entrada | Natureza | Unidade | Perito | Prazo | Situação`.
   Para outro conjunto de colunas, edite o array `COLUNAS` no topo do
   `<script>` em `index.html` — ele governa cabeçalho da tabela, rótulos no
   celular e campos pesquisáveis de uma vez só.

2. **Script.** Em `Codigo.gs`, preencha `PLANILHA_ID` (o trecho entre `/d/` e
   `/edit` na URL da planilha) e `ABA`.

3. **Situações.** O objeto `SITUACOES` em `index.html` mapeia o texto da
   planilha para o rótulo exibido e o tom semântico (`ok`, `warn`, `crit`,
   `neutro`). Valor não mapeado aparece como está, em tom neutro — nada quebra.

4. **Implantar.** `Implantar > Nova implantação > App da Web`:
   - Executar como: **Eu**
   - Quem pode acessar: **Qualquer pessoa em policiacientifica.sc.gov.br**

   A URL `/exec` resultante é o destino do redirect do subdomínio.

## Atualizações futuras

Use sempre `Gerenciar implantações > lápis > Versão: Nova versão`.
Criar uma implantação nova gera outro ID, e isso quebra o redirect do
subdomínio, os favoritos e os ícones instalados nos celulares.

## Detalhes que economizam tempo

- **`addMetaTag('viewport', ...)` é obrigatório.** O HtmlService remove as tags
  `<meta>` e `<title>` do arquivo HTML. Sem essa chamada no `doGet`, o painel
  abre no celular como desktop encolhido, com texto ilegível — e o CSS
  responsivo parece "não estar funcionando" quando na verdade nunca foi
  acionado.

- **`100dvh`, não `100vh`.** No celular, `100vh` conta a barra de endereço que
  se recolhe, e o rodapé do painel fica cortado. O arquivo declara `100vh`
  antes de `100dvh` como fallback para navegadores antigos.

- **A rolagem é da lista.** `body { overflow: hidden }` e a rolagem em
  `.rolagem` garantem uma barra só. É o que elimina a sensação de "duas
  páginas se movendo" do embed.

- **A tabela vira registro abaixo de 700px.** Cada célula recebe
  `data-rotulo`, e o CSS o exibe via `::before`. Nenhuma tabela de sete
  colunas é legível em 360px — ela precisa mudar de forma, não encolher.

- **Conta institucional.** Como app da web restrito ao domínio, o acesso exige
  que a conta do `policiacientifica.sc.gov.br` seja a conta padrão do perfil do
  navegador. Perfil do Chrome dedicado é o arranjo estável para a equipe.

- **Sem recursos externos.** Nenhuma fonte, biblioteca ou imagem de CDN.
  Carrega rápido, funciona em rede restrita e não depende de terceiros.

## Pré-visualização sem publicar

Abrir `index.html` direto no navegador funciona: sem o `google.script.run`
disponível, o painel carrega o conjunto de exemplo e exibe o selo
**Dados de demonstração**. Serve para conferir o layout em celular antes de
mexer no Apps Script.
