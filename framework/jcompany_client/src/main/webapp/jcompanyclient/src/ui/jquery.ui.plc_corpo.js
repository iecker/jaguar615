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

$.widget("ui.plc_corpo", {

	divCorpo: {},
	
	_init: function() {
	
	    plcCorpo = this.element;
	
	    plcCorpo.addClass('ui-widget ui-widget-content block');
	    
	    divCorpo = $('<div/>').attr('id', 'corpo')
	    					  .addClass('span-24')
	    					  .append('Corpo')
	    					  .appendTo(plcCorpo);
	    
	    $.historyInit(this._trataHashHistorico, "");
	    		
	},

    // PageLoad function
    // This function is called when:
    // 1. after calling $.historyInit();
	// 2. after calling $.historyLoad();
	// 3. after pushing "Go Back" button of a browser
	_trataHashHistorico: function (hash) {
		// alert("pageload: " + hash);
		// hash doesn't contain the first # character.
		if(hash) {
			// restore ajax loaded state
			if($.browser.msie) {
				// jquery's $.load() function does't work when hash include special characters like åäö.
				hash = encodeURIComponent(hash);
			}
			divCorpo.load(hash + "/"+hash+".html");
		} else {
			// start page
			divCorpo.empty();
		}
	}

	
});

})(jQuery);