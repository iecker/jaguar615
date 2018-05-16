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
import java.util.Comparator;

import com.powerlogic.jcompany.config.collaboration.PlcConfigForm.ExclusionMode;

@Documented
@Target(PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
/**
 * @since jCompany 3.2.1 Configurações para definição de entidades de sub-detalhe (detalhes de detalhes) ou, em linguagem UML, composições de
 * composições com multiplicidade 1..* (1:N)
 */
public @interface PlcConfigSubDetail {

    /**
     * Define a entidade de subdetalhe
     */
    Class clazz() default Object.class;
    
    /**
     * Define o número padrão de novos objetos de subdetalhe que serão criados no momento da criação da entidade. Implica indiretamente que
     * o formulário irá apresentar este número de linhas em branco, por default.
     */
    int numNew() default 2;
    
    /**
     * Define o nome da propriedade no detalhe onde está a coleção de subdetalhes
     */
    String collectionName() default "";
    
    /**
     * Define as regras de multiplicidade (também chamada cardinalidade) para o relacionamento detalhe-subdetalhe. O jCompany já cuida
     * de manter esta integridade.
     */
 //   String multiplicity() default "0..*";
    
    /**
     * Permite a definição de uma classe que implementa o padrão comparator  para ordenação de sub-detalhes, quando os mesmos não puderem
     * ser ordenados no momento de sua recuperação da persistência (Ex: order by JPA) 
     */
    Class<? extends Comparator> comparator() default Comparator.class;
    
    /**
	 * Define a flag desprezar específica por subdetalhe
	 * @return
	 */
	String despiseProperty() default "";
	
	/**
	 * Define o teste de duplicidade nos itens do subdetalhe
	 * @return
	 */
	boolean testDuplicity() default true;
	
	/**
	 * Define como o jCompany vai processar a exclusão de uma agregação. Quando for LOGICA a exclusão é modificada para na verdade 
	 * alterar a situação de uma propriedade padrão denominada "sitHistoricoPlc", de "A" (de Ativo) para "I" (de Inativo). 
	 * Para o usuário, isso ocorre como se o item tivesse sido excluído fisicamente. Não somente a exclusão, mas também 
	 * as lógicas genéricas afetadas tais como a recuperação de itens para seleção, verificações de integridade de chave (naoDeveExistir), 
	 * dentre outras, respeitam esta configuração também modificando seu comportamento de acordo com esta configuração. 
	 * @see ExclusionMode 
	 */
	ExclusionMode exclusionMode() default ExclusionMode.FISICAL;

}
