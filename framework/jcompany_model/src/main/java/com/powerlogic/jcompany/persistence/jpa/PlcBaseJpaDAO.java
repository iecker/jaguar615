/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.persistence.jpa;



import static org.apache.commons.lang.StringUtils.capitalize;
import static org.apache.commons.lang.StringUtils.replaceOnce;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.Embedded;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.NoResultException;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.PropertyValueException;
import org.hibernate.Session;
import org.hibernate.StaleObjectStateException;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;
import org.hibernate.engine.spi.FilterDefinition;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.id.IdentifierGenerationException;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

import com.powerlogic.jcompany.commons.IPlcFile;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBaseContextVO.Mode;
import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.VO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.PlcFile;
import com.powerlogic.jcompany.commons.annotation.PlcEntityTreeView;
import com.powerlogic.jcompany.commons.annotation.PlcPrimaryKey;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.PlcAnnotationUtil;
import com.powerlogic.jcompany.commons.util.PlcBeanCloneUtil;
import com.powerlogic.jcompany.commons.util.PlcConfigUtil;
import com.powerlogic.jcompany.commons.util.PlcDateUtil;
import com.powerlogic.jcompany.commons.util.PlcEntityCommonsUtil;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.config.application.PlcConfigAppBehavior;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.domain.PlcFileAttach;
import com.powerlogic.jcompany.model.util.PlcDBFactoryUtil;
import com.powerlogic.jcompany.model.util.PlcIocModelUtil;
import com.powerlogic.jcompany.persistence.PlcBaseDAO;
import com.powerlogic.jcompany.persistence.util.PlcAnnotationPersistenceUtil;
import com.powerlogic.jcompany.persistence.util.PlcQBEUtil;
import com.powerlogic.jcompany.persistence.util.jpa.PlcJpaUtil;

/**
 * Classe DAO padrão para implementação JPA
 */
@SuppressWarnings("unchecked")
public abstract class PlcBaseJpaDAO extends PlcBaseDAO  {

	/**
	 * Objeto para saida centralizada de LOG.
	 */
	@Inject
	protected transient Logger log;

