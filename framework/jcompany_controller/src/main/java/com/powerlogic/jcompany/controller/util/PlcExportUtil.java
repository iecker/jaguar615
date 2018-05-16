/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.util;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.comparator.PlcComparatorString;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;

/**
 * @since jCompany 2.1.
 * jCompany. Gera dados em formato para exportação, somente com colunas declaradas pelo desenvolvedor.
 * @author Pedro Henrique 
 * @author Paulo Alvim 
 */

@SPlcUtil
@QPlcDefault
public class PlcExportUtil {

	@Inject
	protected transient Logger log;
	
	public PlcExportUtil() {
		
	}
	
	/**
	 * jCompany 3.0 DP Composite. Devolve o singleton que encapsula lógica de manipulações de recursos de internacionalização<p>
	 * Ao se pegar o serviço de registro visual a partir deste método e não instanciá-lo diretamente, cria-se um desacoplamento
	 * que permite que se altere este serviço por outros específicos, com mínimo de esforço.
	 * @return Serviço de registro de manipulações visuais.
	 */
	protected PlcI18nUtil getI18nUtil()   {
		return (PlcI18nUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcI18nUtil.class);
	}


	/**
	 * jCompany 3.0 Exportacao
	 */
	@SuppressWarnings("unchecked")
	public void export( HttpServletRequest request, HttpServletResponse response) throws Exception {

		log.debug( "########## Entrou em execute");

		try {

			Object[] ret = verifyFields(request,response);

			if (ret != null){ 

				List<List<String>> colunasValores 	= (List<List<String>>) ret[0];
				Map<String,String> ordemInversa 	= (Map<String,String>) ret[1];
				StringBuffer conteudoTexto 			= new StringBuffer("");

				// Tem que ser antes dos métodos abaixo para que descendentes componham contextType
				response.reset();

				if (request.getParameter("fmtPlc") != null && (request.getParameter("fmtPlc").equalsIgnoreCase("csv")))
					conteudoTexto = mountCSV2000(colunasValores,conteudoTexto);

				if (request.getParameter("fmtPlc") != null && (request.getParameter("fmtPlc").equalsIgnoreCase("csv2003")))
					conteudoTexto = montaCSV2003(colunasValores,conteudoTexto);

				else 
					if (request.getParameter("fmtPlc") != null && request.getParameter("fmtPlc").equalsIgnoreCase("xml"))
						conteudoTexto = mountXML(colunasValores,ordemInversa,conteudoTexto);
					else
						conteudoTexto = mountSpecificApi(request,response,colunasValores,ordemInversa,conteudoTexto);       

				log.debug( "passou do conteudoTexto com "+conteudoTexto.toString().getBytes("ISO-8859-1").length);

				if (request.getParameter("fmtPlc") != null && request.getParameter("fmtPlc").toLowerCase().startsWith("csv")) {

					response.setContentType("text/csv;charset=ISO-8859-1");	
					response.setHeader("Content-disposition","inline;filename=jCompanyExportacao.csv");
					response.getOutputStream().write(conteudoTexto.toString().getBytes("ISO-8859-1"));

				} 
				else 
					if (request.getParameter("fmtPlc") != null && request.getParameter("fmtPlc").equalsIgnoreCase("xml")) {
						response.setContentType("text/xml;charset=ISO-8859-1");
						response.setHeader("Content-Disposition","inline;filename=jCompanyExportacao.xml");
						OutputStream out = response.getOutputStream();
						out.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n".getBytes("ISO-8859-1"));
						out.write(conteudoTexto.toString().getBytes("ISO-8859-1"));
					} 
					else {
						log.debug( "Espera-se que o descendente montaEspecifico tenha composto contextType!");	

						// Opção com request para testes no descendente - chama a de response para compatibilidade apiEscreveResposta(response,conteudoTexto);
						writeResponseApi(request,response,conteudoTexto);
					}
			}
			else
			{
				/*
				 * Se não foi encontrado nenhum registro na pesquisa, mostra a msg internacionalizada para o usuário
				 */
				Locale local = new Locale("pt","BR");
				String msg = getI18nUtil().mountLocalizedMessageAnyBundle(request, "jCompanyResources", "jcompany.advertencia.sel.nenhum.encontrado", new String[]{});

				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println("<html>");
				out.println("	<head><title>");
				out.println("		Exportação</title>");
				out.println("	</head>");
				out.println("	<body>");
				out.println("		<H4>"+msg+"</H4>");
				out.println("	</body></html>");
				out.println("</html>");
				out.close();

			}

		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcExportUtil", "export", e, log, "");
		}

	}

