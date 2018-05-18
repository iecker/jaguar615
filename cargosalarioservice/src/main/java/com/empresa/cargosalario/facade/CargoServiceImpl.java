package com.empresa.cargosalario.facade;

import java.math.BigDecimal;

import javax.inject.Inject;

import com.empresa.cargosalario.persistence.jpa.CargoServiceDAO;
import com.empresa.cargosalario.service.CargoServiceWSClient;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;

public class CargoServiceImpl implements ICargoService {

	@Inject
	protected CargoServiceDAO cargoServiceDAO;

	@Inject
	protected CargoServiceWSClient cargoServiceWSClient;

	@Override
	public BigDecimal salarioPorCargo(PlcBaseContextVO context,
			String codigoCargo) {
		return cargoServiceDAO.salarioPorCargo(context, codigoCargo);
	}

	@Override
	public BigDecimal extrasPorCargo(String codigoCargo) {
		return cargoServiceWSClient.extrasPorCargo(codigoCargo);
	}

	@Override
	public BigDecimal salarioComExtrasPorCargo(String codigoCargo) {
		BigDecimal salario = cargoServiceDAO.salarioPorCargo(null, codigoCargo);
		BigDecimal extras = cargoServiceWSClient.extrasPorCargo(codigoCargo);
		BigDecimal total = salario.add(extras);
		return total;
	}

}