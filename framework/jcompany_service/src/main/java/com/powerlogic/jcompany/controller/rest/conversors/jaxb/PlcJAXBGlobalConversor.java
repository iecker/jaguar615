/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */
package com.powerlogic.jcompany.controller.rest.conversors.jaxb;

import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcConversorMediaType;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcConversor;

@SPlcConversor
@QPlcConversorMediaType({ "application/atom+xml" })
public abstract class PlcJAXBGlobalConversor<C> extends PlcJAXBBaseConversor<C> {

}
