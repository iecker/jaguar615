/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.view.jsf.tag;

import javax.inject.Inject;

import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidadinternal.taglib.core.input.CoreSelectBooleanRadioTag;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.controller.cache.PlcCacheSessionVO;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcSelectPreference;
import com.powerlogic.jcompany.view.jsf.renderer.PlcSelectPreferenceRenderer;

public class PlcSelectPreferenciaTag extends CoreSelectBooleanRadioTag {
	
	private String plcPreferencia;
	
	
	/**
	 * Serviço injetado e gerenciado pelo CDI
	 */
	@Inject @QPlcDefault 
	protected PlcContextUtil contextUtil;
	
 
	
	/**
	 * Recupera qual é o componente associado a esta tag
	 */
	@Override
	public String getComponentType() {
		return PlcSelectPreference.COMPONENT_TYPE;
	}

	/**
	 *  Recupera qual é o renderer associado a esta tag
	 */
	@Override
	public String getRendererType() {
		return PlcSelectPreferenceRenderer.RENDERER_TYPE;
	}

	
	@Override
	protected void setProperties(FacesBean bean) {

		/*
		 * Este valor é adicionado no request em um iteração. Ex: preferenciasLayoutPlc
		 */
		String preferencia = (String) contextUtil.getRequestAttribute("plcPreferencia");
		bean.setProperty(PlcSelectPreference.PLC_PREFERENCIA, preferencia);

		PlcCacheSessionVO cacheSessao = (PlcCacheSessionVO) contextUtil.getRequest().getSession().getAttribute(PlcConstants.SESSION_CACHE_KEY);
		
		String layoutCorrente = cacheSessao.getLayout();
		if (layoutCorrente.endsWith("ex"))
			layoutCorrente = layoutCorrente.substring(0, layoutCorrente.length()-2) + "_dinamico";
			
		if (cacheSessao.getIndLayoutReduzido().equals("S"))
			layoutCorrente = layoutCorrente+"_reduzido";
		if (layoutCorrente != null && layoutCorrente.equals(preferencia))
			bean.setProperty(PlcSelectPreference.VALUE_KEY, true);

		String peleCorrente = cacheSessao.getPele();
		if (peleCorrente != null && peleCorrente.equals(preferencia))
			bean.setProperty(PlcSelectPreference.VALUE_KEY, true);

		super.setProperties(bean);
	}
	
	


	public String getPlcPreferencia() {
		return plcPreferencia;
	}

	public void setPlcPreferencia(String plcPreferencia) {
		this.plcPreferencia = plcPreferencia;
	}

}
