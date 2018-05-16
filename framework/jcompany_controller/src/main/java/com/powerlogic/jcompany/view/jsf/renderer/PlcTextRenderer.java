/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.renderer;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.component.UIXComponent;
import org.apache.myfaces.trinidad.component.core.input.CoreInputText;
import org.apache.myfaces.trinidad.context.RenderingContext;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.FormInputRenderer;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.InputTextRenderer;

import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcViewJsfUtil;
import com.powerlogic.jcompany.domain.validation.PlcValSimpleFormat.SimpleFormat;
import com.powerlogic.jcompany.view.jsf.component.PlcText;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcMsgComponentsUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcRendererUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;

/**
 * Especialização do renderer base InputTextRenderer para permitir IoC e DI nos renderes JSF/Trinidad. 
 * 
 */
public class PlcTextRenderer extends InputTextRenderer {

	protected static final Logger	logVisao		= Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	// Definindo o tipo do renderer
	static public final String		RENDERER_TYPE	= "com.powerlogic.jsf.Texto";

	/**
	 * Evita imprimir o asterisco quando for obrigatório.
	 */
	@Override
	protected boolean getShowRequired(UIComponent component, FacesBean bean) {

		return false;
	}

	/**
	 * IoC do jcompany. Renderiza efetivamente, incluindo javascripts e especializacoes jCompany
	 */
	@Override
	protected void encodeAll(FacesContext context, RenderingContext arc, UIComponent component, FacesBean bean) throws IOException {

		PlcMsgComponentsUtil msgComponentsUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgComponentsUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		try {

			if (PlcCDIUtil.getInstance().getInstanceByType(PlcViewJsfUtil.class, QPlcDefaultLiteral.INSTANCE).textMode()) {
				if (bean.getProperty(PlcText.SECRET_KEY) == null  || !((Boolean)bean.getProperty(PlcText.SECRET_KEY))) {
					new PlcTitleRenderer().encodeAll(context, arc, component, bean);
				}	
				return;
			}
						
			String formato = PlcTagUtil.getAnnotationSimpleFormat(bean);

			if (!StringUtils.isBlank(formato) && !formato.equals(SimpleFormat.NUMBER.toString())) {
				if (StringUtils.isBlank((String) bean.getProperty(CoreInputText.ONKEYUP_KEY))) {
					bean.setProperty(CoreInputText.ONKEYUP_KEY, componentUtil.getScriptFormat(bean, CoreInputText.ONKEYUP_KEY, formato));
				} else {
					bean.setProperty(CoreInputText.ONKEYUP_KEY, componentUtil.getScriptFormat(bean, CoreInputText.ONKEYUP_KEY, formato));
				}

				if (StringUtils.isBlank((String) bean.getProperty(CoreInputText.ONKEYPRESS_KEY))) {
					bean.setProperty(CoreInputText.ONKEYPRESS_KEY, componentUtil.getScriptFormat(bean, CoreInputText.ONKEYPRESS_KEY, formato));
				} else {
					bean.setProperty(CoreInputText.ONKEYPRESS_KEY, componentUtil.getScriptFormat(bean, CoreInputText.ONKEYPRESS_KEY, formato));
				}

				if (StringUtils.isBlank((String) bean.getProperty(CoreInputText.ONKEYDOWN_KEY))) {
					bean.setProperty(CoreInputText.ONKEYDOWN_KEY, componentUtil.getScriptFormat(bean, CoreInputText.ONKEYDOWN_KEY, formato));
				} else {
					bean.setProperty(CoreInputText.ONKEYDOWN_KEY, componentUtil.getScriptFormat(bean, CoreInputText.ONKEYDOWN_KEY, formato));
				}

			}

			super.encodeAll(context, arc, component, bean);

			configuraRia(context, component, bean);

		} catch (Exception e) {
			msgComponentsUtil.createMsgError(bean, component, null, e);
		}

		msgComponentsUtil.printMessageError(component);

	}
	
	protected void configuraRia(FacesContext context, UIComponent component, FacesBean bean) throws Exception {

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		String mcPlc = contextUtil.getRequest().getParameter("mcPlc");
		if (!"S".equals(contextUtil.getRequest().getAttribute(PlcConstants.VISUALIZA_DOCUMENTO_PLC) + "") && !("p".equalsIgnoreCase(mcPlc) || "t".equalsIgnoreCase(mcPlc))) {
			String defaultRiaParameters = getDefaultRiaParameters(context, component, bean);
			String defaultRia = getDefaultRia(context, component, bean);
			String customRia = getCustomRia(context, component, bean);
			PlcRendererUtil.includeRiaTemplates(component.getClientId(context), defaultRiaParameters, defaultRia, customRia, context.getResponseWriter());
		}

	}

	@Override
	protected FormInputRenderer getFormInputRenderer() {

		return _plcSimpleInputText;
	}

	@Override
	protected void findTypeConstants(FacesBean.Type type) {

		super.findTypeConstants(type);
		_plcSimpleInputText = new PlcSimpleInputTextRenderer(type);
	}

	private PlcSimpleInputTextRenderer	_plcSimpleInputText;

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

		return (String) bean.getProperty(PlcText.RIA_USA);
	}

	@Override
	public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) {

		PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcBeanMessages chave = PlcBeanMessages.JCOMPANY_ERROR_INVALID_VALUE;
		String tipoComponente = component.getValueExpression("value").getType(context.getELContext()).getName();
		if (tipoComponente.equals("java.lang.Integer")) {
			chave = PlcBeanMessages.ERRORS_LONG;
		} else if (tipoComponente.equals("java.math.BigDecimal")) {
			chave = PlcBeanMessages.ERRORS_DOUBLE;
		}

		try {
			return super.getConvertedValue(context, component, submittedValue);
		} catch (ConverterException e) {
			throw new ConverterException(new FacesMessage(componentUtil.getMessage(chave, new String[] { "" })));
		}

	}
}
