/**
 * plc.geral.tabs
 * Funções javascripts utilizadas pelo jCompany/Jaguar 
 * As funções abaixo, fornecem implementação para as abas, ativação e visualização.
 */

/** Mostrar aba clicada, esconder abas restantes. */
var layers    = new Array();
var layersFilhos   = new Array();
var abaAgilUsaAncora = true;
var tabSelecionada = "";
var tabFilhoSelecionada = "";
function showHideAba(aba,ancora){
	var layersAux = layers;
	var _doc = getRootDocument();
	if(aba.indexOf("->") > -1)
		layersAux = layersFilhos;	
	for( var i=0; i<layersAux.length; i++){
		try{
			if(layersAux[i] == aba){
				if(aba.indexOf("->") < 0)
			   		tabSelecionada = aba;
				_doc.getElementById(layersAux[i]).className = "ativada";
				_doc.getElementById("td_borda_"+layersAux[i]).className = "ativada";
			   	_doc.getElementById(layersAux[i]+"_corpo").className = "tabVisivel";
				if(ancora != null && ancora != "" && abaAgilUsaAncora)
				   	_doc.location.hash=ancora;
				setValorCampo('corpo:formulario:tabCorrenteDinamicoPlc',aba);
				var numAba	= tabSelecionada.substring(tabSelecionada.indexOf("_")+1);
				tabFolderFocaCampo(numAba)		
			}else{
			   	_doc.getElementById(layersAux[i]).className = "";
				_doc.getElementById("td_borda_"+layersAux[i]).className = "";
			   	_doc.getElementById(layersAux[i]+"_corpo").className = "tabOculta";
			}
		}catch(e){}
  	}
}

/** Garante que após a submissão a mesma aba do tab folder permaneça selecionada. */
function mantemAbaSelecionada (){
	tabSelecionada = get('corpo:formulario:tabCorrenteDinamicoPlc') != "" ? get('corpo:formulario:tabCorrenteDinamicoPlc') : tabSelecionada;
	if(tabSelecionada.indexOf("->") > -1){
		var tabSelecionadaAux = tabSelecionada; 
		showHideAba(tabSelecionada.substring(0,tabSelecionada.indexOf("->")));
		tabSelecionada = tabSelecionadaAux; 
		showHideAba(tabSelecionada);
	}else{
		setTimeout("showHideAba('" + tabSelecionada + "')", 0);
		if(tabFilhoSelecionada != "")
			showHideAba(tabFilhoSelecionada);
	}	
}

/** Trocar informação da aba clicada. */
function trocaAba(index,thisId,nomeAba){
	var detCorrPlc = "";
	if (tabFolderCamposFoco != null && typeof tabFolderCamposFoco != "undefined")
		detCorrPlc = getDetalhePeloCampo(tabFolderCamposFoco[index]);
	if(detCorrPlc == "" && getVarGlobal(index) && typeof getVarGlobal(index) != "undefined")
		detCorrPlc = getVarGlobal(index);
	set('detCorrPlc',detCorrPlc);
	showHideAba(thisId, nomeAba);
}

/** Comuta de aba automaticamente. */
 function comutaAbaFacelets(posicaoAba) {
	 plc.jq('#plc-form-tab').tabs('select', posicaoAba);
 }

/** Focar o campo mais apropriada na tab folder após navegação automática. */
 function tabFolderFocaCampo(numNavAba){
 	if (tabFolderCamposFoco != null && tabFolderCamposFoco.length > 0){
		setFocus(tabFolderCamposFoco[numNavAba - 1]);
 	}else{
 		var camposVolta = getVarGlobal("cacheCamposVolta");
 		if (camposVolta!=null) {
 			if(numNavAba == 0 && getVarGlobal('campoFocadoPlc')){
 				setTimeout("setFocus('"+getVarGlobal('campoFocadoPlc').name+"')",20);
 				return;
 			}	
 			if(numNavAba > 0)
 				numNavAba--;	
 			if(camposVolta[numNavAba])
 				setTimeout("setFocus('"+camposVolta[numNavAba].name+"')",20);
 		}
 	}
 }

 /** Esconde as abas e mostra fieldsets*/
function hideAbas() {
	
	var html = plc.jq("body");

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
	
	//Ajustando o div principal
	plc.jq('.plc-form-tab ul').hide();
	plc.jq('.plc-form-tab').css('border','none');
	plc.jq('.plc-form-tab').css('background','none');
	
}
 
 /** Transforma uma aba em fieldset*/
function transformaAbaEmFieldSet(html, div, id) {

		//criando a legenda
		var titulo = plc.jq('<legend class="plc-fieldset-legend">');
		//pegando o titulo da aba
		var texto = plc.jq("[href=#" + id + "]",html).children("span").text().replace("...","");
		//adicionando o titulo a legenda
		plc.jq(titulo).text(texto);
		//criando o fieldset
		var fieldset = plc.jq('<fieldset class="plc-fieldset">');
		//removendo a classe de abas
		plc.jq(div).removeClass("ui-tabs-hide");
		//inserindo o fieldset antes da aba
		plc.jq(fieldset).insertBefore(div);
		//jogando a aba para dentro do fieldset
		plc.jq(fieldset).prepend(div);
		//insertindo a legenda dentro do fieldset
		plc.jq(fieldset).prepend(titulo);


}
 