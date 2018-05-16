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
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.FacesBean.Type;
import org.apache.myfaces.trinidad.component.UIXEditableValue;
import org.apache.myfaces.trinidad.component.core.input.CoreInputDate;
import org.apache.myfaces.trinidad.component.core.input.CoreInputText;
import org.apache.myfaces.trinidad.context.RenderingContext;
import org.apache.myfaces.trinidad.context.RequestContext;
import org.apache.myfaces.trinidad.render.CoreRenderer;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.FormInputRenderer;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.InputDateRenderer;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.OutputLabelRenderer;

import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcViewJsfUtil;
import com.powerlogic.jcompany.controller.util.PlcI18nUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcDate;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcMsgComponentsUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcRendererUtil;


/**
 * Especialização do renderer base InputDateRenderer para permitir IoC e DI nos renderes JSF/Trinidad. 
 * 
 */
public class PlcDateRenderer extends InputDateRenderer {

	protected static final Logger	logVisao				= Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	// Definindo o tipo do renderer
	static public final String		RENDERER_TYPE			= "com.powerlogic.jsf.Data";

	private PlcDateSimpleRenderer	plcDataSimplesRederer	= null;

	@Override
	protected boolean getShowRequired(UIComponent component, FacesBean bean) {

		return false;
	}

	@Override
	protected void delegateRenderer(FacesContext context, RenderingContext arc, UIComponent component, FacesBean bean, CoreRenderer renderer) throws IOException {

		boolean simple = getSimple(component, bean);

		if (renderer instanceof OutputLabelRenderer && simple) {
			Object required = bean.getProperty(UIXEditableValue.REQUIRED_KEY);
			bean.setProperty(CoreInputText.SHOW_REQUIRED_KEY, Boolean.FALSE);
			bean.setProperty(UIXEditableValue.REQUIRED_KEY, Boolean.FALSE);
			super.delegateRenderer(context, arc, component, bean, renderer);
			bean.setProperty(UIXEditableValue.REQUIRED_KEY, required);
		} else {
			super.delegateRenderer(context, arc, component, bean, renderer);
		}

	}

	/**
	 * Sobre escrito para inicializar um novo delegate renderer que vai,
	 * acresentar um class ao icone do calendario de data.
	 */
	@Override
	protected void findTypeConstants(Type type) {

		super.findTypeConstants(type);
		plcDataSimplesRederer = new PlcDateSimpleRenderer(type);
	}

	/**
	 * Sobre escrito para substituir o delegate renderer que renderiza o icone do calendário
	 */

	@Override
	protected FormInputRenderer getFormInputRenderer() {

		if (plcDataSimplesRederer == null) {
			plcDataSimplesRederer = new PlcDateSimpleRenderer();
		}
		return plcDataSimplesRederer;
	}

	/**
	 * Função implementada para acrescentar o código javascript existente na tag data.tag
	 */
	@Override
	protected void encodeAll(FacesContext context, RenderingContext arc, UIComponent component, FacesBean bean) throws IOException {


		if (PlcCDIUtil.getInstance().getInstanceByType(PlcViewJsfUtil.class, QPlcDefaultLiteral.INSTANCE).textMode()) {
			new PlcTitleRenderer().encodeAll(context, arc, component, bean);
			return;
		}

		
		super.encodeAll(context, arc, component, bean);

		PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcMsgComponentsUtil msgComponentsUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgComponentsUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		try {

			StringBuilder sb = new StringBuilder();
			String id = component.getClientId(context);
			ResponseWriter writer = context.getResponseWriter();
			writer.flush();

			String msgErro = (String) bean.getProperty(PlcDate.MSG_ERRO);
			String titulo = (String) bean.getProperty(CoreInputDate.LABEL_KEY);

			int numLinha = 0;
			if (contextUtil.isLooping()) {
				numLinha = Integer.parseInt(contextUtil.getCurrentLineNumber());
			}

			if (StringUtils.isBlank(msgErro)) {
				msgErro = "{jcompany.erros.data}";
			}

			String valorjCompany = componentUtil.createLocalizedMessage(bean, msgErro, new Object[] {});

			writer.append("\n<script id=\"avaliar:" + component.getId() + id + msgErro + numLinha + "\" type=\"text/javascript\">\n");

			sb.append("validacaoCriaCampo(").append("\"" + id + "\",").append("\"data\",").append("'" + valorjCompany + "', 'PARAM_0', ").append("'" + titulo + "', ").append("'PARAM_3', ").append("'" + (numLinha + 1) + "')");
			writer.write(sb.toString());
			writer.append("</script>\n");

			if (!"S".equals(contextUtil.getRequest().getAttribute(PlcConstants.VISUALIZA_DOCUMENTO_PLC) + "")) {
				String defaultRiaParameters = getDefaultRiaParameters(context, component, bean);
				String defaultRia = getDefaultRia(context, component, bean);
				String customRia = getCustomRia(context, component, bean);
				PlcRendererUtil.includeRiaTemplates(component.getClientId(context), defaultRiaParameters, defaultRia, customRia, context.getResponseWriter());
			}

			//addNumberLineOnComponent(context, component, bean);

		} catch (Exception e) {
			msgComponentsUtil.createMsgError(bean, component, null, e);
		}

		msgComponentsUtil.printMessageError(component);
	}

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

		return "datepicker(isPartialRender=" + RequestContext.getCurrentInstance().isPartialRequest(context) + "); datemask";
	}

	/**
	 * @return Templates RIA customizado.
	 */
	protected String getCustomRia(FacesContext context, UIComponent component, FacesBean bean) {

		return (String) bean.getProperty(PlcDate.RIA_USA);
	}

	@Override
	public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) {
		
		//TODO: Melhorar o código, enviando o erro de conversão para a tela, caso ocorra.
		Object retorno = null;
		try {
			retorno = super.getConvertedValue(context, component, submittedValue);
		} catch (Exception e) {
			HttpServletRequest request = ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()); 
			String skipValidation = request.getParameter("skipValidation");
			
			PlcI18nUtil plcI18nUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcI18nUtil.class, QPlcDefaultLiteral.INSTANCE);			
			
			if(!Boolean.parseBoolean(skipValidation)) {				
				throw new ConverterException(plcI18nUtil.getMessageCal10n(request, PlcBeanMessages.JCOMPANY_ERROR_DATA, new Object[] {component.getAttributes().get("propriedade").toString()}));				
			}
		}

		return retorno;
	}

}
