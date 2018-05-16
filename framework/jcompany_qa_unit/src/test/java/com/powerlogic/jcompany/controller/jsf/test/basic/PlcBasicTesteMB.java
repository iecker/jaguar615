/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.test.basic;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.controller.jsf.PlcBaseMB;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@PlcConfigAggregation(
		entity=UserEntity.class
)

@SPlcMB
@PlcUriIoC("testeman")
@PlcHandleException
public class PlcBasicTesteMB extends PlcBaseMB {

	/**
	 * Entidade da ação injetado pela CDI
	 */
	@Produces  @Named("usuario") 
	public UserEntity criaEntidadePlc() {
		if (this.entityPlc==null) {
			this.entityPlc = new UserEntity();
			this.newEntity();
		}
		return (UserEntity)this.entityPlc;
	}
	
}
