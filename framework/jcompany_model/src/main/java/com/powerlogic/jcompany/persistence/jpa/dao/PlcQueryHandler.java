/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */
package com.powerlogic.jcompany.persistence.jpa.dao;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.Query;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationDAOIoC;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;
import com.powerlogic.jcompany.persistence.jpa.PlcBaseJpaDAO;
import com.powerlogic.jcompany.persistence.jpa.PlcQuery;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryFirstLine;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryLineAmount;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryOrderBy;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryParameter;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryService;
import com.powerlogic.jcompany.persistence.util.PlcAnnotationPersistenceUtil;
import com.powerlogic.jcompany.persistence.util.PlcQBEUtil;
import com.powerlogic.jcompany.persistence.util.jpa.PlcJpaUtil;

@PlcQueryService
@Interceptor
public class PlcQueryHandler {

	@Inject
	protected transient Logger log;

	@Inject
	@QPlcDefault
	PlcReflectionUtil reflectionUtil;

	@Inject
	@QPlcDefault
	PlcJpaUtil jpaUtil;

	@Inject
	@QPlcDefault
	PlcQBEUtil qbeUtil;

	@Inject
	@QPlcDefault
	PlcAnnotationPersistenceUtil anotacaoPersistenceUtil;

	@AroundInvoke
	Object handle(InvocationContext ctx) throws Exception {

		Method queryMethod = ctx.getMethod();

		// public native
		if (Modifier.isPublic(queryMethod.getModifiers())
				&& Modifier.isNative(queryMethod.getModifiers())) {

			PlcBaseJpaDAO dao = ((PlcBaseJpaDAO) ctx.getTarget());

			PlcBaseContextVO context = (PlcBaseContextVO)ctx.getParameters()[0];
			
			Class<?> tipoEntidade = descobreTipoEntidade(dao);

			String query = getQueryString(context, 
					dao,
					tipoEntidade,
					queryMethod.getAnnotation(PlcQuery.class) != null ? queryMethod
							.getAnnotation(PlcQuery.class).value() : null,
					queryMethod.getName());

			if (StringUtils.isNotEmpty(query)) {

				EntityManager em = getEntityManager(dao, context, tipoEntidade);

				Object[] valores = ctx.getParameters();
				String[] nomes = new String[valores.length];
				Annotation annotations[][] = queryMethod
						.getParameterAnnotations();
				String where = resolveWhereClause(valores, annotations, nomes);
				if (StringUtils.isNotBlank(where))
					query = qbeUtil.completeSelectWhere(query, where);
				
				query = dao.applyFilters(context, tipoEntidade, query);

				// resolve order by quando houver informado no paramentro ou na
				// clausula
				String orderBy = resolveOrderBy(valores, annotations, nomes);
				query = qbeUtil.changeOrderBy(query, orderBy);

				Query q = em.createQuery(query);				

				if (tipoEntidade != null) {
					jpaUtil.applyTransformer(tipoEntidade, query, q);
				}

				for (int i = 0; i < nomes.length; i++) {
					if (nomes[i] != null) {
						q.setParameter(nomes[i], valores[i]);
					}
				}

				int primeiraLinha = resolveParametroInteiroComAnotacao(valores,
						annotations, PlcQueryFirstLine.class);
				int maximoLinhas = resolveParametroInteiroComAnotacao(valores,
						annotations, PlcQueryLineAmount.class);

				if (primeiraLinha >= 0) {
					q.setFirstResult(primeiraLinha);
				}
				if (maximoLinhas >= 1) {
					q.setMaxResults(maximoLinhas);
				}

				if (Collection.class.isAssignableFrom(queryMethod
						.getReturnType())) {
					List<?> result = q.getResultList();
					return queryMethod.getReturnType().cast(result);
				} else {
					Object result = q.getSingleResult();
					return queryMethod.getReturnType().cast(result);
				}
			}
		} else {
			return ctx.proceed();
		}

		return null;
	}

	private EntityManager getEntityManager(PlcBaseJpaDAO dao, PlcBaseContextVO context, Class entityClass)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {

		Method entityManagerGetter = reflectionUtil.findMethodHierarchically(
				dao.getClass(), "getEntityManager", PlcBaseContextVO.class, Class.class);
		setAccessibleMethod(entityManagerGetter, !entityManagerGetter.isAccessible());
		EntityManager em = (EntityManager) entityManagerGetter.invoke(dao, context, entityClass);
		setNotAccessibleMethod(entityManagerGetter, !entityManagerGetter.isAccessible());

		return em;
	}

	private void setAccessibleMethod(Method entityManagerGetter,
			boolean isNotAccessible) {
		if (isNotAccessible) {
			entityManagerGetter.setAccessible(true);
		}
	}

