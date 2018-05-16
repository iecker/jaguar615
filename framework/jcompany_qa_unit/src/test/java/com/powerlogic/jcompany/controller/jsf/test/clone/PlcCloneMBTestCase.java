/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */
package com.powerlogic.jcompany.controller.jsf.test.clone;

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
import com.powerlogic.jcompany.controller.jsf.mock.HttpServletRequestMock;
import com.powerlogic.jcompany.controller.jsf.test.masterdetail.EmployeeEntity;
import com.powerlogic.jcompany.controller.util.PlcMsgUtil;
import com.powerlogic.jcompany.domain.validation.PlcMessage;
import com.powerlogic.jcompanyqa.controller.mock.ServletContextMock;

@RunWith(Arquillian.class)
public class PlcCloneMBTestCase extends AbstractArquillianTestCase {

	@Inject
	BeanManager beanManager;

	@Inject
	@Named(PlcConstants.PlcJsfConstantes.PLC_MB)
	Instance<PlcBaseMB> action;

	@Inject
	@Named("funcionario")
	Instance<Object> entidade;

	@Inject
	@QPlcDefault
	PlcMsgUtil msgUtil;

	@Test
	public void testaMDTFuncionario() {

		preparaContexto();

		clonaUsuario();

	}

	private void preparaContexto() {
		PlcCDIUtil.getInstance().setBeanManager(beanManager);

		HttpServletRequest request = (HttpServletRequest) PlcCDIUtil.getInstance().getInstanceByType(HttpServletRequest.class);
		((ServletContextMock) request.getSession().getServletContext()).setInitParameter(PlcConstants.CONTEXTPARAM.INI_MODO_EXECUCAO, "T");

	}

	public void clonaUsuario() {
		try {
			HttpServletRequest request = (HttpServletRequest) PlcCDIUtil.getInstance().getInstanceByType(HttpServletRequest.class);
			request.setAttribute(PlcConstants.PlcJsfConstantes.URL_SEM_BARRA, "mestremdt");
			((HttpServletRequestMock) request).setParameter("id", "1");

			PlcBaseMB a = action.get();

			a.edit();

			EmployeeEntity funcionario = (EmployeeEntity) a.getEntityPlc();
			Assert.assertNotNull(funcionario);
			Assert.assertNotNull(funcionario.getId());
			Assert.assertEquals(funcionario.getId().longValue(), 1L);

			getPlcControleRequisicao().setDetCorrPlc("historicoProfissional");
			a.findDetailOnDemand();

			a.cloneEntity();

			Map<String, List<PlcMessage>> mapaMsg = msgUtil.getMensagens();
			Assert.assertNull("Existem mensagens Vermelhas:" + mapaMsg.get(PlcMessage.Cor.msgVermelhoPlc.toString()), mapaMsg.get(PlcMessage.Cor.msgVermelhoPlc.toString()));
			Assert.assertNotNull("Não existem mensagens Azuis", mapaMsg.get(PlcMessage.Cor.msgAzulPlc.toString()));
			Assert.assertFalse("Mapa de Mensagens:" + mapaMsg, mapaMsg.isEmpty());

			msgUtil.getMensagens().clear();

			funcionario.setCpf("33333333333");
			a.save();

			mapaMsg = msgUtil.getMensagens();
			Assert.assertNull("Existem mensagens Vermelhas:" + mapaMsg.get(PlcMessage.Cor.msgVermelhoPlc.toString()), mapaMsg.get(PlcMessage.Cor.msgVermelhoPlc.toString()));
			Assert.assertNotNull("Não existem mensagens Azuis", mapaMsg.get(PlcMessage.Cor.msgAzulPlc.toString()));
			Assert.assertFalse("Mapa de Mensagens:" + mapaMsg, mapaMsg.isEmpty());

			Assert.assertNotNull(funcionario);
			Assert.assertEquals(funcionario.getId().longValue(), 2l);

		} catch (PlcException e) {
			e.printStackTrace();
			Assert.fail();
		}

	}
}
