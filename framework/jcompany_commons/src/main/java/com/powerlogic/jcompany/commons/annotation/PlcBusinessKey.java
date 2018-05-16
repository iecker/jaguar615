/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @since jCompany 3.0
 *  Permite anotação indicando que relação de propriedades que compõem
 * a identificação de negócio (chave natural, alternativa ao OID). Valores usados
 * para comparações (verificação de operação de inclusão x alteração), testes de duplicidade automatizados,
 * etc. Importante:Não deve ser utilizado com mapeamento com chave natural, que neste caso já tem 
 * as chaves naturais separadas em uma classe, de onde as propriedades podem ser inferidas.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PlcBusinessKey {
	String[] props() default "id";
}
