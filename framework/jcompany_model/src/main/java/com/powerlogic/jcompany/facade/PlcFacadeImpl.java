/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.facade;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.ejb.TransactionAttribute;
import javax.inject.Inject;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.ConstraintViolation;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.IPlcFile;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcConstants.EXCLUSAO;
import com.powerlogic.jcompany.commons.PlcConstants.VO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcPrimaryKey;
import com.powerlogic.jcompany.commons.comparator.PlcComparatorId;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.facade.IPlcFacade;
import com.powerlogic.jcompany.commons.util.PlcAnnotationUtil;
import com.powerlogic.jcompany.commons.util.PlcEntityCommonsUtil;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;
import com.powerlogic.jcompany.commons.util.PlcValidationInvariantUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigDetail;
import com.powerlogic.jcompany.config.aggregation.PlcConfigSubDetail;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm.ExclusionMode;
import com.powerlogic.jcompany.model.PlcBaseRepository;
import com.powerlogic.jcompany.model.annotation.PlcTransactional;
import com.powerlogic.jcompany.model.util.PlcIocModelUtil;
import com.powerlogic.jcompany.persistence.PlcBaseDAO;
import com.powerlogic.jcompany.persistence.jpa.PlcBaseJpaDAO;

/**
 * DP Façade. Implementação da Interface IPlcFacade <p>
 * Importante: 
 * 	 	- Deve fechar sessões de persistencia antes de encerrar <p>
 * 		- Descendentes desta classe não devem conter regras do negócio.
 * Esta classe somente serve de adapter entre a camada de controle e as lógicas do negócio dos Business Componentes: 
 * 		Business Objects (BOs) ou Application Services (ASs).
 * @since jCompany 3.0
 */
public abstract class PlcFacadeImpl implements IPlcFacade {
 
	private static final long serialVersionUID = 1L;

	@Inject
	protected transient Logger log;

	/**
	 * Serviço injetado e gerenciado pelo CDI
	 */
	@Inject @QPlcDefault 
	protected PlcMetamodelUtil metamodelUtil;

	@Inject @QPlcDefault 
	protected PlcAnnotationUtil annotationUtil;
	
	@Inject @QPlcDefault
	protected PlcEntityCommonsUtil entityCommonsUtil;

	@Inject @QPlcDefault 
	protected PlcReflectionUtil reflectionUtil;
	
 	@Inject @QPlcDefault 
 	protected PlcIocModelUtil iocModelUtil;
 	
 	@Inject @QPlcDefault 
 	protected PlcValidationInvariantUtil validationInvariantUtil;
	
