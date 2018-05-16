/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.domain.validation;

import java.io.Serializable;
import java.util.regex.Matcher;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validador de máscara especializado para alteração de mensagem do original, somente.
 * @since jCompany 3.2
 */
public class PlcValMaskValidator implements ConstraintValidator<PlcValMask, String>, Serializable {

	private java.util.regex.Pattern mascara;

	public void initialize(PlcValMask parameters) {
		mascara = java.util.regex.Pattern.compile(
				parameters.regex(),
				parameters.flags()
		);
	}
	
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
		if ( value == null ) return true;
		if ( !( value instanceof String ) ) return false;
		String string = (String) value;
		Matcher m = mascara.matcher( string );
		return m.matches();
	}
	
}
