/**
 * plc.geral.form

 * Fun��es javascripts utilizadas pelo jCompany/Jaguar 
 * As fun��es abaixo, s�o utilizadas em eventos de formul�rios
 */


/** Fun��o que � executada ao carregar o formul�rio */
function initializesForm() {

	//adicionando classe de destaque nas linhas de tabular/detalhes/sub-detalhes que est�o marcadas
	plc.jq("form table tr:not(.plc-line) span.celulaFormularioCaixaMarcacao input:checked ").each(function() {
		plc.jq(this).parents("tr:first").addClass("campoComErro plc-linha-destaque");
	}); 	

	plc.jq("form table tr.plc-line span.celulaFormularioCaixaMarcacao input:checked ").each(function() {
		var button = plc.jq(this).parents("tr:first").find("div.plc-exclusionboxcheck-button");
		checkTarja(button, true);
	});

}

/** Fun��o mover o foco automaticamente. */
function moverFoco(){
	if (getCampoFocus() == "") {
		setTimeout("testaCampos()", 0);
	} else {
		setTimeout("setFocus(getCampoFocus(), getCampoFocusSelecionar())", 20);
	}
}

/** Fun��o para posicionar foco em campo especifico. */
var campoFocus = "";
var campoFocusSelecionar = false;
var campoVinculadoFocus = "";
function setCampoFocus(nomeCampo, selecionar){
	this.campoFocus = nomeCampo;
	setCampoFocusSelecionar(selecionar);	
}

function getCampoFocus(){
	return this.campoFocus;
}

function setCampoFocusSelecionar(selecionar){
	if(selecionar && typeof selecionar != "undefined" && selecionar != "")
		this.campoFocusSelecionar = true;
}

function getCampoFocusSelecionar(){
	this.campoFocusSelecionar
}

/**
 * Fun��o que retorna o objeto window correto da janela atual, para evitar problemas com IFrame PPR.
 * Implementado por Bruno Grossi - 29/03/2007
 */
function getRootWindow() {
	return "_pprIFrame" != window.name ? window : window.parent;
}

/**
 * Fun��o que retorna o document da janela atual, para evitar problemas com IFrame PPR.
 * Implementado por Bruno Grossi - 29/03/2007
 */
function getRootDocument() {
	return getRootWindow().document;
}

/**
 * Fun��o para retornar um objeto que representa um form da p�gina
 * @param form Nome do form, caso n�o seja o form padr�o. [String,OP]
 * @see getVarGlobal
 * @return form Objeto form [Object]
 */
function getForm(form){
	var sessForm = getVarGlobal("form");
	// Se houver um opener recupera o form deste
	var parentForm = "";
	if(opener && form != null){
		try {
			if( opener.getVarGlobal("parentForm") != "" && opener.getVarGlobal("parentForm") != 0 &&
					opener.getVarGlobal("parentForm") != null && opener.getVarGlobal("parentForm") != "undefined" )
				return opener.getVarGlobal("parentForm"); 
		} catch(ex) {
			// Erro de permiss�o caso a window opener, esteja em outro dom�nio.
		}
	}	
	if(form != "" && form != 0 && ""+form != "undefined" && form != null)
		form = eval("getRootDocument().forms['"+form+"']");
	else if (sessForm != "" && sessForm != 0 && ""+sessForm != "undefined" && ""+sessForm != "null")
		form = eval("getRootDocument().forms['"+sessForm+"']");
	else
		form = eval("getRootDocument().forms['corpo:formulario']");

	return form;
}

/**
 * Fun��o para retornar um objeto que representa um campo da p�gina
 * @param campo Nome do campo que se quer recuperar. [String, OB]
 * @param form Nome do form, caso n�o seja o form padr�o. [String,OP]
 * @see getForm
 * @return campo Objeto campo [Object]
 */
function getCampo(campo, form) {
	form = getForm(form);
	if(form) { campo = form.elements[campo]; }
	return campo;
}

/**
 * Fun��o para colocar foco no primeiro campo v�lido da p�gina.
 * Apos o onload da p�gina a Fun��o procura nos campos da p�gina um campo v�lido para focar
 * @see setFocus
 */
