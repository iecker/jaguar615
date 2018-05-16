package com.empresa.rhrich.controller.jsf.departamento;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.empresa.rhrich.controller.jsf.AppMB;
import com.empresa.rhrich.entity.DepartamentoEntity;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@PlcConfigAggregation(
		entity = com.empresa.rhrich.entity.DepartamentoEntity.class
		

		,components = {@com.powerlogic.jcompany.config.aggregation.PlcConfigComponent(clazz=com.empresa.rhrich.entity.Endereco.class, property="endereco")}
	)
	



@PlcConfigForm (formPattern=FormPattern.Man,formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/departamento"))


/**
 * Classe de Controle gerada pelo assistente
 */
 
@SPlcMB
@PlcUriIoC("departamento")
@PlcHandleException
public class DepartamentoMB extends AppMB  {

	private static final long serialVersionUID = 1L;
	

	/**
	* Entidade da ação injetado pela CDI
	*/
	@Produces  @Named("departamento")
	public DepartamentoEntity criaEntidadePlc() {

        if (this.entityPlc==null) {
              this.entityPlc = new DepartamentoEntity();
              this.newEntity();
        }

        return (DepartamentoEntity)this.entityPlc;
        
	}	
	

}
