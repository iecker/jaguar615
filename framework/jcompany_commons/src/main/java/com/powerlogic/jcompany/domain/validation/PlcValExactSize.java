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
 * @since jCompany5 Validação invariável para uma propriedade que deva possuir exatamente um único tamanho. 
 * Aprimora o uso de duas validações de tamanho mínimo e máximo, melhorando a mensagem de erro.
 */
@Documented
@Constraint(validatedBy = PlcValExactSizeValidator.class)
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface PlcValExactSize {
	int size();
	String message() default "{validator.tamanho.exato}";
	
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};	
}
