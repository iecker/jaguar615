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
 * jCompany 3.0 Permite anotação em classe indicando que, em qualquer Caso de Uso onde
 * apareça como classe principal, a exclusão ocorra logicamente alterando-se 'sitHistoricoPlc' para 'I'.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PlcLogicalExclusion {
	
	/**
	 * Se é para o jCompany incluir sitHistoricoPlc='A' automaticamente em Named Queries Padrão 'naoDeveExistir'
	 */
	boolean automatizaNaoDeveExistir() default true;
	
	/**
	 * Se é para o jCompany incluir sitHistoricoPlc='A' automaticamente em Named Queries Padrão 'querySel'
	 */
	boolean automatizaQuerySel() default false;
	
	/**
	 * Se é para o jCompany incluir sitHistoricoPlc='A' automaticamente em Named Queries Padrão 'queryEdita'
	 */
	boolean automatizaEdicao() default true;
	
	/**
	 * Se é para o jCompany incluir sitHistoricoPlc='A' automaticamente em Named Queries Padrão 'queryTreeView'
	 */
	boolean automatizaTreeView() default true;
	
}