function testaCampos(){

	if (getCampoFocus() != '') {
		return;
	}
	var numForms = getRootDocument().forms.length;
	var achouVinculado = true;
	if (campoVinculadoFocus != "") {
		achouVinculado = false;
	}
	var primeiroCampo = "";
	var campoFocado	= null;
	if (getVarGlobal("trSelecao") != null) {
		return;
	}
	if (getRootDocument().location.hash && getRootDocument().location.hash != null && getRootDocument().location.hash.indexOf("#") > -1) {
		return;
	}

	for(var i = 0, numElements = 0; i < numForms; i++) {

		numElements = getRootDocument().forms[i].elements.length;
		//Adicionando script para setar focus no campo corrente
		for(j=0; j < numElements; j++) {
			if(getRootDocument().forms[i].elements[j].getAttribute("id") != null && getRootDocument().forms[i].elements[j].getAttribute("type") != null ) {
				typeElement = getRootDocument().forms[i].elements[j].getAttribute("type");
				if(typeElement == 'text' || typeElement == 'radio' || typeElement == 'checkbox' || typeElement == 'select' || typeElement == 'textarea' ) {
					plc.jq(getRootDocument().forms[i].elements[j]).focus(function() { window.fieldFocus = document.activeElement;});
				}
			}
		}

		for(j=0; j < numElements; j++) {

			if (!achouVinculado) {
				if (getRootDocument().forms[i].elements[j].getAttribute("id") == campoVinculadoFocus) {
					achouVinculado = true;
				}
				continue;
			}

			if (getRootDocument().forms[i].elements[j].getAttribute("inibeFoco") != 'S' && 
					!(getRootDocument().forms[i].elements[j].className.indexOf("inibeFoco") > -1)){
				if (getRootDocument().forms[i].elements[j].getAttribute("id") != null && !(getRootDocument().forms[i].elements[j].getAttribute("id").indexOf(":inibeFoco") > -1)
						&& !(getRootDocument().forms[i].elements[j].getAttribute("id").indexOf("inibeFoco_") > -1)){
					if((getRootDocument().forms[i].elements[j].type=="text" ||
							getRootDocument().forms[i].elements[j].type=="password" ||
							getRootDocument().forms[i].elements[j].type=="file" ||
							getRootDocument().forms[i].elements[j].type=="textarea") &&
							getRootDocument().forms[i].elements[j].type != "hidden" &&
							!getRootDocument().forms[i].elements[j].readOnly &&
							!getRootDocument().forms[i].elements[j].disabled){
						if(getRootDocument().forms[i].elements[j].value == ""){

							campoFocado = getRootDocument().forms[i].elements[j];
							primeiroCampo = getRootDocument().forms[i].elements[j];
							i = numForms;
							j = numElements;
						} else if (primeiroCampo == "" && getRootDocument().forms[i].elements[j].type != "hidden" &&
								!getRootDocument().forms[i].elements[j].readOnly && !getRootDocument().forms[i].elements[j].disabled)
							primeiroCampo = getRootDocument().forms[i].elements[j];
					} else if (getRootDocument().forms[i].elements[j].type == "radio"){
						//setFocus(document.forms[i].elements[j].name);
					} else if ((getRootDocument().forms[i].elements[j].type == "select-one" ||
							getRootDocument().forms[i].elements[j].type == "select-multiple") &&
							getRootDocument().forms[i].elements[j].options.length > 0 &&
							getRootDocument().forms[i].elements[j].options.selectedIndex <= 0 &&
							!getRootDocument().forms[i].elements[j].readOnly &&
							!getRootDocument().forms[i].elements[j].disabled){
						try {
							primeiroCampo = "";
							getRootDocument().forms[i].elements[j].focus();
							getRootDocument().forms[i].elements[j].options[0].selected = true;

							campoFocado = getRootDocument().forms[i].elements[j];
						}catch (e){}
						i = numForms;
						j = numElements;
					} else if (primeiroCampo == "" && typeof getRootDocument().forms[i].elements[j].type != 'undefined' &&
							getRootDocument().forms[i].elements[j].type != "button" &&
							getRootDocument().forms[i].elements[j].type != "submit" &&
							getRootDocument().forms[i].elements[j].type != "hidden" &&
							getRootDocument().forms[i].elements[j].type != "radio" &&
							getRootDocument().forms[i].elements[j].type != "checkbox" &&
							!getRootDocument().forms[i].elements[j].readOnly &&
							!getRootDocument().forms[i].elements[j].disabled)
						primeiroCampo = getRootDocument().forms[i].elements[j];
				}
			}
		}


	}
	if (primeiroCampo != "") {
		setFocus(primeiroCampo.name, true);
		campoFocado = primeiroCampo;
	}
	setVarGlobal("campoFocadoPlc", campoFocado);	
}






/** Setar o valor para o campo informado. */
function set(nomeCampo, valor, separador, form) {
	setValorCampo(nomeCampo, valor, separador, form);
}

/** Setar o valor para o campo informado. */
function setValorCampo(nomeCampo, valor, separador, form) {
	nomeCampo = padronizaNomeCampoFormulario(nomeCampo);
	var campo = getCampo(nomeCampo,form);
	if(campo) {
		if(arguments[2]) {
			valor = concatenar(retornaValorCampo(nomeCampo), valor, separador);
		}
		if (campo.type == "select-one" || campo.type == "select-multiple") {
			plc.jq(campo).val(plc.jq(campo).find("option:[text='" + valor + "']").val());
		} else { 
			campo.value = valor;
		} 
		return true;
	}
	return false;
}

/** Inserir um valor em um campo. */
function insereValorCampo(field,value,form) {
	//TODO Ver utiliza��o de setValorCampo
	var campo = "";
	if(form == "" || form == 0 || ""+form == "undefined")
		campo = eval("getForm().elements['"+field+"']");
	else
		campo = eval("getForm('"+form+"').elements['"+field+"']");
	if(campo) {
		if (campo.type == "select-one")	{	
			for(i = 0; i < campo.options.length; i++) {
				if(campo.options[i].value == value) {
					campo.options[i].selected = true;
					i = campo.options.length;
				}
			}
		} else {
			campo.value = value;
		}
	}
}

