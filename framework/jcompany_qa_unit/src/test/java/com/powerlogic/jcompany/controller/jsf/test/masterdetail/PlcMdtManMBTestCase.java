/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */
package com.powerlogic.jcompany.controller.jsf.test.masterdetail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Conversation;
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
import com.powerlogic.jcompany.controller.jsf.mock.HttpServletRequestMock;
import com.powerlogic.jcompany.controller.jsf.producer.PlcComponentProducer;
import com.powerlogic.jcompany.controller.util.PlcMsgUtil;
import com.powerlogic.jcompany.domain.validation.PlcMessage;
import com.powerlogic.jcompanyqa.controller.mock.ServletContextMock;

@RunWith(Arquillian.class)
public class PlcMdtManMBTestCase extends AbstractArquillianTestCase {

	@Inject
	BeanManager beanManager;

	@Inject
	@Named(PlcConstants.PlcJsfConstantes.PLC_MB)
	Instance<PlcBaseMB> action;
	
	@Inject
	@Named("funcionario")
	Instance<Object> entidade;
	
	@Inject
	@Named("funcionarioLista")
	Instance<PlcEntityList> logicaItensPlc;
	
	@Inject
	@Named("plcComponentProducer")
	Instance<PlcComponentProducer> componentFactory;
	
	@Inject
	@QPlcDefault
	Instance<PlcMsgUtil> msgUtil;
	
	@Inject
	Conversation c;

	@Test
	public void gravaMDTFuncionario() {

		preparaContexto();

		gravaFuncionario();
	}

	@Test
	public void editaMDTFuncionario() {
		editaFuncionario();
	}

	@Test
	public void excluiMDTFuncionario() {
		excluiFuncionario();
	}

	private void preparaContexto() {
		PlcCDIUtil.getInstance().setBeanManager(beanManager);

		HttpServletRequest request = (HttpServletRequest) PlcCDIUtil.getInstance().getInstanceByType(HttpServletRequest.class);
		((ServletContextMock) request.getSession().getServletContext()).setInitParameter(PlcConstants.CONTEXTPARAM.INI_MODO_EXECUCAO, "T");

	}

	public void gravaFuncionario() {

		c.begin();

		HttpServletRequest request = (HttpServletRequest) PlcCDIUtil.getInstance().getInstanceByType(HttpServletRequest.class);
		request.setAttribute(PlcConstants.PlcJsfConstantes.URL_SEM_BARRA, "mestremdt");

		try {

			PlcBaseMB a = action.get();

			EmployeeEntity funcionario = (EmployeeEntity) entidade.get();
			funcionario.setNome("Rogerio Baldini");
			funcionario.setCpf("11111111111");
			funcionario.setDataNascimento(new GregorianCalendar(1978, 5, 20).getTime());

			Address e = new Address();
			e.setBairro("Bairro");
			e.setCep("11");
			e.setLogradouro("Lograd");
			e.setNumero("111");
			e.setUf(new UfEntity(1l, "Minas", "MG"));
			funcionario.setEnderecoResidencial(e);

			funcionario.setEstadoCivil(MaritalStatus.C);
			funcionario.setSexo(Sex.M);

			ProfissionalHistoryEntity hp = new ProfissionalHistoryEntity(null, "Teste HP");
			hp.setDataInicio(new Date());
			hp.setSalario(new BigDecimal(1001.00));
			hp.setFuncionario(funcionario);
			ArrayList<ProfissionalHistory> hps = new ArrayList<ProfissionalHistory>();
			hps.add(hp);
			funcionario.setHistoricoProfissional(hps);
			funcionario.setTemCursoSuperior(false);

			a.save();

			Map<String, List<PlcMessage>> mapaMsg = msgUtil.get().getMensagens();

			Assert.assertNull("Existem mensagens Vermelhas:" + mapaMsg.get(PlcMessage.Cor.msgVermelhoPlc.toString()), mapaMsg.get(PlcMessage.Cor.msgVermelhoPlc.toString()));
			Assert.assertNotNull("Não existem mensagens Azuis", mapaMsg.get(PlcMessage.Cor.msgAzulPlc.toString()));
			Assert.assertFalse("Mapa de Mensagens:" + mapaMsg, mapaMsg.isEmpty());

			c.end();

		} catch (PlcException e) {
			e.printStackTrace();
			Assert.fail();
		}

	}

	public void editaFuncionario() {
		try {
			c.begin();

			HttpServletRequest request = (HttpServletRequest) PlcCDIUtil.getInstance().getInstanceByType(HttpServletRequest.class);
			request.setAttribute(PlcConstants.PlcJsfConstantes.URL_SEM_BARRA, "mestremdt");
			((HttpServletRequestMock) request).setParameter("id", "1");

			PlcBaseMB a = action.get();
			getPlcControleRequisicao().setDetCorrPlc("historicoProfissional");

			a.edit();

			EmployeeEntity funcionario = (EmployeeEntity) entidade.get();
			Assert.assertNotNull(funcionario);
			Assert.assertNotNull(funcionario.getId());
			Assert.assertNull(funcionario.getHistoricoProfissional());

			a.findDetailOnDemand();
			Assert.assertNotNull(funcionario.getHistoricoProfissional());
			Assert.assertEquals(funcionario.getHistoricoProfissional().size(), 1);

			c.end();

		} catch (PlcException e) {
			e.printStackTrace();
			Assert.fail();
		}

	}

	public void excluiFuncionario() {
		try {
			c.begin();

			HttpServletRequest request = (HttpServletRequest) PlcCDIUtil.getInstance().getInstanceByType(HttpServletRequest.class);
			request.setAttribute(PlcConstants.PlcJsfConstantes.URL_SEM_BARRA, "mestremdt");
			((HttpServletRequestMock) request).setParameter("id", "1");

			PlcBaseMB a = action.get();
			getPlcControleRequisicao().setDetCorrPlc(null);

			EmployeeEntity funcionario = (EmployeeEntity) entidade.get();

			a.delete();

			Assert.assertNotNull(funcionario);
			Assert.assertNull(funcionario.getId());

			c.end();

		} catch (PlcException e) {
			e.printStackTrace();
			Assert.fail();
		}

	}

}
