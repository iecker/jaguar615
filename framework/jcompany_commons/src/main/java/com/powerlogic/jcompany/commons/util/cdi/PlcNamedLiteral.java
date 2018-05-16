/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.commons.util.cdi;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Named;

/**
 * Literal para anotacao javax.inject.Named para permitir uso do tipo
 * <p>
 *  <code>
 *  new NamedLiteral("nome")
 *  </code>
 * </p> 
 * como qualificador.
 * 
 * @author Savio Grossi
 *
 */
public class PlcNamedLiteral extends AnnotationLiteral<Named> implements Named {

	private String name = "";
	
	public PlcNamedLiteral(String name) {
		this.name = name;
	}
	
	public String value() {
		return name;
	}

}
