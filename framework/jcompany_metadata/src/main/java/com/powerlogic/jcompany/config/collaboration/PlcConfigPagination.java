/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.config.collaboration;

import static java.lang.annotation.ElementType.PACKAGE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
/**
 * @since jCompany 5.0
 * Configurações para definição de opçoes relacionadas as formulários.
 */
// TODO - 6.0 - Passar configuração de navegação para metadados de layout e retirar do controle 
public @interface PlcConfigPagination {

	/**
	 * Indica ligeiras variações sobre a lógica padrão.
	 */
	public enum DynamicType {
		/**
		 * Navegador pré-definido em termos de numero de objetos e somente permitindo paginacao sequencial
		 */
		STATIC,
		/**
		 * Permite usuário saltar direto para qualquer página
		 */
		DYNAMIC_PAGE,
		/**
		 * Permite usuário alterar o núm. de registros.
		 */
		DYNAMIC_REGNUM,
		/**
		 * Permite ao usuário alterar o número de registro e saltar direto para qualquer página
		 */
		DYNAMIC_BOTH
	}
	
	/**
	 * Indica o posicionamento do topo com relação ao registros exibidos.
	 */
	public enum TopPosition {
		/**
		 * Topo abaixo da relação de registro
		 */
		BELOW,
		/**
		 * (Default) Topo acima da relação de registro
		 */
		ABOVE,
		/**
		 * Topo acima e abaixo
		 */
		BOTH
	}
	
	/**
	 * Indica o estilo da IHM do navegador.
	 */
	public enum TopStyle {
		/**
		 * (Default) Setas de navegação
		 */
		ARROWS,
		/**
		 * Links com índices por página
		 */
		INDEXES
	}
	
	/**
	 * Define o numero de registros por página
	 */
	int numberByPage() default 20;
	/**
	 * Define o tipo de variação da lógica do navegador
	 */
	DynamicType dynamicType() default DynamicType.STATIC;
	/**
	 * Define estilo de exibição do navegador
	 */
	TopStyle topStyle() default TopStyle.ARROWS;
	/**
	 * Define posicionamento do topo com relação aos registros exibidos
	 */
	TopPosition topPosition() default TopPosition.ABOVE;
	
}
