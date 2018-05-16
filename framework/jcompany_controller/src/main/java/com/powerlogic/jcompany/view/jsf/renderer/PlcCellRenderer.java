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

import org.apache.myfaces.trinidadinternal.uinode.UINodeRendererBase;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcMsgComponentsUtil;

/**
 * Especialização do renderer base UINodeRendererBase para permitir IoC e DI nos renderes JSF/Trinidad. 
 * 
 */
public class PlcCellRenderer extends UINodeRendererBase {

	// Definindo o tipo do renderer
	static public final String	RENDERER_TYPE	= "com.powerlogic.jsf.Celula";

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

		super.encodeEnd(context, component);

		PlcMsgComponentsUtil msgComponentsUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgComponentsUtil.class, QPlcDefaultLiteral.INSTANCE);

		msgComponentsUtil.printMessageError(component);

	}

}