/**
 * Padroniza o nome do campo, conforme padr�o de nomes de campos do jCompany.  
 * Ex: corpo:formulario:nomeCampo
 */
function padronizaNomeCampoFormulario(nomeCampo){
	if(nomeCampo){
		var temCorpo = nomeCampo.search('corpo:')== 0;
		var temFormulario = nomeCampo.search('corpo:formulario:')== 0;
		if (temCorpo){
			if (! temFormulario)
				nomeCampo = 'corpo:formulario:' + nomeCampo.substring(6,nomeCampo.length);
		}
	}
	return nomeCampo; 
}

/** Atualizar o valor do campo informado. */
function atualizaValorCampo(nomeCampo, valorReplace, valorNovo, separador, form) {
	var campo 	= getCampo(nomeCampo,form);
	var valor 	= "";
	var exp 	= "";
	var sepAux	= "";
	if (valorNovo == "") {sepAux = "";}
	else {sepAux = separador;}
	if(campo) {
		valor = campo.value;
		if(valor.indexOf(separador) < 0) {
			try{
				exp = new RegExp(valorReplace);
				if(valor != "")
					valor = replaceString(exp, valor, valorNovo);
			}catch(e){
				alert("Erro ao criar expressao regular para:\n"+ valorReplace);
			}
		}else {
			try{
				exp = new RegExp(valorReplace+"\\"+separador);
				valor = replaceString(exp, valor, valorNovo+sepAux);
			}catch(e){
				alert("Erro ao criar expressao regular para:\n"+ valorReplace+"\\"+separador);
			}
			try{
				exp = new RegExp("\\"+separador+valorReplace);
				valor = replaceString(exp, valor, sepAux+valorNovo);
			}catch(e){
				alert("Erro ao criar expressao regular para:\n"+ "\\"+separador+valorReplace);
			}
		}
		campo.value = valor;
		return true;
	}
	return false;
}

/** Focar automaticamento o campo informado. */
var botao;
function setFocus(nomeCampo, selecionar){
	campoFocus = "";
	var campo;
	if(window.fieldFocus != null) {

		campo = getCampo(window.fieldFocus.id);
	} else {
		campo = getCampo(nomeCampo);
	}
	if(campo){

		setCampoFocus(nomeCampo);
		try{
			if((campo.type == "text" || campo.type=="password" || campo.type=="textarea" || campo.type=="file") &&
					campo.type != "hidden"  && !campo.readOnly && !campo.disabled){
				campo.focus();
				if(selecionar)
					selecionarCampo(campo.name);
			}
			else if ((campo.type == "select-one" || campo.type == "select-multiple") &&
					campo.options.length > 0 && campo.options.selectedIndex <= 0 && !campo.disabled){

				campo.focus();
				campo.options[0].selected = true;
			}
		}catch(e){}
	}	
}

/** Selecionar o conte�do do campo informado. */
function selecionarCampo(nomeCampo){

	var campo 	= getCampo(nomeCampo);

	if(campo) {
		try{
			if((campo.type == "text" || campo.type=="password" || campo.type=="textarea" || campo.type=="file") &&
					campo.type != "hidden"  && !campo.readOnly && !campo.disabled){
				campo.select();
			}
			else if ((campo.type == "select-one" || campo.type == "select-multiple") &&
					campo.options.length > 0 && campo.options.selectedIndex <= 0 && !campo.disabled) {
				campo.focus();
				campo.selected = true;
			}
		}catch(e){}
	}
}

/** Retornar o valor do campo informado. */
function get(field, form)  {
	return retornaValorCampo(field, form);
}

/** Retornar o valor do campo informado. */
function retornaValorCampo(field, form) {

	field = padronizaNomeCampoFormulario(field);

	var campo = "";
	if(form == "" || form == 0 || ""+form == "undefined"){
		if(getRootDocument().forms && getForm() && getForm().elements)
			campo = eval("getForm().elements['"+field+"']");
	}
	else
		campo = eval("getForm('"+form+"').elements['"+field+"']");

	if(campo) {
//		HACK para retira a mascara do valor.
		if (plc.jq) {
			var jCompanySubmitUnmask = plc.jq(campo).data('jcompany-submit-unmask');
			if (typeof jCompanySubmitUnmask == 'function') {
				return jCompanySubmitUnmask.call() || "";
			}
		}
//		Acerto para resolver problemas de campos duplicados inclu�dos pela
//		gera��o via plugin
//		Alterado: 16/12/2005
		if(campo.length > 0 && campo[0]){
			if(	campo[0].type == "text" || campo[0].type == "hidden" || campo[0].type == "textarea"  ||
					campo[0].type == "file" || campo[0].type == "password")
				campo = campo[0];
		}
		if(	campo.type == "text" || campo.type == "hidden" || campo.type == "textarea" ||
				campo.type == "file" || campo.type == "password") {
			return campo.value;
		} else if (campo.type == "checkbox") {
			if(campo.checked)
				return campo.value;
			else{
				if(getVarGlobal("uncheck_"+campo.name))
					return getVarGlobal("uncheck_"+campo.name);
				else
					return "N";
			}
		} else if (campo.type == "select-one") {
			return campo.options[campo.selectedIndex].value;
		} else if (campo.type == "select-multiple") {
			var valSelect = new Object();
			for(i = 0; i < campo.length; i++){
				if(campo.options[i].selected){
					valSelect[valSelect.length] = campo.options[i].value;
				}
			}
			return valSelect;
		} else if (campo.type == "radio") {
			if(campo.checked)
				return campo.value;
		} else {
			for(var i = 0; i < campo.length; i++){
				if(campo[i].checked){
					return campo[i].value;
				}
			}
		}
	}

	return "";
}