	//cria instancia do propertyUtilsBean para evitar diversas chamadas a metodo syncronized
	private static PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
			
	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After"
	 * ou "api" são eventos (método vazios) destinados a especializações nos
	 * descendentes.
	 * <p>
	 * Grava um Value Object e seus detalhes, gerenciando transação segundo com
	 * utilização da Hibernate. Segue o seguinte roteiro:
	 * <p> - beginTransaction(vo.getFabricaPlc()): se estiver usando transação
	 * via Hibernate, inicializa Transação e Sessão.
	 * <p> - verificaHistoricoAprovacao(sess,baseBO, vo, voAnt): se gravação não
	 * for simples (caso de versionamento, aprovação ou reprovação, em lógicas
	 * de workflow) então verifica se irá gerar registros inativos
	 * (sitHistoricoPlc='I') e outros comportamentos pertinentes a estas
	 * operações de gravação especiais.
	 * <p> - se não for gravação de pendência (registro pendente de aprovação ou
	 * reprovação), então entra para verificar qual operação de gravação
	 * realizar:
	 * <p> - baseBO.verificaAcaoAprovacao(sess,vo,voAnt): se for operação de
	 * aprovação ou reprovação em workflow, chama método específico para
	 * considerar as ações<br> - baseBO.inclui(sess,vo): Se for operação de
	 * gravação simples e voAnt for nulo, então assume inclusão<br> -
	 * baseBO.altera(sess,vo,voAnt): Se for operação de gravação simples e voAnt
	 * for informado, então assume alteração
	 * <p> - commit(sess,tx): se tudo ocorreu ok (nenhuma exceção), efetua
	 * fechamento com commit, encerramento de sessão Hibernate e também
	 * devolução de conexão JDBC para pool
	 * <p> - rollback(sess,tx): se alguma exceção foi dispara, efetua fechamento
	 * com rollback, encerramento de sessão Hibernate e também devolução de
	 * conexão JDBC para pool
	 * 
	 * Exceções devem ser tratadas e retornadas como uma PlcException, 
	 * no padrão do jCompany, para tratamento genérico e exibição para usuário.
	 */
	@PlcTransactional
	@TransactionAttribute(javax.ejb.TransactionAttributeType.REQUIRED)
	@Override
	public Object saveObject(PlcBaseContextVO context, Object entidade)  {
		
		//invocando o BV
		checkBeanValidation(entidade);
		
		PlcBaseRepository baseBO = iocModelUtil.getRepository(entidade.getClass());

		try {
			
			// Knoew Issue: Resolvendo o problema do ehAutoRecuperacao, pode retirar este booleano! #110972# Se usuario usou transacaoUnica no vinculado, deve recuperar as propriedades nesta mesma transação
			boolean isAlteracao = false;
			
			if (context != null && context.getMode().equals(PlcBaseContextVO.Mode.ALTERACAO)) {
				isAlteracao = true;
			}
			getAggregateProperty(entidade, isAlteracao);

			// Inclusão e Alteração Padrões
			if (!isAlteracao) {

				baseBO.insert(context, entidade);

			} else {
				entidade = baseBO.update(context, entidade);
			}

			return entidade;
		}catch(PlcException e){
			throw e;
		} catch (Exception e1) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_PERSISTENCE_SAVE, new Object[] {"saveObject", e1 }, e1, log);			
		}

	}

	/**
	 * 
	 * Grava Coleção de Objetos (Lógica Tabular) em uma única transação. <p>
	 * 
	 * Grava uma coleção (top-level collection) de Value Objects segundo o mapeamento OO x SGBD-R da Hibernate
	 * 
	 * É utilizada em lógicas de manutenção tabular, somente, que são coleções
	 * "top-level". Importante: Muito embora lógicas mestre-detalhe contenham
	 * coleções de detalhes para um Mestre, estas lógicas são gravadas através
	 * da operação gravaObjeto, pois estão agregadas a um Value Object mestre.
	 * 
	 * @param context Contexto com informações - Por se tratar de Classe StateLess 
	 * @param lista Lista de Value Objects a serem gravados
	 * 
	 * Exceções devem ser tratadas e retornadas como uma PlcException, no padrão do jCompany, para tratamento genérico e exibição para usuário.
	 * @since jCompany 3.0
	 */
	@PlcTransactional
	@TransactionAttribute(javax.ejb.TransactionAttributeType.REQUIRED)
	@Override
	public void saveTabular(PlcBaseContextVO context, Class classe, List lista)  {

		//invocando o BV
		checkBeanValidation(lista);
		
		PlcBaseRepository baseBO = iocModelUtil.getRepository(classe);

		Object vo = null;
		try {
			
			PlcPrimaryKey chavePrimaria = (PlcPrimaryKey)classe.getAnnotation(PlcPrimaryKey.class);
			if (chavePrimaria != null)
				checkNaturalKeyDuplicated (lista);

			Iterator i = lista.iterator();

			// se lista não está vazia
			while (i.hasNext()) {

					vo = i.next();

					Object[] ret = checkAction(vo, null);
					String oper = (String) ret[0];
					Object plcVoAnt = null;

					if (log.isDebugEnabled()) {
						log.debug( "antes receber vo ant com oper=" + oper);
					}
					
					if (ret[1] != null) {
						plcVoAnt = ret[1];
					}

					if (oper.equals("E")) {
						
						if (log.isDebugEnabled()){
							log.debug( "Decidiu excluir para " + vo);
						}
						
						// Verifica se é exclusão lógica
						if (ExclusionMode.LOGICAL.toString().equals(context.getDeleteModeAux()) || annotationUtil.isLogicalExclusion(vo.getClass())) {
							log.debug( "Entrou para exclusao logica");
							entityCommonsUtil.updateSitHistoricoPlc(context, vo,VO.SIT_INATIVO, false);
							baseBO.update(context, vo);
						} else
							baseBO.delete(context, vo);

					} else if (oper.equals("I")) {
						if (log.isDebugEnabled()){
							log.debug( "Decidiu incluir para " + vo);
						}
						
						if (EXCLUSAO.MODO_LOGICA.equals(context.getDeleteModeAux()) || annotationUtil.isLogicalExclusion(vo.getClass())) {
							log.debug( "Entrou para inclusao logica");
							entityCommonsUtil.updateSitHistoricoPlc(context, vo,VO.SIT_ATIVO, false);
						}
						
						baseBO.insert(context, vo);
						
					} else if (oper.equals("A")) {
						
						if (log.isDebugEnabled()) {
							log.debug( "Decidiu alterar para " + vo);
						}
					
						baseBO.update(context, vo);
					}

					log.debug( "passou loop gravacao");

			}
		}catch(PlcException plcE){
			throw plcE;
		} catch (Exception e1) {			
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_PERSISTENCE_SAVE, new Object[] {"saveTabular", e1 }, e1, log);			
		}

	}

	/**
	 * Verifica duplicidade somente para chave natural 
	 */
	protected void checkNaturalKeyDuplicated(List lista)  {
		for (Iterator iterator = lista.iterator(); iterator.hasNext();) {
			Object vo = iterator.next();
			PlcEntityInstance voInstance = metamodelUtil.createEntityInstance(vo);
			for (Iterator iterator2 = lista.iterator(); iterator2.hasNext();) {
				Object vo2 = iterator2.next();
				PlcEntityInstance vo2Instance = metamodelUtil.createEntityInstance(vo2);
				if (vo != vo2 && voInstance.equalsChaveNatural(vo2Instance)) {
					throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_PERSISTENCE_DUPLICATED_ID, new Object [] {}, false);
				}
			}
		}
	}


	/**
	 * 
	 * Recupera listas simples, que são tabelas básicas com poucos registros e de uso abrangente (tipicamente combos) 
	 * por toda a aplicação. A implementação deve conter lógica de caching para estas tabelas, "thread-safe" e homologada para clusters <P>
	 * 
	 * Esta operação é chamada no início de cada sessão, para as classes declaradas no web.xml como classes de lookup. <p>
	 * 
	 * Importante: Esta operação não deve ser utilizada para tabelas com muitos registros.<br>
	 * @since jCompany 1.0 . 
	 */
	@PlcTransactional(commit=false)
	@TransactionAttribute(javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
	@Override
	public Collection findSimpleList(PlcBaseContextVO context, Class classe,String orderByDinamico)  {

		try {
			
			Collection listaRecuperada = iocModelUtil.getPersistenceObject(classe).findAll(context, classe, orderByDinamico);			

			return listaRecuperada;

		}catch(PlcException e){
			throw e;
		} catch (Exception e) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_PERSISTENCE_FIND_SIMPLE_LIST, new Object[] {"findSimpleList", classe.getName(), e }, e, log);
		}

	}

	/**
	 * Recupera Lista de VOs trazendo páginas, a partir de argumentos informados
	 * @since jCompany 3.0
	 */
	@PlcTransactional(commit=false)
	@TransactionAttribute(javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
	@Override
	public Collection findList(PlcBaseContextVO context, Object entidadeArg, String orderByDinamico, int primeiraLinha, int maximoLinhas)  {

		try {
			
			/** 
			 * Invocando o BV passando o grupo padrão de argumentos
			 */
			// checkBeanValidation(entidadeArg, PlcValGroupArgument.class);
			 
			
			Collection listaRecuperada = iocModelUtil.getRepository(entidadeArg.getClass()).findList(context, entidadeArg, orderByDinamico, primeiraLinha, maximoLinhas);

			return listaRecuperada;

		}catch(PlcException ePlc){
			throw ePlc;
		} catch (Exception e) {
			throw new PlcException("PlcFacadeImpl", "findList", e, log, "");
		}

	}	
	

	/**
	 * Recupera um Value Object e todas as suas classes agregadas 
	 * ex: listas one-to-many em detalhes de lógica Mestre-Detalhe, classes many-to-one), a partir de sua chave primária <P>
	 *             Exceções são tratadas e retornadas como uma PlcException, no
	 *             padrão do jCompany, para tratamento genérico e exibição para
	 *             usuário. Uma exceção deve ser disparada caso a ocorrência
	 *             esperada não seja encontrada. (tipicamente devido ao filtro
	 *             de segurança ou concorrência entre usuários)
	 */
	@PlcTransactional(commit=false)
	@TransactionAttribute(javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
	@Override
	public Object[] edit(PlcBaseContextVO context, Class classe, Object id)  {

		try {
			
			Object[]  ret = iocModelUtil.getRepository(classe).edit(context, classe, id); 

			return ret;

		}catch(PlcException ePlc){
			throw ePlc;
		} catch (Exception e) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_PERSISTENCE_FIND, new Object[] {"findObject", e }, e, log);
		}

	}

	/**
	 * Exclui um Value Object e todas as classes agregadas (detalhes) mapeados como cascade="true", a partir de sua identificação em id. 
	 * Deve receber os VOs atual e anterior, porque a exclusão pode ser lógica (alteração para sitHistoricoPlc='I') <P>
	 * 
	 * @param context Contexto com informações - Por se tratar de Classe StateLess 
	 * @param vo
	 *            Value Object da classe específica, descendente de PlcEntityInstance, a
	 *            ser excluido.
	 * 
	 * 
	 *             Exceções Tratada e retornadas como uma PlcException, no
	 *             padrão do jCompany, para tratamento genérico e exibição para
	 *             usuário. Uma exceção deve ser disparada caso a ocorrência
	 *             esperada não seja encontrada. (tipicamente devido ao filtro
	 *             de segurança ou concorrência entre usuários)
	 */
	@PlcTransactional
	@TransactionAttribute(javax.ejb.TransactionAttributeType.REQUIRED)
	@Override
	public void deleteObject(PlcBaseContextVO context, Object vo) {

		Object voAnt = null;

		try {

			PlcBaseRepository baseBO = (PlcBaseRepository) iocModelUtil.getRepository(vo.getClass());

			if (context != null && 
				(EXCLUSAO.MODO_LOGICA.equals(context.getDeleteModeAux()) || annotationUtil.isLogicalExclusion(vo.getClass()))) {
				log.debug( "Entrou para exclusao logica");
				voAnt = BeanUtils.cloneBean(vo);
				entityCommonsUtil.updateSitHistoricoPlc(context, vo,VO.SIT_INATIVO, false);
				baseBO.update(context, vo);
			} else
				baseBO.delete(context, vo);

		}catch(PlcException ePlc){
			throw ePlc;
		} catch (Exception e) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_PERSISTENCE_FIND_OBJECT, new Object[] {"deleteObject", e }, e, log);
		}

	}

	/**
	 * Baixa um arquivo anexado gravado em SGBD, via Hibernate.
	 * 
	 * Importante: O upload é realizado automaticamente em um conceito "mestre-detalhe", ou seja, com o arquivo agregado a um 
	 * Value Object principal, no mesmo evento de gravação (botão Grava). 
	 * Já o evento de download deve ser feito á parte, para evitar recuperação desnecessária de arquivos.
	 * Exceções são retornadas como uma PlcException, no padrão do jCompany, para tratamento genérico e exibição para usuário.
	 */
	@PlcTransactional(commit=false)
	@TransactionAttribute(javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
	@Override
	public IPlcFile downloadFile(PlcBaseContextVO context, Class classe, Object id)  {

		IPlcFile vo = null;

		try {
			
			PlcBaseRepository baseBO = iocModelUtil.getRepository(classe);

			if (id instanceof Long) {
				Object[] ret = baseBO.edit(context, classe, id);
				vo = (IPlcFile) ret[0];
			} else {
				// Se for url, dispensa BO para download
				PlcBaseDAO baseDAO = iocModelUtil.getPersistenceObject(classe);
				vo = (IPlcFile) baseDAO.findFileByUrl(context, classe,(String) id);
			}	

			return vo;
			
		}catch(PlcException ePlc){
			throw ePlc;
		} catch (Exception e) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_PERSISTENCE_DOWNLOAD, new Object[] {"downloadFile", e }, e, log);
		}

	}

	
	/**
	 * Recupera Total 
	 * @return Recupera total de registros para consulta paginada
	 */
	@PlcTransactional(commit=false)
	@TransactionAttribute(javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
	@Override
	public Long findCount(PlcBaseContextVO context, Object entidadeArg)  {

		try {

			return iocModelUtil.getRepository(entidadeArg.getClass()).findCount(context, entidadeArg);
			
		}catch(PlcException ePlc){
			throw ePlc;
		} catch (Exception e) {
			throw new PlcException("PlcFacadeImpl", "findCount", e, log, "");
		}

	}

	
	/**
	 * Recupera agregado lookup 
	 */
	@PlcTransactional(commit=false)
	@TransactionAttribute(javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
	@Override
	public Object findAggregateLookup(PlcBaseContextVO context, Object vo, Map<String, Object> propriedadesValores)  {
		
		try {
			
			// Chama template method
			Object voRecuperado = iocModelUtil.getRepository(vo.getClass()).findAggregateLookup(context, vo, propriedadesValores);

			return voRecuperado;

		}catch(PlcException ePlc){
			throw ePlc;
		} catch (Exception e) {
			throw new PlcException("PlcFacadeImpl", "findAggregateLookup", e, log, "");
		}
		
	}
	
	/**
	 * Recupera por navegação de um Vo para outro
	 * @since jCompany 2.7.3.
	 */
	@PlcTransactional(commit=false)
	@TransactionAttribute(javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
	@Override
	public Collection findNavigation(PlcBaseContextVO context, Class classeOrigemPlc, Object pk, Class classeDestinoPlc) {

		try {

			if (!(pk instanceof Long)) {
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_AGGREGATE_LOOKUP_ONLY_OID);
			}
			
			// Chama template method
			List l = iocModelUtil.getRepository(classeDestinoPlc).findNavigationAggregate(context, classeOrigemPlc, pk, classeDestinoPlc);

			return l;

		}catch(PlcException ePlc){
			throw ePlc;
		} catch (Exception e) {
			throw new PlcException("PlcFacadeImpl", "findNavigation", e, log, "");
		}
	}


    /**
     * Recupera lista de exploração a partir de uma classe e OID
	 * @param context Contexto com informações - Por se tratar de Classe StateLess 
     * @param classe Classe-Base 
     * @param id Identificador (até esta versão somente OID)
     * @return Lista valores de detalhamento para o objeto
     * @since jCompany 3.0
     */
	@PlcTransactional(commit=false)
	@TransactionAttribute(javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
	@Override
	public Collection findListTreeView(PlcBaseContextVO context, Class classeBase, Object id, Class classeFilha, long posIni)  {
    	
		List listaRecuperada = iocModelUtil.getRepository(classeBase).findListTreeView(context, classeBase,id,classeFilha,posIni);

		return listaRecuperada;
	}
    
	
	/**
	 * Recupera mestre com os detalhe paginados e não paginados;
	 * @throws InstantiationException 
	 */
	@PlcTransactional(commit=false)
	@TransactionAttribute(javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
	@Override	 
	public Object[] findObjectPagedDetail(PlcBaseContextVO context, Class classeMestre, Object id, Long posAtual, String ordenacaoPlc, PlcConfigDetail ... configDetalhes) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException, InstantiationException {
		
		context.setDetailNamesProps(null);
		context.setDetailNames(null);
		
		Object[] recuperaObjeto = edit(context, classeMestre, id);
		
		Object	entidadeMestre = recuperaObjeto[0];
		
		for (PlcConfigDetail configDetalhe : configDetalhes) {
			if (!context.isOnDemand(configDetalhe.collectionName())) {
				Collection detalhes = findListPagedDetail(context, configDetalhe, entidadeMestre, configDetalhe.navigation().numberByPage(), ordenacaoPlc, 0, true);
				findSubDetail(context, configDetalhe, detalhes);
				propertyUtilsBean.setProperty(entidadeMestre, configDetalhe.collectionName(), detalhes);
			} else {
				propertyUtilsBean.setProperty(entidadeMestre, configDetalhe.collectionName(), null);
			}
		}
		
		return recuperaObjeto;
	}

	protected void findSubDetail(PlcBaseContextVO context, PlcConfigDetail configDetalhe, Collection detalhes) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
		
		PlcConfigSubDetail subDetalhe = configDetalhe.subDetail();
		
		Field atribudoDetalhe;
		
		try {
			
			if (subDetalhe != null && StringUtils.isNotBlank(subDetalhe.collectionName())){
				
				for (Object det : detalhes) {
					
					atribudoDetalhe = reflectionUtil.findFieldHierarchically(det.getClass(), subDetalhe.collectionName());
					OneToMany oneToMany = atribudoDetalhe.getAnnotation(OneToMany.class);
					String mappedBy = oneToMany.mappedBy();
					
					PlcPrimaryKey chavePrimaria = (PlcPrimaryKey)configDetalhe.clazz().getAnnotation(PlcPrimaryKey.class);

					Object subdetalheInst = subDetalhe.clazz().newInstance();
					
					if (chavePrimaria == null){
						// Mestre utiliza oid
						PlcEntityInstance detalheInstance = metamodelUtil.createEntityInstance(det);

						//somente estava trazendo um subdetalhe, dessa forma traz a coleção completa à partir do detlhe
						propertyUtilsBean.setProperty(subdetalheInst, mappedBy, det);
						
					} else {
						// Utiliza chave natural
						// TODO - Implementar busca de subdetalhes para chave natural.
					}
					
					String ordenacao = "";
					OrderBy orderBy = reflectionUtil.getAnnotationFromProperty(OrderBy.class, configDetalhe.clazz() , subDetalhe.collectionName());
					if (orderBy != null) {
						ordenacao = orderBy.value();
					} else {
						Id id = reflectionUtil.getAnnotationFromProperty(Id.class, subDetalhe.clazz() , "id");
						if(id != null) {
							ordenacao = "id";
						}
					}
					
					Collection listaSubDetalhes = findList(context, subdetalheInst, ordenacao, 0,0);
					
					

					Class<?> classeDetalhe = det.getClass();
					Field campo = null;
					if(classeDetalhe != null) {
						campo = reflectionUtil.findFieldHierarchically(det.getClass(), subDetalhe.collectionName());
						if(campo != null) { 
							classeDetalhe = campo.getType();
						} else {
							classeDetalhe = Object.class;
						}
					} else {
						classeDetalhe = Object.class;
					}

					if (classeDetalhe.isAssignableFrom(List.class)){
						// Mantem a ordenação provinda do Banco de Dados
						// O OrderBy definido na entidade
						propertyUtilsBean.setProperty(det, subDetalhe.collectionName(), listaSubDetalhes);
					}else{
						SortedSet listaSubdetalhes = new TreeSet(new PlcComparatorId());
						for (Object object : listaSubDetalhes) {
							listaSubdetalhes.add(object);
						}
						propertyUtilsBean.setProperty(det, subDetalhe.collectionName(), listaSubdetalhes);
					}
					
				}			
			}
			
		}catch(PlcException ePlc){
			throw ePlc;
		} catch (Exception e) {
			throw new PlcException("PlcFacadeImpl", "findNavigation", e, log, "");
		}
			
	}
	
	/**
	 * Método utilizado para paginação de detalhes. 
	 * Recupera uma lista de detalhes, baseando-se em uma posição e o numero de detalhes  que irá aparecer por páginas
	 */
	@PlcTransactional(commit=false)
	@TransactionAttribute(javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
	@Override	 
	public Collection findListPagedDetail(PlcBaseContextVO context, PlcConfigDetail configDetalhe, Object entidadeMestre, int numPorPagina, String ordenacaoPlc, int posicaoAtual, boolean incluiArgPai) throws NoSuchFieldException {
		
		try {

			Collection<Object> detalhePaginado = null;
			Object detalhe = configDetalhe.clazz().newInstance();
			getEntityWithArgs(configDetalhe, entidadeMestre, detalhe, incluiArgPai);
			PlcBaseJpaDAO dao = (PlcBaseJpaDAO) iocModelUtil.getPersistenceObject(entidadeMestre.getClass());
			
			Method m = reflectionUtil.findAllMethodsHierarchicallyByName(dao.getClass().getSuperclass(), "findListPagedDetail")[0];
			
			String ordenacao = ordenacaoPlc;
			if (StringUtils.isBlank(ordenacaoPlc)) {
				OrderBy orderBy = reflectionUtil.getAnnotationFromProperty(OrderBy.class, entidadeMestre.getClass(), configDetalhe.collectionName());
				if (orderBy != null && orderBy.value() != null && StringUtils.isNotEmpty(orderBy.value())) {
					ordenacao = orderBy.value();
				} else {
					ordenacao = "id";
				}
			}
			
			detalhePaginado = (Collection) m.invoke(dao, context, detalhe, ordenacao, posicaoAtual, numPorPagina);

			findSubDetail(context, configDetalhe, detalhePaginado);

			return detalhePaginado;

		}catch(PlcException ePlc){
			throw ePlc;
		} catch (Exception e) {
			throw new PlcException("PlcFacadeImpl", "findListPagedDetail", e, log, "");
		}
		
	}
	
	
	protected void getEntityWithArgs(PlcConfigDetail configDetalhe, Object entidadeMestre, Object detalhe, boolean incluiArgPai)  {
		
		try{

			if (detalhe == null || incluiArgPai){

				Field atribudoDetalhe = reflectionUtil.findFieldHierarchically(entidadeMestre.getClass(), configDetalhe.collectionName());
				OneToMany oneToMany = atribudoDetalhe.getAnnotation(OneToMany.class);
				String mappedBy = oneToMany.mappedBy();

				PlcPrimaryKey chavePrimaria = (PlcPrimaryKey)entidadeMestre.getClass().getAnnotation(PlcPrimaryKey.class);
				
				PlcEntityInstance entityPlcInstance = metamodelUtil.createEntityInstance(entidadeMestre);
				
				if (chavePrimaria == null){
					// Mestre utiliza oid
					propertyUtilsBean.setProperty(detalhe, mappedBy, entityPlcInstance.getInstancia());

				} else {
					propertyUtilsBean.setProperty(detalhe, mappedBy, entidadeMestre);
				}

			}

		}catch(PlcException ePlc){
			throw ePlc;
		}catch (Exception e) {
			throw new PlcException("PlcFacadeImpl", "getEntityWithArgs", e, log, "");
		}
	}

	/**
	 * Se usuario usou transacaoUnica no vinculado, deve recuperar as propriedades nesta mesma transação
	 */
	protected void getAggregateProperty(Object vo, boolean isAlteracao, Class classeMestre)  {
		//TODO - Verificar a necessidade de implementação.
	}
	
	/**
	 * Mesmo que resolvePropriedadesVinculado(Object vo, null)
	 */
	protected void getAggregateProperty(Object vo, boolean isAlteracao)  {
		getAggregateProperty(vo, isAlteracao, null);
	}


    /**
 	 * Verifica a operação de persistência a ser realizada para um item de uma coleção de Value Objects, comparando-o com a situação anterior, a saber<p>:
 	 *
 	 * Se indExcPlc for "S", exclui<br>
 	 * Se item existia anteriormente e foi alterado, altera<br>
 	 * Se item não existir anteriormente, inclui<p>
 	 *
 	 * Importante: Esta averiguação manual é necessário porque a Hibernate na versão
 	 * 2.0 beta3 não comportava bem com persistência de coleções "top-level". Em breve
 	 * poderá se tornar desnecessárias, a partir do momento em que a framework de
 	 * persitência interpretar essas lógicas automaticamente.
 	 *
 	 * @param voAtual
 	 * @param listAnt Coleção de Value Objects anterior, para comparação.
 	 * @return Object[] Retorna uma String e o VO anterior em caso de alteração,
 	 *      sendo a String a operação escolhida: ""-ignora, "E"-exclui, "A"-altera,
 	 *      "I"-inclui
 	 */	
 	protected Object[] checkAction(Object voAtual, List listAnt)  {

 		log.debug( "########## Entrou para verificar operacao");

 		Object[] ret = null;
 	 
 		// Tabulares devem ter chaves naturais na mesma classe para usarem as lógicas
 		// genéricas do jCompany (não podem usar classes externas)
        ret = entityCommonsUtil.checkOperationKeySameClass(voAtual,listAnt);

 		String oper = (String) ret[0];
 		Object voAnt = ret[1];

 		if (log.isDebugEnabled()) {
 			log.debug( "operacao="+oper);
 			if (voAnt != null) log.debug( " anterior="+voAnt);
 		}

 		return new Object[] {oper,voAnt};

 	}
    
    /**
     *  Recupera registro de mensagens tratadas de erros da camada de persistencia
     *  @param causaRaiz Exception
     *  @return String[0] mensagem internacionalizada, String[1]: arg1 (opcional), String[2] arg2 (opcional) ou null se não
     *  encontrou ou se ocorreu outra exceção no meio do tratamento.
     *  @since jCompany 3.0 
     */
	public Object[] findExceptionMessage(Throwable causaRaiz) {
		// TODO - Verificar a necessidade desse metodo na fachada.
		
		try{
			return iocModelUtil.getPersistenceObject(null).findExceptionMessage(causaRaiz);
		} catch (Exception e) {
			return null;
		}
	
	}
  	
	/**
	 * Método para a validação invariante do formulário na gravação simples.
	 * @return true se tudo estiver válido.
	 */
	public void checkBeanValidation(Object entityPlc, Class<?>... groups)  {

	 	// Validação invariante padrão Hibernate Validator.
		Set<ConstraintViolation<Object>> cv = null;
		
		if (entityPlc!=null) {
			try {
				cv = validationInvariantUtil.invariantValidation(entityPlc, groups);
			}catch(PlcException ePlc){
				throw ePlc;
			} catch (Exception e) {
				throw new PlcException("PlcFacadeImpl", "checkBeanValidation", e, log, "");
			}	
		}	
		boolean validacaoInvarianteOk = (cv == null || cv.isEmpty());

		//invocando a validação
		if (!validacaoInvarianteOk) 
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_BEAN_VALIDATION, cv);
		
	}
 
	/**
	 * Método para a validação invariante de tabular
	 * @return true se tudo estiver válido.
	 */
	public void checkBeanValidation(List entityListPlc, Class<?>... groups)  {

		for (Object object : entityListPlc) {
			checkBeanValidation(object);
		}

	}
}