package com.empresa.rhtutorial.controller.jsf.proventoDesconto;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.empresa.rhtutorial.controller.jsf.AppMB;
import com.empresa.rhtutorial.entity.proventodesconto.ProventoDescontoEntity;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigBehavior;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.config.collaboration.PlcConfigTabular;
import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
	
@PlcConfigAggregation(entity = com.empresa.rhtutorial.entity.proventodesconto.ProventoDescontoEntity.class)

@PlcConfigForm(formPattern=FormPattern.Ctb, tabular = @PlcConfigTabular(numNew = 8),
		formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/proventodesconto"),
		behavior=@PlcConfigBehavior(batchInput=true))

@SPlcMB
@PlcUriIoC("proventodesconto")	
@PlcHandleException
public class ProventoDescontoMB extends AppMB  {

	private static final long serialVersionUID = 1L;

	
	/**
	* Entidade da aÃ§Ã£o injetado pela CDI
	*/
	@Produces  @Named("proventodescontoArg")
	public ProventoDescontoEntity criaEntidadePlc() {

        if (this.entityPlc==null) {
            this.entityPlc = new ProventoDescontoEntity();
            this.newEntity();
      }

      return (ProventoDescontoEntity)this.entityPlc;
        
	}
	
	/**
	 * Entidade da aÃ§Ã£o injetado pela CDI
	*/
	@Produces  @Named("proventodescontoLista")
	public PlcEntityList criaLogicaItensPlc() {
		if (this.entityListPlc==null) {
			this.entityListPlc = new PlcEntityList();
			this.newObjectList();
		}
		return this.entityListPlc;
	}
	
}
