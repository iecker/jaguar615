plc.Bus.escutar('plc.acao.usuariosel.pesquisar', function(){
	// Pede pela pesquisa.
	plc.Bus.evento('plc.dados.usuariosel.pesquisar', {});
});


// Pega o template de resultado. 
var tpl = $('table tr.resultado-pesquisa-template', $('#usuarioSel')).html();

plc.Bus.escutar('plc.dados.usuariosel.pesquisar.sucesso', function(evento, lista){
	// limpa as linhas existentes.
	$('table tbody', $('#usuarioSel')).find('tr.linhapar, tr.linhaimpar').remove();
	// Monta cada linha da resposta.
	$.each(lista, function(i, item){
		// Monta o Template.
		var t = tpl.replace('#i', i + 1);
		for (var p in item) {
			t = t.replace('#'+p, item[p]);
		}

		var $tr = $('<tr class="ui-widget-default">'+ t +'</tr>');
		
		$tr.addClass(i % 2 == 0 ? 'linhapar': 'linhaimpar')
		$tr.click(function(){// Dispara o evento de edição para a linha.
			plc.Bus.evento('plc.logica.paginas.usuario', function(){
				plc.Bus.evento('plc.acao.usuarioman.editar', item);
			});
		});
		// Adiciona a linha.
		$('table tbody', $('#usuarioSel')).append($tr);
	});
});