/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.config.persistence;

import static java.lang.annotation.ElementType.PACKAGE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.powerlogic.jcompany.config.metadata.PlcMetaConfig;
import com.powerlogic.jcompany.config.metadata.PlcMetaEditor;
import com.powerlogic.jcompany.config.metadata.PlcMetaEditorParameter;
import com.powerlogic.jcompany.config.metadata.PlcMetaConfig.Layer;
import com.powerlogic.jcompany.config.metadata.PlcMetaConfig.Scope;

@Documented
@Target(PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
@PlcMetaConfig(root=true, scope={Scope.APP, Scope.EMP}, layer=Layer.PERSISTENCE)
@PlcMetaEditor(label="Persistência", description="Configurações globais da camada de persistência")
/**
 * @since jCompany 6.0
 * Configurações globais da camade de persistência.
 */
public @interface PlcConfigPersistence {

	/**
	 * Prefixo de Constraint (tipicamente Foreign Key) utilizada. Importante para emitir mensagens personalizadas quando ocorrer erro de ConstraintViolationException
	 */
	@PlcMetaEditorParameter(label="Prefixo Constraint", description="Prefixo de Constraint, tipicamente Foreign Keys utilizada. Importante para emitir mensagens personalizadas quando ocorrer erro de ConstraintViolationException", length=30)
	String constraintPrefix() default "FK_";
}
