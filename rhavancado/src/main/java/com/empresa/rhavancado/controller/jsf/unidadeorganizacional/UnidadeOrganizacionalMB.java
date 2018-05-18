package com.empresa.rhavancado.controller.jsf.unidadeorganizacional;

import javax.enterprise.inject.Produces;
import javax.inject.Named;


import com.empresa.rhavancado.entity.UnidadeOrganizacional;
import com.empresa.rhavancado.controller.jsf.AppMB;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.config.collaboration.FormPattern;

import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm.ExclusionMode;



 
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;

@PlcConfigAggregation(
		entity = com.empresa.rhavancado.entity.UnidadeOrganizacionalEntity.class
		
	)
	


@PlcConfigForm (
	
	formPattern=FormPattern.Man,
	formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls")
	
	
)


/**
 * Classe de Controle gerada pelo assistente
 */
 
@SPlcMB
@PlcUriIoC("unidadeorganizacional")
@PlcHandleException
public class UnidadeOrganizacionalMB extends AppMB  {

	private static final long serialVersionUID = 1L;
	
	
     		
	/**
	* Entidade da ação injetado pela CDI
	*/
	@Produces  @Named("unidadeorganizacional")
	public UnidadeOrganizacional createEntityPlc() {
        if (this.entityPlc==null) {
              this.entityPlc = new UnidadeOrganizacional();
              this.newEntity();
        }
        return (UnidadeOrganizacional)this.entityPlc;     	
	}
		
}
