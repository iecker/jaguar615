/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.config.application;

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
@PlcMetaConfig(root=true, scope=Scope.APP, layer=Layer.CONTROLLER)
@PlcMetaEditor(label="Diretórios Estáticos", description="Configurações globais de definição de diretórios estáticos auxiliares a lógicas de download e upload")
/**
 * @since jCompany 3.2.1 
 * Configurações globais de definição de diretórios estáticos auxiliares a lógicas de download e upload
 */
public @interface PlcConfigDirFileDML {

    /**
     * Diretório auxiliar para geração de arquivo de DDL e outros auxiliares
     */
	@PlcMetaEditorParameter(label="Arquivos DML", description="Diretório auxiliar para geração de arquivo de DDL e outros auxiliares")
    String directory() default "c:\\tomcat\\webapps\\root\\ti";
     
}
