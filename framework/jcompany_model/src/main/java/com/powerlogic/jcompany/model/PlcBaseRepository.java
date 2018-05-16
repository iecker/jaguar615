/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.model;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcSpecific;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcRepository;

/**
 * Base para serviços ou objetos de negócio.<p>
 * @since jCompany 3.0 Segundo 'Core J2EE Design Patterns': Um Businesss Object (BO) é um tipo de Business Component que implementa
 * as regras de granuralidade mais fina do modelo de domínio, podendo ser chamado de outros BOs, Application Services (ASs)
 * ou diretamente de Façades. Normalmente chama outros BOs ou DataAcessObjects (DAOs), para serviços de dados.
 * <p>
 * O sufixo Repository é preferido a partir do jCompany 6 (Jaguar 6) pois esta classe traz serviços de apoio às entidade de domínio, 
 * tipicamente intermediando a manutenção de seu ciclo de vida, chamando os DAOs. Ver conceito de Repository na teoria de Domain Driven-Design.
 */
@SPlcRepository
@QPlcDefault
public class PlcBaseRepository   {

	@Inject @QPlcSpecific
	protected PlcBaseEditRepository baseRetrieve;
	
	@Inject @QPlcSpecific
	protected PlcBaseDeleteRepository baseDelete;
	
	@Inject @QPlcSpecific
	protected PlcBaseUpdateRepository baseUpdate;
	
	@Inject @QPlcSpecific
	protected PlcBaseCreateRepository baseCreate;

	@Inject @QPlcSpecific
	protected PlcBaseFilterDefRepository baseFilterDef;
	
	/**
	 * Repository responsável pela auditoria da entidade
	 */
	@Inject @QPlcSpecific
	protected PlcBaseAuditingRepository baseAuditoria;

	/**
	 * Recupera uma coleção de Entidades
	 * @param classe Classe da entidade Raiz da Agregação
	 * @param orderyByDinamico Ordenação dinamica via jqGRID
	 * @param primeiraLinha Primeira linha para paginação
	 * @param maximoLinhas Máximo de linhas para paginação
	 * @return List<Object> contendo Entidades
	 */
	public List findList(PlcBaseContextVO context, Class classe, String orderyByDinamico, int primeiraLinha, int maximoLinhas)  {
		return baseRetrieve.editList(context, classe, orderyByDinamico, primeiraLinha, maximoLinhas);
	}

	/**
	 * Recupera uma coleção de Entidades
	 * @param entidade Entidade Raiz da Agregação
	 * @param orderyByDinamico Ordenação dinamica via jqGRID
	 * @param primeiraLinha Primeira linha para paginação
	 * @param maximoLinhas Máximo de linhas para paginação
	 * @return List<Object> contendo Entidades
	 */
	public List findList(PlcBaseContextVO context, Object entidade, String orderyByDinamico, int primeiraLinha, int maximoLinhas)  {
		return baseRetrieve.findList(context, entidade, orderyByDinamico, primeiraLinha, maximoLinhas);
	}
	
	/**
	 * Insere (persiste) uma Agregação de Entidades
	 * @param entidade Entidade raiz da agregação (pode ter detalhes, componentes, referencias manyToOne, etc.)
	 * @return referencia à entiadde recém-persistida
	 * TODO Retirar throws.
	 */
	public Object insert( PlcBaseContextVO context, Object entidade) throws PlcException, Exception {
		return baseCreate.insert(context, entidade);
	}	
	
	/**
	 * Atualiza (persiste) modificações em uma Agregação de Entidades já existente
	 * @param entidade Entidade raiz da agregação (pode ter detalhes, componentes, referencias manyToOne, etc.)
	 * @return referencia à entidade recém-persistida
	 */
	public Object update( PlcBaseContextVO context, Object entidade)  {
		return baseUpdate.update(context, entidade);
	}

	/**
	 * Exclui uma Agregação de Entidades
	 * @param entidade Entidade raiz da agregação (pode ter detalhes, componentes, referencias manyToOne, etc.)
	 */
	public void delete(PlcBaseContextVO context, Object entidade)  {
		baseDelete.delete(context, entidade);
	}

