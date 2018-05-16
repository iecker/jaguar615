package com.powerlogic.jcompany.controller.jsf.producer;

import java.util.Locale;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Specializes;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.util.cdi.PlcLocaleProducer;

/**
 * Produz o Locale da requisicao. Se nao estiver em uma requisicao, produz o
 * default do Faces.
 */
@Specializes
public class PlcJsfLocaleProducer extends PlcLocaleProducer {

	@Produces
	@QPlcDefault
	@Specializes
	@Override
	public Locale getLocale() {
		FacesContext currentInstance = FacesContext.getCurrentInstance();
		if (currentInstance != null) {
			UIViewRoot viewRoot = currentInstance.getViewRoot();
			if (viewRoot != null) {
				return viewRoot.getLocale();
			} else {
				return currentInstance.getApplication().getDefaultLocale();
			}
		}
		return Locale.getDefault();
	}
}
