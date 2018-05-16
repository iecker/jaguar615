/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.persistence.jpa;


import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.util.List;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.transform.ResultTransformer;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.model.util.PlcIocModelUtil;
import com.powerlogic.jcompany.modelo.commons.AbstractArquillianTestCase;
import com.powerlogic.jcompany.persistence.jpa.entity.attach.EmployeeEntity;
import com.powerlogic.jcompany.persistence.util.jpa.PlcJpaUtil;

/**
 * The class <code>PlcBeanResultTransformerTest</code> contains tests for the
 * class {@link <code>PlcBeanResultTransformer</code>}
 * 
 * @pattern JUnit Test Case
 * 
 * @author George Gastaldi
 * 
 * @version $Revision$
 */
@RunWith(Arquillian.class)
public class PlcBeanResultTransformerTest extends AbstractArquillianTestCase {
	
	@Inject
	BeanManager beanManager;

	@Inject
	@QPlcDefault
	PlcJpaUtil jpaUtil;

	@Inject
	@QPlcDefault
	PlcIocModelUtil iocModelUtil;


	@SuppressWarnings("unchecked")
	@Test
	public void pesquisarSomenteNomeUsuario() throws Exception {
		PlcCDIUtil.getInstance().setBeanManager(beanManager);
		PlcBaseContextVO context = new PlcBaseContextVO();
		context.setExecutionMode("T");
		PlcBaseJpaManager gerenciadorFabrica = (PlcBaseJpaManager) iocModelUtil.getFactoryManager("default");
		EntityManager entityManager = gerenciadorFabrica.getEntityManager(context);
		Query query = entityManager.createQuery("select nome as nome from EmployeeEntity");
		
		ResultTransformer transformer = PlcBeanResultTransformer.aliasToBean(EmployeeEntity.class);
		jpaUtil.applyTransformer(query, transformer);
		List<Object> lista = query.getResultList();
		assertFalse("Lista está vazia",lista.isEmpty());
		assertTrue("Transformer não objeto Funcionario",lista.get(0) instanceof EmployeeEntity);

		// Note o null (Não pode trazer o ID)
		EmployeeEntity expected = new EmployeeEntity(null, "George");
		EmployeeEntity actual = (EmployeeEntity) lista.get(0);
		assertEquals("Objetos diferentes",expected, actual);
	}

}