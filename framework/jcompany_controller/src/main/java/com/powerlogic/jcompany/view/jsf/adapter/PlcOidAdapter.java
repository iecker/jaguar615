/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.adapter;

import javax.el.ValueExpression;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.component.UIXValue;
import org.apache.myfaces.trinidad.component.core.input.CoreInputText;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcOid;
import com.powerlogic.jcompany.view.jsf.component.PlcText;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentPropertiesUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;


public class PlcOidAdapter {

	private static PlcOidAdapter INSTANCE = new PlcOidAdapter ();

	private PlcOidAdapter () {
		
	}

	public static PlcOidAdapter getInstance () {
		return INSTANCE;
	}
	
	private Logger logAdvertenciaDesenv = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_ADVERTENCIA_DESENVOLVIMENTO);

	public void adapter (FacesBean bean, String autoRecuperacao, ValueExpression exibeSe){
		
		PlcComponentUtil componenteUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcComponentPropertiesUtil componentPropertiesUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentPropertiesUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcConfigUtil configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);

		// Para oid, necessário imprimir flag para teste do qa
		bean.setProperty(PlcOid.RENDERED_KEY, true);
		
		PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		if (exibeSe != null){
			Boolean valorExibeSe = elUtil.evaluateExpressionGet(exibeSe.getExpressionString(), Boolean.class);
			bean.setProperty(PlcOid.EXIBE_SE_KEY_OID, valorExibeSe != null ? valorExibeSe.booleanValue() : true);
		}
		
	
		//Se não foi definido o value, será o id do managedBean atual.
		Object managedBeanName = contextUtil.getRequestAttribute(PlcJsfConstantes.PLC_MANAGED_BEAN_KEY);
		
		
		if (!componenteUtil.isValueDefined(bean, UIXValue.VALUE_KEY)) {
			bean.setValueExpression(UIXValue.VALUE_KEY, elUtil.createValueExpression("#{"+managedBeanName+".id}", Object.class));
			
			//Essa redefinição de propriedade é necessária, pois no super, quando seria setado, ainda não tinha Value.
			String propriedade = componenteUtil.getValueProperty(bean);
			if (propriedade!=null) {
				bean.setProperty(PlcText.PROPRIEDADE_KEY, propriedade);
			}

		}
	
		if (autoRecuperacao.equals("N"))
			bean.setProperty(PlcOid.DISABLED_KEY, true);
		else
			bean.setProperty(PlcOid.AUTO_RECUPERACAO_KEY, autoRecuperacao);
				
		componentPropertiesUtil.setPropertyNestedJavaScript(CoreInputText.ONKEYDOWN_KEY, bean, "return validaCaracter(this, event, 'V');");
				
		if (!StringUtils.isBlank(autoRecuperacao)){
			if ("S".equals(autoRecuperacao)){
				componentPropertiesUtil.setPropertyNestedJavaScript(CoreInputText.ONBLUR_KEY, bean, "autoRecuperacao('S');");
			}
		}
		
		// Fazer que botão não aparece ao clonar.
		bean.setValueExpression(CoreInputText.RENDERED_KEY,	elUtil.createValueExpression("#{requestScope.indAcaoOriginal!='clona'}", Boolean.class));
		
		
	}
	
}
