/** ---------------------------------------------------------------------- *\
  Function    : setVarGlobal
  Description : set a variable with a global scope
  Usage       : setVarGlobal(varName, value);
  Arguments   : varName - name of the global variable to set
                value - value of the global variable to set
\* ---------------------------------------------------------------------- */
function setVarGlobal(nome, valor) {
   if (this.cache == null) {this.cache = new Object();}
   this.cache[nome] = valor;
}

/** ---------------------------------------------------------------------- *\
  Function    : getGlobalVar
  Description : get a variable in a global scope
  Usage       : value = getGlobalVar(varName);
  Arguments   : varName - name of the global variable to get
                value - value of the global variable to get
\* ---------------------------------------------------------------------- */
function getVarGlobal(nome, valor) {
   if (this.cache == null) {
     return null;
   } else {
     return this.cache[nome];
   }
}

function focaElementoInformado () {
	if (plc.componenteFoco){
		plc.componenteFoco.focus();
	}
}

/*Submeter (GET) a URL informada, na mesma instancia.*/
function submeteUrl(url) {
	document.location.href=url;
}

/* Concatenar dois valores com o separados informado */
function concatenar(oldValue, newValue, separator,doisLados) {
	var valRetorno = oldValue;
	if(newValue != "") {
		if(oldValue == "") {
			if (doisLados)
				valRetorno = separator+newValue+separator;
			 else
				valRetorno = newValue;
		} else if (doisLados) {
			if (oldValue.indexOf(separator+newValue+separator) == -1)
				valRetorno = oldValue + newValue+separator;
		} else if (oldValue.indexOf(newValue) == -1)
			valRetorno = oldValue + separator + newValue;
	}
	return valRetorno;
}

/*Adiciona uma classe ao elemento*/
//TODO Implementar em jQuery
function setClasse(E, tag, classe) {
	var Etag = E.tagName;
	while (Etag != tag) {
		if(ExpYes)
			E = E.parentElement;
		else
			E = E.parentNode;
		Etag = E.tagName;
	}
	E.className = classe;
	E.marcado 	= true;
}

/*Verifica se um keycode esta inserido em um array de keycodes*/
function validaKeyArray(keycode, keyArray){
	var achou = false;
	if(keyArray != null){
		for(var i = 0; i < keyArray.length; i++){
			if(keyArray[i] == keycode){
				achou = true;
				break;
			}
		}
		return achou;
	} else {
		return true;
	}
}

/*Substitui uma String por outra*/
function replaceString(exp, str, repl){
	return new String(str).replace(exp,repl);
}

/*Retorna um elemento dado seu ID*/
//TODO Implementar em jQuery
function getElementoPorId(elementoID){
	var crossElemento = null;
	if(document.getElementById && document.getElementById(elementoID))
		crossElemento = document.getElementById(elementoID)
	else if (elementoID){
		if(document.all && eval("document.all['"+elementoID+"']"))
			crossElemento = eval("document.all['"+elementoID+"']");
		else if (eval("document['"+elementoID+"']"))
			return eval("document['"+elementoID+"']");
	}
	return crossElemento;
}

/*Retorno estilo de um elemento dado seu ID*/
//TODO Implementar em jQuery
function getElementoStyle(elementoID){

	var crossElemento = getElementoPorId(elementoID);
	var crossElementoStyle = "";
	if(crossElemento){
		if (document.all||document.getElementById)
			crossElementoStyle =  eval(crossElemento.style);
		else if (document.layers)
			crossElementoStyle =  crossElemento;
	}
	return crossElementoStyle;
}

