/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.jsf.producer;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.controller.injectionpoint.HttpParam;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;

/**
 * Classe responsável para criação de componentes para weld
 * 
 * @author Moisés Paula, Rogério Baldini
 */
@Named("plcHttpParamProducer")
@ConversationScoped
public class PlcHttpParamProducer implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	@QPlcDefault
	protected PlcContextUtil contextUtil;
	
	/**
	 * Para campos anotado com @HttpParam, irá procurar o parametro informado em
	 * request param Ex: @Inject @HttpParam ("id") String id;
	 */
	@Produces
	@HttpParam
	public String getRequestParam(InjectionPoint ip) {

		HttpServletRequest request = (HttpServletRequest) contextUtil.getRequest();
		HttpParam q = ip.getAnnotated().getAnnotation(HttpParam.class);
		String value = request.getParameter(q.value());
		return value;
	}

}
