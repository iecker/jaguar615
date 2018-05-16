/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.config.application;

import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.TYPE;

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
@Target({TYPE,PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
@PlcMetaConfig(root=true, scope={Scope.APP, Scope.EMP}, layer=Layer.COMMONS)
@PlcMetaEditor(label="Pacotes", description="Configurações globais para pacotes da aplicação")
/**
 * @since jCompany 5.0 
 * Permite anotações que definem preferências globais para pacotes base
 */
public @interface PlcConfigPackage {

	/**
	 * Pacote-base para aplicação 
	 */
	@PlcMetaEditorParameter(label="Pacote Aplicação", description="Pacote para aplicação ", length=15)
	String application() default "";	
	
	/**
	 * Pacote-base para Entidades (Classes de Domínio Mapeadas). Default é ".vo."
	 */
	@PlcMetaEditorParameter(label="Entidade", description="Pacote-base para Entidades (Classes de Domínio Mapeadas)", length=15)
	String entity() default ".entity.";

	/**
	 * Pacote-base para Controladores (Classes de Controle, tais como Action). Default é ".controle."
	 */
	@PlcMetaEditorParameter(label="Controle", description="Pacote-base para Controladores (Classes de Controle, tais como Action)", length=15)
	String control() default ".controller.";

	/**
	 * Pacote-base para BOs e ASx (Classes de Business Components). Default é ".modelo."
	 */
	@PlcMetaEditorParameter(label="Modelo", description="Pacote-base para BOs e ASx (Classes de Business Components)", length=15)
	String model() default ".model.";
	/**
	 * Pacote-base para DAOs (Classes de Persistência). Default é ".persistencia."
	 */
	@PlcMetaEditorParameter(label="Persistência", description="Pacote-base para DAOs (Classes de Persistência)", length=15)
	String persistence() default ".persistence.";
}