/*Altera classes dos objetos*/
//TODO Implementar em jQuery
 function alteraClasse () {
	if(arguments && arguments.length > 0) {
		this.ID 		= "";
		this.CAMPO		= "";
		this.TIPO		= "";
		this.CLASSE		= "";
		this.OBJETO		= "";
		this.INICIAL	= false;
		this.NOVACLASSE	= false;

		for(i = 0; i < arguments.length; i++) {
			if(arguments[i] == "ID")
				this.ID = arguments[++i];
			else if (arguments[i] == "CAMPO")
				this.CAMPO = arguments[++i];
			else if (arguments[i] == "TIPO")
				this.TIPO = arguments[++i];
			else if (arguments[i] == "CLASSE")
				this.CLASSE = arguments[++i];
			else if (arguments[i] == "OBJETO")
				this.OBJETO = arguments[++i];
			else if (arguments[i] == "NOVACLASSE")
				this.NOVACLASSE = true;
			else if (arguments[i] == "INICIAL")
				this.INICIAL = true;
		}
	}

	var elements = "";
	if(this.ID != ""){
		elements = getElementoPorId(this.ID);
	} else if (this.OBJETO != ""){
		elements = this.OBJETO;
		elements = new Array(elements);
	} else if (this.CAMPO){
		elements = getForm().elements[this.CAMPO];
		elements = new Array(elements);
	}
  	if(elements) {
		for (var e = 0; e < elements.length; e++) {
			if (elements[e]) {
				if(this.NOVACLASSE){
					if(elements[e].type=="radio" && NavYes){
						elements[e].parentNode.className = this.CLASSE;
					}
					else{
						elements[e].className = this.CLASSE;
					}
				}
				else if (this.INICIAL){
					var exp = this.CLASSE;
					if(elements[e].type=="radio" && NavYes){
						elements[e].parentNode.className = this.CLASSE;
					}
					else{
						elements[e].className = elements[e].className.replace(exp,"");
					}
				}
				else{
					if(elements[e].type=="radio" && NavYes){
						elements[e].parentNode.className = this.CLASSE;
					}
					else{
						elements[e].className = elements[e].className +" "+ this.CLASSE;
					}
				}
			}
		}
	}
}

/*Recuperar valor de um parametros da url*/
function getParametroUrl ( parametro, queryString) {
	queryString = (typeof queryString == "undefined" && queryString != "") ? getQueryString() : queryString;
	if(queryString.indexOf(parametro+"=") >= 0){
		queryString = queryString.lastIndexOf("#") >= 0 ? queryString.substring(0,queryString.lastIndexOf("#")) : queryString;
 		var arrayParametros	= queryString.split("&");
		for(i = 0; i < arrayParametros.length; i++){
			if(arrayParametros[i].indexOf(parametro+"=") >= 0){
				return (arrayParametros[i].substring(arrayParametros[i].indexOf("=")+1,arrayParametros[i].length));
			}
		}
	}
}

/*Recuperar todos os parametros da url*/
function getQueryString(){
	return document.location.search;
}

/*Avaliar o atributo de um elemento para uma condicao preestabelecida*/
function plcEval(elemento, atributo, condicao){
	if(atributo == "name"){
		return eval("\"" + elemento.name + "\"" + condicao);
	}
	return false;
}


/*******************************************************************/
/***                                                             ***/
/***   Tokenizer.js - JavaScript String Tokenizer Function       ***/
/***                                                             ***/
/***   Version   : 0.2                                           ***/
/***   Date      : 01.05.2005                                    ***/
/***   Copyright : 2005 Adrian Zentner                           ***/
/***   Website   : http://www.adrian.zentner.name/               ***/
/***                                                             ***/
/***   This library is free software. It can be freely used as   ***/
/***   long as this this copyright notice is not removed.        ***/
/***                                                             ***/
/*******************************************************************/
String.prototype.tokenize = function tokenize(){
     var input             = "";
     var separator         = " ";
     var trim              = "";
     var ignoreEmptyTokens = true;

     try {
       String(this.toLowerCase());
     }
     catch(e) {
       window.alert("Tokenizer Usage: string myTokens[] = myString.tokenize(string separator, string trim, boolean ignoreEmptyTokens);");
       return;
     }

     if(typeof(this) != "undefined"){
    	 input = String(this);
     }
     if(typeof(tokenize.arguments[0]) != "undefined"){
    	 separator = String(tokenize.arguments[0]);
     }
     if(typeof(tokenize.arguments[1]) != "undefined"){
    	 trim = String(tokenize.arguments[1]);
     }
     if(typeof(tokenize.arguments[2]) != "undefined"){
    	 if(!tokenize.arguments[2]){
    		 ignoreEmptyTokens = false;
    	 }
     }
     var array = input.split(separator);
     if(trim){
    	 for(var i=0; i<array.length; i++){
    		 while(array[i].slice(0, trim.length) == trim){
    			 array[i] = array[i].slice(trim.length);
    		 }
    		 while(array[i].slice(array[i].length-trim.length) == trim){
    			 array[i] = array[i].slice(0, array[i].length-trim.length);
    		 }
    	 }
     }
     var token = new Array();
     if(ignoreEmptyTokens){
    	 for(var i=0; i<array.length; i++){
    		 if(array[i] != ""){
    			 token.push(array[i]);
    		 }
    	 }
     }else{
    	 token = array;
     }

     return token;
}

