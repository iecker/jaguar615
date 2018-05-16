/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.config.metadata;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
/**
 * @since jCompany 5.1
 * Metadados sobre uma anotação para editor visual do jCompany IDE
 */
public @interface PlcMetaEditor {
	/**
	 * Rótulo para exibição no editor visual
	 */
	String label();
	
	/**
	 * Descrição da anotação/configuração
	 */
	String description() default "";


}
