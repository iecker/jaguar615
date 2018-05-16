/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.extension.manytomanypanel.commons;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.beanutils.PropertyUtils;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.controller.jsf.PlcBaseMB;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcCreateContextUtil;
import com.powerlogic.jcompany.controller.util.PlcIocControllerFacadeUtil;
import com.powerlogic.jcompany.extension.manytomanypanel.metadata.PlcManyToManyPanelConfig;

/**
 * Classe utilitária 
 * 
 * @author Mauren Ginaldo Souza
 * @since jun/2012
 * 
 */
@SPlcUtil
@QPlcDefault
public class PlcManyToManyPanelUtil {

	@Inject @QPlcDefault 
	protected PlcIocControllerFacadeUtil iocControleFacadeUtil;
	
	@Inject @QPlcDefault
	protected PlcCreateContextUtil contextMontaUtil;
	
	/**
	 * Retorna a configuração do extension que esta no MB
	 * 
	 * @return PlcManyToManyPanelConfig
	 */
	public PlcManyToManyPanelConfig getPlcManyToManyPanelConfig() {
		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);
		PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);
		Class mb = metamodelUtil.getUriIocMB((String)contextUtil.getRequestAttribute(PlcConstants.PlcJsfConstantes.URL_SEM_BARRA)).getBeanClass();
		PlcManyToManyPanelConfig config = (PlcManyToManyPanelConfig) mb.getAnnotation(PlcManyToManyPanelConfig.class);
		return config;
	}
	
	/**
	 * Busca lista 
	 * 
	 * @param entity
	 * @return
	 */
	public List findList(Object entity) {
		return (List) iocControleFacadeUtil.getFacade().findList(
				contextMontaUtil.createContextParamMinimum(), entity, "", 0, 0);
	}
	
	/**
	 * Salva a lista
	 * 
	 * @param listAssociationFinal
	 */
	public void save(List listAssociationFinal) {
		iocControleFacadeUtil.getFacade().saveTabular(
				contextMontaUtil.createContextParamMinimum(), getPlcManyToManyPanelConfig().associationClass(), listAssociationFinal);

	}
	
	/**
	 * Cria o objeto da entidade principal da agregação
	 * 
	 * @param entityMaster
	 * @return objeto
	 */
	public Object createObjectAssociation(Object entityMaster) {
		PlcManyToManyPanelConfig config = getPlcManyToManyPanelConfig();
		try {
			//Criando entidade associativa
			Object associationEntity = config.associationClass().newInstance();
			//Adicionando a entidade selecionada na entidade associativa
			PropertyUtils.setProperty(associationEntity, config.propertyNameEntityMaster(), entityMaster);
			
			return associationEntity;
		} catch (Exception e) {
			throw new PlcException(e);
		}
	}
	
	/**
	 * Realiza o ajuste das listas "target", "source" e "association"
	 */
	public void adjustLists(PlcBaseMB plcAction, PlcManyToManyPanelConfig config, List listPanelSource, List listPanelTarget) {
		
		List listAux = new ArrayList();
		
		try {
			
			List association = new  ArrayList();
			association.addAll(listPanelTarget);
			
			PropertyUtils.setProperty(plcAction, "listAssociation", association);
			
			for (Object objeto : listPanelTarget) {
				listAux.add(PropertyUtils.getProperty(objeto, config.propertyNameEntityPanel()));
			}
			
			listPanelTarget.clear();
			
			for (Object objeto : listPanelSource) {
				if (listAux.contains(objeto)) {
					listPanelTarget.add(objeto);
				}
			}
			
			PropertyUtils.setProperty(plcAction, "listPanelSource", listPanelSource);
			PropertyUtils.setProperty(plcAction, "listPanelTarget", listPanelTarget);
			
		
		} catch (Exception e) {
			new PlcException(e);	
		}

	}

}
