import com.powerlogic.gerador.GeradorVelocity;
import net.sf.json.*;
import com.powerlogic.jcompany.geradordinamico.helper.PlcGeradorHelper;

/**
 *
 * Cria uma Classe Packge-Info com base em um modelo velocity pr� definido.
 *
 * Utiliza��o.: Toda l�gica que necessite da cria��o de Packge-Info.
 *
 * Desenvolvida para auxiiar na cria��o dos Packge-Info, pode ser utilizada em qualquer projeto, de forma din�mica conforme o exemplo.
 *
 *
 * Ex.:
 *
 *  <acao>
 *	<tipo-acao>groovy/comuns/PlcCriarPackageInfo</tipo-acao>
 *	<template-origem>/scripts/velocity/extension/tabular/package-info-tabular-grupoagregacao-alteracao.vm</template-origem>
 *	<diretorio-arq-destino>/src/main/config/com/powerlogic/jcompany/config/domain/app/${casouso}/package-info.java</diretorio-arq-destino>
 *	<projeto-destino>${projeto}_commons</projeto-destino>
 *  </acao>
 *
 * Onde:
 * <tipo-acao> � o nome do arquivo Groovy sem extens�o
 * <template-origem>  � o arquivo velocity que servir� como template para cria��o do Packge-Info, ver exemplo em (velocity/extensions/tabular/package-info-tabular-grupoagregacao-alteracao.vm)
 * <diretorio-arq-destino> onde a classe ser� criada
 * 		Podemos notar que o caminho possui o Token ${casouso}.
 * 		Esses Token ser� substituido pelo valor informado pelo usu�rio no decorrer da logica.
 * 		Esses valores podem ser definidos no template da l�gica, como o arquivo tabular.padrao.xml
 * <projeto-destino> Pode ser definido em qual projeto a classe ser� criada.
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
	
	diretorioArqDestino = PlcGeradorHelper.getInstance().substituiTokens(padrao, diretorioArqDestino);
	templateOrigem = PlcGeradorHelper.getInstance().substituiTokens(padrao, templateOrigem);
	
	if (!projetoDestino)
		projetoDestino = padrao.getNomeProjeto();
	else
		projetoDestino = PlcGeradorHelper.getInstance().substituiTokens(padrao, projetoDestino);
	
	File file = new File(gerador.pastaGeracao + File.separator + projetoDestino + File.separator + diretorioArqDestino);
	
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
	
	GeradorVelocity.getInstance().geraTemplate("contexto", valoresMap, writer, templateOrigem, gerador.pastaPadroes);
	writer.flush();
	
	println("Criado com sucesso! ${file}");
}

// Chama a funcao que cria
main();
