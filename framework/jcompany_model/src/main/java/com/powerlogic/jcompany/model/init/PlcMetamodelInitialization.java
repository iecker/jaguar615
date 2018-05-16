package com.powerlogic.jcompany.model.init;

import java.util.Set;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import com.powerlogic.jcompany.commons.config.metamodel.PlcBusinessObject;
import com.powerlogic.jcompany.commons.config.metamodel.PlcDataAccessObject;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntity;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcDataAccessObject;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcDataAccessObjectEjb;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcFacade;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcFacadeEjb;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcRepository;
import com.powerlogic.jcompany.commons.facade.IPlcFacade;
import com.powerlogic.jcompany.commons.metamodel.bootstrap.PlcMetamodelUtilBootstrapAfter;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.facade.PlcFacadeImpl;
import com.powerlogic.jcompany.model.PlcBaseRepository;
import com.powerlogic.jcompany.persistence.jpa.PlcBaseJpaDAO;


public class PlcMetamodelInitialization {

	@Inject @QPlcDefault
	Instance<IPlcFacade> facades;
	
	public void initializeMetamodel() {
		
		if(PlcCDIUtil.getInstance().getBeanManager()!=null){
			BeanManager beanManager = PlcCDIUtil.getInstance().getBeanManager();
			PlcMetamodelUtil metamodel = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);
			if(metamodel!=null && metamodel.getApplication().getDataAcccessObjectClassDefault() ==null) {
				metamodel.getApplication().setBusinessObjectClassDefault(PlcCDIUtil.getInstance().getInstanceByType(PlcBaseRepository.class, QPlcDefaultLiteral.INSTANCE).getClass().getSuperclass());
				metamodel.getApplication().setDataAcccessObjectClassDefault(PlcCDIUtil.getInstance().getInstanceByType(PlcBaseJpaDAO.class, QPlcDefaultLiteral.INSTANCE).getClass().getSuperclass());
				metamodel.getApplication().setFacadeClassDefault(PlcFacadeImpl.class);
				Set<Bean<?>> allBeans = beanManager.getBeans(Object.class);
				for (Bean<?> bean : allBeans) {
					processaBean(bean, beanManager, metamodel);
				}
				PlcMetamodelUtilBootstrapAfter metamodelAposEvento = new PlcMetamodelUtilBootstrapAfter();
				beanManager.fireEvent(metamodelAposEvento);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> void processaBean(Bean<T> bean, BeanManager beanManager, PlcMetamodelUtil metamodel) {
		if (bean.getStereotypes().contains(SPlcEntity.class)) {	
			PlcEntity<T> entity = (PlcEntity<T>) metamodel.createEntity( bean.getBeanClass() );
			metamodel.addEntity(entity);
		} else if (bean.getStereotypes().contains(SPlcMB.class)) {	
			metamodel.addUriIocMB(bean);
		} else if (bean.getStereotypes().contains(SPlcFacade.class) || bean.getStereotypes().contains(SPlcFacadeEjb.class)) {	
			metamodel.addUriIocFacade(bean);
		} else if (bean.getStereotypes().contains(SPlcRepository.class)) {
			PlcBusinessObject<T> businessObject = (PlcBusinessObject<T>) metamodel.createBusinessObject( bean.getBeanClass() );
			metamodel.addBusinessObject(businessObject);
		} else if (bean.getStereotypes().contains(SPlcDataAccessObject.class) || bean.getStereotypes().contains(SPlcDataAccessObjectEjb.class)) {
			PlcDataAccessObject<T> dataAccessObject = (PlcDataAccessObject<T>) metamodel.createDataAccessObject( bean.getBeanClass() );
			metamodel.addDataAccessObject(dataAccessObject);
		} 
	}
}
