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
 * Configurações para definição de Combo Aninhados
 */
// TODO - Revisar mecanismo de combo aninhado. Possivelmente essa configuração não é mais possível.
public @interface PlcConfigNestedCombo {

	/**
	 * Propriedade inicial(Mestre) da recuperação 
	 */
	String origemProp() default "";
	/**
	 * Propriedade recuperada(detalhe) a partir da seleção da origem(mestre)
	 */
	String destinyProp() default "";
	
	/**
	 * se o combo dinâmico é de um detalhe, informe o nome da coleção referete ao detalhe.
	 * Propriedade OneToMany do mestre que representa o detalhe
	 */
	String detailName() default "";
	
}
