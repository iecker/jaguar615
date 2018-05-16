package com.powerlogic.jcompany.controller.rest.controllers;

import java.util.Collection;
import java.util.LinkedList;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerName;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcController;
import com.powerlogic.jcompany.controller.rest.extensions.PlcControllerInfo;
import com.powerlogic.jcompany.controller.rest.extensions.PlcServiceManager;

@SPlcController
@QPlcControllerName("help")
public class PlcBaseHelpController<E, I> extends PlcBaseController<E, I> {
	//<PlcControllerInfo, String>
	
	@Inject
	private PlcServiceManager serviceManager;
	
	private HttpServletRequest request;

	@Override
	public void retrieveCollection() {
		setEntityCollection((Collection<E>)new LinkedList<PlcControllerInfo>(serviceManager.getAllControllerInfo().values()));
	}

	@Override
	public void retrieve(I identificador) {
		setEntity((E)serviceManager.findControllerInfo((String)identificador));
	}

	
	public HttpServletRequest getRequest() {
		return request;
	}

	@Inject
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	
}
