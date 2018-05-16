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
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.PropertyKey;
import org.apache.myfaces.trinidad.context.FormData;
import org.apache.myfaces.trinidad.context.RenderingContext;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.AutoSubmitUtils;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.CoreFormData;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.SimpleSelectOneChoiceRenderer;
import org.apache.myfaces.trinidadinternal.renderkit.uix.SelectItemSupport;

import com.powerlogic.jcompany.commons.annotation.PlcPrimaryKey;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcDynamicCombo;

/**
 * Especialização do renderer base SimpleSelectOneChoiceRenderer para permitir o uso de objetos lookups e enums com toString() sobrescrito. 
 * 
 */
public class PlcSimpleSelectOneChoiceRenderer extends SimpleSelectOneChoiceRenderer {

	Logger	log	= Logger.getLogger(PlcSimpleSelectOneChoiceRenderer.class.getCanonicalName());

	@Override
	public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {

		FacesBean facesBean = getFacesBean(component);

		boolean valuePassThru = getValuePassThru(component, facesBean);

		if (!valuePassThru) {

			FacesBean bean = facesBean;
			Converter converter = getConverter(component, bean);

			if (converter == null) {
				converter = getDefaultConverter(context, component, bean);
			}

			List<SelectItem> selectItems = getSelectItems(component, converter, true);

			int index = __getIndex(submittedValue, selectItems);
			if (index < 0 || submittedValue==null) {
				return null;
			}

			if (selectItems != null && selectItems.size()>0) {
				SelectItem item = selectItems.get(index);
				if (item != null && item.getValue() instanceof Enum) {
					Object converted = item.getValue();
					if (converter != null && converted != null) {
						converted = converter.getAsObject(context, component, ((Enum) converted).name());
					}
					return converted;
				}
				if (item != null) {
					Object converted = item.getValue();
					if (converter != null && converted != null) {
						converted = converter.getAsObject(context, component, converted.toString());
					}
					return converted;
				} else {
					return null;
				}
			} else
				return null;
		} else {
			return super.getConvertedValue(context, component, submittedValue);
		}
	}

