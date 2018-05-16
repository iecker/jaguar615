/** Configurações de variáveis */
var NNav 				= ((navigator.appName == "Netscape"));
var AgntUsr				= navigator.userAgent.toLowerCase();
var AppVer				= navigator.appVersion.toLowerCase();
var DomYes				= document.getElementById ? 1:0;
var NavYes				= AgntUsr.indexOf('mozilla') != -1 && AgntUsr.indexOf('compatible') == -1 ? 1:0;
var ExpYes				= AgntUsr.indexOf('msie') != -1 ? 1:0;
var Opr					= AgntUsr.indexOf('opera')!= -1 ? 1:0;

var idVinculadoAlterado = "";
var plcGeral 			= new PlcGeral(); 

/** Construtor para objeto PlcGeral */
function PlcGeral(){
}

plcGeral.exibeAlertaAlteracao = true;

/** Configura valores dinamicamente no objeto plcGeral */
if (typeof setValuesInPlcGeral == 'function'){
	setValuesInPlcGeral();
}

/** Função para permitir utilização do evento onload por diversos scripts */
PlcGeral.prototype.eventOnLoad = function(){}
/** Variável que contem o contexto da aplicação */
PlcGeral.prototype.contextPath = "";
/** Mensagem de erro para obrigatórios */
PlcGeral.prototype.obrigatorioMsg = "";
/** Expressão regular para valores alfabéticos (sem números) */
PlcGeral.prototype.alfabeticoPattern = /^[^0-9]+$/;
/** Expressão regular para valores numéricos */
PlcGeral.prototype.numericoPattern =  /\d/;
/** Expressão regular para valores monetarios */
PlcGeral.prototype.currencyPattern =  /[][,]{1}\d{2}/;
/** Expressão regular para data */
PlcGeral.prototype.dataPattern =  /\d{2}\/\d{2}\/\d{4}/;
/** Expressão regular para data/hora */
PlcGeral.prototype.datahoraPattern =  /\d{2}\/\d{2}\/\d{4} \d{2}:\d{2}/;
/** Indica se menu de sistema est? ativo para navegação via setas */
PlcGeral.prototype.MENU_ATIVO = false;


