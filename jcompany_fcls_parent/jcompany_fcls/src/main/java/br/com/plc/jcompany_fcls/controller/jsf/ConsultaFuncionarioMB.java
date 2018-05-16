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

import br.com.plc.jcompany_fcls.entity.funcionario.FuncionarioEntity;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.aggregation.PlcConfigComponent;
import com.powerlogic.jcompany.config.aggregation.PlcConfigDetail;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@PlcConfigAggregation(
		entity = br.com.plc.jcompany_fcls.entity.funcionario.FuncionarioEntity.class,

		components = {@PlcConfigComponent(clazz=br.com.plc.jcompany_fcls.entity.departamento.Endereco.class, property="endereco")},
		
		details = {
			@PlcConfigDetail(clazz = br.com.plc.jcompany_fcls.entity.funcionario.HistoricoFuncionalEntity.class,
								collectionName = "historicoFuncional", numNew = 4,
								onDemand = false),
			@PlcConfigDetail(clazz = br.com.plc.jcompany_fcls.entity.funcionario.DependenteEntity.class,
								collectionName = "dependente", numNew = 4,
								onDemand = false)


		}
	)
	
@SPlcMB
@PlcUriIoC("consultafuncionario")	
@PlcHandleException
@ConversationScoped
public class ConsultaFuncionarioMB extends AppMB {
	
	
	/**
	 * Entidade da aÃ§Ã£o injetado pela CDI
	 */
	@Produces  @Named("consultafuncionario") 
	public FuncionarioEntity criaentityPlc() {
		if (this.entityPlc==null) {
			this.entityPlc = new FuncionarioEntity();
			this.newEntity();
		}
		return (FuncionarioEntity)this.entityPlc;
	}

}
