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
 * Configurações para lógica de seleção
 */
public @interface PlcConfigSelection {

    /**
     * Define configurações do navegador da seleção se existir. 
     */
    PlcConfigPagination pagination() default @PlcConfigPagination;
    
    /**
     *	Indica a NamedQuery para seleção. 
     */
    String apiQuerySel() default "";
    
    /**
     * Define configurações da Exportação da seleção se existir. 
     */
    PlcConfigExport export() default @PlcConfigExport;

}
