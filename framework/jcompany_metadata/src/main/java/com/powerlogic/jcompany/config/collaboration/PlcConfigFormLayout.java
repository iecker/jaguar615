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
 * Configurações para definição de opçoes de layout
 */
public @interface PlcConfigFormLayout {
	
	/**
	 * Em Facelets, o layout universal é default com URL /f/l/ ou /f/d/. E o prefixo já é assumido como /WEB-INF/fcls. Se desejar outro local ele pode 
	 * simplesmente usar /f/<meudir>/minhaPagina.xhtml, referenciando o layout desejado na pagina.
	 */
	String dirBase() default "";
	
}