/** Fun��o para diferenciar linha para exclus�o em l�gicas tabulares*/
function marcarExclusaoDetalhe(chave, checkbox, evt){

	if(!checkbox.checked){
		var campo = getCampo("corpo:formulario:indExcDetPlc");
		if(campo){
			campo.value = campo.value.replace('#'+chave+"#","#");
			if (campo.value=='#') {
				campo.value='';
			}
		}
	}
	else
		set("corpo:formulario:indExcDetPlc", concatenar(get("corpo:formulario:indExcDetPlc"), chave, "#",true));
}

/** Executar a��es ao marcar exclus�o da linha. */
function marcarExclusao(checkbox, evt) {
	marcarExclusaoDetalhe(checkbox.name, checkbox, evt)
	checarUm(checkbox);
}

/** Executar a��es ao marcar checkbox da linha de exclus�o*/
function checarUm(CB, CT, nomeChk, CBID, frm) {
	frm  		= getForm(frm);
	CT	  		= getCheckTodos(CT, frm);
	CBID 		= getCheckExc(CBID, frm);
	nomeChk 	= getNomeChk(nomeChk);
	testarChekbox(CB);
	var TB=TO=0;
	for (var i=0;i < CBID.length;i++) {
		var e = CBID[i];
		if ((e.name && e.name.indexOf(nomeChk) >= 0) && (e.type=='checkbox')) {
			TO++;
			if (e.checked)	TB++;
		}
	}
	CT.checked=(TO==TB)?true:false;
}

/** Testar marca��o checkbox da linha de exclus�o e diferenciar linha. */
function testarChekbox(CHK) {
	var tag = "TR";
	if (CHK.checked) setClasse(CHK, tag, "campoComErro plc-linha-destaque");
	else setClasse(CHK, tag, "");
}

/** Recuperar campo checkbox que marca todos os checkboxes de exclus�o. */
function getCheckTodos(CT, frm) {
	if(""+CT != "undefined" && CT != "") 	return CT;
	else if (frm.cbTodos)
		return frm.cbTodos;
	else
		return frm;
}

/** Recuperar campo checkbox de exclus�o clicado*/
function getCheckExc(CBID, frm) {
	if(""+CBID != "undefined" && CBID != "") {
		if(frm.CBID)
			return frm.CBID;
	}
	return frm.elements;
}

/** Recuperar nome do campo checkbox de exclus�o clicado*/
function getNomeChk(nomeChk) {
	if(""+nomeChk != "undefined" && nomeChk != "")
		return nomeChk;
	return "indExcPlc";
}

/** Fun��es para marca��o de tabular/detalhe com tarja */ 
function applyCheckTarja(id,titleExc,titleUndo,msgConfirm) {

	plc.jq.merge(plc.jq("#corpo\\:formulario\\:" + id  + " span.celulaFormularioCaixaMarcacao"), plc.jq("#" + id + " span.celulaFormularioCaixaMarcacao")).each(function() {

		if (plc.jq(this).parent().find("div.plc-exclusionboxcheck-button").size() == 0) {

			if (!titleExc) 
				titleExc = "Excluir";
			if (!titleUndo) 
				titleUndo = "Desfazer";
			if (!msgConfirm) 
				msgConfirm = "Clique em 'Gravar' para confirmar a exclus&atilde;o do item.";

			//pegando checkbox
			var input = plc.jq("input", this); 
			//escondendo checkbox
			plc.jq(input).css("display","none");

			//criando botao remove
			var button = plc.jq('<div title="' + titleExc + '" alt="' + titleExc + '" class="plc-exclusionbox-button plc-exclusionboxcheck-button" onclick="checkTarja(this);" />');
			plc.jq(button).insertBefore(input);

			var line = plc.jq(this).parents("tr");
			plc.jq(line).addClass("plc-line");

			//criando linha
			var numColumns = plc.jq(input).parents("tr").children().size() - 2;
			var firstColumn = plc.jq(input).parents("tr").children()[0];
			var lineUn = plc.jq('<tr class="plc-line plc-line-exclusion"><td>' + plc.jq(firstColumn).html() + '</td><td><div title="' + titleUndo + '" alt="' + titleUndo + '" class="plc-exclusionbox-button plc-exclusionboxuncheck-button" onclick="uncheckTarja(this);" /></td><td colSpan="' + numColumns + '"><div class="plc-exclusionbox-title">' + msgConfirm + '</div></td></tr>');
			plc.jq(lineUn).insertAfter(line);
		}	

	});		
}		

