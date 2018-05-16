/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.action;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes.NAVEGACAO;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.commons.entity.PlcLayout;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.domain.type.PlcYesNo;

@PlcConfigForm(
	formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls-plc/plc")
)
            
/**
 * Controlador MB para formulário de customização de leiaute
 */
@PlcUriIoC("personalizarlayoutplc")
@PlcHandleException
@SPlcMB
public class PlcCustomizeLayoutMB extends PlcCustomizeMB {
	
	private static final long serialVersionUID = -2555017328706696192L;
	
	@Inject
	protected transient Logger log;

	public String trocaLayoutRequest() {

		HttpServletRequest request = contextUtil.getRequest();

		PlcLayout layout = new PlcLayout();
		
		final String layoutPlcName = "layoutPlc";
		final String layoutReduzidoPlcName = "layoutReduzidoPlc";
		final String reduzidoSuffix = "_reduzido";
		final String dinamicoSuffix = "_dinamico";

		String layoutPlc = request.getParameter(layoutPlcName);
		
		if (StringUtils.isNotEmpty(layoutPlc)) {
			
			layout.setLayout(layoutPlc);
			
			String layoutReduzidoPlc = PlcYesNo.N.name();
			
			if (StringUtils.contains(layoutPlc, reduzidoSuffix)) {
				layoutPlc = StringUtils.replace(layoutPlc, reduzidoSuffix, StringUtils.EMPTY);
				layoutReduzidoPlc = PlcYesNo.S.name();
			}
			
			if (StringUtils.contains(layoutPlc, dinamicoSuffix)) {
				layoutPlc = StringUtils.replace(layoutPlc, dinamicoSuffix, "ex");
			}
			
			request.setAttribute(layoutPlcName, layoutPlc);
			request.setAttribute(layoutReduzidoPlcName, layoutReduzidoPlc);
		}
		
		this.entityPlc = layout;

		return null;
	}
	
}
	

