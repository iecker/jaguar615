/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.util;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;

/**
 * Classe auxiliar para os variados contextos da aplicação.
 * Busca atributos dos contextos, utilizando o {@link FacesContext}.
 * @author Bruno Grossi
 *
 */
@SPlcUtil
@QPlcDefault
public class PlcContextUtil {
	
 	public PlcContextUtil() { 
 		
 	}
 	
 	/**
 	 * Procura um atributo nos seguintes contextos, nessa ordem: Request, Session e Application.
 	 * @param name nome do atributo.
 	 * @return valor encontrado.
 	 */
	public Object findAttribute(String name) {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Object object = externalContext.getRequestMap().get(name);
		if (object==null) {
			object = externalContext.getSessionMap().get(name);
			if (object==null) {
				object = externalContext.getApplicationMap().get(name);
			}
		}
		
		return object;
	}
	
	/**
	 * Busca parâmetro da requisição.
	 */
	public String getRequestParameter(String name) {
		return getRequest().getParameter(name);
	}
	
	/**
	 * Busca atributo da requisição.
	 */
	public Object getRequestAttribute(String name) {
		return getRequest().getAttribute(name);
	}

	/**
	 * Define atributo da requisição.
	 */
	public void setRequestAttribute(String name, Object value) {
		getRequest().setAttribute(name, value);
	}

	/**
	 * Remove atributo da requisição.
	 */
	public void removeRequestAttribute(String name) {
		getRequest().removeAttribute(name);
	}

	/**
	 * Busca o mapa de atributos da requisição.
	 * Todas as alterações no mapa serão refletidos nos atributos da requisição.
	 * Buscar o requestMap e manipulá-lo várias vezes é mais eficiente que utilizar
	 * várias vezes os métodos {@link #getRequestAttribute(String)} e {@link #setRequestAttribute(String, Object)}.
	 */
	public Map<String, Object> getRequestMap() {
		
		if (FacesContext.getCurrentInstance() != null) {
			return FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
		}

		final HttpServletRequest request = getRequest();

		return new AbstractMap<String, Object>(){
			@Override
			public Object put(String key, Object value) {
				Object old = request.getAttribute(key);
				request.setAttribute(key, value);
				return old;
			}
			@Override
			public Object get(Object key) {
				return request.getAttribute(key != null ? key.toString() : null);
			}
			@Override
			public Set<java.util.Map.Entry<String, Object>> entrySet() {
				throw new IllegalStateException();
			}
		};
	}

	/**
	 * Busca atributo da sessão.
	 */
	public Object getSessionAttribute(String name) {
		return getSessionMap().get(name);
	}

	/**
	 * Define atributo da sessão.
	 */
	public void setSessionAttribute(String name, Object value) {
		getSessionMap().put(name, value);
	}

	/**
	 * Busca o mapa de atributos da sessão.
	 * Todas as alterações no mapa serão refletidos nos atributos da sessão.
	 * Buscar o sessionMap e manipulá-lo várias vezes é mais eficiente que utilizar
	 * várias vezes os métodos {@link #getSessionAttribute(String)} e {@link #setSessionAttribute(String, Object)}.
	 */
	public Map<String, Object> getSessionMap() {
		return FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	}

	/**
	 * Busca atributo da aplicação.
	 */
	public Object getApplicationAttribute(String name) {
		return getApplicationMap().get(name);
	}

	/**
	 * Define atributo da aplicação.
	 */
	public void setApplicationAttribute(String name, Object value) {
		getApplicationMap().put(name, value);
	}

	/**
	 * Busca o mapa de atributos da aplicação.
	 * Todas as alterações no mapa serão refletidos nos atributos da aplicação.
	 * Buscar o applicationMap e manipulá-lo várias vezes é mais eficiente que utilizar
	 * várias vezes os métodos {@link #getApplicationAttribute(String)} e {@link #setApplicationAttribute(String, Object)}.
	 */
	public Map<String, Object> getApplicationMap() {
		return FacesContext.getCurrentInstance().getExternalContext().getApplicationMap();
	}
	
	/**
	 * Busca o objeto {@link HttpServletRequest}, para a requisição atual.
	 */
	public HttpServletRequest getRequest() {
		
		HttpServletRequest request = null;
		
		if (FacesContext.getCurrentInstance()!=null) {
			request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		} else {
			request =  PlcCDIUtil.getInstance().getInstanceByType(HttpServletRequest.class, QPlcDefaultLiteral.INSTANCE);
			if(request == null) {
				request =  PlcCDIUtil.getInstance().getInstanceByType(HttpServletRequest.class);
			}
		}
		
		return request;
	}
	
	/**
	 * Busca o objeto {@link HttpServletResponse}, para a requisição atual.
	 */
	public HttpServletResponse getResponse() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		return (HttpServletResponse) externalContext.getResponse();
	}
	
	/**
	 * Busca o {@link ResponseWriter} JSF para a requisição atual.
	 */
	public ResponseWriter getResponseWriter (){
		return FacesContext.getCurrentInstance().getResponseWriter(); 
	}
	
	/**
	 * Busca o objeto {@link ServletContext}.
	 */
	public ServletContext getApplicationContext(){
		return (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
	}

	/**
	 * Busca o identificador da conversação corrente.
	 */
	public String getCurrentConversationId() {
		return null;
	}
	
	/**
	 * Método auxiliar que retorna uma string passando o primeiro caracter para minusculo
	 */
	public String capitalizeFirstLetter(String nome) {
		if (nome==null||nome.length()==0)
			return nome;
		
        String retorno;

        if (!Character.isLowerCase(nome.charAt(0))) {
            retorno = String.valueOf(Character.toLowerCase(nome.charAt(0))) + nome.substring(1);
        } else {
            retorno = nome;
        }

        return retorno;
    }
	
	/**
	 * Verifica se está num loop, e retorna o indice da linha atual.
	 * @return indice da linha atual, ou nulo se não estiver iterando atualmente 
	 */
	public String getCurrentLineNumber() {
		if (!isLooping())
			return null;
		
		String indiceDetalhe = (String) getRequestAttribute(PlcConstants.PlcJsfConstantes.PLC_ITENS_LINHA);
		if (StringUtils.isBlank(indiceDetalhe)) {
			PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
			indiceDetalhe = (String) elUtil.evaluateExpressionGet("#{"+PlcJsfConstantes.PLC_ITENS_STATUS+".index}", String.class);
		}
		return indiceDetalhe;
	}

	/**
	 * Verifica se está num loop atualmente
	 * @return
	 */
	public boolean isLooping() {
		Map<String, Object> requestMap = getRequestMap();
		return requestMap.containsKey(PlcJsfConstantes.PLC_ITENS_LINHA) || requestMap.containsKey(PlcJsfConstantes.PLC_ITENS_STATUS);
	}

	
	
}
