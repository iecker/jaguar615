package com.empresa.rhtutorial2.controller.jsf.pendencia;

import javax.enterprise.inject.Produces;
import javax.inject.Named;


import com.empresa.rhtutorial2.entity.funcionario.pendencia.Pendencia;
import com.empresa.rhtutorial2.controller.jsf.AppMB;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.config.collaboration.FormPattern;

import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm.ExclusionMode;



import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;

@PlcConfigAggregation(
		entity = com.empresa.rhtutorial2.entity.funcionario.pendencia.PendenciaEntity.class
	
	)


@PlcConfigForm (
	
	formPattern=FormPattern.Con,
	formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/funcionario.pendencia")
	
	
)


/**
 * Classe de Controle gerada pelo assistente
 */
 
@SPlcMB
@PlcUriIoC("pendencia")
@PlcHandleException
public class PendenciaMB extends AppMB  {

	private static final long serialVersionUID = 1L;
	
	
     		
	/**
	* Entidade da ação injetado pela CDI
	*/
	@Produces  @Named("pendencia")
	public Pendencia createEntityPlc() {
        if (this.entityPlc==null) {
              this.entityPlc = new Pendencia();
              this.newEntity();
        }
        return (Pendencia)this.entityPlc;     	
	}
		
}
