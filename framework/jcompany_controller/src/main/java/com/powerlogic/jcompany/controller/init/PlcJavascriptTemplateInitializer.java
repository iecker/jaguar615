package com.powerlogic.jcompany.controller.init;

import javax.servlet.ServletContext;


import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.init.IPlcServiceInitializer;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.template.PlcRiaJavaScriptLocator;
import com.powerlogic.jcompany.controller.template.PlcRiaJavaScriptProcessor;
import com.powerlogic.jcompany.controller.template.PlcRiaJavaScriptUtil;

public class PlcJavascriptTemplateInitializer implements IPlcServiceInitializer {

	@Override
	public void init(ServletContext servletContext) throws Exception {
		
		PlcRiaJavaScriptUtil riaJavaScriptUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcRiaJavaScriptUtil.class, QPlcDefaultLiteral.INSTANCE);

		// Inicializa o Locator e o processor de Templates.
		PlcRiaJavaScriptLocator locator = PlcCDIUtil.getInstance().getInstanceByType(PlcRiaJavaScriptLocator.class, QPlcDefaultLiteral.INSTANCE);
		locator.setServletContext(servletContext);
		PlcRiaJavaScriptProcessor processor = PlcCDIUtil.getInstance().getInstanceByType(PlcRiaJavaScriptProcessor.class,QPlcDefaultLiteral.INSTANCE);
		// Injeta os Locator e Processor.
		riaJavaScriptUtil.setRiaJavaScriptLocator(locator);
		riaJavaScriptUtil.setRiaJavaScriptProcessor(processor);

	}
	
	@Override
	public boolean isInitialized() {
		
		PlcRiaJavaScriptUtil riaJavaScriptUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcRiaJavaScriptUtil.class, QPlcDefaultLiteral.INSTANCE);

		return riaJavaScriptUtil.getLocator()!=null;
	}	

}
