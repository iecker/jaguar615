package com.powerlogic.jcompany.controller.validation;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.myfaces.extensions.validator.beanval.MappedConstraintSourceBeanValidationModuleValidationInterceptor;

public class PlcMappedConstraintSourceBeanValidationModuleValidationInterceptor extends MappedConstraintSourceBeanValidationModuleValidationInterceptor{

	@Override
	protected void processValidation(FacesContext facesContext, UIComponent uiComponent, Object convertedObject) {
		if (!skipValidation())
			super.processValidation(facesContext, uiComponent, convertedObject);
	}	

	private boolean skipValidation() {
		HttpServletRequest request = ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()); 
		String skipValidation = request.getParameter("skipValidation");
		return Boolean.parseBoolean(skipValidation);
	}
	
	
}
