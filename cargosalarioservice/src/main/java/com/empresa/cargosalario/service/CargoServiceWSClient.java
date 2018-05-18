package com.empresa.cargosalario.service;

import java.math.BigDecimal;

public class CargoServiceWSClient {

	public BigDecimal extrasPorCargo(String codigoCargo) {
		if (codigoCargo.startsWith("EXE")) {
			return new BigDecimal(1000);
		} else {
			return new BigDecimal(500);
		}
	}

}