package com.powerlogic.rhavancado.controller.jsf.funcionario;

import javax.enterprise.inject.Produces;
import javax.inject.Named;


import com.powerlogic.rhavancado.entity.funcionario.FuncionarioEntity;
import com.powerlogic.rhavancado.controller.jsf.AppMB;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.config.collaboration.FormPattern;

import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm.ExclusionMode;



 
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;

@PlcConfigAggregation(
		entity = com.powerlogic.rhavancado.entity.funcionario.FuncionarioEntity.class
		
	)
	


@PlcConfigForm (
	
	formPattern=FormPattern.Man,
	formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/funcionario")
	
	
)


/**
 * Classe de Controle gerada pelo assistente
 */
 
@SPlcMB
@PlcUriIoC("funcionario")
@PlcHandleException
public class FuncionarioMB extends AppMB  {

	private static final long serialVersionUID = 1L;
	
	
     		
	/**
	* Entidade da ação injetado pela CDI
	*/
	@Produces  @Named("funcionario")
	public FuncionarioEntity createEntityPlc() {
        if (this.entityPlc==null) {
              this.entityPlc = new FuncionarioEntity();
              this.newEntity();
        }
        return (FuncionarioEntity)this.entityPlc;     	
	}
		
}
