package com.empresa.rhtutorial2.controller.jsf.unidadeorganizacional;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcSpecific;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.controller.jsf.PlcBaseSaveMB;
import com.powerlogic.jcompany.controller.jsf.PlcEntityList;

@QPlcSpecific(name="unidadeorganizacional")
public class UnidadeOrganizacionalSaveMB extends PlcBaseSaveMB {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected boolean saveBefore (FormPattern pattern, Object entityPlc, PlcBaseContextVO contextVO) {
		log.info("TREINAMENTO - CICLO DE VIDA - SAVE BEFORE");
		super.saveBefore(pattern, entityPlc, contextVO);
		return true;
	}
	
	@Override
	protected String saveCompleteEntityBefore (FormPattern pattern, Object entityPlc, PlcBaseContextVO contextVO) {
		log.info("TREINAMENTO - CICLO DE VIDA - SAVE COMPLETE ENTITY BEFORE");
		super.saveCompleteEntityBefore(pattern, entityPlc, contextVO);
		return null;
	}
	
	@Override
	protected String saveItensAfter (PlcEntityList entityListPlc, PlcBaseContextVO contextVO) {
		log.info("TREINAMENTO - CICLO DE VIDA - SAVE ITENS AFTER");
		super.saveItensAfter (entityListPlc, contextVO);
		return null;
	}	
	
}
