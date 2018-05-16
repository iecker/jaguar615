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

$.widget("ui.plc_rodape", {

	_init: function() {
	
	    plcRodape = this.element;
	
	    plcRodape.addClass('ui-widget ui-widget-header ui-corner-bottom plc-rodape block');
		
	    //divRodape = $('<div/>').addClass('span-24')
		//  					   .append('Rodapé')
		//  					   .appendTo(plcRodape);
	    
	}
	
});

})(jQuery);