/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.config.aggregation;

import static java.lang.annotation.ElementType.PACKAGE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
/**
 * @since jCompany 5.0 Configurações de metadados para definição de opções relacionadas à navegação em instâncias de objetos de detalhes. 
 * Deve ser utilizada quando o total de objetos possíveis em uma classe de detalhe (composição 1:N com a entidade raiz/mestre) for muito
 * grande e não se deseja recuperar todas de uma única vez.
 */
public @interface PlcConfigPagedDetail {

	/**
	 * Indica ligeiras variações sobre o mecanismo padrão
	 */
	public enum DynamicType {
		/**
		 * Navegador pré-definido em termos de número de objetos e somente permitindo paginação sequencial
		 */
		STATIC,
		/**
		 * Permite ao usuário saltar direto para qualquer página
		 */
		DYNAMIC_PAGE,
		/**
		 * Permite ao usuário alterar o número de registros.
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
	 * Define o número de registros por página
	 */
	int numberByPage() default -1;
	/**
	 * Define o tipo de variação da lógica do navegador
	 */
	DynamicType dynamicType() default DynamicType.STATIC;
	/**
	 * Define estilo de exibição do navegador. <br/> Obs.: muito embora este metadado tenha relação com apresentação, ele é mantido aqui
	 *  para coesão (evitar que o desenvolvedor tenha que configurar isso em vários lugares, como também no componente visual ou
	 *  camada de aplicação/controle.
	 */
	TopStyle topStyle() default TopStyle.ARROWS;
	/**
	 * Define posicionamento do topo com relação aos registros exibidos. <br/> Obs.: muito embora este metadado tenha relação com apresentação, ele é mantido aqui
	 *  para coesão (evitar que o desenvolvedor tenha que configurar isso em vários lugares, como também no componente visual ou
	 *  camada de aplicação/controle.
	 */
	TopPosition topPosition() default TopPosition.ABOVE;
	
}
