/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.adapter;

import javax.el.ValueExpression;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.trinidad.bean.FacesBean;

import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcLineSelection;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;

public class PlcLineSelectionAdapter {

	private static PlcLineSelectionAdapter INSTANCE = new PlcLineSelectionAdapter ();

	private PlcLineSelectionAdapter () {
		
	}

	public static PlcLineSelectionAdapter getInstance () {
		return INSTANCE;
	}

	public void adapter (FacesBean bean,  String action, ValueExpression linkEdicao, ValueExpression linkEdicaoCT, ValueExpression propAgregada,
			ValueExpression propAgregadaCampo, ValueExpression redirectModal, ValueExpression titleModal){
		
		PlcComponentUtil componenteUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		String fwPlc 	= contextUtil.getRequestParameter("fwPlc");
		if (StringUtils.isNotBlank(fwPlc))
			action 		= fwPlc;
		bean.setProperty(PlcLineSelection.ACTION_KEY, action);
		PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
		if (linkEdicao==null) {
			String managedBeanName = (String)contextUtil.getRequestAttribute(PlcJsfConstantes.PLC_MANAGED_BEAN_KEY);
			
			linkEdicao = elUtil.createValueExpression("#{"+managedBeanName+".linkEdicaoPlc}", String.class);
		}
		
		bean.setProperty(PlcLineSelection.REDIRECT_MODAL, redirectModal);
		bean.setProperty(PlcLineSelection.TITLE_MODAL, titleModal);	
		
		bean.setProperty(PlcLineSelection.LINK_EDICAO_KEY, linkEdicao);
		bean.setProperty(PlcLineSelection.LINK_EDICAO_CT_KEY, linkEdicaoCT);
		bean.setProperty(PlcLineSelection.PROP_AGREGADA_KEY, propAgregada);
		bean.setProperty(PlcLineSelection.PROP_AGREGADA_CAMPO_KEY, propAgregadaCampo);
		bean.setProperty(PlcLineSelection.ITEM_EXPRESSION_KEY, elUtil.createValueExpression(getItemExpression(), Object.class));
		
		if(!componenteUtil.isValueDefined(bean, PlcLineSelection.ONMOUSEOVER_KEY))
			bean.setProperty(PlcLineSelection.ONMOUSEOVER_KEY, "marcaSelecao(this, event)");
		
		if(!componenteUtil.isValueDefined(bean, PlcLineSelection.ONMOUSEOUT_KEY))
			bean.setProperty(PlcLineSelection.ONMOUSEOUT_KEY, "marcaSelecao(this, event)");
		
		bean.setProperty(PlcLineSelection.ONKEYDOWN_KEY, "marcaSelecao(this, event)");
		bean.setProperty(PlcLineSelection.ONKEYUP_KEY, "marcaSelecao(this, event)");
	
	}
	
	private String getItemExpression() {
		
		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		String managedBean = (String)contextUtil.getRequestAttribute(PlcJsfConstantes.PLC_MANAGED_BEAN_KEY);
		if(true)
			return "#{"+managedBean+"}";
		
		/*ELContextTag _parentELContext = (ELContextTag)
		TagSupport.findAncestorWithClass(this, ELContextTag.class);

		if ((_parentELContext != null)) {
			return _parentELContext.transformExpression("#{"+managedBean+"}");
		}*/
		
		return null;
	}
	
}
