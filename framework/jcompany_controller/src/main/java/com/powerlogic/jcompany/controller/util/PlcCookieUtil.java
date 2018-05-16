/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.util;

import java.io.Serializable;
import java.util.StringTokenizer;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.PlcStringUtil;
import com.powerlogic.jcompany.controller.cache.PlcCacheSessionVO;

/**
 * jCompany 3.0 Serviços de manipulação de cookies
 * @since jCompany 3.0
 */

@SPlcUtil
@QPlcDefault
public class PlcCookieUtil  implements Serializable {

	private static final long serialVersionUID = 7856272017030319436L;

	@Inject
	protected transient Logger log;

	/**
	 * Serviço injetado e gerenciado pelo CDI
	 */
	@Inject @QPlcDefault 
	protected PlcLocaleUtil localeUtil;

	@Inject @QPlcDefault 
	protected PlcStringUtil stringUtil;

	public PlcCookieUtil() {

	}

	/**
	 * Recupera o valor de um cookie específico do request. Se não achou retorna "".
	 * @param request Objeto Request Http
	 * @param chave Nome do cookie a ser retornado
	 * @return Valor do cookie em String ou "" se não existe cookie
	 */
	public String getCookieValue(HttpServletRequest request, String chave) {
		if (log.isDebugEnabled()) 
			log.debug( "################## Entrou para ler valor do cookie para chave: "+chave);

		Cookie c = getCookie(request, chave);

		if (c != null) {
			return c.getValue();
		} else {
			return "";
		}		
	}

	/**
	 * jCompany. Função que encapsula o procedimento de gravar um cookie existentes no objeto Response.
	 */
	public void saveCookie(HttpServletRequest request, HttpServletResponse response,Cookie c) {
		saveCookieUnique(request,response, c.getName(), c.getValue(), c.getDomain(), c.getPath());
	}

