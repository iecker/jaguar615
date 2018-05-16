package com.empresa.rhtutorial.controller.jsf.funcionario;

import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerName;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerQualifier;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcController;
import com.powerlogic.jcompany.controller.rest.controllers.PlcBaseGridController;

@SPlcController
@QPlcControllerName("grid")
@QPlcControllerQualifier("funcionario")
public class FuncionarioGridController<E, I> extends PlcBaseGridController<E, I>{

	@Override
	protected void retrieveCollectionAfter() {
		super.retrieveCollectionAfter();
	}
	
	
}