function checkTarja(item, hide) {
	linha = plc.jq(item).parents('.plc-line')[0];
	if (hide) {
		plc.jq(linha).hide();
	} else {
		plc.jq(linha).slideUp(50);
	}	
	plc.jq(linha).next('.plc-line-exclusion').slideDown(100);
	plc.jq(".celulaFormularioCaixaMarcacao  input", linha).attr("checked",  true);
}

function uncheckTarja(item) {
	linha = plc.jq(item).parents('.plc-line-exclusion')[0];
	plc.jq(linha).slideUp(50);
	var  linhaAnt = plc.jq(linha).prev('.plc-line');
	plc.jq(linhaAnt).slideDown(100);
	plc.jq(".celulaFormularioCaixaMarcacao  input", linhaAnt).attr("checked",  false);
}

/** Retorno nome do campo detalhe pelo nome do campo simples. */
function getDetalhePeloCampo (nomeCampo){
	if (nomeCampo.indexOf("corpo:formulario:") > -1){

		var padraoDetalhe = new RegExp ("corpo:formulario:(\\w+):(\\d+):(\\w+)");
		var padraoSubDetalhe = new RegExp ("corpo:formulario:(\\w+):(\\d+):(\\w+):(\\d+):(\\w+)");
		if (padraoSubDetalhe.exec(nomeCampo) != null){
			var grupos = padraoSubDetalhe.exec(nomeCampo);
			var nomeCampoSubDetalhe = grupos[1]+"["+grupos[2]+"]."+grupos[3];
			return nomeCampoSubDetalhe;
		} else if (padraoDetalhe.exec(nomeCampo) != null) {
			var grupos = padraoDetalhe.exec(nomeCampo);
			var nomeCampoDetalhe = grupos[1];
			return nomeCampoDetalhe;
		} else {
			return "";
		}
	} else {
		var padraoDetalhe = new RegExp ("corpo:(\\w+):(\\d+):(\\w+)");
		var padraoSubDetalhe = new RegExp ("corpo:(\\w+):(\\d+):(\\w+):(\\d+):(\\w+)");
		if (padraoSubDetalhe.exec(nomeCampo) != null){
			var grupos = padraoSubDetalhe.exec(nomeCampo);
			var nomeCampoSubDetalhe = grupos[1]+"["+grupos[2]+"]."+grupos[3];
			return nomeCampoSubDetalhe;
		} else if (padraoDetalhe.exec(nomeCampo) != null) {
			var grupos = padraoDetalhe.exec(nomeCampo);
			var nomeCampoDetalhe = grupos[1];
			return nomeCampoDetalhe;
		} else {
			return "";
		}
	}
}

/** jCompany 2.7.2 - Fun��o para sobreposi��o em caso de regras para inibi��o de alerta de altera��o. */
PlcGeral.prototype.inibeAlertaAlteracao = function (objeto) {return false;}
function inibeAlertaAlteracaoPadrao(objeto){

	if(getAlertaAlteracao() != "S")
		return true;
	var inibeAlertaAtributo = false;

	try{
		inibeAlertaAtributo = !( typeof objeto != "undefined" && (typeof objeto.id != "undefined" && objeto.id.indexOf("EXIBE_ALERTA_ALTERACAO") > -1));								
	}catch(e){}
	var ehNovoDetalhe = objeto != undefined && objeto.id != undefined && objeto.id.indexOf("botaoAcaoNovo") > -1  && !get('detCorrPlc') == "";

	return inibeAlertaAtributo || ehNovoDetalhe;
}

/**
 * Pega valor de campo de formul�rio quando usando apache trinidad para renderizar.
 * Para pegar de itens pegar o indice com '#{plcItensStatus.index}'
 */
function getValorJsf(field, indice, detalhe)  {
	if (indice != null) {
		field = "corpo:formulario:"+detalhe+":"+indice+":"+field;
	} else {
		field = "corpo:formulario:"+field;     
	}
	return retornaValorCampo(field);
}
/**
 * Coloca valor em campo de formul�rio quando usando JSF para renderizar.
 */
function setValorJsf(campo, indice, detalhe, valor)  {
	if (indice != null) {
		// Assume iteracao 
		campo = "corpo:formulario:"+detalhe+":"+indice+":"+campo;
	} else {
		campo = "corpo:formulario:"+campo;     
	}
	return set(campo,valor);
}
/**
 * Pega uma referencia a campo de formul�rio quando usando JSF para renderizar.
 */
function getObjetoJsf(campo, indice, detalhe)  {
	if (indice != null) {
		// Assume iteracao 
		campo = "corpo:formulario:"+detalhe+":"+indice+":"+campo;
	} else {
		campo = "corpo:formulario:"+campo;     
	}
	return getCampo(campo);
}

/** Exclui uma linha de tabular ou detalhe  */
function deleteItem(colecao,indice) {
	set('corpo:formulario:formAuxPlc',colecao+'#'+indice);					
	plc.jq('#corpo\\:formulario\\:botaoDeleteItem').get(0).click();
}

/** Configurar a utiliza��o de alerta de altera��o. */
function setAlertaAlteracao(evt, alerta){
	set("corpo:formulario:alertaAlteracaoPlc",alerta != "" ? "S" : "");
	return eventoTrataonchange(evt, this);
}

