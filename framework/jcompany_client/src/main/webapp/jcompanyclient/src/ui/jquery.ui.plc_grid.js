/*
 * jQuery UI jCompany Framework- O Plugin jQuery UI Para Construir Aplicações jCompany
 * Copyright (c) Powerlogic
 * 
 * Autores:  
 *    Savio Grossi - savio.grossi@powerlogic.com.br
 *    
 * Depends:
 *	ui.core.js  
 */
(function($) {

$.widget("ui.plc_grid", {

    _createDelegate: function(fn){
		var self = this;
		return function(){
			return fn.apply(self, arguments);
		};
	},
	
	_init: function() {
		
	    var gridSelf = this, o = this.options;
		
		this._onRecuperaColecaoResposta = this._createDelegate(this._onRecuperaColecaoResposta);
		this._onIncluiResposta = this._createDelegate(this._onIncluiResposta);
		this._onAtualizaResposta = this._createDelegate(this._onAtualizaResposta);
		this._onExcluiResposta = this._createDelegate(this._onExcluiResposta);
		
        //registra eventos
        $(document).bind('plc_datastore_recuperaColecaoResposta', this._onRecuperaColecaoResposta);
        $(document).bind('plc_datastore_incluiResposta', this._onIncluiResposta);
        $(document).bind('plc_datastore_atualizaResposta', this._onAtualizaResposta);
        $(document).bind('plc_datastore_excluiResposta', this._onExcluiResposta);
        
	    var gridContainer = this.element;
	    var gridTable = $('<table/>').attr('id', 'tabgrid')
	    					     .addClass('scroll')
	    					     .attr('cellpadding', '0')
	    					     .attr('cellspacing', '0')
	    					     .appendTo(gridContainer);
	    
	    var gridPager = $('<div/>').attr('id', 'tabpager')
	                           .appendTo(gridContainer);
	    
	    var self = this.element;
		
	    var colNames = $(document).plc_datamodel('getLabels', o.entidade);
	    var colModel = $(document).plc_datamodel('getMetamodel', o.entidade);
	    
		colNames = [''].concat(colNames);
		colModel = [{name:'acao_excluir', index:'acao_excluir', width: 4, sortable:false}].concat(colModel);
		
		// Funcoes de edicao.
		var onEdit = function(rowid){
			
			gridSelf._currentEditing = rowid;
		};
		
		var onEditAfterSave = function(rowid){
			
			if (rowid == gridSelf._currentEditing) {
				delete gridSelf._currentEditing;
			}
			
			var objeto = $.extend({}, gridSelf.dados[rowid], $('#tabgrid').getRowData(rowid));
			
			delete objeto.acao_excluir;
			
			$(document).trigger('plc_datastore_atualiza', {entidade: gridSelf.options.entidade, objeto: objeto});
		};
		
		var onSelectRow = function(rowid){
			if (rowid && rowid !== gridSelf._currentEditing){
				if (gridSelf._currentEditing) {
					$('#tabgrid').saveRow(gridSelf._currentEditing, null, 'clientArray', null, onEditAfterSave);
				}
			}
		};
		
		var onDblClickRow = function(rowid){
			if (rowid && rowid !== gridSelf._currentEditing){
				if (gridSelf._currentEditing) {
					$('#tabgrid').saveRow(gridSelf._currentEditing, null, 'clientArray', null, onEditAfterSave);
				}
				$('#tabgrid').editRow(rowid, true, onEdit, null, 'clientArray', null, onEditAfterSave);
			}
		};
		
		var onAdd = function(){
			
			$("#tabgrid").addRowData('', {});
			
			$('#tabgrid').editRow('', true, null, null, 'clientArray', null, function(){
				
				var objeto = $("#tabgrid").getRowData('');
				
				delete objeto.acao_excluir;
				
				objeto.id = 0;
				objeto.versao = 0;
				
				$("#tabgrid").delRowData('', true);
				
				$(document).trigger('plc_datastore_inclui', {entidade: gridSelf.options.entidade, objeto: objeto});
				
			}, null, function(){
				
				$("#tabgrid").delRowData('', true);
			});
		};
		
		$("#tabgrid").jqGrid({
			
			datatype: "clientSide"
			//,height: 250
			,colNames: colNames
			,colModel: colModel
			//,imgpath: gridimgpath
			,autowidth: true
			//,multiselect: true
			//,multiboxonly: true
			,pager : '#tabpager'
			,caption: "Logica Tabular"
			
			,onSelectRow: onSelectRow
			
			,ondblClickRow: onDblClickRow
			
	    }).navGrid('#tabpager',{
			edit:false
			,add:true
			,del:false
			,addfunc: onAdd
		});
		
		var delRowData = jQuery.prototype.delRowData;
		
		jQuery.prototype.delRowData = function(rowid, localDelete){
			if (localDelete === true) {
				if (delRowData.call(this, rowid)) {
					if (gridSelf.dados) {
						delete gridSelf.dados[rowid];
					}
				}
			} else {
				
				var objeto = gridSelf.dados[rowid];
				
				$(document).trigger('plc_datastore_exclui', {entidade: gridSelf.options.entidade, objeto: objeto});
			}
		};
		
	    // solicita colecao inicial de entidades para o grid via datastore
	    $(document).trigger('plc_datastore_recuperaColecao', { entidade: o.entidade });
	},
	
    destroy: function(){ 
		$(document).unbind('plc_datastore_recuperaColecaoResposta', this._onRecuperaColecaoResposta);
        $(document).unbind('plc_datastore_incluiResposta', this._onIncluiResposta);
		$(document).unbind('plc_datastore_atualizaResposta', this._onRecuperaColecaoResposta);
		$(document).unbind('plc_datastore_excluiResposta', this._onExcluiResposta);
    },
	
	_onRecuperaColecaoResposta: function(evt, dados) {
		this._populaGrid(dados.colecao);
	},
	
	_onAtualizaResposta: function(evt, dados) {
		this._adicionaObjeto(dados.objeto);
	},
	
	_onIncluiResposta: function(evt, dados) {
		this._adicionaObjeto(dados.objeto);
	},
	
	_onExcluiResposta: function(evt, dados) {
		
		//console.log('_onExcluiResposta', dados);
		
		var objeto = dados.objeto;
		
		if (this.dados.hasOwnProperty(objeto.id)) {
			//console.log('_onExcluiResposta:', objeto.id, objeto);
			$("#tabgrid").delRowData(objeto.id, true);
			delete this.dados[objeto.id];
		}
	},
	
	_populaGrid: function(colecao){
		var self = this;
		//console.log('colecao: ', colecao);
		$.each(colecao, function(i, dado){
			if (dado) {
				self._adicionaObjeto(dado);
			}
		});
	},
	
	_adicionaObjeto: function(objeto){
		if (!this.dados) {
			this.dados = {};
		}
		if (!this.dados.hasOwnProperty(objeto.id)) {
			
			$("#tabgrid").addRowData(objeto.id, objeto);
			
			var acao_excluir = '<button class="ui-state-default ui-corner-all"  onclick="if(confirm(\'Excluir Registro?\'))jQuery(\'#tabgrid\').delRowData(\''+ objeto.id +'\');">';
			acao_excluir += '<span class="ui-icon ui-icon-trash"></span>';
			acao_excluir += '</button>';
			
			jQuery("#tabgrid").setRowData(objeto.id,{acao_excluir: acao_excluir}); 
			
			//console.log('_adicionaObjeto:', objeto.id, objeto);
		} else {
			$("#tabgrid").setRowData(objeto.id, objeto);
		}
		this.dados[objeto.id] = objeto;
	}
});

$.ui.plc_grid.defaults = {
       entidade: ''
};


})(jQuery);