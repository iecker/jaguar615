/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Anotação para detalhe e subdetalhe. 
 *
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PlcDetail  {
	
	/**
	 * Se é classe de detalhe com relacionamento ManyToMany (lado secundário)
	 */
	boolean manyToMany() default false;
	
	/**
	 * Nome da propriedade que, quando não informada, faz com que o objeto todo seja desconsiderado nas GUIs.
	 */
	Class classePropRelacionamento() default Object.class;
	
	/**
	 * Se é classe de subdetalhe
	 */
	boolean subDetalhe() default false;
	
	/**
	 * Se o detalhe é somente para leitura
	 */
	boolean somenteLeitura() default false;

}