/*Retrair/expandir um subdetalhe*/
function retraiExpandeSubdetalhe (componente, subdetalhe){
	if (plc.jq(componente).hasClass('iMinimizar')){
		plc.jq(componente).removeClass('iMinimizar').addClass('iMaximizar');
		plc.jq('#corpo\\:formulario\\:novoComponente\\:' + subdetalhe ).hide();
		plc.jq('#corpo\\:formulario\\:fieldset\\:' + subdetalhe + ' > *').not('legend').hide()
	} else {
		plc.jq(componente).removeClass('iMaximizar').addClass('iMinimizar');
		plc.jq('#corpo\\:formulario\\:novoComponente\\:' + subdetalhe ).show(); 
		plc.jq('#corpo\\:formulario\\:fieldset\\:' + subdetalhe + ' > *').not('legend').show();
	}
}

//Recupera dados da url.
function extractURLParams(url){
	var u = url || window.location.href;
	var h = {};
	var i = u.indexOf('?');
	// url?querystring
	if (i != -1) {
		u = u.substring(i + 1);
		// desconsidera &amp; no split
		u = u.split(/&(?!amp;)/);
		// Gera um hash com os parametros.
		for(i = 0; i < u.length; i++){
			var p = u[i];
			var j = p.indexOf('=');
			if (j != -1) {
				h[p.substring(0, j)] = unescape(p.substring(j + 1).replace(/&amp;/gi, '&'));
			} else if (p) {
				h[p] = '';
			}
		}
	}
	return h;
}

//Recebe os parametros de URL GET e seta os seus valores em campos na tela de acordo com o prefixo do form.
function setURLParamIntoForm(formPrefix){
	var params = document.location.search.split("&");
	for(var cont=0; cont<params.length; cont++){
	    var paramsSep = params[cont].split("=");
	    if(document.getElementById(formPrefix+paramsSep[0])){
	        document.getElementById(formPrefix+paramsSep[0]).value=paramsSep[1];
	    }
	}
}

function funcaoHome() {
	window.location.replace( plcGeral.contextPath );
}

function funcaoDesconectar() {
	plc.jq('#corpo\\:formulario\\:plc-acao-desconecta0, #plc-acao-desconecta1').get(0).click();
}

function cliqueBotao(idBotao) {
	plc.jq('#corpo\\:formulario, #'+plc-acao-novo).get(0).click();
}

function cliqueBotaoPlc(idBaseBotao) {
	if ((idBaseBotao == 'botaoAcaoNovo') || 
	     idBaseBotao == 'botaoAcaoAbrir' || idBaseBotao == 'botaoAcaoClonar')
		plc.jq('#corpo\\:formulario\\:'+idBaseBotao+'_EXIBE_ALERTA_ALTERACAO').get(0).click();
	else if (idBaseBotao == 'botaoAcaoLimparArgs')
		plc.jq('#corpo\\:formulario\\:'+idBaseBotao+'_LIMPAR_ARGS').get(0).click();
	else
		plc.jq('#corpo\\:formulario\\:'+idBaseBotao).get(0).click();
}

