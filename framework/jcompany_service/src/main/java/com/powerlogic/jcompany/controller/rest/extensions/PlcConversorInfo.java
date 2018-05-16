package com.powerlogic.jcompany.controller.rest.extensions;

import javax.enterprise.inject.spi.Bean;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.rest.api.controller.IPlcController;
import com.powerlogic.jcompany.controller.rest.api.conversor.IPlcConversor;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcConversorMediaType;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcConversorToController;

public class PlcConversorInfo {

	private Bean<?> bean;

	private Class<?> type;

	private String[] mediaTypes;

	private String defaultMediaType;

	@SuppressWarnings("unchecked")
	public PlcConversorInfo(Bean<?> bean) {

		//List<Class<?>> type = new PlcReflectionUtil().getDeclaredGenericTypes((Class<? extends IPlcConversor<?>>) bean.getBeanClass(), IPlcConversor.class);

		QPlcConversorToController toController = PlcServiceExtension.findQualifier(bean.getQualifiers(), QPlcConversorToController.class);
		
		QPlcConversorMediaType mediaTypes = PlcServiceExtension.findQualifier(bean.getQualifiers(), QPlcConversorMediaType.class);

		this.bean = bean;
		
		this.type = (toController != null && toController.type()!=null ? toController.type() : IPlcController.class);
		
		this.mediaTypes = mediaTypes != null ? mediaTypes.value() : new String[] { MediaType.WILDCARD };
		
		for (String mediaType : this.mediaTypes) {
			if (!MediaType.WILDCARD.equals(mediaType)) {
				this.defaultMediaType = mediaType;
				break;
			}
		}
		
		this.defaultMediaType = StringUtils.defaultIfEmpty( defaultMediaType, MediaType.TEXT_PLAIN );
	}

	public Class<?> getType() {
		return type;
	}

	public String[] getMediaTypes() {
		return mediaTypes;
	}

	public String getDefaultMediaType() {
		return defaultMediaType;
	}

	Bean<?> getBean() {
		return bean;
	}

	@SuppressWarnings("unchecked")
	public IPlcConversor<IPlcController<?, ?>> createInstance() {
		return (IPlcConversor<IPlcController<?, ?>>) PlcCDIUtil.getInstance().getReference(getBean());
	}
}