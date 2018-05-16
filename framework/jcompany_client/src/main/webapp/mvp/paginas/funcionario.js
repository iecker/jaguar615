
$('#funcionario').tabs();

var usuarioEmEdicao;

plc.Bus.escutar('plc.acao.usuarioman.editar', function(t, usuario){

	usuarioEmEdicao = usuario;

	$('input,select,textarea', $('#usuarioMan')).each(function(){
		if (usuarioEmEdicao[this.name]) {
			$(this).val(usuarioEmEdicao[this.name]);
		} else {
			$(this).val('');
		}
	});
});

plc.Bus.escutar('plc.acao.usuarioman.excluir', function(t){
	plc.Bus.evento('plc.dados.usuariosel.excluir', usuarioEmEdicao);
});

plc.Bus.escutar('plc.acao.usuarioman.novo', function(t, usuario){

	usuarioEmEdicao = {};

	$('input,select,textarea', $('#usuarioMan')).each(function(){
		$(this).val('');
	});
});

plc.Bus.escutar('plc.acao.usuarioman.gravar', function(t, usuario){

	$('input,select,textarea', $('#usuarioMan')).each(function(){
		usuarioEmEdicao[this.name] = $(this).val();
	});

	plc.Bus.evento('plc.dados.usuariosel.gravar', usuarioEmEdicao);
});

plc.Bus.escutar('plc.dados.usuariosel.gravar.sucesso', function(t, usuario){
	plc.Bus.evento('plc.logica.usuariosel', function(){
		plc.Bus.evento('plc.acao.usuariosel.pesquisar');
	});
});

plc.Bus.escutar('plc.dados.usuariosel.excluir.sucesso', function(t, usuario){
	plc.Bus.evento('plc.logica.usuariosel', function(){
		plc.Bus.evento('plc.acao.usuariosel.pesquisar');
	});
});
