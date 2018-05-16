/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.domain.validation;

import java.io.Serializable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validação para Tamanho exato de propriedades
 */
public class PlcValExactSizeValidator implements ConstraintValidator<PlcValExactSize, String>, Serializable {

	private static final long serialVersionUID = 1L;
	private int size;

	public void initialize(PlcValExactSize parameters) {
		size = parameters.size();
	}

	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
		if ( value == null ) return true;
		if ( !( value instanceof String ) ) return false;
		String string = (String) value;
		int length = string.length();
		return length == size;
	}

}

