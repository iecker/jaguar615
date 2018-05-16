/*  																													
486	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.

		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */

package com.powerlogic.jcompany.commons.util.metamodel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationDAOIoC;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationIoC;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.metamodel.PlcApplication;
import com.powerlogic.jcompany.commons.config.metamodel.PlcBusinessObject;
import com.powerlogic.jcompany.commons.config.metamodel.PlcDataAccessObject;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntity;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.PlcConfigUtil;
import com.powerlogic.jcompany.commons.util.PlcNomenclatureUtil;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;

@SPlcUtil
@QPlcDefault
public class PlcMetamodelUtil {

	private Logger log = Logger.getLogger(PlcMetamodelUtil.class.getSimpleName());

	private PlcApplication application;

	private Map<Class<?>, PlcEntity<?>> entidadesPorClasse = new ConcurrentHashMap<Class<?>, PlcEntity<?>>();

	private Map<String, Bean<?>> uriMBPorClasse = new ConcurrentHashMap<String, Bean<?>>();

	private Map<String, Bean<?>> uriFacadePorClasse = new ConcurrentHashMap<String, Bean<?>>();

	private Set<PlcEntity<?>> entidades = new HashSet<PlcEntity<?>>();

	private Map<Class<?>, PlcBusinessObject<?>> businessObjects = new ConcurrentHashMap<Class<?>, PlcBusinessObject<?>>();

	private Map<Class<?>, PlcDataAccessObject<?>> dataAccessObjects = new ConcurrentHashMap<Class<?>, PlcDataAccessObject<?>>();

	/**
	 * Serviço injetado e gerenciado pelo CDI
	 */
	@Inject
	private BeanManager beanManager;

	@Inject
	@QPlcDefault
	protected PlcReflectionUtil reflectionUtil;

	@Inject
	@QPlcDefault
	protected PlcNomenclatureUtil nomenclaturaUtil;

	@Inject
	public void setApplication(PlcApplication aplicacao) {
		this.application = aplicacao;
	}

	public PlcApplication getApplication() {
		return application;
	}

	public <T> PlcEntity<T> createEntity(Class<T> entityClass) {
		PlcEntity<T> entity = new PlcEntity<T>();

		entity.setEntityClass(entityClass);

		AnnotatedType<T> annotatedType = beanManager.createAnnotatedType(entityClass);

		Method[] methods = reflectionUtil.findAllMethodsHierarchically(annotatedType.getJavaClass());
		for (Method method : methods) {

			// acha o atfributo ID da classe
			if (entity.getIdAtributo() == null && (method.getAnnotation(javax.persistence.Id.class) != null || method.getAnnotation(javax.persistence.EmbeddedId.class) != null)) {
				try {
					entity.setIdAtributo(reflectionUtil.findFieldHierarchically(entityClass, StringUtils.uncapitalize(method.getName().replace("get", ""))));
				} catch (Exception e) {
				}
			}
			if (entity.getVersaoAtributo() == null && method.getAnnotation(javax.persistence.Version.class) != null) {
				try {
					entity.setVersaoAtributo(reflectionUtil.findFieldHierarchically(entityClass, StringUtils.uncapitalize(method.getName().replace("get", ""))));
				} catch (Exception e) {
				}
			}
		}

		Set<String> nomesAtributos = new HashSet<String>();

		Field[] fields = reflectionUtil.findAllFieldsHierarchically(annotatedType.getJavaClass());
		for (Field field : fields) {
			String nomeAtributo = field.getName();

			nomesAtributos.add(nomeAtributo);

			// acha o atributo ID da classe
			if (entity.getIdAtributo() == null && (field.getAnnotation(javax.persistence.Id.class) != null || field.getAnnotation(javax.persistence.EmbeddedId.class) != null)) {
				entity.setIdAtributo(field);
			}
			if (entity.getVersaoAtributo() == null && field.getAnnotation(javax.persistence.Version.class) != null) {
				entity.setVersaoAtributo(field);
			}
			if (entity.getRowIdAtributo() == null && nomeAtributo.equals("rowId")) {
				entity.setRowIdAtributo(field);
			}
			if (entity.getDataUltimaAlteracaoAtributo() == null && nomeAtributo.equals("dataUltAlteracao")) {
				entity.setDataUltimaAlteracaoAtributo(field);
			}
			if (entity.getUsuarioUltimaAlteracaoAtributo() == null && nomeAtributo.equals("usuarioUltAlteracao")) {
				entity.setUsuarioUltimaAlteracaoAtributo(field);
			}
			if (entity.getIndExcPlcAtributo() == null && nomeAtributo.equals("indExcPlc")) {
				entity.setIndExcPlcAtributo(field);
			}

		}

		entity.setNomesAtributos(nomesAtributos);

		return entity;
	}

