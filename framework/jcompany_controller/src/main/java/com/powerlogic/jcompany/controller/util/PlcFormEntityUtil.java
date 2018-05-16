/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.util;


import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.IPlcFile;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.PlcFileContent;
import com.powerlogic.jcompany.commons.annotation.PlcMaster;
import com.powerlogic.jcompany.commons.annotation.PlcPrimaryKey;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.file.PlcBaseMapFileContent;
import com.powerlogic.jcompany.commons.util.PlcAnnotationUtil;
import com.powerlogic.jcompany.commons.util.PlcBeanCloneUtil;
import com.powerlogic.jcompany.commons.util.PlcDetailUtil;
import com.powerlogic.jcompany.commons.util.PlcEntityCommonsUtil;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigDetail;
import com.powerlogic.jcompany.config.aggregation.PlcConfigSubDetail;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.domain.PlcFileAttach;
import com.powerlogic.jcompany.config.domain.PlcNotCloneable;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcCreateContextUtil;


/**
 * jCompany 3.0. Singleton. Utilitário para lógicas de transformação de
 * informaçoes de Form-Bean para VOs e vice-versa. Encapula cópias e regras
 * vinculadas à operação.
 * 
 * @author alvim, cláudia seara
 * @since jCompany 3.0
 */
@SPlcUtil
@QPlcDefault
public class PlcFormEntityUtil implements Serializable {

	private static final long serialVersionUID = 9060167964564477425L;

	@Inject
	protected transient Logger log;
	
	/**
	 * Serviço injetado e gerenciado pelo CDI
	 */
	@Inject @QPlcDefault 
	protected PlcMetamodelUtil metamodelUtil;
	
	@Inject @QPlcDefault 
	protected PlcEntityCommonsUtil entityCommonsUtil;
	
	@Inject @QPlcDefault 
	protected PlcBeanCloneUtil beanCloneUtil;
	
	@Inject @QPlcDefault 
	protected PlcReflectionUtil reflectionUtil;
	
	@Inject @QPlcDefault
	protected PlcAnnotationUtil annotationUtil;
	
	@Inject @QPlcDefault 
	protected PlcURLUtil urlUtil;
	
	@Inject @QPlcDefault 
	protected PlcIocControllerFacadeUtil iocControleFacadeUtil;
	
	@Inject @QPlcDefault 
	protected PlcCreateContextUtil contextMontaUtil;	

	@Inject @QPlcDefault 
	protected PlcContextUtil contextUtil;

	//cria instancia do propertyUtilsBean para evitar diversas chamadas a metodo syncronized
	private static PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
	
