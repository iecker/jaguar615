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

$.widget("ui.plc_topo", {

	_init: function() {
	
	    plcTopo = this.element;
	
	    plcTopo.addClass('ui-widget ui-widget-header ui-corner-top ui-jcompany_topo block');
	    
	    //NomeEmpresa
	    plcNomeEmpresa = $('<div/>').attr('id', 'nome-empresa')
	    							.addClass('column span-7 ui-jcompany_topo-empresa')
	    							.appendTo(plcTopo);

	    plcImagemLink =$('<a/>').attr('href', '#')
	    						.appendTo(plcNomeEmpresa);
	    
	    plcImagem = $('<img/>').attr('src', '../../src/images/topo-logo-empresa.png')
	    					   .attr('alt', '')
	    					   .addClass('plc-imagem')
	    					   .appendTo(plcImagemLink);	    

	    //TituloPagina
	    plcTituloPagina = $('<div/>').attr('id', 'titulo-pagina')
	    							 .addClass('ui-state-highlight ui-corner-all ui-jcompany_topo-titulo column span-9')
	    							 .append('Titulo')
	    							 .appendTo(plcTopo);
	    	    
	    //SiglaAplicacao
	    plcSiglaAplicacao = $('<div/>').attr('id', 'sigla-aplicacao')
	    							   .addClass('ui-jcompany_topo-aplicacao column span-7 last')
	    							   .append("SIGLA")
	    							   .appendTo(plcTopo);
	    
	}
	
});

})(jQuery);