	protected StringBuffer mountCSV2000(List<List<String>> colunasValores,StringBuffer conteudoTexto) {
		return mountCSV(colunasValores,conteudoTexto,",");
	}

	protected StringBuffer montaCSV2003(List<List<String>> colunasValores,StringBuffer conteudoTexto) {
		return mountCSV(colunasValores,conteudoTexto,";");
	}

	/**
	 * Retorna String no formato necessário para Excel com CSV
	 * @param colunasValores
	 */
	protected StringBuffer mountCSV(List<List<String>> colunasValores,StringBuffer conteudoTexto,String separador) {

		String valor = "";
		for(List<String> linha : colunasValores){

			int cont = 0;
			for(String obj : linha){

				cont++;
				if (cont>1)
					conteudoTexto.append(separador);

				if (obj == null)
					valor = "";
				else if (obj instanceof String)
					valor = (String)obj;
				else 
					log.debug( "É instancia de "+obj.getClass().getName());

				// Troca aspas duplas por simples
				valor = StringUtils.replace(valor,"\"","'");
				conteudoTexto.append("\""+valor+"\"");               

			}

			// marca quebra de linha
			conteudoTexto.append("\n");
		}

		if (log.isDebugEnabled())
			log.debug( "vai devolver string = "+conteudoTexto);

		return conteudoTexto;

	}

	/**
	 * Retorna String no formato XML
	 * @param colunasValores Coleção (matriz) de coleções (linhas com colunas String)
	 * @return StringBuffer com dados selecionados separados em XML, utilizando nome da propriedade como atributo.
	 */
	protected StringBuffer mountXML(List<List<String>> colunasValores,Map<String,String> ordemInversa, StringBuffer conteudoTexto) {


		log.debug( "########## Entrou em montaXML");

		conteudoTexto.append("<dados>");

		int contGeral = 0;

		for(List<String> linha : colunasValores){

			contGeral ++;

			// Salta o cabeçalho
			if (contGeral > 1) {
				int cont 	= 0;
				String rotulo = "";

				conteudoTexto.append("	<registro>");
				for(String valor : linha){

					// pega rótulo
					cont++;
					rotulo = (String) ordemInversa.get(cont+""); 

					if (cont>1)
						conteudoTexto.append("\n");

					conteudoTexto.append("		<"+rotulo+">");

					conteudoTexto.append("<![CDATA[" + valor + " ]]>");               
					conteudoTexto.append("</"+rotulo+">");

				}

				// marca quebra de linha
				conteudoTexto.append("	</registro>");
				conteudoTexto.append("\n");

			}


		}

		conteudoTexto.append("</dados>");

		if (log.isDebugEnabled())
			log.debug( "vai devolver string = "+conteudoTexto);

		return conteudoTexto;

	}

	/**
	 * Monta valores em um array, incluindo cabecalho
	 * 
	 */
	protected Object[] mountExportContent(List<Object> listaVOs, String campos)  {

		log.debug( "########## Entrou em montaSaidaExportacao");

		StringTokenizer s= new StringTokenizer(campos,",");

		List<List<String>> matriz 	= new ArrayList<List<String>>();
		List<String> linha 			= new ArrayList<String>();
		StringBuffer colunas 		= new StringBuffer("");
		Locale locale 				= new Locale("pt","BR");
		String[] sAux 				= new String[]{};
		Map<String,String> ordem 	= new HashMap<String,String>();
		Map<String,String> ordemInversa = new HashMap<String,String>();

		int cont=0;

		// Inclui cabecalho
		while (s.hasMoreElements()) {
			cont++;
			String nomeCol 	= (String) s.nextElement();
			colunas.append("#"+nomeCol+"#");
			String chave 	= "label."+nomeCol;

			String msg = getI18nUtil().mountLocalizedMessage("ApplicationResources", chave, sAux);

			if (msg == null)
				msg = nomeCol;

			linha.add(msg);     

			// AUxiliar para montar XML
			ordemInversa.put(cont+"",nomeCol);

			// Auxiliar para ordenação da coleção até 99 colunas
			if (cont<10) {
				ordem.put(nomeCol,"0"+cont+"");
			} else {
				ordem.put(nomeCol,cont+"");
			}
		}

		linha = mountExportContentBefore(linha,ordem,ordemInversa,listaVOs);
		matriz.add(linha);

		for(Object entity : listaVOs){

			log.debug( "vai popular por reflexao para colunas "+colunas);

			// Recupera valor por reflexao
			linha = populateByReflection(colunas,entity,ordem);
			linha = addLineBefore(linha,ordem,entity);

			matriz.add(linha);
		}

		log.debug( "vai retornar matriz com "+matriz);

		return new Object[]{matriz,ordemInversa};
	}

