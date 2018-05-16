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
@PlcMetaConfig(root=true, scope={Scope.EMP, Scope.APP}, layer=Layer.CONTROLLER)
@PlcMetaEditor(label="Aparência", description="Configurações globais de definição de aparencia da aplicação")
/**
 * @since jCompany 5.0
 * Configurações globais de definição de aparencia da aplicação
 */
public @interface PlcConfigLookAndFeel {
	
	public enum FormLookAndFeel {
		/**
		 * Botões com texto somente
		 */
		ONLY_TEXT,
		/**
		 * Botões com texto e ícone (default)
		 */
		TEXT_AND_IMAGE,
		/**
		 * Imagem somente, com ícones pequenos (fornecidos pelo jCompany). Ficam em '/res-plc/midia/[icodir]/'
		 */
		ONLY_IMAGE,
		/**
		 * Imagem somente, com ícones grandes (não fornecidos pelo jCompany). Devem ser criados e ficar em '/res-plc/midia/[icodir]/g/'
		 */
		ONLY_LARGE_IMAGE;
		
		public String toString() {
			
			if (this.equals(ONLY_IMAGE))
				return "I";
			else if (this.equals(TEXT_AND_IMAGE))
				return "A";
			else if (this.equals(ONLY_TEXT))
				return "T";
			else if (this.equals(ONLY_LARGE_IMAGE))
				return "G";
			else
				return "A";
		}
		
	}
	
	/**
	 * Define a pele utilizada  
	 */
	@PlcMetaEditorParameter(label="Pele", description="Pele da Aplicação")
	String skin() default "itunes";
	
	// TODO - PlcConfigAparencia - peleFixa não está sendo utilizado
	@PlcMetaEditorParameter(label="Pele fixa (sem personalização)", description="Define se a pele padrão deve ser fixa (sem personalização), mesmo que recebendo cookies com outra configuração")
	boolean pinnedSkin() default false;
	
	/**
	 * Define o layout dos menus
	 */
	@PlcMetaEditorParameter(label="Layout", description="Layout dos menus")
	String layout() default "sistema";
	
	// TODO - PlcConfigAparencia - layoutFixo não está sendo utilizado
	@PlcMetaEditorParameter(label="Leiaute fixo (sem personalização)", description="Define se o leiaute padrão deve ser fixo (sem personalização), mesmo que recebendo cookies com outra configuração")
	boolean pinnedLayout() default false;
	
	/**
	 * Define o estilo dos botões de ação
	 */
	@PlcMetaEditorParameter(label="Modo exibição para os Botões de Ação", description="Define se é para exibir texto ou imagem ou ambos, como estilo dos botões de ação")
	FormLookAndFeel formLookAndFeel() default FormLookAndFeel.TEXT_AND_IMAGE;

	/**
	 * Define se o caso de uso vai utilizar um título específico, ou seja não segue o padrão do jCompany
	 */
	// TODO - Implementar tituloActionEspecifico para facelets
	@PlcMetaEditorParameter(label="Título Específico para Caso de Uso", description="Define se o caso de uso vai utilizar um título específico, ou seja não segue o padrão do jCompany")
	boolean specificTitle() default true;
	
	 /**
     * Classe que permite a customização da opção genérica de personalização de formulário
     */
	// TODO - PlcConfigAparencia - aparenciaFormulario não está sendo utilizado
	@PlcMetaEditorParameter(label="Personalização de Formulário", description="Permite ligar ou desligar opções de personalização de formulário")
    PlcConfigLookAndFeelForm lookAndFeelForm() default @PlcConfigLookAndFeelForm;
	
   /**
    * Classe que permite a customização da opção genérica de personalização de pele
    */
   @PlcMetaEditorParameter(label="Relação de Peles", description="Permite relacionar as peles possíveis para a aplicação. O default são todas as existentes")
   String[] customizedSkin() default {"itunes","azul","brasil","pergaminho","terra","oceano","preto_branco","moeda"};
   
   /**
    * Classe que permite a customização da opção genérica de personalização de pele
    */
   @PlcMetaEditorParameter(label="Relação de Leiaute", description="Permite relacionar os leiautes possíveis para a aplicação. O default são todos os existentes")
   String[] customizedLayout() default {"sistema","sistema_reduzido","classico","classico_reduzido","oriental","oriental_reduzido",
	   						"classico_dinamico","classico_dinamico_reduzido","oriental_dinamico","oriental_dinamico_reduzido"};

}
