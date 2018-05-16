/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.adapter;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.component.core.input.CoreSelectOneRadio;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcRadio;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcGlobalTag;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;

public class PlcRadioAdapter {

	private static PlcRadioAdapter INSTANCE = new PlcRadioAdapter ();

	private PlcRadioAdapter () {
		
	}

	public static PlcRadioAdapter getInstance () {
		return INSTANCE;
	}

	public void adapter (FacesBean bean,  ValueExpression dominio, String obrigatorio, String obrigatorioDestaque, String somenteLeitura, String classeCSS,
	 String ajudaChave, String ajuda, ValueExpression exibeSe, String bundle, String chavePrimaria, String colunas, ValueExpression titulo, String tituloChave, String riaUsa){
		
		PlcComponentUtil componenteUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		// Passar estas propriedades para a função setPropertiesComuns		
		PlcGlobalTag globalTag = new PlcGlobalTag ( componenteUtil.getValueProperty(bean),
													classeCSS, ajuda,
													ajudaChave, titulo, tituloChave,
													somenteLeitura, chavePrimaria, obrigatorio,
													null, null ,bundle, exibeSe);
		
		// Propriedades especificas
		if(dominio.isLiteralText()){
			PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
			bean.setValueExpression(PlcRadio.DOMINIO_KEY, elUtil.createValueExpression("#{plcDominios.dominios['"+dominio.getValue(FacesContext.getCurrentInstance().getELContext())+"']}", Object.class));
		}
		else{
			bean.setValueExpression(PlcRadio.DOMINIO_KEY, dominio);
		}
		
		if (componenteUtil.isValueDefined(bean, CoreSelectOneRadio.LAYOUT_KEY)) {
			bean.setProperty(CoreSelectOneRadio.LAYOUT_KEY, "horizontal");
		}
		
		bean.setProperty(PlcRadio.RIA_USA, riaUsa);
		
		// Propriedades Comuns
		PlcTagUtil.setCommonProperties(bean, bean.getType(),globalTag);
		
	}
	
}