	public Collection<PlcEntity<?>> getEntities() {
		return entidades;
	}

	/**
	 * Obtem o metamodelo de uma entidade gerenciada pelo jCOmpany para a classe
	 * específicada.
	 */
	@SuppressWarnings("unchecked")
	public <T> PlcEntity<T> getEntity(Class<T> entityClass) {
		return (PlcEntity<T>) entidadesPorClasse.get(entityClass);
	}

	@SuppressWarnings("unchecked")
	public <T> void addEntity(PlcEntity<T> entity) {
		PlcEntity<T> targetEntity = null;

		Class<T> entityClass = entity.getEntityClass();
		if (entidadesPorClasse.containsKey(entityClass)) {
			targetEntity = (PlcEntity<T>) entidadesPorClasse.get(entityClass);
		} else {
			targetEntity = entity;
		}

		entidades.add(targetEntity);
		entidadesPorClasse.put(entityClass, targetEntity);
		Class entitySuperClass = entityClass.getSuperclass();

		if (!entitySuperClass.equals(Object.class)) {
			if (entidadesPorClasse.get(entitySuperClass) != null) {
				//Removendo a classe ancestral somente se a mesma não for entidade
				if (Modifier.isAbstract(entitySuperClass.getModifiers())
						|| entitySuperClass.getAnnotation(MappedSuperclass.class) != null) {
					entidadesPorClasse.remove(entitySuperClass);
				}	
			} else {
				//Adicionando a classe ancestral somente se a mesma não for entidade
				if (Modifier.isAbstract(entitySuperClass.getModifiers())
						|| entitySuperClass.getAnnotation(MappedSuperclass.class) != null) {
					entidadesPorClasse.put(entitySuperClass, targetEntity);
				}	
			}
		}

		if (targetEntity.getBusinessObject() == null) {
			PlcBusinessObject<T> businessObject = getBusinessObjectParaEntityClass(entityClass);
			if (businessObject == null) {
				Class<?> businessObjectClass;
				businessObjectClass = nomenclaturaUtil.resolveBOClass(entityClass);
				if (businessObjectClass == null)
					businessObjectClass = getApplication().getBusinessObjectClassDefault();
				if (businessObjectClass != null) {
					PlcBusinessObject businessObjectDefault = getBusinessObject(businessObjectClass);
					if (businessObjectDefault == null) {
						businessObjectDefault = createBusinessObject(businessObjectClass);
						addBusinessObject(businessObjectDefault);
					}
					businessObject = (PlcBusinessObject<T>) businessObjectDefault;
				}

			}
			businessObject.addEntity(targetEntity);
			targetEntity.setBusinessObject(businessObject);
		}
		if (targetEntity.getDataAccessObject() == null) {
			PlcDataAccessObject<T> dataAccessObject = getDataAccessObjectParaEntityClass(entityClass);
			if (dataAccessObject == null) {
				Class<?> dataAcccessObjectClassDefault;
				dataAcccessObjectClassDefault = nomenclaturaUtil.resolveDAOClass(entityClass);
				if (dataAcccessObjectClassDefault == null)
					dataAcccessObjectClassDefault = getApplication().getDataAcccessObjectClassDefault();
				if (dataAcccessObjectClassDefault != null) {
					PlcDataAccessObject dataAccessObjectDefault = getDataAccessObject(dataAcccessObjectClassDefault);
					if (dataAccessObjectDefault == null) {
						dataAccessObjectDefault = createDataAccessObject(dataAcccessObjectClassDefault);
						addDataAccessObject(dataAccessObjectDefault);
					}
					dataAccessObject = (PlcDataAccessObject<T>) dataAccessObjectDefault;
				}
			}
			dataAccessObject.addEntity(targetEntity);
			targetEntity.setDataAccessObject(dataAccessObject);
		}
	}

