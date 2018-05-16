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

import br.com.plc.jcompany_fcls.entity.pedido.PedidoEntity;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.aggregation.PlcConfigDetail;
import com.powerlogic.jcompany.config.aggregation.PlcConfigSubDetail;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@PlcConfigAggregation(
		entity = br.com.plc.jcompany_fcls.entity.pedido.PedidoEntity.class,
		details = {
			@PlcConfigDetail(
					clazz 			= br.com.plc.jcompany_fcls.entity.pedido.ProdutosEntity.class,
					collectionName 	= "produtos", 
					numNew 			= 4,
					
					onDemand 		= false,
					subDetail 		= @PlcConfigSubDetail(
										clazz 			= br.com.plc.jcompany_fcls.entity.pedido.CompradorEntity.class,
										collectionName 	= "comprador", 
										numNew 			= 4)
					)
		}
)


@SPlcMB
@PlcUriIoC("pedido")	
@PlcHandleException
@ConversationScoped
public class PedidoMB extends AppMB {
	
	/**
	 * Entidade da aÃ§Ã£o injetado pela CDI
	 */
	@Produces  @Named("pedido") 
	public PedidoEntity criaentityPlc() {
		if (this.entityPlc==null) {
			this.entityPlc = new PedidoEntity();
			this.newEntity();
		}
		return (PedidoEntity)this.entityPlc;
	}


}
