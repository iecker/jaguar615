package com.empresa.rhtutorial.controller.jsf.funcionario;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.empresa.rhtutorial.controller.jsf.AppMB;
import com.empresa.rhtutorial.entity.funcionario.FuncionarioEntity;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.config.collaboration.PlcConfigNestedCombo;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@PlcConfigAggregation(
		entity = com.empresa.rhtutorial.entity.funcionario.FuncionarioEntity.class

		,components = {@com.powerlogic.jcompany.config.aggregation.PlcConfigComponent(clazz=com.empresa.rhtutorial.entity.Endereco.class, property="enderecoResidencial")}
		,details = {@com.powerlogic.jcompany.config.aggregation.PlcConfigDetail(clazz = com.empresa.rhtutorial.entity.funcionario.DependenteEntity.class,
								collectionName = "dependente", numNew = 2,onDemand = true)
			,
		@com.powerlogic.jcompany.config.aggregation.PlcConfigDetail(clazz = com.empresa.rhtutorial.entity.funcionario.HistoricoProfissionalEntity.class,
								collectionName = "historicoProfissional", numNew = 4, onDemand = true)			
		}
	)

@PlcConfigForm (
	formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/funcionario")
	,behavior = @com.powerlogic.jcompany.config.collaboration.PlcConfigBehavior(batchInput=true),
	nestedCombo=@PlcConfigNestedCombo(origemProp="enderecoResidencial.uf",destinyProp="enderecoResidencial.municipio")
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
              this.entityPlc = new FuncionarioEntity();
              this.newEntity();
        }

        return (FuncionarioEntity)this.entityPlc;
        
	}	
}