	private void setNotAccessibleMethod(Method entityManagerGetter,
			boolean isNotAccessible) {
		if (isNotAccessible) {
			entityManagerGetter.setAccessible(false);
		}
	}

	protected Class<?> descobreTipoEntidade(PlcBaseJpaDAO dao) {
		Class<?> entidade = null;
		PlcAggregationDAOIoC agregacaoIoC = dao.getClass().getAnnotation(
				PlcAggregationDAOIoC.class);
		if (agregacaoIoC == null || agregacaoIoC.value() == null) {
			// Procura no Superclass
			agregacaoIoC = dao.getClass().getSuperclass()
					.getAnnotation(PlcAggregationDAOIoC.class);
		}
		if (agregacaoIoC != null) {
			entidade = agregacaoIoC.value();
		}
		return entidade;
	}

	protected String getQueryString(PlcBaseContextVO context, PlcBaseJpaDAO dao, Class<?> entidade,
			String query, String metodo) {
		
		String q = context.getApiQuerySel();
		if (q == null)
			q = query;
		if (q == null)
			q = "querySel";
		if (entidade != null) {

			NamedQuery nq = anotacaoPersistenceUtil.getNamedQueryByName(
					entidade, q);
			if (nq != null) {
				q = nq.query();
				if (metodo.equals("findCount")) {
					q = "select count(*) "
							+ q.substring(q.indexOf("from"), q.length());
					q = dao.removeGroupbyOrderby(q);
					return q;
				}
			}
			// nao encontrou a query na entidade, verificar se a query passada e
			// realmente uma query. Deve conter 'from'.
			if (!q.contains("from ")) {
				throw new PlcException(
						PlcBeanMessages.JCOMPANY_ERROR_NAMED_QUERY_NOT_FOUND,
						new String[] { entidade.getCanonicalName(), q }, false);
			}
		}

		return q;
	}

	protected int resolveParametroInteiroComAnotacao(Object[] valores,
			Annotation[][] annotations, Class<? extends Annotation> anotacao) {

		int index = 0;
		for (Object valor : valores) {
			for (Annotation annotation : annotations[index]) {
				if (valor != null
						&& annotation.annotationType().isAssignableFrom(
								anotacao)) {
					return new Integer(valor.toString()).intValue();
				}
			}
			index++;
		}
		return -1;
	}

	/**
	 * Retorna o order by de acordo com os valores informados para pesquisa
	 * 
	 * @param valores
	 *            - Array de objetos com valores informados
	 * @param annotations
	 *            - Anotações de cada parâmetro do método de pesqusia.
	 * @param nomes
	 *            - Array de Strings pelo qual serão retornados os nomes do
	 *            parâmetros preenchidos na query
	 * @return orderBy
	 */
	private String resolveOrderBy(Object[] valores, Annotation[][] annotations,
			String[] nomes) {

		String orderby = "";
		int index = 0;

		for (Object valor : valores) {
			PlcQueryOrderBy queryOrderBy = null;
			for (Annotation annotation : annotations[index]) {
				if (annotation.annotationType().isAssignableFrom(
						PlcQueryOrderBy.class)) {
					queryOrderBy = ((PlcQueryOrderBy) annotation);
				}
			}

			if (valor != null && queryOrderBy != null) {
				orderby += valor;
			}

			ArrayUtils.remove(valores, index);
			index++;
		}

		return orderby;
	}

	/**
	 * Retorna a clausura where de acordo com os valores informados para
	 * pesquisa
	 * 
	 * @param valores
	 *            - Array de objetos com valores informados
	 * @param annotations
	 *            - Anotações de cada parâmetro do método de pesqusia.
	 * @param nomes
	 *            - Array de Strings pelo qual serão retornados os nomes do
	 *            parâmetros preenchidos na query
	 * @return
	 */
	protected String resolveWhereClause(Object[] valores,
			Annotation[][] annotations, String[] nomes) {

		String where = "";
		int index = 0;

		for (Object valor : valores) {
			PlcQueryParameter queryParameter = null;
			for (Annotation annotation : annotations[index]) {
				if (annotation.annotationType().isAssignableFrom(
						PlcQueryParameter.class)) {
					queryParameter = ((PlcQueryParameter) annotation);
				}
			}

			if (valor != null && queryParameter != null) {
				nomes[index] = queryParameter.name().replace(".", "_");
				if (queryParameter.expression() == null
						|| queryParameter.expression().equals("")) {
					where += " " + queryParameter.name() + " = :"
							+ queryParameter.name().replace(".", "_");
				} else {
					where += queryParameter.expression();
				}
				where += " and ";
			}
			ArrayUtils.remove(valores, index);
			index++;
		}
		if (where != null && where.endsWith("and ")) {
			where = where.substring(0, where.lastIndexOf("and "));
		}

		return where;
	}

}