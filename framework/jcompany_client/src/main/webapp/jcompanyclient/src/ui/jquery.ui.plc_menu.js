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

$.widget("ui.plc_menu", {

	_init: function() {
	
	    plcMenu = this.element;
	
	    plcMenu.addClass('ui-widget ui-widget-header ui-jcompany_menu block');
	    
	    
	    menuLogicasDiv = $('<div/>').attr('id', 'menu-1')
		 								 .addClass('column')
		 								 .appendTo(plcMenu);

	    menuLogicas = $('<a/>').attr('id', 'menu-area-ti')
	    						   .addClass('fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all')
	    						   .append('Casos de Uso')
	    						   .appendTo(menuLogicasDiv);

	    menuLogicasUl1 = $('<ul/>').addClass('fg-hidden')
	    							   .appendTo(menuLogicasDiv);

	    menuLogicasUl1Li1 = $('<li/>').appendTo(menuLogicasUl1);

	    menuLogicasUl1Li1Link = $('<a/>').attr('href', '#logica01')
	    									 .append('Lógica 1')
	    									 .appendTo(menuLogicasUl1Li1);
	    
	    //this._trataLinkHash(menuLogicasUl1Li1Link);
	    

	    menuLogicasUl1Li2 = $('<li/>').appendTo(menuLogicasUl1);

	    menuLogicasUl1Li2Link = $('<a/>').attr('href', '#logica02')
	    									 .append('Lógica 2')
	    									 .appendTo(menuLogicasUl1Li2);	
	    
	    menuLogicasUl1Li3 = $('<li/>').appendTo(menuLogicasUl1);

	    menuLogicasUl1Li3Link = $('<a/>').attr('href', '#logica03')
	    									 .append('Lógica 3')
	    									 .appendTo(menuLogicasUl1Li3);	
		
	    //this._trataLinkHash(menuLogicasUl1Li2Link);
	    	    
	    
	    menuPreferenciaDiv = $('<div/>').attr('id', 'menu-2')
	    								.addClass('column')
	                                    .appendTo(plcMenu);
	    
	    menuPreferencia = $('<a/>').attr('id', 'menu-preferencia')
	    						   .addClass('fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all')
	                               .append('Preferencias')
	                               .appendTo(menuPreferenciaDiv);
	    	    
	    menuPreferenciaUl1 = $('<ul/>').addClass('fg-hidden')
	    							   .appendTo(menuPreferenciaDiv);
	    
	    menuPreferenciaUl1Li1 = $('<li/>').appendTo(menuPreferenciaUl1);
	    
	    menuPreferenciaUl1Li1Link = $('<a/>').attr('href', 'http://www.uol.com.br')
	     									 .append('Definir Layout')
	     									 .appendTo(menuPreferenciaUl1Li1);
	    	    
	    menuPreferenciaUl1Li2 = $('<li/>').appendTo(menuPreferenciaUl1);
	    
	    menuPreferenciaUl1Li2Link = $('<a/>').attr('href', "javascript:(function(){if%20(!/Firefox[\/\s](\d+\.\d+)/.test(navigator.userAgent)){alert('Sorry,%20due%20to%20security%20restrictions,%20this%20tool%20only%20works%20in%20Firefox');%20return%20false;%20};%20if(window.jquitr){%20jquitr.addThemeRoller();%20}%20else{%20jquitr%20=%20{};%20jquitr.s%20=%20document.createElement('script');%20jquitr.s.src%20=%20'http://jqueryui.com/themeroller/developertool/developertool.js.php';%20document.getElementsByTagName('head')[0].appendChild(jquitr.s);}%20})();")
	     									.append('Definir Tema')
	     									.appendTo(menuPreferenciaUl1Li2);
	  
	    
	    
	    menuAreaTecnicaDiv2 = $('<div/>').attr('id', 'menu-3')
	    								 .addClass('column')
	                                     .appendTo(plcMenu);
	    
	    menuAreaTecnica = $('<a/>').attr('id', 'menu-area-ti')
	    						   .addClass('fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all')
	    						   .append('Área de TI')
	    						   .appendTo(menuAreaTecnicaDiv2);

		menuAreaTecnicaUl1 = $('<ul/>').addClass('fg-hidden')
			   						   .appendTo(menuAreaTecnicaDiv2);

		menuAreaTecnicaUl1Li1 = $('<li/>').appendTo(menuAreaTecnicaUl1);

		menuAreaTecnicaUl1Li1Link = $('<a/>').attr('href', '#')
						 					 .append('Gerar Esquema')
						 					 .appendTo(menuAreaTecnicaUl1Li1);

		menuAreaTecnicaUl1Li2 = $('<li/>').appendTo(menuAreaTecnicaUl1);

		menuAreaTecnicaUl1Li2Link = $('<a/>').attr('href', '#')
											 .append('Outro Item')
											 .appendTo(menuAreaTecnicaUl1Li2);
	    

			
		menuLogicas.menu({ 
			content: '<ul>' + menuLogicasUl1.html() + '</ul>',
			flyOut: true
		});
		
		
	    menuPreferencia.menu({ 
			content: '<ul>' + menuPreferenciaUl1.html() + '</ul>',
			flyOut: true
		});
		
		menuAreaTecnica.menu({ 
			content: '<ul>' + menuAreaTecnicaUl1.html() + '</ul>',
			flyOut: true
		});
		
		menuThemeSitcherDiv = $('<div/>').attr('id', 'menuThemeSitcherDiv')
		 .addClass('column')
        .appendTo(plcMenu);
        //.themeswitcher();
	    
	},

    _trataLinkHash: function(el) {
    	el.click(function(){
		   var hash = this.href;
		   hash = hash.replace(/^.*#/, '');
		   // moves to a new page. 
		   // pageload is called at once. 
		   // hash don't contain "#", "?"
		   $.historyLoad(hash);
		   return false;
    	});
	}

	
});

})(jQuery);