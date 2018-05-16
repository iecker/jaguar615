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
 * @since jCompany5 Validação invariável para CPF. Valida dígito conforme padrão.
 */
@Documented
@Constraint(validatedBy = PlcValCnpjValidator.class)
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface PlcValCnpj {

	String propTitulo() default "###propTituloAutomaticoPlc###";
	
	String message() default "{validator.cnpj}";
	
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};	
}