	/**
	 * Implementa a clonagem de detalhes.
	 * @param pattern
	 * @param vo
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void cloneDetails(FormPattern pattern, Object entity, PlcConfigDetail[] detalhes)   {

		try {

			for (PlcConfigDetail detalhe : detalhes) {
				if (detalhe != null && StringUtils.isNotBlank(detalhe.collectionName())) {
					Field f =  reflectionUtil.findFieldHierarchically(entity.getClass(),detalhe.collectionName());
					Object detalheEntityClone;
					if (Set.class.isAssignableFrom(f.getType())) { 
						Set<Object> setOriginal  =  (Set)propertyUtilsBean.getProperty(entity, f.getName());
						Set<Object> set			=  PlcDetailUtil.instanceTreeSet(detalhe.comparator());//PlcEntidadeUtil.getInstance().instanciaTreeSet();
						
						if (setOriginal != null){
							for (Object detalheEntity : setOriginal){
								detalheEntityClone = beanCloneUtil.cloneBean(detalheEntity, true);
								cloneDetail(detalheEntityClone,detalhe);
								removeDetailKeyFields(detalheEntityClone, detalhe);
								setMasterOnDetail(entity,detalhe,detalheEntityClone);
								set.add(detalheEntityClone);
							}
						}
						
						propertyUtilsBean.setProperty(entity, detalhe.collectionName(),set);
						
					} else { 

						List<Object> listaOriginal 	= (List<Object>)propertyUtilsBean.getProperty(entity, f.getName());
						List<Object> lista   		= new ArrayList<Object>();
						if ( listaOriginal != null ){
							for(Object detalheEntity : listaOriginal){
								detalheEntityClone =  beanCloneUtil.cloneBean(detalheEntity);
								cloneDetail(detalheEntityClone,detalhe);
								removeDetailKeyFields(detalheEntityClone, detalhe);
								setMasterOnDetail(entity,detalhe,detalheEntityClone);
								lista.add(detalheEntityClone);
							}
						}
						propertyUtilsBean.setProperty(entity, detalhe.collectionName(),lista);
					} 

				}
			}
		
		} catch(PlcException plcE){
			throw plcE;
		} catch (Exception e) {
			log.fatal( "Erro ao tentar limpar detalhes =" + e);
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_INSERT, new Object[] { e }, e, log);
		}

	}
	/**
	 * Retira campos que são chave natural. Também é feito em subdetalhes
	 * @param detalhe
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private void removeDetailKeyFields (Object detalhe, PlcConfigDetail configDetalhe) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException{
		// Retirar campos chaves
		PlcPrimaryKey chavePrimaria = detalhe.getClass().getAnnotation(PlcPrimaryKey.class);
		if (chavePrimaria != null){
			Object idNatural = propertyUtilsBean.getProperty(detalhe, "idNatural");
			if (idNatural==null) {
				idNatural = chavePrimaria.classe().newInstance();
				propertyUtilsBean.setProperty(detalhe, "idNatural", idNatural);
			}
			String[] propriedades = chavePrimaria.propriedades();
			for (String propChave : propriedades) {
				propertyUtilsBean.setProperty(idNatural, propChave, null);
			}
		}
		
		if (configDetalhe != null){
			PlcConfigSubDetail configSubDetalhe = configDetalhe.subDetail();
			if (metamodelUtil.isEntityClass(configSubDetalhe.clazz())){
				Collection <Object> colecaoSubDetalhe = (Collection <Object>)propertyUtilsBean.getProperty(detalhe, configSubDetalhe.collectionName());
				for (Object subDetalhe : colecaoSubDetalhe) {
					removeDetailKeyFields(subDetalhe, null);
				}
			}
		}
	}
	
	/**
	 * Registra o Mestre no detalhe
	 */
	private void setMasterOnDetail(Object entity, PlcConfigDetail detalhe, Object detalheEntityClone)throws Exception{
		 
		String classeMestre 		= entity.getClass().getName();
		PlcMaster mestre 			= (PlcMaster)detalhe.clazz().getAnnotation(PlcMaster.class);
		if (mestre != null)
			classeMestre =  reflectionUtil.findFieldHierarchically(detalhe.clazz(),mestre.value()).getType().getName();
		
		PlcEntityCommonsUtil entityCommonsUtil = (PlcEntityCommonsUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcEntityCommonsUtil.class , QPlcDefaultLiteral.INSTANCE);
		
		List propAgregadaParaTipo 	= entityCommonsUtil.getAggregatePropertyByType(detalheEntityClone,classeMestre	);
		if (propAgregadaParaTipo != null && !propAgregadaParaTipo.isEmpty()) {
			String nomeProp = (String) propAgregadaParaTipo.get(0);
			propertyUtilsBean.setProperty(detalheEntityClone, nomeProp, entity);
		}

	}
	
