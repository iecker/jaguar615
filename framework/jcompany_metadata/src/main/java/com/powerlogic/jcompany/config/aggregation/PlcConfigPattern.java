/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.config.aggregation;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
/**
 * @since jCompany 5.0  Configurações de metadado para definição de formulários que implementam os casos de uso padrões identificados e
 * implementados com alta generalização e reuso no jCompany. <p> 
 * Um padrão de formulário engloba todas as lógicas de programação de cliente (javascript, ajax, ...) 
 * e servidor (controle, fachada, modelo e persistência) realizadas a partir do disparo de todos os botões e links do formulário. <p>
 * 
 * Não confundir com um padrão de Caso de Uso, que é mais conceitual e definido nos Padrões e Métodos da documentação do produto. 
 * O mais comum é que um Caso de Uso Padrão seja implementado por mais de um "Formulário Padrão", que interagem entre si 
 * (ex: manutenção e seleção).
 */
public @interface PlcConfigPattern {
	

}
