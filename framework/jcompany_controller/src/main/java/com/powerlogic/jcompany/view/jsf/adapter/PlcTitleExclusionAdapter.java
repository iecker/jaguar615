/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.adapter;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.trinidad.bean.FacesBean;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcTitle;


public class PlcTitleExclusionAdapter {

	private static PlcTitleExclusionAdapter INSTANCE = new PlcTitleExclusionAdapter ();
	
	private PlcTitleExclusionAdapter () {
		
	}
	
	public static PlcTitleExclusionAdapter getInstance () {
		return INSTANCE;
	}
	
	/**
	 * Adapter
	 * @param bean
	 * @param caminhoImagem
	 */
	public void adapter (FacesBean bean, String caminhoImagem){
		
		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcConfigUtil configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);

		final String icoLixeiraGif = "/res-plc/midia/ico_lixeira.gif";
		final String formLixeiraPng = "/res-plc/midia/form-lixeira.png";
		if (StringUtils.isBlank(caminhoImagem)) {
			caminhoImagem = formLixeiraPng;
		}
		
		String contextPath = contextUtil.getRequest().getContextPath();
		
		bean.setProperty(PlcTitle.CAMINHO_IMAGEM_KEY, contextPath+caminhoImagem);
	}
	
}
