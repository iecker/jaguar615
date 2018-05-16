/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.jsf.action;

import javax.enterprise.context.ConversationScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.commons.entity.PlcForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.controller.cache.PlcCacheSessionVO;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

/**
 * Controlador MB para formulário de Customização de Formulário
 */
@PlcConfigForm(formLayout = @PlcConfigFormLayout(dirBase = "/WEB-INF/fcls-plc/plc"))
@PlcUriIoC("personalizarformplc")
@PlcHandleException
@ConversationScoped
@SPlcMB
public class PlcCustomizeFormMB extends PlcCustomizeMB {

	@Inject
	protected transient Logger log;

	private static final long serialVersionUID = -842407781728331380L;

	public void formAcaoExibeTexto(ValueChangeEvent event) {

		HttpServletRequest request = contextUtil.getRequest();
		
		PlcCacheSessionVO plcSessao = (PlcCacheSessionVO) request.getSession().getAttribute(PlcConstants.SESSION_CACHE_KEY);
		
		if (!"".equals(event.getNewValue())) {

			PlcForm formulario = new PlcForm();

			formulario.setFormAcaoExibeTexto(event.getNewValue().toString());
			formulario.setFormAlertaAlteracao(plcSessao.getFormAlertaAlteracao().equals("S"));
			formulario.setFormAlertaExclusaoDetalhe(plcSessao.getFormAlertaExclusaoDetalhe().equals("S"));
			formulario.setPesquisaRestful(plcSessao.getPesquisaRestful().equals("S"));

			this.entityPlc = formulario;
		}
	}
}
