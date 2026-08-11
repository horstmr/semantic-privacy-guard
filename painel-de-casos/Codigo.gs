/**
 * Painel de Casos — lado servidor (Google Apps Script)
 *
 * ---------------------------------------------------------------------
 * SUPERFÍCIE EXPOSTA — leia antes de acrescentar qualquer função
 * ---------------------------------------------------------------------
 * Toda função global deste projeto é um endpoint público para quem
 * consegue abrir o app da web. Basta digitar google.script.run.<nome>()
 * no console do navegador — não é preciso passar pelos controles do
 * painel. Hoje existem exatamente duas:
 *
 *   doGet()      entrega o HTML
 *   getCasos()   devolve as linhas da aba fixada em ABA
 *
 * Nenhuma das duas recebe identificador de arquivo do cliente. É isso, e
 * só isso, que impede o painel de virar um leitor genérico das planilhas
 * do proprietário — porque a implantação roda com a autorização dele.
 *
 *   REGRA: nunca exponha função que aceite ID, caminho ou nome de
 *   arquivo vindo do cliente.
 *
 * Auxiliares ficam em Interno.* — método de objeto não é função global,
 * então o google.script.run não alcança.
 *
 * ---------------------------------------------------------------------
 * Publicação
 * ---------------------------------------------------------------------
 *   Implantar > Nova implantação > Tipo: App da Web
 *     Executar como .............. Eu (o proprietário do script)
 *     Quem pode acessar .......... Qualquer pessoa em policiacientifica.sc.gov.br
 *
 * Para NÃO trocar a URL a cada alteração, use sempre
 *   Gerenciar implantações > lápis (editar) > Versão: Nova versão > Implantar.
 * Criar uma implantação nova gera um ID novo e quebra todos os links,
 * favoritos e o redirect do subdomínio.
 */

/** ID da planilha de origem (o trecho entre /d/ e /edit na URL). */
var PLANILHA_ID = 'COLE_AQUI_O_ID_DA_PLANILHA';

/** Nome da aba que contém os casos. */
var ABA = 'Casos';


/** Auxiliares fora do alcance do google.script.run. */
var Interno = {

  /**
   * Abre a planilha de casos.
   *
   * Como está: projeto AUTÔNOMO. O escopo concedido é
   * .../auth/spreadsheets, que alcança todas as planilhas do
   * proprietário. A planilha certa é garantida pelo código, não pela
   * permissão.
   *
   * Alternativa mais restrita: crie o projeto VINCULADO à planilha
   * (na planilha, Extensões > Apps Script), troque o corpo deste método
   * por  return SpreadsheetApp.getActive();  e use os escopos de
   * appsscript.vinculado.json. O escopo cai para
   * .../auth/spreadsheets.currentonly, que não alcança nenhum outro
   * arquivo — nem se alguém inserir código malicioso no projeto.
   */
  abrirPlanilha: function () {
    if (!PLANILHA_ID || PLANILHA_ID.indexOf('COLE_AQUI') === 0) {
      throw new Error('Defina PLANILHA_ID no Codigo.gs antes de publicar.');
    }
    return SpreadsheetApp.openById(PLANILHA_ID);
  },

  /**
   * "Situação" -> "situacao", "Nº do Protocolo" -> "ndoprotocolo".
   * As chaves resultantes precisam bater com o array COLUNAS no index.html.
   */
  normalizarChave: function (texto) {
    return String(texto)
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .toLowerCase()
      .replace(/[^a-z0-9]/g, '');
  }
};


function doGet() {
  return HtmlService.createHtmlOutputFromFile('index')
    .setTitle('Painel de Casos')
    // O HtmlService remove <meta> do arquivo HTML. Sem esta linha o painel
    // abre "desktop encolhido" no celular, com texto minúsculo.
    .addMetaTag('viewport', 'width=device-width, initial-scale=1, viewport-fit=cover')
    // DEFAULT impede que sites de terceiros embutam o painel em iframe —
    // fecha clickjacking contra um servidor já autenticado. Só volte a
    // ALLOWALL se o painel precisar ser incorporado no Google Sites.
    .setXFrameOptionsMode(HtmlService.XFrameOptionsMode.DEFAULT);
}


/**
 * Lê a aba de casos e devolve uma lista de objetos para o cliente.
 * Chamado pelo navegador via google.script.run.getCasos().
 *
 * Sem parâmetros, de propósito: a origem dos dados é decidida no
 * servidor. Não acrescente argumentos que escolham arquivo ou aba.
 */
function getCasos() {
  var aba = Interno.abrirPlanilha().getSheetByName(ABA);
  if (!aba) {
    throw new Error('Aba "' + ABA + '" não encontrada na planilha.');
  }

  // getDisplayValues devolve o texto como aparece na planilha. Evita que
  // datas virem objetos Date e sofram deslocamento de fuso ao atravessar
  // a ponte google.script.run.
  var valores = aba.getDataRange().getDisplayValues();
  if (valores.length < 2) return [];

  var cabecalho = valores.shift().map(Interno.normalizarChave);

  return valores
    .filter(function (linha) {
      return linha.some(function (celula) { return String(celula).trim() !== ''; });
    })
    .map(function (linha) {
      var caso = {};
      cabecalho.forEach(function (chave, i) {
        if (chave) caso[chave] = linha[i];
      });
      return caso;
    });
}