	/**
	 * Recupera uma Agregação de Entidades para Edição/Alteração
	 * @param classe Classe da entidade raiz da agregação (pode ter detalhes, componentes, referencias manyToOne, etc.)
	 * @param id Object-Id
	 * @return 0. Entidade raiz da agregação recuperada; 1. Object-Id
	 */
	public Object[] edit (PlcBaseContextVO context, Class classe, Object id)  {
		return baseRetrieve.edit(context, classe, id);
	}	
	
	/**
	 * TODO Homologar que nao esta sendo usado e remover
	 *
	public Object findAggregateLookup( Object entidade, String nomePropriedade, Object valor)  {
		return baseRetrieve.findAggregateLookup(entidade, nomePropriedade, valor);
	}
	*/
	
	/**
	 * Recupera lista de entidades simples (lookup) tipicamente para popular combos, usando opção de metadados
	 */
	public Object findAggregateLookup(PlcBaseContextVO context, Object entidade, Map<String, Object> propriedadesValores) {
		return baseRetrieve.editLookup(context, entidade, propriedadesValores);
	}	
	
	/**
	 * Recupera lista de entidades simples partindo de uma referencia em classe de origem (tipicamente usado para combos aninhados)
	 * @param classeOrigem Classe da Entidade que possui a referencia 
	 * @param pk Object-Id do objeto de origem	
	 * @param classeDestino Classe de destino a ser recuperada
	 * @return List de objetos do tipo da Classe de Destino
	 */
	public List findNavigationAggregate(PlcBaseContextVO context, Class classeOrigem, Object pk, Class classeDestino)  {
		return baseRetrieve.editNavigation(context, classeOrigem, pk, classeDestino);
	}
	
	/**
	 * Método administrativo utilizado pelo jCompany para recuperar definições de Filtros Verticais
	 * @param classeDAOouVO Classe a ser investigada sobre a existencia de Filtros
	 * @return Lista de Filtros
	 */
	public List<String> findFilterDefs(PlcBaseContextVO context, Class entity)  {
		return baseFilterDef.findFilterDefs(context, entity);
	}
	
	/**
	 * Recupera lista de entidades de modo recursivo, tipicamente para Treeview
	 * @param classeBase Classe Pai
	 * @param id Chave do objeto Pai
	 * @param classeFilha Classe do nível inferior
	 * @param posIni 
	 * @return Lista de entidades do tipo da Classe Filha
	 */
	public List findListTreeView(PlcBaseContextVO context, Class classeBase, Object id,Class classeFilha,long posIni)  {
		return baseRetrieve.findListTreeView(context, classeBase, id, classeFilha, posIni);
	}	

	/**
	 * Recupera total de objetos a serem recuperados segundo os argumentos informados no parametro
	 * @param entidadeArg Entidade preenchida com argumentos de filtragem
	 * @return Total de objetos que seriam recuperados com estes critérios
	 */
	public Long findCount(PlcBaseContextVO context, Object entidadeArg) {
		return baseRetrieve.findCount(context, entidadeArg);
	}

	/**
	 * Retorna o 'repository' responsável por recuperar entidades.
	 * @return repository do tipo PlcBaseRetrieveRepository
	 */
	public PlcBaseEditRepository getBaseRetrieve() {
		return baseRetrieve;
	}

	/**
	 * Retorna o 'repository' responsável por apagar entidades.
	 * @return repository do tipo PlcBaseDeleteRepository
	 */
	public PlcBaseDeleteRepository getBaseDelete() {
		return baseDelete;
	}

	/**
	 * Retorna o 'repository' responsável por atualizar entidades.
	 * @return repository do tipo PlcBaseUpdateRepository
	 */
	public PlcBaseUpdateRepository getBaseUpdate() {
		return baseUpdate;
	}

	/**
	 * Retorna o 'repository' responsável por criar entidades.
	 * @return repository do tipo PlcBaseCreateRepository
	 */
	public PlcBaseCreateRepository getBaseCreate() {
		return baseCreate;
	}

	/**
	 * Retorna o 'repository' responsável por definiões de filtros.
	 * @return repository do tipo PlcBaseFilterDefRepository
	 */
	public PlcBaseFilterDefRepository getBaseFilterDef() {
		return baseFilterDef;
	}

	/**
	 * Retorna o 'repository' responsável por auditoria da entidade.
	 * @return repository do tipo PlcBaseAuditingRepository
	 */
	public PlcBaseAuditingRepository getBaseAuditoria() {
		return baseAuditoria;
	}

}