/** Recuperar utiliza��o de alerta de altera��o. */
function getAlertaAlteracao(){
	return get("corpo:formulario:alertaAlteracaoPlc");
}

/** Recuperar campos de entrada com seus respectivos valores em um array. */
function getCamposEntrada (strTest, atributo, operador, f) {

	var condicao = 	(typeof strTest != "undefined" &&
			typeof atributo != "undefined" &&
			typeof operador != "undefined") ?
					operador == "indexOf" ?
							".indexOf('"+strTest+"') >= 0" :
								operador+strTest : "";
	var arrayCamposEntrada = new Array();
	var form = f || getForm();
	if(form){
		var formElements = form.elements;
		//
		for(e = 0; e < formElements.length; e++ ){
			//
			if(condicao == "" || plcEval(formElements[e], atributo, condicao)){
				arrayCamposEntrada[arrayCamposEntrada.length] = formElements[e];
			}
		}
	}
	return arrayCamposEntrada;
}

function limparReferenciaVinculado() {
	alterouVinculado = false;
	idVinculadoAlterado = "";
}

function mostraLimparVinculado(id) {
	var botao = document.getElementById(id);
	if (botao != null && botao != undefined) {
		botao.style.display = '';
		limparReferenciaVinculado();
	}
}

/** Desmarca lista de sele��o quando campo de argumento � focado. */
function desmarcaListaSelecao(){
	if(getVarGlobal("trSelecao") != null){	
		alteraClasse('OBJETO', getVarGlobal("trSelecao"), 'CLASSE', 'campoComErro','INICIAL');
		setVarGlobal("trSelecao", null)
	}
}

var flagDesprezarA = new Array();
function pegaFlagDesprezar(chaveDet,chaveSubDet) {
	for(var i = 0; i < flagDesprezarA.length; i++){
		if ((chaveDet.indexOf(flagDesprezarA[i].componente)>-1 && chaveSubDet=='') ||
				(chaveSubDet!='' && flagDesprezarA[i].componente.indexOf(chaveSubDet)>-1)) {
			return flagDesprezarA[i].flagDesprezar;
		}
	}
	return '';
}


/** Escreve marca de obrigatorio ao final do campo. */
function escrevePlcAfter(tags,nameClass,alt,src) {
	var ts = tags.length;
	for (var i = 0; i < ts; ++i) {
		var tag = tags[i];
		if (tag.className.indexOf('AFReadOnly') < 1) {
			if (tag.className.indexOf(nameClass) > -1) {
				var $tag = plc.jq(tag);
				if (!$tag.hasClass('plc-obrigatorio')) {
					var $parent = $tag.parent();
					$tag.detach();
					$tag.addClass('plc-obrigatorio');
					var $table = plc.jq('<table><tr><td/><td/></tr></table>');
					$table.find('td:eq(0)').append($tag);
					$table.find('td:eq(1)').append('<span class="plc-img-obrigatorio"><img id="'+ tag.childNodes[0].id +'_icone" src="'+src+'"style="float:left; border:0" title="'+alt+'" /></span>');
					$parent.append($table);
				}
			}
		}
	}
}

/** 
 * 
 * Fun��o que registra os campos de retorno no array.
 * Array para conter os campos para l�gicas de retorno de valores
 */
var camposRetorno = new Array();
function getCampoRetornoById(id) {
	for(i = 0; i < camposRetorno.length; i++) {
		if(camposRetorno[i].id == id)
			return camposRetorno[i].nome;
	}
	return "";
}

/****************************************************************
Bloqueia digita��o de caracter n�o permitido pelo tipo
------------------------------------------------
Fun��o:		validaCaracter(campo, evt, tipo)
------------------------------------------------

=> campo  =	Tipo: String
		Nome do campo atual
=> evt    =	[event]
		O evento disparado para chamar a Fun��o
		(tecla pressionada, por exemplo)
=> tipo   =	Tipo: String
		Tipo do valor no campo [D=Data; V=Valor ; H=Hora]

<Chamar no ONKEYDOWN do campo testando seu retorno>

Exemplo:	return validaCaracter('fldCPF',event, "V");

 ****************************************************************/
