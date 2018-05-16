/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.adapter;

import javax.el.ValueExpression;

import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.ValueExpressionValueBinding;
import org.apache.myfaces.trinidad.component.UIXSelectBoolean;
import org.apache.myfaces.trinidad.component.UIXValue;
import org.apache.myfaces.trinidad.component.core.input.CoreSelectBooleanCheckbox;

import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcExclusionBox;
import com.powerlogic.jcompany.view.jsf.component.PlcSelectionBox;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentPropertiesUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;

public class PlcExclusionBoxAdapter  extends PlcSelectionBoxAdapter{

	private static PlcExclusionBoxAdapter INSTANCE = new PlcExclusionBoxAdapter ();

	public static PlcExclusionBoxAdapter getInstance () {
		return INSTANCE;
	}

	public void adapter(FacesBean bean, ValueExpression valorChave) {
	
		PlcComponentUtil componenteUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcComponentPropertiesUtil componentPropertiesUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentPropertiesUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		String ajudaChave = "ajuda.indExcPlc";
		
		componentPropertiesUtil.setPropertyShortDesc(PlcExclusionBox.SHORT_DESC_KEY,bean,null,ajudaChave,componenteUtil.getValueProperty(bean));

		Object managedBeanName = contextUtil.getRequestAttribute(PlcJsfConstantes.PLC_MANAGED_BEAN_KEY);
		
		//Se não foi definido o value, será o indExcPlc do managedBean atual.
		if (!componenteUtil.isValueDefined(bean, UIXValue.VALUE_KEY)) {
			PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
			ValueExpression valueExpression = elUtil.createValueExpression("#{"+managedBeanName+".indExcPlc}", Object.class);
			bean.setProperty(UIXValue.VALUE_KEY, valueExpression);
			bean.setValueBinding(UIXValue.VALUE_KEY, ValueExpressionValueBinding.getValueBinding(valueExpression));
			//Essa redefinição de propriedade é necessária, pois no super, quando seria setado, ainda não tinha Value.
			String propriedade = componenteUtil.getValueProperty(bean);
			if (propriedade!=null) {
				bean.setProperty(PlcSelectionBox.PROPRIEDADE_KEY, propriedade);
			}
			//Propriedades específicas
			if (valorChave==null) {
				valorChave = elUtil.createValueExpression("#{"+managedBeanName+".id}", Object.class);
			}
		} else {
			if (valorChave==null) {
				PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
				String valorExpression = componenteUtil.getExpression(bean, UIXValue.VALUE_KEY);
				if(valorExpression.lastIndexOf(".")>0) {
					valorExpression = valorExpression.substring(0, valorExpression.lastIndexOf("."))+".id}";
				} else {
					valorExpression = "#{id}";
				}
				
				valorChave = elUtil.createValueExpression(valorExpression, Object.class);
			}
		}


		bean.setProperty(PlcExclusionBox.VALORCHAVE_KEY, valorChave);

		//Força o componente a não vir marcado.
		bean.setProperty(UIXSelectBoolean.SELECTED_KEY, Boolean.FALSE);

		// inclui javascript que marca linha
		if (!componenteUtil.isValueDefined(bean, CoreSelectBooleanCheckbox.ONCLICK_KEY))
			bean.setProperty(CoreSelectBooleanCheckbox.ONCLICK_KEY, "marcarExclusao(this,event)");



	}

}
