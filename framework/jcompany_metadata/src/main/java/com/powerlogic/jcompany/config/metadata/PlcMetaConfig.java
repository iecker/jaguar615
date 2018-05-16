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
 * Metadados sobre uma anotação de configuração.
 */
public @interface PlcMetaConfig {
	
	public enum Scope {
		EMP,
		APP,
		MAN,
		SEL
	}
	
	public enum Layer {
		COMMONS,
		PERSISTENCE,
		DOMAIN,
		MODEL,
		CONTROLLER,
		VIEW
	}
	
	/**
	 * Indica se a anotação pode ser utilizada como raiz.
	 */
	boolean root() default false;
	
	/**
	 * Escopo da anotação.
	 */
	Scope[] scope();
	
	/**
	 * Camada em que pode ser utilizada.
	 */
	Layer layer();
	
	/**
	 * Descrição da anotação.
	 */
	String description() default "";
}
