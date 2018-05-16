/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.rest.api.qualifiers.literals;

import javax.enterprise.util.AnnotationLiteral;

import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcConversorMediaType;

public class QPlcConversorMediaTypeLiteral extends AnnotationLiteral<QPlcConversorMediaType> implements QPlcConversorMediaType {
	
	private static final long serialVersionUID = 1L;

	private String[] mediaType;
	
	public QPlcConversorMediaTypeLiteral(String mediaType) {
		this.mediaType = new String[]{mediaType};
	}
	
    @Override
    public String[] value() {
        return mediaType;
    }

}
