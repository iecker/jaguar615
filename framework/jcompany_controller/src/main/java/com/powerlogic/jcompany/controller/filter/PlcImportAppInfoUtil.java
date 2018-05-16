/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */

package com.powerlogic.jcompany.controller.filter;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.controller.appinfo.PlcAppInfoUtil;

/**
 * Verifica se uma requisição tem intenção de pesquisar por recursos
 * configurados na aplicação (Ex: jSecurity) TODO - Refatorar fornecimento de
 * URLs da aplicação para jSecurity
 */
@SPlcUtil
@QPlcDefault
public class PlcImportAppInfoUtil {

	private static final String URL_SERVICO_APPINFO = "/plc.servico.appinfo.do";

	private static final String URL_SERVICO_APPINFO_JSF_FACELETS = "/f/plc.servico.appinfo";
	
	private static final String URL_SERVICO_APPINFO_JSF = "/f/t/plcjsecurity.auto.cadastro";

	@Inject
	@QPlcDefault
	protected PlcAppInfoUtil appInfoUtil;

	/**
	 * Verifica se a requisição é uma solicitação da listagem de recursos da
	 * aplicação para cadastro automático no jSecurity.
	 */
	public boolean checkAndResponseAppInfo(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String path = extractResourceName(req);
		if (StringUtils.isNotEmpty(path)) {
			if (path.endsWith(URL_SERVICO_APPINFO) || path.endsWith(URL_SERVICO_APPINFO_JSF) || path.endsWith(URL_SERVICO_APPINFO_JSF_FACELETS)) {
				appInfoUtil.responseAppInfo(req, resp);
				return true;
			}
		}
		return false;
	}

	/**
	 * Recupera o nome do recurso, removendo a string "jsessionid".
	 * 
	 * @param request
	 * @return
	 */
	public String extractResourceName(HttpServletRequest request) {
		String uri = removeSessionId(request.getRequestURI());
		String ctx = request.getContextPath();
		uri = uri.substring(ctx.length());
		return uri;
	}

	/**
	 * Remove sessionid da uri, se existir.
	 * 
	 * @param uri
	 * @return
	 */
	private String removeSessionId(String uri) {
		int ini = uri.indexOf(";jsessionid");
		if (ini != -1) {
			int fim = uri.indexOf("?");
			if (fim == -1) {
				uri = uri.substring(0, ini);
			} else {
				uri = uri.substring(0, ini) + uri.substring(fim);
			}
		}
		return uri;
	}
}
