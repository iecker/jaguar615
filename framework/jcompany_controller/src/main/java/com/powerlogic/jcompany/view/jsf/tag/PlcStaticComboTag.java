/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.view.jsf.tag;

/**
 * Especialização da tag base PlcComboDinamicoTag para permitir IoC e DI nos componentes JSF/Trinidad.
 *
 * @Descricao Renderiza um campo em forma de lista, para escolha de dados. A lista é Estática, com base em um java.lang.Enum!
 * @Exemplo <plcf:comboEstatico id="estadoCivil" value="#{plcEntidade.estadoCivil}" dominio="EstadoCivil"  obrigatorio="S" exibeBranco="S" />!
 * @Tag comboEstatico!
 */
public class PlcStaticComboTag extends PlcDynamicComboTag{

}
