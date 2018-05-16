
var varLocal = 'TESTE NAO VISUALIZAVEL';

setInterval(function(){
	alert(varLocal);
}, 3000);

var ultimoResultado = null;

plc.Bus.escutar('plc.acao.categoriatab.novo', function(t){
	ultimoResultado = ultimoResultado || [];
	ultimoResultado.push({idNatural:{sigla:''},nome:''});
	var html = $('#categoriatab-template tbody').html();
	var i = $('#categoria-tbody > tr').length;
	html = html.replace(/#\{i\}/g, i + 1);
	html = html.replace(/#\{idNatural_sigla\}/g, '');
	html = html.replace(/#\{nome\}/g, '');
	$(html).appendTo('#categoria-tbody');
});

plc.Bus.escutar('plc.acao.categoriatab.pesquisar', function(t){
	plc.Bus.evento('plc.dados.categoriatab.pesquisar', {});
});

plc.Bus.escutar('plc.dados.categoriatab.pesquisar.sucesso', function(t, resultado){
	
	ultimoResultado = resultado;
	
	$('#categoria-tbody tr').remove();
	
	$.each(resultado, function(i){
		var html = $('#categoriatab-template tbody').html();
		html = html.replace(/#\{i\}/g, i + 1);
		html = html.replace(/#\{idNatural_sigla\}/g, this.idNatural.sigla);
		html = html.replace(/#\{nome\}/g, this.nome);
		$(html).appendTo('#categoria-tbody');
	});
});

plc.Bus.escutar('plc.acao.categoriatab.gravar', function(t, pesquisa){
	$('#categoria-tbody > tr').each(function(i, tr){
		var sigla = $('#'+ (i+1) +'idNatural_sigla').val();
		var nome = $('#'+ (i+1) +'nome').val();
		ultimoResultado[i].idNatural.sigla = sigla;
		ultimoResultado[i].nome = nome;
	});
});

plc.Bus.escutar('plc.acao.categoriatab.gravar.sucesso', function(t, pesquisa){
	plc.Bus.evento('plc.dados.categoriatab.pesquisar', {});
});
