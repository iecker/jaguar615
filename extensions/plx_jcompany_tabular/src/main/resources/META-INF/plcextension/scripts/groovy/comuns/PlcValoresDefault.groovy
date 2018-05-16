import com.powerlogic.jcompany.geradordinamico.helper.PlcGeradorHelper;


/**
 * 
 * Utilizado para preencher valores defaults nos campos do wizard.
 * 
 * Utilização.:
 * 	Ao definir um campo para uma página, o usuário pode definir um valor padrão para esse campo.
 * 
 * Ex.:
 * 
 *	<campo>
 *		<codigo>campo_teste</codigo>
 *		<rotulo>Campo: </rotulo>
 *		<ajuda>Digite aqui o nome do Campo.</ajuda>
 *		<dominio>STRING</dominio>
 *		<obrigatoriedade>true</obrigatoriedade>
 *		<valor-default>default</valor-default>
 *		<script>groovy/comuns/PlcValoresDefault</script>
 *	</campo>
 * 
 * 
 */


println ("processando campo : " + campo.codigo);

if (campo.codigo == 'projeto'){
	campo.valorDefault = padrao.getNomeProjeto();
} else if (campo.codigo == 'pacotebase'){
	def campoEntidade = PlcGeradorHelper.getInstance().getCampo(padrao,'entidade');
	def entidade = campoEntidade.valor;
	entidade = entidade.substring (0, entidade.indexOf('.entity.'));
	campo.valor = entidade;
}

