/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.renderer;

import java.io.IOException;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.component.UIXEditableValue;
import org.apache.myfaces.trinidad.component.UIXValue;
import org.apache.myfaces.trinidad.component.core.input.CoreInputText;
import org.apache.myfaces.trinidad.component.core.input.CoreSelectOneChoice;
import org.apache.myfaces.trinidad.context.RenderingContext;
import org.apache.myfaces.trinidad.render.CoreRenderer;
import org.apache.myfaces.trinidadinternal.agent.TrinidadAgent;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.FormInputRenderer;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.OutputLabelRenderer;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.SelectOneChoiceRenderer;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.SimpleSelectOneChoiceRenderer;
import org.apache.myfaces.trinidadinternal.renderkit.uix.SelectItemSupport;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.annotation.PlcPrimaryKey;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregationPOJO;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.controller.cache.PlcCacheSessionVO;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcViewJsfUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcButton;
import com.powerlogic.jcompany.view.jsf.component.PlcDynamicCombo;
import com.powerlogic.jcompany.view.jsf.util.PlcMsgComponentsUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcRendererUtil;

/**
 * Especialização do renderer base SelectOneChoiceRenderer para permitir IoC e DI nos renderes JSF/Trinidad. 
 * 
 */
public class PlcDynamicComboRenderer extends SelectOneChoiceRenderer {

	Logger							log				= Logger.getLogger(PlcDynamicComboRenderer.class.getCanonicalName());

	protected static final Logger	logVisao		= Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	// Definindo o tipo do renderer
	static public final String		RENDERER_TYPE	= "com.powerlogic.jsf.ComboDinamico";

	@Override
	protected boolean getShowRequired(UIComponent component, FacesBean bean) {

		return false;
	}

	/**
	 * Mantem a escolha do argumento da seleção para o comboDinamico se não for OutputLabelRenderer
	 */
	@Override
	protected void delegateRenderer(FacesContext context, RenderingContext arc, UIComponent component, FacesBean bean, CoreRenderer renderer) throws IOException {

		PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcMsgComponentsUtil msgComponentsUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgComponentsUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcConfigUtil configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcURLUtil urlUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcURLUtil.class, QPlcDefaultLiteral.INSTANCE);

		//cria instancia do propertyUtilsBean para evitar diversas chamadas a metodo syncronized
		PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
		
