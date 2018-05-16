/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.model.util;

import java.util.Iterator;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.PlcAnnotationUtil;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.model.PlcBaseRepository;
import com.powerlogic.jcompany.persistence.PlcBaseDAO;
import com.powerlogic.jcompany.persistence.PlcBaseManager;

/**
 * 
 * @author savio, baldini
 * 
 */
@SPlcUtil
@QPlcDefault
public class PlcIocModelUtil {

	@Inject
	protected transient Logger log;

	protected @Inject
	@QPlcDefault
	PlcMetamodelUtil metamodelUtil;

	@Inject
	@Any
	Instance<PlcBaseRepository> todosObjetosNegocio;

	@Inject
	@Any
	Instance<PlcBaseDAO> todosObjetosDAO;

	@Inject
	@Any
	Instance<PlcBaseManager> todosObjetosManager;

	@Inject
	@QPlcDefault
	PlcBaseRepository boPadrao;

	@Inject
	@QPlcDefault
	PlcBaseDAO daoPadrao;

	@Inject
	@QPlcDefault
	protected PlcAnnotationUtil annotationUtil;
	
	@Inject @QPlcDefault
	protected PlcReflectionUtil reflectionUtil;

	/**
	 * 
	 * @param classeManagerOuEntity
	 * @param classeDefault
	 * @return
	 * 
	 */
	public PlcBaseRepository getRepository(Class classeManagerOuEntity) {
		
		if (todosObjetosNegocio == null) {
			return null;
		}
		if (classeManagerOuEntity == null) {
			return boPadrao;
		}
		
		Class classeObjetoNegocio = resolveClassRepository(classeManagerOuEntity);

		if (metamodelUtil.getEntity(classeManagerOuEntity)!=null && metamodelUtil.getEntity(classeManagerOuEntity).getBusinessObject().getBusinessObjectInstance()!=null) {
			return (PlcBaseRepository)metamodelUtil.getEntity(classeManagerOuEntity).getBusinessObject().getBusinessObjectInstance();
		} else if (metamodelUtil.getBusinessObject(classeManagerOuEntity)!=null && metamodelUtil.getBusinessObject(classeManagerOuEntity).getBusinessObjectInstance()!=null) {
			return (PlcBaseRepository)metamodelUtil.getBusinessObject(classeManagerOuEntity).getBusinessObjectInstance();
		}
		
		PlcBaseRepository objetoNegocio = null;

		Instance<PlcBaseRepository> boInstance = todosObjetosNegocio.select(classeObjetoNegocio);

		if (!boInstance.isUnsatisfied()) {
			if (!boInstance.isAmbiguous()) {
				objetoNegocio = boInstance.get();
			} else {
				
				if (boInstance.iterator() != null) {
					for (Iterator iterator = boInstance.iterator(); iterator.hasNext();) {
						PlcBaseRepository objetoNegocioBusca = (PlcBaseRepository) iterator.next();
						if(reflectionUtil.getObjectType(objetoNegocioBusca, true).equals(classeObjetoNegocio)) {
							objetoNegocio = objetoNegocioBusca;
						}
					}
				}
			}
		} else {
			log.warn( "CDI: Instância de Objeto de Negocio para " + classeManagerOuEntity + " não encontrada!");
		}
		if (metamodelUtil.getEntity(classeManagerOuEntity)!=null) {
			metamodelUtil.getEntity(classeManagerOuEntity).getBusinessObject().setBusinessObjectInstance(objetoNegocio);
		} else if (metamodelUtil.getBusinessObject(classeManagerOuEntity)!=null && metamodelUtil.getBusinessObject(classeManagerOuEntity).getBusinessObjectInstance()!=null) {
			metamodelUtil.getBusinessObject(classeManagerOuEntity).setBusinessObjectInstance(objetoNegocio);
		}

		return objetoNegocio;
	}