	private Logger logAdvertenciaDesenv = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_ADVERTENCIA_DESENVOLVIMENTO);

	/**
	 * Objeto para saida de LOG de profile.
	 */
	protected static Logger logProfiling = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_QA_PROFILING);

	@Inject @QPlcDefault 
	protected PlcMetamodelUtil metamodelUtil;

	@Inject @QPlcDefault 
	protected PlcIocModelUtil iocModelUtil;

	@Inject @QPlcDefault
	protected PlcQBEUtil qbeUtil;

	@Inject @QPlcDefault
	protected PlcAnnotationPersistenceUtil annotationPersistenceUtil;

	@Inject @QPlcDefault
	protected PlcJpaUtil jpaUtil;	

	@Inject @QPlcDefault
	protected PlcEntityCommonsUtil entityCommonsUtil;

	@Inject @QPlcDefault 
	protected PlcAnnotationUtil annotationUtil;

	@Inject @QPlcDefault 
	protected PlcDateUtil dateUtil;

	@Inject @QPlcDefault 
	protected PlcBeanCloneUtil beanCloneUtil;
	
	@Inject @QPlcDefault
	protected PlcReflectionUtil reflectionUtil;

	@Inject @QPlcDefault
	protected PlcDBFactoryUtil dBFactoryUtil;

	//cria instancia do propertyUtilsBean para evitar diversas chamadas a metodo syncronized
	private static PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
	
	/**
	 * Construtor padrão
	 */
	public PlcBaseJpaDAO(){ 

	}

	//============== CADIDATOS abstratos AO ANCESTRAL ======================= 
	/**
	 * Encapsula a obtenção do EntityManager
	 * @param context Contexto com informações - Por se tratar de Classe StateLess 
	 * @return Devolve EntityManager Padrão (equivalente sessão Hibernate).
	 * @since jCompany 5.0 
	 */
	protected EntityManager getEntityManager(PlcBaseContextVO context)  {
		return getEntityManager(context, "default");
	}

	/**
	 * Retorna a entity manager específico para uma determinada entidade
	 * @param context Contexto com informações - Por se tratar de Classe StateLess 
	 * @param entidade
	 * @return
	 */
	protected EntityManager getEntityManager(PlcBaseContextVO context, Class<?> entidade)  {
		return getEntityManager(context, dBFactoryUtil.getDBFactoryName(null, entidade, context));
	}

	/**
	 * Encapsula a obtenção do EntityManager
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param fabrica (Entity Manager)
	 * @return Devolve EntityManager(equivalente sessão Hibernate) da unidade de persistência selecionada .
	 * @since jCompany 5.0
	 */
	protected EntityManager getEntityManager(PlcBaseContextVO context, String fabricaPlc)  {

		try {
			
			String nomeFabrica = dBFactoryUtil.getDBFactoryName(fabricaPlc==null?"default":fabricaPlc, context!=null?context.getEntityClassPlc():null, context);

			EntityManager em = null;
			
			try {
				em = (EntityManager)propertyUtilsBean.getProperty(this, "em"+StringUtils.capitalize(nomeFabrica));
			} catch (Exception e) {}
			
			if (em!=null) {
				return em;			
			}
			em = ((PlcBaseJpaManager) iocModelUtil.getFactoryManager(nomeFabrica)).getEntityManager(context);

			return em;

		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcBaseJpaDAO", "getEntityManager", e, log, "");
		}

	}

	/**
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param classeBase
	 * @return Lista de classe relacionadas com a classe base
	 * @since jCompany 3.0
	 */
	public List<Class> getChildren(PlcBaseContextVO context, Class classeBase) {

		Metamodel cfg = getCfg(context);

		List<Class> l = new ArrayList<Class>();
		Iterator<EntityType<?>> i = cfg.getEntities().iterator(); 
		try {

			while (i.hasNext()) {
				EntityType et = (EntityType) i.next();
				if (jpaUtil.hasManyToOneReference(null,classeBase.getName()) &&
						annotationUtil.useTreeView(et.getJavaType().getName()))
					l.add(et.getJavaType());
			}

		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcBaseJpaDAO", "getChildren", e, log, "");
		}

		return l;
	}


	/** 
	 * Encerra uma transação/sessao com rollback
	 * @param fabrica Nome da fabrica a ser utilizada
	 */
	public void rollback(String fabrica)  {

		if (logPersistencia.isDebugEnabled()) {
			logPersistencia.debug( this.getClass().getSimpleName()+":ROLLBACK:");
		}
		
		if (log.isDebugEnabled()){
			log.debug("########## ENTROU EM ROLLBACK PARA UNIDADE DE PERSISTENCIA " + (StringUtils.isNotBlank(fabrica) ? fabrica.toUpperCase() : "") + "!");
		}
			

		iocModelUtil.getFactoryManager(fabrica).rollback();
	}

	/**
	 * Encerra uma transação/sessao com commit
	 * @param fabrica Nome da fabrica a ser utilizada
	 * @since jCompany 3.0
	 */
	public void commit(String fabrica)  {

		if (logPersistencia.isDebugEnabled())
			logPersistencia.debug( this.getClass().getSimpleName()+":COMMIT:");

		if (log.isDebugEnabled())
			log.debug( "########## ENTROU EM COMMIT PARA UNIDADE DE PERSISTENCIA "+fabrica+"!");

		iocModelUtil.getFactoryManager(fabrica).commit();

	}

	/**
	 * Dispara os comandos em buffer gerenciados pela engine de persistencia até o momento.
	 * Importante: Não faz confirmação final (commit, por exemplo), somente envia.
	 * @since jCompany 3.0
	 */
	@Override
	public void sendFlush(PlcBaseContextVO context, Class classe)  {

		EntityManager emLocal = null;
		
		try {
			emLocal = getEntityManager(context, dBFactoryUtil.getDBFactoryName("default", classe, context));

			emLocal.flush();

		} catch(PlcException plcE){
			throw plcE;				
		} catch (Exception e) {
			throw new PlcException("PlcBaseJpaDAO", "sendFlush", e, log, "");
		}
	}

	/**
	 * Persiste um objeto no SGBD
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param entity Entidade a ser incluida
	 * @return Identificador do objeto
	 */
	@Override
	public Long insert(PlcBaseContextVO context, Object entity)  {

		PlcEntityInstance voInstance = metamodelUtil.createEntityInstance(entity);

		try {

			EntityManager em = getEntityManager(context, dBFactoryUtil.getDBFactoryName(null, entity.getClass(), context));

			if(voInstance.getId()!=null) {
				voInstance.setId(null);
			}
			
			Object newEntity = entity;
			
			em.persist(entity);
			em.flush();
			Long pk = voInstance.getId();
			
			findCompleteGraph(context, newEntity);
			em.detach(entity);
			em.detach(newEntity); 
			beanCloneUtil.copyProperties(entity, newEntity, true);

			return pk;

		} catch(PlcException plcE){
			throw plcE;				
		}  catch (Exception e) {
			Object[] handledException = findExceptionMessage(e);
			if (handledException[0]!=null && !handledException[0].equals("")) {
				throw new PlcException((PlcBeanMessages)handledException[0], getException("insert", handledException), getCause(e), log);
			} else {
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_INSERT, 
							new Object[] {"insert", e }, e, log);
			}
		}
	}

	/**
	 * Altera os dados e uma entidade persistida no SGBD
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param entity Entidade a ser alterada
	 */
	@Override
	public void update(PlcBaseContextVO context, Object entity)  {

		try {

			EntityManager em = getEntityManager(context, dBFactoryUtil.getDBFactoryName(null, entity.getClass(), context));

			if (log.isDebugEnabled())
				log.debug( "Vai alterar entity "+entity);

			PlcEntityInstance voInstance = metamodelUtil.createEntityInstance(entity);

			if(voInstance != null && voInstance.getId()!=null || voInstance.getIdNaturalDinamico()!=null) {
				if (context!=null && FormPattern.Tab.name().equals(context.getFormPattern())) {
					em.merge(entity);
					em.flush();
				}
				else {
					Object newEntity = em.merge(entity);
					em.flush();
					// Copia dados alteradoes no listener como Version e dataUltAlteracao
					findCompleteGraph(context, newEntity);
					em.detach(entity);
					em.detach(newEntity); 
					beanCloneUtil.copyProperties(entity, newEntity, true);
				}
			} else {
				em.persist(entity);
			}
		} catch(PlcException plcE){
			throw plcE;			
		}  catch (Exception e) {
			Object[] handledException = findExceptionMessage(e);
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_GENERIC, new Object[]{"update", handledException[1]} , e.getCause(),log);
		}

	}

	/**
	 * Remove uma entidade persistida do SGBD
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param entity Entidade a ser removida
	 */
	@Override
	public void delete(PlcBaseContextVO context, Object entity)  {
		delete(context, entity, true);
	}

	@Override
	public void delete(PlcBaseContextVO context, Object entity, boolean flush)  {

		try {
			EntityManager em = getEntityManager(context, dBFactoryUtil.getDBFactoryName(null, entity.getClass(), context));

			log.info( "Vai excluir entity " + entity);
			
			em.remove(em.merge(entity));

			if (flush){
				em.flush();
			}

		} catch(PlcException plcE){
			throw plcE;					
		} catch (Exception e) {
			if (PersistenceException.class.isAssignableFrom(e.getClass()) && e.getCause()!=null) {
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_PERSISTENCE_DELETE,new Object[] {"delete", e.getCause(), entity},e.getCause(),log);
			} else {
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_PERSISTENCE_DELETE,new Object[] {"delete", e, entity},e,log);
			}
		}

	}


	/**
	 * Recupera do SGBD uma entidade
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param classe Classe o objeto a ser recuperado
	 * @param id Identificador do Objeto (chave primária)
	 * @return Entidade Persistida
	 */
	@Override
	public Object findById(PlcBaseContextVO context, Class classe, Object id) {

		try {			

			Class classeAux = jpaUtil.convertDynamicProxyToRealClass(classe);

			String queryEdita = annotationPersistenceUtil.getQueryEditDefault(classe);			
			
			if(queryEdita == null) {
				if(context!=null && annotationPersistenceUtil.getNamedQueryByName(classe, context.getApiQuerySel()) != null) {
					queryEdita = annotationPersistenceUtil.getNamedQueryByName(classe, context.getApiQuerySel()).query();
				} else {
					queryEdita = annotationPersistenceUtil.getQueryEditDefault(classe);
				}
			} 

			queryEdita = qbeUtil.createFromWhere(queryEdita, classeAux, id);			

			queryEdita = handlePrimaryKey(queryEdita, classe, id);
			
			queryEdita = applyFilters(context, classe, queryEdita);

			Object entity = null;
			if (metamodelUtil.isEntityClass(classe) && metamodelUtil.getEntity(classe).isIdNatural()) {
				entity = apiCreateQuery(context, classe, queryEdita).setParameter("id",id).getSingleResult();
			} else {
				entity = apiCreateQuery(context, classe,queryEdita).setParameter(1,id).getSingleResult();
			}

			if (entity == null) {
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_ITEM_NOT_FOUND, new Object[]{id.toString()}, true);
			}
			
			// Garante que colecoes participantes da lógica principal, mesmo se estiverem Lazy, são carregadas antes do 'detached'
			entity = findCompleteGraph(context, entity);

			return entity;
			
		} catch(PlcException plcE){
			throw plcE;
		} catch (Exception e) {
			if (NoResultException.class.isAssignableFrom(e.getClass())) {
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_ITEM_NOT_FOUND, new Object[]{"recupera", id.toString()}, true);
			} else {
				throw new PlcException("PlcBaseJpaDAO", "findById", e, log, "");
			}
		}

	}

	/**
	 * Recupera grafo padrao para Entidade informada
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param classe para completar o grafo
	 */
	@Override
	protected Object findCompleteGraph(PlcBaseContextVO context, Object entidade)  {

		//Transforma proxy do objeto principal no objeto real, caso ele seja um
		entidade = proxyToRealObject(context, entidade);

		if (context != null && !PlcConstants.EVENTOS.DOWNLOAD.equals(context.getOriginalAction())) {
			if (context.getEntityClassPlc()!=null && entidade.getClass().getName().equals(context.getEntityClassPlc().getName())) {
				// Para cada coleçao declarada na lógica, força a recuperaçao se estiver lazy
				if (context.getDetailNamesProps() != null) {
					Iterator i = context.getDetailNamesProps().iterator();
					while (i.hasNext()) {
						String nomeColDet = (String) i.next();
						try {
							//Pode ser download ou exclusao de arquivo. Exclusão força a carregar todo o Grafo, eliminando problemas com Detalhe Paginado e Por Demanda
							if ((context.getMode()!= null && context.getMode().equals(Mode.EXCLUSAO)) || (propertyUtilsBean.isReadable(entidade,nomeColDet) && !context.isOnDemand(nomeColDet))) {
								try {
									//recuperando anotação campo
									OneToMany anotacaoOneToMany = reflectionUtil.getAnnotationFromProperty(OneToMany.class,entidade.getClass(),nomeColDet);
									//criando objeto de pesquisa e setando o mestre e indicando que é ativo
									Object entidadeDetArg = anotacaoOneToMany.targetEntity().newInstance();
									propertyUtilsBean.setProperty(entidadeDetArg, anotacaoOneToMany.mappedBy(), entidade);
									//criando e realizando a query
									String query;
									if(context.getDeleteModeAux().equals("LOGICAL")) {
										query = "from " + entidadeDetArg.getClass().getSimpleName() + " where " + VO.SIT_HISTORICO_PLC + " = '" + VO.SIT_ATIVO + "'";
									} else{
										query = "from " + entidadeDetArg.getClass().getSimpleName();
									} 
									
									// Busca pela propriedade @javax.persistence.OrderBy na Lista de Detalhes
									OrderBy orderBy = reflectionUtil.getAnnotationFromProperty(OrderBy.class, entidade.getClass(), nomeColDet);
									
									//Ordenacao natural pela chave primaria - Padrão JPA 2.0
									String ordenacao = "id";
									if(orderBy != null && orderBy.value() != null && StringUtils.isNotEmpty(orderBy.value()) ) {
										ordenacao = orderBy.value();
									}
									
									Query q = createQueryWithEntity(context, entidadeDetArg, query, ordenacao);
									List resultList = q.getResultList();
									propertyUtilsBean.setProperty(entidade,nomeColDet, resultList);
								} catch (Exception e) {
									Hibernate.initialize(propertyUtilsBean.getProperty(entidade,nomeColDet));
									if(e.getCause() != null && e.getCause().getClass().isAssignableFrom(SQLGrammarException.class)) {
										log.error(e.getCause());	
										if(e.getCause() != null && e.getCause().toString().contains("SITHISTORICOPLC")) { 
											throw new PlcException(e.getCause());
										}
									} else {
										log.info("Carregando detalhe " + nomeColDet + " pelo initialize");
									}
								}	
								
								Class fieldDetail = reflectionUtil.findFieldHierarchically(entidade.getClass(), nomeColDet).getType();
								
								if(fieldDetail.isAssignableFrom(List.class)) {
									List detalhes = (List) propertyUtilsBean.getProperty(entidade,nomeColDet);
									if(detalhes != null) {
										for (int j = 0; j < detalhes.size(); j++) {
											detalhes.set(j, proxyToRealObject(context, detalhes.get(j)));
										}
									}
								} else {
									Set detalhes = (Set) propertyUtilsBean.getProperty(entidade,nomeColDet);
									if(detalhes != null) {
										for (Object object : detalhes) {
											object = proxyToRealObject(context, object);
										}
									}
								}
 
							} else if (context.isOnDemand(nomeColDet)) {
								propertyUtilsBean.setProperty(entidade,nomeColDet,null);
							}

							//Colecoes de detalhe manyToMany devem ter o outro lado anulado para evitar problemas de Lazy Initialization.
							Collection detalhes = (Collection) propertyUtilsBean.getProperty(entidade,nomeColDet);
							
							if (detalhes != null) {
								for (Iterator iter = detalhes.iterator(); iter.hasNext();) {
									Object beanDetalhe = iter.next();

									if (annotationUtil.hasManyToMany(beanDetalhe.getClass()))
										propertyUtilsBean.setProperty(beanDetalhe,annotationUtil.getEntityDefaultPropName(entidade.getClass(),null),null);

									// Sub-Detalhe
									if (context.getSubDetailParent()!=null && beanDetalhe.getClass().getName().equals(context.getSubDetailParent())) {
										
										if (propertyUtilsBean.isReadable(beanDetalhe, context.getSubDetailPropNameCollection())) {
											
											try {
												//recuperando anotação campo
												OneToMany anotacaoOneToMay = reflectionUtil.getAnnotationFromProperty(OneToMany.class,beanDetalhe.getClass(),context.getSubDetailPropNameCollection());
												//criando objeto de pesquisa e setando o mestre e indicando que é ativo
												Object entidadeSubDetArg = anotacaoOneToMay.targetEntity().newInstance();
												propertyUtilsBean.setProperty(entidadeSubDetArg, anotacaoOneToMay.mappedBy(), beanDetalhe);
												//criando e realizando a query
												String query;
												if(context.getDeleteModeAux().equals("LOGICAL")){
													query = "from " + entidadeSubDetArg.getClass().getSimpleName() 
																	+ " where " + VO.SIT_HISTORICO_PLC + " = '" + VO.SIT_ATIVO + "'";
												}else{
													query = "from " + entidadeSubDetArg.getClass().getSimpleName();
												}
												Query q = createQueryWithEntity(context, entidadeSubDetArg, query, null);
												propertyUtilsBean.setProperty(beanDetalhe,context.getSubDetailPropNameCollection(),q.getResultList());
											} catch (Exception e) {
												log.info("Carregando sub-detalhe " + nomeColDet + " pelo initialize");
												Hibernate.initialize(propertyUtilsBean.getProperty(beanDetalhe,context.getSubDetailPropNameCollection()));
											}	
											
											if(propertyUtilsBean.getProperty(beanDetalhe,context.getSubDetailPropNameCollection()) instanceof List) {
												List subDetalhes = (List) propertyUtilsBean.getProperty(beanDetalhe,context.getSubDetailPropNameCollection());
												if(subDetalhes != null) {
													for (int j = 0; j < subDetalhes.size(); j++) {
														subDetalhes.set(j, proxyToRealObject(context, subDetalhes.get(j)));
													}
												}
											}
										}
										
									}
								}
							}

						} catch(PlcException plcE){
							throw plcE;
						}  catch (Exception e) {
							throw new PlcException("PlcBaseJpaDAO", "findCompleteGraph", e, log, "");
						}
					}
				}
			} else {
				// neste caso é detalhe, entao inicializa a colecao de subdetalhe
				try {
					String subDetalhePropNomeColecao = context.getSubDetailPropNameCollection();
					if (subDetalhePropNomeColecao!=null && propertyUtilsBean.isReadable(entidade, subDetalhePropNomeColecao)) {
						Hibernate.initialize(propertyUtilsBean.getProperty(entidade,subDetalhePropNomeColecao));	
					}

					//verifica se existe alguma propriedade como proxy...
					PropertyDescriptor [] propertyDescriptors =  propertyUtilsBean.getPropertyDescriptors(entidade);

					for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
						if(!Hibernate.isInitialized(propertyUtilsBean.getProperty(entidade, propertyDescriptor.getName()))) {
							Hibernate.initialize(propertyUtilsBean.getProperty(entidade,propertyDescriptor.getName()));
						}
					}
				} catch(PlcException plcE){
					throw plcE;
				}  catch (Exception e) {
					throw new PlcException("PlcBaseJpaDAO", "findCompleteGraph", e, log, "");
				}
			}	
		}

		try {

			String nomePropriedade = null; 
			boolean isMultiplo = false;
			
			Field[] fields = reflectionUtil.findAllFieldsHierarchicallyWithAnnotation(entidade.getClass(), PlcFileAttach.class);
			
			for (Field field : fields) {
				
				nomePropriedade = field.getName();
				isMultiplo = field.getAnnotation(PlcFileAttach.class).multiple();
				if (nomePropriedade!=null && propertyUtilsBean.isReadable(entidade,nomePropriedade)) {
					if (isMultiplo) {
						Hibernate.initialize(propertyUtilsBean.getProperty(entidade,nomePropriedade));
					} else {
						IPlcFile file = (IPlcFile) propertyUtilsBean.getProperty(entidade,nomePropriedade);
						if (file != null) {
							propertyUtilsBean.setProperty(entidade,nomePropriedade,proxyToRealObject(context,file));
						}	
					}
				}	
			}

		} catch(PlcException plcE){
			throw plcE;
		} catch (Exception e) {
			throw new PlcException("PlcBaseJpaDAO", "recuperaGrafoPadrao", e, log, "");
		}

		return entidade;

	}

	/**
	 * Recupera um VO com arquivo incluido, baseada em sua URL
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param classe
	 * @param url URL para recuperar o arquivo.
	 * @return VO contendo arquivo.
	 * @since jCompany 6.1
	 */
	public IPlcFile findFileByUrl(PlcBaseContextVO context, Class classe, String url)  {

		String query = "from "+classe.getName() + " where lower(url)=:url";

		try {

			EntityManager em = getEntityManager(context, dBFactoryUtil.getDBFactoryName(null, classe, context));

			List l = em.createQuery(query).setParameter("url",url.toLowerCase()).getResultList();

			if (l.size()>0) {
				return (PlcFile) l.get(0);
			} else {
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_RETRIEVE_FILE_CONTENT, new Object[] {"Query="+query}, true);
			}
		} catch(PlcException plcE){
			throw plcE;			
		}  catch (Exception e) {
			throw new PlcException("PlcBaseJpaDAO", "findFileByUrl", e, log, "");
		}
	}


	/**
	 * Recupera relação de registros a partir de uma classe origem com o pk informado
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param principal
	 * @param pk OID ou Classe de chave composta com valores
	 * @param agregadoDestino Classe agregado de destino
	 * @return Coleção de valores possíveis para destino.
	 * @since jCompany 3.0
	 */
	@Override
	public List<Object> findNavigationAggregate(PlcBaseContextVO context, Class principal, Object pk, Class agregadoDestino)  {

		try {  	

			// Descobre por qual propriedade a classe destino é acessada a partir da origem
			String propNavegacao = jpaUtil.getManyToOnePropertyFromClass(agregadoDestino, principal);

			String querySelLookup = null;
			if (querySelLookup==null)
				querySelLookup="from "+agregadoDestino.getName() +" where "+propNavegacao+".id=?";        
			Query query = apiCreateQuery(context,agregadoDestino,querySelLookup);

			if (querySelLookup.contains("?")) {
				query.setParameter(1,pk);
			} else if (querySelLookup.contains(":id")) {
				query.setParameter("id", pk);
			} else {
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_QUERYSEL_LOOKUP, new Object[]{agregadoDestino.getName()}, true);
			}

			return query.getResultList();
		} catch(PlcException plcE){
			throw plcE;
		}  catch (Exception e) {
			throw new PlcException("PlcBaseJpaDAO", "findNavigationAggregate", e, log, "");
		}
	}

	/**
	 * Recupera classe de lookup, somente trazendo propriedades relacionadas em getLookupPropsPlc,
	 * caso o método seja declarado no VO. Caso contrário, recupera com "load" (deve-se ter cuidado, neste caso,
	 * com relacionamento com lazy=false, para evitar recuperações excessivas)<p>
	 * Importante: somente para chaves Object-ID, nesta versão.
	 * @param baseVO VO a ser recuperado
	 * @param nomePropriedade nome da propriedade que será utilizada para a pesquisa. Se null, será considerado o OID.
	 * @param valor valor de pesquisa da propriedade indicada.
	 * @param props Relação de propriedades, com exceção das chaves, a serem recuperadas
	 * @return VO contendo propriedades indicadas.
	 */
	public Object findAggregateLookup(PlcBaseContextVO context, Object baseVO, String nomePropriedade, Object valor, String[] props)  {
		// TODO - Implementar pesquisa utilizando os filtros verticais

		try {

			if (nomePropriedade==null || nomePropriedade.trim().length()==0) {
				nomePropriedade = "id";
			}

			Object baseVORecuperado = findAggregateLookupByNamedQuery(context, baseVO.getClass(), nomePropriedade, valor);
			if (baseVORecuperado==null) {
				propertyUtilsBean.setProperty(baseVO, nomePropriedade, valor);
				//baseVO.setId((Long)id);

				// Se tem declaração do método getLookupPropsPlc, recupera somente estas propriedades
				if (propertyUtilsBean.isReadable(baseVO,"lookupPropsPlc")) {
					Object lookupPropsPlcObj = propertyUtilsBean.getProperty(baseVO,"lookupPropsPlc");
					String[] lookupPropsPlc = (String[]) lookupPropsPlcObj;
					baseVORecuperado = findWithoutGraph(context,baseVO,nomePropriedade,lookupPropsPlc);
				} else {
					// Se não tem declaração do método getLookupPropsPlc, recupera com load
					baseVORecuperado = findWithGraph(context, baseVO, nomePropriedade);
				}
			}

			return baseVORecuperado;
		} catch(PlcException plcE){
			throw plcE;
		}  catch (Exception e) {
			throw new PlcException("PlcBaseJpaDAO", "findAggregateLookup", e, log, "");
		}
	}

	
	/**
	 * Método Genérico para execução de uma NamedQuery com os parâmetros passados por argumento.
	 * A NamedQuery deve ser declarada na entidade, por exemplo: "from UsuarioEntity where login = ? and cpf = ?"
	 * Sendo que o objeto parâmetros deve conter os nomes dos parâmetros da Query: {"login", "cpf"}
	 * O objeto valores deve conter o valore de cada filtro utilizados na Query: {"igor.guimaraes", "11111111111"}
	 * @param context Contexto com informações - Por se tratar de Classe StateLess 
	 * @param classe
	 * @param namedQuery
	 * @param parametros
	 * @param valores
	 * @return
	 */
	public List findByFields(PlcBaseContextVO context, Class classe, String namedQuery, String[] parametros, Object[] valores) {
		
		Query query = getNamedQueryWithParams(context, classe, namedQuery, parametros, valores);
		
		return query.getResultList();
	}

	/**
	 * Cria um Objeto Query, atachando os parâmetros e os valores automaticamente.
	 * Pode ser utilizada para executar:
	 * 	Select
	 * 	Update
	 * 	Insert
	 * 	Delete 
	 * Exemplos: 
	 * Batch processing - Bulk update/delete 
	 * Batch processing has traditionally been difficult in full object/relational mapping. 
	 * ORM is all about object state management, which implies that object state is available in memory. 
	 * However, Hibernate has some features to optimize batch processing which are discussed in the Hibernate reference guide, however, EJB3 persistence differs slightly.
	 * http://docs.jboss.org/hibernate/orm/4.0/hem/en-US/html/batch.html
	 * @param context Contexto com informações - Por se tratar de Classe StateLess 
	 * @param classe
	 * @param namedQuery
	 * @param parametros
	 * @param valores
	 * @return Query
	 */
	public Query getNamedQueryWithParams(PlcBaseContextVO context, Class classe, String namedQuery, String[] parametros, Object[] valores) {
		
		namedQuery = annotationPersistenceUtil.getNamedQueryByName(classe, namedQuery).query();

		Query query = apiCreateQuery(context, classe, namedQuery);

		for (int i = 0; i < parametros.length; i++) {
			query.setParameter(parametros[i], valores[i]);
		}
		
		return query;
	}

	/**
	 * Trata filtros do jCompany no padrão da Hibernate 3.x utilizando JPA. 
	 * Habilita todos os filtros que não possuírem argumentos para a classe corrente e também aqueles que possuem argumentos e os tem 
	 * informados no Map plcSegurancaVertical do perfil do usuário que existe no context. 
	 * Considera classes filhas, se for lógica mestre-detalhe-subdetalhe.
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param classe
	 * @since jCompany 6.1
	 */
	@Override
	public String applyFilters(PlcBaseContextVO context, Class classe, String query)  {
		
		String queryWhere = query;
		
		Annotation[] annotations = classe.getDeclaredAnnotations();

		for (int i = 0; i < annotations.length; i++) {

			Annotation annotation = annotations[i];

			// Somente anotações de filtros da hibernate
			// Neste caso tem somente uma anotação
			if (Filter.class.isAssignableFrom(annotation.getClass())) {
				queryWhere = applyOneFilter(context, classe, (Filter) annotation, query);
			} else {
				// Verifica se tem coleção de filtros
				// Neste caso tem um conjunto
				if (Filters.class.isAssignableFrom(annotation.getClass())) {
					Filters fs = (Filters) annotation;
					for (int j = 0; j < fs.value().length; j++) {
						Filter f = (Filter) fs.value()[j];
						queryWhere = applyOneFilter(context, classe, f, queryWhere);
					}
				}
			}
		}
		
		return queryWhere;
	}

	/**
	 * Habilita um filtro para a classe corrente que possue argumentos e os tem informados no Map plcSegurancaVertical do perfil do usuário que existe no context.
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param fA Filtro a ser considerado
	 * @since jCompany 6.1
	 */
	public String applyOneFilter(PlcBaseContextVO context, Class classe, Filter fA, String query) {
		
		String segundaParteQuery = "";
		String queryWhere = query;
		
		List<String> queryLista = separaQuery(queryWhere);
		
		queryWhere = queryLista.get(0);
		segundaParteQuery = queryLista.get(1);
		
		EntityManager entityManager = null;

		try {
			entityManager = getEntityManager(context);
		} catch (PlcException e) {
			log.error( e.getMessage());
		}

		if (entityManager != null) {

			Session session = (Session) entityManager.getDelegate();
			org.hibernate.Filter f = session.enableFilter(fA.name());

			// Depois varre todos os argumentos. Se o filtro tiver todos os argumentos informados, deixa-o habilitado
			Set s = f.getFilterDefinition().getParameterNames();

			Iterator j = s.iterator();
			while (j.hasNext()) {

				String argumento = (String) j.next();
				Object argumentoValor = null;

				if (context.getUserProfile() != null 
						&& context.getUserProfile().getPlcVerticalSecurity() != null ) {
					
					List<String> listaFiltro = context.getUserProfile().getFilters();
					Map mapaFiltro = new java.util.HashMap<String, String>();
					
					for(String filtro:listaFiltro){
						mapaFiltro.put(filtro.substring(0,filtro.indexOf("#")), filtro.substring(filtro.indexOf("#")+1, filtro.length()));						
					}					
					
					for (Iterator iterator = mapaFiltro.keySet().iterator(); iterator.hasNext();) {
						String chave = (String) iterator.next();
						if(chave.equalsIgnoreCase(argumento)) {
							argumentoValor = mapaFiltro.get(argumento);
							
							String condicao = fA.condition();
							condicao = condicao.substring(0, condicao.indexOf(":"));
							
							String condicaoMontada = condicao + "'"+argumentoValor+"' ";
							
							if(!queryWhere.contains(condicaoMontada)){
								queryWhere = queryWhere + condicaoMontada;
							}else{
								if(queryWhere.endsWith("and ")){
									queryWhere = queryWhere.substring(0, queryWhere.length()-4);
								}
							}
						}
					}
				}

				if (argumentoValor != null) {
					if (List.class.isAssignableFrom(argumentoValor.getClass())) {
						if (log.isDebugEnabled()) {
							log.debug( "FILTRO: Vai colocar valor de lista " + argumentoValor + " no argumento de filtro" + argumento);
						}
						f.setParameterList(argumento,(List) argumentoValor);
					} else {
						if (log.isDebugEnabled()) {
							log.debug( "FILTRO: Vai colocar valor  " + argumentoValor + " no argumento de filtro" + argumento);
						}
						f.setParameter(argumento, argumentoValor);
					}
				} else {
					// Se não encontrar um argumento advindo da lógica de
					// profile ou método específico, entao desabilita o filtro
					session.disableFilter(fA.name());
					return query;
				}
			}
			
			session.disableFilter(fA.name());

			if (log.isDebugEnabled()) {
				log.debug( "Vai usar filtro com valores " + fA);
			}
		}
		
		queryWhere = queryWhere + segundaParteQuery;
		
		return queryWhere;
	}
	
	/**
	 * Trata filtros do jCompany no padrão da Hibernate 3.x utilizando JPA. 
	 * Habilita todos os filtros que não possuírem argumentos para a classe corrente e também aqueles que possuem 
	 * argumentos e os tem informados no Map plcSegurancaVertical do perfil do usuário que existe no context. 
	 * Considera classes filhas, se for lógica mestre-detalhe-subdetalhe.
	 * Observação: Não modifica a query
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param classe
	 * @since jCompany 5.6
	 * @deprecated - retirado na 6.2
	 */
	@Override
	public void applyFilters(PlcBaseContextVO context, Class classe)  {

		Annotation[] annotations = classe.getDeclaredAnnotations();

		for (int i = 0; i < annotations.length; i++) {

			Annotation annotation = annotations[i];

			// Somente anotações de filtros da hibernate
			// Neste caso tem somente uma anotação
			if (Filter.class.isAssignableFrom(annotation.getClass())) {
				applyOneFilter(context, classe, (Filter) annotation);
			} else {
				// Verifica se tem coleção de filtros
				// Neste caso tem um conjunto
				if (Filters.class.isAssignableFrom(annotation.getClass())) {
					Filters fs = (Filters) annotation;
					for (int j = 0; j < fs.value().length; j++) {
						Filter f = (Filter) fs.value()[j];
						applyOneFilter(context, classe, f);
					}
				}
			}
		}
	}	
	
	/**
	 * Habilita um filtro para a classe corrente que possue argumentos e os tem informados no Map plcSegurancaVertical do 
	 * perfil do usuário que existe no context. Observação: não modifica a query.
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param fA Filtro a ser considerado
	 * @since jCompany 5.6
	 * @deprecated - retirado na 6.2
	 */
	public void applyOneFilter(PlcBaseContextVO context, Class classe, Filter fA) {

		EntityManager entityManager = null;

		try {
			entityManager = getEntityManager(context);
		} catch (PlcException e) {
			log.error( e.getMessage());
		}

		if (entityManager != null) {

			Session session = (Session) entityManager.getDelegate();
			org.hibernate.Filter f = session.enableFilter(fA.name());

			// Depois varre todos os argumentos. Se o filtro tiver todos os argumentos informados, deixa-o habilitado
			Set s = f.getFilterDefinition().getParameterNames();

			Iterator j = s.iterator();
			while (j.hasNext()) {

				String argumento = (String) j.next();
				Object argumentoValor = null;

				if (context.getUserProfile() != null 
						&& context.getUserProfile().getPlcVerticalSecurity() != null ) {
					
					Map mapaFiltro = context.getUserProfile().getPlcVerticalSecurity();
					
					for (Iterator iterator = mapaFiltro.keySet().iterator(); iterator.hasNext();) {
						String chave = (String) iterator.next();
						if(chave.contains(argumento)) {
							argumentoValor = context.getUserProfile().getPlcVerticalSecurity().get(argumento);
						}
					}
				}

				if (argumentoValor != null) {
					if (List.class.isAssignableFrom(argumentoValor.getClass())) {
						if (log.isDebugEnabled()) {
							log.debug( "FILTRO: Vai colocar valor de lista " + argumentoValor + " no argumento de filtro" + argumento);
						}
						f.setParameterList(argumento,(List) argumentoValor);
					} else {
						if (log.isDebugEnabled()) {
							log.debug( "FILTRO: Vai colocar valor  " + argumentoValor + " no argumento de filtro" + argumento);
						}
						f.setParameter(argumento, argumentoValor);
					}
				} else {
					// Se não encontrar um argumento advindo da lógica de
					// profile ou método específico, entao desabilita o filtro
					session.disableFilter(fA.name());
					return;
				}
			}

			if (log.isDebugEnabled()) {
				log.debug( "Vai usar filtro com valores " + fA);
			}
		}
	}	
	
	protected List<String> separaQuery(String query){
		
		List<String> queryLista = new ArrayList<String>();
		
		String primeiraParte = query;
		String segundaParte = "";
		int indice = 0;
				
		if(primeiraParte.contains("order by")){
			indice = primeiraParte.indexOf("order by");
		}
		
		if(primeiraParte.contains("having")){
			if(primeiraParte.indexOf("having") < indice || indice == 0){
				indice = primeiraParte.indexOf("having");
			}
		}
		
		if(primeiraParte.contains("group by")){
			if(primeiraParte.indexOf("group by") < indice || indice == 0){
				indice = primeiraParte.indexOf("group by");
			}
		}
		
		if(indice != 0){
			segundaParte = primeiraParte.substring(indice, primeiraParte.length());
			primeiraParte = primeiraParte.replace(segundaParte, "");
		}
		
		if(!primeiraParte.contains("where")){
			primeiraParte = primeiraParte+" where ";
		}
		
		if(!primeiraParte.endsWith("where ")){
			primeiraParte = primeiraParte +" and ";
		}
		
		queryLista.add(primeiraParte);
		queryLista.add(segundaParte);
		
		return queryLista;
		
	}

	/**
	 * Recupera uma lista de objetos da classe informada, utilizando queries anotadas no padrao "querySel" ou "queryTreeView", etc., em 
	 * conformidade com o Application Pattern utilizado. Pode receber orderByDinamico (alterado pelo usuário ou desenvolvedor em cada
	 * requisição) e também um trecho de where condition para ser dinamicamente composto (uso em lógicas dinamicas tais como
	 * explorer), além de POJO de argumentos e intervalo para paginação
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param classe Classe da qual será obtida a query em anotação padrao ("querySel", "queryTreeView", etc., conforme a lógica chamadora)
	 * @param whereDinamico (Opcional - informe null ou "" para desconsiderar) Trecho a ser adicionado dinamicamente à query anotada. Importante: Nao informar o 'where' em si e utilizar o alias padrão 'obj.' (Ex:'obj.status=:status and obj.valor>:valor')
	 * @param orderByDinamico (Opcional - informe null ou "" para desconsiderar) Trecho de orderBy a ser adicionado dinamicamente à query anotada
	 * @param argQBE POJO contendo valores preenchidos para todos os argumentos existentes fixos na query ou montados dinamicamente em whereDinamico
	 * @param primeiraLinha Primeira linha para recuperação paginada ou -1 para todos
	 * @param maximoLinhas Máximo de linhas (tamanho da página de recuperação) ou -1 para todos
	 * @since jCompany 5.0
	 * @see com.powerlogic.jcompany.persistence.PlcBaseDAO#recuperaListaQBEPaginada(java.lang.Class, java.lang.String, com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance, int, int)
	 */
	@Override
	public List findListPagedQBE(PlcBaseContextVO context, Class classe, String whereDinamico, String orderByDinamico, Object argQBE, int primeiraLinha, int maximoLinhas)  {

		String query = qbeUtil.getQuerySelDefault(context, classe);

		try{

			// Se informado whereDinamico, inclui
			if (whereDinamico != null && !whereDinamico.equals("")) {
				// caso seja uma TreeView, substitui o where defaul, que lista somente objetos sem 'pai' (listagem inicial)
				if(context.getApiQuerySel().equalsIgnoreCase("queryTreeView")) {
					query = findListPagedQBEReplaceWhere(query, whereDinamico);
				} else {
					query =  findListPagedQBEChangeWhere(query,whereDinamico);
				}
			}

			// Troca query para receber argumentos nulos com is null
			query = changeHqlIsNull(query,argQBE);

			//Vai funcionar apenas para treeview
			String[] nomesParametros =  qbeUtil.destileArgsHql(query);
			
			query = applyFilters(context, classe, query);
			
			Query q = apiCreateQuery(context, classe,query);
			for (String parametro : nomesParametros) {
				if(null != parametro) {
					if(propertyUtilsBean.isReadable(argQBE, parametro)) {
						Object o = propertyUtilsBean.getProperty(argQBE, parametro);
						if (metamodelUtil.isEntityClass(o.getClass())) {
							PlcEntityInstance oInstance = metamodelUtil.createEntityInstance(o);
							q.setParameter(parametro, oInstance.getId());	
						} else {
							q.setParameter(parametro, o);
						}
					}
				}

			}

			if (primeiraLinha >= 0){
				q.setFirstResult(primeiraLinha);
			}

			if (maximoLinhas == 0 ) {
				logAdvertenciaDesenv.debug( this.getClass().getCanonicalName()+": Numero maximo de linhas enviado como Zero!");
			} else if (maximoLinhas >= 1){
				q.setMaxResults(maximoLinhas);
			}

			List lista = q.getResultList();

			if (log.isDebugEnabled()) {
				log.debug( "achou "+lista.size()+" registro(s)");
			}

			return lista;
		} catch(PlcException plcE){
			throw plcE;
		} catch (Exception e) {
			throw new PlcException("PlcBaseJpaDAO", "findListPagedQBE", e, log, "");
		}
	}



	/**
	 * Recupera uma lista de objetos da classe informada, utilizando queries anotadas no padrao "querySel" ou "queryTreeView", etc., em 
	 * conformidade com o Application Pattern utilizado. Pode receber orderByDinamico (alterado pelo usuário ou desenvolvedor em cada
	 * requisição) e também um trecho de where condition para ser dinamicamente composto (uso em lógicas dinamicas tais como
	 * explorer).
	 * @param context Contexto com informações - Por se tratar de Classe StateLess 
	 * @param classe Classe da qual será obtida a query em anotação padrao ("querySel", "queryTreeView", etc., conforme a lógica chamadora)
	 * @param whereDinamico (Opcional - informe null ou "" para desconsiderar) Trecho a ser adicionado dinamicamente à query anotada. Importante: Nao informar o 'where' em si e utilizar o alias padrão 'obj.' (Ex:'obj.status=:status and obj.valor>:valor')
	 * @param orderByDinamico (Opcional - informe null ou "" para desconsiderar) Trecho de orderBy a ser adicionado dinamicamente à query anotada
	 * @param argQBE POJO contendo valores preenchidos para todos os argumentos existentes fixos na query ou montados dinamicamente em whereDinamico
	 * @since jCompany 5.0
	 */
	@Override
	public Long findListCountQBE(PlcBaseContextVO context, Class classe, String whereDinamico, Object argsQBE)  {		

		try {
			String queryCount = qbeUtil.getQueryCount(classe);

			if(queryCount == null) {
				String query = qbeUtil.getQuerySelDefault(context, classe);

				if (whereDinamico != null && !whereDinamico.equals("")) {
					query = findListPagedQBEChangeWhere(query,whereDinamico);
				}
				int posFrom = query.indexOf("from");

				if (posFrom == -1) {
					throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_HQL_WITHOUT_FROM);
				}
				
				queryCount = "select count(*) " + query.substring(posFrom, query.length());

				queryCount = removeGroupbyOrderby(queryCount);

			}

			apiHandleArgsBeforeQuery(classe,queryCount,argsQBE);

			String nomeArgs[] = qbeUtil.destileArgsHql(queryCount);
			
			queryCount = applyFilters(context, classe, queryCount);

			Query q = apiCreateQuery(context, classe, queryCount);


			for(int i=0; nomeArgs[i]!=null;i++) {
				if(propertyUtilsBean.isReadable(argsQBE, nomeArgs[i]))
					q.setParameter(nomeArgs[i],propertyUtilsBean.getProperty(argsQBE, nomeArgs[i]));
			}

			List l = q.getResultList();
			Object ret = l.get(0);

			return (Long) ret;
		} catch(PlcException plcE){
			throw plcE;
		} catch (Exception e) {
			throw new PlcException("PlcBaseJpaDAO", "findListCountQBE", e, log, "");
		}
	}

	public String removeGroupbyOrderby(String queryCount) {
		// retira order by e group by
		int posOrder = queryCount.toLowerCase().indexOf("order by");
		int posGroup = queryCount.toLowerCase().indexOf("group by");
		int tam = queryCount.length();
		int fim = tam;

		if (posOrder != -1 || posGroup != -1) {
			if (posOrder < posGroup){
				fim = posGroup;
			} else {
				fim = posOrder;
			}
		}

		if (fim != tam) {
			queryCount = queryCount.substring(0, fim);
		}
		return queryCount;
	}


	/**
	 * Recupera objetos de 'classe', filhos do objeto de 'classeBase' cujo OID seja 'idClasseBase'.
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param classe Classe para recuperação, que possui um relacionamento many-to-one com a classeBase
	 * @param classeBase Classe do lado "one" do relacionamento
	 * @param idClasseBase OID de objeto da classe do lado "one" do relacionamento
	 * @return Lista contendo objetos que atendem ao critério, com as seguintes regras:<p>
	 * 1. se 'classe' tiver anotação de paginação, e posIni for -1 (primeira chamada), entao devolve o total de registros existentes na
	 * primeira posição, considerando somente Ativos se estiver utilizando "sitHistoricoPlc" e este campo estiver mapeado na classe.<br>
	 * 2. se 'classe' contiver anotação de paginação e posIni for acima de 1 (usa 1 para primeiro objeto), então recupera o numero de registros
	 * indicado a partir de posIni<br>
	 * 3. se 'classe' não contiver anotação de paginação, recupera todos os registros respeitando 'sitHistoricoPlc' também.<p>
	 * Em qualquer caso, recupera obedecendo a ordenação indicada em PlcEntidade(ordenacao=....)
	 * @since jCompany 3.0
	 * @see com.powerlogic.jcompany.persistence.PlcBaseDAO#recuperaListaExplorer(java.lang.Class, java.lang.Class, java.lang.Object)
	 */
	@Override
	public List findListTreeView(PlcBaseContextVO context, Class classe, Class classeBase, Object idClasseBase, long posIni)  {

		int numPorPagina = annotationUtil.getEntityNumberByPage(classe,0);

		// Ajusta para reutilizar funcoes de paginacao do jCompany que originalmente usam int
		int posIniInt = (int) posIni;

		boolean temPaginacao = numPorPagina >0;

		boolean usaSitHistoricoPlc = jpaUtil.hasSitHistoricoPlc(getCfg(context),classe);

		String ordenacao = annotationUtil.getEntityOrder(classe,null);

		try {

			List l=null;
			Object entityArg = classe.newInstance();
			String where=null;
			if (usaSitHistoricoPlc) {
				propertyUtilsBean.setProperty(entityArg,VO.SIT_HISTORICO_PLC,VO.SIT_ATIVO);
				where = "sitHistoricoPlc='A'";
			}

			where = findListTreeViewCompleteArgs(classe,classeBase,idClasseBase,posIni,entityArg,where);

			if (temPaginacao && posIni==-1) {
				Long tot = findListCountQBE(context,classe,where,entityArg);
				l = new ArrayList(0);
				l.add(tot);
				if (log.isDebugEnabled())
					log.debug( "Encerrou contagem para explorer com "+tot+" registros");
			} else if (temPaginacao && posIni>-1) {
				// posIniInt, se for 1, tem que iniciar do zero
				l = findListPagedQBE(context,classe,where,ordenacao,entityArg,posIniInt-1,numPorPagina);

			} else if (!temPaginacao) {
				l = findListPagedQBE(context,classe,where,ordenacao,entityArg,-1,-1);
			}

			return l;
			
		} catch(PlcException plcE){
			throw plcE;
		} catch (Exception e) {
			throw new PlcException("PlcBaseJpaDAO", "findListTreeView", e, log, "");
		}	
	}

	/**
	 * Permite que se componha de forma especifica os argumentos
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param classe classe a ter seus objetos recuperados
	 * @param classeBase classe base cujo objeto com object-id 'idClasseBase' é o pai da atual recuperação
	 * @param idClasseBase object-id da classe base
	 * @param posIni Posiçao para recuperação (-1 para todos)
	 * @param voArg Vo de argumentos para QBE a ser composto.
	 * @since jCompany 3.0
	 */
	protected String findListTreeViewCompleteArgs(Class classe, Class classeBase, Object idClasseBase, long posIni, Object voArg,String where)  {

		if(idClasseBase == null) {
			return "";
		} else {
			if (logPersistencia.isDebugEnabled()) {
				logPersistencia.debug( this.getClass().getSimpleName()+":recuperaListaExplorerCompoeArgs:");
			}
			
			String prop = annotationUtil.getEntityDefaultPropName(classeBase,null);
			
			// Se for recursivo, permite um ajuste no nome da propriedade.
			if (classe.getName().equals(classeBase.getName())) {
				PlcEntityTreeView entidade = (PlcEntityTreeView) classeBase.getAnnotation(PlcEntityTreeView.class);
				if (!"".equals(entidade.recursividadeNomeProp())){
					prop = entidade.recursividadeNomeProp();
				}
			}
			
			try {
				Object voPai = classeBase.newInstance();
				propertyUtilsBean.setProperty(voPai,"id",idClasseBase);
				if (log.isDebugEnabled()){
					log.debug( "Vai incluir valor "+voPai+" na propriedade "+prop+" do VO "+voArg);
				}
				propertyUtilsBean.setProperty(voArg,prop,voPai);
				if (where == null){
					return prop+".id=:"+prop;
				} else {
					return where + " and "+prop+".id=:"+prop;
				}
			} catch(PlcException plcE){
				throw plcE;				
			} catch (Exception e) {
				throw new PlcException("PlcBaseJpaDAO", "recuperaListaExplorerCompoeArgs", e, log, "");
			}
		}
	}

	/**
	 * Cria o objeto Query a partir de um HQL enviado. Este método é chamado de todos os demais métodos do DAO, 
	 * permitindo assim um ponto de sobreposição único para lógicas que necessitam alterar dinamicamente
	 * o HQL, ou mesmo incluir novos parametros ou modificações quaisquer no objeto Query.
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param sess Sessao a ser utilizada
	 * @param hql HQL a ser utilizado para criação da Query
	 * @return Query, podendo ter modificações em descendentes
	 */
	protected Query apiCreateQuery(PlcBaseContextVO context, Class classeEntidade, String hql)  {

		if (logPersistencia.isDebugEnabled()){
			logPersistencia.debug( this.getClass().getSimpleName()+":apiCreateQuery:");
		}
		
		String fabrica = dBFactoryUtil.getDBFactoryName(null, classeEntidade, context);
				
		EntityManager em = getEntityManager(context, fabrica);
		Query jpaQuery = em.createQuery(hql);
		jpaUtil.applyTransformer(classeEntidade, hql, jpaQuery);
		return jpaQuery;
	}


	/**
	 * Cria uma query, executa e retorna lista de resultados
	 */
	@Override
	protected  List apiNewExecute(PlcBaseContextVO context, Class classeVO, String hql)  {

		if (logPersistencia.isDebugEnabled()) {
			logPersistencia.debug( this.getClass().getSimpleName()+":apiCriaExecuta("+hql+"):");
		}
		try {
			Query q = apiCreateQuery(context, classeVO, hql);
			return q.getResultList();
		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcBaseJpaDAO", "apiNewExecute", e, log, "");
		}
	}

	
	/**
	 * Transforma o proxy de uma entidade em um objeto real, recupperando todo seu grafo. 
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param collection
	 * @since jCompany 5.0
	 */	
	protected void proxyToRealObject(PlcBaseContextVO context, Collection<?> collection) {
		//percore cada atributo para identificar classe que contenha proxy
		for (Iterator<?> iter = collection.iterator(); iter.hasNext();) {
			Object voDet = (Object) iter.next();
			voDet = findCompleteGraph(context, voDet);
		}
	}	
	
	/**
	 * Transforma o proxy de uma entidade em um objeto real, recupperando todo seu grafo. 
	 * @since jCompany 5.0
	 */
	@Override
	protected  Object proxyToRealObject(PlcBaseContextVO context, Object entity) {
		//FIXME - Implementar de maneira genérica p/ JPA
		if (HibernateProxy.class.isAssignableFrom(entity.getClass())) {
			entity = ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
		}

		Map<String, Object> propriedades = Collections.emptyMap();

		try {
			propriedades = (Map<String, Object>) propertyUtilsBean.describe(entity);
		} catch (Exception e) {
			log.debug( "proxyObjetoReal: ", e);
		}

		for (String nome : propriedades.keySet()) {
			try {
				Field f = reflectionUtil.findFieldHierarchically(entity.getClass(), nome);
				// Propriedades com Id, Column e não tenham Version
				
				if (f != null) {
					Object valor = propriedades.get(nome);
					if (f.isAnnotationPresent(ManyToOne.class)) {
						if (valor != null && HibernateProxy.class.isAssignableFrom(valor.getClass())) {
							//tenta primeiro obter atraves da queryLookup.
							HibernateProxy hProxy = (HibernateProxy) valor;
							LazyInitializer lInitializer = hProxy.getHibernateLazyInitializer();
							valor = findAggregateLookupByNamedQuery(context, Class.forName(lInitializer.getEntityName()),null,lInitializer.getIdentifier());
							//senao deixa o hibernate inicializar. Isso tem problema de performance, faz um 'from'.
							if(valor==null) {
								valor = lInitializer.getImplementation();
							}
							reflectionUtil.setEntityPropertyPlc(entity, nome, valor);
						}
					} else if (f.isAnnotationPresent(Embedded.class)) {
						if(valor != null) {
							reflectionUtil.setEntityPropertyPlc(entity, nome, proxyToRealObject(context, valor));
						}
					}
				}
			} catch (Exception e) {
				log.debug( "Propriedade '"+nome+"'não encontrada!");
			}
		}

		return entity;
	}
	
	/**
	 * Ajusta primeira query inicializa, para treeview, trocando argumento pai para is null
	 */
	@Override
	protected  String newQueryWithArgsTreeview(String query, Object[] valorArgs) {
		if ((valorArgs == null || valorArgs.length==0) && query.indexOf(":id")>-1) {
			return replaceOnce(replaceOnce(query, "= :id"," is null"), "=:id"," is null");
		}
		return query;
	}

	/**
	 * Procura agregado pela NamedQuery
	 * @param context
	 * @param classe
	 * @param nomePropriedade
	 * @param valor
	 * @return
	 */
	protected Object findAggregateLookupByNamedQuery(PlcBaseContextVO context, Class<? extends Object> classe, String nomePropriedade, Object valor)   {

		NamedQuery namedQuery = annotationPersistenceUtil.getNamedQueryByName(classe, PlcConstants.ANOTACAO.SUFIXO_QUERYSEL_LOOKUP);
		if (namedQuery!=null) {
			EntityManager em = getEntityManager(context, dBFactoryUtil.getDBFactoryName(null, classe, context));
			try{
				Query query = apiCreateQuery(context, classe, namedQuery.query());
				String queryString = query.toString();
				if (queryString.contains("?") || namedQuery.query().contains("?")) {
					query.setParameter(1, valor);
				} else {
					query.setParameter(nomePropriedade, valor);
				}
				List list = query.getResultList();
				if (list.size()>0) {
					return list.get(0);
				}
			}
			catch (Exception e){
				return null;
			}
		}
		return null;
	}

	/**
	 * Recupera valores de propriedades de um VO informado, a partir de um campo qualquer, devolvendo os valores preenchidos no próprio VO.
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param nomePropriedade nome da propriedade de indexação. Se null, considera OID
	 */
	protected Object findWithGraph(PlcBaseContextVO context, Object baseVO, String nomePropriedade)  {

		try {

			if (nomePropriedade==null || nomePropriedade.trim().length()==0) {
				nomePropriedade = "id";
			}

			String hql = "from "+baseVO.getClass().getName()+" where "+nomePropriedade+"=?";

			Object valor = propertyUtilsBean.getProperty(baseVO, nomePropriedade);

			Query q = apiCreateQuery(context, baseVO.getClass(),hql);
			List l = q.setParameter(1,valor).getResultList();

			return l.size()==0 ? null : l.get(0);

		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcBaseJpaDAO", "findWithGraph", e, log, "");
		}
	}

	/**
	 * Recupera um VO informado e seu grafo (lazy=false), a partir de uma propriedade qualquer, que deve vir preenchida no VO passado.
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param nomePropriedade nome da propriedade pesquisada. Se for null, considera OID.
	 * @return instancia do VO recuperada, contendo coleções e classes membro do grafo (lazy=false)
	 */
	protected Object findWithoutGraph(PlcBaseContextVO context, Object baseVO, String nomePropriedade,  String[] props)  {

		try {

			if (nomePropriedade==null || nomePropriedade.trim().length()==0) {
				nomePropriedade = "id";
			}

			if (!"id".equals(nomePropriedade) && !props[0].equals("id")) {
				String[] _props = new String[props.length+1];
				_props[0] = "id";
				System.arraycopy(props, 0, _props, 1, props.length);
				props=_props;
			}

			String hql = "select "+jpaUtil.createSelectFromStringArray(props)
			+" from "+baseVO.getClass().getName()+" where "+nomePropriedade+"=?";

			Object valor = propertyUtilsBean.getProperty(baseVO, nomePropriedade);

			Query q = apiCreateQuery(context, baseVO.getClass(),hql);

			List l = q.setParameter(1,valor).getResultList();

			if (l.size()==0) {
				return null;
			}

			if (props.length>1) {
				baseVO = entityCommonsUtil.fillEntityByObjectArray(baseVO,  props, (Object[])l.get(0));
			} else {
				propertyUtilsBean.setProperty(baseVO,props[0],l.get(0));
			}
			return baseVO;

		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcBaseJpaDAO", "findWithoutGraph", e, log, "");
		}

	}


	/**
	 * Executa um "select count(*)" do tipo "naoDeveExistir"
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param modo
	 * @param hql
	 * @param plcVO
	 * @return Total de registros e valores informados
	 * @return Total de registros e valores informados
	 * @since jCompany 5.0
	 */
	@Override
	protected Object[] findCountNoSimilar(PlcBaseContextVO context, String modo, String hql, Object plcVO)  {

		try {

			Object[] ret = handleNulls(hql,plcVO);

			hql = (String) ret[0];
			String valorMsg = (String) ret[1];
			Map propriedadesAgregadas = (Map) ret[2];

			String nomeArgs[] = qbeUtil.destileArgsHql(hql);

			Query q = apiCreateQuery(context, plcVO.getClass(), hql);

			for(int j=0;nomeArgs[j]!=null;j++) {
				if(propertyUtilsBean.isReadable(plcVO, nomeArgs[j]))
					q.setParameter(nomeArgs[j], propertyUtilsBean.getProperty(plcVO, nomeArgs[j]));
			}

			log.debug( "Depois criar query e setProperties");

			Set params = propriedadesAgregadas.keySet();
			Iterator i = params.iterator();
			String param = "";

			while (i.hasNext()) {
				param = (String) i.next();
				q.setParameter(param,propriedadesAgregadas.get(param));
			}

			log.debug( "Antes de submeter query");

			Iterator contagem = q.getResultList().iterator();

			Object row = (Object) contagem.next();

			if (row instanceof Long) {
				row = new Integer(((Long)row).intValue());
			}

			return  new Object[]{(Integer) row,valorMsg};

		} catch(PlcException plcE){
			throw plcE;				
		} catch (Exception e) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_PERSISTENCE_DUPLICITY,new Object[] {"findCountNoSimilar", e},e,log);
		}
	}

	public Object findAggregateLookup(PlcBaseContextVO context, Object baseVO, Map<String, Object> propriedadesValores)  {

		try {

			Object baseVORecuperado = findAggregateLookupByNamedQuery(context, baseVO,propriedadesValores);

			return baseVORecuperado;

		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcBaseJpaDAO", "findAggregateLookup", e, log, "");
		}    	
	}

	/**
	 * Recupera agregado utilizando query nomeada padrão
	 */
	protected Object findAggregateLookupByNamedQuery(PlcBaseContextVO context, Object entity, Map<String, Object> propriedadesValores)  {

		NamedQuery namedQuery = annotationPersistenceUtil.getNamedQueryByName(entity.getClass(), PlcConstants.ANOTACAO.SUFIXO_QUERYSEL_LOOKUP);

		if (namedQuery!=null) {
			EntityManager em = getEntityManager(context, dBFactoryUtil.getDBFactoryName(null, entity.getClass(), context));
			String queryString 				= namedQuery.query();
			Query query 					= apiCreateQuery(context, entity.getClass(), queryString);
			PlcPrimaryKey chavePrimaria 	= entity.getClass().getAnnotation(PlcPrimaryKey.class);
			try{	
				int index=1;
				for(String propriedade : propriedadesValores.keySet()){

					if (queryString.contains("?")) {
						if (chavePrimaria == null)	{
							// É OID ou então é um campo auxiliar 
							if (propriedade != null && "id".equals(propriedade.toLowerCase())) {
								query.setParameter(index,  new Long(""+propriedadesValores.get(propriedade)));
							}else {
								//é Recuperação por propriedade auxliar Ex: cpf
								NamedQuery namedQuery2 	= annotationPersistenceUtil.getNamedQueryByName(entity.getClass(), PlcConstants.ANOTACAO.SUFIXO_QUERYSEL_LOOKUP + capitalize(propriedade));
								if (namedQuery2 != null) {
									query 	= apiCreateQuery(context, entity.getClass(), namedQuery2.name());
								} else{
									String queryPropriedade = queryString.substring(0, queryString.indexOf("where")) + " where " + propriedade + "= ? ";
									query 	= apiCreateQuery(context, entity.getClass(), queryPropriedade);
								}
								query.setParameter(index, propriedadesValores.get(propriedade));

							}
						}
						else{
							Class idNatural 	= propertyUtilsBean.getPropertyType(entity, "idNatural");
							Class typeOfValue 	= propertyUtilsBean.getPropertyType(idNatural.newInstance(), propriedade);
							Object value		= propriedadesValores.get(propriedade);
							if (typeOfValue != null && Long.class.isAssignableFrom(typeOfValue) && value instanceof String) {
								query.setParameter(index,  new Long((String)value));
							} else {
								if (typeOfValue != null && Date.class.isAssignableFrom(typeOfValue) && value instanceof String){
									query.setParameter(index,  dateUtil.stringToDate(((String)value)));
								} else { 
									if (typeOfValue != null && Integer.class.isAssignableFrom(typeOfValue) && value instanceof String) {
										query.setParameter(index,  new Integer((String)value));
									} else {
										query.setParameter(index,  value);
									}
								}
							}
						}
					} else {
						if (chavePrimaria == null){
							query.setParameter(propriedade, propriedadesValores.get(propriedade));
						} else{
							Class idNatural 	= propertyUtilsBean.getPropertyType(entity, "idNatural");
							Class typeOfValue 	= propertyUtilsBean.getPropertyType(idNatural.newInstance(), propriedade);
							Object value		= propriedadesValores.get(propriedade);
							if (typeOfValue != null && Long.class.isAssignableFrom(typeOfValue) && value instanceof String) {
								query.setParameter(propriedade,  new Long((String)value));
							} else
								if (typeOfValue != null && Date.class.isAssignableFrom(typeOfValue) && value instanceof String){
									query.setParameter(propriedade,  dateUtil.stringToDate(((String)value)));
								} else {
									if (typeOfValue != null && Integer.class.isAssignableFrom(typeOfValue) && value instanceof String) {
										query.setParameter(propriedade,  new Integer((String)value));
									} else{
										query.setParameter(propriedade,  propriedadesValores.get(propriedade));
									}
								}
						}
					}

					index++;
				}
			} catch(PlcException plcE){
				throw plcE;				
			}catch(Exception e){
				throw new PlcException("PlcBaseJpaDAO", "findAggregateLookupByNamedQuery", e, log, "");
			}
			List list = query.getResultList();
			if (list.size()>0) {
				return list.get(0);
			}
			else {
				return null;
			}
		}
		else {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_LOOKUP_NAMED_QUERY_NOT_FOUND, new Object[] { entity.getClass()}, true);
		}

	}

	public Object deletePagedDetails(PlcBaseContextVO context, Object entity, String nomeDetalhe, Class classeDetalhe)  {
		//TODO - Implementar para JPA - excluiDetalhesPaginados
		return null;
	}

	public Map<String, FilterDefinition> findFilterDefs(PlcBaseContextVO context, Class classe)  {
		//TODO - Implementar para JPA - recuperaFilterDefs
		return null;
	}


	//******************************************************************************************* 
	//********************************  INICIO TRATAMENTO MSG *********************************** 
	//******************************************************************************************* 

	/**
	 * Deve verificar o tipo da Exceção e devolver msg de erro apropriada
	 * @param causaRaiz Exception a ser investigada
	 * @return null se não for de responsabilidade da persistencia ou String[0]: msg internacionalizada,
	 * String[1]: arg1 (opcional) e String[2]: arg2 (opcional)
	 */
	@Override
	public Object[] findExceptionMessage(Throwable causaRaiz)  {

		Throwable causaRaizAux = null;

		do {
			if (causaRaiz != null && (TransactionRequiredException.class.isAssignableFrom(causaRaiz.getClass()))) {
				// Se é erro de transacao
				return new Object[]{PlcBeanMessages.JCOMPANY_ERROR_GENERIC, causaRaiz.getMessage() };
			} else if (causaRaiz != null && (SQLException.class.isAssignableFrom(causaRaiz.getClass()))) {
				// Se é erro de JDBC
				return msgExceptionHandleSQL((SQLException) causaRaiz);
			} else if (causaRaiz != null && (javax.validation.ConstraintViolationException.class.isAssignableFrom(causaRaiz.getClass()))) {
				// Se é erro de validacao
				return new Object[]{PlcBeanMessages.JCOMPANY_ERROR_GENERIC, causaRaiz.getMessage() };
			} else if (causaRaiz != null && (javax.validation.ValidationException.class.isAssignableFrom(causaRaiz.getClass()))) {
				// Se é erro de validacao
				return new Object[]{PlcBeanMessages.JCOMPANY_ERROR_GENERIC, causaRaiz.getMessage() };
			} else if (causaRaiz != null && causaRaiz instanceof HibernateException) {
				// Trata erros de Hibernate (não controlados)
				return msgExceptionHandleHibernate((HibernateException) causaRaiz);
			} else if (causaRaiz != null && causaRaiz.getCause()!=null && causaRaiz.getCause() instanceof HibernateException) {
				// Trata erros de Hibernate (não controlados)
				return msgExceptionHandleHibernate((HibernateException) causaRaiz.getCause());
			} else if (PlcException.class.isAssignableFrom(causaRaiz.getClass())) {
				causaRaiz = ((PlcException) causaRaiz).getCausaRaiz();
				if (causaRaiz != null)
					causaRaizAux = causaRaiz;
			} else {
				causaRaizAux = causaRaiz;
				break;
			}
		} while (causaRaiz != null);

		String[] ret = apiHandleModelSpecificErrors(causaRaizAux);
		return ret;

	}


	/**
	 * Trata erros de JDBC e devolve msg apropriada genérica
	 */
	protected Object[] msgExceptionHandleSQL (SQLException erroSQL) {
		
		PlcConfigUtil configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);
		Boolean showInternalMessages = true;
		if (configUtil != null){
			PlcConfigAppBehavior appBehavior = configUtil.getConfigApplication().behavior();
			showInternalMessages = appBehavior.showInternalMessages();
		}

		if (erroSQL.getErrorCode() == 2292) {
			return new Object[]{PlcBeanMessages.JCOMPANY_ERROR_COMMIT_INTEGRITY, showInternalMessages ? erroSQL.getLocalizedMessage() : ""};
		} else if (erroSQL.getErrorCode() == 1400) {
			return new Object[]{PlcBeanMessages.JCOMPANY_ERROR_COLUMN_NULL, showInternalMessages ? erroSQL.getLocalizedMessage() : ""};
		} else if (erroSQL.getErrorCode() == 1401) {
			return new Object[]{PlcBeanMessages.JCOMPANY_ERROR_VALUE_TOO_LONG, showInternalMessages ? erroSQL.getLocalizedMessage() : ""};
		} else if (erroSQL.getErrorCode() == 0001 || erroSQL.getSQLState().equals("23505")) {
			return new Object[]{PlcBeanMessages.JCOMPANY_ERROR_SQL_DUPLICATED_KEY, showInternalMessages ? erroSQL.getLocalizedMessage() : ""};
		} else if (erroSQL.getErrorCode() == 1407) {
			return new Object[]{PlcBeanMessages.JCOMPANY_ERROR_SQL_COLUMN_NULL, showInternalMessages ? erroSQL.getLocalizedMessage() : ""};
		} else if (erroSQL.getErrorCode() == -1 && erroSQL.getMessage().contains("foreign key constraint")){
			return new Object[]{PlcBeanMessages.JCOMPANY_ERROR_PERSISTENCE_DELETE_CONSTRAINT, showInternalMessages ? erroSQL.getLocalizedMessage() : ""};
		} else if (erroSQL.getErrorCode() == 2291){
			return new Object[]{PlcBeanMessages.JCOMPANY_ERROR_SUPERIOR_HIERARCHY_NOT_FOUND, showInternalMessages ? erroSQL.getLocalizedMessage() : ""};
		}else if (erroSQL.getErrorCode() == 20000) {
			return new Object[]{PlcBeanMessages.JCOMPANY_ERROR_SQL_DUPLICATED_KEY, showInternalMessages ? erroSQL.getLocalizedMessage() : ""};
		} else{
			return new Object[]{PlcBeanMessages.JCOMPANY_ERROR_SQL_UNEXPECTED, showInternalMessages ? erroSQL.getLocalizedMessage() : ""};
		}
	}

	/**
	 * Trata erros capturados pela Hibernate
	 * @since jCompany 3.5
	 */
	public Object[] msgExceptionHandleHibernate (HibernateException erroH) {

		if (erroH instanceof StaleObjectStateException) {
			return new Object[]{PlcBeanMessages.JCOMPANY_ERROR_COMMIT_CONCURRENCY, erroH.getLocalizedMessage()};
		} else if (erroH instanceof PropertyValueException) {
			String atributo = ((PropertyValueException)erroH).getPropertyName();
			return new Object[]{PlcBeanMessages.JCOMPANY_ERROR_ATTRIBUTE_NULL_NOT_FILLED, atributo,erroH.getLocalizedMessage(),((PropertyValueException)erroH).getMessage()};
		} else if (erroH instanceof ObjectNotFoundException) {
			return new Object[]{PlcBeanMessages.JCOMPANY_ERROR_COMMIT_NOT_FOUND, erroH.getLocalizedMessage()};
		} else if (erroH instanceof IdentifierGenerationException) {
			return new Object[]{PlcBeanMessages.JCOMPANY_ERROR_COMMIT_KEY_NOT_FILLED, erroH.getLocalizedMessage()};
		} else if (erroH.getLocalizedMessage().indexOf("Another object was associated with this id") > -1) {
			return new Object[]{PlcBeanMessages.JCOMPANY_ERROR_SQL_DUPLICATED_KEY, erroH.getLocalizedMessage()};	
		} else if (erroH.getLocalizedMessage().indexOf("Could not execute JDBC batch update") > -1 ) {
			return new Object[]{PlcBeanMessages.JCOMPANY_ERROR_SQL_DUPLICATED_KEY, erroH.getLocalizedMessage()};	
		} else if (erroH instanceof NonUniqueObjectException){ 
			return new Object[]{PlcBeanMessages.JCOMPANY_ERROR_SQL_DUPLICATED_KEY, erroH.getLocalizedMessage()};	
		} else if (erroH instanceof ConstraintViolationException){ 
			if (erroH.getCause()!=null && SQLException.class.isAssignableFrom(erroH.getCause().getClass()))
				return msgExceptionHandleSQL((SQLException)erroH.getCause());
			else
				return new Object[]{PlcBeanMessages.JCOMPANY_ERROR_PERSISTENCE_DELETE_CONSTRAINT,erroH.getLocalizedMessage()};
		} else {
			return new Object[]{PlcBeanMessages.JCOMPANY_ERROR_GENERIC,erroH.getLocalizedMessage()};
		}
	}


	/**
	 * @since jCompany 3.5
	 * 
	 * Template-Method para especialização do tratamento de mensagens da camada modelo
	 * @param causaRaiz Exceçao
	 * @return String[] contendo mensagem + args. Exemplo: return new String[]{"apl.erros.commit.concorrencia",causaRaiz.toString()};
	 * ou null se a exceção nao form reconhecida para a camada modelo
	 */
	protected String[] apiHandleModelSpecificErrors(Throwable causaRaiz)  {
		return new String[]{"",""};
	}

	/**
	 * Chamado por reflexão no novo padrão de QBE
	 */
	public Long findCount(PlcBaseContextVO context, Object entidadeArg){

		try {

			String queryCount = qbeUtil.getQueryCount(entidadeArg.getClass());
			if (queryCount==null) {
				queryCount = qbeUtil.getQuerySelDefault(context, entidadeArg.getClass());
				queryCount = "select count(*) " + queryCount.substring(queryCount.indexOf("from"), queryCount.length());
				queryCount = removeGroupbyOrderby(queryCount);
			}

			Query q = createQueryWithEntity(context, entidadeArg, queryCount, null);				
			return (Long)q.getSingleResult();
		} catch(PlcException plcE){
			throw plcE;			
		}catch(Exception e){
			throw new PlcException("PlcBaseJpaDAO", "findCount", e, log, "");
		}		
	}


	/**
	 * Chamado por reflexão no novo padrão de QBE, repassando o contexto com as modificações do caso de uso
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param entidadeArg
	 * @param orderBy
	 * @param primeiraLinha
	 * @param maximoLinhas
	 * @return Lista de Objetos
	 */
	public List findList(PlcBaseContextVO context, Object entidadeArg, String orderBy, int primeiraLinha, int maximoLinhas){

		try {
			
			if(context == null) {
				context = context;
			}
			
			String query = qbeUtil.getQuerySelDefault(context, entidadeArg.getClass());
			Query q = createQueryWithEntity(context, entidadeArg, query, orderBy);

			if (primeiraLinha >= 0) {
				q.setFirstResult(primeiraLinha);
			}
			if (maximoLinhas >= 1) {
				q.setMaxResults(maximoLinhas);
			}

			return q.getResultList();

		} catch(PlcException plcE){
			throw plcE;
		} catch(Exception e){
			throw new PlcException("PlcBaseJpaDAO", "findList", e, log, "");
		}		
	}

	/**
	 * 
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param entidadeArg
	 * @param query
	 * @param orderBy
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	protected Query createQueryWithEntity(PlcBaseContextVO context, Object entidadeArg, String query, String orderBy) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		//TODO 6.0 - Tratar tipos primitivos que vem com valor padrão
		
		String where = "";
		Object[] valores = {null};
		Field[] fields = {null};
		
		// A lógica de preferencia usuário sempre faz a query baseada no login
		if (context != null && FormPattern.Usu.name().equals(context.getFormPattern())) {
			//Pegando o valor definido para login
			valores[0] = propertyUtilsBean.getProperty(entidadeArg, context.getArgPreference());
			try {
				fields[0] = reflectionUtil.findFieldHierarchically(entidadeArg.getClass(), context.getArgPreference());
			} catch(PlcException plcE){
				throw plcE;				
			} catch (Exception e) {
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_LOGIN_FIELD_NOT_FOUND, new Object[]{"createQueryWithEntity", context.getArgPreference()}, true);	
			}
		} else {	
			fields = reflectionUtil.findAllFieldsHierarchicallyWithoutTransientsAndStatics(entidadeArg.getClass());
			
			valores = new Object[fields.length];
			int index=0;
			for (Field field : fields) {
				Object v = propertyUtilsBean.getProperty(entidadeArg, field.getName());
				if (v !=null && !PlcConstants.VO.DATA_ULT_ALTERACAO.equals(field.getName())
						&& !PlcConstants.VO.USUARIO_ULT_ALTERACAO.equals(field.getName())
						&& !PlcConstants.VO.VERSAO.equals(field.getName())
						&& !PlcConstants.VO.SIT_HISTORICO_PLC.equals(field.getName())
						&& !ArrayUtils.contains(PlcConstants.VO.ALM_AUDIT, field.getName())) {
					if (v instanceof String) {
						where+=  " upper(" + qbeUtil.getAliasWithDot(query, entidadeArg.getClass()) + field.getName() + ") like '%' || upper(:"+field.getName()+") || '%' and ";
					} else {
						where+=  qbeUtil.getAliasWithDot(query, entidadeArg.getClass())+field.getName()+" = :"+field.getName()+" and ";
					}	
					valores[index] = v;
				}
				index++;
			}
		}

		if (where!=null && where.endsWith("and "))
			where = where.substring(0, where.lastIndexOf("and "));
		if (where!=null && !where.equals(""))
			query = qbeUtil.completeSelectWhere(query, where);
		if (StringUtils.isNotBlank(orderBy))
			query+=" order by "+orderBy;
		
		Query q = apiCreateQuery(context, entidadeArg.getClass(), query);
		for (int i = 0; i < valores.length; i++) {
			if (valores[i]!=null)
				q.setParameter(fields[i].getName(), valores[i]);
		}
		return q;
	}	


	/**
	 * Recupera a lista detalhe paginada e remove o proxy das classes com proxy
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param entidadeArg
	 * @param ordenacaoPlc
	 * @param posicaoAtual
	 * @param numPorPagina
	 * @return
	 * @throws PlcException
	 */
	public Collection<Object> findListPagedDetail(PlcBaseContextVO context, Object entidadeArg, String ordenacaoPlc, int posicaoAtual, int numPorPagina) throws PlcException {

		//Transforma proxy do objeto principal no objeto real, caso ele seja um
		entidadeArg = proxyToRealObject(context, entidadeArg);
		Collection<Object> collection  = null;

		try {

			//recupera coleção de objetos
			collection = findList(context, entidadeArg, ordenacaoPlc, posicaoAtual, numPorPagina);

			proxyToRealObject(context, collection);

		} catch(PlcException plcE){
			throw plcE;			
		}  catch (Exception e) {
			throw new PlcException("PlcBaseJpaDAO", "findListPagedDetail", e, log, "");
		}

		return collection;
	}

	private Metamodel getCfg(PlcBaseContextVO context)  {
		return ((PlcBaseJpaManager)iocModelUtil.getFactoryManager("default")).getMetamodel(context);
	}
	
	private Throwable getCause(Exception e) {
		
		if(e instanceof PlcException) {
			return ((PlcException)e).getCausaRaiz(); 
		} else {
			if(e != null && e.getCause() != null) {
				return e.getCause();
			} else {
				return e;
			}
		}
	}

	private Object[] getException(String method, Object[] handledException) {
		
		return new Object[] {method, handledException != null ? handledException[1] : ""};
	}
	
	private String handlePrimaryKey(String queryEdita, Class classeEntity, Object id) {

		if (id != null && metamodelUtil.isEntityClass(classeEntity) && metamodelUtil.getEntity(classeEntity).isIdNatural())	{
			return queryEdita.replace("idNatural=?", "idNatural=:id");
		}

		return queryEdita;
	}	
}

