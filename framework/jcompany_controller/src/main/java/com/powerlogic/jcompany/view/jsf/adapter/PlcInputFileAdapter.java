/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.adapter;

import javax.el.MethodExpression;
import javax.el.ValueExpression;

import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.component.UIXCommand;
import org.apache.myfaces.trinidad.component.core.input.CoreInputFile;

import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcGlobalTag;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;

public class PlcInputFileAdapter {

	private static PlcInputFileAdapter INSTANCE = new PlcInputFileAdapter ();

	private PlcInputFileAdapter () {
		
	}

	public static PlcInputFileAdapter getInstance () {
		return INSTANCE;
	}

	public void adapter (FacesBean bean, String propriedade, String tituloChave, ValueExpression titulo, String tamanho, String ajudaChave, String ajuda, String classeCSS, String obrigatorio, String colunas, String bundle, ValueExpression exibeSe) {
		
		PlcComponentUtil componenteUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		// Passar estas propriedades para a função setPropertiesComuns
		PlcGlobalTag globalTag = new PlcGlobalTag ( propriedade, classeCSS, ajuda, ajudaChave, titulo, tituloChave, null, null, obrigatorio, tamanho, null ,bundle, exibeSe);
		
		PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		String downloadAction = componenteUtil.transformProperty(PlcJsfConstantes.PLC_ACTION_KEY, "downloadFile", false);
		MethodExpression downloadMethod = elUtil.createMethodExpression(downloadAction, null, null);
		bean.setProperty(UIXCommand.ACTION_EXPRESSION_KEY, downloadMethod);
	
		bean.setProperty(CoreInputFile.ONMOUSEOVER_KEY, "try{animarBotaoMenu(event , \'2\')}catch(e){}");
		bean.setProperty(CoreInputFile.ONMOUSEOUT_KEY, "try{animarBotaoMenu(event, \'\')}catch(e){}");
				
		PlcTagUtil.setCommonProperties(bean, bean.getType(), globalTag);
		
		
	}
	
}
