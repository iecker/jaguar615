/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;

/**
 * Classe Helper que implementada alguns comportamentos não encontrados nos métodos de
 * HttpServletRequest padrao da especificacao Servlet.
 * 
 * @author Savio Grossi <savio.grossi@powerlogic.com.br>
 *
 */


@QPlcDefault
public class PlcServletRequestUtil {

	@Inject
	private transient Logger log;
	
	public PlcServletRequestUtil() { 
		
	}

	/**
	 * Veja getParameter(HttpServletRequest request, String propriedade, String charsetName).
	 * Usa o default "UTF-8" no charsetName;
	 * 
	 * @param request Instancia de HttpServletRequest
	 * @param propriedade Propriedade que se deseja obter o valor 
	 * @return O valor da propriedade requisita, decodificada com o charset UTF-8.
	 */
	public String getParameter(HttpServletRequest request, String propriedade) {
		return getParameter(request, propriedade, "UTF-8");
	}

	/**
	 * A especificação Servlet não definie qual o charset deve user utilizado para "decodificar" um
	 * parametro passado via URL em requisições GET ("query param"). O charset default usado nesta 
	 * ocasiao pode variar de container para contaneir (Ex: o Tomcat utiliza ISO-8859-1 por default, 
	 * já o Websphere utiliza UTF-8. E, além disso, a especificação Servlet também não permite se 
	 * escolha, programaticamente, o charset usado na decodificação de uma parametro via metodo 
	 * HttpServletRequest.getParameter(). 
	 * 
	 * Este método desta classe Helper implementa esta comportamento, permitindo que se informe 
	 * qual o charset a ser utilizado no momento de decodificação de um parametro ("query param") 
	 * passado via GET. 
	 * 
	 * Veja PlcServletRequestUtilTest para verificar em detalhes os comportamentos previstos
	 * para este metodo.
	 * 
	 * @param request Instancia de HttpServletRequest
	 * @param propriedade Propriedade que se deseja obter o valor 
	 * @param charsetName O charset (nome) utilizado para decodificar a URL / query string
	 * @return O valor da propriedade requisita, decodificada com o charset informado.
	 */
	public String getParameter(HttpServletRequest request, String propriedade, String charsetName) {
		if (propriedade == null) {
			throw new NullPointerException();
		}
		String paramValue = request.getParameter(propriedade);
		String queryString = request.getQueryString();
		
		//Se não existe queryString ou queryString é vazia ou requisicao é via POST, então nada por ser feito
		if (queryString != null) {
			
			//Se a propriedade requisitada existe na queryString
			if (queryString.contains(propriedade+"=")) {

				// Decodifica a queryString com o charsetName solicitados
				try {
					queryString = URLDecoder.decode( queryString , charsetName );
				} catch (UnsupportedEncodingException e) {
					log.warn( e.getLocalizedMessage());
				}
				
				// Se paramValue obtido via getParameter() nao coincide com queryString decodificada
				if (! queryString.contains("="+paramValue) ) {
					// Entao codificacao do container nao coincide com a codificacao da URL. Deve ser obtidada da queryString
					paramValue = geQueryStringParameterValue(propriedade, queryString);
				}
			}
		}
		
		return paramValue;
	}

	public String getServletPath(HttpServletRequest request) {
		String path = request.getServletPath();
		if (StringUtils.isEmpty(path)) {
			path = request.getRequestURI();
			if (path != null && path.startsWith(request.getContextPath())) {
				path = path.substring(request.getContextPath().length());
			}
		}
		return path;
	}

	/**
	 * 
	 * @param paramName
	 * @param queryString
	 * @return
	 */
	private String geQueryStringParameterValue(String paramName, String queryString) {
        Matcher matcher = Pattern.compile(paramName + "=([^&]*)").matcher(queryString);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
	}
}
