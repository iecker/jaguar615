/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.config.application;

import static java.lang.annotation.ElementType.PACKAGE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
/**
 * @since jCompany 5.0
 * Definições principais da aplicação (nome, versão, etc.)
 */
public @interface PlcConfigApplicationDefinition {

	/**
	 * Define o nome da aplicação
	 */
	String name() default "";
	/**
	 * Define a sigla da aplicação
	 */
	String acronym() default "";
	/**
	 * Define a versão da aplicação
	 */
	int version() default 0;
	/**
	 * Define a release da aplicação
	 */
	int release() default 0;
	
	
}
