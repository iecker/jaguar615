package com.empresa.rhtutorial2.model;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.model.PlcBaseUpdateRepository;
import com.powerlogic.jcompany.persistence.PlcBaseDAO;

public class UnidadeOrganizacionalUpdateRepository extends PlcBaseUpdateRepository {

	@Override
	protected void updateAfter (PlcBaseContextVO context, PlcBaseDAO dao, Object entity)  {
		log.info ("TREINAMENTO - CICLO DE VIDA - UPDATE AFTER");
		updateAfter (context, dao, entity);		
	}
	
	protected boolean updateBefore (Object dao, PlcBaseContextVO context, Object entity)  {
		log.info ("TREINAMENTO - CICLO DE VIDA - UPDATE BEFORE");
		updateBefore (dao, context, entity);
		return true;
	}
	
}
