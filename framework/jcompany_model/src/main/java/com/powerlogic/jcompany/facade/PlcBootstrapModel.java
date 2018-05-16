/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.facade;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.metamodel.bootstrap.PlcMetamodelUtilBootstrapBefore;
import com.powerlogic.jcompany.commons.util.PlcIocFacadeUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.model.PlcBaseRepository;
import com.powerlogic.jcompany.persistence.jpa.PlcBaseJpaDAO;

public class PlcBootstrapModel {

	@Inject
	private transient Logger log;

	@Inject @QPlcDefault PlcIocFacadeUtil iocFacadeUtil;
	@Inject @QPlcDefault PlcBaseRepository boDefault;
	@Inject @QPlcDefault PlcBaseJpaDAO daoDefault;
	
	
	public void process(@Observes PlcMetamodelUtilBootstrapBefore evento, @QPlcDefault PlcMetamodelUtil metamodel) {
		log.debug( "########## Entrou em PlcMetamodelUtilBootstrapAntes...");

		try {
			metamodel.getApplication().setFacadeClassDefault(iocFacadeUtil.getFacade().getClass().getSuperclass());
			metamodel.getApplication().setBusinessObjectClassDefault(boDefault.getClass().getSuperclass());
			metamodel.getApplication().setDataAcccessObjectClassDefault(daoDefault.getClass().getSuperclass());
		} catch (PlcException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}  
	}
}
