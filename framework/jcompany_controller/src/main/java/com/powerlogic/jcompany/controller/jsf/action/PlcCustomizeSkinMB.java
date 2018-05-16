/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.action;

import java.util.Map;

import javax.enterprise.context.ConversationScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.commons.entity.PlcSkin;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.view.jsf.component.PlcSelectPreference;

@PlcConfigForm(
	formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls-plc/plc")
)
                  
/**
 * Controlador para personalização de pele
 */
@PlcUriIoC("personalizarpeleplc")
@PlcHandleException
@ConversationScoped
@SPlcMB
public class PlcCustomizeSkinMB extends PlcCustomizeMB {
	
	private static final long serialVersionUID = 8092349060616474037L;
	
	@Inject
	protected transient Logger log;
	
	public void registraPlcPreferencia(ValueChangeEvent event) {

		if (event.getNewValue().equals(Boolean.TRUE)) {
			
			PlcSkin pele = new PlcSkin();
			
			PlcSelectPreference source = (PlcSelectPreference) event.getSource();
			Map<String, Object> attributes = source.getAttributes();
			pele.setPele(attributes.get("plcPreferencia").toString());
			
			this.entityPlc = pele;
		}
	}
}
	

