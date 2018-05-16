/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.config.collaboration;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
/**
 * Opções da lógica TABULAR 
 */
public @interface PlcConfigTabular {

	/**
	 * Define a propriedade utilizada para ordenar a tabela no formulário
	 */
	// TODO - Colocar como configuração no layout e retirar do controle ?
	String orderProp() 			default "";

	/**
	 * Define o numero de novos registros do formulário
	 */
	// TODO - Colocar como configuração no layout e retirar do controle ?
	int numNew() 						default 1;
}
