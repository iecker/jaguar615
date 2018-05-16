/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.config.aggregation;

import static java.lang.annotation.ElementType.PACKAGE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
/**
 * @since jCompany 5.0 Configurações de metadados para definição de classes descendentes em um mapeamento O-R de herança, participante em
 * uma agregação de entidades a ser gerenciada pelo jCompany
 */
public @interface PlcConfigDescendent {

	/**
	 * Define a entidade descendente em uma definição de herança.
	 */
	Class clazz() default Object.class;
	
}
