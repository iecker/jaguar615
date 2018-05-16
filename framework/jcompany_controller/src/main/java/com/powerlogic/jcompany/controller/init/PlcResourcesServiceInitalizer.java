package com.powerlogic.jcompany.controller.init;

import javax.servlet.ServletContext;


import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.init.IPlcServiceInitializer;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.appinfo.PlcAppInfoUtil;

public class PlcResourcesServiceInitalizer implements IPlcServiceInitializer {

	@Override
	public void init(ServletContext servletContext) throws Exception {
		
		PlcAppInfoUtil appInfoUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcAppInfoUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		// Registra os Recursos sem conflitar com o jSecurity.
		appInfoUtil.config(servletContext);
	}

	@Override
	public boolean isInitialized() {
		
		PlcAppInfoUtil appInfoUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcAppInfoUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		return appInfoUtil.getServletContext()!=null;
	}	
}
