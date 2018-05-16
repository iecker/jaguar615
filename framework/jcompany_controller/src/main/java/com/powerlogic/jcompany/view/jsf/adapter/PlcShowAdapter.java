/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.adapter;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.component.core.input.CoreInputText;
import org.apache.myfaces.trinidad.component.core.output.CoreOutputLabel;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcTitle;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;

public class PlcShowAdapter {

	private static PlcShowAdapter INSTANCE = new PlcShowAdapter ();
	
	private PlcShowAdapter () {
		
	}
	
	public static PlcShowAdapter getInstance () {
		return INSTANCE;
	}
	
	/**
	 * Adapter entre o Handler e a tag
	 * 
	 * @param bean
	 * @param titulo
	 * @param colunas
	 * @param classeCSS
	 * @param tituloChave
	 * @param bundle
	 * @param riaUsa 
	 * @param alias
	 * @param propOrdenacao
	 * @param ordem
	 */
	public void adapter (FacesBean bean, String chavePrimaria, String bundle, String enumI18n, String riaUsa, String classeCSS){
		
		PlcComponentUtil componenteUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		String propriedade = componenteUtil.getValueProperty(bean);
		if (StringUtils.isNotBlank(propriedade)) {
			bean.setProperty(PlcTitle.PROPRIEDADE_KEY, propriedade);
		}

		
		if ("S".equals(chavePrimaria)){
			bean.setProperty(CoreInputText.DISABLED_KEY, Boolean.TRUE);
		}
		
		if (StringUtils.isNotBlank(bundle)) {
			bean.setProperty(PlcTitle.BUNDLE, bundle);
		}
		
		if ("S".equals((enumI18n))) {
			bean.setProperty(PlcTitle.ENUM_I18N, Boolean.TRUE);
		}
		
		if (! StringUtils.isBlank( classeCSS )) 
			bean.setProperty(CoreOutputLabel.STYLE_CLASS_KEY,  classeCSS);
		
		bean.setProperty(PlcTitle.RIA_USA, riaUsa);
	    
	}
	
}
