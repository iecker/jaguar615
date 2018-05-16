/**
 * plc.geral.print
 * Fun��es javascripts utilizadas pelo jCompany/Jaguar 
 * As fun��es abaixo, utilizadas para suporte a impres�o das p�ginas, ajustando cabe�alho e corpo da p�gina.
 */

/** Construtor de objeto de impress�o. */
var objImpressao;
function objetoImpressao (url, titulo, nome, html) {
	
	this.titulo 	= titulo;
	this.nome	 	= nome;
	this.url 		= url;
	this.html 		= html;
}

/**
* Fun��o de chamada da impress�o
* Chamada: <br><dd><code>&lt;a href='#' onclick='janela("url_janela","width","height","props"); return false;'&gt;</code>
* @param url Endere�o para abertura da janela de impress�o
* @param titulo T�tulo da janela. [String]
* @param nome Nome da janela. [String]
* @variable objImpressao Objeto que cont?m dados para impress�o
* @see objetoImpressao
* @see htmlImpressao
*/
function chamarImpressao(url,titulo,nome) {
	objImpressao = new objetoImpressao(url, titulo, nome, hmtlImpressao(window));
	window.open(objImpressao.url,objImpressao.nome,'');
}

/**
* Abrir uma janela tipo IMPRESSAO<br>
* Chamada: <code><br><dd>&lt;a href=# onClick="impressao()"; return=false;&gt;Link&lt;/a&gt;
* A fun��o retira do par?metro objeto uma parte do html entre as<br>
* tags de coment�rio e retorna este html para a p�gina que a chamou
* Chamada: <br><dd><code>&lt;a href='#' onclick='janela("url_janela","width","height","props"); return false;'&gt;</code>
* @param window Objeto Window
* @return textoImpressao Conteudo final para janela de impress�o
*/
function hmtlImpressao(window) {
	var html = plc.jq("body").clone(); 
	var textoInicioImpressao = new String("<!-- INI -->");
	var textoTerminoImpressao = new String("<!-- FIM -->");
	var posIni = plc.jq(html).html().search(textoInicioImpressao);
	var posFim = plc.jq(html).html().search(textoTerminoImpressao);
	
	//tornando as abas visiveis
	html = exibirAbas(html);
	var posTer;
	var conteudoImpressao = "";
	if (posIni >= 0 && posFim > posIni ) {
		posTer = posFim - posIni + textoTerminoImpressao.length;
		conteudoImpressao = html.substr(posIni, posTer);
	}

	return conteudoImpressao;
}

function exibirAbas(html){
	
	//pegando a aba ativa
	var idAbaAtiva = plc.jq("a","li.ui-state-active",html).attr("href");
	if (idAbaAtiva) {
		var div = plc.jq(idAbaAtiva, html);
		transformaAbaEmFieldSet(html, div, idAbaAtiva.replace("#",""));	
	}

	//pegando as outras abas
	plc.jq(".ui-tabs-hide",html).each(function() {
		transformaAbaEmFieldSet(html, this, this.id);
	});

	return plc.jq(html).html().replace('%28','(').replace('%29',')');
}



/** Montar a p�gina de impress�o. */
function executaImpressao() {
	var bodyOriginal = document.body.innerHTML;

	document.body.innerHTML = "<form>"+bodyOriginal + window.opener.objImpressao.html+"</form>";
	//Verifica se utiliza impress�o inteligente
	var impIntel = getParametroUrl ( "impIntel", document.location.search);
	if(impIntel != null && impIntel.toLowerCase() == "s"){
		gerarImpressaoInteligente()
	}
}


function gerarImpressaoInteligente(){

	var tag 	= "";

	var tags 	= document.getElementsByTagName("INPUT");
	for(t = 0; t < tags.length; t++){
		tag = tags[t];
		if(tag.type == "reset"){
			tag.style.display = 'none';
		} else if (tag.type == "checkbox"){
			tag.disabled = true;
		} else if (tag.type == "password"){
			tag.disabled = true;
		} else if (tag.type == "radio"){
			tag.disabled = true;			
		} else if (tag.type != "hidden" && tag.type != "button" && tag.type != "password" && tag.type != "submit"){
			substituirCampoPorLabel(tag);
		}
	}
	tags = document.getElementsByTagName("BUTTON");
	for(t = 0; t < tags.length; t++){
		tag = tags[t];
		if (tag.parentNode.id!="barraAcoes"){
			tag.disabled = true;
		}
	}
	tags = document.getElementsByTagName("TEXTAREA");
	for(t = 0; t < tags.length; t++){
		tag = tags[t];
		substituirCampoPorLabel(tag)
	}
	tags = document.getElementsByTagName("PASSWORD");
	for(t = 0; t < tags.length; t++){
		tag = tags[t];
		tag.disabled = true;
	}
	tags = document.getElementsByTagName("SELECT");
	for(t = 0; t < tags.length; t++){
		tag = tags[t];
		substituirCampoPorLabel(tag)
	}

	tags = document.getElementsByTagName("SPAN");
	for(t = 0; t < tags.length; t++){
		tag = tags[t];
		if(tag.className == "bt")
			tag.style.display = 'none';
	}
	
	ajustesFinos();
	
	//EDITORES HTML
	/*tags = opener.getVarGlobal("editores");
	if(tags){
		for(t = 0; t < tags.length; t++){
			tag = tags[t];
			tag.style.display = 'none';
		}
	}*/
}

/** Componentes visuais que n�o se enquadram no padr�o*/
function ajustesFinos() {
	plc.jq(".plc-exclusionbox-button").hide();
	plc.jq('.plc-form-tab').css('background','none');
}
