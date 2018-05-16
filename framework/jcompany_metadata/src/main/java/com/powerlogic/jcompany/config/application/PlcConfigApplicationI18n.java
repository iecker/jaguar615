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
 * Definições de internacionalização da aplicação
 */
public @interface PlcConfigApplicationI18n {

	/**
	 * Define a lista de idiomas disponíveis
	 */
	String[] languages() default {"pt_BR","en_US","es_ES"};
	/**
	 * Define se a aplicação só pode ser exibida em um único idioma
	 */
	boolean localeUnique() default false;
	/**
	 * Define a lista de arquivos(bundles) com os rótulos e textos da aplicação
	 */
	String[] bundles() default {"ApplicationResources","jCompanyResources","EmpResources"};

}