	@SuppressWarnings("unchecked")
	protected List<String> populateByReflection(StringBuffer colunas, Object vo, Map<String,String> ordem)  {

		log.debug( "########## Entrou em populateByReflection");

		List<String> linha 		= new ArrayList<String>();
		String colunasStr		= colunas.toString().replaceAll("##", ",");
		colunasStr				= colunasStr.replaceAll("#", "");
		StringTokenizer arrayColunas	= new StringTokenizer(colunasStr,",");

		try {
			
			//cria instancia do propertyUtilsBean para evitar diversas chamadas a metodo syncronized
			PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();

			Object valor	= null;
			String valorAux	= "";
			while (arrayColunas.hasMoreElements()) {
				String coluna 	= (String) arrayColunas.nextElement();

				try {
					String colunaFinal 	= "";
					Object voFinal	= null;
					// Pode ser componente ou propriedade de um agregado
					if (coluna.contains("_")){
						colunaFinal 	= coluna.substring(0,coluna.indexOf("_"));
						voFinal			= propertyUtilsBean.getProperty(vo,colunaFinal);
						colunaFinal		= coluna.substring(coluna.indexOf("_")+1,coluna.length());		
					}
					else{
						colunaFinal = coluna;
						voFinal	 = vo;
					}
					
					valor 		= propertyUtilsBean.getProperty(voFinal,colunaFinal);
					valorAux 	= valor + "";

					Class typecoluna = propertyUtilsBean.getPropertyType(voFinal, colunaFinal);
					if (Date.class.isAssignableFrom(typecoluna)) {
						// Ajusta valor inicial
						if (valor != null) {
							SimpleDateFormat f 	= new SimpleDateFormat("dd/MM/yyyy HH:mm");
							valorAux 			= f.format((java.util.Date)valor);					
						}
						if (valorAux.endsWith("00:00"))
							valorAux = valorAux.substring(0,10);
					}

					valorAux = exportInsertUnitValueBefore(typecoluna.getClass().getName(),valorAux);
					// Adiciona uma flag para ordenar a lista
					if ("null".equals(valorAux))
						valorAux = "";
					linha.add(ordem.get(coluna)+"####"+valorAux);

				} catch (Exception e) {
					log.error( "Erro ao tentar pegar valor inicial para propriedade = " + coluna+" verifique se o getter para esta propriedade existe no VO");
				}
			}


			Collections.sort(linha,new PlcComparatorString());

			// Retira flag de ordenação
			int cont=0;
			List<String> linhaFinal = new ArrayList<String>();
			for(String obj: linha){
				obj = obj.substring(obj.indexOf("####")+4);
				linhaFinal.add(cont,obj);
				cont++;
			}

			return linhaFinal;

		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcExportUtil", "populateByReflection", e, log, "");
		}

	}

	@SuppressWarnings("unchecked")
	protected Object[] verifyFields ( javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)  {


		// Obtendo coleção 
		List<Object> listaVOs = exportGetCollectionBefore(request,response);
		listaVOs = (List<Object>)request.getAttribute("itensPlcExportacao");


		Object[] ret =	null;

		if ( listaVOs != null  && listaVOs.size() > 0 && 
				(request.getAttribute("exportacaoCamposPlc") != null && !((String)request.getAttribute("exportacaoCamposPlc")).equals(""))) {

			String campos 	= ((String)request.getAttribute("exportacaoCamposPlc"));
			campos 			= verifyFieldsBefore(campos,request,response);

			if (log.isDebugEnabled())
				log.debug( "vai montar a saida exportacao para "+listaVOs.size()+" itens e para colunas="+campos);

			ret = mountExportContent(listaVOs,campos);

			if (log.isDebugEnabled())
				log.debug( "vai disponibilizar "+((List<List<String>>)ret[0]));

		}

		return ret;

	}



	/**
	 * jCompany 2.1. Método para descendente modificar relação de campos dinamicamente.
	 * @param campos Relação de campos originalmente definidos no action-mapping
	 * @return String relação modificada de campos, separados por vírgula
	 */
	protected String verifyFieldsBefore(String campos,  HttpServletRequest request, HttpServletResponse response)  {
		return campos;
	}


	/**
	 * jCompany 2.1. Método para complemento de nomes de cabeçalhos, para inclusão dinamica no descendente.
	 * @param linha Coleção com nomes de colunas padrões já populadas
	 * @param ordem Map contendo "nomePropriedade"+"contador" para auxiliar na ordenação
	 * @param ordemInversa Map contendo "contador"+"nomePropriedade" para auxiliar na montagem de XML
	 * @return coleção linha modificada, com acrescimo de cabeçalhos no descendente.
	 */
	protected List<String> mountExportContentBefore(List<String> linha, Map<String,String> ordem, Map<String,String> ordemInversa, List<Object> listaVOs)  {
		return linha;
	}