	// -- Metodos para PlcBusinessObject

	@SuppressWarnings("unchecked")
	public <T> void addBusinessObject(PlcBusinessObject<T> businessObject) {
		PlcBusinessObject<T> targetBusinessObject = null;

		Class<?> businessObjectClass = businessObject.getBusinessObjectClass();
		if (businessObjects.containsKey(businessObjectClass)) {
			targetBusinessObject = (PlcBusinessObject<T>) businessObjects.get(businessObjectClass);
		} else {
			targetBusinessObject = businessObject;
		}

		if (targetBusinessObject.getBusinessObjectClass().isAnnotationPresent(PlcAggregationIoC.class)) {
			Class clazz = businessObject.getBusinessObjectClass().getAnnotation(PlcAggregationIoC.class).clazz();
			if (clazz != null) {
				PlcEntity<T> entity = (PlcEntity<T>) entidadesPorClasse.get(clazz);
				if (entity == null) {
					entity = (PlcEntity<T>) createEntity(clazz);
					addEntity(entity);
				}
				if (entity.getBusinessObject() != null && !entity.getBusinessObject().getBusinessObjectClass().equals(targetBusinessObject.getBusinessObjectClass())) {
					if (entity.isBusinessObjectAnnotated())
						throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_ENTITY_ANNOTATED_REPOSITORY_OR_DAO, new Object[]{clazz}, true);
					else
						entity.getBusinessObject().getEntidades().remove(entity);
				}

				entity.setBusinessObject(targetBusinessObject);
				entity.setBusinessObjectAnnotated(true);
				targetBusinessObject.addEntity(entity);
			}

		}

