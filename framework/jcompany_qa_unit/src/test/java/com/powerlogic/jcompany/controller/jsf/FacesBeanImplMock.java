/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.replay;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.el.ValueExpression;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.PropertyKey;

import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompanyqa.factory.PlcMockFactory;

public class FacesBeanImplMock implements FacesBean {

	
	private Type facesBeanType;
	private PropertyKey propertyKey;
	private Object value;	

	public void addAll(FacesBean from) {

	}

	public void addEntry(PropertyKey listKey, Object value) {

	}

	public Set<PropertyKey> bindingKeySet() {

		return null;
	}

	public boolean containsEntry(PropertyKey listKey, Class<?> clazz) {

		return false;
	}

	public Iterator<? extends Object> entries(PropertyKey listKey) {

		return null;
	}

	public Object[] getEntries(PropertyKey listKey, Class<?> clazz) {

		return null;
	}

	public Object getLocalProperty(PropertyKey key) {

		return null;
	}

	public Object getProperty(PropertyKey key) {
		return this.value;
	}

	public Object getRawProperty(PropertyKey key) {

		return null;
	}

	public Type getType() {
		return getFacesBeanType();
	}
	public Type getFacesBeanType() {
		facesBeanType	= PlcMockFactory.getMock(Type.class);
		propertyKey		= PlcMockFactory.getMock(PropertyKey.class);
		expect(facesBeanType.findKey(PlcJsfConstantes.PROPRIEDADES.LOOKUP_VALUE_KEY)).andReturn(propertyKey).anyTimes();
		replay(facesBeanType);
		return facesBeanType;
	}
	public ValueBinding getValueBinding(PropertyKey key) {

		return null;
	}

	public ValueExpression getValueExpression(PropertyKey key) {

		return null;
	}

	public Set<PropertyKey> keySet() {

		return null;
	}

	public void markInitialState() {

		
	}

	public void removeEntry(PropertyKey listKey, Object value) {

		
	}

	public void restoreState(FacesContext context, Object state) {

		
	}

	public Object saveState(FacesContext context) {

		return null;
	}

	public void setProperty(PropertyKey key, Object value) {
		this.propertyKey = key;
		this.value = value;
	}

	public void setValueBinding(PropertyKey key, ValueBinding binding) {

	}

	public void setValueExpression(PropertyKey key, ValueExpression expression) {
	
	}

	public void addClientBehavior(String arg0, ClientBehavior arg1) {

	}

	public void clearInitialState() {

	}

	public Map<String, List<ClientBehavior>> getClientBehaviors() {

		return null;
	}

	public boolean initialStateMarked() {

		return false;
	}
	
	


	



	
	
}
