package com.empresa.rhtutorial2.controller.jsf.unidadeorganizacional;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcSpecific;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.controller.jsf.PlcBaseDeleteMB;
import com.powerlogic.jcompany.controller.jsf.PlcEntityList;


@QPlcSpecific(name="unidadeorganizacional")
public class UnidadeOrganizacionalDeleteMB extends PlcBaseDeleteMB {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected boolean deleteBefore (FormPattern pattern, Object entityPlc, PlcBaseContextVO contextVO) {
		log.info ("TREINAMENTO - CICLO DE VIDA - DELETE BEFORE");
		deleteBefore (pattern, entityPlc, contextVO);
		return true;
	}
	
	@Override
	protected Object deleteCompleteEntityBefore (Object entity) {
		log.info("TREINAMENTO - CICLO DE VIDA - DELETE COMPLETE ENTITY BEFORE");
		super.deleteCompleteEntityBefore (entity);
		return null;
	}
	
	@Override
	protected boolean deleteItemBefore (Object entityPlc, PlcEntityList entityListPlc, String collection, int index) {
		log.info ("TREINAMENTO - CICLO DE VIDA - DELETE ITEM BEFORE");
		super.deleteItemBefore (entityPlc, entityListPlc, collection, index);
		return true;
	}
	
	@Override
	protected String deleteAfter (Object vo, Object entityPlc, PlcBaseContextVO context)  {
		log.info ("TREINAMENTO - CICLO DE VIDA - DELETE AFTER");
		deleteAfter (vo, entityPlc, context);
		return null;
	}
	
	@Override
	protected String deleteItemAfter (Object entityPlc, PlcEntityList entityListPlc)  {
		log.info ("TREINAMENTO - CICLO DE VIDA - DELETE ITEM AFTER");
		deleteItemAfter (entityPlc, entityListPlc);
		return null;
	}
	
}
