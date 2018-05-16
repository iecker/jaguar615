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

import br.com.plc.jcompany_fcls.entity.departamento.DepartamentoEntity;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.aggregation.PlcConfigComponent;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@PlcConfigAggregation(
		entity = br.com.plc.jcompany_fcls.entity.departamento.DepartamentoEntity.class,
		components = {@PlcConfigComponent(clazz=br.com.plc.jcompany_fcls.entity.departamento.Endereco.class, property="endereco")}
)

@PlcConfigForm(
		formPattern= FormPattern.Con,
		formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/departamento")
		
)

@SPlcMB
@PlcUriIoC("consultardepartamento")	
@PlcHandleException
@ConversationScoped
public class ConsultaDepartamentoMB extends AppMB {

	/**
	 * Entidade da aÃ§Ã£o injetado pela CDI
	 */
	@Produces  @Named("consultardepartamento") 
	public DepartamentoEntity createEntityPlc() {
		if (this.entityPlc==null) {
			this.entityPlc = new DepartamentoEntity();
			this.newEntity();
		}
		return (DepartamentoEntity)this.entityPlc;
	}

}
