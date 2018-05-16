/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */
package com.powerlogic.jcompany.controller.rest.producers;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.facade.IPlcFacade;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcCurrent;
import com.powerlogic.jcompany.controller.util.PlcIocControllerFacadeUtil;

@RequestScoped
public class PlcFacadeProducer {

	@Produces
	@QPlcCurrent
	@RequestScoped
	public IPlcFacade getFacade(@QPlcDefault PlcIocControllerFacadeUtil controllerFacadeUtil) {
		return controllerFacadeUtil.getFacade();
	}
}
