/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.faces.context.mock;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.faces.bean.ViewScoped;

import com.powerlogic.jcompany.faces.context.ViewScopedContext;

/**
 * Extension que registra escopos adicionais deste projeto para classes de teste
 * unitário
 * 
 * @author george
 * 
 */
public class ScopesExtensionMock implements Extension {

	public void addScope(@Observes final BeforeBeanDiscovery event) {
		event.addScope(ViewScoped.class, true, true);
	}

	public void registerContext(@Observes final AfterBeanDiscovery event) {
		event.addContext(new ViewScopedContextMock());
	}

	/**
	 * Classe mock para {@link ViewScoped}
	 * 
	 * @author george
	 * 
	 */
	private class ViewScopedContextMock extends ViewScopedContext implements
			Serializable {
		private static final long serialVersionUID = 1L;
		private final Map<String, Object> map = new ConcurrentHashMap<String, Object>();

		@Override
		public boolean isActive() {
			return true;
		}

		@Override
		protected Map<String, Object> getViewMap() {
			return map;
		}

		@Override
		protected void assertJsfSubscribed() {
			// Não deve fazer nada.
		}
	}
}
