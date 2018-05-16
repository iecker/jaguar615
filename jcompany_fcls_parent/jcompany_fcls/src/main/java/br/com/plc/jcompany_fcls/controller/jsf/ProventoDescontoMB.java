/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
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

import br.com.plc.jcompany_fcls.entity.funcionario.ProventoDescontoEntity;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigBehavior;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@PlcConfigAggregation(
		entity = br.com.plc.jcompany_fcls.entity.funcionario.ProventoDescontoEntity.class
	)
	
@PlcConfigForm(
			formPattern=FormPattern.Ctb,
			behavior = @PlcConfigBehavior(rememberDetail = true)
	)

@SPlcMB
@PlcUriIoC("proventodesconto")
@PlcHandleException
@ConversationScoped
public class ProventoDescontoMB extends AppMB {

	/**
	 * Entidade da aÃ§Ã£o injetado pela CDI
	 */
	@Produces  @Named("proventodesconto") 
	public ProventoDescontoEntity criaentityPlc() {
		if (this.entityPlc==null) {
			this.entityPlc = new ProventoDescontoEntity();
			this.newEntity();
		}
		return (ProventoDescontoEntity)this.entityPlc;
	}

	/**
	 * Entidade da aÃ§Ã£o injetado pela CDI
	 */
	@Produces  @Named("proventodescontoLista")
	public PlcEntityList criaentityListPlc() {
		if (this.entityListPlc==null) {
			this.entityListPlc = new PlcEntityList();
			this.newObjectList();
		}
		return this.entityListPlc;
	}
}
