package com.empresa.cargosalario.service;

import java.math.BigDecimal;

import com.empresa.rhavancado.entity.Funcionario;
import com.powerlogic.rhavancado.ws.BuscaFuncionarioWS;
import com.powerlogic.rhavancado.ws.BuscaFuncionarioWSService;

public class CargoServiceWSClient {

	public BigDecimal extrasPorCargo(String codigoCargo) {
		if (codigoCargo.startsWith("EXE")) {
			return new BigDecimal(1000);
		} else {
			return new BigDecimal(500);
		}
	}

	public Funcionario buscaFuncionario(String cpf) {
		BuscaFuncionarioWSService servico = new BuscaFuncionarioWSService();
		BuscaFuncionarioWS port = servico.getBuscaFuncionarioWSPort();
		return port.busca(cpf);
	}

}