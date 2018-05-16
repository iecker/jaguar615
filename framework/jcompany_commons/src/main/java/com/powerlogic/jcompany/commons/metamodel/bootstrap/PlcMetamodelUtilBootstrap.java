/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.commons.metamodel.bootstrap;

import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.util.AnnotationLiteral;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.powerlogic.jcompany.commons.config.metamodel.PlcBusinessObject;
import com.powerlogic.jcompany.commons.config.metamodel.PlcDataAccessObject;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntity;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcDataAccessObject;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcDataAccessObjectEjb;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcFacade;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcFacadeEjb;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcRepository;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;

public class PlcMetamodelUtilBootstrap implements Extension {

	private final Logger log = LoggerFactory.getLogger(PlcMetamodelUtilBootstrap.class);
	
	// SAVIO: Podemos usar aqui para validações e para assumir alguns defaults para nossos AnnotatedType...
	public <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> evento) {
		log.debug( "ProcessAnnotatedType: Escaneando tipo: {}", evento.getAnnotatedType().getJavaClass().getName());
	}
	
	public void afterDeploymentValidation(@Observes AfterDeploymentValidation evento, BeanManager beanManager, @QPlcDefault PlcMetamodelUtil metamodel) {
		log.debug( "AfterDeploymentValidation...");

		log.debug( " Inicio do bootstrap...");
				
		PlcMetamodelUtilBootstrapBefore metamodelAntesEvento = new PlcMetamodelUtilBootstrapBefore();
		beanManager.fireEvent(metamodelAntesEvento);
		
		Set<Bean<?>> allBeans = beanManager.getBeans(Object.class);
		for (Bean<?> bean : allBeans) {
			processaBean(bean, beanManager, metamodel);
		}
		
		PlcMetamodelUtilBootstrapAfter metamodelAposEvento = new PlcMetamodelUtilBootstrapAfter();
		beanManager.fireEvent(metamodelAposEvento);
		
		log.debug( " Final do bootstrap...");
	}
	
	@SuppressWarnings("unchecked")
	public <T> void processaBean(Bean<T> bean, BeanManager beanManager, PlcMetamodelUtil metamodel) {
		log.debug( "Processando bean {}", bean);
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