	/**
	 * Recupera um cookie específico do request
	 * @param chave Nome do cookie a ser retornado
	 * @return um cookie
	 */
	public Cookie getCookie(HttpServletRequest request, String chave) {		
		if (log.isDebugEnabled()) {
			log.debug( "################## Entrou para recuperar cookie para chave: "+chave);
		}
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for(int i=0;i<cookies.length;i++) {
				Cookie c = (Cookie) cookies[i];	
				if (log.isDebugEnabled())
					log.debug( "**************Cookie: "+c.getName()+" - Valor: "+c.getValue()+
							"Versao="+c.getVersion()+" dominio="+ c.getDomain()+" caminho="+c.getPath());

				if (c.getName().equals(chave)) { 
					return c;
				}
			}
		}
		return null;
	}

	/**
	 * jCompany. Função que encapsula o procedimento de registrar um cookie no objeto Response.
	 * Todo registro de cookie deve utilizar esta função, para facilitar aplicação de 
	 * procedimentos padrões como RFC 2109
	 * @param chave Chave de acesso do novo cookie
	 * @param valor Valor do novo cookie
	 */
	public void saveCookie(HttpServletRequest request, HttpServletResponse response, String chave, String valor) {
		saveCookie(request,response,chave,valor,"","");
	}	

	/**
	 * jCompany. Função que encapsula o procedimento de registrar um cookie no objeto Response.
	 * Todo registro de cookie deve utilizar esta função, para facilitar aplicação de 
	 * procedimentos padrões como RFC 2109.
	 * @param chave Chave de acesso do novo cookie
	 * @param valor Valor do novo cookie
	 */
	public void saveCookie(HttpServletRequest request, HttpServletResponse response,String chave, String valor,String dominio, String path) {

		if (log.isDebugEnabled()) {
			log.debug( "####### Vai gravar cookie com chave:"+chave+" e valor="+valor+" no path"+ dominio+"/"+path);
		}

		saveCookieUnique(request, response, chave, valor, dominio, path);		
	}

	/**
	 * jCompany. Esta função evita gravar dois cookies com mesmo nome, procurando e alterando o valor de um
	 * cookie ao invés de incluir dois com mesmo nome.
	 * @param chave Chave de acesso do novo cookie
	 * @param valor Valor do novo cookie
	 */
	public void saveCookieUnique(HttpServletRequest request, HttpServletResponse response,
			String chave, String valor,String dominio, String path) {

		if (log.isDebugEnabled()) {
			log.debug( "####### Vai gravar cookie unico com chave:"+chave+" e valor="+valor+" no path"+ dominio+"/"+path);
		}

		Cookie cookie = null; 

		if (log.isDebugEnabled())
			log.debug( "**************** Vai criar novo cookie="+chave+ " para valor:"+valor);

		cookie = new Cookie(chave,valor);
		cookie.setMaxAge(2592000);

		if (!path.equals("")) {
			cookie.setPath(path);
		} else {
			cookie.setPath("/");
		}

		if (!dominio.equals("")) {
			cookie.setDomain(dominio);
		}

		response.addCookie(cookie);	

	}


	/**
	 * jCompany. Trata os cookies que armazenam as preferências do usuário.
	 * Como pele, língua e layout, garantindo que sejam "lembrados".
	 */
	public void checkCookies(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException {

		if (request.getParameter(PlcConstants.COOKIE.REDIRECT) != null) {
			addCookies(request,response);
		} else {

			Cookie[] cookieA = request.getCookies();

			int cont=0;
			if (cookieA != null && cookieA.length > 0) {

				HttpSession session = request.getSession();
				PlcCacheSessionVO plcSessao = (PlcCacheSessionVO) session
				.getAttribute(PlcConstants.SESSION_CACHE_KEY);

				while (cont < cookieA.length) {

					Cookie c = (Cookie) cookieA[cont];
					String nome = c.getName();
					String valor = c.getValue();
					int versao = c.getVersion();
					if (log.isDebugEnabled()) {
						log.debug( "Cookie="+nome+" valor="+valor+ "versao="+versao);
					}

					// Este tratamento so deve ser realizado no inicio de cada sessao
					if (request.getSession().getAttribute(PlcConstants.COOKIE.COOKIES_LAYOUT_PROCESSADOS) == null) {


						if (PlcConstants.COOKIE.COOKIE_IDIOMA.equals(nome)) {

							//TODO - Verificar como registrar um idioma alternativo padrão selecionado pelo usuario

						} else if (PlcConstants.COOKIE.COOKIE_PELE.equals(nome)) {
							// existe pele
							if (request.getSession().getServletContext().getResourcePaths("/res-plc/css/"+valor+"/PlcPele.css")!=null
									|| request.getSession().getServletContext().getResourcePaths("/res-plc/css/"+valor) != null)	{
								plcSessao.setPele(valor);
							}

						} else if (PlcConstants.COOKIE.COOKIE_LAYOUT.equals(nome)) {
							int aux = versao;
							String layout = valor;
							String layoutBase = layout;
							int pos = layout.indexOf("_reduzido");
							if (pos > -1) {
								layoutBase = layout.substring(0, pos);
								plcSessao.setIndLayoutReduzido("S");
							} else {
								plcSessao.setIndLayoutReduzido("N");
							}

							plcSessao.setLayout(layoutBase);

						} else if(PlcConstants.COOKIE.COOKIE_FORM.equals(nome)) {
							// TODO - Retirar os NULLs e consequentemente tirar os nextToken
							StringTokenizer st = new StringTokenizer(valor,"_",false);
							if(st.hasMoreTokens()) { 
								st.nextToken();
							}
							if(st.hasMoreTokens()) { 
								st.nextToken();
							}
							if(st.hasMoreTokens()) {
								String acoesExibeTexto = st.nextToken();
								plcSessao.setFormAcaoExibeTexto(acoesExibeTexto);
							}
							if(st.hasMoreTokens()) {
								st.nextToken();
							}
							if(st.hasMoreTokens()) {
								String formAlertaAlteracao = st.nextToken();
								plcSessao.setFormAlertaAlteracao(formAlertaAlteracao);

							}
							if(st.hasMoreTokens()) {
								String formAlertaExclusaoDetalhe = st.nextToken();
								plcSessao.setFormAlertaExclusaoDetalhe(formAlertaExclusaoDetalhe);
							}
							if(st.hasMoreTokens()) {
								st.nextToken();
							}
							if(st.hasMoreTokens()) {
								String pesquisaRestful = st.nextToken();
								plcSessao.setPesquisaRestful(pesquisaRestful);
							}							

						} else {
							// Cookies que têm os valores simplesmente copiados
							// com o mesmo
							// nome como atributos da sessão.

							session.setAttribute(nome, valor);
						}
					}
					cont++;
				}
				// Para lembrar que ja foi feito o tratamento inicial para os cookeis de layout
				request.getSession().setAttribute(PlcConstants.COOKIE.COOKIES_LAYOUT_PROCESSADOS, PlcConstants.SIM);
				session.setAttribute(PlcConstants.SESSION_CACHE_KEY,plcSessao);

			} else {
				log.debug( "Cookies nulos");
			}

		}
		// Se não tem cookie (primeira vez) assume um default
		localeUtil.availableDefaultLocale(request.getSession());
	}

	/**
	 * jCompany. Após um redirect, eventos que registram cookies podem ser perdidos,
	 * porque o objeto response não é enviado para o Browser. Este evento garante,
	 * devendo ser chamado após um redirect, que os registros atualmente gravados
	 * na sessão serão registrados em cookies.
	 * 
	 */
	public void addCookies(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {

		log.debug( "########## Entrou para incluir cookie");

		HttpSession session = request.getSession();
		PlcCacheSessionVO cache = (PlcCacheSessionVO) session.getAttribute(PlcConstants.SESSION_CACHE_KEY);

		if (cache != null) {

			if (StringUtils.isNotBlank(cache.getLayout())) {
				if (log.isDebugEnabled()) {
					log.debug( "Vai incluir "+cache.getLayout()+" no cookie");
				}

				String layoutBase = cache.getLayout();
				if (cache.getIndLayoutReduzido().equals("S")) {
					layoutBase = layoutBase + "_reduzido";
				}

				saveCookie(request, response, PlcConstants.COOKIE.COOKIE_LAYOUT, layoutBase);
			}

			if (StringUtils.isNotBlank(cache.getPele())) {

				if (log.isDebugEnabled()) {
					log.debug( "Vai incluir "+cache.getPele()+" no cookie");
				}

				saveCookie(request, response, PlcConstants.COOKIE.COOKIE_PELE, cache.getPele());
			}

			if(StringUtils.isNotBlank(cache.getFormToCookie())) {
				if (log.isDebugEnabled()) {
					log.debug( "Vai incluir "+cache.getFormToCookie()+" no cookie");
				}
				saveCookie(request,response,PlcConstants.COOKIE.COOKIE_FORM,cache.getFormToCookie());
			}

		}

		String resolucao = (String) session.getAttribute(PlcConstants.COOKIE.RESOLUCAO_VIDEO);

		if (StringUtils.isNotBlank(resolucao)) {
			saveCookie(request, response, PlcConstants.COOKIE.RESOLUCAO_VIDEO, resolucao);
		}

		String ultEd = (String) session.getAttribute(PlcConstants.COOKIE.COOKIE_ULT_EDICOES);
		if (ultEd != null) {
			saveCookie(request, response, PlcConstants.COOKIE.COOKIE_ULT_EDICOES, ultEd);
		}
	}



}
