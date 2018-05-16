package com.acme.modulo.rhdemoenterprise.negocio.entity;

import java.math.BigDecimal;

public class DadosProfional extends AcmBaseEntity {

	private BigDecimal salario;

	public BigDecimal getSalario() {
		return salario;
	}

	public void setSalario(BigDecimal salario) {
		this.salario = salario;
	}	
}
