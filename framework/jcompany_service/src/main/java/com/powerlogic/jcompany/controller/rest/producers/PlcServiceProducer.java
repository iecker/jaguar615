package com.powerlogic.jcompany.controller.rest.producers;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.controller.rest.api.controller.IPlcController;
import com.powerlogic.jcompany.controller.rest.api.conversor.IPlcConversor;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerName;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerQualifier;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcCurrent;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcEntityId;
import com.powerlogic.jcompany.controller.rest.extensions.PlcControllerInfo;
import com.powerlogic.jcompany.controller.rest.extensions.PlcConversorInfo;
import com.powerlogic.jcompany.controller.rest.extensions.PlcServiceManager;
import com.powerlogic.jcompany.controller.rest.service.PlcServiceConstants.PATH_PARAM;
import com.powerlogic.jcompany.controller.rest.service.PlcServiceConstants.QUERY_PARAM;

/**
 * @author Adolfo Jr.
 */
@RequestScoped
public class PlcServiceProducer {
	
	@Inject
	private PlcServiceManager serviceManager;

	private String controllerPath;

	private String entityId;

	private MediaType conversorMediaType;

	private PlcControllerInfo controllerInfo;

	private IPlcController<?, ?> controller;

	private PlcConversorInfo conversorInfo;

	private IPlcConversor<IPlcController<?, ?>> conversor;

	@PostConstruct
	public void init() {
		if (controllerPath != null) {
			// Faz a busca por um controller que atenda a requisição.
			controllerInfo = serviceManager.findControllerInfo(controllerPath);
			// Encobtrando um controller, continua a busca por um conversor para a
			// requisição.
			if (controllerInfo != null) {
				if (conversorMediaType == null) {
					conversorMediaType = MediaType.WILDCARD_TYPE;
				}
				// Busca pelo conversor especificado.
				conversorInfo = serviceManager.findConversorInfo(controllerInfo, conversorMediaType);
				// Se não encontrou, tenta pelo Wildcard.
				if (conversorInfo == null && !MediaType.WILDCARD_TYPE.equals(conversorMediaType)) {
					// Busca pelo Conversor Default para o Controller.
					conversorInfo = serviceManager.findConversorInfo(controllerInfo, MediaType.WILDCARD_TYPE);
				}
				// O MediaType do conversor deve ser assumido para a resposta.
				if (conversorInfo != null) {
					conversorMediaType = MediaType.valueOf(conversorInfo.getDefaultMediaType());
				}
			}
		}
	}

	@Inject
	public void setControllerPath(@PathParam(PATH_PARAM.CONTROLLER_NAME) String controllerPath) {
		this.controllerPath = controllerPath;
	}

	public String getControllerPath() {
		return controllerPath;
	}

	@Produces
	@QPlcControllerName
	public String getControllerName() {
		if (this.controllerInfo != null) {
			return this.controllerInfo.matchPathName(this.controllerPath);
		}
		return null;
	}

	@Inject
	public void setEntityId(@PathParam(PATH_PARAM.RESOURCE_ID) String entityId) {
		this.entityId = entityId;
	}

	@Produces
	@QPlcEntityId
	public String getEntityId() {
		return entityId;
	}

	@Inject
	public void setConversorMediaType(MediaType mediaType, @QueryParam(QUERY_PARAM.OUTPUT) String output) {
		// Override de MediaType.
		if (StringUtils.isNotEmpty(output)) {
			if ("json".equalsIgnoreCase(output)) {
				conversorMediaType = MediaType.APPLICATION_JSON_TYPE;
			} else if ("atom".equalsIgnoreCase(output)) {
				conversorMediaType = MediaType.APPLICATION_ATOM_XML_TYPE;
			} else if ("xml".equalsIgnoreCase(output)) {
				conversorMediaType = MediaType.TEXT_XML_TYPE;
			} else if ("html".equalsIgnoreCase(output)) {
				conversorMediaType = MediaType.TEXT_HTML_TYPE;
			} else if ("text".equalsIgnoreCase(output)) {
				conversorMediaType = MediaType.TEXT_PLAIN_TYPE;
			} else {
				conversorMediaType = MediaType.valueOf(output);
			}
		}
		if (conversorMediaType == null) {
			conversorMediaType = mediaType;
		}
	}

	@Produces
	@QPlcCurrent
	public MediaType getConversorMediaType() {
		return conversorMediaType;
	}

	@Produces
	@QPlcControllerQualifier
	public String getControllerQualifier() {
		if (controllerInfo != null) {
			return this.controllerInfo.matchPathQualifier(this.controllerPath);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> T getControllerEntityId() {
		IPlcController<?, ?> controller = getController();
		if (controller != null) {
			Object entityId = getControllerTypedEntiryId(controller.getIdType());
			return (T) entityId;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> T getControllerTypedEntiryId(Class<?> type) {
		// FIXME: Tratar Chave Natural.
		Object id = null;
		if (String.class.isAssignableFrom(type)) {
			id = entityId;
		} else if (Object.class.equals(type) || Long.class.isAssignableFrom(type)) {
			if (StringUtils.isNotEmpty(entityId) && StringUtils.isNumeric(entityId)) {
				id = Long.parseLong(entityId);
			} else {
				id = null;
			}
		}
		return (T) id;
	}

	@Produces
	@QPlcCurrent
	@SuppressWarnings("rawtypes")
	public IPlcController getController() {
		if (controllerInfo != null) {
			if (controller == null) {
				controller = controllerInfo.createInstance();
			}
		}
		return controller;
	}

	@Produces
	@QPlcCurrent
	@SuppressWarnings("rawtypes")
	public IPlcConversor getConversor() {
		if (conversorInfo != null) {
			if (conversor == null) {
				conversor = conversorInfo.createInstance();
			}
		}
		return conversor;
	}
}