function executarExclusao(botaoID, alertaExcluir, alertaExcluirDetalhe){
	if(get('corpo:formulario:indExcDetPlc') == ''){
		alteraClasse('CLASSE','campoComErro')
		regMensagem(botaoID, "CONFIRMACAO", alertaExcluir);
	}else{
		var ids	= get('corpo:indEcorpo:formucorpo:formulario:lc').split('#');
		alteraClasse('CLASSE','campoComErro','ID',ids,'INICIAL');
		regMensagem(botaoID,"CONFIRMACAO",alertaExcluirDetalhe);
	}
	if(!enviarMensagem(botaoID)){
		alteraClasse('CLASSE','campoComErro','INICIAL');
		return false;
	}
	return true;
}


function executarExclusaoGravacao(botaoID, alertaExcluirDetalhe) {

	if(get('corpo:formulario:indExcDetPlc') != ''){
		var ids	= get('corpo:indEcorpo:formucorpo:formulario:lc').split('#');
		alteraClasse('CLASSE','campoComErro','ID',ids,'INICIAL');
		if(plcGeral.formPattern.toLowerCase() != 'tab'){
			regMensagem(botaoID,"CONFIRMACAO",alertaExcluirDetalhe);
		}else{
			regMensagem(botaoID,"",alertaExcluirDetalhe);
		}
		if(!enviarMensagem(botaoID)){
			alteraClasse('CLASSE','campoComErro','INICIAL');
			return false;
		}
	} 
	return true;
}

/**
* Função que seleciona o botão associado à Função da tecla
* @param acao Ação a qual o botão está associado
* @return botao Objeto Button representando o botão associado a ação
*/
function selBotao(acao, form) {
	var numElements;
	var form = getForm(form);
	var elementValue;
	var retorno = false;
	var botao = null;

	if (document.forms && document.forms.length > 0) {
		if (typeof form != "undefined" && typeof form.elements["evento"] != "undefined") {
			numElements = form.elements["evento"].length;
			if (typeof numElements != "undefined"){
				for(i=0; i < numElements; i++) {
					elementValue = form.elements["evento"][i].value;
					if(elementValue == acao && form.elements["evento"][i].id != "RECUPERACAO_AUTOMATICA")
					{
						botao = eval(form.elements["evento"][i]);
						i = numElements;
					}
				}
			} else if (typeof form.elements["evento"] != "undefined"){
				elementValue = form.elements["evento"].value;
				if(elementValue == acao && form.elements["evento"].id != "RECUPERACAO_AUTOMATICA")
					botao = eval(form.elements["evento"]);
			} 
		} else if (eval(form.elements[acao]))
			botao = eval(form.elements[acao]);
		// acerto para encontrar os botoes do trinidad
		else if (document.getElementById('corpo:formulario:'+acao)) 
			botao = document.getElementById('corpo:formulario:'+acao);
	}
	return botao;
}


/*Recuperar keycode da teclar pressionada*/
function getKeyCode(evt){
	var key;
	if (ExpYes)
		key = evt.keyCode;
	else
		key = evt.which;
	return key;
}


/*Configurar eventos da página*/
function configuraEventos(){
	setUpOnEventoTag ('INPUT', 'onchange', 'setAlertaAlteracao');
	setUpOnEventoTag ('TEXTAREA', 'onchange', 'setAlertaAlteracao');
	setUpOnEventoTag ('SELECT', 'onchange', 'setAlertaAlteracao');
	setUpOnEventoTag ('A', 'onclick', 'enviaAlertaAlteracao');
	setUpOnEventoTag ('INPUT#button', 'onclick', 'enviaAlertaAlteracao');
	setUpOnEventoTag ('button', 'onclick', 'enviaAlertaAlteracao');
	//setUpOnEventoElemento ('botao_menu', 'onclick', 'enviaAlertaAlteracao');
}

 
