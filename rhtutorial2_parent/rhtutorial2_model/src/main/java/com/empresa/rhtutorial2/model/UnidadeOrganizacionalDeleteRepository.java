package com.empresa.rhtutorial2.model;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.model.PlcBaseDeleteRepository;

public class UnidadeOrganizacionalDeleteRepository extends PlcBaseDeleteRepository {

	@Override
	protected void deleteAfter (Object dao, PlcBaseContextVO context, Object entidade)  {
		log.info ("TREINAMENTO - CICLO DE VIDA - DELETE AFTER");
		deleteAfter (dao, context, entidade);		
	}
	
	protected boolean deleteBefore (PlcBaseContextVO context,Object dao, Object entidade)  {
		log.info ("TREINAMENTO - CICLO DE VIDA - DELETE BEFORE");
		deleteBefore (context, dao, entidade);
		return true;
	}
	
}
