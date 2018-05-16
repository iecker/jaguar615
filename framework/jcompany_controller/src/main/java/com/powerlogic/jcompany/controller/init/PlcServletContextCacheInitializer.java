package com.powerlogic.jcompany.controller.init;

import java.lang.reflect.Field;

import javax.inject.Inject;
import javax.servlet.ServletContext;


import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.init.IPlcServiceInitializer;
import com.powerlogic.jcompany.controller.appinfo.PlcAppInfoUtil;
import com.powerlogic.jcompany.controller.servlet.PlcServletContextProducer;

public class PlcServletContextCacheInitializer implements IPlcServiceInitializer {

	@Inject @QPlcDefault
	PlcServletContextProducer servletContextProducer;
	
	@Inject @QPlcDefault
	PlcAppInfoUtil appUtil;
	
	@Override
	public void init(ServletContext servletContext) throws Exception {
		
		servletContextProducer.setServletContext(servletContext);
		Field f = appUtil.getClass().getSuperclass().getDeclaredField("servletContext");
		f.setAccessible(true);
		f.set(appUtil, servletContext);
	}

	@Override
	public boolean isInitialized() {
		return servletContextProducer.getServletContext()!=null;
	}
	
}
