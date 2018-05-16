/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.rest.api.qualifiers.literals;

import javax.enterprise.util.AnnotationLiteral;

import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerQualifier;

public class QPlcControllerQualifierLiteral extends AnnotationLiteral<QPlcControllerQualifier> implements QPlcControllerQualifier {
	
	private static final long serialVersionUID = 1L;

	public static final String CONTROLE_QUALIFICADOR_TODOS = "*";
	
	private String nome;
	
	public QPlcControllerQualifierLiteral(String nome) {
		this.nome = nome;
	}
	
    @Override
    public String value() {
        return nome;
    }
}
