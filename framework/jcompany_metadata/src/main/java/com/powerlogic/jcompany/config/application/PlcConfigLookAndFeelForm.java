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
@PlcMetaEditor(label="Formulário", description="Configurações de opções para personalização de formulário")
/**
 * @since jCompany 5.1
 * Configurações globais de definição de aparencia da aplicação
 */
// TODO - PlcConfigAparenciaFormulario não está sendo utilizado
public @interface PlcConfigLookAndFeelForm {
	
	/**
	 * Indica se é permitido ao usuário personalizar a paleta de ícones da barra de ações. Informar um estilo para fixar. Ex: 'icoElegante'"
	 */
	@PlcMetaEditorParameter(label="Personaliza Botão de Ações", 
	description="Indica se é permitido ao usuário personalizar a barra de ações.")
	boolean actionBar() default true;
	
	/**
	 * Indica se é permitido ao usuário personalizar alerta em saida de formulários, quando alterados
	 */
	@PlcMetaEditorParameter(label="Alerta de Saída sem Gravação", description="Indica se é permitido ao usuário personalizar o uso ou não de alerta na saída de formulários")
	boolean updateWarning() default true;
	
	/**
	 * Indica se é permitido ao usuário personalizar alerta ao excluir detalhes
	 */
	@PlcMetaEditorParameter(label="Alerta Gravação com Exclusão de detalhes/subdetalhes", description="Indica se é permitido ao usuário personalizar o uso ou não de alerta ao excluir detalhes/subdetalhes")
	boolean deleteDetailWarning() default false;

	
}
