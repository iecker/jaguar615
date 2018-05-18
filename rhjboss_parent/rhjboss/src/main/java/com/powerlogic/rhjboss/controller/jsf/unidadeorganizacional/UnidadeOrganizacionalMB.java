package com.powerlogic.rhjboss.controller.jsf.unidadeorganizacional;

import javax.enterprise.inject.Produces;
import javax.inject.Named;


import com.powerlogic.rhjboss.entity.UnidadeOrganizacionalEntity;
import com.powerlogic.rhjboss.controller.jsf.AppMB;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.config.collaboration.FormPattern;

import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm.ExclusionMode;



 
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;

@PlcConfigAggregation(
		entity = com.powerlogic.rhjboss.entity.UnidadeOrganizacionalEntity.class
		

		,components = {@com.powerlogic.jcompany.config.aggregation.PlcConfigComponent(clazz=com.powerlogic.rhjboss.entity.Endereco.class, property="endereco")}
	)
	


@PlcConfigForm (
	
	formPattern=FormPattern.Man,
	formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/unidadeorganizacional")
	
	
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
	public UnidadeOrganizacionalEntity createEntityPlc() {
        if (this.entityPlc==null) {
              this.entityPlc = new UnidadeOrganizacionalEntity();
              this.newEntity();
        }
        return (UnidadeOrganizacionalEntity)this.entityPlc;     	
	}
		
}
