/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.modelo.commons;

import static org.easymock.classextension.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.easymock.classextension.EasyMock;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.annotation.PlcDbFactory;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.util.PlcBaseContextUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregationPOJO;

@RunWith(Arquillian.class)
public class PlcBaseContextModelUtilTest extends AbstractArquillianTestCase {

	@Inject @QPlcDefault PlcBaseContextUtil	baseContextMontaUtil;

	// Inner classes para teste
	@PlcDbFactory(nome = "fabricaTeste")
	public class VOComPlcFabricaTeste {

	}

	@Test
	public void testCreateDbFactory()  {

		PlcBaseContextVO context = new PlcBaseContextVO();
		Class<?> valueObjectClass = VOComPlcFabricaTeste.class;
		PlcConfigAggregationPOJO configAcao = EasyMock.createStrictMock(PlcConfigAggregationPOJO.class);
		replay(configAcao);
		String[] siglaModulos = new String[] { "MOD1", "MOD2" };

		baseContextMontaUtil.createDbFactory(context, valueObjectClass, configAcao, siglaModulos);

		assertEquals("fabricaTeste", context.getDbFactory());

	}


}
