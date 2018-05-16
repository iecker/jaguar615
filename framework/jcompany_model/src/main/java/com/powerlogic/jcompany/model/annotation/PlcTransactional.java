/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;

/**
 * jCompany 3.0 Permite anotação indicando que o método deve encerrar com o tratamento
 * genérico do jCompany para encerramento das sessões/transações:<p>
 * Se ocorrer exceção, fecha com rollback<br>
 * Se não, fecha com commit<br>
 * Em qualquer caso, a lógica é disparada via chamadas aos métodos apropriados
 * da camada de persistência, preservando o MVC. As sessões de persistência
 * e conexões do pool também são sempre fechadas/devolvidas, em qualquer caso.
 * @since jCompany 3.0
 */
@InterceptorBinding
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface PlcTransactional {
	/** Fábrica */
	@Nonbinding String fabrica() default "default";
	
	@Nonbinding boolean commit() default true;
}