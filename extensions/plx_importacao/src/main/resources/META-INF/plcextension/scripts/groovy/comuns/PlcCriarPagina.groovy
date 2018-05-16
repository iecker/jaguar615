import com.powerlogic.jcompany.geradordinamico.helper.*;
import net.sf.json.*;
import com.powerlogic.jcompany.ide.geradordinamico.*;
import com.powerlogic.gerador.*;


/**
 *
 * Permite a cria��o de paginas (HTML, JSP, XHTML) em um projeto. 
 *
 * Utiliza��o.: Toda l�gica que utilize paginas para apresenta��o na camada de vis�o.
 *
 *
 * Ap�s a inser��o, tenta ajustar a identa��o do XML passado.
 * Se n�o conseguir, volta como estava, pois n�o � uma tarefa obrigat�ria.
 *
 * Ex.:
 *
 *	<acao>
 *		<tipo-acao>groovy/comuns/PlcCriarPagina</tipo-acao>
 *		<template-origem>/scripts/velocity/extension/tabular/corpo-xhtml-tabular-alteracao.vm</template-origem>
 *		<diretorio-arq-destino>/src/main/webapp/WEB-INF/fcls/${subdiretorio}/${casouso}Tab.xhtml</diretorio-arq-destino>
 *	</acao>
 *
 * Onde:
 * <tipo-acao> � o nome do arquivo Groovy sem extens�o
 * <template-origem>  � o arquivo velocity que servir� como template para cria��o das p�ginas, ver exemplo em (velocity/extensions/tabular/corpo-xhtml-tabular-alteracao.vm) 
 * <diretorio-arq-destino> onde a P�gina ser� criada
 * 		Podemos notar que o caminho possui o Token ${casouso}.
 * 		Esses Token ser� substituido pelo valor informado pelo usu�rio no decorrer da logica.
 * 		Esses valores podem ser definidos no template da l�gica, como o arquivo tabular.padrao.xml
 *
 * Podemos observar que essa <acao> n�o defini um <template-origem> pois altera��o do arquivo � feita em sua pr�pria l�gica.
 *
 */


def ajustaIdentacaoXML(def xml) {
	try {
		String[] tokens = xml.split("\\s*\\>\\s*\\<\\s*");
		StringBuilder sb = new StringBuilder();
		int ident = 0;
		boolean pri = true;
		for (String string : tokens) {
			if (string.charAt(0)=='/') {
				ident--;
			}
			if (!pri) {
				sb.append('>');
				sb.append(System.getProperty("line.separator"));
				for(int i=0; i<ident; i++) {
					sb.append('\t');
				}
				sb.append('<');
			}
			pri=false;
			sb.append(string);

			if (string.charAt(0)!='/'
					&& string.charAt(0)!='!'
					&& !string.startsWith("<!")
					&& !string.startsWith("<%")
					&& string.charAt(string.length()-1)!='/'
					&& !string.contains("</")
					&& !string.contains("/>")) {
				ident++;
			}
		}
		return sb.toString();
	} catch (Exception e) {
		//Como � uma tentativa, se houver algum erro, retorna a string original.
		e.printStackTrace();
		return xml;
	}
}

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

def diretorioArqDestino = acao.getDiretorioArqDestino();
def templateOrigem = acao.getTemplateOrigem();				

diretorioArqDestino = PlcGeradorHelper.getInstance().substituiTokens(padrao, diretorioArqDestino);
templateOrigem = PlcGeradorHelper.getInstance().substituiTokens(padrao, templateOrigem);

println "diretorioArqDestino -> ${diretorioArqDestino}";
println "templateOrigem -> ${templateOrigem}";

def projetoDestino = padrao.getNomeProjeto();
	
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

def valoresMap = PlcGeradorHelper.getInstance().getValoresMap(padrao);
def map = getPropriedadesGrid(padrao, '${propriedades}');
valoresMap['entidade'] = map;

//println "Nome caso de uso -> ${valoresMap.casouso}";

StringWriter swriter = new StringWriter ();
GeradorVelocity.getInstance().geraTemplate("contexto", valoresMap, swriter, templateOrigem, gerador.pastaPadroes);
swriter.flush();
swriter.close();

FileWriter fwriter = new FileWriter(file);
fwriter.write (ajustaIdentacaoXML(swriter.toString()));
fwriter.flush();
fwriter.close();

PlcGeradorHelper.getInstance().registraLogCriacaoArtefato(templateOrigem, diretorioArqDestino, "");

