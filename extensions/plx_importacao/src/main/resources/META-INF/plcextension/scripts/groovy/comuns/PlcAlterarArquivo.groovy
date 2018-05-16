import com.powerlogic.gerador.GeradorVelocity;
import net.sf.json.*;
import com.powerlogic.jcompany.geradordinamico.helper.PlcGeradorHelper;

/**
 *
 * Altera um arquivo qualquer procurando pelos tokens (coment�rios pelo arquivo)
 * Caso n�o encontre, insere o conte�do no final do arquivo. 
 * 
 * Utiliza��o.: Alterar qualquer arquivo texto, inserindo o conte�do definido em um arquivo velocity.
 * 
 * Utilizado a partir de uma <acao> definida no xml de padr�es...
 * 
 * Ex.:
 *  <acao>
 *		<tipo-acao>groovy/comuns/PlcAlterarArquivo</tipo-acao>
 *		<template-origem>/scripts/velocity/comuns/plc-altera-arquivo.vm</template-origem>
 *		<diretorio-arq-destino>src/main/webapp/WEB-INF/fcls/geralAcoesComplemento.xhtml</diretorio-arq-destino>
 *	</acao>
 * 
 * Onde:
 * <tipo-acao> � o nome do arquivo Groovy sem extens�o
 * <template-origem> � o arquivo velocity que estar� inserindo os dados, ver exemplo em (velocity/comuns/plc-altera-arquivo.vm)
 * <diretorio-arq-destino> arquivo a ser alterado
 * 
 * Obs.:
 * N�o esquecer de definir os Pontos de inser��o.
 * Esses pontos, podem variar, dependendo da maneira que o coment�rio se comporta no arquivo escolhido.
 */
def getPropriedadesGrid(padrao, propriedades) {

	def props = PlcGeradorHelper.getInstance().substituiTokens(padrao, propriedades);

	println props;

	def gridLinhas = JSONArray.toList(JSONArray.fromObject(props));

	def listaPropriedades = [];

	def map = ['listaPropriedades' : listaPropriedades];

	gridLinhas.each { linha ->
		
		def umaProp = [:];
		
		def gridColunas = JSONArray.fromObject(linha);

		def prop = gridColunas.getString(0);
		def gerar = gridColunas.getString (1);
		def tipo = gridColunas.getString (2);
		def titulo = gridColunas.getString (3);
		
		umaProp['tipo']=tipo;
		umaProp['gerar']=gerar;
		umaProp['nome']=prop;
		umaProp['titulo']=titulo;
		
		listaPropriedades.add(umaProp);
	
	}

	return map;
}

def main(){
	String diretorioArqDestino = acao.getDiretorioArqDestino();
	String templateOrigem = acao.getTemplateOrigem();
	String projetoDestino = acao.getProjetoDestino();
	
	diretorioArqDestino = PlcGeradorHelper.getInstance().substituiTokens (padrao, diretorioArqDestino);
	templateOrigem = PlcGeradorHelper.getInstance().substituiTokens(padrao, templateOrigem);
	
	if (!projetoDestino)
		projetoDestino = padrao.getNomeProjeto();
	else
		projetoDestino = PlcGeradorHelper.getInstance().substituiTokens(padrao, projetoDestino);
	
	File file = new File(gerador.pastaGeracao + File.separator + projetoDestino + File.separator + diretorioArqDestino);
	
	
	if (file.exists()){
		StringBuilder conteudo = PlcGeradorHelper.getInstance().leConteudoArquivo(file);
		
		StringWriter writer = new StringWriter();
		
		def valoresMap = PlcGeradorHelper.getInstance().getValoresMap(padrao);
		def map = getPropriedadesGrid(padrao, '${propriedades}');
		
		valoresMap['entidade'] = map;
		
		GeradorVelocity.getInstance().geraTemplate("contexto", valoresMap, writer, templateOrigem, gerador.pastaPadroes);
		writer.flush();
		
		boolean realizouTroca = false;
		if (conteudo.toString().contains("<!--PONTO_INSERCAO-->")){
			realizouTroca = true;
			String aux = conteudo.toString();
			aux = aux.replace("<!--PONTO_INSERCAO-->", writer.toString() + "\n<!--PONTO_INSERCAO-->");
			conteudo = new StringBuilder(aux);
		}
		
		if (conteudo.toString().contains("/*PONTO_INSERCAO*/")){
			realizouTroca = true;
			String aux = conteudo.toString();
			aux = aux.replace("/*PONTO_INSERCAO*/", writer.toString() + "\n/*PONTO_INSERCAO*/");
			conteudo = new StringBuilder(aux);
		}
		
		if (conteudo.toString().contains("#PONTO_INSERCAO")){
			realizouTroca = true;
			String aux = conteudo.toString();
			aux = aux.replace("#PONTO_INSERCAO", writer.toString() + "\n#PONTO_INSERCAO");
			conteudo = new StringBuilder(aux);
		}
		
		if (!realizouTroca){
			conteudo.append("\n").append(writer.toString());
		}
		PlcGeradorHelper.getInstance().gravaConteudoArquivo(file, conteudo);
		
		PlcGeradorHelper.getInstance().registraAlteracaoArtefato(diretorioArqDestino, "");
		
	}
}

// Chama a funcao de altera��o
main();
