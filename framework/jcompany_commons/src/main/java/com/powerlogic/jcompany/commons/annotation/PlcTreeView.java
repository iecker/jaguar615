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
 * Meta-dados utilizados na lógicas de Explorador (Navegador em dados utilizando ergonomia do Windows-Explorer)
 * @since jCompany 3.03
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PlcTreeView {
	
	/**
	 * Se a classe corrente deve ser utilizada pelo explorador (o default é utilizar; se marcada para false, então a árvore do 
	 * explorador irá omitir a classe do tipo corrente)
	 * @since jCompany 3.03
	 */
	boolean usa() default true;
	
	/**
	 * Relação de classes a serem omitidas, separadas por vírgula e sem incluir o package (pressupõe-se que não haverá mais de um
	 * relacionamento desta classe com duas outras homônimas e em packages diferentes). O default é utilizar todas as classes
	 * que possuam relacionamento "many-to-one" para esta classe.
	 * @since jCompany 3.03
	 */
	String omiteClasses() default "";

}
