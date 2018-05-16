/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.persistence.util.jpa;

import javax.persistence.Query;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.apache.commons.lang.StringUtils;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.transform.ResultTransformer;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.persistence.jpa.PlcBeanResultTransformer;
import com.powerlogic.jcompany.persistence.util.PlcPersistenceUtil;

/**
 * @since jCompnay 5.0 Utilitário geral para JPA
 */

@SPlcUtil
@QPlcDefault
public class PlcJpaUtil extends PlcPersistenceUtil {

	public PlcJpaUtil() {

	}

	/**
	 * XXX: Não implementado
	 * 
	 * @param pc
	 * @param name
	 * @return
	 */
	public boolean hasManyToOneReference(PersistentClass pc, String name) {
		return false;
	}

	/**
	 * Verifica se a classe informada está utilizando o campo reservado
	 * "sitHistoricoPlc", o que indica que possui registros "I"-Inativos,
	 * "A"-Ativos e, potencialmente (se utilizar aprovação ou publicação),
	 * "P"-Pendente
	 * 
	 * @param classe
	 *            Classe a ser investigada
	 * @return true se 'classe' contiver a propriedade 'sitHistoricoPlc' e ele
	 *         estiver mapeado.
	 */
	public boolean hasSitHistoricoPlc(Metamodel cfg, Class<?> classe) {

		EntityType<?> et = cfg.entity(classe);

		try {
			if (et.getAttribute(PlcConstants.VO.SIT_HISTORICO_PLC) == null)
				return false;
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	/**
	 * Aplica um transformer a uma query JPA
	 * 
	 * @param jpaQuery
	 * @param transformer
	 */
	public void applyTransformer(Query jpaQuery, ResultTransformer transformer) {
		if (jpaQuery instanceof QueryImpl<?>) {
			QueryImpl<?> queryImpl = (QueryImpl<?>) jpaQuery;
			org.hibernate.Query hibernateQuery = queryImpl.getHibernateQuery();
			hibernateQuery.setResultTransformer(transformer);
		} else {
			throw new IllegalArgumentException(
					"JPA Query deve ser do Hibernate");
		}
	}

	/**
	 * Retorna se a query é escalar (projeção somente dos atributos) ou não
	 * (retorna objeto)
	 * 
	 * @param query
	 * @return
	 */
	public boolean isScalarQuery(String query) {
		String lcaseQuery = query.toLowerCase();
		String[] constraints = { "select count(", "select new ", "from ",
				"select obj " };
		return !StringUtils.startsWithAny(lcaseQuery, constraints);
	}
	
	/**
	 * Aplica o transformer a query informada
	 */
	public void applyTransformer(Class<?> entidade, String hql, Query jpaQuery) {
		ResultTransformer resultTransformer = getResultTransformerForQuery(entidade, hql);
		if (resultTransformer != null) {
			applyTransformer(jpaQuery, resultTransformer);
		}
	}	
	
	/**
	 * Retorna se a query informada necessita de um {@link ResultTransformer}
	 * 
	 * @param entidade
	 * @param jpaQuery
	 * @return {@link ResultTransformer} se o hql necessitar. Caso contrário, retorna nulo
	 */
	protected ResultTransformer getResultTransformerForQuery(Class<?> entidade, String hql) {
		ResultTransformer resultTransformer;
		if (isScalarQuery(hql) && entidade!=null) {
			resultTransformer = PlcBeanResultTransformer.aliasToBean(entidade);
		} else {
			resultTransformer = null;
		}
		return resultTransformer;
	}
	
	

}
