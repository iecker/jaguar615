/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.persistence.jpa;


import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBaseUserProfileVO;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.model.util.PlcIocModelUtil;
import com.powerlogic.jcompany.modelo.commons.AbstractArquillianTestCase;
import com.powerlogic.jcompany.persistence.PlcBaseDAO;
import com.powerlogic.jcompany.persistence.jpa.entity.basic.UserEntity;


@RunWith(Arquillian.class)
public class PlcBasicJPATestCase extends AbstractArquillianTestCase{

	@Inject BeanManager beanManager;

	@Inject @QPlcDefault PlcIocModelUtil iocModelUtil;

	PlcBaseContextVO baseContextVO;

	/**
	 * Permite que se gere um context default incluido em Thread Local, 
	 * com usuário para lógicas de testes de persistencia
	 * que usam acesso aos dados.
	 * @since jCompany 3.04
	 */
	@Before 
	public void setContextDefault() {
		baseContextVO = new PlcBaseContextVO();
		PlcBaseUserProfileVO perfilVO = new PlcBaseUserProfileVO();
		perfilVO.setLogin("usuario_teste_unidade");
		baseContextVO.setUserProfile(perfilVO);	
		
	}
	
	@Test
	public void testaCRUDUsuario() throws Exception {

		inserirUsuario();
		verificarUsuario();
		alterarUsuario();
		verificarUsuarioAlterado();
		excluirUsuario();
		verificarUsuarioExcluido();
		
	}	


	public void inserirUsuario() throws Exception
	{

		PlcCDIUtil.getInstance().setBeanManager(beanManager);

		PlcBaseDAO dao = iocModelUtil.getPersistenceObject(UserEntity.class);

		UserEntity u = new UserEntity();
		u.setName("Baldini");

		dao.insert(baseContextVO, u);
		Assert.assertTrue(true);
	}


	public void verificarUsuario() throws Exception
	{

		PlcBaseDAO dao = iocModelUtil.getPersistenceObject(UserEntity.class);

		UserEntity u = (UserEntity)dao.findById(baseContextVO, UserEntity.class, 1l);

		Assert.assertNotNull(u);
		Assert.assertEquals(u.getId().longValue(), 1l);
		Assert.assertEquals(u.getName(), "Baldini");
	}


	public void alterarUsuario() throws Exception
	{

		PlcBaseDAO dao = iocModelUtil.getPersistenceObject(UserEntity.class);

		UserEntity u = (UserEntity)dao.findById(baseContextVO, UserEntity.class, 1l);

		u.setName("Rogerio Baldini");

		dao.update(baseContextVO, u);
		Assert.assertTrue(true);

	}


	public void verificarUsuarioAlterado() throws Exception
	{

		PlcBaseDAO dao = iocModelUtil.getPersistenceObject(UserEntity.class);

		UserEntity u = (UserEntity)dao.findById(baseContextVO, UserEntity.class, 1l);

		Assert.assertNotNull(u);
		Assert.assertEquals(u.getId().longValue(), 1l);
		Assert.assertEquals(u.getName(), "Rogerio Baldini");
	}


	public void excluirUsuario() throws Exception
	{

		PlcBaseDAO dao = iocModelUtil.getPersistenceObject(UserEntity.class);

		UserEntity u = (UserEntity)dao.findById(baseContextVO, UserEntity.class, 1l);

		dao.delete(baseContextVO, u);
		
		Assert.assertTrue(true);
	}


	public void verificarUsuarioExcluido() throws Exception
	{

		PlcBaseDAO dao = iocModelUtil.getPersistenceObject(UserEntity.class);

		try {
			dao.findById(baseContextVO, UserEntity.class, 1l);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertTrue(e.toString().contains("Não foi possível recuperar um registro"));
		}

	}
}


