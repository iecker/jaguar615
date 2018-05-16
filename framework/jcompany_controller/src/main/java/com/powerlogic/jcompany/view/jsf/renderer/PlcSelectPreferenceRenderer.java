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

import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.context.RenderingContext;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.SelectBooleanRadioRenderer;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcMsgComponentsUtil;

public class PlcSelectPreferenceRenderer extends SelectBooleanRadioRenderer {

	// Definindo o tipo do renderer
	static public final String		RENDERER_TYPE	= "com.powerlogic.jsf.SelectPreferencia";

	@Override
	protected void encodeAll(FacesContext context, RenderingContext arc, UIComponent component, FacesBean bean) throws IOException {

		PlcMsgComponentsUtil msgComponentsUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgComponentsUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		try {
			super.encodeAll(context, arc, component, bean);
		} catch (Exception e) {
			msgComponentsUtil.createMsgError(bean, component, null, e);
		}

		msgComponentsUtil.printMessageError(component);

	}

}
