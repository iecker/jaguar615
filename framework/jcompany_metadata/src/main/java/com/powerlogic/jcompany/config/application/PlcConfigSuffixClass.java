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
@PlcMetaEditor(label="Sufixos", description="Define preferências globais para nomenclatura de sufixos de classes padroes do framework.")
/**
 * @since jCompany 5.0 
 * Permite anotações que definem preferências globais para nomenclatura de sufixos de classes padroes do framework.
 */
public @interface PlcConfigSuffixClass {

	/**
	 * Sufixo-padrão para descendentes concretos de Entidades (Classes de Domínio Mapeadas). Default é "VO"
	 */
	@PlcMetaEditorParameter(label="Entidade", description="Sufixo-padrão para descendentes concretos de Entidades (Classes de Domínio Mapeadas)", length=15)
	String entity() default "Entity";
	
	/**
	 * Sufixo-padrão para classes de serviço (ou gerenciadoras) de Entidades (Classes de Domínio Mapeadas) na camada Modelo. Default é "BO"
	 */
	@PlcMetaEditorParameter(label="Gerente", description="Sufixo-padrão para classes de serviço (ou gerenciadoras) de Entidades (Classes de Domínio Mapeadas) na camada Modelo", length=15)
	String repository() default "Repository";
	
	/**
	 * Sufixo-padrão para classes de persistência. Default é "DAO")
	 */
	@PlcMetaEditorParameter(label="Persistência", description="Sufixo-padrão para classes de persistência", length=15)
	String persistence() default "DAO";
	
	/**
	 * Sufixo-padrão para classes de controle. Default é "MB")
	 */
	@PlcMetaEditorParameter(label="Controle", description="Sufixo-padrão para classes de controle", length=15)
	String control() default "MB";

}
