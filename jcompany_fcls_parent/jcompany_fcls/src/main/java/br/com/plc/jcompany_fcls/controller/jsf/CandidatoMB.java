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
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import br.com.plc.jcompany_fcls.entity.candidato.CandidatoEntity;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.aggregation.PlcConfigDetail;
import com.powerlogic.jcompany.config.aggregation.PlcConfigPagedDetail;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@PlcConfigAggregation(
		entity = CandidatoEntity.class,
		details = {
			@PlcConfigDetail(clazz = br.com.plc.jcompany_fcls.entity.candidato.HistoricoPoliticoEntity.class,
								collectionName = "historicoPolitico", numNew = 4,
								onDemand = true),
								
			@PlcConfigDetail(clazz = br.com.plc.jcompany_fcls.entity.candidato.PartidoEntity.class,
								collectionName = "partido", numNew = 4,
								onDemand = false,
								navigation =@PlcConfigPagedDetail(numberByPage=3)
								)


		}
	)

@SPlcMB
@PlcUriIoC("candidato")	
@PlcHandleException
@ConversationScoped
public class CandidatoMB extends AppMB {

	@Inject
	protected transient Logger log;
	
	@Produces  @Named("candidato") 
	public CandidatoEntity criaentityPlc() {
		if (this.entityPlc==null) {
			this.entityPlc = new CandidatoEntity();
			this.newEntity();
		}
		return (CandidatoEntity)this.entityPlc;
	}
 

}