		businessObjects.put(businessObjectClass, targetBusinessObject);
	}

	@SuppressWarnings("unchecked")
	public <T> PlcBusinessObject<T> createBusinessObject(Class<T> businessObjectClass) {
		PlcBusinessObject<T> businessObject = new PlcBusinessObject<T>();
		businessObject.setBusinessObjectClass(businessObjectClass);

		Class<T> targetEntityClass = getTargetEntityClass(businessObjectClass);
		if (targetEntityClass == null) {
			targetEntityClass = (Class<T>) Object.class;
		}
		businessObject.setTargetEntityClass(targetEntityClass);

		return businessObject;
	}

	public Collection<PlcBusinessObject<?>> getBusinessObjects() {
		return businessObjects.values();
	}

	@SuppressWarnings("unchecked")
	public <T> PlcBusinessObject<T> getBusinessObject(Class<T> businessObjectClass) {
		return (PlcBusinessObject<T>) businessObjects.get(businessObjectClass);
	}

	@SuppressWarnings("unchecked")
	public <T> PlcBusinessObject<T> getBusinessObjectParaEntityClass(Class<T> entityClass) {
		PlcBusinessObject<T> businessObjectObtido = null;
		for (PlcBusinessObject<?> businessObject : businessObjects.values()) {
			if (businessObject.getTargetEntityClass().equals(entityClass)) {
				businessObjectObtido = (PlcBusinessObject<T>) businessObject;
				break;
			}
		}
		return businessObjectObtido;
	}

	// -- Metodos para PlcDataAccessObject

	@SuppressWarnings("unchecked")
	public <T> void addDataAccessObject(PlcDataAccessObject<T> dataAccessObject) {
		PlcDataAccessObject<T> targetDataAccessObject = null;

		Class<?> dataAcesssObjectClass = dataAccessObject.getDataAcessObjectClass();
		if (dataAccessObjects.containsKey(dataAcesssObjectClass)) {
			targetDataAccessObject = (PlcDataAccessObject<T>) dataAccessObjects.get(dataAcesssObjectClass);
		} else {
			targetDataAccessObject = dataAccessObject;
		}

		if (targetDataAccessObject.getDataAcessObjectClass().isAnnotationPresent(PlcAggregationDAOIoC.class)) {
			Class clazz = targetDataAccessObject.getDataAcessObjectClass().getAnnotation(PlcAggregationDAOIoC.class).value();
			if (clazz != null) {
				PlcEntity<T> entity = (PlcEntity<T>) entidadesPorClasse.get(clazz);
				if (entity == null) {
					entity = (PlcEntity<T>) createEntity(clazz);
					addEntity(entity);
				}
				if (entity.getDataAccessObject() != null && !entity.getDataAccessObject().getDataAcessObjectClass().equals(targetDataAccessObject.getDataAcessObjectClass())) {
					if (entity.isDataAccessObjectAnnotated())
						throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_ENTITY_ANNOTATED_REPOSITORY_OR_DAO, new Object[]{clazz}, true);
					else
						entity.getDataAccessObject().getEntidades().remove(entity);
				}

				entity.setDataAccessObject(targetDataAccessObject);
				entity.setDataAccessObjectAnnotated(true);
				targetDataAccessObject.addEntity(entity);
			}
		}

		dataAccessObjects.put(dataAcesssObjectClass, targetDataAccessObject);
	}

	@SuppressWarnings("unchecked")
	public <T> PlcDataAccessObject<T> createDataAccessObject(Class<T> dataAcccessObjectClass) {
		PlcDataAccessObject<T> dataAccessObject = new PlcDataAccessObject<T>();
		dataAccessObject.setDataAcessObjectClass(dataAcccessObjectClass);

		Class<T> targetEntityClass = getTargetEntityClass(dataAcccessObjectClass);
		if (targetEntityClass == null) {
			targetEntityClass = (Class<T>) Object.class;
		}
		dataAccessObject.setTargetEntityClass(targetEntityClass);

		return dataAccessObject;
	}

	public Collection<PlcDataAccessObject<?>> getDataAccessObjects() {
		return dataAccessObjects.values();
	}

	@SuppressWarnings("unchecked")
	public <T> PlcDataAccessObject<T> getDataAccessObject(Class<T> dataAccessObjectClass) {
		return (PlcDataAccessObject<T>) dataAccessObjects.get(dataAccessObjectClass);
	}

	@SuppressWarnings("unchecked")
	public <T> PlcDataAccessObject<T> getDataAccessObjectParaEntityClass(Class<T> entityClass) {
		PlcDataAccessObject<T> dataAccessObjectObtido = null;
		for (PlcDataAccessObject<?> dataAccessObject : dataAccessObjects.values()) {
			if (dataAccessObject.getTargetEntityClass().equals(entityClass)) {
				dataAccessObjectObtido = (PlcDataAccessObject<T>) dataAccessObject;
				break;
			}
		}
		return dataAccessObjectObtido;
	}

	// -- Utilitarios
	@SuppressWarnings("unchecked")
	public <T> Class<T> getTargetEntityClass(Class<?> classe) {
		Type genericSuperClass = classe.getGenericSuperclass();
		if (genericSuperClass instanceof ParameterizedType) {
			ParameterizedType parametrizedType = (ParameterizedType) genericSuperClass;
			Type[] genericTypes = parametrizedType.getActualTypeArguments();
			if (genericTypes != null && genericTypes.length >= 1 && genericTypes[0] instanceof Class) {
				return (Class<T>) genericTypes[0];
			}
		}
		return null;
	}

	public <T> PlcEntityInstance<T> createEntityInstance(Object instance) {
		PlcEntityInstance<T> plcEntityInstance = null;

		if (instance != null) {

			Class<?> entityType = reflectionUtil.getObjectType(instance, false);

			PlcEntity<?> plcEntity = getEntity(entityType);

			if (plcEntity != null) {
				plcEntityInstance = new PlcEntityInstance(plcEntity, instance);
			}
		}

		return plcEntityInstance;
	}

	/**
	 * Verifica se a classe especificada pertence a uma entidade gerenciada pelo
	 * jCompany.
	 * 
	 * @param entityClass
	 *            Classe a ser verificada.
	 */
	public boolean isEntityClass(Class<?> entityClass) {
		if (getEntity(entityClass) != null) {
			return true;
		}
		return false;
	}

	public void addUriIocMB(Bean<?> bean) {

		PlcUriIoC annotation = bean.getBeanClass().getAnnotation(PlcUriIoC.class);

		if (annotation != null) {
			String value = annotation.value();
			uriMBPorClasse.put(value, bean);
			if (!(value.endsWith("*") || endsWithSuffix(value))) {
				for (String suffix : PlcConfigUtil.SUFIXOS_URL) {
					uriMBPorClasse.put(value + suffix, bean);
				}
			}
		} else {
			try {
				String casoDeUso = nomenclaturaUtil.resolveUseCase(bean.getBeanClass());
				if (casoDeUso != null){
					for (String suffix : PlcConfigUtil.SUFIXOS_URL) {
						uriMBPorClasse.put(casoDeUso + suffix, bean);
					}
				}
			} catch (PlcException plcE) {
				throw plcE; 
			}
		}

	}

	protected boolean endsWithSuffix(String value) {

		for (String suffix : PlcConfigUtil.SUFIXOS_URL) {
			if (value.endsWith(suffix)) {
				return true;
			}
		}

		return false;
	}

	public Bean getUriIocMB(String colaboracao) {

		if (uriMBPorClasse.containsKey(colaboracao)){
			return uriMBPorClasse.get(colaboracao);
		}
		else {
			for (String value : uriMBPorClasse.keySet()) {
				if (value.endsWith("*")) {
					for (String suffix : PlcConfigUtil.SUFIXOS_URL) {
						if(value.startsWith(colaboracao) ||
								(value.startsWith(colaboracao.subSequence(0, colaboracao.length() -3).toString()) && colaboracao.endsWith(suffix))) {
							Bean bean = uriMBPorClasse.get(value);
							uriMBPorClasse.put(colaboracao, bean);
							return bean;
						}  
					}
				}
			}
		}
		//uriMBPorClasse.put(colaboracao, null);
		return null;

	}

	public void addUriIocFacade(Bean<?> bean) {

		PlcUriIoC annotation = bean.getBeanClass().getAnnotation(PlcUriIoC.class);

		if (annotation != null) {
			String value = annotation.value();

			if (value.endsWith("*"))
				uriFacadePorClasse.put(value, bean);
			else if (endsWithSuffix(value)) {
				uriFacadePorClasse.put(value, bean);
			} else {
				uriFacadePorClasse.put(value, bean);
				for (String suffix : PlcConfigUtil.SUFIXOS_URL) {
					uriFacadePorClasse.put(value + suffix, bean);
				}
			}

		} else {
			try {
				String casoDeUso = nomenclaturaUtil.resolveUseCase(bean.getBeanClass());
				if (casoDeUso != null){
					for (String suffix : PlcConfigUtil.SUFIXOS_URL) {
						uriFacadePorClasse.put(casoDeUso + suffix, bean);
					}
				}
			} catch (PlcException plcE) {
				throw plcE; 
			}
		}

	}

	public Bean<?> getUriIocFacade(String colaboracao) {
		if (uriFacadePorClasse.containsKey(colaboracao)) {
			return uriFacadePorClasse.get(colaboracao);
		} else {
			for (String value : uriFacadePorClasse.keySet()) {
				// colaboracao (xxxman) startsWith "xxx*"
				if (value.endsWith("*") && colaboracao.startsWith(StringUtils.removeEnd(value, "*"))) {
					Bean<?> bean = uriFacadePorClasse.get(value);
					uriFacadePorClasse.put(colaboracao, bean);
					return bean;
				}
			}
		}
		//uriFacadePorClasse.put(colaboracao, null);
		return null;
	}

	public List<String> getAllUriMB() {
		Set<String> allUriMB = new HashSet<String>();
		for (Bean<?> bean : uriMBPorClasse.values()) {
			if (bean!=null && bean.getClass()!=null) {
				PlcUriIoC uriIoC = bean.getBeanClass().getAnnotation(PlcUriIoC.class);
				if (uriIoC != null) {
					String uri = uriIoC.value();
					allUriMB.add(uri);
				}
			} 
		}
		return new ArrayList<String>(allUriMB);
	}
}
