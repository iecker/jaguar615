package com.powerlogic.jcompany.service.init;

import java.lang.reflect.Method;
import java.util.Set;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;
import javax.servlet.ServletContext;


import com.powerlogic.jcompany.commons.config.qualifiers.QPlcJCompanyService;
import com.powerlogic.jcompany.commons.init.IPlcServiceInitializer;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.rest.api.controller.IPlcController;
import com.powerlogic.jcompany.controller.rest.api.conversor.IPlcConversor;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcController;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcConversor;
import com.powerlogic.jcompany.controller.rest.extensions.PlcServiceManager;

/**
 * Inicializador do jCompany Service
 * @author bruno.carneiro
 *
 */
@QPlcJCompanyService
public class PlcServiceInitializer implements IPlcServiceInitializer {

	@Override
	public void init(ServletContext servletContext) throws Exception {
		
		final AnnotationLiteral<Any> anyQualifier = new AnnotationLiteral<Any>() {
			private static final long serialVersionUID = 1L;
		};

		PlcServiceManager serviceManager =  PlcCDIUtil.getInstance().getInstanceByType(PlcServiceManager.class);
		Set<Bean<?>> allControllers = PlcCDIUtil.getInstance().getBeanManager().getBeans(IPlcController.class, anyQualifier);

		for (Bean<?> bean : allControllers) {
			if (bean.getBeanClass().isAnnotationPresent(SPlcController.class)) {
				Method m = serviceManager.getClass().getDeclaredMethod("addController", Bean.class);
				m.setAccessible(true);
				m.invoke(serviceManager, bean);
			}
		}

		Set<Bean<?>> allConversors =  PlcCDIUtil.getInstance().getBeanManager().getBeans(IPlcConversor.class, anyQualifier);

		for (Bean<?> bean : allConversors) {
			if (bean.getBeanClass().isAnnotationPresent(SPlcConversor.class)) {
				Method m = serviceManager.getClass().getDeclaredMethod("addConversor", Bean.class);
				m.setAccessible(true);
				m.invoke(serviceManager, bean);
			}
		}

	}

	@Override
	public boolean isInitialized() {
		
		PlcServiceManager serviceManager =  PlcCDIUtil.getInstance().getInstanceByType(PlcServiceManager.class);
		return serviceManager.getAllControllerInfo()!=null && serviceManager.getAllControllerInfo().keySet().size()>0;
	}
	
}
