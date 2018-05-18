package com.empresa.rhavancado.model;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.empresa.rhavancado.commons.AppAbstractArquillianTestCase;
import com.empresa.rhavancado.entity.FuncionarioEntity;
import com.empresa.rhavancado.persistence.jpa.AppJpaDAO;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBaseUserProfileVO;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.model.PlcBaseRepository;

@RunWith(Arquillian.class)
public class FuncionarioTestCase extends AppAbstractArquillianTestCase {

	@Inject
	private BeanManager beanManager;

	@Inject
	@QPlcDefault
	private AppJpaDAO appJpaDAO;

	@Inject
	@QPlcDefault
	private PlcBaseRepository repository;

	private PlcBaseContextVO context;

	@Before
	public void setContextDefault() {
		context = new PlcBaseContextVO();
		PlcBaseUserProfileVO perfilVO = new PlcBaseUserProfileVO();
		perfilVO.setLogin("usuario_teste_unidade");
		context.setUserProfile(perfilVO);
		context.setExecutionMode("T");
	}

	@Test
	public void testFuncionarioEditarCodigoCargo() throws Exception {

		PlcCDIUtil.getInstance().setBeanManager(beanManager);

		try {
			FuncionarioEntity funcionario = (FuncionarioEntity) appJpaDAO
					.findById(context, FuncionarioEntity.class, new Long(1));

			funcionario.setCodigoCargo("1234");
			repository.update(context, funcionario);

			// Assert.assertNull( );
			// Assert.assertNotNull( );
			// Assert.assertEquals(-1000d, -1000d, 0d);

		} catch (Exception e) {
			Assert.fail();
		}
	}

	//@Test
	public void testCodigoDuplicado() throws Exception {

		PlcCDIUtil.getInstance().setBeanManager(beanManager);

		FuncionarioEntity funcionario = new FuncionarioEntity();

		funcionario.setNome("Sheldon Cooper");

		repository.update(context, funcionario);

		try {

		} catch (Exception e) {
			Assert.fail();
		}
	}

}
