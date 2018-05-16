import com.powerlogic.gerador.GeradorVelocity;
import net.sf.json.*;
import com.powerlogic.jcompany.geradordinamico.helper.PlcGeradorHelper;
import org.apache.commons.lang.StringUtils;

/**
 *
 * Cria uma Classe Java com base em um modelo velocity pré definido. 
 *
 * Utilização.: Toda lógica que necessite da criação de Classes Java, como Beans por exemplo.
 *
 * Desenvolvida para criar qualquer tipo de classe java, pode ser utilizada em qualquer projeto, de forma dinâmica conforme o exemplo.
 *
 *
 * Ex.:
 *
 *	<acao>
 *		<tipo-acao>groovy/comuns/PlcCriarClasse</tipo-acao>
 *		<template-origem>/scripts/velocity/extension/tabular/action-evento-especifico-tabular-criacao.vm</template-origem>
 *		<diretorio-arq-destino>src/main/java/${pacotebase}/controller/jsf/${nomeAction}</diretorio-arq-destino>
 *		<projeto-destino>${projeto}_commons</projeto-destino>
 *	</acao>
 *
 * Onde:
 * <tipo-acao> é o nome do arquivo Groovy sem extensão
 * <template-origem>  é o arquivo velocity que servirá como template para criação da classe, ver exemplo em (velocity/extensions/tabular/action-evento-especifico-tabular-criacao.vm)
 * <diretorio-arq-destino> onde a classe será criada
 * 		Podemos notar que o caminho possui dois Tokens, ${pacotebase} e ${nomeAction}.
 * 		Esses Tokens serão substituidos pelos valores informados pelo usuário no decorrer da logica.
 * 		Eles são definidos no template da lógica, como o arquivo tabular.padrao.xml
 * <projeto-destino> Pode ser definido em qual projeto a classe será criada.
 * 
 *
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

// Cria um arquivo
def main() {
	
	String diretorioArqDestino = acao.getDiretorioArqDestino();
	String templateOrigem = acao.getTemplateOrigem();				
	String projetoDestino = acao.getProjetoDestino();
	
	println("Diretorio antes " + diretorioArqDestino);
	diretorioArqDestino = PlcGeradorHelper.getInstance().substituiTokens(padrao, diretorioArqDestino);
	println("Diretorio depois " + diretorioArqDestino);
	
	templateOrigem = PlcGeradorHelper.getInstance().substituiTokens(padrao, templateOrigem);
	
	String nomeEntidade = PlcGeradorHelper.getInstance().substituiTokens(padrao, '${entidade}');
	nomeEntidade = nomeEntidade.substring(nomeEntidade.lastIndexOf(".") + 1,nomeEntidade.length());
	String classe = StringUtils.capitalize(diretorioArqDestino.substring(diretorioArqDestino.lastIndexOf("/") + 1,diretorioArqDestino.length()));
	String path = diretorioArqDestino.substring(0,diretorioArqDestino.lastIndexOf("/"));
	path = path.replaceAll("\\.","/");
	
	String casoUsoC = StringUtils.capitalize(PlcGeradorHelper.getInstance().substituiTokens(padrao, '${casouso}'));

	println("projetoDes" + projetoDestino);
	println("padrao" + padrao.getNomeProjeto());
	
	if (PlcGeradorHelper.getInstance().isProjetoSimples() || !projetoDestino)
		projetoDestino = padrao.getNomeProjeto();
	else
		projetoDestino = PlcGeradorHelper.getInstance().substituiTokens(padrao, projetoDestino);

	println("path " + path);	
	println("classe " + classe);	
		
	File file = new File(gerador.pastaGeracao + File.separator + projetoDestino + File.separator + path + File.separator + classe);
	
	if (!file.exists()){
		if (file.isDirectory())
			file.mkdirs();
		else {
			File diretorio = new File (file.getParent());
			if (!diretorio.exists())
				diretorio.mkdirs();
			file.createNewFile();
		}
	}
	
	FileWriter writer = new FileWriter(file);
	def valoresMap = PlcGeradorHelper.getInstance().getValoresMap(padrao);
	
	def map = getPropriedadesGrid(padrao, '${propriedades}');
	valoresMap['propsentidade'] = map;
	valoresMap['nomeEntidade'] = nomeEntidade;
	valoresMap['casoUsoC'] = casoUsoC;
	
	GeradorVelocity.getInstance().geraTemplate("contexto", valoresMap, writer, templateOrigem, gerador.pastaPadroes);
	writer.flush();
	
	PlcGeradorHelper.getInstance().registraLogCriacaoArtefato(templateOrigem, path + File.separator + classe, "");
	
	println("Criado com sucesso! ${file}");
}

// Chama a funcao que cria
main();
