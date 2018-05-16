/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.renderer;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.PropertyKey;
import org.apache.myfaces.trinidad.component.UIXEditableValue;
import org.apache.myfaces.trinidad.component.core.input.CoreInputText;
import org.apache.myfaces.trinidad.context.RenderingContext;
import org.apache.myfaces.trinidad.render.CoreRenderer;
import org.apache.myfaces.trinidad.util.IntegerUtils;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.FormInputRenderer;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.OutputLabelRenderer;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.SelectOneRadioRenderer;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.SimpleSelectOneRadioRenderer;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.controller.cache.PlcCacheSessionVO;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcViewJsfUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcRadio;
import com.powerlogic.jcompany.view.jsf.util.PlcMsgComponentsUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcRendererUtil;

/**
 * Especialização do renderer base SelectOneRadioRenderer para permitir IoC e DI nos renderes JSF/Trinidad. 
 * 
 */
public class PlcRadioRenderer extends SelectOneRadioRenderer {

	protected static final Logger	logVisao		= Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	// Definindo o tipo do renderer
	static public final String		RENDERER_TYPE	= "com.powerlogic.jsf.Radio";

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

	@Override
	protected FormInputRenderer getFormInputRenderer() {

		return new SimpleSelectOneRadioRenderer() {

			@Override
			protected Object getValue(UIComponent component, FacesBean bean) {

				PropertyKey valueKey = bean.getType().findKey("value");
				Object obj = bean.getProperty(valueKey);

				if (obj == null)
					return null;

				ValueExpression ve = bean.getValueExpression(PlcRadio.DOMINIO_KEY);
				Collection<SelectItem> c = null;
				if (ve != null) {
					c = (Collection) ve.getValue(FacesContext.getCurrentInstance().getELContext());
				}

				if (c == null || c.isEmpty())
					return obj;
				Iterator<SelectItem> i = c.iterator();
				while (i.hasNext()) {
					SelectItem item = i.next();
					if (!(item.getValue() instanceof Enum))
						return obj;
					Enum _item = (Enum) item.getValue();
					if (obj.getClass().isEnum()) {
						Enum valor = (Enum) obj;
						if (valor.name().equals(_item.name())) {
							return item.getValue();
						}
					} else {
						String valor = (String) obj;
						if (valor.equals(_item.name())) {
							return _item;
						}
					}
				}

				return obj;
			}

			@Override
			protected boolean isHiddenLabelRequired(RenderingContext arc) {

				return true;
			}

			protected boolean encodeSelectItem(FacesContext context, RenderingContext arc, UIComponent component, SelectItem item, Converter converter, boolean valuePassThru, Object accessKey, int index, boolean isSelected, boolean isDisabled, boolean renderBreak, String itemOnclick) throws IOException {

				PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

				PlcConfigUtil configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);

				if (item == null) {
					return false;
				}

				String id = arc.getCurrentClientId();
				if (id == null)
					return false;

				// Create the per-item ID, necessary for generating the <label>
				// tag.  We use "parentid:_[index]"
				StringBuffer subidBuffer = new StringBuffer(id.length() + 4);
				subidBuffer.append(id);
				subidBuffer.append(":_");
				subidBuffer.append(IntegerUtils.getString(index));
				String subid = subidBuffer.toString();

				Object itemValue = getItemValue(context, component, item, converter, valuePassThru, index);

				FacesBean bean = getFacesBean(component);
				ResponseWriter rw = context.getResponseWriter();

				// Render a <br> if necessary (in "vertical" alignment)
				if (renderBreak) {
					rw.startElement("br", null);
					rw.writeAttribute("style", "clear:both;", null);
					rw.endElement("br");
				}

				// OK, now render the input control
				rw.startElement("input", null);
				rw.writeAttribute("type", "radio", null);
				rw.writeAttribute("name", id, null);
				rw.writeAttribute("id", subid, null);
				rw.writeAttribute("value", itemValue, null);
				rw.writeAttribute("accesskey", accessKey, null);

				PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);

				try {
					if (configUtil.getConfigApplication().behavior().useRestfulSearch() && ((PlcCacheSessionVO) contextUtil.getRequest().getSession().getAttribute(PlcConstants.SESSION_CACHE_KEY)).getPesquisaRestful().equals("S")) {
						if (item.getValue().getClass().isEnum()) {
							rw.writeAttribute("id", item.getValue().toString(), null);
						} else if (metamodelUtil.isEntityClass(item.getValue().getClass())) {
							PlcEntityInstance itemValueInstance = metamodelUtil.createEntityInstance(item.getValue());
							rw.writeAttribute("id", itemValueInstance.getId(), null);
						}
					}
				} catch (PlcException e) {
					e.printStackTrace();
				}

				if (isSelected)
					rw.writeAttribute("checked", Boolean.TRUE, null);
				if (isDisabled || item.isDisabled())
					rw.writeAttribute("disabled", Boolean.TRUE, null);

				
				if (PlcCDIUtil.getInstance().getInstanceByType(PlcViewJsfUtil.class, QPlcDefaultLiteral.INSTANCE).textMode()) {
					rw.writeAttribute("readonly", Boolean.TRUE, null);
				}

				
				// =-=AEW Render all the Javascript needed on a per-item basis.
				// We could optimize SelectOneRadio a bit by gathering
				// up the "form event handlers" in one pass (seems to be about
				// 8% slower this way)
				rw.writeAttribute("onclick", itemOnclick, null);
				renderItemFormEventHandlers(context, component, bean);

				rw.endElement("input");

				// And render the label
				rw.startElement("label", null);
				rw.writeAttribute("for", subid, null);

				// For reasons that aren't especially clear to me, we're getting
				// passed the empty string for our title.
				String description = item.getDescription();
				if ((description != null) && !"".equals(description))
					rw.writeAttribute("title", description, null);

				rw.writeText(item.getLabel(), null);
				rw.endElement("label");

				return true;
			}

		};
	}

	/**
	 *  IoC do jcompany. Função implementada para acrescentar o código javascript existente na tag radio.tag
	 */

	@Override
	protected void encodeAll(FacesContext context, RenderingContext arc, UIComponent component, FacesBean bean) throws IOException {

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcMsgComponentsUtil msgComponentsUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgComponentsUtil.class, QPlcDefaultLiteral.INSTANCE);

		try {

			super.encodeAll(context, arc, component, bean);

			if (!"S".equals(contextUtil.getRequest().getAttribute(PlcConstants.VISUALIZA_DOCUMENTO_PLC) + "")) {
				String defaultRiaParameters = getDefaultRiaParameters(context, component, bean);
				String defaultRia = getDefaultRia(context, component, bean);
				String customRia = getCustomRia(context, component, bean);
				PlcRendererUtil.includeRiaTemplates(component.getClientId(context), defaultRiaParameters, defaultRia, customRia, context.getResponseWriter());
			}

			//addNumberLineOnComponent(context, component, bean);

		} catch (Exception e) {
			msgComponentsUtil.printMessageError(component);
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

		return null;
	}

	/**
	 * @return Templates RIA customizado.
	 */
	protected String getCustomRia(FacesContext context, UIComponent component, FacesBean bean) {

		return (String) bean.getProperty(PlcRadio.RIA_USA);
	}
}
