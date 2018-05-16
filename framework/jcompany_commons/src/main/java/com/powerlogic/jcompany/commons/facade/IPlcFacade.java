/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.facade;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.powerlogic.jcompany.commons.IPlcFile;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.config.aggregation.PlcConfigDetail;

/**
 * jCompany Façade. 
 * 
 * Interface com a Camada Modelo
 *
 * Interface que serve de "contrato" entre a camada de controle e a de modelo, deste
 * modo provendo um isolamento simples e permitindo a codificação com segurança de
 * diversas "implementações" de camada modelo para a mesma camada controle.
 *
 * O jCompany possui duas implementações desta interface, uma com gravações para a
 * framework de persistência Hibernate e outra de Simulação de Persitência, que faz
 * a gravação em memória RAM e em arquivo convencional
 * 
 */
public interface IPlcFacade {

    /**
  	 * Grava um Value Object e seus detalhes, em uma única transação.
  	 * A implementação desta operação deve gravar o Value Object recebido em algum mecanismo
  	 * de persistência (SGBD, LDAP, File System, RAM, etc.), para posterior recuperação.
  	 *
  	 * @param VO Value Object a ser gravado
  	 * @param VOAnterior Value Object anterior (o jCompany mantém em sessão a última versão
  	 *                   recuperada do Value Object, para possibilitar seu envio juntamente
  	 *                   com as modificações nesta operação. Deste modo, pode-se fazer lógicas
  	 *                   que considerem a alteração efetuada ou de auditoria, com imagens de
  	 *                   dados antes e depois das alterações).<br>
  	 *                   Importante: Se for inclusão ou exclusão este objeto é enviado como NULL.
  	 *
  	 * @return PlcEntityInstance Value Object com os dados gravados, similar ao enviado para
  	 *                   gravação, porém contendo chave gerada (caso seja gerada pela persistência)
  	 *                   e informações que podem ter sido complementadas em lógicas do negócio
  	 *                   na implementação da persistência.<br>
  	 *                   Importante: Todos os dados que necessitam de ser retornados após uma
  	 *                   gravação devem estar contidos (agregados) no Value Object de retorno.
  	 *
  	 *  Exceções devem ser tratadas e retornadas como uma PlcException, no padrão do jCompany, para tratamento genérico e exibição para usuário.
  	 * @since jCompany.
  	 */
	public Object saveObject(PlcBaseContextVO context, Object VO)	;
   
	/**
 	 * Grava Coleção de Objetos (Lógica Tabular) em uma única transação.
 	 *
 	 * A implementação desta operação deve gravar uma coleção (List) de Value Objects
 	 * em algum mecanismo de persistência (SGBD, LDAP, File System, RAM, etc.), p
 	 * para posterior recuperação.
 	 *
 	 * Na implementação do jCompany, esta operação é implementada para lógicas de manutenção
 	 * tabular, somente, que são coleções "top-level". <br>
 	 * Importante: Muito embora lógicas mestre-detalhe contenham coleções de
 	 * detalhes para um Mestre, estas lógicas são gravadas através da operação gravaObjeto, pois
 	 * estão agregadas a um Value Object mestre.
 	 *
 	 * @param lista Lista de Value Objects a serem gravados
 	 * @param listaAnt Lista de Value Objects anteriores (o jCompany mantém em sessão a última
 	 *                   versão
 	 *                   recuperada da lista de Value Objects, para possibilitar seu envio juntamente
 	 *                   com as modificações nesta operação. Deste modo, pode-se fazer lógicas
 	 *                   que considerem a alteração efetuada ou de auditoria, com imagens de
 	 *                   dados antes e depois das alterações).<br>
 	 *
 	 *  Exceções devem ser tratadas e retornadas como uma PlcException, no
 	 *                   padrão do jCompany, para tratamento genérico e exibição para usuário.
 	 * @since jCompany.
 	 */
   	public void saveTabular(PlcBaseContextVO context,Class classe,List lista) ;

