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

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.PropertyKey;
import org.apache.myfaces.trinidad.component.UIXEditableValue;
import org.apache.myfaces.trinidad.component.core.input.CoreInputText;
import org.apache.myfaces.trinidad.context.RenderingContext;
import org.apache.myfaces.trinidad.render.CoreRenderer;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.FormInputRenderer;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.OutputLabelRenderer;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.SelectBooleanCheckboxRenderer;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.SimpleSelectBooleanCheckboxRenderer;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcSelectionBox;
import com.powerlogic.jcompany.view.jsf.util.PlcMsgComponentsUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcRendererUtil;

/**
 * Especialização do renderer base SelectBooleanCheckboxRenderer para permitir IoC e DI nos renderes JSF/Trinidad. 
 * 
 */
public class PlcSelectionBoxRenderer extends SelectBooleanCheckboxRenderer {

	protected static final Logger	logVisao		= Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	// Definindo o tipo do renderer
	static public final String		RENDERER_TYPE	= "com.powerlogic.jsf.CaixaMarcacao";
	
	/**
	 * Evita imprimir o asterisco quando for obrigatório.
	 */
	@Override
	protected boolean getShowRequired(UIComponent component, FacesBean bean) {

		return false;
	}

	@Override
	protected void delegateRenderer(FacesContext context, RenderingContext arc, UIComponent component, FacesBean bean, CoreRenderer renderer) throws IOException {

		if (renderer instanceof OutputLabelRenderer) {
			Object required = bean.getProperty(UIXEditableValue.REQUIRED_KEY);
			bean.setProperty(CoreInputText.SHOW_REQUIRED_KEY, Boolean.FALSE);
			bean.setProperty(UIXEditableValue.REQUIRED_KEY, Boolean.FALSE);
			super.delegateRenderer(context, arc, component, bean, renderer);
			bean.setProperty(UIXEditableValue.REQUIRED_KEY, required);
		} else {
			super.delegateRenderer(context, arc, component, bean, renderer);
		}

	}

	@Override
	protected void encodeAll(FacesContext context, RenderingContext arc, UIComponent component, FacesBean bean) throws IOException {
        
		PropertyKey somenteLeitura = (PropertyKey)bean.getProperty(PlcSelectionBox.SOMENTE_LEITURA);
		
		//Confirma se a propriedade somente leitura vai ser utilizada		
		if(somenteLeitura != null){
			if(somenteLeitura.toString().equalsIgnoreCase("S")){
				bean.setProperty(PlcSelectionBox.DISABLED_KEY, Boolean.TRUE);
			}
		}		

		super.encodeAll(context, arc, component, bean);

		PlcMsgComponentsUtil msgComponentsUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgComponentsUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		try {
			if (!"S".equals(contextUtil.getRequest().getAttribute(PlcConstants.VISUALIZA_DOCUMENTO_PLC) + "")) {
				String defaultRiaParameters = getDefaultRiaParameters(context, component, bean);
				String defaultRia = getDefaultRia(context, component, bean);
				String customRia = getCustomRia(context, component, bean);
				PlcRendererUtil.includeRiaTemplates(component.getClientId(context), defaultRiaParameters, defaultRia, customRia, context.getResponseWriter());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		msgComponentsUtil.printMessageError(component);

	}

	/**
	 * IoC do jcompany. Especializa renderer Trinidad para acrescentar classe "checkbox" a todos os inputs deste tipo (facilita estilos cross-browser)
	 */
	@Override
	protected FormInputRenderer getFormInputRenderer() {

		return _simpleSelectBooleanCheckbox;
	}

	private SimpleSelectBooleanCheckboxRenderer	_simpleSelectBooleanCheckbox	= 
		new SimpleSelectBooleanCheckboxRenderer() {

		@Override
		protected void renderShortDescAttribute(FacesContext context, RenderingContext arc, UIComponent component, FacesBean bean) throws IOException {

			super.renderShortDescAttribute(context, arc, component, bean);
			ResponseWriter rw = context.getResponseWriter();
			rw.writeAttribute("class", "checkbox", "class");
		}
	
	};

	/**
	 * @return Parâmetros default de todos os templates ria.
	 */
	protected String getDefaultRiaParameters(FacesContext context, UIComponent component, FacesBean bean) {

		return "id='" + component.getClientId(context) + "'";
	}

	/**
	 * @return Templates RIA default do componente.
	 */
	protected String getDefaultRia(FacesContext context, UIComponent component, FacesBean bean) {

		return null;
	}

	/**
	 * @return Templates RIA customizado.
	 */
	protected String getCustomRia(FacesContext context, UIComponent component, FacesBean bean) {

		return (String) bean.getProperty(PlcSelectionBox.RIA_USA);
	}

}