function validaCaracter(campo, evt, tipo) {
	//Contribui��o Dionatan Almeida
	var key;
	var keychar;
	key = getKeyCode(evt);	
	/**
	 * Cdigos de teclas do teclado num�rico
		de 96 a 105 =  0 - 9
	        106 = *
	        107 = +
	        109 = -
	        110 = ,
	        111 = /
	        194 = .
	 */

	// array das setas
	var keyseta = new Array(37,39);

	// array dos numeros + setas
	var keynum = new Array(96,97,98,99,100,101,102,103,104,105,37,39);

	// array de data + numeros
	var keynumD = new Array(96,97,98,99,100,101,102,103,104,105,39,111);

	// array dos numeros
	var keydigit = new Array(96,97,98,99,100,101,102,103,104,105);

	keychar = String.fromCharCode(key);

	var ehValido = false;

	if ((key==null) || (key==0) || (key==8) || (key==9)|| (key==27) || (key==46)) {
		ehValido = true;
	} else if (tipo=="V" && ((("0123456789").indexOf(keychar) > -1) || validaKeyArray(key,keynum))) {
		//valor
		ehValido = true;
	} else if (tipo=="D" && ((("/0123456789").indexOf(keychar) > -1) || validaKeyArray(key,keynumD))) {
		//data
		ehValido = true;
	} else if (tipo=="DT" && (((" :/0123456789").indexOf(keychar) > -1) || validaKeyArray(key,keynumD))) {
		//datetime
		ehValido = true;
	} else if (tipo=="H" && (((":0123456789").indexOf(keychar) > -1) || validaKeyArray(key,keynum))) {
		//hora
		ehValido = true;
	} else if (tipo=="A" && (("0123456789").indexOf(keychar) == -1 || validaKeyArray(key,keyseta))) {
		//
		ehValido = true;
	} else if (tipo=="L" && !((("0123456789").indexOf(keychar) > -1))) {
		ehValido = true;
	}	   

	if (!ehValido && campo != null) {
		while (campo.value.indexOf(keychar) > -1 || campo.value.indexOf(keychar.toLowerCase()) > -1){
			campo.value = campo.value.replace(keychar, "");
			campo.value = campo.value.replace(keychar.toLowerCase(), "");
		}
	}	
	//Adapta��o para Internet Explorer, visto que, no Internet Explorer, o keychar n�o chega corretamente para os s�mbolos abaixo. 
	// TODO ver como melhorar, pois no internet explorer continua aceitando / e : por causa de Data
	var caracteresEspeciais = new Array(String.fromCharCode(6), String.fromCharCode(252), String.fromCharCode(219), String.fromCharCode(180), String.fromCharCode(220), "!","@","#","$","%","^","&","*","(",")", "_","-","+","=","{","}","|","[","]","\\","\"",":",";","'","<",">","?",",",";","`","~");

	for (var i=0;i<caracteresEspeciais.length;i++){
		var caracter = caracteresEspeciais[i];
		if (campo.value.indexOf(caracter) > -1){
			campo.value = campo.value.replace(caracter, "");
		}
	}

	return ehValido;
}

var arrayValidacaoCampos 	= new Array();
function validacaoCampo (argumentos){
	this.nomeCampo 		= argumentos[0];	
	this.formatoCampo 	= argumentos[1];
	this.msgErro		= argumentos[2];
	this.PARAM_0	= "";
	this.PARAM_1	= "";
	this.PARAM_2	= "";
	this.PARAM_3	= "";

	for(i = 3; i < argumentos.length; i++) {
		if(argumentos[i] == "PARAM_0")
			this.PARAM_0 = argumentos[++i];
		if(argumentos[i] == "PARAM_1")
			this.PARAM_1 = argumentos[++i];
		if(argumentos[i] == "PARAM_2")
			this.PARAM_2 = argumentos[++i];
		if(argumentos[i] == "PARAM_3")
			this.PARAM_3 = argumentos[++i];
	}
	this.msgErro = this.msgErro.replace("{0}", this.PARAM_0);
	this.msgErro = this.msgErro.replace("{1}", this.PARAM_1);
	this.msgErro = this.msgErro.replace("{2}", this.PARAM_2);
	this.msgErro = this.msgErro.replace("{3}", this.PARAM_3);
}

function validacaoCriaCampo(){
	arrayValidacaoCampos[arrayValidacaoCampos.length] = new validacaoCampo(arguments);
}

/**
 * Seleciona (marca/desmarca) todos os checkbox
 */
function selecionaTodosCheckbox(id) {

	var todosSelecionados = true;
	if (!isEmpty(id)) {
		id = '#' + id;
	}
	plc.jq(':checkbox',id + ' .celulaFormularioCaixaMarcacao').each(function(){
		if (!this.checked){
			todosSelecionados = false;
		}
	});

	if (todosSelecionados){
		plc.jq(':checkbox', id + ' .celulaFormularioCaixaMarcacao').each(function(){this.checked=false});
	}else{
		plc.jq(':checkbox', id + ' .celulaFormularioCaixaMarcacao').each(function(){this.checked=true});
	}
}

function autoRecuperacaoVinculado(vinculado, propRecupera){
	var p = document.createElement("input");
	p.type = "hidden";
	p.name = 'skipValidation';
	p.value = 'true';
	var f = jQuery('#corpo\\:formulario');
	f.append(p);	
	if (!(typeof mojarra == 'undefined'))
		mojarra.ab('corpo:formulario',window.event,'keyup','@form','@this');
	else
		jsf.ajax.request('corpo:formulario',window.event,'keyup','@form','@this');	
	campoVinculadoFocus = vinculado;
}

/** Remover m�scara de um campo */
function removeMascaraCampo (prefixoElementos){

	var elements = plc.jq(prefixoElementos)[0].elements;
	plc.jq.each(elements, function(){
		if (typeof plc.jq(this).data('jcompany-submit-unmask') == 'function') {
			var jCompanySubmitUnmask = plc.jq(this).data('jcompany-submit-unmask');
			var valor = jCompanySubmitUnmask.call() || "";
			plc.jq(this).val(valor);
			return;
		}
	});
}

