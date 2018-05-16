package com.acme.rhdemoenterprise.controller.jsf.funcionario;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.acme.rhdemoenterprise.controller.jsf.AppMB;
import com.acme.rhdemoenterprise.entity.Endereco;
import com.acme.rhdemoenterprise.entity.Funcionario;
import com.acme.rhdemoenterprise.entity.FuncionarioEntity;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.aggregation.PlcConfigComponent;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@PlcConfigAggregation(
		entity = com.acme.rhdemoenterprise.entity.FuncionarioEntity.class,
		components= {
            @PlcConfigComponent(clazz= com.acme.rhdemoenterprise.entity.Endereco.class, property="enderecoResidencial" )
		},
		details = {
			@com.powerlogic.jcompany.config.aggregation.PlcConfigDetail(clazz = com.acme.rhdemoenterprise.entity.DependenteEntity.class,
								collectionName = "dependente", numNew = 2,
								onDemand = true),
			@com.powerlogic.jcompany.config.aggregation.PlcConfigDetail(clazz = com.acme.rhdemoenterprise.entity.HistoricoProfissionalEntity.class,
								collectionName = "historicoProfissional", numNew = 4,
								onDemand = true)
		}
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
	* Entidade da aÃ§Ã£o injetado pela CDI
	*/
	@Produces  @Named("funcionario")
	public FuncionarioEntity criaEntidadePlc() {

        if (this.entityPlc==null) {
        	Funcionario funcionario = new FuncionarioEntity();
        	funcionario.setEnderecoResidencial(new Endereco());
        	this.entityPlc = funcionario;
        	this.newEntity();
        }

        return (FuncionarioEntity)this.entityPlc;
	}	
}
