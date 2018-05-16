/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */
package com.powerlogic.jcompany.controller.util.facade;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.facade.IPlcFacade;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.AbstractArquillianTestCase;
import com.powerlogic.jcompany.controller.util.PlcIocControllerFacadeUtil;
import com.powerlogic.jcompanyqa.controller.mock.ServletContextMock;

@RunWith(Arquillian.class)
public class PlcIocControllerUtilTestCase extends AbstractArquillianTestCase {

	@Inject
	BeanManager beanManager;

	@Inject
	@QPlcDefault
	PlcIocControllerFacadeUtil iocControleFacadeUtil;

	@Test
	public void checkIocControllerUtil() {
		PlcCDIUtil.getInstance().setBeanManager(beanManager);

		HttpServletRequest request = (HttpServletRequest) PlcCDIUtil.getInstance().getInstanceByType(HttpServletRequest.class);
		((ServletContextMock) request.getSession().getServletContext()).setInitParameter(PlcConstants.CONTEXTPARAM.INI_MODO_EXECUCAO, "T");

		request.setAttribute(PlcConstants.PlcJsfConstantes.URL_SEM_BARRA, "testemdt");

		IPlcFacade facade = iocControleFacadeUtil.getFacade();
		Assert.assertNotNull(facade);

		IPlcFacade facade2 = iocControleFacadeUtil.getFacade(IPlcFacade.class);
		Assert.assertNotNull(facade2);

		IPlcExtendeFacade facade3 = iocControleFacadeUtil.getFacade(IPlcExtendeFacade.class);
		Assert.assertNotNull(facade3);

		IPlcSpecificFacade facade4 = iocControleFacadeUtil.getFacadeSpecific(IPlcSpecificFacade.class);
		Assert.assertNotNull(facade4);
	}
}
