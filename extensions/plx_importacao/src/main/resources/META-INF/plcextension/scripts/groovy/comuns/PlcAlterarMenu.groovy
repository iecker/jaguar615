import org.dom4j.io.SAXReader 
import com.powerlogic.jcompany.geradordinamico.helper.PlcGeradorHelper;
import java.util.regex.*;
import org.dom4j.*;
import org.dom4j.tree.*;
import org.xml.sax.SAXException;


/**
 * 
 * Altera o arquivo de menu do jCompany, adicionando os links para acessar as l�gicas.
 * 
 * Utiliza��o.: Toda l�gica que acesse o menu.
 * 
 * Essa Classe e um pouco mais elaborada, uma vez que foi desenvolvida especificamente para alterar o menu, n�o sendo poss�vel sua utiliza��o
 * em outras situa��es.
 * 
 * Ap�s a inser��o, tenta ajustar a identa��o do XML passado.
 * Se n�o conseguir, volta como estava, pois n�o � uma tarefa obrigat�ria.
 * 
 * Ex.:
 * 
 *	<acao>
 *		<tipo-acao>groovy/comuns/PlcAlterarMenu</tipo-acao>
 *		<!-- <template-origem>/scripts/velocity/comuns/plc-item-menu.vm</template-origem> -->
 *		<diretorio-arq-destino>/src/main/webapp/WEB-INF/fcls/geralMenu.xhtml</diretorio-arq-destino>
 *	</acao>
 *
 * Onde:
 * <tipo-acao> � o nome do arquivo Groovy sem extens�o
 * <diretorio-arq-destino>  arquivo do menu a ser alterado
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

def getSAXReader() {
	def xmlReader = new SAXReader();
	xmlReader.setValidation(false);
	
	try {
		xmlReader.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
		xmlReader.setFeature("http://xml.org/sax/features/namespaces", false);
		xmlReader.setFeature("http://xml.org/sax/features/validation", false);
		xmlReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
	} catch (SAXException e1) {
		e1.printStackTrace();
	}
	return xmlReader;
}
	
def determinaEncodingXML(Reader reader) {
	
	final Pattern pattern = Pattern.compile("<\\?xml[^>\\?]+encoding[^>\\?]*=[^>\\?]*(\"|\')(([\\w\\-])+)(\"|\')[^>\\?]*\\?>");
	Matcher m;
	def sb;
	def br;
	
	try {
		br = new BufferedReader(reader);
		String linha = br.readLine();
		if (null==linha) {
			return null;
		}
		for (m=pattern.matcher(linha); linha!=null && !m.find(); m=pattern.matcher(sb.toString())) {
			if (sb==null)
				sb = new StringBuilder(linha);
			
			linha=br.readLine();
			if (linha!=null) {
				sb.append('\n');
				sb.append(linha);
			}
		}
		
		if (linha == null) {
			return null;
		}
		
		return m.group(2);
		
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br!=null) {
			try {
				br.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	return null;
}

def getDocumentFromFile(File arquivo) {

	if (!arquivo.canRead() || !arquivo.isFile()) {
		System.err.println("N�o � poss�vel ler o arquivo ou n�o � um arquivo: "+arquivo.getPath());
		return null;
	}
	
	SAXReader xmlReader = getSAXReader();
	
	try {
		String charset = determinaEncodingXML(new FileReader(arquivo));
		if (charset==null)
			charset = 'UTF-8';
		
		Document doc = xmlReader.read(new InputStreamReader(new FileInputStream(arquivo), charset));
		
		// Caso charset esteja definido, inclua no document
		if (charset != null)
			doc.setXMLEncoding(charset);
		
		return doc;
	} catch (DocumentException e) {
		e.printStackTrace();
		return null;
	} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
		return null;
	} catch (FileNotFoundException e) {
		e.printStackTrace();
		return null;
	}
	
}

/*
* TODO ---- Extrair os m�todos acima para um arquivo groovy Helper depois de resolver o problema do classpath -----
*/

def main(){
	String diretorioArqDestino = acao.getDiretorioArqDestino();
	String projetoDestino = acao.getProjetoDestino();
	
	diretorioArqDestino = PlcGeradorHelper.getInstance().substituiTokens (padrao, diretorioArqDestino);
		
	def casouso = PlcGeradorHelper.getInstance().getCampo(padrao, 'casouso');
	def titulo = PlcGeradorHelper.getInstance().getCampo(padrao, 'titulo');
		
	if (!projetoDestino)
		projetoDestino = padrao.getNomeProjeto();
	else
		projetoDestino = PlcGeradorHelper.getInstance().substituiTokens(padrao, projetoDestino);
	
	File file = new File(gerador.pastaGeracao + File.separator + projetoDestino + File.separator + diretorioArqDestino);
	
	if (file.exists()){
	
		try {

    		String caminho;
    		   		
    		Document dom = getDocumentFromFile(file);
			
    		if (dom != null){
    			// Recuperando ul do menu;
    			DefaultElement ul = (DefaultElement)dom.selectSingleNode("//*[name()='ul']");
    			
    			List elements = ul.elements();
    			if (ul.element("li") != null && ul.element("li").element("ul") != null) {
    				elements = ul.element("li").element("ul").elements();
				}
    			Element li = new DefaultElement ("li");
    			
    			Element a = new DefaultElement ("a");
    			
    			Element label = new DefaultElement("label");
    			
    			li.add(a);
    			
    			String link = '#{request.contextPath}/f/n/' + casouso.valor;
    			a.addAttribute("href" , link);

    			a.add(label);
    			label.addAttribute("jsfc", "plcf:titulo");
    			String tituloChave = "menu."+casouso.valor+".titulo";
    			label.addAttribute("tituloChave", tituloChave);
    			label.setText(titulo.valor);
    			
    			elements.add(li);

    			String menuFinal = dom.asXML().replace(" xmlns=\"\"", "");
    			menuFinal = ajustaIdentacaoXML(menuFinal);
    			
				PlcGeradorHelper.getInstance().gravaConteudoArquivo(file, new StringBuilder(menuFinal), "UTF-8");
				
    		}
    		
			
			
    	} catch (Exception e) {
    		e.printStackTrace();
    		System.out.println("Erro ao tentar altera menu para facelets"+e);
    	} 
	
		PlcGeradorHelper.getInstance().registraAlteracaoArtefato(diretorioArqDestino, "Gerando entrada no arquivo de menu");
	}
}

// Chama a funcao de altera��o
main();
