/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.adapter;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.component.html.HtmlRowLayout;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;

public class PlcLineAdapter {

	private static PlcLineAdapter INSTANCE = new PlcLineAdapter ();
	
	private PlcLineAdapter () {
		
	}

	public static PlcLineAdapter getInstance () {
		return INSTANCE;
	}

	public void adapter (FacesBean bean){
		

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		String styleClass = (String)bean.getProperty(HtmlRowLayout.STYLE_CLASS_KEY);
		if(StringUtils.isBlank(styleClass)) {
			if (contextUtil.isLooping()) {
				
				// default é linhaFormulario
				bean.setProperty(HtmlRowLayout.STYLE_CLASS_KEY, "linhaFormulario");	
			
				// Tenta assumir default apropriadamente pelo contexto 
				if (contextUtil.getRequestAttribute(PlcConstants.FORM_PATTERN)!=null) {
					String logica = (String)contextUtil.getRequestAttribute(PlcConstants.FORM_PATTERN);
					if (logica.startsWith(FormPattern.Con.name()) || 
			                logica.startsWith(FormPattern.Sel.name()) ||
			                logica.startsWith(FormPattern.Smd.name())) {
						if ((Integer.valueOf((String)contextUtil.getCurrentLineNumber())%2==0)) {
							bean.setProperty(HtmlRowLayout.STYLE_CLASS_KEY, "linhapar");
						} else {
							bean.setProperty(HtmlRowLayout.STYLE_CLASS_KEY, "linhaimpar ui-widget-default");
						}
					}
				}
			}
		}
	
	}
	
}
