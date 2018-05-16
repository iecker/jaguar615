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
 * @since jCompany 5.0 Configurações para definição de componentes (padrão JPA) que participam de 
 * uma agregação de entidades gerenciada pelo jCompany
 */
public @interface PlcConfigComponent {

    /**
     * Classe que representa o componente 
     */
    Class clazz() default Object.class;

    /**
     * Nome da propriedade do componente na Entidade principal/raiz
     */
    String property() default "";

    /**
     * Indica que o componente será renderizado separado do mestre, em uma aba própria.
     * Essa configuração é utilizada quando o layout é tabFolder.
     */
    boolean separate() default true;
    
    
}
