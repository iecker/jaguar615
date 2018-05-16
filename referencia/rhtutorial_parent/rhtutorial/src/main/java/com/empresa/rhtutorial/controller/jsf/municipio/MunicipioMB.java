package com.empresa.rhtutorial.controller.jsf.municipio;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.empresa.rhtutorial.controller.jsf.AppMB;
import com.empresa.rhtutorial.entity.UfEntity;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@PlcConfigAggregation(
		entity = com.empresa.rhtutorial.entity.UfEntity.class

		,details = { 		@com.powerlogic.jcompany.config.aggregation.PlcConfigDetail(clazz = com.empresa.rhtutorial.entity.MunicipioEntity.class,
								collectionName = "municipio", numNew = 4,onDemand = false)
		
		}
	)


@PlcConfigForm (
	formPattern=FormPattern.Mad,	
	formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/uf")
	

		,behavior = @com.powerlogic.jcompany.config.collaboration.PlcConfigBehavior(batchInput=true)
)


/**
 * Classe de Controle gerada pelo assistente
 */
 
@SPlcMB
@PlcUriIoC("municipio")
@PlcHandleException
public class MunicipioMB extends AppMB  {

	private static final long serialVersionUID = 1L;
	
	
     		
	/**
	* Entidade da aÃ§Ã£o injetado pela CDI
	*/
	@Produces  @Named("municipioMB")
	public UfEntity criaEntidadePlc() {

        if (this.entityPlc==null) {
              this.entityPlc = new UfEntity();
              this.newEntity();
        }

        return (UfEntity)this.entityPlc;
        
	}	
}
