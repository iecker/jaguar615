package com.empresa.cargosalario.controller.jsf.cargo;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.empresa.cargosalario.controller.jsf.AppMB;
import com.empresa.cargosalario.entity.CargoEntity;
import com.empresa.cargosalario.facade.ICargoService;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@PlcConfigAggregation(entity = com.empresa.cargosalario.entity.CargoEntity.class

)
@PlcConfigForm(

formPattern = FormPattern.Man, formLayout = @PlcConfigFormLayout(dirBase = "/WEB-INF/fcls")

)
/**
 * Classe de Controle gerada pelo assistente
 */
@SPlcMB
@PlcUriIoC("cargo")
@PlcHandleException
public class CargoMB extends AppMB {

	private static final long serialVersionUID = 1L;

	@Inject
	ICargoService cargoService;

	/**
	 * Entidade da ação injetado pela CDI
	 */
	@Produces
	@Named("cargo")
	public CargoEntity createEntityPlc() {
		if (this.entityPlc == null) {
			this.entityPlc = new CargoEntity();
			this.newEntity();
		}
		return (CargoEntity) this.entityPlc;
	}

	public String calculaExtras() {

		String codigoCargo = "00001";

		cargoService.extrasPorCargo(codigoCargo);

		return null;
	}

}
