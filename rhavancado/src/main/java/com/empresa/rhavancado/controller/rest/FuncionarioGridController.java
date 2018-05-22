package com.empresa.rhavancado.controller.rest;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import com.empresa.cargosalario.facade.ICargoService;
import com.empresa.rhavancado.entity.FuncionarioEntity;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerName;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerQualifier;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcController;
import com.powerlogic.jcompany.controller.rest.controllers.PlcBaseGridController;

@SPlcController
@QPlcControllerName("grid")
@QPlcControllerQualifier("funcionario")
public class FuncionarioGridController<E, I> extends
		PlcBaseGridController<E, I> {

	private Double totalSalario = 0.0;

	@Inject
	protected ICargoService cargoService;

	@Override
	protected void retrieveCollectionAfter() {

		List<FuncionarioEntity> lista = (List<FuncionarioEntity>) getEntityCollection();

		BigDecimal salario;
		for (FuncionarioEntity e : lista) {
			// recupera salario de um funcionario
			salario = cargoService.salarioPorCargo(getContext(),
					((FuncionarioEntity) e).getCodigoCargo());
			if (salario != null)
				totalSalario += salario.doubleValue();
		}
		super.retrieveCollectionAfter();
	}

	public Double getTotalSalario() {
		return totalSalario;
	}

	public void setTotalSalario(Double totalSalario) {
		this.totalSalario = totalSalario;
	}
}
