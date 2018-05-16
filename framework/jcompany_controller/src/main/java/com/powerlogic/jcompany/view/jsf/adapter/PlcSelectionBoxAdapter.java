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
import org.apache.myfaces.trinidad.component.core.input.CoreSelectBooleanCheckbox;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcSelectionBox;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcGlobalTag;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;

public class PlcSelectionBoxAdapter {

	private static PlcSelectionBoxAdapter INSTANCE = new PlcSelectionBoxAdapter ();

	protected PlcSelectionBoxAdapter () {
		
	}

	public static PlcSelectionBoxAdapter getInstance () {
		return INSTANCE;
	}

	public void adapter (FacesBean bean, ValueExpression texto, String classeCSS, String chavePrimaria, String ajuda, String ajudaChave, String obrigatorio,
			String obrigatorioDestaque, String somenteLeitura, String textoChave, String tituloChave, ValueExpression titulo, ValueExpression exibeSe, String bundle,
			String valorMarcado, String valorDesmarcado, String colunas, String riaUsa){
		
		PlcComponentUtil componenteUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		// Passar estas propriedades para a função setPropertiesComuns
		PlcGlobalTag globalTag = new PlcGlobalTag ( componenteUtil.getValueProperty(bean),
													classeCSS, ajuda,
													ajudaChave, titulo, tituloChave,
													somenteLeitura, chavePrimaria, obrigatorio,
													null, null ,bundle, exibeSe);
		
		//	 Propriedades Especificas desta Tag
		if (StringUtils.isBlank((String)bean.getProperty(CoreSelectBooleanCheckbox.TEXT_KEY))) {
			if (texto!=null)
				bean.setProperty( CoreSelectBooleanCheckbox.TEXT_KEY, texto);
			
			if (! StringUtils.isBlank(textoChave)){
				//String bundle = PlcComponentPropertiesUtil.getInstance().getPropertyBundle(bean);
				String valorChave = componenteUtil.createLocalizedMessage(bean, textoChave, new Object []{});
				bean.setProperty(CoreSelectBooleanCheckbox.TEXT_KEY, valorChave);
			}
		}
		
		bean.setProperty(new FacesBean.Type(CoreSelectBooleanCheckbox.TYPE).registerKey("valorMarcado", String.class), valorMarcado);
		bean.setProperty(new FacesBean.Type(CoreSelectBooleanCheckbox.TYPE).registerKey("valorDesmarcado", String.class), valorDesmarcado);
		
		bean.setProperty(PlcSelectionBox.RIA_USA, riaUsa);
		
		// Propriedades Comuns
		PlcTagUtil.setCommonProperties(bean, bean.getType(),globalTag);
		
	   
		
	}
	

	
}
