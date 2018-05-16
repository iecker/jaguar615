/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.persistence.jpa;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import com.powerlogic.jcompany.commons.annotation.PlcDbFactory;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcJpaManager;

@SPlcJpaManager
@PlcDbFactory(nome="default",autoDetectDialect=true)
public class PlcDefaultJpaManager extends PlcBaseJpaManager {
	

	@PersistenceUnit(unitName = "default")
	private EntityManagerFactory persistenceUnit;
	
	public EntityManagerFactory getPersistenceUnit() {
		return persistenceUnit;
	}

}