	/**
	 * jCompany 2.1. Método para complemento de valores para exportação, para inclusão dinamica no descendente.
	 * @param linha Coleção com nomes de colunas padrões já populadas
	 * @param ordem Map contendo "nomePropriedade"+"contador" para auxiliar na ordenação
	 * @param vo VO corrente para se obter valores.
	 * @return coleção linha modificada, com acrescimo de cabeçalhos no descendente.
	 */
	protected List<String> addLineBefore(List<String> linha, Map<String,String> ordem, Object entity)   {
		return linha;
	}

	/**
	 * jCompany 2.1. Permite que o descendente monte um String contendo exportação para um formato especifico.
	 * Ele deve também 
	 * @param response
	 * @param request
	 * @param matriz Tabela (coleção de coleções, contendo linhas e colunas com valores em String)
	 * @param ordemInversa (Map com chave 1 a N e valor contendo nome da propriedade, na ordem da exportação)
	 * @param conteudoTexto (Conteudo para ser montado e devolvido.
	 * @return conteudoTexto deve ser devolvido populado
	 */
	protected StringBuffer mountSpecificApi(HttpServletRequest request, HttpServletResponse response, 
			List<List<String>> matriz, Map<String,String> ordemInversa, StringBuffer conteudoTexto)  {
		return conteudoTexto;
	}

	/**
	 * jCompany 2.1 Método para permitir que descendente faça procedimentos antes da exportação dos dados,
	 * por exemplo, capturando uma coleção diferente de "itensPlc".
	 * @return List Coleção com valores para extração dos dados de exportação, devendo conter PlcObjetoNegocioVO ou null
	 * para padrão "itensPlc".
	 */
	protected List<Object> exportGetCollectionBefore( HttpServletRequest request, HttpServletResponse response)  {
		return null;
	}

	/**
	 * jCompany 2.1 Método para permitir que descendente faça a montagem da String de saída 
	 * antes da exportação dos dados, já de posse de uma coleção (matriz) contendo outras coleções (linhas) 
	 * para parametos de formato diferentes de "csv","txt" e "xml". Espera-se também que o descendente
	 * inclua o contextType no response. Exemplo: response.setContentType("text/plain");	
	 * @param matriz Coleção (matriz com linhas)
	 * @return StringBuffer com formato de saida.
	 */
	protected StringBuffer exportPrepareContentBefore(List<List<String>> matriz, HttpServletRequest request, HttpServletResponse response)  {
		return null;
	}

	/**
	 * jCompany 2.1. Método para descendente ajustar valor default.
	 * @param nomePropriedade Nome da propriedade
	 * @param valor Valor corrente capturado
	 * @return String Valor modificado
	 */
	protected String exportInsertUnitValueBefore(String nomePropriedade, String valor)  {
		return valor;
	}

	/**
	 * jCompany 2.1. Método para sobreposição no descendente para alerações na forma de se escrever
	 * o download. Assume o default:  response.getOutputStream().write(conteudoTexto.toString().getBytes("ISO-8859-1"));
	 * @param response
	 * @param conteudoTexto
	 */
	protected void writeResponseApi(HttpServletResponse response, StringBuffer conteudoTexto)  {

		try {
			response.getOutputStream().write(conteudoTexto.toString().getBytes("ISO-8859-1"));
		} catch (Exception e) {
			throw new PlcException("PlcExportUtil", "writeResponseApi", e, log, "");
		}

	}

	protected void writeResponseApi(HttpServletRequest request, HttpServletResponse response, StringBuffer conteudoTexto)  {

		writeResponseApi(response,conteudoTexto);

	}

	/**
	 *  jCompany 3.0 - Monta o código Html que faz com que a Janela que chamou a exportação chame o método
	 *  de pesquisa, para mostrar para o usuário que tem erros de validação na tela "validator"
	 *  Ex: Campo obrigatório que não foi informado e o usuário chamou a exportação.
	 *  
	 * @param request - Referência ao request
	 * @param response - Referência ao response
	 * @throws Exception - Exceção caso ocorra algum erro
	 */
	public void writeError( HttpServletRequest request, HttpServletResponse response) throws Exception {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("	<head><title>");
		out.println("		Exportação</title>");
		out.println("	</head>");
		out.println("	<body>");
		out.println("		<script>		" +
				"if(opener)	" +
				"opener.disparaBotao(opener.selBotao('Pesquisar'));" +
				"close(); 	" +
		"</script>");
		out.println("	</body></html>");
		out.println("</html>");
		out.close();

	}



}