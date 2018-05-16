/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.renderer;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.context.RenderingContext;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.SimpleInputTextRenderer;

import com.powerlogic.jcompany.view.jsf.component.PlcText;

public class PlcSimpleInputTextRenderer extends SimpleInputTextRenderer {

	public PlcSimpleInputTextRenderer(FacesBean.Type type) {

		super(type);
	}

	@Override
	protected void renderAllAttributes(FacesContext context, RenderingContext rc, UIComponent component, FacesBean bean, boolean renderStyleAttrs) throws IOException {

		super.renderAllAttributes(context, rc, component, bean, renderStyleAttrs);
		ResponseWriter rw = context.getResponseWriter();
		if (StringUtils.isNotBlank(getTabIndex(bean))) {
			rw.writeAttribute("tabIndex", getTabIndex(bean), "tabIndex");
		}

	}

	protected String getTabIndex(FacesBean bean) {

		return (String) bean.getProperty(PlcText.TAB_INDEX);
	}

}
