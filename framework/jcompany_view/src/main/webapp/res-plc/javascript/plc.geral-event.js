
var msgAlteracao;
var disparouBotao = false;

function regMensagem(evento, tipo, msg) {
	if (this.msgArray == null) {
		this.msgArray = new Object();
	}
	
	this.msgArray[evento] = new regMsg(evento, tipo, msg);
}

function regMsg(evento, tipo, msg) {
	this.evento	= evento;
	this.tipo 	= tipo;
	this.msg	= msg;
}

function getMsgArray(evento) {
	return this.msgArray[evento];
}

function enviarMensagem(evento) {
	auxArray = getMsgArray(evento);
	if (auxArray != null) {
		if(auxArray.tipo == "CONFIRMACAO")
			return confirm(auxArray.msg);
		else if (auxArray.tipo == "ALERTA")
			alert(auxArray.msg);
		else if (auxArray.tipo == "ALERTA_ERRO") {
			alert(auxArray.msg);
			return false;
		}
	}
	return true;
}

/** Construtor para objeto PlcEvento. */
function PlcEvento (){
	
}

/** Variável de instancia do objeto PlcEvento. */
var plcEvento = new PlcEvento();

/** Guardar objeto do foco para facilitar criação de novos detalhes. */
function trataOnFocus (evt) {
	// Pega nome do detalhe, se campo contiver, ou esvazia o "detCorrPlc"
	var nomeDetalhe = getDetalhePeloCampo(this.name);
	plc.jq('#detCorrPlc').val(nomeDetalhe);
	if (this.oldOnFocus && this.oldOnFocus != trataOnFocus){
		this.oldOnFocus(evt);
	}
}

/** Recuperar o elemento associado ao evento ocorrido. */
PlcEvento.prototype.getEventoElemento = function(evento){
	var elemento = null;
	if(ExpYes){
		if(evento.srcElement)
			elemento = evento.srcElement;
	}else{
		if(evento.target)
			elemento = evento.target;
	}
	return elemento
}

/** Recuperar o evento ocorrido. */
PlcEvento.prototype.getEventoAtual = function(evento){
	if(ExpYes && window.event)
		return window.event.type;
	else if (NavYes && evento && Event){
		if(evento.toUpperCase() == "ONCHANGE" && document.captureEvents(Event.ONCHANGE))
			return document.captureEvents(Event.ONCHANGE);
		if(evento.toUpperCase() == "ONCLICK" && document.captureEvents(Event.ONCLICK))
			return document.captureEvents(Event.ONCLICK);
		if(evento.toUpperCase() == "ONKEYDOWN" && document.captureEvents(Event.ONKEYDOWN))
			return document.captureEvents(Event.ONKEYDOWN);
	}
}

/** jCompany 2.5. Acrescentar evento onFocus para logica de registro do objeto. */
function setUpOnFocusHandlers () {
	//TODO Ver possibilidade de adequar a novo modelo de eventos
	if(getRootDocument().forms) {
		for (var f = 0; f < getRootDocument().forms.length; f++) {
			if(getRootDocument().forms[f].elements) {
				for (var e = 0; e < getRootDocument().forms[f].elements.length; e++) {
					if (getRootDocument().forms[f].elements[e] &&
							(getRootDocument().forms[f].elements[e].type == 'text'
									|| getRootDocument().forms[f].elements[e].type == 'textarea'
										|| getRootDocument().forms[f].elements[e].type == 'select-one'
											|| getRootDocument().forms[f].elements[e].type == 'select-multiple'
												|| getRootDocument().forms[f].elements[e].type == 'password'
													|| getRootDocument().forms[f].elements[e].type == 'checkbox'
														|| getRootDocument().forms[f].elements[e].type == 'radio'
															|| getRootDocument().forms[f].elements[e].type == 'file'
																|| getRootDocument().forms[f].elements[e].type == 'fileupload')) {
						if (typeof getRootDocument().forms[f].elements[e].oldOnFocus == "undefined"){
							getRootDocument().forms[f].elements[e].oldOnFocus = getRootDocument().forms[f].elements[e].onfocus;
							getRootDocument().forms[f].elements[e].onfocus = trataOnFocus;
						}
					}
				}
			}
		}
	}
}

/** Desmarca item da lista de seleção pois foi focado campo de argumento. */
PlcEvento.prototype.trataEventoJcp = function (evt) {
	desmarcaListaSelecao();
	return true;
};

/** jCompany 2.7.2 - Tratar evento onclick genericamente. */
PlcEvento.prototype.eventoTrataClick = function (evt) {return true;};
function eventoTrataonclick(evt, objeto){
	var retClick = true;
	if(typeof objeto == "undefined")
		objeto = this;
	if(plcEvento.eventoTrataClick(evt)){
		if (objeto.oldonclick)
			retClick = objeto.oldonclick(evt);
		if(retClick)
			retClick = plcEvento.trataEventoJcp(evt);
	}else
		return false;
	return retClick;
}

/** jCompany 2.7.2 - Tratar evento onchange genericamente. */
PlcEvento.prototype.eventoTrataChange = function (evt) {return true;};
function eventoTrataonchange(evt, objeto){

	if(typeof evt == "undefined")
		evt = plcEvento.getEventoAtual('ONCHANGE')
		var retChange = true;
	if(typeof objeto == "undefined")
		objeto = this;
	if(plcEvento.eventoTrataChange(evt)){
		if (objeto.oldonchange)
			retChange = objeto.oldonchange(evt);
		if(retChange)
			retChange = plcEvento.trataEventoJcp(evt);
	}else
		return false;
	return retChange;
}