	/**
	 * 
	 * @param classeDAOouManagerouEntity
	 * @param classeDefault
	 * @return
	 * 
	 */
	// TODO - Método getObjetoPersistencia não está preparado para receber classe dao ou manager apenas entidade 
	public PlcBaseDAO getPersistenceObject(Class classeDAOouManagerOuEntity)
			 {
		if (todosObjetosDAO == null) {
			return null;
		}
		
		if (classeDAOouManagerOuEntity == null){
			return daoPadrao;
		}
		
		if(metamodelUtil.getEntity(classeDAOouManagerOuEntity) != null && metamodelUtil.getEntity(classeDAOouManagerOuEntity).getDataAccessObject().getDataAcessObjectInstance()!=null) {
			return (PlcBaseDAO)metamodelUtil.getEntity(classeDAOouManagerOuEntity).getDataAccessObject().getDataAcessObjectInstance();
		}
		
		Class classeObjetoPersistencia = resolveClassPersistenceObject(classeDAOouManagerOuEntity);
		
		PlcBaseDAO objetoPersistencia = null;

		Instance<PlcBaseDAO> daoInstance = todosObjetosDAO.select(classeObjetoPersistencia);

		if (!daoInstance.isUnsatisfied()) {
			if (!daoInstance.isAmbiguous()) {
				objetoPersistencia = daoInstance.get();
			} else if (daoInstance.iterator() != null) {
				Iterator<PlcBaseDAO> i = daoInstance.iterator();
				while(i.hasNext() && objetoPersistencia==null) {
					objetoPersistencia = i.next();
					if (!objetoPersistencia.getClass().getSuperclass().equals(classeObjetoPersistencia))
						objetoPersistencia = null;
				}
			}
		} else {
			log.warn( "CDI: Instância de DAO para " + classeDAOouManagerOuEntity + " não encontrada!");
		}
		if (metamodelUtil.getEntity(classeDAOouManagerOuEntity) != null) {
			metamodelUtil.getEntity(classeDAOouManagerOuEntity).getDataAccessObject().setDataAcessObjectInstance(objetoPersistencia);
		}	
		
		return objetoPersistencia;
	}

	/**
	 * Retorna o gerenciador da fábrica
	 * 
	 * Este método é um facilitador para o
	 * {@link PlcIocModelUtil#getFactoryManager(String)}
	 * a fim de evitar type casts
	 * 
	 * @param <T>
	 * @param managerClass
	 * @return
	 * 
	 */
	@SuppressWarnings("unchecked")
	public <T extends PlcBaseManager> T getFactoryManager(
			Class<? extends PlcBaseManager> managerClass)  {
		return (T) getFactoryManager(annotationUtil.getDbFactoryName(managerClass));
	}

	/**
	 * Retorna a classe que gerencia a conexão com a fábrica passada por
	 * parâmetro. Busca uma classes JpaManager que possui uma anotação
	 * PLcFabrica com o mesmo nome da fábrica passada por parâmetro.
	 * 
	 * @param fabrica
	 * @return
	 * 
	 */
	public PlcBaseManager getFactoryManager(String fabrica)
			 {
		if (todosObjetosManager == null) {
			return null;
		}

		PlcBaseManager objetoManager = null;
		
		for (PlcBaseManager plcBaseManager : todosObjetosManager) {
			if (annotationUtil.getDbFactoryName(plcBaseManager.getClass()).equals(fabrica)) {
				objetoManager = plcBaseManager;
				break;
			}
		}

		if (objetoManager == null) {
			log.warn( "CDI: Instância de JpaManager para fábrica '" + fabrica+ "' não encontrada!");
		}

		return objetoManager;
	}

	/**
	 * 
	 * @param classeDAOouManagerouEntity
	 * @param classeDefault
	 * @return
	 * 
	 */
	protected Class<PlcBaseDAO> resolveClassPersistenceObject(Class classeDAOouManagerOuEntity)  {
		
		Class<PlcBaseDAO> classeObjetoPersistencia = null;

		if (metamodelUtil.isEntityClass(classeDAOouManagerOuEntity)) {
			classeObjetoPersistencia = metamodelUtil.getEntity(classeDAOouManagerOuEntity).getDataAccessObject().getDataAcessObjectClass(); 
		} else if (PlcBaseDAO.class.isAssignableFrom(classeDAOouManagerOuEntity)) {
			classeObjetoPersistencia = classeDAOouManagerOuEntity;
		} else {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_DEPENDENCY_INJECTION_INVALID_DAO,	new Object[] { classeDAOouManagerOuEntity.getName() }, true);
		}

		return classeObjetoPersistencia;
	}

	/**
	 * 
	 * @param classeManagerOuEntity
	 * @param classeDefault
	 * @return
	 * 
	 */
	protected Class resolveClassRepository(Class classeManagerOuEntity)  {

		Class classeObjetoNegocio = null;

		if (metamodelUtil.isEntityClass(classeManagerOuEntity)) {
			classeObjetoNegocio = metamodelUtil.getEntity(classeManagerOuEntity).getBusinessObject().getBusinessObjectClass();
		} else if (PlcBaseRepository.class.isAssignableFrom(classeManagerOuEntity)) {
			classeObjetoNegocio = classeManagerOuEntity;
		} else {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_DEPENDENCY_INJECTION_INVALID_REPOSITORY,
					new Object[] { classeManagerOuEntity.getName() }, true);
		}

		return classeObjetoNegocio;
	}


}
