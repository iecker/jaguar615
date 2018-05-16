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

$.widget("ui.plc_app", {

	_init: function() {
	
	    plcApp = this.element;
	
		plcTopo = $('#plc-topo');
		if (plcTopo.length == 0) { 	
			plcTopo = $('<div/>').attr('id', 'plc-topo'); 
		}
		plcTopo.plc_topo()
		       .appendTo(plcApp);

		
		plcMenu = $('#plc-menu');
		if (plcMenu.length == 0) {
			plcMenu = $('<div/>').attr('id', 'plc-menu');
		}
		plcMenu.plc_menu()
		       .appendTo(plcApp);

		plcCorpo = $('#plc-corpo');
		if (plcCorpo.length == 0) {
			plcCorpo = $('<div/>').attr('id', 'plc-corpo');
		}
		plcCorpo.plc_corpo()
		        .appendTo(plcApp);
		
		plcRodape = $('#plc-rodape');
		if (plcRodape.length == 0) {
			plcRodape = $('<div/>').attr('id', 'plc-rodape')
			                       .append('Rodapé');
		}
		plcRodape.plc_rodape()
		         .appendTo(plcApp);
		
	}
	
});

})(jQuery);