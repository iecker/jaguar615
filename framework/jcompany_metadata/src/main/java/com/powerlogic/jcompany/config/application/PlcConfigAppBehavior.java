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
 * Configurações globais de definição de comportamentos padrões da aplicação
 */
public @interface PlcConfigAppBehavior {

	/**
	 * Registra filtro de segurança vertical quando o usuário é anônimo e não passa pela lógica de perfil (profiling) de usuários da autenticação.
	 */
	String anonymousFilter() default "#";

	/**
	 * Indica se será exibida a parte técnica "Mensagem do Sistema:" para as mensagens de erro inesperadas
	 */
	boolean showInternalMessages() default  true;   

	/**
	 * Indica usa pesquisa com URL RESTful
	 */
	// TODO - Verificar como está e/ou implementar pesquisaRestfulUsa
	boolean useRestfulSearch() default  true;
	
}

