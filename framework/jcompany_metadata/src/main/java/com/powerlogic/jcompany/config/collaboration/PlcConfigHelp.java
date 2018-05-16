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
 * Configurações globais de definição de opções de Ajuda On-Line
 * 
 */
//TODO - Implentar o padrão de Ajuda para facelets
public @interface PlcConfigHelp {

    /**
     * Url da página (html, jsp, etc.) que irá conter ajuda nos conceitos do sistema.
     */
    String urlHelpConcept() default "";
    
    /**
     * Url da página (html, jsp, etc.) que irá conter ajuda nos termos envolvidos no sistema.
     */
    String urlHelpGlossary() default "";
    
    /**
     * Url da página (html, jsp, etc.) que irá conter ajuda na operação do sistema.
     */	
    String urlHelpOperation() default "";

    /**
     * Colocar o link completo ou relativo para o módulo de FAQ do Projeto para aparecar na documentação. 
     */
    String groupwareFAQ() default "";

    /**
     * Colocar o link completo ou relativo para o módulo de Fórum do Projeto para aparecar na documentação. 
     */
    String groupwareForum() default "";
    
    /**
     * Colocar o link completo ou relativo para o módulo de HelpDesk do Projeto para aparecar na documentação. 
     */
    String groupwareHelpDesk() default "";
  }
