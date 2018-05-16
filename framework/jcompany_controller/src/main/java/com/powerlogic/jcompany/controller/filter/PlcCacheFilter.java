/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.filter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.util.PlcCacheUtil;
import com.powerlogic.jcompany.controller.util.PlcJavaEEFileUtil;
import com.powerlogic.jcompany.controller.util.PlcServletRequestUtil;

/**
 * jCompany 1.5 Filtro utililizado para registro de informações no Header de 
 * mensagens HTTP. É utilizado no jCompany, por default, para registrar
 * caching de 1 hora para imagens, arquivos javascript, css e outros artefatos
 * de "cliente". <p>
 * Importante: É altamente recomendado seu uso porque evita grande volume
 * de tráfego desnecessário na red. Conferir se está registrado
 * corretamente no web.xml.
 * @since jCompany 1.5
 */
public class PlcCacheFilter implements Filter {

	private static final Logger log = Logger.getLogger(PlcCacheFilter.class.getCanonicalName());
	
	private static final Logger logControle = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_CONTROLE);

	FilterConfig fc;	

	/**
	 * Implementa o filtro que registra qualquer informação no Header.
	 * @since jCompany 5.5 Otimiza carga de arquivos .js e .css com técnica de unificação.
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

		PlcServletRequestUtil servletRequestUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcServletRequestUtil.class, QPlcDefaultLiteral.INSTANCE);

		HttpServletRequest request = (HttpServletRequest) req;
		
		HttpServletResponse response = (HttpServletResponse) res;
		// set the provided HTTP response parameters
		for (Enumeration<?> e = fc.getInitParameterNames(); e.hasMoreElements();) {
			String headerName = (String) e.nextElement();
			response.addHeader(headerName, fc.getInitParameter(headerName));
		}
		
		String path = servletRequestUtil.getServletPath(request);
		
		// Intercepta arquivos de otimização de javascript e CSS
		if (!path.equals("/res-plc/javascript/plc.comuns.js") && !path.equals("/res-plc/css/plc.comuns.css")	)
			chain.doFilter(req, response);
		else
			processaPlcComuns(request,response);	

	}
	
	/**
	 * Trabalha os arquivos com padrão "comuns", de Javascript e CSS, para serví-los de forma otimizada.
	 * Mantém em caching de escopo de aplicação, após a primeira interpretação.
	 */
	protected void processaPlcComuns(HttpServletRequest request, HttpServletResponse response){

		PlcServletRequestUtil servletRequestUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcServletRequestUtil.class, QPlcDefaultLiteral.INSTANCE);

		try {
			
			String plcomuns = servletRequestUtil.getServletPath(request);
			
			PlcCacheUtil cacheUtil = (PlcCacheUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcCacheUtil.class, QPlcDefaultLiteral.INSTANCE);
		
			StringBuffer arquivoUnificado = (StringBuffer) cacheUtil.getObject(plcomuns);
			
			if (arquivoUnificado==null) {	
								
				arquivoUnificado = trataArquivoComuns(request, plcomuns);
				
				if (isModoProducao(request.getSession().getServletContext()) )
					cacheUtil.putObject(plcomuns, arquivoUnificado);
				
			}
			
			if (arquivoUnificado!=null) {
				if (plcomuns.endsWith(".css")) { 
					response.setContentType("text/css");
				} else if (plcomuns.endsWith(".js")) {
					response.setContentType("text/javascript");
				}
				response.setCharacterEncoding("UTF-8");
				response.getWriter().append(arquivoUnificado);
				response.flushBuffer();
			}

		} catch (PlcException e) {
			log.error( "Erro ao tentar otimizar CSS/Javascript "+e.getCausaRaiz(),e.getCausaRaiz());
		} catch (IOException e) {
			log.error( "Erro ao tentar otimizar CSS/Javascript "+e,e);
		}



	}

	private StringBuffer trataArquivoComuns(HttpServletRequest request, String plcomuns) throws IOException {
		
		PlcJavaEEFileUtil arquivoJavaEEUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcJavaEEFileUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		Object[] arquivos = geraListaArquivoPorTipo(request, plcomuns, arquivoJavaEEUtil);
		StringBuffer arquivoUnificado = new StringBuffer();
		if (arquivos!=null) {
			for (Object umaLinha : arquivos) {
				// Tem que extrair o nome do arquivo da declaração, conforme seja javascript ou CSS
				String nomeArquivo = recuperaNomeDeDeclaracao(umaLinha.toString());
				
				if (nomeArquivo != null) {
					// tratar bibliotecas de extensions
					if ((nomeArquivo.endsWith("/plx.commons.js")) || (nomeArquivo.endsWith("/plx.commons.css"))) {
						arquivoUnificado = trataExtensionLib(request, nomeArquivo, arquivoUnificado);
					} else {	
						arquivoUnificado = adicionaArquivoUnificado(request, plcomuns, arquivoUnificado, umaLinha, nomeArquivo);
					}
				}	
			}
		}
		
		return arquivoUnificado;
		
	}

	private StringBuffer trataExtensionLib(HttpServletRequest request, String arquivoPlxCommons, StringBuffer arquivoUnificado) throws IOException {
		String path = "/res-plx/javascript";
		if (arquivoPlxCommons.endsWith(".css")) {
			path = "/res-plx/css";
		}
			
		PlcJavaEEFileUtil arquivoJavaEEUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcJavaEEFileUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		Set<String> resourcePaths = request.getSession().getServletContext().getResourcePaths(path);

		if (resourcePaths != null && !resourcePaths.isEmpty()) {
			for (String plxcommons : resourcePaths) {
				if ((StringUtils.substringAfterLast(plxcommons, "/").startsWith("plx")) && (plxcommons.endsWith(".commons.js") || plxcommons.endsWith(".commons.css"))) {
					
					Object[] arquivos = geraListaArquivoPorTipo(request, plxcommons, arquivoJavaEEUtil);
					if (arquivos!=null) {
						for (Object umaLinha : arquivos) {
							// Tem que extrair o nome do arquivo da declaração, conforme seja javascript ou CSS
							String nomeArquivo = recuperaNomeDeDeclaracao(umaLinha.toString());
							
							arquivoUnificado = adicionaArquivoUnificado(request, plxcommons, arquivoUnificado, umaLinha, nomeArquivo);
						}
					}
					
				}
			}
		}
		
		return arquivoUnificado;
	}

	private Object[] geraListaArquivoPorTipo(HttpServletRequest request, String plcomuns, PlcJavaEEFileUtil arquivoJavaEEUtil) {
		Object[] arquivos;
		try {
			if (plcomuns.endsWith("css")) { 
				arquivos = arquivoJavaEEUtil.readLines(request, plcomuns);
			} else {
				arquivos = arquivoJavaEEUtil.readLines(request, plcomuns);
			}
		} catch (PlcException e) {
			arquivos = null;
		}
		return arquivos;
	}

	private StringBuffer adicionaArquivoUnificado(HttpServletRequest request, String plcomuns, StringBuffer arquivoUnificado, Object umaLinha, String nomeArquivo) throws IOException {
		String umaLinhaComContexto;
		// Erro ocorrido por causa do path relativo do css
		if (nomeArquivo!=null && !nomeArquivo.endsWith(".comuns.css") && !nomeArquivo.endsWith(".comuns.js") && isModoProducao(request.getSession().getServletContext()) ) {
			// Le o conteúdo do arquivo e concatena com os demais, unificando.
			InputStream is = request.getSession().getServletContext().getResourceAsStream(nomeArquivo);
			if (is!=null) {
				arquivoUnificado.append(IOUtils.toString(is, "UTF-8") );
				arquivoUnificado.append("\n");
			}
		} else {
			// Se entrou aqui é modo de desenvolvimento, entao somente inclui o contexto relativo da aplicacao.
			//umaLinhaComContexto = PlcStringUtil.getInstance().trocaTermo(umaLinha.toString(), "#contextPath#",request.getContextPath());
			if (umaLinha.toString().contains(".comuns.")) { 
				if (StringUtils.substringBetween(umaLinha.toString(), "#contextPath#", "\">'")!=null)
					umaLinhaComContexto = trataArquivoComuns(request, StringUtils.substringBetween(umaLinha.toString(), "#contextPath#", "\">'")).toString();
				else
					umaLinhaComContexto = trataArquivoComuns(request, StringUtils.substringBetween(umaLinha.toString(), "#contextPath#", ");")).toString();
			}
			else
				umaLinhaComContexto = umaLinha.toString().replaceAll("#contextPath#", request.getContextPath());
			
			if ((!nomeArquivo.endsWith("/plx.commons.js")) && (!nomeArquivo.endsWith("/plx.commons.css"))) {
				arquivoUnificado.append(umaLinhaComContexto);
				arquivoUnificado.append("\n");
			}	
			
		}
		return arquivoUnificado;
	}

	private StringBuffer trataCommonsFile(HttpServletRequest request, String plxCommons) throws IOException {
		
		PlcJavaEEFileUtil arquivoJavaEEUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcJavaEEFileUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		StringBuffer arquivoUnificado;
		Object[] arquivos;
		arquivos = geraListaArquivoPorTipo(request, plxCommons, arquivoJavaEEUtil);
		arquivoUnificado = new StringBuffer();
		if (arquivos!=null) {
			String umaLinhaComContexto;
			for (Object umaLinha : arquivos) {
				// Tem que extrair o nome do arquivo da declaração, conforme seja javascript ou CSS
				String nomeArquivo = recuperaNomeDeDeclaracao(umaLinha.toString());
				// Erro ocorrido por causa do path relativo do css
				if (!plxCommons.endsWith("css") && nomeArquivo!=null && !nomeArquivo.startsWith("plx") && (!nomeArquivo.endsWith(".commons.css") && !nomeArquivo.endsWith(".commons.js")) && isModoProducao(request.getSession().getServletContext()) ) {
					// Le o conteúdo do arquivo e concatena com os demais, unificando.
					InputStream is = request.getSession().getServletContext().getResourceAsStream(nomeArquivo);
					if (is!=null) {
						arquivoUnificado.append(IOUtils.toString(is, "UTF-8") );
						arquivoUnificado.append("\n");
					}
				} else {
					// Se entrou aqui é modo de desenvolvimento, entao somente inclui o contexto relativo da aplicacao.
					//umaLinhaComContexto = PlcStringUtil.getInstance().trocaTermo(umaLinha.toString(), "#contextPath#",request.getContextPath());
					if (umaLinha.toString().contains(".commons.")) { 
						if (StringUtils.substringBetween(umaLinha.toString(), "#contextPath#", "\">'")!=null)
							umaLinhaComContexto = trataArquivoComuns(request, StringUtils.substringBetween(umaLinha.toString(), "#contextPath#", "\">'")).toString();
						else
							umaLinhaComContexto = trataArquivoComuns(request, StringUtils.substringBetween(umaLinha.toString(), "#contextPath#", ");")).toString();
					}
					else
						umaLinhaComContexto = umaLinha.toString().replaceAll("#contextPath#", request.getContextPath());
					arquivoUnificado.append(umaLinhaComContexto);		
					arquivoUnificado.append("\n");
				}
			}
		}
		return arquivoUnificado;
	}
	
	/**
	 * Extrai o nome de um arquivo de uma declaração de importaçao CSS ou Javascript, para unificação.
	 * Importante: Declarar exatamente como o exemplo para evitar problemas de interpretação. Cuidado com espaços duplos.
	 * @param umaLinha Linha contendo uma das hipóteses dos exemplos abaixo:
	 * <pre>
	 * @import url(#contextPath#/res-plc/css/jquery/jquery.autocomplete.css);
	 * ou
	 * <script src="#contextPath#/res-plc/javascript/jquery/jquery.js" type="text/javascript"></script>
	 * </pre>
	 * @return Nome do arquivo relativo como '/res-plc/css/jquery/jquery.autocomplete.css' ou '/res-plc/javascript/jquery/jquery.js'
	 */
	private String recuperaNomeDeDeclaracao(String umaLinha) {
		
		if (umaLinha.startsWith("@import url")) {
			return StringUtils.substringBetween(umaLinha, "#contextPath#", ");");
		} else if (umaLinha.startsWith("includeScript")){
			return StringUtils.substringBetween(umaLinha, "#contextPath#", "');");
		} else if (StringUtils.substringBetween(umaLinha, "#contextPath#", "\" type=\"text/javascript\"")!=null){
			return StringUtils.substringBetween(umaLinha, "#contextPath#", "\" type=\"text/javascript\"");
		} else {
			return StringUtils.substringBetween(umaLinha, "#contextPath#", "\">'");
		}	
		
	}
	
	public void init(FilterConfig filterConfig) {
		this.fc = filterConfig;
	}
	public void destroy() {
		this.fc = null;
	}
	
    public boolean isModoProducao(ServletContext ctx)  {

    	return "P".equalsIgnoreCase(ctx.getInitParameter(PlcConstants.CONTEXTPARAM.INI_MODO_EXECUCAO));
    	
    }
}

