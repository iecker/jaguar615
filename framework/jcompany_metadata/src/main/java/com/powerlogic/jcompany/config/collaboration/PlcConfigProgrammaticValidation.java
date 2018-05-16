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
/**
 * @since jCompany 5.0 
 * Configuração para validação programática da colaboração.
 */
// TODO - Homologar configuração de validação programática
public @interface PlcConfigProgrammaticValidation {
	
	/**
	 * Indica a classe que será utilizada para a validação.
	 * Essa classe deve utilizar anotações Hibernate Validator em métodos sem parâmetros.
	 */
	Class value();

}
