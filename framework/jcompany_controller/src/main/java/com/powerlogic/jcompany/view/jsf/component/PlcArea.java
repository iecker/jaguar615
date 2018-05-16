/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.component;

import org.apache.myfaces.trinidad.bean.FacesBean;

import com.powerlogic.jcompany.view.jsf.renderer.PlcAreaRenderer;

/**
 * Especialização do componente base PlcTexto para permitir IoC e DI nos componentes JSF/Trinidad. 
 * 
 */
public class PlcArea extends PlcText {

	/*
	 * Define o Tipo a Família para o componente 
	 */
	static public final String COMPONENT_FAMILY = "com.powerlogic.jsf.componente.entrada";

	static public final String COMPONENT_TYPE = "com.powerlogic.jsf.componente.PlcArea";

	static public final FacesBean.Type TYPE = new FacesBean.Type(PlcText.TYPE);

	static {//Registra o tipo do componente
		TYPE.lockAndRegister(COMPONENT_TYPE, PlcAreaRenderer.RENDERER_TYPE);
	}

	@Override
	public String getFamily() {

		return COMPONENT_FAMILY;
	}

	@Override
	protected FacesBean.Type getBeanType() {

		return TYPE;
	}

}
