package com.powerlogic.jcompany.controller.rest.service;

import javax.ws.rs.core.Response;

import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.rest.api.controller.IPlcController;
import com.powerlogic.jcompany.controller.rest.api.conversor.IPlcConversor;
import com.powerlogic.jcompany.controller.rest.extensions.PlcServiceManager;
import com.powerlogic.jcompany.controller.rest.producers.PlcJAXRSProducer;
import com.powerlogic.jcompany.controller.rest.producers.PlcServiceProducer;

/**
 * @author Adolfo Jr.
 */
public abstract class PlcBaseService {

	protected PlcJAXRSProducer getJAXRSProducer() {
		return PlcCDIUtil.getInstance().getInstanceByType(PlcJAXRSProducer.class);
	}

	protected PlcServiceManager getServiceManager() {
		return PlcCDIUtil.getInstance().getInstanceByType(PlcServiceManager.class);
	}

	protected PlcServiceProducer getServiceProducer() {
		return PlcCDIUtil.getInstance().getInstanceByType(PlcServiceProducer.class);
	}

	@SuppressWarnings("unchecked")
	protected <E, I> IPlcController<E, I> getController() {
		return (IPlcController<E, I>) getServiceProducer().getController();
	}

	@SuppressWarnings("unchecked")
	protected <E, I> IPlcConversor<IPlcController<E, I>> getConversor() {
		return (IPlcConversor<IPlcController<E, I>>) getServiceProducer().getConversor();
	}

	@SuppressWarnings("unchecked")
	protected <I> I getControllerEntityId() {
		return (I) getServiceProducer().getControllerEntityId();
	}

	protected <E, I> Response getResponse(IPlcController<E, I> controller) {
		
		return Response
			.ok()
			.type(getServiceProducer().getConversorMediaType())
			.entity(controller)
			.build();
	}
}
