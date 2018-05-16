/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.acme.planos_assist.model;

import com.powerlogic.jcompany.commons.annotation.PlcDbFactory;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcJpaManager;
import com.powerlogic.jcompany.persistence.jpa.PlcDefaultJpaManager;

@SPlcJpaManager
@PlcDbFactory(nome="pla",autoDetectDialect=true)
public class PlaDefaultJpaManager extends PlcDefaultJpaManager {

}
