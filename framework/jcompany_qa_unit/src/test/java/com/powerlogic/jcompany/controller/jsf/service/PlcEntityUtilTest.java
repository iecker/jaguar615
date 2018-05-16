/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.service;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.replay;

import java.util.HashMap;
import java.util.Map;

import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;

import org.apache.myfaces.trinidad.component.UIXComponent;
import org.junit.Test;

import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompanyqa.factory.PlcMockFactory;

/**
 * Classe de apoio aos testes da classe @link PlcEntityUtil
 * @author Pedro Henrique
 *
 */
public class PlcEntityUtilTest {

	/**
	 * Mocks utilizados no teste
	 */
	private ValueChangeEvent valueChangeEvent;
	private UIXComponent uiXComponent;
	private HttpServletRequest request;
	private PlcContextUtil contextUtil;		
	
	private final String ID = "12345";

	
	/**
	 * Testa a Auto Recuperação do componente Vinculado.
	 */
	@Test
	public void autofindAggregate() throws Exception {
		/*
		entityCommonsUtil.autoRecuperaVinculado(getValueChangeEvent());
		
		PropertyKey key = ((UIXComponent)valueChangeEvent.getComponent()).getFacesBean().getType().findKey(PlcJsfConstantes.PROPRIEDADES.LOOKUP_VALUE_KEY);
		String value	= (String)((UIXComponent)valueChangeEvent.getComponent()).getFacesBean().getProperty(key);

		assertEquals("O Valor correto para o componente vinculado deveria ser [12345]","[id="+ID+"]",value);
		*/
	}
	
	public ValueChangeEvent getValueChangeEvent() {
		valueChangeEvent = PlcMockFactory.getMock(ValueChangeEvent.class);
		expect(valueChangeEvent.getNewValue()).andReturn(ID).anyTimes();
		expect(valueChangeEvent.getComponent()).andReturn(getUiComponent()).anyTimes();
		expect(valueChangeEvent.getOldValue()).andReturn("1").anyTimes();
		replay(valueChangeEvent);
		return valueChangeEvent;
	}
 
	public UIXComponent getUiComponent() {
		
		uiXComponent	= PlcMockFactory.getMock(UIXComponent.class);
//		facesBeanMock	= PlcMockFactory.getInstanceClasseTeste(FacesBeanImplMock.class);

		Map<String, Object> attributes = new  HashMap<String, Object>();
		attributes.put(PlcJsfConstantes.PROPRIEDADES.AUTO_RECUPERACAO_PROPRIEDADE_KEY, "");
		attributes.put(PlcJsfConstantes.PROPRIEDADES.AUTO_RECUPERACAO_CLASSE_KEY, "com.powerlogic.jcompany.commons.PlcEntityInstance");
		attributes.put(PlcJsfConstantes.PROPRIEDADES.PROPS_CHAVE_NATURAL_PLC, new  HashMap<String, Object>());
		expect(uiXComponent.getAttributes()).andReturn(attributes).anyTimes();
//		expect(uiXComponent.getFacesBean()).andReturn(facesBeanMock).anyTimes();
		replay(uiXComponent);

		return uiXComponent;
	}

	public PlcContextUtil getPlcContextUtil() {
		contextUtil	= PlcMockFactory.getMock(PlcContextUtil.class);
		request				= PlcMockFactory.getMock(HttpServletRequest.class);
		
		expect(contextUtil.getRequest()).andReturn(request).anyTimes();
		replay(contextUtil);
		return contextUtil;
	}


	
}
