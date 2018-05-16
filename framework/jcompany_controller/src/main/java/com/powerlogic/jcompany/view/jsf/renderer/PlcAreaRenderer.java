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

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.component.core.input.CoreInputText;
import org.apache.myfaces.trinidad.context.RenderingContext;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcViewJsfUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcArea;
import com.powerlogic.jcompany.view.jsf.util.PlcMsgComponentsUtil;

/**
 * Especialização do renderer base PlcTextoRenderer para permitir IoC e DI nos renderes JSF/Trinidad. 
 * 
 */
public class PlcAreaRenderer extends PlcTextRenderer {

	// Definindo o tipo do renderer
	static public final String	RENDERER_TYPE	= "com.powerlogic.jsf.Area";

	/**
	 * Função implementada para acrescentar o código javascript existente na tag area.tag
	 * que verifica o tamanho maximo para o campo.
	 */
	@Override
	protected void encodeAll(FacesContext context, RenderingContext arc, UIComponent component, FacesBean bean) throws IOException {

		PlcMsgComponentsUtil msgComponentsUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgComponentsUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		try {
			if (PlcCDIUtil.getInstance().getInstanceByType(PlcViewJsfUtil.class, QPlcDefaultLiteral.INSTANCE).textMode()) {
				new PlcTitleRenderer().encodeAll(context, arc, component, bean);
				return;
			}
			
			Integer tamanhoMaximo = (Integer) bean.getProperty(CoreInputText.MAXIMUM_LENGTH_KEY);

			if (tamanhoMaximo == null)
				tamanhoMaximo = -1;

			bean.setProperty(CoreInputText.ONKEYDOWN_KEY, "return garanteTamanhoMaximo(getCampo('" + component.getClientId(context) + "'),'" + tamanhoMaximo.toString() + "');");
			bean.setProperty(CoreInputText.ONKEYUP_KEY, "return garanteTamanhoMaximo(getCampo('" + component.getClientId(context) + "'),'" + tamanhoMaximo.toString() + "');");

		} catch (Exception e) {
			msgComponentsUtil.createMsgError(bean, component, null, e);
		}

		super.encodeAll(context, arc, component, bean);

		msgComponentsUtil.printMessageError(component);

	}

	@Override
	protected String getDefaultRiaParameters(FacesContext context, UIComponent component, FacesBean bean) {

		Integer columns = (Integer) ObjectUtils.defaultIfNull(bean.getProperty(PlcArea.COLUMNS_KEY), 0);
		Integer tamanhoMaximo = (Integer) ObjectUtils.defaultIfNull(bean.getProperty(PlcArea.MAXIMUM_LENGTH_KEY), 0);
		String areaParameters = "columns='" + columns + "',tamanhoMaximo='" + tamanhoMaximo + "'";
		String defaultRiaParameters = StringUtils.defaultString(super.getDefaultRiaParameters(context, component, bean));
		if (StringUtils.isEmpty(defaultRiaParameters)) {
			defaultRiaParameters = areaParameters;
		} else {
			defaultRiaParameters += "," + areaParameters;
		}
		return defaultRiaParameters;
	}

	@Override
	protected String getDefaultRia(FacesContext context, UIComponent component, FacesBean bean) {

		return super.getDefaultRia(context, component, bean);
	}
}
