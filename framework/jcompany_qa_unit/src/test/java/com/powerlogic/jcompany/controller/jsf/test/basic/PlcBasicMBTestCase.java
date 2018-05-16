/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.test.basic;

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
import com.powerlogic.jcompany.controller.jsf.mock.HttpServletRequestMock;
import com.powerlogic.jcompany.controller.jsf.producer.PlcComponentProducer;
import com.powerlogic.jcompanyqa.controller.mock.ServletContextMock;

@RunWith(Arquillian.class)
public class PlcBasicMBTestCase extends AbstractArquillianTestCase {

	@Inject BeanManager beanManager;

	@Inject @Named(PlcConstants.PlcJsfConstantes.PLC_MB) Instance<PlcBaseMB> action; 
	@Inject @Named("usuario") Instance<Object> entidade;
	@Inject @Named("plcComponentProducer") Instance<PlcComponentProducer> componentFactory;

	@Test
	public void gravaUsuario() {		
		PlcCDIUtil.getInstance().setBeanManager(beanManager);

		HttpServletRequest request = (HttpServletRequest)PlcCDIUtil.getInstance().getInstanceByType(HttpServletRequest.class);
		((ServletContextMock)request.getSession().getServletContext()).setInitParameter(PlcConstants.CONTEXTPARAM.INI_MODO_EXECUCAO, "T");

		request.setAttribute(PlcConstants.PlcJsfConstantes.URL_SEM_BARRA, "testeman");

		PlcBaseMB baseJsfAction = action.get();

		UserEntity usuario = (UserEntity)entidade.get();
		usuario.setName("Baldini");
		
		baseJsfAction.save();
		Assert.assertNotNull(usuario.getId());	
	}

	@Test
	public void pesquisaUsuario() {				
		

		HttpServletRequest request = (HttpServletRequest)PlcCDIUtil.getInstance().getInstanceByType(HttpServletRequest.class);
		request.setAttribute(PlcConstants.PlcJsfConstantes.URL_SEM_BARRA, "testeman");
		((HttpServletRequestMock)request).setParameter("id", "1");

		PlcBaseMB a = action.get();

		a.edit();

		UserEntity usuario = (UserEntity)entidade.get();
		Assert.assertNotNull("Usuário não pode ser nulo",usuario);
	}	
}
