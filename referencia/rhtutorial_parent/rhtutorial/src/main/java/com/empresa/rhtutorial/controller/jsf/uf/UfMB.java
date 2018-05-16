package com.empresa.rhtutorial.controller.jsf.uf;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.empresa.rhtutorial.controller.jsf.AppMB;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
	
@PlcConfigAggregation(entity = com.empresa.rhtutorial.entity.UfEntity.class)

@SPlcMB
@PlcUriIoC("uf")	
@PlcHandleException
public class UfMB extends AppMB  {

	private static final long serialVersionUID = 1L;

	@Produces  @Named("ufLista")
	public PlcEntityList criaListaEntidadePlc() {
		if (this.entityListPlc==null) {
			this.entityListPlc = new PlcEntityList();
			this.newObjectList();
		}
		return this.entityListPlc;
	}	
}
