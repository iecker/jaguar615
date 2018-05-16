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
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.InputHiddenRenderer;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcMsgComponentsUtil;

/**
 * Especialização do renderer base InputHiddenRenderer para permitir IoC e DI nos renderes JSF/Trinidad. 
 * 
 */
public class PlcHiddenRenderer extends InputHiddenRenderer {

	// Definindo o tipo do renderer
	public static final String	RENDERER_TYPE	= "com.powerlogic.jsf.Escondido";

	@Override
	protected void encodeEnd(FacesContext context, RenderingContext arc, UIComponent component, FacesBean bean) throws IOException {

		super.encodeEnd(context, arc, component, bean);

		PlcMsgComponentsUtil msgComponentsUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgComponentsUtil.class, QPlcDefaultLiteral.INSTANCE);

		msgComponentsUtil.printMessageError(component);

	}
	

}
