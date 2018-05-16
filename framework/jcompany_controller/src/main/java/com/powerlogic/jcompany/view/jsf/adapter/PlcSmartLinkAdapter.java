/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.adapter;

import javax.el.ValueExpression;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.trinidad.bean.FacesBean;

import com.powerlogic.jcompany.view.jsf.component.PlcSmartLink;

public class PlcSmartLinkAdapter {

	private static PlcSmartLinkAdapter INSTANCE = new PlcSmartLinkAdapter ();

	private PlcSmartLinkAdapter () {}

	public static PlcSmartLinkAdapter getInstance () {
		return INSTANCE;
	}

	public void adapter (FacesBean bean, ValueExpression link, String alvo, String ajuda, String ajudaChave, 
			String tituloChave, ValueExpression titulo, ValueExpression exibeSe, String classeCSS, String bundle, String riaUsa){
		
		bean.setValueExpression(PlcSmartLink.LINK_KEY, link);
		bean.setValueExpression(PlcSmartLink.TITULO_KEY, titulo);
		bean.setValueExpression(PlcSmartLink.EXIBESE_KEY, exibeSe);
		
		bean.setProperty(PlcSmartLink.RIA_USA, riaUsa);
		
		if (!StringUtils.isBlank(tituloChave))
			bean.setProperty(PlcSmartLink.TITULO_CHAVE_KEY, tituloChave);
		if (!StringUtils.isBlank(ajudaChave))
			bean.setProperty(PlcSmartLink.AJUDA_CHAVE_KEY, ajudaChave);
		if (!StringUtils.isBlank(ajuda))
			bean.setProperty(PlcSmartLink.AJUDA_KEY, ajuda);
		if (!StringUtils.isBlank(classeCSS))
			bean.setProperty(PlcSmartLink.CLASSECSS_KEY, classeCSS);
		if (!StringUtils.isBlank(bundle))
			bean.setProperty(PlcSmartLink.BUNDLE_KEY, bundle);
		if (!StringUtils.isBlank(alvo))
			bean.setProperty(PlcSmartLink.ALVO_KEY, alvo);
		
		
	}
	
	
}
