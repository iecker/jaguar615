package com.empresa.rhrich.controller.jsf.funcionario;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.empresa.rhrich.controller.jsf.AppMB;
import com.empresa.rhrich.entity.FuncionarioEntity;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@PlcConfigAggregation(
		entity = com.empresa.rhrich.entity.FuncionarioEntity.class
		,components = {@com.powerlogic.jcompany.config.aggregation.PlcConfigComponent(clazz=com.empresa.rhrich.entity.Endereco.class, property="endereco")}
		,details = { 		@com.powerlogic.jcompany.config.aggregation.PlcConfigDetail(clazz = com.empresa.rhrich.entity.DependenteEntity.class,
								collectionName = "dependente", numNew = 1, onDemand = true)
			,
		@com.powerlogic.jcompany.config.aggregation.PlcConfigDetail(clazz = com.empresa.rhrich.entity.HistoricoFuncionalEntity.class,
								collectionName = "historicoFuncional", numNew = 1, onDemand = true)
		}
	)
	
@PlcConfigForm (formPattern=FormPattern.Mdt,formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/funcionario"))

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
	public FuncionarioEntity criaEntidadePlc() {

        if (this.entityPlc==null) {
              this.entityPlc = new FuncionarioEntity();
              this.newEntity();
        }

        return (FuncionarioEntity)this.entityPlc;
        
	}	
}