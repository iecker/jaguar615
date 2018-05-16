package com.empresa.rhtutorial2.model;

import com.empresa.rhtutorial2.entity.UnidadeOrganizacionalEntity;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationIoC;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcSpecific;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcRepository;
import com.powerlogic.jcompany.model.PlcBaseCreateRepository;

@QPlcSpecific(name="com.powerlogic.rhtutorial2.entity.UnidadeOrganizacionalEntity")
@SPlcRepository
@PlcAggregationIoC (clazz = UnidadeOrganizacionalEntity.class)
public class UnidadeOrganizacionalCreateRepository extends PlcBaseCreateRepository {

	@Override
	protected boolean insertBefore(PlcBaseContextVO context, Object dao,
			Object entidade) {
		log.info("TREINAMENTO - CICLO DE VIDA MODELO - INSERT BEFORE");
		super.insertBefore(context, dao, entidade);
		return true;
	}

	@Override
	protected void insertAfter (PlcBaseContextVO context, Object dao, Object entidade) {
		log.info("TREINAMENTO - CICLO DE VIDA MODELO - INSERT AFTER");
		super.insertAfter(context, dao, entidade);
	}

	@Override
	protected void insertBuildIdentifierApi (PlcBaseContextVO context, Object dao, Object entidade) {
		log.info("TREINAMENTO - CICLO DE VIDA MODELO - INSERT API");
		super.insertBuildIdentifierApi(context, dao, entidade);
	}

}
