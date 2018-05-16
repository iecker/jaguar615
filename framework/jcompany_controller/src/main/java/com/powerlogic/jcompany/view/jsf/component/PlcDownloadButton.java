/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.component;

import org.apache.myfaces.trinidad.bean.FacesBean;

/**
 * Especialização do componente base PlcBotao para permitir IoC e DI nos componentes JSF/Trinidad. 
 * 
 */
public class PlcDownloadButton extends PlcButton {

	/*
	 * Define o Tipo para o componente 
	 */
	static public final String	COMPONENT_TYPE	= "com.powerlogic.jsf.componente.PlcBotaoDownload";

	@Override
	public String getFamily() {

		return COMPONENT_FAMILY;
	}

	@Override
	protected FacesBean.Type getBeanType() {

		return TYPE;
	}

}
