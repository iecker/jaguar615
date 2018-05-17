package com.empresa.rhtutorial2.entity.funcionario.pendencia;

import java.math.BigDecimal;

public class RegraSalarioMinimoPorDependente {

	private static final BigDecimal VALOR_POR_DEPENDENTE = new BigDecimal(2000);

	public BigDecimal calculaPendencia(BigDecimal salarioAtual,
		int totalDependentes) {
		if (totalDependentes == 0)
			return new BigDecimal(0);
		
		BigDecimal pendencia = salarioAtual.subtract(VALOR_POR_DEPENDENTE.multiply(new BigDecimal(totalDependentes)));
		
		if (pendencia.longValue() < 0)
			return pendencia;
		else
			return new BigDecimal(0);
	}

}
