/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.domain.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @since jCompany5 Indica exigência de algumas formatações simples, tais como uso somente de números, maiúsculos ou minúsculos.
 */
@Documented
@Constraint(validatedBy = PlcValSimpleFormatValidator.class)
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface PlcValSimpleFormat {
	
	String message() default "{validator.formato.simples}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};	
	
	public enum SimpleFormat {
		/**
		 * Transforma a entrada de dados em maiúsculos
		 */
		UPPER_CASE,
		/**
		 * Transforma a entrada de dados em minúsculos
		 */
		LOWER_CASE,
		/**
		 * Transforma a entrada de dados em capitalizada (iniciais em maiúsculas
		 */
		CAPITALIZE,
		/**
		 * Somente aceita números (0-9)
		 */
		NUMBER, 
		/**
		 * Transforma a entrada de dados em somente alfabéticos
		 */
		ALPHABETIC,
		
		
	}
	
	SimpleFormat format();
	
}

