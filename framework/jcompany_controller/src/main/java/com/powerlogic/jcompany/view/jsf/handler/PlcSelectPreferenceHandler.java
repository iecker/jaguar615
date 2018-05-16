/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.handler;

import java.io.IOException;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;

import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidadinternal.facelets.TrinidadComponentHandler;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.cache.PlcCacheSessionVO;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcSelectPreference;

public class PlcSelectPreferenceHandler extends TrinidadComponentHandler {

	private TagAttribute	plcPreferencia;

	public PlcSelectPreferenceHandler(ComponentConfig config) {

		super(config);

		plcPreferencia = getAttribute("plcPreferencia");

	}

	@Override
	public void setAttributes(FaceletContext ctx, Object instance) {

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		FacesBean bean = ((PlcSelectPreference) instance).getFacesBean();

		/*
		 * Este valor é adicionado no request em um iteração. Ex: preferenciasLayoutPlc
		 */
		String preferencia = (String) contextUtil.getRequestAttribute("plcPreferencia");

		bean.setProperty(PlcSelectPreference.PLC_PREFERENCIA, preferencia);

		PlcCacheSessionVO cacheSessao = (PlcCacheSessionVO) contextUtil.getRequest().getSession().getAttribute(PlcConstants.SESSION_CACHE_KEY);

		String layoutCorrente = cacheSessao.getLayout();

		if (layoutCorrente.endsWith("ex")) {
			layoutCorrente = layoutCorrente.substring(0, layoutCorrente.length() - 2) + "_dinamico";
		}

		if (cacheSessao.getIndLayoutReduzido().equals("S")) {
			layoutCorrente = layoutCorrente + "_reduzido";
		}

		super.setAttributes(ctx, instance);

		if (layoutCorrente != null && layoutCorrente.equals(preferencia)) {
			bean.setProperty(PlcSelectPreference.VALUE_KEY, true);
		}

		String peleCorrente = cacheSessao.getPele();
		if (peleCorrente != null && peleCorrente.equals(preferencia)) {
			bean.setProperty(PlcSelectPreference.VALUE_KEY, true);
		}

	}
	
	@Override
	public void applyNextHandler(FaceletContext ctx, UIComponent c) throws IOException, FacesException, ELException {
		// forçando a regeração do clientBehaviour.
		c.setParent(null);
		super.applyNextHandler(ctx, c);
	}

}
