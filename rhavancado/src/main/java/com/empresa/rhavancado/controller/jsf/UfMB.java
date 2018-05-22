package com.empresa.rhavancado.controller.jsf;

import javax.inject.Named;
import javax.ws.rs.Produces;

import com.empresa.rhavancado.entity.Uf;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.config.collaboration.PlcConfigTabular;
import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;


@PlcConfigForm(
	formPattern=FormPattern.Tab,
	tabular = @PlcConfigTabular(numNew = 10),
	formLayout =@PlcConfigFormLayout(dirBase="/WEB-INF/fcls/uf")
)
@PlcConfigAggregation(entity = Uf.class)
@SPlcMB
@PlcUriIoC("uf")
@PlcHandleException
public class UfMB extends AppMB {

	@Produces
	@Named("ufLista")
	public PlcEntityList criaListaEntidadePlc() {
		if (this.entityListPlc == null) {
			this.entityListPlc = new PlcEntityList();
			this.newObjectList();
		}
		return this.entityListPlc;
	}
	
	

}