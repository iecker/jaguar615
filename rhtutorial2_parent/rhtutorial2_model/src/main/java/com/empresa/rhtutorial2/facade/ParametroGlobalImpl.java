package com.empresa.rhtutorial2.facade;

import javax.inject.Inject;

import com.empresa.rhtutorial2.entity.ParametroGlobal;
import com.empresa.rhtutorial2.persistence.jpa.parametroglobal.ParametroGlobalDAO;
import com.powerlogic.jcompany.model.annotation.PlcTransactional;

public class ParametroGlobalImpl implements IParametroGlobal {

	// Daqui para a frente é possível usar o CDI normalmente
	@Inject
	ParametroGlobalDAO parametroGlobalDAO;

	// Transação local declarativa como EJB, porém funcional para POJOs
	@PlcTransactional(commit = false)
	@Override
	public ParametroGlobal recuperaParametroGlobal() {
		return parametroGlobalDAO.recuperaParametroGlobal();
	}

}
