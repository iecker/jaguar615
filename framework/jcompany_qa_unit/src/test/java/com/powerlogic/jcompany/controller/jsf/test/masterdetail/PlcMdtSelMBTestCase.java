/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.test.masterdetail;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.AbstractArquillianTestCase;
import com.powerlogic.jcompany.controller.jsf.PlcBaseMB;
import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompanyqa.controller.mock.ServletContextMock;

@RunWith(Arquillian.class)
public class PlcMdtSelMBTestCase extends AbstractArquillianTestCase {

	@Inject
	BeanManager beanManager;

	@Inject @Named(PlcConstants.PlcJsfConstantes.PLC_MB)	Instance<PlcBaseMB> action;
	
	@Inject	@Named("funcionarioLista")	Instance<PlcEntityList> logicaItensPlc;
	

	@Test
	public void testaMDTFuncionario()  {

		preparaContexto();

		pesquisaFuncionario();

	}

	private void preparaContexto() {

		PlcCDIUtil.getInstance().setBeanManager(beanManager);

		HttpServletRequest request = (HttpServletRequest) PlcCDIUtil.getInstance().getInstanceByType(HttpServletRequest.class);
		((ServletContextMock) request.getSession().getServletContext()).setInitParameter(PlcConstants.CONTEXTPARAM.INI_MODO_EXECUCAO,"T");
	}

	public void pesquisaFuncionario()  {
		HttpServletRequest request = (HttpServletRequest) PlcCDIUtil.getInstance().getInstanceByType(HttpServletRequest.class);
		request.setAttribute(PlcConstants.PlcJsfConstantes.URL_SEM_BARRA,"mestresel");

		PlcBaseMB a = action.get();

		PlcEntityList itens = logicaItensPlc.get();

		a.search();

		itens = logicaItensPlc.get();

		Assert.assertNotNull(itens);
		Assert.assertNotNull(itens.getItensPlc());
		Assert.assertEquals(1, itens.getItensPlc().size());
	}

}
