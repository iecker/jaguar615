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
 * Configurações globais de definição de comportamentos padrões da aplicação
 */
public @interface PlcConfigBehavior {

	/**
	 * Indica para o jCompany salvar o detalhe anterior para ser utilizado como informação adcional na lógica
	 */
	boolean rememberDetail() default  false;

	/**
	 * Indica se a aplicação deve informar ao usuário que uma tela editada e não gravada esta sendo "abandonada".
	 */
	boolean useUpdateWarning() default  true;

	/**
	 * Indica se a aplicação deve informar ao usuário que uma tela existem detalhes e/ou subdetalhe marcados para excluir e quando esta gravando um registro.
	 */
	boolean useDeleteDetailWarning() default  false;

	/**
	 * Indica que após a gravação de um registro, um novo registro será criado para edição
	 */
	boolean batchInput() default  false;

	/**
	 * Indica que a colaboração corrente (url) irá permitir ativação do explorador universal
	 */
	boolean useTreeView() default false;


	/**
	 * <b>modoJanela </b>: Indica modos para exibição da janela (layout)
	 * desta lógica
	 */
	String windowMode() default "";

	/**
	 * Indica se faz validação de dados na exclusão. 
	 */
	boolean deleteValidationUse() default false;




}

