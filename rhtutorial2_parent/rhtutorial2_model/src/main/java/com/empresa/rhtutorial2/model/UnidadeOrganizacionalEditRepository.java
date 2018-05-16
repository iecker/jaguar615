package com.empresa.rhtutorial2.model;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.model.PlcBaseEditRepository;

public class UnidadeOrganizacionalEditRepository extends PlcBaseEditRepository {

	@Override
	protected void editAfter (PlcBaseContextVO context, Object dao, Class classe, Object id, Object entidade)  {
		log.info ("TREINAMENTO - CICLO DE VIDA - EDIT AFTER");
		editAfter (context, dao, classe, id, entidade);		
	}
	
	protected boolean editBefore (PlcBaseContextVO context,Object dao,Class classe, Object id)  {
		log.info ("TREINAMENTO - CICLO DE VIDA - EDIT AFTER");
		editBefore (context, dao, classe, id);
		return true;
	}
	
}
