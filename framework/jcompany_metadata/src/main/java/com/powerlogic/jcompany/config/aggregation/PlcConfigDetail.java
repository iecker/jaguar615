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
import com.powerlogic.jcompany.config.metadata.PlcMetaEditorParameter;

@Documented
@Target(PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
/**
 * @since jCompany 5.0 Configurações de metadados para definição de entidades de detalhe, ou seja, 
 * que participem em agregações como "composições UML 1:N" com relação a uma entidade raiz/principal, neste caso também chamada de "mestre".
 * Uma entidade de "detalhe" se caracteriza por possuir "n" instâncias que referenciam uma entidade "mestre", mantidas em uma mesma transação
 * que esta principal.
 */
public @interface PlcConfigDetail {

    /**
     * Define a entidade de detalhe
     */
    Class clazz() default Object.class;
    
    /**
     * Define o numero padrão de novos detalhes para cada nova agregação do mestre criada (o que no formulário
     *  se reflete em linhas iniciais em branco no detalhe)
     */
    int numNew() default 4;
    
    /**
     * Define o nome da propriedade no mestre onde está a coleção de detalhes 
     */
    String collectionName() default "";
    
    /**
     * Define uma classe que implementa o algoritmo no padrão "comparator", que faz uma comparação entre instâncias para produzir
     * uma ordenação inicial no App Server. Deve-se utilizar somente quando a ordenação em SGBD-R (com order by no querySel) não puder 
     * ser usada, por exemplo, se houver manipulação de impacto após a recuperação. 
     */
    Class<? extends Comparator> comparator() default Comparator.class;

    /**
     * Se informado 'true' o conjunto de detalhes não será recuperado inicialmente, junto com a entidade raiz/mestre. Quando marcado
     * o jCompany implementa um padrão "Lazy MVC", que não somente evita recuperar via JPA (Lazy JPA) mas também cria uma solução completa
     * englobando Web-Design em formulários (apresentação dinâmica de "..." em abas ou componentes expansíveis) e mecanismo Ajax para disparo
     * da recuperação na medida em que o usuário clique nos detalhes, com toda a transação gerenciada. O uso deste recurso pode otimizar
     * consideravelmente a performance quando utilizando grandes formulários com consulta e edição massivas.
     */
    boolean onDemand() default false;
    
    /**
     * Define configurações do navegador (paginação) em detalhe se existir. Em termos da agregação, este metadado define que não 
     * serão recuperados todas as instâncias de detalhes da primeira vez em que se recupera uma entidade raiz/mestre. 
     * Esta recuperação irá então continuar na medida em que o usuário comande a paginação através do formulário.
     */
    PlcConfigPagedDetail navigation() default @PlcConfigPagedDetail;

    /**
     * Define as configurações de entidades de subdetalhe, que são "composições de composições", exercendo um papel de "neto" da
     * entidade/raiz da agregação. Não se recomenda formulários com agregações abaixo deste nível, dada a complexidade para o usuário
     * que uma entrada de dados de sub-subdetalhe exigiria. <br/> É importante notar que o "corte" de agregações (para serem mantidas em
     * diferentes momentos/formulários) é sempre uma opção do projetista, dada a impossibilidade prática do usuário de entrar 
     * como todos os dados de uma estrutura em uma única passada. 
     */
    PlcConfigSubDetail subDetail() default @PlcConfigSubDetail;

	/**
	 * Define como o jCompany vai processar a exclusão de uma agregação. Quando for LOGICA a exclusão é modificada para na verdade 
	 * alterar a situação de uma propriedade padrão denominada "sitHistoricoPlc", de "A" (de Ativo) para "I" (de Inativo). 
	 * Para o usuário, isso ocorre como se o item tivesse sido excluído fisicamente. Não somente a exclusão, mas também 
	 * as lógicas genéricas afetadas tais como a recuperação de itens para seleção, verificações de integridade de chave (naoDeveExistir), 
	 * dentre outras, respeitam esta configuração também modificando seu comportamento de acordo com esta configuração. 
	 * @see ExclusionMode 
	 */
	ExclusionMode exclusionMode() default ExclusionMode.FISICAL;
	
	
	@PlcMetaEditorParameter(label="Componentes", description="Lista de componentes da agregação")
    PlcConfigComponent[] components() default @PlcConfigComponent;

}