/** jCompany 3.0 - Tratar evento onfocus genericamente. */
PlcEvento.prototype.eventoTrataFocus = function (evt) {return true;};
function eventoTrataonfocus(evt, objeto){
	var retFocus = true;
	if(typeof objeto == "undefined")
		objeto = this;
	if(plcEvento.eventoTrataFocus(evt)){
		if (objeto.oldonfocus)
			retFocus = objeto.oldonfocus(evt);
		if(retFocus)
			retFocus = plcEvento.trataEventoJcp(evt);
	}else
		return false;
	return retFocus;
}

/** jCompany 3.0 - Tratar evento onblur genericamente. */
PlcEvento.prototype.eventoTrataBlur = function (evt) {return true;};
function eventoTrataonblur(evt, objeto){
	var retBlur = true;
	if(typeof objeto == "undefined")
		objeto = this;
	if(plcEvento.eventoTrataBlur(evt)){
		if (objeto.oldonblur)
			retBlur = objeto.oldonblur(evt);
		if(retBlur)
			retBlur = plcEvento.trataEventoJcp(evt);
	}else
		return false;
	return retBlur;
}

/** Configurar um evento para um elemento informado*/
function setUpOnEventoElemento (idElemento, evento, funcao) {
	setUpEventos ("", idElemento, evento, funcao,"");
}

/** Configurar um evento para todas os elementos da tag informada*/
function setUpOnEventoTag (tag, evento, funcao) {
	if( ExpYes && ( funcao == 'destacaCampoFocado' || funcao == 'reverteDestaqueCampoFocado') ){ 
		return ; 
	}
	setUpEventos (tag, "", evento, funcao,"");
}

/** Configurar um evento para um elemento informado da tag informada*/
function setUpOnEventoTagCampoNome (tag, evento, funcao, nome) {
	setUpEventos (tag, "", evento, funcao, nome);
}

/** Configurar eventos genericamente*/
function setUpEventos (tag, idElemento, evento, funcao, nome) {

	var tags;
	var tipo = "";
	var elementos;
	if(typeof tag != "undefined" && tag != ""){
		var posTipo = tag.indexOf("#")
		if(posTipo >= 0){
			tags = getRootDocument().getElementsByTagName(tag.substring(0,posTipo));
			tipo = tag.substring(posTipo+1)
		}else
			tags = getRootDocument().getElementsByTagName(tag);
	} else if (typeof nome != "undefined" && nome != ""){
		tags = new Array();
		tags[tags.length] = getCampo(nome);
	} else if (typeof idElemento != "undefined" && idElemento != "")
		elementos = getElementoPorId(idElemento);

	if(tags) {
		for (var t = 0; t < tags.length; t++) {
			var umaTag = tags[t];
			if(umaTag && ((tipo == "" || umaTag.type == tipo) && 
					(nome == "" || umaTag.name == nome))) {
				if(eval("umaTag."+funcao) != funcao ) {
					try {
						eval("umaTag.old"+evento.toLowerCase()+" = umaTag."+evento.toLowerCase());
					} catch(err) {}
				}	
				if(typeof funcao != "undefined" && funcao != ""){
					eval("umaTag."+funcao+"='"+funcao+"'");
					eval("umaTag."+evento.toLowerCase()+" = "+funcao);
				}
				else
					eval("umaTag."+evento.toLowerCase()+" = eventoTrata"+evento.toLowerCase());
			}
		}
	}
	if(elementos) {
		var elementoAnterior = elementos;
		if(!elementos.length){
			elementos = new Array();
			elementos[elementos.length] = elementoAnterior
		}
		if (!ExpYes && evento.substring(0,2)=="on") {
			evento = evento.substring(2);
		}
		for (e = 0; e < elementos.length; e++) {
			var umElemento = elementos[e];
			if(umElemento){
				if(eval("umElemento."+funcao) != funcao )
					eval("umElemento.old"+evento.toLowerCase()+" = umElemento."+evento.toLowerCase());
				if(typeof funcao != "undefined" && funcao != ""){
					if(ExpYes)
						eval("umElemento."+evento.toLowerCase()+" = "+funcao);
					else
						eval("umElemento.addEventListener('"+evento.toLowerCase()+"',"+funcao+",false)");
				}
				else{
					if(ExpYes)
						eval("umElemento."+evento.toLowerCase()+" = eventoTrata"+evento.toLowerCase());	
					else
						eval("umElemento.addEventListener('"+evento.toLowerCase()+"','eventoTrata"+evento.toLowerCase()+",false)");
				}
			}
		}
	}
}

/** Devolver um evento (tecla clicada, por exemplo), cross-browser. */
function getEvento(evt) {
	if (ExpYes) {
		return window.event;
	} else {
		return evt;
	}
}

/** Disparar um evento de um elemento. */
function dispararEvento(elemento, evento){
	evento = evento.toLowerCase();
	if(ExpYes){
		var evObj = document.createEventObject();
		elemento.fireEvent(evento, evObj);
	}
	else{
		var evObj = document.createEvent('MouseEvents');
		evObj.initMouseEvent( evento.replace("on",""), true, true, window, 1, 12, 345, 7, 220, false, false, true, false, 0, null );
		elemento.dispatchEvent(evObj);
	}		
}

/** jCompany 2.7.2 - Verifica se ha alteração em algum campo para enviar alerta. */
function enviaAlertaAlteracao (evt) {
	if(!disparouBotao  && !inibeAlertaAlteracaoPadrao(this) && !plcGeral.inibeAlertaAlteracao(this) && plcGeral.exibeAlertaAlteracao){
		if(confirm(msgAlteracao)){
			setAlertaAlteracao();
		}else
			return false;
	}
	return eventoTrataonclick(evt, this);
}