/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.

		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.util.AnnotationLiteral;

import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.IPlcFile;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.model.bindingtype.PlcEditAfter;
import com.powerlogic.jcompany.model.bindingtype.PlcEditBefore;
import com.powerlogic.jcompany.model.bindingtype.PlcEditListBefore;
import com.powerlogic.jcompany.model.bindingtype.PlcFindCountAfter;
import com.powerlogic.jcompany.model.bindingtype.PlcFindCountBefore;
import com.powerlogic.jcompany.model.bindingtype.PlcFindListAfter;
import com.powerlogic.jcompany.model.bindingtype.PlcFindListBefore;
import com.powerlogic.jcompany.persistence.PlcBaseDAO;
import com.powerlogic.jcompany.persistence.jpa.PlcBaseJpaDAO;
import com.powerlogic.jcompany.persistence.jpa.PlcQuery;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryFirstLine;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryLineAmount;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryOrderBy;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryParameter;

/**
 * Edita objetos (recupera do repositório para edição)
 */
@QPlcDefault
@ApplicationScoped
public class PlcBaseEditRepository extends PlcBaseParentRepository {

	/**
	 * Recupera coleção de entidades paginada (ex: 1 a 20 de 200) para edição
	 */
	public List<Object> editList(PlcBaseContextVO context, Class classe, String orderyByDinamico, int primeiraLinha, int maximoLinhas)  {

		PlcBaseDAO dao = iocModelUtil.getPersistenceObject(classe);
		List<Object> lista=null;

		if (editListBefore(context,dao,orderyByDinamico, primeiraLinha, maximoLinhas)) {
			lista = dao.findListPagedQBE(context, classe,orderyByDinamico,null,null,primeiraLinha,maximoLinhas);
		}

		return editListAfter(context, dao,orderyByDinamico, primeiraLinha, maximoLinhas, lista);

	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 */
	protected boolean editListBefore(PlcBaseContextVO context,Object dao,String orderyByDinamico, int primeiraLinha, int maximoLinhas)  {
		context.setDefaultProcessFlow(true);
		PlcCDIUtil.getInstance().fireEvent(context, new AnnotationLiteral<PlcEditListBefore>(){});
		return context.isDefaultProcessFlow();
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 */
	protected List<Object> editListAfter(PlcBaseContextVO context, Object dao,String orderyByDinamico, 
			int primeiraLinha, int maximoLinhas, List<Object> lista)  {	
		return lista;
	}

	/**
	 * Recupera um objeto da camada de persistência para edição<p>
	 */
	public Object[] edit ( PlcBaseContextVO context, Class classe, Object id)  {

		try {

			Object entidade = null;

			List lookupNavegacaoLista = null;

			PlcBaseDAO dao = iocModelUtil.getPersistenceObject(classe);

			if (editBefore(context,dao,classe,id)) {

				// Importante: Se não tiver filtro ou for download padrao de arquivos, recupera com a chave passada
				if (context==null || StringUtils.isBlank(context.getVerticalFilter()) || IPlcFile.class.isAssignableFrom(classe)) {
					entidade = dao.findById(context, classe, id);			
				} else {
					entidade = dao.findByIdWithFilter(context, classe,id);
				}
				
			}

			editAfter(context,dao,classe,id,entidade);

			if (log.isDebugEnabled()) {
				log.debug( "Vai devolver entidade="+entidade);
			}
			
			return new Object[]{entidade,null,lookupNavegacaoLista};

		} catch (PlcException ePlc) {
			throw ePlc;
		} catch (Exception e) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_PERSISTENCE_FIND,new Object[] {"find",e},e,log);
		}
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 */
	protected boolean editBefore(PlcBaseContextVO context,Object dao,Class classe, Object id)  {
		context.setDefaultProcessFlow(true);
		PlcCDIUtil.getInstance().fireEvent(context, new AnnotationLiteral<PlcEditBefore>(){});
		return context.isDefaultProcessFlow();
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 */
	protected void editAfter(PlcBaseContextVO context,Object dao,Class classe, Object id, Object entidade)  {
		context.setEntityForExtension(entidade);
		PlcCDIUtil.getInstance().fireEvent(context, new AnnotationLiteral<PlcEditAfter>(){});
	}

	/**
	 * Recupera objetos a partir da selecao de uma entidade de origem
	 * @return Coleção de Entidades
	 */
	public List<Object> editNavigation(PlcBaseContextVO context, Class classeOrigem, Object pk,Class classeDestino)  {

		try {

			PlcBaseDAO dao = iocModelUtil.getPersistenceObject(classeDestino);

			List l = null;

			if (editNavigationBefore(context,dao,classeOrigem,pk,classeDestino)) {

				// Recupera
				l = dao.findNavigationAggregate(context, classeOrigem,pk,classeDestino);

			}

			return editNavigationAfter(context,dao,classeOrigem,pk,classeDestino,l);

		}catch(PlcException ePlc){
			throw ePlc;
		} catch (Exception e) {
			throw new PlcException("PlcBaseEditRepository", "findNavigationAggregate", e, log, "");			
		}

	}

	/**
	 * Evento para implementação em Template Methods, ocorrendo antes da recuperação de classes via navegação automatizada
	 */
	protected boolean editNavigationBefore(PlcBaseContextVO context, PlcBaseDAO dao, 
			Class classeOrigem, Object pk,Class classeDestino)  {
		context.setDefaultProcessFlow(true);
		return context.isDefaultProcessFlow();
	}

	/**
	 * Evento para implementação em Template Methods, ocorrendo após a recuperação de classes via navegação automatizada
	 */
	protected List<Object> editNavigationAfter(PlcBaseContextVO plcBaseContextVO, PlcBaseDAO dao, 
			Class classeOrigem, Object pk, Class classeDestino,List<Object> l)  {
		return l;
	}

	/**
	 * Recupera uma entidade de lookup, utilizando um mapa de Propriedades/Valores com metadados para orientar a recuperacao 
	 */
	public Object editLookup(PlcBaseContextVO context, Object entidade, Map<String, Object> propriedadesValores) {

		try {

			// Pega instância de DAO para recuperar
			PlcBaseDAO dao = iocModelUtil.getPersistenceObject(entidade.getClass());
			Object entidadeRecuperada = null;

			if (editLookupBefore(context,dao,entidade,propriedadesValores)) {
				// Recupera
				entidadeRecuperada = dao.findAggregateLookup(context, entidade,propriedadesValores);

			}

			return editLookupAfter(context,dao,entidade,propriedadesValores,entidadeRecuperada);

		}catch(PlcException ePlc){
			throw ePlc;
		} catch (Exception e) {
			throw new PlcException("PlcBaseEditRepository", "findAggregateLookup", e, log, "");
		}
	}

	/**
	 * Design Pattern: Template Method. 
	 * Os métodos com sufixo "Before" ou "After" são destinados a especializações nos descendentes via Template Methods
	 */
	protected boolean editLookupBefore(PlcBaseContextVO context,Object dao,Object entidade,Map<String, Object> propriedadesValores)  {
		if (context!=null)
			context.setDefaultProcessFlow(true);
		// TODO Nao define evento ate comprovar necessidade
		return true;
	}

	/**
	 * Design Pattern: Template Method. 
	 * Os métodos com sufixo "Before" ou "After" são destinados a especializações nos descendentes via Template Methods
	 */
	protected Object editLookupAfter(PlcBaseContextVO context,Object dao,Object entidade, Map<String, Object> propriedadesValores, Object entidadeRecuperada)  {
		return entidadeRecuperada;
	}
	/**
	 * Recupera filhos segundo a regra:<p>
	 * 1. Se tiver mais classes que possuem relacionamentos many-to-one na persistencia<br>
	 * @param context
	 * @param classeBase
	 * @param id
	 * @param classeFilha
	 * @param posIni
	 * @return
	 */
	public List findListTreeView(PlcBaseContextVO context, Class classeBase, Object id,Class classeFilha,long posIni)  {


		// Lista conterá objetos ou relação de classes
		List listaFilhos=null;

		if (classeFilha!=null) {
			// É para recuperar dados de classeFilha relacionados ao objeto da classeBase que possui o OID informado
			PlcBaseDAO baseDAO = iocModelUtil.getPersistenceObject(classeFilha);

			listaFilhos = baseDAO.findListTreeView(context, classeFilha,classeBase,id,posIni); 
		} else {
			// É para recuperar possíveis filhos nos meta-dados, somente
			PlcBaseDAO baseDAO = iocModelUtil.getPersistenceObject(classeBase);
			listaFilhos = baseDAO.getChildren(context, classeBase); 
		}

		return listaFilhos;	

	}

	/**
	 * Recupera lista de entidades para edicao
	 */
	public List<Object> findList(PlcBaseContextVO context, Object entidadeArg, String orderyByDinamico, int primeiraLinha, int maximoLinhas) {

		try {

			PlcBaseDAO dao = iocModelUtil.getPersistenceObject(entidadeArg.getClass());			
			Method [] methods = reflectionUtil.findAllMethodsHierarchicallyByName(dao.getClass().getSuperclass(), "findList");
			String query = context.getApiQuerySel();

			if (findListBefore(context,dao,entidadeArg,orderyByDinamico,primeiraLinha,maximoLinhas,query)) {

				if(StringUtils.isNotBlank(query)) {
					for (Method method : methods) {
						PlcQuery plcQuery = method.getAnnotation(PlcQuery.class);
						if(plcQuery != null && plcQuery.value().equals(query)) {
							List<Object> l = executeMethodList(context, entidadeArg, method, dao, orderyByDinamico, primeiraLinha, maximoLinhas);
							return findListAfter(context,dao,entidadeArg,orderyByDinamico,l);
						} 
					}
				}	
				Method method = methods[0];
				List<Object> l =  executeMethodList(context, entidadeArg, method, dao, orderyByDinamico, primeiraLinha, maximoLinhas);
				return findListAfter(context,dao,entidadeArg,orderyByDinamico,l);
			}	

			return null;

		}catch(PlcException ePlc){
			throw ePlc;	
		} catch (Exception e) {
			throw new PlcException("PlcBaseEditRepository", "findList", e, log, "");			
		}
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 */
	protected boolean findListBefore(PlcBaseContextVO context,Object dao, Object entidadeArg, String orderyByDinamico, int primeiraLinha, int maximoLinhas, String query)  {
		context.setDefaultProcessFlow(true);
		context.setEntityForExtension(entidadeArg);
		PlcCDIUtil.getInstance().fireEvent(context, new AnnotationLiteral<PlcFindListBefore>(){} );
		return context.isDefaultProcessFlow();
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 */
	protected List<Object> findListAfter(PlcBaseContextVO context,Object dao, Object entidadeArg, String orderyByDinamico, List<Object> lista)  {
		context.setEntityForExtension(entidadeArg);
		PlcCDIUtil.getInstance().fireEvent(context, new AnnotationLiteral<PlcFindListAfter>(){});
		return lista;
	}

	/**
	 * Recupera total de objetos para critérios informados no parametro
	 * @param entidadeArg Objeto com argumentos para filtragem
	 * @return Total
	 */
	public Long findCount(PlcBaseContextVO context, Object entidadeArg) {

		try{

			PlcBaseDAO dao = iocModelUtil.getPersistenceObject(entidadeArg.getClass());

			Method [] methods = reflectionUtil.findAllMethodsHierarchicallyByName(dao.getClass().getSuperclass(), "findCount");
			String query = context.getApiQuerySel();

			if (findCountBefore(context,dao,entidadeArg,query)) {

				if(StringUtils.isNotBlank(query)) {
					for (Method method : methods) {
						PlcQuery plcQuery = method.getAnnotation(PlcQuery.class);
						if(plcQuery != null && plcQuery.value().equals(query)) {
							Long total = executeMethodCount(context, method, entidadeArg, dao);
							return findCountAfter(context, dao, entidadeArg, total);
						} 
					}
				}
				
				Method method = methods[0];
				Long total =  executeMethodCount(context, method, entidadeArg, dao);
				return findCountAfter(context, dao, entidadeArg, total);

			}

			return null;

		} catch(PlcException ePlc){
			throw ePlc;			
		} catch (Exception e) {
			throw new PlcException("PlcBaseEditRepository", "findCount", e, log, "");
		}		
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 */
	protected boolean findCountBefore(PlcBaseContextVO context,Object dao, Object entidadeArg, String query)  {
		context.setDefaultProcessFlow(true);
		context.setEntityForExtension(entidadeArg);
		PlcCDIUtil.getInstance().fireEvent(context, new AnnotationLiteral<PlcFindCountBefore>(){} );
		return context.isDefaultProcessFlow();
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 */
	protected Long findCountAfter(PlcBaseContextVO context,Object dao, Object entidadeArg, Long total)  {
		context.setEntityForExtension(entidadeArg);
		PlcCDIUtil.getInstance().fireEvent(context, new AnnotationLiteral<PlcFindCountAfter>(){});
		return total;
	}


	/**
	 * Devolve uma lista com os argumentos da pesquisa, declarados na query nativa do DAO, para uso da Pesquisa QBE.
	 * @param entidadeArg
	 * @param method
	 * @param orderByDinamico
	 * @param primeiraLinha
	 * @param maximoLinhas
	 * @return
	 */
	protected Object[] verifySearchArguments(PlcBaseContextVO context, Object entidadeArg, Method method, String orderByDinamico,  int primeiraLinha, int maximoLinhas) {

		Object[] args = new Object[method.getParameterTypes().length];
		Annotation annotations[][] = method.getParameterAnnotations();
		int i =0;

		for (Annotation[] _annotations : annotations) {
			if (_annotations.length!=0) {
				for (Annotation annotation : _annotations) {
					if (annotation.annotationType().isAssignableFrom(PlcQueryParameter.class)) {
						String nome = ((PlcQueryParameter)annotation).name().replace('_', '.');
						args[i] = reflectionUtil.getFieldValue(entidadeArg, nome);
					} else if (orderByDinamico!=null && annotation.annotationType().isAssignableFrom(PlcQueryOrderBy.class)) {
						args[i] = orderByDinamico;					
					} else if (primeiraLinha>=0 && annotation.annotationType().isAssignableFrom(PlcQueryFirstLine.class)) {
						args[i] = new Integer(primeiraLinha);					
					} else if (maximoLinhas>=0 && annotation.annotationType().isAssignableFrom(PlcQueryLineAmount.class)) {
						args[i] = new Integer(maximoLinhas);		
					}
				}
			} else if (method.getParameterTypes()[i].isAssignableFrom(PlcBaseContextVO.class)) {
				args[i] = context;
			}
			i++;
		}
		return args;
	}	

	protected List<Object> executeMethodList(PlcBaseContextVO context, Object entity, Method method, PlcBaseDAO dao, String orderyByDinamico, int primeiraLinha, int maximoLinhas) throws IllegalAccessException, InvocationTargetException {

		List lista;

		if (method.getDeclaringClass().equals(PlcBaseJpaDAO.class)){
			lista = (List)method.invoke(dao, context, entity, orderyByDinamico, primeiraLinha, maximoLinhas);
			return lista;
		} else {
			Object[] args = verifySearchArguments(context, entity, method, orderyByDinamico, primeiraLinha, maximoLinhas);
			lista = (List)method.invoke(dao, args);
			return lista;
		}

	}  	

	protected Long executeMethodCount(PlcBaseContextVO context, Method method, Object entidadeArg, PlcBaseDAO dao) throws IllegalAccessException, InvocationTargetException {

		Long total;
		method.setAccessible(true);
		if (method.getDeclaringClass().equals(PlcBaseJpaDAO.class)){
			total = (Long)method.invoke(dao, context, entidadeArg);
			return total;				
		} else {
			Object[] args = verifySearchArguments(context, entidadeArg, method, null, -1, -1);
			total = (Long)method.invoke(dao, args);
			return total;
		}
	}  

}
