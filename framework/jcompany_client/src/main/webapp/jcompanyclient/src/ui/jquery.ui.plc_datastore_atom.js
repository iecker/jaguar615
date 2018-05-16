/*
 * jQuery UI jCompany Framework- O Plugin jQuery UI Para Construir Aplicações jCompany
 * Copyright (c) Powerlogic
 * 
 * 
 * Autores:  
 *    Savio Grossi - savio.grossi@powerlogic.com.br
 *    
 *    
 * Depends:
 *	ui.core.js  
 */
(function($) {

$.widget("ui.plc_datastore_atom", $.extend({}, $.ui.plc_datastore.prototype, {
	
	_init : function() {
		$.ui.plc_datastore.prototype._init.call(this); // call the original function
	}
	
	,destroy : function() {
		$.ui.plc_datastore.prototype.destroy.call(this); // call the original function
	}
	
	,_criaColecaoObjetos: function(xml){
		var colecao = [];
		$('entry > content > *', xml).each(function(){
			var o = {};
			// Adiciona cada propriedade no Objeto.
			$(this).contents().each(function(){
				o[this.tagName] = $(this).text();
			});
			// Retorna um novo objeto json!
			colecao.push(o);
		});
		return colecao;
	}
	
	,_criaXmlEntry: function(entidade, o){
		var xml = '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>';
		xml += '<entry xmlns="http://www.w3.org/2005/Atom" xmlns:plcAtom="http://www.powerlogic.com.br/2009/AtomPlcExt">\n';
		xml += '	<content type="application/xml">\n';
		xml += '		<'+ entidade +' xmlns:ns3="http://www.w3.org/2005/Atom" xmlns="">\n';
		for(var p in o){
			// TODO: Erro de parser de Data no ATOM
			if (p == 'dataUltAlteracao') {
				continue;
			}
			xml += '			<'+p+'>'+ o[p]+'</'+p+'>\n';
		}
		xml += '		</'+ entidade +'>\n';
		xml += '	</content>\n';
		xml += '</entry>\n';
		return xml;
	}
	
	,recuperaColecao : function(evt, dadosRequisicao) {
		
		var self = this;
		
		var entidade = $(document).plc_datamodel('getEntidade', dadosRequisicao.entidade);
		
		$.ajax({
			type: "GET"
			,url: this.options.urlBase + entidade
			,dataType: "xml"
			,success: function(xml) {
				var colecao = self._criaColecaoObjetos(xml);
				$(document).trigger('plc_datastore_recuperaColecaoResposta', {colecao : colecao});
			}
		});
	}
	
	,recupera : function(evt, dadosRequisicao) {
    	
		var self = this;
		
		var entidade = $(document).plc_datamodel('getEntidade', dadosRequisicao.entidade);
		
		var objeto = dadosRequisicao.objeto;
		
		$.ajax({
			url: this.options.urlBase + entidade
			,type: "POST"
			,contentType: "application/atom+xml"
			,processData: false
			,data: this._criaXmlEntry(objeto)
			,success: function(xml) {
				var objeto = self._criaColecaoObjetos(xml)[0];
				$(document).trigger('plc_datastore_recuperaColecaoResposta', {objeto : objeto});
			}
		});
	}
	
	,inclui : function(evt, dadosRequisicao) {
    	
		var self = this;
		
		var entidade = $(document).plc_datamodel('getEntidade', dadosRequisicao.entidade);
		
		var objeto = dadosRequisicao.objeto;
		
		$.ajax({
			url: this.options.urlBase + entidade
			,type: "POST"
			,contentType: "application/atom+xml"
			,processData: false
			,data: this._criaXmlEntry(entidade, objeto)
			,success: function(xml) {
				var objeto = self._criaColecaoObjetos(xml)[0];
				$(document).trigger('plc_datastore_incluiResposta', {objeto : objeto});
			}
		});
	}
	
	,atualiza : function(evt, dadosRequisicao) {
    	
		var self = this;
		
		var entidade = $(document).plc_datamodel('getEntidade', dadosRequisicao.entidade);
		
		var objeto = dadosRequisicao.objeto;
		
		$.ajax({
			url: this.options.urlBase + entidade + '/' + objeto.id + '/' + objeto.versao
			,type: "PUT"
			,contentType: "application/atom+xml"
			,processData: false
			,data: this._criaXmlEntry(entidade, objeto)
			,success: function(xml) {
				var objeto = self._criaColecaoObjetos(xml)[0];
				$(document).trigger('plc_datastore_atualizaResposta', {objeto : objeto});
			}
		});
	}
	
	,exclui : function(evt, dadosRequisicao) {
		
		var self = this;
		
		var entidade = $(document).plc_datamodel('getEntidade', dadosRequisicao.entidade);
		
		var objeto = dadosRequisicao.objeto;
		
		$.ajax({
			url: this.options.urlBase + entidade + '/' + objeto.id + '/' + objeto.versao
			,type: "DELETE"
			,contentType: "application/atom+xml"
			,processData: false
			,data: this._criaXmlEntry(entidade, objeto)
			,success: function(xml) {
				$(document).trigger('plc_datastore_excluiResposta', {objeto : objeto});
			}
		});
	}
}));

$.ui.plc_datastore_atom.defaults = $.extend({}, $.ui.plc_datastore.defaults, { 
     urlBase: ''
});

})(jQuery);