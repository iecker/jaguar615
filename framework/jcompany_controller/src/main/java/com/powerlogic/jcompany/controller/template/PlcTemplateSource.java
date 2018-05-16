/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.template;

import java.io.IOException;

/**
 * @author Adolfo Jr.
 */
public abstract class PlcTemplateSource {

	private String name;

	public PlcTemplateSource(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public abstract CharSequence getContent() throws IOException;

	public abstract boolean isValidSource();
}
