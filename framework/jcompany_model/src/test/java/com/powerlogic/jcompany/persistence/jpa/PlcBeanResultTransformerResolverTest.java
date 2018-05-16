/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.persistence.jpa;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.modelo.commons.AbstractArquillianTestCase;
import com.powerlogic.jcompany.persistence.jpa.entity.attach.Employee;
import com.powerlogic.jcompany.persistence.jpa.entity.attach.ImageEntity;

@RunWith(Arquillian.class)
public class PlcBeanResultTransformerResolverTest extends AbstractArquillianTestCase {
	@Inject
	BeanManager beanManager;

	@Test
	public void testResolveClassByProperty() {
		PlcCDIUtil.getInstance().setBeanManager(beanManager);
		PlcBeanResultTransformerResolver resolver = PlcCDIUtil.getInstance().getInstanceByType(PlcBeanResultTransformerResolver.class);
		ImageEntity actual = resolver.resolveObjetoPelaPropriedade(ImageEntity.class,	Employee.class, "foto");
		assertThat(actual, instanceOf(ImageEntity.class));
	}
}
