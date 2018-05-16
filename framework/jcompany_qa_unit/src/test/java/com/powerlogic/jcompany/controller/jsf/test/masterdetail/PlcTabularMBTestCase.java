/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */
package com.powerlogic.jcompany.controller.jsf.test.masterdetail;

import java.util.List;
import java.util.Map;

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
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.AbstractArquillianTestCase;
import com.powerlogic.jcompany.controller.jsf.PlcBaseMB;
import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompany.controller.util.PlcMsgUtil;
import com.powerlogic.jcompany.domain.validation.PlcMessage;
import com.powerlogic.jcompanyqa.controller.mock.ServletContextMock;

@RunWith(Arquillian.class)
public class PlcTabularMBTestCase extends AbstractArquillianTestCase {

	@Inject
	BeanManager beanManager;

	@Inject
	@Named(PlcConstants.PlcJsfConstantes.PLC_MB)
	Instance<PlcBaseMB> action;

	@Inject
	@Named("ufLista")
	Instance<PlcEntityList> entityListPlc;

	@Inject
	@QPlcDefault
	PlcMsgUtil msgUtil;

	@Test
	public void testaUf() {

		preparaContexto();

		gravaUf();

		excluiUf();

	}

	private void preparaContexto() {
		PlcCDIUtil.getInstance().setBeanManager(beanManager);

		HttpServletRequest request = (HttpServletRequest) PlcCDIUtil.getInstance().getInstanceByType(HttpServletRequest.class);

		((ServletContextMock) request.getSession().getServletContext()).setInitParameter(PlcConstants.CONTEXTPARAM.INI_MODO_EXECUCAO, "T");

	}

	public void gravaUf() {

		HttpServletRequest request = PlcCDIUtil.getInstance().getInstanceByType(HttpServletRequest.class);
		request.setAttribute(PlcConstants.PlcJsfConstantes.URL_SEM_BARRA, "uftab");

		try {
			PlcBaseMB a = action.get();

			UfEntity uf = new UfEntity();
			uf.setNome("Minas");
			uf.setSigla("MG");

			entityListPlc.get().getItensPlc().add(uf);

			a.save();

			Map<String, List<PlcMessage>> mapaMsg = msgUtil.getMensagens();

			Assert.assertNotNull("Não existem mensagens Azuis", mapaMsg.get(PlcMessage.Cor.msgAzulPlc.toString()));
			Assert.assertFalse("Mapa de Mensagens:" + mapaMsg, mapaMsg.isEmpty());

			Assert.assertEquals(entityListPlc.get().getItensPlc().size(), 2);

		} catch (PlcException e) {
			e.printStackTrace();
			Assert.fail();
		}

	}

	public void excluiUf() {

		HttpServletRequest request = (HttpServletRequest) PlcCDIUtil.getInstance().getInstanceByType(HttpServletRequest.class);
		request.setAttribute(PlcConstants.PlcJsfConstantes.URL_SEM_BARRA, "uftab");

		try {
			PlcBaseMB a = action.get();

			PlcEntityList itens = entityListPlc.get();

			UfEntity uf = (UfEntity) itens.getItensPlc().get(1);
			uf.setIndExcPlc("S");

			a.save();

			Map<String, List<PlcMessage>> mapaMsg = msgUtil.getMensagens();

			Assert.assertNotNull("Não existem mensagens Azuis", mapaMsg.get(PlcMessage.Cor.msgAzulPlc.toString()));
			Assert.assertFalse("Mapa de Mensagens:" + mapaMsg, mapaMsg.isEmpty());

			Assert.assertEquals(entityListPlc.get().getItensPlc().size(), 1);

		} catch (PlcException e) {
			e.printStackTrace();
			Assert.fail();
		}

	}
}
