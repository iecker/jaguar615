/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.extension.tabular.metadata;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.powerlogic.jcompany.config.metadata.PlcMetaConfig;
import com.powerlogic.jcompany.config.metadata.PlcMetaEditor;
import com.powerlogic.jcompany.config.metadata.PlcMetaConfig.Layer;
import com.powerlogic.jcompany.config.metadata.PlcMetaConfig.Scope;


@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@PlcMetaConfig(root = true, scope = Scope.APP, layer = Layer.COMMONS)
@PlcMetaEditor(label = "Consulta Tabular", description = "Configurações complementares para Tabular Somente Consulta")

/**
 * 
 * Configurações globais de definição de logica tabular somente consulta
 * @author igor.guimaraes
 *
 */
public @interface PlcConfigEditOnlyTabular {
	
	boolean isReadOnlyTabular() default true;
	
}