/**
 * Adicionar a propriedade skipValidation = true no form, para a valida��o ser desprezada.
 * Pode ser utilizada em qualquer evento do browser, desde que seja antes do submit, evidando contaminar o forma com esse valores
 */
function addSkipValidation() {
	plc.jq('<input type="hidden" value="true" name="skipValidation" />').appendTo("#corpo\\:formulario");
}

/**
 * Fun��o ger�rica a ser executada antes do submit 
 * Utilizada para:
 *  - Remover as mascaras dos campos, tipo cpf
 *  - Atualizar os conteudos dos campos que tem riaUsa="fckeditor".
 *  		Nesse caso, como o fckeditor utiliza um campo para formata o texto como HTML
 *  		Antes do submit, jogamos esse conteudo para o campo correto do form. 
 */
function beforeSubmit(){
	removeMascaraCampo('#corpo\\:formulario');

	if (typeof(FCKeditorAPI)!== 'undefined'){
		var textareas = plc.jq('textarea');
		if (textareas) {
			var i;
			for (i=0;i<textareas.length ;i++)
			{
				if (FCKeditorAPI.GetInstance(textareas[i].id)) {
					FCKeditorAPI.GetInstance(textareas[i].id).UpdateLinkedField();	
				}	
			}
		}
	}	
	
	if (typeof(CKEDITOR)!== 'undefined'){
		var textareas = plc.jq('textarea');
		if (textareas) {
			var i;
			for (i=0;i<textareas.length ;i++)
			{
				if (CKEDITOR.instances[textareas[i].id]) {
					CKEDITOR.instances[textareas[i].id].updateElement();
				}	
			}
		}
	}	

}

/**
 * Fun��o utilizada para exporta��o de Registros da Pesquisa.
 * Esta fun��o abre uma Janela e faz a requisi��o no servidor para montar a exporta��o de acordo com o 'formato' escolhido.
 *
 * Chama o Servlet de exporta��o 'PlcExportaJsfcaoServlet' que recupera da Conversa��o a lista de registros da sele��o, 
 * evitando refazer a pesquisa.
 * @autor Pedro Henrique 
 */	
PlcGeral.prototype.exportaPopup = function(contextoAplicacao,plcURLComBarra,isRequestJSF){
	//TODO Mover para arquivo plc.geral.form.js

	var campoExportaPlc 	= getCampo("exportaPlc");
	var parentForm 			= getForm();
	var metodo				= 'post'; 

	var win 		= janela("");
	var action 		= "";
	var	conteudo  	= '<html><body> <form name="inicialForm" method="post" action="'+ contextoAplicacao +  plcURLComBarra ;
	/**
	 * Com JSF chama o Servlet de exporta��o 'PlcExportaJsfcaoServlet' que recupera da Conversa��o a lista de registros da sele��o, 
	 * evitando refazer a pesquisa.
	 */
	if (isRequestJSF == 'S'){
		var conversationIdPlc = getCampo('corpo:formulario:conversationIdPlc');
		action = contextoAplicacao + '/exportacaojsf">';
		if (typeof conversationIdPlc != "undefined")
			action +=  ' <input type="hidden" name="conversationIdPlc" value="' + conversationIdPlc.value +'" id="conversationIdPlc" >';
		action +=  ' <input type="hidden" name="action" value="' + plcURLComBarra +'" id="action" >';
		if (typeof campoExportaPlc != "undefined")
			action +=  ' <input type="hidden" name="fmtPlc" value="' + campoExportaPlc.value +'" id="fmtPlc" >';
	} 
	// montando o conte�do da p�gina
	var	conteudo  	= '<html><body> <form name="inicialForm" method="' +metodo+ '" action="'+ action + '</form> </body></html>';
	win.document.write(conteudo);
	var theForm 			= getForm();  
	theForm.style.display 	= 'none';
	theForm.submit();
	// Voltando com a sele��o do campo de exporta��o para a primeira Op��o  [Exportar para ...]
	if(campoExportaPlc) {campoExportaPlc.selectedIndex = 0;}
}

PlcGeral.prototype.formSubmit = function(action,evento){
	//TODO Mover para arquivo plc.geral.form.js
	// N�o retirar o timeout porque deve esperar a fun��o TrocaAba ser executada
	setTimeout("document.getElementById('corpo:formulario:botaoAcaoRecuperaPorDemanda').click()",100);
}

function mudaPaginaGridJsf(numReg) {
	var pag = plc.jq('option:selected', '[name=corpo\\:formulario\\:plc-pag-list-jsf]').val();
	var destino = (((pag - 1) * numReg) + 1);
	plc.jq('#navDe').val(destino);
	plc.jq('#corpo\\:formulario\\:botaoAcaoPesquisar').click();
}

function createLineSelection(link) {

	plc.jq("table.plc-table-seljsf tr td span.idItem").each(function(index, value) {
		var cod = plc.jq(this).text();
		plc.jq(this).parents("tr:first")
		.click(function() {document.location.href= link + "?id=" + cod;})
		.addClass(((index % 2) == 0 ? "linhapar" : "linhaimpar ui-widget-default"));

	});
}
