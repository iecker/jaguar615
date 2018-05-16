package com.empresa.rhtutorial2.persistence.jpa.parametroglobal;

import javax.persistence.EntityManager;

import com.empresa.rhtutorial2.commons.AppBaseContextVO;
import com.empresa.rhtutorial2.entity.ParametroGlobal;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.persistence.jpa.PlcBaseJpaDAO;

public class ParametroGlobalDAO extends PlcBaseJpaDAO {
	
	public ParametroGlobal recuperaParametroGlobal() {	
		try {
		// Se for EJB, o EM pode ser injetado pelo CDI
		EntityManager em = getEntityManager(new AppBaseContextVO());	
		return (ParametroGlobal)em.createNamedQuery("ParametroGlobalEntity.edita").getSingleResult();
		} catch (Exception e) {
		throw new PlcException("ParametroGlobalDAO",
		"recuperaParametroGlobal", e, log, "");
		}
	}
}
