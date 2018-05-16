/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/ 
package br.com.plc.jcompany_fcls.controller.jsf;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import br.com.plc.jcompany_fcls.entity.suitecasoteste.CasoTesteEntity;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@PlcConfigAggregation(
		entity = br.com.plc.jcompany_fcls.entity.suitecasoteste.CasoTesteEntity.class
)


@SPlcMB
@PlcUriIoC("casoteste")
@PlcHandleException
public class CasoTesteMB extends AppMB {


	@Produces  @Named("casoTestes") 
	public PlcEntityList criaEntityListPlc() {
		if (this.entityListPlc==null) {
			this.entityListPlc = new PlcEntityList();
			this.newObjectList();
		}
		return this.entityListPlc;
	}		
	
	@Produces  @Named("casoteste") 
	public CasoTesteEntity criaEntityPlc() {
		if (this.entityPlc==null) {
			this.entityPlc = new CasoTesteEntity();
		}
		return (CasoTesteEntity)this.entityPlc;
	}

	
}
