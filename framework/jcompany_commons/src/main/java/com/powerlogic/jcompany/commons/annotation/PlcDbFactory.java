/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * jCompany 3.0 Anotação utilizada em classes de gerenciamento de sessões de persistência e também em VOs.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface PlcDbFactory {
	/**
	 * @since jCompany 3.0
	 *  Fábrica a ser utilizada. Equivale ao fabricaPlc registrado no construtor na versão 2.7.x
	 */
	String nome() default "default";
	/**
	 * @since jCompany 3.0
	 * Se usa deteção de dialeto automatica ou nao
	 */
	boolean autoDetectDialect() default true;

}