     /**
  	 * Exclui um Value Object e todas as classes agregadas (detalhes) mapeados
  	 *         como cascade="true", a partir de sua chave primária <P>
  	 *
  	 * @param vo Value Object da classe específica, descendente de PlcEntityInstance, a ser excluido.
  	 *
  	 *  Exceções devem ser tratadas e retornadas como uma PlcException, no
  	 *                   padrão do jCompany, para tratamento genérico e exibição para usuário.
  	 *                   Uma exceção deve ser disparada caso a ocorrência esperada não seja encontrada.
  	 *                 (tipicamente devido ao filtro de segurança ou concorrência entre usuários)
  	 * @since jCompany.
  	 */
   	public void deleteObject(PlcBaseContextVO context, Object vo) ;

  	/**
	 * Recupera um Value Object e todas as suas classes agregadas (ex: listas
	 *        one-to-many em detalhes de lógica Mestre-Detalhe, classes many-to-one), a partir
	 *        de sua chave primária <P>
	 *
	 * @return Object[0]-VO Recuperado, Object[1]-Modificações quando em aprovação,
	 * Object[2] List[] Lista de coleções para classes lookup em navegação, se existirem
	 *
	 *  Exceções são tratadas e retornadas como uma PlcException, no
	 *                   padrão do jCompany, para tratamento genérico e exibição para usuário.
	 *                   Uma exceção deve ser disparada caso a ocorrência esperada não seja encontrada.
	 *                 (tipicamente devido ao filtro de segurança ou concorrência entre usuários)
	 * @since jCompany.
	 */
   	public Object[] edit(PlcBaseContextVO context,Class classe, Object id) ;
   
	/**
	 * Recupera mestre com os detalhes já paginados
	 * @param detalhesPaginados 
	 * @throws InstantiationException 
	 */
   	public Object[] findObjectPagedDetail(PlcBaseContextVO context, Class classe, Object id, Long posAtual, String ordenacaoPlc, PlcConfigDetail ... detalhes) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException, InstantiationException;
    

   	/**
   	 * Recuperação do Arquivo Anexado
   	 * @param context
   	 * @param classe
   	 * @param id
   	 * @return
   	 */
   	public IPlcFile downloadFile(PlcBaseContextVO context, Class classe, Object id) ;

	/**
	 * Recupera uma lista de VO's para a classe informada
   	 * @param context
   	 * @param entidadeArg
   	 * @param orderByDinamico
   	 * @param primeiraLinha
   	 * @param maximoLinhas
   	 * @return
   	 */
    public Collection findList(PlcBaseContextVO context,Object entidadeArg,String orderByDinamico, int primeiraLinha, int maximoLinhas) ;

    /**
	 * Recupera listas simples de todos os objetos de classes lookup, de uso abrangente (tipicamente em combos) por toda a aplicação.
	 *
	 * Esta operação pode ser chamada no início da aplicação, para as classes declaradas no web.xml como classes de lookup, ou a partir de comandos do usuario<p>
	 *
	 * Importante 1: Esta operação não deve ser utilizada para tabelas com muitos registros.<br>
	 * Importante 2: Tabelas básicas, por default, não têm filtro de segurança.<P>
	 *
	 * A implementação desta operação deve recuperar uma lista de Value Objects do tipo informado concatenando a cláusula where e order by informadas.
	 *
	 * @param context Contexto com informações - Por se tratar de Classe StateLess 
	 * @param classe Classe dos Value Objects a serem recuperados, incluindo package completo.<br> Exemplo: "com.empresa.app.vo.TipoCurso"
	 * @param orderByDinamico (Opcional, "" não ordena) Cláusula de ordenação, devendo conter o alias "obj"<br>
	 *               Exemplo: "order by obj.tipo".
	 *       Importante: Como o Design Pattern do jCompany para lógicas de manutenção tabular
	 *       inclui a existência de pelo menos uma propriedade "nome", a ordenação por esta
	 *       propriedade é assumida por default nas lógicas genéricas.
	 *
	 *
	 *  Exceções devem ser tratadas e retornadas como uma PlcException, no padrão do jCompany, para tratamento genérico e exibição para usuário.
	 * @since jCompany 3.0
	 */
	public Collection findSimpleList(PlcBaseContextVO contextParam,Class classe, String orderByDinamico) ;

