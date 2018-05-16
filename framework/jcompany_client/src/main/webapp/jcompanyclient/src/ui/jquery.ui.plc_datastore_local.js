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

		
	
$.widget("ui.plc_datastore_local", $.extend({}, $.ui.plc_datastore.prototype, {
	
    _init: function(){
		$.ui.plc_datastore.prototype._init.call(this); // call the original function
		
		var self = this;
		
		$.each(self.options.dadosIniciais, function(i, dadoInicial) {
			
			var dados = self.dados[dadoInicial.entidadeAlias];
			
			if (dados) {
				
				var colecao = dados.colecao = {};
				
				$.each(dadoInicial.dados, function(j, objeto){
					colecao[objeto.id] = objeto;
					dados.id = Math.max(-1, objeto.id);
				});
			}
		});
		
		delete self.options.dadosIniciais;
    }
    
    ,destroy: function(){ 
		$.ui.plc_datastore.prototype.destroy.call(this); // call the original function 
    }
	
	,recuperaColecao: function (evt, dadosRequisicao) {
		
		var colecao = this.dados[dadosRequisicao.entidade].colecao;
		
		$(document).trigger('plc_datastore_recuperaColecaoResposta', {colecao : colecao});
    }
	
    ,recupera: function ( evt, dadosRequisicao ) {
		
		var dados = this.dados[dadosRequisicao.entidade];
		var colecao = dados.colecao;
		var objeto = dadosRequisicao.objeto && colecao[dadosRequisicao.objeto.id];
		
		if (objeto) {
			$(document).trigger('plc_datastore_recuperaResposta', {objeto : objeto});
		}
    }
    
    ,inclui: function ( evt, dadosRequisicao ) {
		
		var dados = this.dados[dadosRequisicao.entidade];
		var colecao = dados.colecao;
		var objeto = $.extend({}, dadosRequisicao.objeto, {id: ++dados.id, versao: 0});
		
		colecao[objeto.id] = objeto;
		
		$(document).trigger('plc_datastore_incluiResposta', {objeto : objeto});
    }
    
    ,atualiza: function ( evt, dadosRequisicao ) {
		
		var dados = this.dados[dadosRequisicao.entidade];
		var colecao = dados.colecao;
		var objeto = dadosRequisicao.objeto && colecao[dadosRequisicao.objeto.id];
		
		if (objeto) {
			
			$.extend(objeto, dadosRequisicao.objeto);
			
			$(document).trigger('plc_datastore_atualizaResposta', {objeto : objeto});
		}
    }
    
    ,exclui: function ( evt, dadosRequisicao ) {
		
		var dados = this.dados[dadosRequisicao.entidade];
		var colecao = dados.colecao;
		var objeto = dadosRequisicao.objeto && colecao[dadosRequisicao.objeto.id];
		
		if (objeto) {
			
			delete colecao[objeto.id];
			
			$(document).trigger('plc_datastore_excluiResposta', {objeto : objeto});
		}
    }
}));

$.ui.plc_datastore_local.defaults = $.extend({}, $.ui.plc_datastore.defaults, { 
     dadosIniciais: [ ]
});

})(jQuery);