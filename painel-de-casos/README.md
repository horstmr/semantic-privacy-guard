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
| `appsscript.json` | Manifesto do projeto (projeto autônomo) |
| `appsscript.vinculado.json` | Manifesto alternativo, para projeto vinculado à planilha |
| `PEDIDO-TI.md` | Texto para solicitar o subdomínio ao TI |

Para editar o manifesto no editor do Apps Script:
`Configurações do projeto > Mostrar "appsscript.json" no editor`.

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

## Segurança

A implantação roda com a autorização do **proprietário**. Quem abre o painel
não precisa ter acesso à planilha — o script acessa por ele. Isso é o que faz o
painel funcionar, e também o que exige cuidado com o que o código expõe.

### Toda função global é um endpoint público

Qualquer pessoa que consiga abrir o app pode chamar **qualquer função de topo**
digitando `google.script.run.<nome>()` no console do navegador, sem passar
pelos controles do painel. Para conferir o que está exposto:

```sh
grep -nE '^function [A-Za-z0-9_]+' Codigo.gs
```

Hoje devolve apenas `doGet` e `getCasos`. Auxiliares ficam em `Interno.*`:
método de objeto não é função global, então o `google.script.run` não alcança.

### A regra que sustenta tudo

**Nenhuma função exposta pode aceitar identificador de arquivo vindo do
cliente.** `getCasos()` não recebe parâmetro justamente por isso — a origem dos
dados é decidida no servidor. O anti-padrão que quebraria o modelo:

```javascript
// NUNCA num app da web: vira leitor sob demanda das planilhas do proprietário
function lerPlanilha(id) { return SpreadsheetApp.openById(id)/* ... */; }
```

### Escopo

`appsscript.json` fixa `.../auth/spreadsheets` — amplo: alcança **todas as
planilhas do proprietário**. A planilha certa é garantida pelo código, não pela
permissão. Fixar o escopo no manifesto faz com que código novo que tente ir
além (por exemplo `DriveApp`) falhe e exija reautorização visível, em vez de
herdar acesso em silêncio.

**Opção mais restrita:** criar o projeto **vinculado** à planilha
(nela, `Extensões > Apps Script`), trocar o corpo de `Interno.abrirPlanilha`
por `return SpreadsheetApp.getActive();` e usar
`appsscript.vinculado.json`. O escopo cai para
`.../auth/spreadsheets.currentonly`, que não alcança nenhum outro arquivo — nem
se alguém inserir código malicioso no projeto. Deixa de depender de disciplina
no código e passa a depender da permissão.

Duas ressalvas antes de escolher essa opção:

- Vincular significa **outro projeto**, logo **outra URL `/exec`**. Decida
  antes de pedir o subdomínio ao TI, senão o redirect nasce apontando para o
  lugar errado.
- Valide `spreadsheets.currentonly` publicando e abrindo o painel de fato. É o
  escopo pensado para o contexto do documento, e vale confirmar o
  comportamento no contexto de app da web antes de confiar nele.

### Iframe

`doGet` usa `XFrameOptionsMode.DEFAULT`, que impede sites de terceiros de
embutir o painel — fecha clickjacking contra um servidor já autenticado.
`ALLOWALL` só é necessário para incorporar no Google Sites; servindo pelo
subdomínio, não use.

### Quem pode alterar o código

O projeto do Apps Script é um arquivo no Drive, com compartilhamento próprio.
Quem tiver **edição** nele (direta, ou pela pasta / Drive compartilhado que o
contém) pode publicar nova versão na implantação existente, e o código alterado
passa a rodar na URL que a equipe já usa. Revise essa lista periodicamente.

### Restringir a um subgrupo

O controle "qualquer pessoa no domínio" é grosso: hoje qualquer servidor do
domínio lê todos os casos. Para restringir mais, filtre no início de
`getCasos()` com `Session.getActiveUser().getEmail()` contra uma lista de
e-mails — o método é confiável neste arranjo, porque acessante e proprietário
estão no mesmo domínio.

### Conteúdo da planilha

O painel monta a tabela com `createElement` e `textContent`, nunca com
`innerHTML` sobre os dados. Uma célula contendo HTML aparece como texto
literal, não executa.

## Pré-visualização sem publicar

Abrir `index.html` direto no navegador funciona: sem o `google.script.run`
disponível, o painel carrega o conjunto de exemplo e exibe o selo
**Dados de demonstração**. Serve para conferir o layout em celular antes de
mexer no Apps Script.
