/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.persistence.dao;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.apache.log4j.Logger;


import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcDataAccessObject;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.persistence.util.jpa.PlcJpaUtil;


/**
 * @author igor.guimaraes
 *
 */
@SPlcDataAccessObject
public class TaskListDAO extends AppJpaDAO {

	
	/**
	 * objeto para saida centralizada de LOG.
	 */
	@Inject
	protected transient Logger log;

	/**
	 * objeto para saida de LOG de profile.
	 */
	protected static Logger logProfiling = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_QA_PROFILING);
	
	@Inject @QPlcDefault 
	private PlcMetamodelUtil metamodelUtil;
	
	@Inject @QPlcDefault
	private PlcJpaUtil jpaUtil;	
	
	@Override
	public Object findById( PlcBaseContextVO context, Class classe, Object id) {

		try {

			applyFilters(context, classe);

			Class classeAux = jpaUtil.convertDynamicProxyToRealClass(classe);

			String queryEdita = context.getApiQuerySel();

			Object vo = null;
			if (metamodelUtil.isEntityClass(classe) && metamodelUtil.getEntity(classe).isIdNatural())
				vo = apiCreateQuery(context, classe,queryEdita).setParameter("id",id).getSingleResult();
			else
				vo = apiCreateQuery(context, classe,queryEdita).setParameter(1,id).getSingleResult();

			if (vo==null)
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_GENERIC,new Object[]{"recupera", "Erro ao recuperar objeto:"+id.toString()}, true);

			// Garante que colecoes participantes da lÃ³gica principal, mesmo se estiverem Lazy, sÃ£o carregadas
			// antes do 'detached'
			vo = findCompleteGraph(context, vo);

			return vo;

		} catch (Exception e) {
			if (NoResultException.class.isAssignableFrom(e.getClass()))
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_GENERIC,new Object[]{id.toString()}, true);
			else
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_GENERIC, new Object[] {"recupera", e }, e,log);
		}

	}
}
