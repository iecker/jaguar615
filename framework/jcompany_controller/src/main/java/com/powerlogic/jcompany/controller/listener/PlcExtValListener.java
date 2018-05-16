/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 

package com.powerlogic.jcompany.controller.listener;

import org.apache.myfaces.extensions.validator.MappedConstraintSourcePropertyValidationModuleValidationInterceptor;
import org.apache.myfaces.extensions.validator.PropertyValidationModuleValidationInterceptor;
import org.apache.myfaces.extensions.validator.beanval.BeanValidationModuleValidationInterceptor;
import org.apache.myfaces.extensions.validator.beanval.MappedConstraintSourceBeanValidationModuleValidationInterceptor;
import org.apache.myfaces.extensions.validator.core.ExtValContext;
import org.apache.myfaces.extensions.validator.core.ExtValCoreConfiguration;
import org.apache.myfaces.extensions.validator.core.el.AbstractELHelperFactory;
import org.apache.myfaces.extensions.validator.core.factory.FactoryNames;
import org.apache.myfaces.extensions.validator.core.startup.AbstractStartupListener;
import org.apache.myfaces.extensions.validator.crossval.DefaultExtValCrossValidationModuleConfiguration;
import org.apache.myfaces.extensions.validator.trinidad.interceptor.TrinidadRendererInterceptor;

import com.powerlogic.jcompany.controller.jsf.util.PLcAbstractELHelperFactory;
import com.powerlogic.jcompany.controller.validation.PlcBeanValidationModuleValidationInterceptor;
import com.powerlogic.jcompany.controller.validation.PlcMappedConstraintSourceBeanValidationModuleValidationInterceptor;
import com.powerlogic.jcompany.controller.validation.PlcMappedConstraintSourcePropertyValidationModuleValidationInterceptor;
import com.powerlogic.jcompany.controller.validation.PlcPropertyValidationInterceptor;
import com.powerlogic.jcompany.controller.validation.PlcPropertyValidationModuleValidationInterceptor;
import com.powerlogic.jcompany.controller.validation.PlcValidationExceptionInterceptor;

/**
 * Listener do MyFaces ExtVal
 * Estamos registrando os componentes e interceptors específicos nesse ponto, para correção de Bugs e ajustando o funcionamento com projetos Jaguar.
 * 
 * @author Igor Guimarães
 */

public class PlcExtValListener extends AbstractStartupListener {

	@Override
	protected void init() {
		
		// Registrando o ElHelper do ExtVal corrigindo o bug do Weld.
		ExtValContext.getContext().getFactoryFinder().getFactory(FactoryNames.EL_HELPER_FACTORY, AbstractELHelperFactory.class).setCustomELHelperFactory(new PLcAbstractELHelperFactory());

		// Registrando o interceptor para adicionar um Label as mensagem de erro.
		ExtValContext.getContext().addValidationExceptionInterceptor(new PlcValidationExceptionInterceptor());
		
		//Interceptor responsável por ajustar a mensagem de validação, adicionando o numero da linha.
		ExtValContext.getContext().addPropertyValidationInterceptor(new PlcPropertyValidationInterceptor());
		
		
		ExtValContext.getContext().deregisterRendererInterceptor(MappedConstraintSourceBeanValidationModuleValidationInterceptor.class);
		ExtValContext.getContext().deregisterRendererInterceptor(PropertyValidationModuleValidationInterceptor.class);
		ExtValContext.getContext().deregisterRendererInterceptor(TrinidadRendererInterceptor.class);
		ExtValContext.getContext().deregisterRendererInterceptor(BeanValidationModuleValidationInterceptor.class);
		ExtValContext.getContext().deregisterRendererInterceptor(MappedConstraintSourcePropertyValidationModuleValidationInterceptor.class);

		ExtValContext.getContext().denyRendererInterceptor(MappedConstraintSourceBeanValidationModuleValidationInterceptor.class);
		ExtValContext.getContext().denyRendererInterceptor(PropertyValidationModuleValidationInterceptor.class);
		ExtValContext.getContext().denyRendererInterceptor(BeanValidationModuleValidationInterceptor.class);
		ExtValContext.getContext().denyRendererInterceptor(MappedConstraintSourcePropertyValidationModuleValidationInterceptor.class);
		
		
		ExtValContext.getContext().registerRendererInterceptor(new PlcMappedConstraintSourceBeanValidationModuleValidationInterceptor());
		ExtValContext.getContext().registerRendererInterceptor(new PlcPropertyValidationModuleValidationInterceptor());
		ExtValContext.getContext().registerRendererInterceptor(new TrinidadRendererInterceptor());
		ExtValContext.getContext().registerRendererInterceptor(new PlcBeanValidationModuleValidationInterceptor());
		ExtValContext.getContext().registerRendererInterceptor(new PlcMappedConstraintSourcePropertyValidationModuleValidationInterceptor());

		//ExtValContext.getContext().getModuleConfiguration(ExtValCoreConfiguration.class);
		ExtValContext.getContext().addModuleConfiguration(ExtValCoreConfiguration.class, new PlcExtValCoreConfiguration(), true);
		
	}
	
}