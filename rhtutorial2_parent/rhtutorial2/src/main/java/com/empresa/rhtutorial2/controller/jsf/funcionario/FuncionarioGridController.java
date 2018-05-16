package com.empresa.rhtutorial2.controller.jsf.funcionario;

import java.lang.reflect.Array;

import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerName;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerQualifier;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcController;
import com.powerlogic.jcompany.controller.rest.controllers.PlcBaseGridController;

@SPlcController
@QPlcControllerName("grid")
@QPlcControllerQualifier("funcionario")
public class FuncionarioGridController<E, I> extends PlcBaseGridController {

	@Override
	public void retrieveCollectionBefore() {

		Object nome = super.getRequest().getParameterMap().get("nome");		
		
		if (nome == null || StringUtils.isEmpty((String) Array.get(nome, 0)))
			throw new PlcException("Informe iniciais do nome para pesquisar");
	}
	
}
