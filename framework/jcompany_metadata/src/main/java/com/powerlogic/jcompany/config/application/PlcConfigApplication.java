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

import com.powerlogic.jcompany.config.collaboration.PlcConfigBehavior;
import com.powerlogic.jcompany.config.metadata.PlcMetaConfig;
import com.powerlogic.jcompany.config.metadata.PlcMetaEditor;
import com.powerlogic.jcompany.config.metadata.PlcMetaEditorParameter;
import com.powerlogic.jcompany.config.metadata.PlcMetaConfig.Layer;
import com.powerlogic.jcompany.config.metadata.PlcMetaConfig.Scope;

@Documented
@Target(PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
@PlcMetaConfig(root=true, scope=Scope.APP, layer=Layer.CONTROLLER)
@PlcMetaEditor(label="Controle Aplicação", description="Configurações para escopo de Aplicação (globais para a aplicação)")
/**
 * @since jCompany 5.0
 * Configurações para escopo de Aplicação (globais para a aplicação)
 */
public @interface PlcConfigApplication {

    /**
     * Define as opções principais da aplicação
     */
	@PlcMetaEditorParameter(label="Definição", description="Opções principais da aplicação")
    PlcConfigApplicationDefinition definition() default @PlcConfigApplicationDefinition;
    
    /**
     * Define as opções de internacionalização da aplicação 
     */
	@PlcMetaEditorParameter(label="Internacionalização", description="Opções de internacionalização da aplicação")
    PlcConfigApplicationI18n i18n()  default @PlcConfigApplicationI18n;
    
    /**
     * Define os módulos utilizados na aplicação.
     */
	@PlcMetaEditorParameter(label="Módulos", description="Módulos utilizados na aplicação")
    PlcConfigModule[] modules() default {};

    /**
     * Define a lista de classes lookup da aplicação
     */
    @PlcMetaEditorParameter(label="Classes Lookup", description="Lista de classes lookup da aplicação")
    Class[] classesLookup() default {};

    /**
     * Defini a ordenação de cada classes lookup, as classes que não deve ter ordenação, utilizar <code>""</code>.
     * <p>
     * Exemplo:
     * classesLookup = {com.emp.app.entidade.EntidadeA.class, com.emp.app.entidade.EntidadeB.class},
     * classesLookupOrderBy = {"", "nome asc"}
     *
     * @return Ordenação das classes de lookup.
     * @see #classesLookup()
     */
    @PlcMetaEditorParameter(label="Ordenação das Classes Lookup", description="Ordenação da lista de classes lookup da aplicação")
    String[] classesLookupOrderBy() default {};

    /**
     * Define a lista de classes de domínio discreto da aplicação
     */
    @PlcMetaEditorParameter(label="Classes de Domínio Discreto", description="Lista de classes de domínio discreto da aplicação")
    Class<? extends Enum>[] classesDiscreteDomain() default {};
    
    /**
     * Indica chave da conta do Google Analytics
     */
    String googleAnalyticsKey() default  "";  
    
    /**
     * Indica verifica esquema automaticamente
     */
    @PlcMetaEditorParameter(label="Indica se verifica esquema", description="Se for verdadeiro a aplicacao, ao entrar, fara uma verificao de sincronia de esquema de SGBD")
    boolean checkSchema() default false;
    
}
