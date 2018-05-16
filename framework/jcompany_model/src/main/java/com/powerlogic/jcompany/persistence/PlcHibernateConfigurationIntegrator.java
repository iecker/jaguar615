/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.persistence;

import org.apache.log4j.Logger;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.persistence.jpa.PlcSchemaUpdate;

public class PlcHibernateConfigurationIntegrator  implements Integrator {
	
	protected Logger	log	= Logger.getLogger(getClass().getCanonicalName());
	
	
	@Override
	public void integrate(Configuration configuration, SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
		
		try{
			PlcSchemaUpdate schema = PlcCDIUtil.getInstance().getInstanceByType(PlcSchemaUpdate.class);
			schema.setConfiguration(configuration);
		} catch (Exception e) {
			log.warn("Não encontrou objeto PlcSchemaUpdate. Gerador de esquema não estará funcional");
		}
		
	}

	@Override
	public void integrate(MetadataImplementor metadata, SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
		
		log.info("PlcHibernateConfigurationIntegrator -  integrate(MetadataImplementor metadata, SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) ");
		
	}

	@Override
	public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {

		log.info("PlcHibernateConfigurationIntegrator - disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {");
		
	}
}

