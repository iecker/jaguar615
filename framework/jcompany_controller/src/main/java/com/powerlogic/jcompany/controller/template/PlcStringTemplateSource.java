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
public class PlcStringTemplateSource extends PlcTemplateSource {

	private String source;

	public PlcStringTemplateSource(String name, String source) {
		super(name);
		this.source = source;
	}

	public CharSequence getContent() throws IOException {
		return source;
	}

	public boolean isValidSource() {
		return true;
	}
}
