package com.empresa.cargosalario.facade;

import java.io.Serializable;
import java.math.BigDecimal;

import com.empresa.rhavancado.entity.Funcionario;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;

public interface ICargoService extends Serializable {

	public BigDecimal salarioPorCargo(PlcBaseContextVO context,
			String codigoCargo);

	public BigDecimal extrasPorCargo(String codigoCargo);

	public BigDecimal salarioComExtrasPorCargo(String codigoCargo);
	
	public Funcionario buscaFuncionario(String cpf);

}