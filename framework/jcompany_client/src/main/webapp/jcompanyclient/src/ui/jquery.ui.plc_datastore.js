/*
 * jQuery UI jCompany Framework- O Plugin jQuery UI Para Construir Aplicações jCompany
 * Copyright (c) Powerlogic
 * 
 * Um componente usado para recuperacao e armazenamento de dados de maneira desacoplada de
 * um widget "visual". Inspirado no (futuro?) Data Library do jQueryUI:
 *    http://wiki.jqueryui.com/Data-Library
 * 
 * Outros frameworks, como Dojo, oferecem este servico:
 *    http://unclescript.blogspot.com/2009/01/what-makes-dojo-diferent.html
 * 
 * Tres nomes podem ser usados para este padrão: DataStore, DataService ou DataManager? O critério 
 * foi a quantidade de resultados ao pesquisar no Google (em 30/09/2009): ;-)
 *  
 * 512.000 para "datastore" pattern
 * 128.000 para "data store" pattern
 *
 * 264.000 para "dataservice" pattern
 * 237.000 para "data service" pattern
 *
 * 4.870 para "datamanager" pattern
 * 55.800 para "data manager" pattern
 * 
 * 
 * Autores:  
 *    Savio Grossi - savio.grossi@powerlogic.com.br
 *    Adolfo Santos - adolfo.santos@powerlogic.com.br
 *    
 * Depends:
 *	ui.core.js  
 */
(function($) {

$.widget("ui.plc_datastore", {
	
	_init: function() {
		
		var self = this;
		// Cria o hash para as entidades!
		self.dados = {};
		// Cria um store para cada entidade Gerenciada.
		$.each(this.options.entidadesGerenciadas, function(i, entidade) {
			self.dados[entidade] = {};
		});
		
		// Cria o delegate para a função que trata os eventos!
		this._trataEventos = this._createDelegate(this._trataEventos);
		
		// Escuta e delega eventos definidos para um datastore
		$(document).bind('plc_datastore_recuperaColecao', this._trataEventos);
		$(document).bind('plc_datastore_recupera', this._trataEventos);
		$(document).bind('plc_datastore_inclui', this._trataEventos);
		$(document).bind('plc_datastore_atualiza', this._trataEventos);
		$(document).bind('plc_datastore_exclui', this._trataEventos);
	}
    
	,destroy: function(){
		delete this.dados;
		// Descarta os eventos.
		$(document).unbind('plc_datastore_recuperaColecao', this._trataEventos);
		$(document).unbind('plc_datastore_recupera', this._trataEventos);
		$(document).unbind('plc_datastore_inclui', this._trataEventos);
		$(document).unbind('plc_datastore_atualiza', this._trataEventos);
		$(document).unbind('plc_datastore_exclui', this._trataEventos);
	}
	
	,_createDelegate: function(fn){
		var self = this;
		return function(){
			return fn.apply(self, arguments);
		};
	}
	
    // Listeners para eventos de requisicao ao DataStore
    ,_trataEventos: function(evt, dadosRequisicao) {
		// Verifica se a entidade é gerenciada pela intância desse DataStorevt
		if (this.dados.hasOwnProperty(dadosRequisicao.entidade)) {
			
			console.log('DataStore Event: ' + evt.type, arguments);
			
			// Dispara o evento correspondentevt
			if (evt.type == 'plc_datastore_recuperaColecao') {
				this.recuperaColecao(evt, dadosRequisicao);
			} else if (evt.type == 'plc_datastore_recupera') {
				this.recupera(evt, dadosRequisicao);
			} else if (evt.type == 'plc_datastore_inclui') {
				this.inclui(evt, dadosRequisicao);
			} else if (evt.type == 'plc_datastore_atualiza') {
				this.atualiza(evt, dadosRequisicao);
			} else if (evt.type == 'plc_datastore_exclui') {
				this.exclui(evt, dadosRequisicao);
			}
		}
    }
	
    // Template Methods para implementadores de DataStore
    
	,recuperaColecao: function ( evt, dadosRequisicao ) {
    }
	
    ,recupera: function ( evt, dadosRequisicao ) {
    }
    
    ,inclui: function ( evt, dadosRequisicao ) {
    }
    
    ,atualiza: function ( evt, dadosRequisicao ) {
    }
    
    ,exclui: function ( evt, dadosRequisicao ) {
    }
});

$.ui.plc_datastore.defaults = {
        entidadesGerenciadas: [ ]
};

})(jQuery);