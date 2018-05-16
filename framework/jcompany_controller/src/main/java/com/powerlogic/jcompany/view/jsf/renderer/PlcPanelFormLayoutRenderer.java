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

import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.context.RenderingContext;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.PanelFormLayoutRenderer;

import com.powerlogic.jcompany.view.jsf.component.PlcPanelFormLayout;

/**
 * Especialização do renderer base UINodeRendererBase para permitir IoC e DI nos renderes JSF/Trinidad. 
 * 
 */
public class PlcPanelFormLayoutRenderer extends PanelFormLayoutRenderer {

	// Definindo o tipo do renderer
	static public final String	RENDERER_TYPE	= "com.powerlogic.jsf.PanelFormLayout";

	@Override
	protected void encodeAll(FacesContext context, RenderingContext rc, UIComponent component, FacesBean bean) throws IOException {

		super.encodeAll(context, rc, component, bean);

		ResponseWriter writer = context.getResponseWriter();
		// Renderiza uma tabela ao final
		String aoSair = (String) bean.getProperty(PlcPanelFormLayout.AOSAIR_KEY);
		if (aoSair != null) {
			// Renderiza linha contendo campo que possui o comando javascript declarado no aoSair, no onfocus do campo
			writer.startElement("table", component);
			writer.writeAttribute("cellpadding", "0", null);
			//writer.writeAttribute("align","right",null);
			writer.writeAttribute("cellspacing", "0", null);
			writer.writeAttribute("style", "table-layout: fixed;height: 0px; text-align:right;", null);
			writer.startElement("tr", component);
			writer.startElement("td", component);
			writer.writeAttribute("style", "height: 0px;", null);
			writer.startElement("input", component);
			writer.writeAttribute("type", "text", null);
			writer.writeAttribute("style", "height: 0;width:0;margin: 0;padding: 0;border:0;", null);
			writer.writeAttribute("onfocus", aoSair, null);
			writer.endElement("input");
			writer.endElement("td");
			writer.endElement("tr");
			writer.endElement("table");
		}

	}

}