	@Override
	protected Object getValue(UIComponent component, FacesBean bean) {

		PropertyKey valueKey = bean.getType().findKey("value");
		Object obj = bean.getProperty(valueKey);

		if (obj == null)
			return null;

		ValueExpression valueExpression = bean.getValueExpression(PlcDynamicCombo.DOMINIO_KEY);
		Collection<SelectItem> c = (Collection<SelectItem>) valueExpression.getValue(FacesContext.getCurrentInstance().getELContext());
		if (c == null || c.isEmpty())
			return obj;
		Iterator i = c.iterator();
		while (i.hasNext()) {
			Object item = i.next();
			Enum _item;
			if (item instanceof Enum) {
				_item = (Enum) item;
			} else if (item instanceof SelectItem && ((SelectItem) item).getValue() instanceof Enum) {
				_item = (Enum) ((SelectItem) item).getValue();
			} else {
				return obj;
			}
			if (obj.getClass().isEnum()) {
				Enum valor = (Enum) obj;
				if (valor.name().equals(_item.name())) {
					return _item;
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
	protected void encodeAllAsElement(FacesContext context, RenderingContext arc, UIComponent component, FacesBean bean) throws IOException {

		Converter converter = getConverter(component, bean);
		if (converter == null)
			converter = getDefaultConverter(context, component, bean);
		boolean valuePassThru = getValuePassThru(component, bean);

		if (isAutoSubmit(component, bean))
			AutoSubmitUtils.writeDependencies(context, arc);

		// Only add in validators and converters when we're in valuePassThru
		// mode; otherwise, there's not enough on the client to even consider
		FormData fData = arc.getFormData();
		if (fData != null) {
			((CoreFormData) fData).addOnSubmitConverterValidators(component, valuePassThru ? converter : null, valuePassThru ? getValidators(component, bean) : null, getClientId(context, component), isImmediate(component, bean), getRequired(component, bean), getRequiredMessageKey());
		}

		List<SelectItem> selectItems = SelectItemSupport.getSelectItems(component, converter);

		int selectedIndex = _getSelectedIndex(context, component, bean, selectItems, converter, valuePassThru);

		ResponseWriter writer = context.getResponseWriter();
		boolean simple = getSimple(component, bean);
		if (simple) {
			writer.startElement("span", component);
			// put the outer style class here, like af_selectOneRadio, styleClass,
			// inlineStyle, 'state' styles like p_AFDisabled, etc.
			renderRootDomElementStyles(context, arc, component, bean);
		}

		encodeElementContent(context, arc, component, bean, selectItems, selectedIndex, converter, valuePassThru);

		if (isHiddenLabelRequired(arc))
			renderShortDescAsHiddenLabel(context, arc, component, bean);

		if (simple) {
			writer.endElement("span");
		}
	}

	private int _getSelectedIndex(FacesContext context, UIComponent component, FacesBean bean, List<SelectItem> selectItems, Converter converter, boolean valuePassThru) {

		Object submittedValue = getSubmittedValue(component, bean);
		// In passthru mode, if there's a submitted value, we just
		// have to turn it into an int and range-check it
		if ((submittedValue != null) && !valuePassThru) {
			return __getIndex(submittedValue, selectItems);
		}
		// Figure out the current value, whether it's submitted or not
		else {
			Object value;
			if (submittedValue == null) {
				value = getValue(component, bean);
			} else {
				// submittedValue: run it through the converter if there is one
				if (converter != null) {
					try {
						value = converter.getAsObject(context, component, submittedValue.toString());
					}
					// This shouldn't happen unless we got sent a bogus value;
					// log a warning and move on
					catch (ConverterException ce) {
						log.debug( ce.getMessage());
						value = null;
					}
				} else
					value = submittedValue;
			}

			int index = _findIndex(value, selectItems);
			if ((value != null) && (index < 0)) {
				if (log.isDebugEnabled())
					log.debug( "CANNOT_FIND_SELECTED_ITEM_MATCHING_VALUE" + new Object[] { value, component });
			}
			return index;
		}
	}

	@SuppressWarnings("unchecked")
	int __getIndex(final Object submittedValue, final List<SelectItem> selectItems) {

		if ("".equals(submittedValue))
			return -1;

		try {
			int index = Integer.parseInt(submittedValue.toString());
			if ((-1 < index) && (selectItems.size() > index)) {
				return index;
			} else {
				return 0;

			}
		} catch (Exception ne) {
			return 0;
		
		}
	}

	private int _findIndex(Object value, List<SelectItem> selectItems) {

		PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);

		int size = selectItems.size();
		for (int i = 0; i < size; i++) {
			SelectItem item = selectItems.get(i);
			if (item == null)
				continue;

			if (value == null) {
				Object itemValue = item.getValue();
				if ((item == null) || (itemValue == null) || "".equals(itemValue))
					return i;
			} else {
				if (value != null && metamodelUtil.isEntityClass(value.getClass())) {
					Object entity = value;
					Object entityItem = item.getValue();
					PlcPrimaryKey pc = entity.getClass().getAnnotation(PlcPrimaryKey.class);
					if (entity != null && entityItem != null) {
						PlcEntityInstance entityInstance = metamodelUtil.createEntityInstance(entity);
						PlcEntityInstance entityItemInstance = metamodelUtil.createEntityInstance(entityItem);
						if ((pc == null && ObjectUtils.equals(entityInstance.getId(), entityItemInstance.getId()))) {// é chave primaria
							return i;
						} else if ((pc != null && entityInstance.equalsChaveNatural(entityItemInstance))) {
							return i;
						}
					}
				} else {
					if (value.equals(item.getValue()))
						return i;
				}
			}
		}
		return -1;
	}

}
