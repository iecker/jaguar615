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


@Documented
@Constraint(validatedBy = PlcValMultiplicityValidator.class)
@Target({METHOD, FIELD})
@Retention(RUNTIME)
/**
 * Define número mínimo e máximo de ítens filhos possíveis para uma associação
 */
public @interface PlcValMultiplicity {
	
	/**
	 * Máximo de objetos válidos presentes na coleção
	 */
	int max() default Integer.MAX_VALUE;

	/**
	 * Mínimo de objetos válidos presentes na coleção
	 */
	int min() default 0;
	
	/**
	 * Propriedade de referência quando se estiver utilizado desprezo de linhas. 
	 * Objetos sem chave informada e sem esta propriedade informada serão desprezados
	 */
	String referenceProperty();

	String message() default "{validator.cardinalidade}";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};	

}

