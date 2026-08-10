/**
 * Painel de Casos — lado servidor (Google Apps Script)
 *
 * Publicação:
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


function doGet() {
  return HtmlService.createHtmlOutputFromFile('index')
    .setTitle('Painel de Casos')
    // O HtmlService remove <meta> do arquivo HTML. Sem esta linha o painel
    // abre "desktop encolhido" no celular, com texto minúsculo.
    .addMetaTag('viewport', 'width=device-width, initial-scale=1, viewport-fit=cover')
    // Necessário apenas se o painel também for embutido no Google Sites.
    // Servindo pelo subdomínio, pode virar XFrameOptionsMode.DEFAULT (mais restrito).
    .setXFrameOptionsMode(HtmlService.XFrameOptionsMode.ALLOWALL);
}


/**
 * Lê a aba de casos e devolve uma lista de objetos para o cliente.
 * Chamado pelo navegador via google.script.run.getCasos().
 */
function getCasos() {
  var aba = SpreadsheetApp.openById(PLANILHA_ID).getSheetByName(ABA);
  if (!aba) {
    throw new Error('Aba "' + ABA + '" não encontrada na planilha.');
  }

  // getDisplayValues devolve o texto como aparece na planilha. Evita que
  // datas virem objetos Date e sofram deslocamento de fuso ao atravessar
  // a ponte google.script.run.
  var valores = aba.getDataRange().getDisplayValues();
  if (valores.length < 2) return [];

  var cabecalho = valores.shift().map(normalizarChave);

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


/**
 * "Situação" -> "situacao", "Nº do Protocolo" -> "ndoprotocolo".
 * As chaves resultantes precisam bater com o array COLUNAS no index.html.
 */
function normalizarChave(texto) {
  return String(texto)
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .toLowerCase()
    .replace(/[^a-z0-9]/g, '');
}
