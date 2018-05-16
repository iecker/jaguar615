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
import com.powerlogic.jcompany.persistence.jpa.entity.attach.EmployeeEntity;
import com.powerlogic.jcompany.persistence.jpa.entity.attach.ImageContentEntity;
import com.powerlogic.jcompany.persistence.jpa.entity.attach.ImageEntity;


@RunWith(Arquillian.class)
public class PlcFileAttachJPATestCase extends AbstractArquillianTestCase {

	private static final String CONTEUDO_ARQUIVO = new String("CONTEUDO DO ARQUIVO A SER GAVADO");
	
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
	public void inserirFuncionario() throws Exception
	{
		PlcCDIUtil.getInstance().setBeanManager(beanManager);

		PlcBaseDAO daoA= iocModelUtil.getPersistenceObject(ImageEntity.class);
		
		ImageEntity a = new ImageEntity();
		a.setNome("arquivo.txt");
		
		ImageContentEntity conteudo = new ImageContentEntity();
		conteudo.setBinaryContent(CONTEUDO_ARQUIVO.getBytes());
		a.setBinaryContent(conteudo);
		
		a.setLength(CONTEUDO_ARQUIVO.length());
		a.setType("text/plain");
		daoA.insert(baseContextVO, a);
		daoA.commit();
		Assert.assertNotNull(a.getId());
		
		PlcBaseDAO daoF = iocModelUtil.getPersistenceObject(EmployeeEntity.class);		
		EmployeeEntity f = new EmployeeEntity();
		f.setNome("Baldini");
		f.setArquivoAnexado(a);
		
		daoF.insert(baseContextVO, f);
		daoF.commit();
		Assert.assertNotNull(f.getId());
	}

	@Test
	public void verificarFuncionario() throws Exception
	{
		PlcBaseDAO dao = iocModelUtil.getPersistenceObject(EmployeeEntity.class);
		
		EmployeeEntity f = (EmployeeEntity)dao.findById(baseContextVO, EmployeeEntity.class, 1l);
		
		Assert.assertNotNull(f);
		Assert.assertEquals(f.getId().longValue(), 1l);
		Assert.assertEquals(f.getNome(), "Baldini");
		
		ImageEntity a = f.getArquivoAnexado();
		
		Assert.assertNotNull(a);
		Assert.assertEquals(a.getId().longValue(), 1l);
		Assert.assertEquals(a.getNome(), "arquivo.txt");
		Assert.assertEquals(a.getLength(), new Integer(CONTEUDO_ARQUIVO.length()));
		Assert.assertEquals(a.getType(), "text/plain");
		Assert.assertEquals(new String(a.getBinaryContent().getBinaryContent()), CONTEUDO_ARQUIVO);
		
	}	
	
	@Test
	public void excluirFuncionario() throws Exception
	{
		PlcBaseDAO dao = iocModelUtil.getPersistenceObject(EmployeeEntity.class);
		PlcBaseDAO daoF = iocModelUtil.getPersistenceObject(ImageEntity.class);
		
		EmployeeEntity f = (EmployeeEntity)dao.findById(baseContextVO, EmployeeEntity.class, 1l);
		
		dao.delete(baseContextVO, f);
		daoF.delete(baseContextVO, f.getArquivoAnexado());
		dao.commit();
		Assert.assertTrue(true);
	}

	@Test
	public void verificarFuncionarioExcluido() throws Exception
	{
		PlcBaseDAO dao = iocModelUtil.getPersistenceObject(EmployeeEntity.class);
		
		try {
			dao.findById(baseContextVO, EmployeeEntity.class, 1l);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertTrue(e.toString().contains("Não foi possível recuperar um registro"));
		}
		
		PlcBaseDAO daoA = iocModelUtil.getPersistenceObject(ImageEntity.class);
		
		try {
			daoA.findById(baseContextVO, ImageEntity.class, 1l);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertTrue(e.toString().contains("Não foi possível recuperar um registro"));
		}		
	}
	
}