	/**
	 * O Clone de um detalhe, nada mais que anular os Ids do detalhe e dos SubDetalhes 
	 * @param entity,  Detalhe
	 */
	public void cloneDetail(Object entity,PlcConfigDetail detalhe){
		try {		
			PlcEntityInstance entityInstance = metamodelUtil.createEntityInstance(entity);
			
			entityInstance.setId(null);
			entityInstance.setIdAux(null);	

			//Clona os subdetalhes
			if (StringUtils.isNotBlank(detalhe.subDetail().collectionName())){
				Collection<Object> lSubDet		= (Collection)propertyUtilsBean.getProperty(entity, detalhe.subDetail().collectionName());
				Iterator<Object> iteratorSubDet 	= lSubDet.iterator();
				
				// Nome da propriedade que associa o subdetalhe ao seu detalhe.
				String nomePropPai = null;
				
				while (iteratorSubDet.hasNext()) {
					Object subDetalhe = iteratorSubDet.next();
					PlcEntityInstance subDetalheInstance = metamodelUtil.createEntityInstance(subDetalhe);
					subDetalheInstance.setId(null);
					subDetalheInstance.setIdAux(null);

					// Associa o subdetalhe ao detalhe. Necessário, pois o
					// detalhe foi realmente clonado, deixando os
					// subdetalhes apontando para o objeto original.
					// @author Roberto Badaro, 2008-07-15. 
					
					if (nomePropPai == null) {
						PlcEntityCommonsUtil entityCommonsUtil = (PlcEntityCommonsUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcEntityCommonsUtil.class , QPlcDefaultLiteral.INSTANCE);
						final List lPossiveisPais = entityCommonsUtil.getAggregatePropertyByType(subDetalhe, entity.getClass().getName());
				
						if (lPossiveisPais != null && !lPossiveisPais.isEmpty()) {
							nomePropPai = (String) lPossiveisPais.get(0);
						}
					}

					propertyUtilsBean.setProperty(subDetalhe, nomePropPai, entity);
					// Fim associação subdetalhe-detalhe.
				}
			}

		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			log.fatal( "Erro ao tentar limpar clonarDetalhe =" + e);
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_INSERT, new Object[] { e },
					e, log);
		}

	}
	
	/**
	 * Implementa a clonagem de arquivo anexado.
	 * @param entity
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void cloneFileAttach(Object entityPlc)   {

		String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
		Field [] camposAnotados = reflectionUtil.findAllFieldsHierarchicallyWithAnnotation(entityPlc.getClass(), PlcFileAttach.class);
		
		PlcBaseContextVO contexto = contextMontaUtil.createContextParamMinimum();
		for (Field field : camposAnotados) {
			if(field.getAnnotation(PlcNotCloneable.class) == null) {
				PlcFileAttach fileAttach = field.getAnnotation(PlcFileAttach.class);
				try {
					
					if (List.class.isAssignableFrom(propertyUtilsBean.getProperty(entityPlc, field.getName()).getClass())) {
						List<IPlcFile> lista = (List) propertyUtilsBean.getProperty(entityPlc, field.getName());
						List<IPlcFile> listaNova = new ArrayList<IPlcFile>();
						for (IPlcFile entityFile : lista) {
							entityFile = cloneFile(contexto, entityPlc, url, entityFile);
							listaNova.add(entityFile);
						}
						propertyUtilsBean.setProperty(entityPlc, field.getName(), listaNova);
					} else {					
						IPlcFile entityFile = (IPlcFile) propertyUtilsBean.getProperty(entityPlc, field.getName());
						entityFile = cloneFile(contexto, entityPlc,	url, entityFile);
						propertyUtilsBean.setProperty(entityPlc,  field.getName(), entityFile);
						//setando  o arquivo na sessão para ser recuperado  pelo service
						if  (fileAttach.image()) {
							contextUtil.getRequest().getSession().setAttribute(url + "_" + field.getName(), entityFile);
						}
					}
					
				} catch (Exception e) {
					log.debug("Erro ao clonar arquivo anexado. Campo "  + field.getName() + " da entidade " + entityPlc.getClass()  + ".<br>Erro original: "+e);
				}
			}	
		}		

	}
	private IPlcFile cloneFile(PlcBaseContextVO contexto, Object entityPlc,
			String url, IPlcFile entityFile) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			InstantiationException {

		// Clonando o PlcFile
		entityFile = iocControleFacadeUtil.getFacade(url).downloadFile(contexto, entityFile.getClass(), entityFile.getId());
		entityFile.setId(null);

		//Clonando o PlcFileContent
		PlcFileContent entityFileContent = (PlcFileContent) entityFile.getClass().getMethod("getBinaryContent").getAnnotation(javax.persistence.OneToOne.class).targetEntity().newInstance();
		propertyUtilsBean.setProperty(entityFileContent, "binaryContent", ((PlcBaseMapFileContent)propertyUtilsBean.getProperty(entityFile, "binaryContent")).getBinaryContent());
		
		//setando as propriedades
		propertyUtilsBean.setProperty(entityFile, "binaryContent", entityFileContent);
		return entityFile;
	}

	
	
}
