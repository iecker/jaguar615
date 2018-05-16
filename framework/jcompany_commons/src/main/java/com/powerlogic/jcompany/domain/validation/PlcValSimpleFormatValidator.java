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

import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.domain.validation.PlcValSimpleFormat.SimpleFormat;

/**
 * Valida valor texto segundo formato simples, considerando ok se chegou com null ou ""
 */
public class PlcValSimpleFormatValidator implements ConstraintValidator<PlcValSimpleFormat, Object>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SimpleFormat simpleFormat;
	
	public boolean isValid(Object valorInformado, ConstraintValidatorContext constraintValidatorContext) {

		if (valorInformado == null || "".equals(valorInformado.toString().trim())) {
			return true;
		}

		if (simpleFormat.equals(SimpleFormat.UPPER_CASE)) {
			
			return valorInformado.equals(valorInformado.toString().toUpperCase());
				
		} else if (simpleFormat.equals(SimpleFormat.LOWER_CASE)) {
			
			return valorInformado.equals(valorInformado.toString().toLowerCase());
			
		} else if (simpleFormat.equals(SimpleFormat.ALPHABETIC)) {
			
			return StringUtils.isAlpha(valorInformado.toString());
			
		} else if (simpleFormat.equals(SimpleFormat.CAPITALIZE)) {
			
			return valorInformado.equals(StringUtils.capitalize(valorInformado.toString()));
			
		} else {
			// NUMBER
			return StringUtils.isNumeric(valorInformado.toString());
		} 

	}

	public void initialize(PlcValSimpleFormat parameters) {
		simpleFormat = parameters.format();
	}

}
