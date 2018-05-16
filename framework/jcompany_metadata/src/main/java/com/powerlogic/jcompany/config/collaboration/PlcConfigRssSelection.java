/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.config.collaboration;

import static java.lang.annotation.ElementType.PACKAGE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PlcConfigRssSelection {

	/**
	 * Titulo para o RSS
	 */
	String title() default "";
	
	/**
	 * Descrição para o RSS 
	 */
	String description() default "";
	
	/**
	 * Utilizado para montagem do link de cada item do RSS
	 */
	String actionMan() default "";
	
	/**
	 * Campos que deverão aparecer na descrição de cada item do RSS
	 */
	String [] fields() default {};
	
	/**
	 * Informando se deve utilizar rss
	 */
	boolean useRss() default true;
}
