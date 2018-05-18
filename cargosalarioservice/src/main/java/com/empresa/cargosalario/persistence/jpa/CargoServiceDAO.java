package com.empresa.cargosalario.persistence.jpa;

import java.math.BigDecimal;

import javax.persistence.NoResultException;

import com.empresa.cargosalario.entity.CargoView;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.persistence.jpa.PlcBaseJpaDAO;

public class CargoServiceDAO extends PlcBaseJpaDAO {

	public BigDecimal salarioPorCargo(PlcBaseContextVO context,
			String codigoCargo) {

		String query = annotationPersistenceUtil.getNamedQueryByName(
				CargoView.class, "querySelByCodigo").query();

		CargoView cargo = null;

		try {
			cargo = (CargoView) apiCreateQuery(context, CargoView.class, query)
					.setParameter("codigo", codigoCargo).getSingleResult();
		} catch (NoResultException exception) {
			log.info("Nenhum registro encontrado com o login.:" + codigoCargo);
		}

		return cargo.getSalario();
	}	

}
