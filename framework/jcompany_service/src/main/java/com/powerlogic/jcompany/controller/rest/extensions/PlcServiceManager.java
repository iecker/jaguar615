package com.powerlogic.jcompany.controller.rest.extensions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.enterprise.inject.spi.Bean;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.controller.rest.api.controller.IPlcController;
import com.powerlogic.jcompany.controller.rest.api.conversor.IPlcConversor;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcController;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcConversor;

@Singleton
public class PlcServiceManager {

	@Inject
	protected transient Logger log;

	public static final String DEFAULT_MEDIA_TYPE = "*/*";

	private Map<String, PlcControllerInfo> controllerMap = new HashMap<String, PlcControllerInfo>();

	private Map<Class<?>, List<PlcConversorInfo>> conversorMap = new HashMap<Class<?>, List<PlcConversorInfo>>();

	public PlcControllerInfo findControllerInfo(String path) {

		PlcControllerInfo controller = null;

		if (path != null) {
			// procura pelo path original.
			controller = controllerMap.get(path);
			// Procura pelas URIs com Wildcards.
			if (controller == null && StringUtils.isNotEmpty(path)) {

				// quebra o path em segmentos, e testa [a.b.c, a.b.*, a.*, *]
				String[] ps = StringUtils.split(path, ".");

				if (ps.length > 1) {
					// flexibiliza o qualificador.
					for (int i = ps.length - 1; i >= 0; i--) {
						// adiciona o wildcard de tras para frente até
						// encontrar!
						String wildcard = StringUtils.join(ps, ".", 0, i).concat(".*");
						// caso encontre retorna direto.
						controller = controllerMap.get(wildcard);
						if (controller != null) {
							return controller;
						}
					}
				} else {
					// ControllerName(path).ControllerQualifier(*)
					controller = controllerMap.get(path.concat(".*"));
				}
				// ControllerName(*).ControllerQualifier(*)
				if (controller == null) {
					controller = controllerMap.get("*.*");
				}
				// ControllerName(*)
				if (controller == null) {
					controller = controllerMap.get("*");
				}
				return controller;
			}
		}
		return controller;
	}

	public PlcConversorInfo findConversorInfo(PlcControllerInfo controllerInfo, MediaType mediaType) {
		// Procura por un conversor para o tipo do Bean.
		Class<?> type = controllerInfo.getBean().getBeanClass();
		// procaura por todos os ancestrais, até encontrar.
		while (type != null && !Object.class.equals(type)) {
			// Procura por um conversor para o tipo especifica.
			PlcConversorInfo conversor = findConversorForMediaType(conversorMap.get(type), mediaType);
			// caso não tenha encontrado, procura para o ancestral.
			if (conversor == null) {
				type = type.getSuperclass();
			} else {
				return conversor;
			}
		}
		// Procura pelo Conversor Default para o Tipo.
		return findConversorForMediaType(conversorMap.get(IPlcController.class), mediaType);
	}

	protected PlcConversorInfo findConversorForMediaType(List<PlcConversorInfo> conversors, MediaType mt) {
		// Procura por um conversor que atenda o MediaType.
		if (conversors != null && !conversors.isEmpty()) {
			for (PlcConversorInfo conversor : conversors) {
				for (String cmt : conversor.getMediaTypes()) {
					if (mt.toString().equalsIgnoreCase(cmt)) {
						return conversor;
					}
				}
			}
		}
		return null;
	}

	protected void addController(Bean<?> bean) {

		PlcControllerInfo controller = new PlcControllerInfo(bean);

		PlcControllerInfo duplicated = controllerMap.get(controller.getPath());

		if (duplicated == null) {
			controllerMap.put(controller.getPath(), controller);
		} else {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_AMBIGUOUS_CONTROLLER, new Object[] { duplicated.getBean().getBeanClass(), bean.getBeanClass() }, true);
		}

		if (log != null && log.isInfoEnabled()) {
			log.info(String.format("Adding @%s -> \"%s\" URI: \"../%s\"", SPlcController.class.getSimpleName(), bean.getBeanClass().getSimpleName(), controller.getPath()));
		}
	}

	protected void addConversor(Bean<?> bean) {

		PlcConversorInfo conversor = new PlcConversorInfo(bean);

		List<PlcConversorInfo> conversors = conversorMap.get(conversor.getType());

		if (conversors == null) {
			conversorMap.put(conversor.getType(), conversors = new LinkedList<PlcConversorInfo>());
		}

		for (PlcConversorInfo current : conversors) {
			for (String mt : conversor.getMediaTypes()) {
				if (!MediaType.WILDCARD.equals(mt)) {
					for (String cmt : current.getMediaTypes()) {
						if (mt.equals(cmt)) {
							throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_AMBIGUOUS_CONVERSOR, new Object[] { current.getBean().getBeanClass(), bean.getBeanClass() }, true);
						}
					}
				}
			}
		}

		conversors.add(conversor);

		if (log != null && log.isInfoEnabled()) {
			log.info(String.format("Adding @%s -> \"%s\" of controllers: \"%s\" for mediaTypes: %s", SPlcConversor.class.getSimpleName(), conversor.getBean().getBeanClass().getSimpleName(), conversor.getType().getSimpleName(), ArrayUtils.toString(conversor.getMediaTypes())));
		}
	}

	public SortedMap<String, PlcControllerInfo> getAllControllerInfo() {
		SortedMap<String, PlcControllerInfo> sortedMap = new TreeMap<String, PlcControllerInfo>();
		sortedMap.putAll(controllerMap);
		return sortedMap;
	}

	public boolean isController(Class<?> type) {
		return IPlcController.class.isAssignableFrom(type);
	}

	public boolean isConversor(Class<?> type) {
		return IPlcConversor.class.isAssignableFrom(type);
	}
}
