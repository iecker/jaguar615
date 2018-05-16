/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.controller.jsf;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import br.com.plc.jcompany_fcls.entity.suitecasoteste.SuiteCasoTesteEntity;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@PlcConfigAggregation(
		entity = br.com.plc.jcompany_fcls.entity.suitecasoteste.SuiteCasoTesteEntity.class
)

@SPlcMB
@PlcUriIoC("suitecasoteste")
@PlcHandleException
@ConversationScoped
public class SuiteCasoTesteMB extends AppMB {
	
	/**
	 * Entidade da aÃ§Ã£o injetado pela CDI
	 */
	@Produces  @Named("suitecasoteste") 
	public SuiteCasoTesteEntity criaentityPlc() {
		if (this.entityPlc==null) {
			this.entityPlc = new SuiteCasoTesteEntity();
			this.newEntity();
		}
		return (SuiteCasoTesteEntity)this.entityPlc;
	}


}
