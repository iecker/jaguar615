package com.powerlogic.jcompany.controller.validation;

import java.util.Map;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.myfaces.extensions.validator.PropertyValidationModuleValidationInterceptor;
import org.apache.myfaces.extensions.validator.util.ExtValUtils;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;

public class PlcPropertyValidationModuleValidationInterceptor extends PropertyValidationModuleValidationInterceptor {

	private PlcURLUtil urlUtil;

	private PlcConfigUtil configUtil;

	public PlcPropertyValidationModuleValidationInterceptor() {
		
		this.urlUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcURLUtil.class, QPlcDefaultLiteral.INSTANCE);
		this.configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);
		
	}
	
	
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
	
	@Override
	protected void initComponent(FacesContext facesContext, UIComponent uiComponent)     {
		
        logger.finest("start to init component " + uiComponent.getClass().getName());

        Map<String, Object> metaDataResult = getTransformedMetaDataFor(facesContext, uiComponent);

        //get component initializer for the current component and configure it
        //also in case of skipped validation to reset e.g. the required attribute
        if(!metaDataResult.isEmpty())
        {
    		String url = urlUtil.resolveCollaborationNameFromUrl(((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()));
    		FormPattern pattern = configUtil.getConfigAggregation(url).pattern().formPattern();

   			if (FormPattern.Sel.equals(pattern)) {
   				metaDataResult.remove("required");
   			}

            ExtValUtils.configureComponentWithMetaData(facesContext, uiComponent, metaDataResult);
        }

        logger.finest("init component of " + uiComponent.getClass().getName() + " finished");
    }
}
