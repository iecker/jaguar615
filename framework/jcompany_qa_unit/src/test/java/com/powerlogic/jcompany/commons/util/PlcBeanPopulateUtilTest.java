/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.util;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.AbstractArquillianTestCase;
import com.powerlogic.jcompany.controller.jsf.test.masterdetail.EmployeeEntity;
import com.powerlogic.jcompany.controller.jsf.test.masterdetail.Sex;
import com.powerlogic.jcompany.controller.util.PlcBeanPopulateUtil;

@RunWith(Arquillian.class)
public class PlcBeanPopulateUtilTest extends AbstractArquillianTestCase {

	@Inject BeanManager beanManager;

	@Inject @QPlcDefault
	PlcBeanPopulateUtil beanPopulateUtil;

	private void preparaContexto() {
		PlcCDIUtil.getInstance().setBeanManager(beanManager);
	}

	@Test
	public void testTransferBeans() {

		preparaContexto();
		
		Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("temCursoSuperior", new String[]{"t"});
		mapa.put("id", new String[]{"1"});
		mapa.put("nome", new String[]{"Baldini"});
		mapa.put("sexo", new String[]{"M"});
		mapa.put("enderecoResidencial.uf", new String[]{"1"});

		EmployeeEntity destino = new EmployeeEntity();

		beanPopulateUtil.transferMapToBean(mapa, destino);

		Assert.assertTrue(destino.getTemCursoSuperior());
		Assert.assertEquals(destino.getId(), new Long(1));
		Assert.assertEquals(destino.getNome(), "Baldini");
		Assert.assertEquals(destino.getSexo(), Sex.M);
		Assert.assertEquals(destino.getEnderecoResidencial().getUf().getId(), new Long(1));


	}	

}
