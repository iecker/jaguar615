package com.empresa.rhtutorial2.controller.jsf.unidadeorganizacional;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcSpecific;
import com.powerlogic.jcompany.controller.jsf.PlcBaseEditMB;

@QPlcSpecific(name="unidadeorganizacional")
public class UnidadeOrganizacionalEditMB extends PlcBaseEditMB {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected boolean editBefore (Object entityPlc, PlcBaseContextVO contextVO) {
		log.info("TREINAMENTO - CICLO DE VIDA - EDIT BEFORE");
		editBefore(entityPlc, contextVO);
		return true;
	}
	
	@Override
	protected String editAfter (Object entityPlc, PlcBaseContextVO contextVO) {
		log.info("TREINAMENTO - CICLO DE VIDA - EDIT AFTER");
		editAfter (entityPlc, contextVO);
		return null;
	}
	
}
