/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.config.collaboration;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.powerlogic.jcompany.config.metadata.PlcMetaConfig;
import com.powerlogic.jcompany.config.metadata.PlcMetaConfig.Layer;
import com.powerlogic.jcompany.config.metadata.PlcMetaConfig.Scope;
import com.powerlogic.jcompany.config.metadata.PlcMetaEditor;
import com.powerlogic.jcompany.config.metadata.PlcMetaEditorParameter;


@Documented
@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
@PlcMetaConfig(root=true, scope={Scope.MAN, Scope.SEL}, layer=Layer.CONTROLLER)
@PlcMetaEditor(label="Camada de Controle", description="Configurações da Camada Controle para escopo de Colaboração")
/**
 * @since jCompany 6. Metadados para padrões de formulários
 */
public @interface PlcConfigForm {
	
	/**
	 * Opções de exclusão que podem ser utilizadas em padrões primários de manutenção.
	 */
	public enum ExclusionMode {
		/**
		 * A exclusão é implementada através da remoção efetiva do objeto (exclusão da tupla da tabela relacional, para SGBD-Rs) 
		 */
		FISICAL,
		/**
		 * Exclusao somente altera os objetos do SGBD-R para 'situação de inativo', seguindo o padrão:<P>
		 * sitHistoricoPlc='A' (Ativo)<br>
		 * sitHistoricoPlc='I' (Inativo)<p>
		 * O sitHistoricoPlc pode ser mapeado para qualquer coluna String.
		 */
		LOGICAL;
		
	}
	
	/**
	 * Indica ligeiras variações sobre a colaboração padrão, especialmente CRUDTABULAR. Com este recursos o jCompany facilita
	 * futuras "pequenas variações" em padrões, por exemplo, para espelhar níveis de complexidade.
	 */
	public enum FormPatternModality {
		/**
		 * Forma padrão se omitido
		 */
		A,
		/**
		 * Primeira variação
		 */
		B,
		/**
		 * Segunda variação
		 */
		C
	}
	
	/**
	 * Define o padrão a ser utilizado na colaboração (formulário)
	 */
	FormPattern formPattern() default FormPattern.Ctl;
	
	/**
	 * Define as propriedades a serem utilizadas no cabeçalho de formulários CRUDTABULAR 
	 */
	String[] ctbHeaderProperties() default {};

	/**
	 * Define um novo padrão definido em um Extension (exige que a colaboracaoPadrao seja PLX)
	 */	
	String collaborationPatternPlx() default "";

	/**
	 * Define como o jCompany vai processar um exclusão
	 * @see ExclusionMode 
	 */
	ExclusionMode exclusionMode() default ExclusionMode.FISICAL;
	/**
	 * Define o tipo de variação da colaboração 
	 * @see UseCaseModality
	 */
	FormPatternModality modality() default FormPatternModality.A;
	
    /**
     * Configurações para definição de opçoes de layout 
     */
	@PlcMetaEditorParameter(label="Layout Universal", description="Opções de layout da Colaboração")
    PlcConfigFormLayout formLayout() default @PlcConfigFormLayout;
    
    /**
     * Configurações de definição de opções de Ajuda On-Line
     */
	@PlcMetaEditorParameter(label="Ajuda", description="Opções de Ajuda On-Line")
    PlcConfigHelp help() default @PlcConfigHelp;
    
    /**
     * Configurações globais para impressão
     */
	@PlcMetaEditorParameter(label="Impressão", description="Configurações para impressão")
    PlcConfigPrint print() default @PlcConfigPrint;
    
    /**
     * Configurações globais de definição de comportamentos padrões da aplicação
     */
	@PlcMetaEditorParameter(label="Comportamento", description="Definição de comportamentos padrões da aplicação")
    PlcConfigBehavior behavior() default @PlcConfigBehavior;

    /**
     * Configuração para validação programática da colaboração.
     */
    // TODO - 6.0 - Refatorar mecanistmo de validacao programática 
    @PlcMetaEditorParameter(label="Validação Programática", description="Configuração para validação programática da colaboração")
    PlcConfigProgrammaticValidation[] programmaticValidation() default{};
    
    /**
     * Configurações para definição opções da lógica TABULAR 
     */
    @PlcMetaEditorParameter(label="Tabular", description="Configurações para lógica Tabular")
    PlcConfigTabular tabular() default @PlcConfigTabular;
    
    /**
     * Configurações para definição de opções específicas da lógica RELATORIO
     */
    @PlcMetaEditorParameter(label="Relatório", description="Configurações para lógicas de Relatório")
    PlcConfigReport report() default @PlcConfigReport;
    
    /**
     * Configurações para definição de Combo Aninhados
     */
    // TODO - 6.0 - Implementar mecanismo de combo aninhado 
    @PlcMetaEditorParameter(label="Combos Aninhados", description="Definições de Combo Aninhados")
    PlcConfigNestedCombo[] nestedCombo() default @PlcConfigNestedCombo;
    
    /**
     * Configurações para definição de Combo Aninhados
     */
    // TODO - Implementar mecanismo de rss na seleção 
    @PlcMetaEditorParameter(label="Configuração RSS", description="Configuração para utilização de RSS para esta seleção")
    PlcConfigRssSelection rssSelection() default @PlcConfigRssSelection;
    
    /**
     * Configurações para definição de opções de exibição de mapas do Google Map
     */
    @PlcMetaEditorParameter(label="Google Map", description="Opções de exibição de mapas do Google Map")
    PlcConfigGoogleMap googleMap() default @PlcConfigGoogleMap;
    
    /**
     * Configurações para lógica de seleção
     */
    @PlcMetaEditorParameter(label="Seleção", description="Configurações para lógica Seleção")
    PlcConfigSelection selection() default @PlcConfigSelection;
    

}
