/** Configura��es de vari�veis */
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

/** Fun��o para permitir utiliza��o do evento onload por diversos scripts */
PlcGeral.prototype.eventOnLoad = function(){}
/** Vari�vel que contem o contexto da aplica��o */
PlcGeral.prototype.contextPath = "";
/** Mensagem de erro para obrigat�rios */
PlcGeral.prototype.obrigatorioMsg = "";
/** Express�o regular para valores alfab�ticos (sem n�meros) */
PlcGeral.prototype.alfabeticoPattern = /^[^0-9]+$/;
/** Express�o regular para valores num�ricos */
PlcGeral.prototype.numericoPattern =  /\d/;
/** Express�o regular para valores monetarios */
PlcGeral.prototype.currencyPattern =  /[][,]{1}\d{2}/;
/** Express�o regular para data */
PlcGeral.prototype.dataPattern =  /\d{2}\/\d{2}\/\d{4}/;
/** Express�o regular para data/hora */
PlcGeral.prototype.datahoraPattern =  /\d{2}\/\d{2}\/\d{4} \d{2}:\d{2}/;
/** Indica se menu de sistema est? ativo para navega��o via setas */
PlcGeral.prototype.MENU_ATIVO = false;


