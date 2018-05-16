package com.empresa.rhtutorial2.controller.jsf.unidadeorganizacional;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcSpecific;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.controller.jsf.PlcBaseCreateMB;
import com.powerlogic.jcompany.controller.jsf.PlcEntityList;

@QPlcSpecific(name="unidadeorganizacional")
public class UnidadeOrganizacionalCreateMB extends PlcBaseCreateMB {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected boolean createBefore(FormPattern pattern, Object entityPlc, PlcEntityList entityListPlc, boolean isMestreDetalheOuSubDetalhe, String detCorrPlc) {
		log.info("TREINAMENTO - CICLO DE VIDA - CREATE BEFORE");
		super.createBefore(pattern, entityPlc, entityListPlc,
		isMestreDetalheOuSubDetalhe, detCorrPlc);
		return true;
	}
	
	@Override
	protected String createAfter(FormPattern pattern, Object entityPlc,
		PlcEntityList entityListPlc, String detCorrPlc) {
		log.info("TREINAMENTO - CICLO DE VIDA - CREATE AFTER");
		super.createAfter(pattern, entityPlc, entityListPlc, detCorrPlc);
		return null;
	}
	
}
