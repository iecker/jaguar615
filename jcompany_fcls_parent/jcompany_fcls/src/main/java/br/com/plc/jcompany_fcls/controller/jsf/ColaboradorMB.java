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

import br.com.plc.jcompany_fcls.entity.colaborador.ColaboradorEntity;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.aggregation.PlcConfigDetail;
import com.powerlogic.jcompany.config.aggregation.PlcConfigPagedDetail;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@PlcConfigAggregation(
		entity = br.com.plc.jcompany_fcls.entity.colaborador.ColaboradorEntity.class,
		details = {
			@PlcConfigDetail(clazz = br.com.plc.jcompany_fcls.entity.colaborador.SetorEntity.class,
								collectionName = "setor", numNew = 4,
								onDemand = false,
			navigation =@PlcConfigPagedDetail(numberByPage=2))
,
			@PlcConfigDetail(clazz = br.com.plc.jcompany_fcls.entity.colaborador.PromocaoEntity.class,
								collectionName = "promocao", numNew = 4,
								onDemand = false,
								navigation =@PlcConfigPagedDetail(numberByPage=4)/*,
								argumentos = {
									@PlcConfigArgumentoDetalhe(
											formato=Formato.DATE,
											operador=Operador.LIKE_PERC_FINAL,
											propriedade = "dataPromocao") 
									}*/
			)
		}
	)

@SPlcMB
@PlcUriIoC("colaborador")	
@PlcHandleException
@ConversationScoped
public class ColaboradorMB extends AppMB {

	@Inject
	protected transient Logger log;

	/**
	 * Entidade da aÃ§Ã£o injetado pela CDI
	 */
	@Produces  @Named("colaborador") 
	public ColaboradorEntity criaentityPlc() {
		if (this.entityPlc==null) {
			this.entityPlc = new ColaboradorEntity();
			this.newEntity();
		}
		return (ColaboradorEntity)this.entityPlc;
	}
	
}
