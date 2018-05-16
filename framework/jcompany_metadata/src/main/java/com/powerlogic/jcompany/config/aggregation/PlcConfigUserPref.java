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
 * @since jCompany 5.0 Configurações de metadados específicas para definição de opções do padrão de Preferência do Usuário
 */
public @interface PlcConfigUserPref {

    /**
     * Nome da propriedade da classe que persiste as preferências de usuário. Esta propriedade é utilizada para recuperar um registro
     * existente utilizando o login do usuário corrente. Também é onde o jCompany armazena o login, caso seja a primeira vez que o 
     * úsuário registra as preferência.
     */
    String argument() default "login";
}