	/**
     * Recupera objetos de classes com ligação many-to-one com uma classe origem informada,
     * desde que a identificacao da classe origem seja OID
	 * @param context Contexto com informações - Por se tratar de Classe StateLess 
     * @param classeDestinoPlc classe dos objetos a serem recuperados
     * @param classeOrigemPlc classe cuja classeDestinoPlc enxerga como many-to-one
     * @param pk Identificador da instência da classe Origem que servirá de base para a recuperação
     * @param context Parâmetros gerais da sessão e contexto de importância para a camada Modelo.
     * @return Coleção de instancias do tipo de classeDestino que se ligam à instância de classeOrigem com OID "oid".
     * @since jCompany 2.7.3
     **/
    public Collection findNavigation(PlcBaseContextVO context,Class classeOrigemPlc, Object pk,Class classeDestinoPlc) ;
    
    /**
     * Recupera lista de exploração a partir de uma classe e OID
	 * @param context Contexto com informações - Por se tratar de Classe StateLess 
     * @param classe Classe-Base 
     * @param id Identificador (até esta versão somente OID)
     * @return Lista valores de detalhamento para o objeto
     * @since jCompany 3.0.3
     */
	public Collection findListTreeView(PlcBaseContextVO context, Class classeBase, Object id, Class classeFilha,long posIni) ;
 
    /**
     * Recupera lista de registro de um detalhe paginado.
     * 
	 * @param context Contexto com informações - Por se tratar de Classe StateLess 
     * @param configDetalhe
     * @param entityPlc
     * @param entidadeMestre
     * @param numPorPagina
     * @param context
     * @param plx
     * @param ordenacaoPlc
     * @param listaArgumentos
     * @param posicaoAtual
     * @return
     * @throws NoSuchFieldException
     * 
     **/
	public Collection findListPagedDetail(PlcBaseContextVO context, PlcConfigDetail configDetalhe, Object entidadeMestre, int numPorPagina, String ordenacaoPlc, int posicaoAtual, boolean incluiArgPai) throws NoSuchFieldException;

	/**
	 * Idem recuperaTotal mas recebendo POJOs de argumento em lugar da classe.(Mais flexível)
	 * @param context
	 * @param entidadeArg
	 * @return
	 * @since jCompany 3.0
	 */
	public Long findCount(PlcBaseContextVO context, Object entidadeArg) ;

	/**
	 * Recupera classes de lookup many-to-one (agregados), utilizando qualquer propriedade da Entidade
	 * Utiliza um mapa de Propriedades Valores para recuperar medianete várias propriedades. Apóio à Chave Natural
	 * @return Instância da Entidade de lookup
	 * @param context
	 * @param baseVO
	 * @param propriedadesValores
	 * @return
	 * @since jCompany 5.1
	 */
	public Object findAggregateLookup(PlcBaseContextVO context,Object baseVO,Map<String, Object> propriedadesValores) ;	
	
	/**
	 * Recupera registro de mensagens tratadas de erros da camada de persistencia
	 * @param causaRaiz Exception
	 * @return String[0] mensagem internacionalizada, String[1]: arg1 (opcional), String[2] arg2 (opcional)
	 * @since jCompany 3.0 
	 */
	public Object[] findExceptionMessage(Throwable causaRaiz);
	
	/**
	 * Faz a chamada do Bean Validation para entidade
	 * @param entityPlc
	 * @param groups
	 */
	public void checkBeanValidation(Object entityPlc, Class<?>... groups);
	
	
	/**
	 * Faz a chamada do Bean Validation para a lista de entidades
	 * @param entityListPlc
	 * @param groups
	 */
	public void checkBeanValidation(List entityListPlc, Class<?>... groups);
	
}