		try {
			if (renderer instanceof OutputLabelRenderer) {

				boolean simple = getSimple(component, bean);
				Object required = null;
				if (simple) {
					required = bean.getProperty(UIXEditableValue.REQUIRED_KEY);
					bean.setProperty(CoreInputText.SHOW_REQUIRED_KEY, Boolean.FALSE);
					bean.setProperty(UIXEditableValue.REQUIRED_KEY, Boolean.FALSE);
				}
				super.delegateRenderer(context, arc, component, bean, renderer);

				if (simple)
					bean.setProperty(UIXEditableValue.REQUIRED_KEY, required);

			} else {

				PlcConfigAggregationPOJO _configAcao = configUtil.getConfigAggregation(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest()));
				FormPattern pattern = _configAcao.pattern().formPattern();

				if (FormPattern.Rel.equals(pattern) || FormPattern.Sel.equals(pattern) || FormPattern.Con.equals(pattern) || FormPattern.Ctl.equals(pattern) || FormPattern.Ctl.equals(pattern) || FormPattern.Smd.equals(pattern)) {

					//Código para manter a escolha do argumento da selecao para comboDinamico
					Object value = bean.getProperty(UIXValue.VALUE_KEY);
					if (value != null) {

						List<SelectItem> selectItems = SelectItemSupport.getSelectItems(component, null);

						for (SelectItem item : selectItems) {
							if (metamodelUtil.isEntityClass(item.getValue().getClass())) {
								Object vo = item.getValue();
								PlcEntityInstance voInstance = metamodelUtil.createEntityInstance(vo);
								if (value != null && metamodelUtil.isEntityClass(value.getClass())) {
									PlcEntityInstance valueInstance = metamodelUtil.createEntityInstance(value);
									PlcPrimaryKey chavePrimaria = vo.getClass().getAnnotation(PlcPrimaryKey.class);
									if (chavePrimaria == null) {
										if (valueInstance.getId().equals(voInstance.getId())) {
											bean.setProperty(UIXValue.VALUE_KEY, vo);
											break;
										}
									} else {
										Object chaveVo = propertyUtilsBean.getProperty(vo, "idNatural");
										Object chaveValue = propertyUtilsBean.getProperty(value, "idNatural");
										if (chaveVo.equals(chaveValue)) {
											bean.setProperty(UIXValue.VALUE_KEY, vo);
											break;
										}
									}
								} else { //String
									if (value.equals(voInstance.getId().toString())) {
										bean.setProperty(UIXValue.VALUE_KEY, vo);
										break;
									}
								}
							} else if (value.equals(item.getValue())) {
								bean.setProperty(UIXValue.VALUE_KEY, item.getValue());
								break;
							}
						}
					}
				}
				super.delegateRenderer(context, arc, component, bean, renderer);
			}
		} catch (Exception e) {
			msgComponentsUtil.createMsgError(bean, component, null, e);
		}

	}

	/**
	 * Sobrescrito para alterar o objeto responsável por manipular os objetos de escolha.
	 */
	@Override
	protected FormInputRenderer getFormInputRenderer() {

		return new PlcSimpleSelectOneChoiceRenderer();
	}

	/**
	 * IoC do jcompany.  Função implementada para acrescentar o código javascript existente na tag comboDinamico.tag
	 */
	@Override
	protected void encodeAll(FacesContext context, RenderingContext arc, UIComponent component, FacesBean bean) throws IOException {

		PlcMsgComponentsUtil msgComponentsUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgComponentsUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		try{
			
			if (PlcCDIUtil.getInstance().getInstanceByType(PlcViewJsfUtil.class, QPlcDefaultLiteral.INSTANCE).textMode()) {
				new PlcTitleRenderer().encodeAll(context, arc, component, bean);
				return;
			}
		
			ResponseWriter writer = context.getResponseWriter();

			writer.startElement("span", component);
			//Estilos fixos retiram a flexibilidade!
			//Deste jeito fica flexivel - MAS NAO FUNCIONOU - TESTAR E REFATORAR AS DEMAIS
			if (bean.getProperty(CoreSelectOneChoice.STYLE_CLASS_KEY) != null)
				writer.writeAttribute("class", bean.getProperty(CoreSelectOneChoice.STYLE_CLASS_KEY).toString(), null);
			if (bean.getProperty(CoreSelectOneChoice.INLINE_STYLE_KEY) != null)
				writer.writeAttribute("style", bean.getProperty(CoreSelectOneChoice.INLINE_STYLE_KEY).toString(), null);

			super.encodeAll(context, arc, component, bean);

			// criado para identificar que combobox possui valor do índice e não do id do registro.
			writer.startElement("input", component);
			writer.writeAttribute("id", component.getClientId()+"_idx", null);
			writer.writeAttribute("name", component.getClientId()+"_idx", null);
			writer.writeAttribute("type", "hidden", null);
			writer.writeAttribute("value", "S", null);
			writer.endElement("input");			
			
			renderizaBotaoAtualiza(context, component);

			//addNumberLineOnComponent(context, component, bean);

			writer.endElement("span");
			if (!"S".equals(contextUtil.getRequest().getAttribute(PlcConstants.VISUALIZA_DOCUMENTO_PLC) + "")) {
				String defaultRiaParameters = getDefaultRiaParameters(context, component, bean);
				String defaultRia = getDefaultRia(context, component, bean);
				String customRia = getCustomRia(context, component, bean);
				PlcRendererUtil.includeRiaTemplates(component.getClientId(context), defaultRiaParameters, defaultRia, customRia, context.getResponseWriter());
			}
		} catch (Exception e) {
			msgComponentsUtil.createMsgError(bean, component, null, e);
			log.error( e.getMessage(), e);
		}  

		msgComponentsUtil.printMessageError(component);

	}
	

	/**
	 *  IoC do jcompany.  Monta no componente o botao 'A' para atualiza a lista de itens
	 *
	 */
	private void renderizaBotaoAtualiza(FacesContext context, UIComponent component) throws IOException {

		PlcButton botaoAtualiza = ((PlcDynamicCombo) component).getBotaoAtualiza();
		if (botaoAtualiza != null) {
			botaoAtualiza.encodeAll(context);
		}
	}

	static public boolean encodeOption(FacesContext context, RenderingContext arc, UIComponent component, SelectItem item, Converter converter, boolean valuePassThru, int index, boolean isSelected) throws IOException {

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcConfigUtil configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);

		if (item == null) {
			return false;
		}

		if (item.isDisabled()) {
			if (!Boolean.TRUE.equals(arc.getAgent().getCapabilities().get(TrinidadAgent.CAP_SUPPORTS_DISABLED_OPTIONS))) {
				return false;
			}
		}

		PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);

		Object itemValue = SimpleSelectOneChoiceRenderer.getItemValue(context, component, item, converter, valuePassThru, index);

		ResponseWriter writer = context.getResponseWriter();

		writer.startElement("option", null);

		if (item.isDisabled())
			writer.writeAttribute("disabled", Boolean.TRUE, null);

		// Never write out null, because that will result in the label
		// getting submitted, instead of null.
		if (itemValue == null)
			itemValue = "";
		writer.writeAttribute("value", itemValue, null);

		if (configUtil.getConfigApplication().behavior().useRestfulSearch() && ((PlcCacheSessionVO) contextUtil.getRequest().getSession().getAttribute(PlcConstants.SESSION_CACHE_KEY)).getPesquisaRestful().equals("S")) {
			if (item.getValue().getClass().isEnum()) {
				writer.writeAttribute("id", item.getValue().toString(), null);
			} else if (metamodelUtil.isEntityClass(item.getValue().getClass())) {
				PlcEntityInstance itemValueInstance = metamodelUtil.createEntityInstance(item.getValue());
				writer.writeAttribute("id", itemValueInstance.getId(), null);
			}
		}

		if (isSelected)
			writer.writeAttribute("selected", Boolean.TRUE, null);

		// For reasons that aren't especially clear to me, we're getting
		// passed the empty string for our title.
		String description = item.getDescription();
		if ((description != null) && !"".equals(description))
			writer.writeAttribute("title", description, null);

		writer.writeText(item.getLabel(), null);

		writer.endElement("option");

		return true;
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

		return null;
	}

	/**
	 * @return Templates RIA customizado.
	 */
	protected String getCustomRia(FacesContext context, UIComponent component, FacesBean bean) {

		return (String) bean.getProperty(PlcDynamicCombo.RIA_USA);
	}

}
