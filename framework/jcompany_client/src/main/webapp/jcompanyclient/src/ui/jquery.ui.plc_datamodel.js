/*
 * jQuery UI jCompany Framework- O Plugin jQuery UI Para Construir Aplicações jCompany
 * Copyright (c) Powerlogic
 * 
 * Autores:  
 *    Savio Grossi - savio.grossi@powerlogic.com.br
 *    
 *    
 * Depends:
 *	ui.core.js  
 */
(function($) {

$.widget("ui.plc_datamodel", {
	
	_init: function(){
    }
    
    ,destroy: function(){ 
    }
	
    ,getLabels: function (entidade) {
    	var self = this;
    	var labels = [ ];
    	jQuery.each(self.options.entidades, function(i, e) {
    		if (e.alias === entidade) {
    			labels = e.labels;
    			return false;
    		}
    	});
    	
    	return labels;
    }
    
    ,getMetamodel: function (entidade) {
    	var self = this;
    	var metamodel = [ ];
    	jQuery.each(self.options.entidades, function(i, e) {
    		if (e.alias === entidade) {
    			metamodel = e.metamodel;
    			return false;
    		}    		
    	});
    	return metamodel;
    }
	
	,getEntidade: function(alias) {
		var entidade = '';
    	$.each(this.options.entidades, function(i, e) {
    		if (e.alias === alias) {
    			entidade = e.entidade;
    			return false;
    		}
    	});
    	return entidade;
    }
});

$.ui.plc_datamodel.getter = "getLabels getEntidade getMetamodel";

$.ui.plc_datamodel.defaults = {
        entidades: [ ]
};

})(jQuery);