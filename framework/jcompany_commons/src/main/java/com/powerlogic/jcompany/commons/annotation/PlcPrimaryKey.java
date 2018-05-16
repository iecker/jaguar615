/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.annotation;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
/**
 * @since jCompany 5.0
 * Meta-dado para identificação da chave primária composta da entidade.
 * @author Bruno Grossi
 */
public @interface PlcPrimaryKey {
	/**
	 * Define a classe da Entidade que possui a chave composta 
	 */
	Class classe();
	/**
	 *  Define a lista das propriedas que montam a chave composta
	 */
	String[] propriedades();
	/**
	 * 
	 */
	boolean autoRecuperacao() default false;
}
