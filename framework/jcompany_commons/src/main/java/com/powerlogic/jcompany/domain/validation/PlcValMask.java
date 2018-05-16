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
 * @since jCompany5 Melhora a mensagem da validação de máscara padrão do Hibernate Validator. 
 * Somente acrescenta o atributo de exemplo formatado, que deve finalizar a mensagem.
 */
@Documented
@Constraint(validatedBy = PlcValMaskValidator.class)
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface PlcValMask {
	
	/** regular expression */
	String regex();

	/** regular expression processing flags */
	int flags() default 0;

	String message() default "{validator.pattern}";
	
	String messageExemploFormatado();

	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};	
	
}

