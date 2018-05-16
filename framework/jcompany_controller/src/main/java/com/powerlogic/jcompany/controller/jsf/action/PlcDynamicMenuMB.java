/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.jsf.action;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.aggregation.PlcConfigPattern;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.config.collaboration.PlcConfigTabular;
import com.powerlogic.jcompany.controller.jsf.PlcBaseMB;
import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.domain.dynamicmenu.PlcDynamicMenuEntity;

@PlcConfigForm( formPattern=FormPattern.Tab,
			 	tabular= @PlcConfigTabular(numNew=1),
			 	formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls-plc/plc")
)

@PlcConfigAggregation(entity = com.powerlogic.jcompany.domain.dynamicmenu.PlcDynamicMenuEntity.class)
/**
 * Controlador para menu dinamico
 */
@PlcUriIoC("menudinamico")	
@PlcHandleException
@SPlcMB
public class PlcDynamicMenuMB extends PlcBaseMB {

	private static final long serialVersionUID = 1L;

	@Inject
	protected transient Logger log;

	/**
	 * Entidade da ação injetado pela CDI
	 */
	@Produces
	@Named("menudinamicoLista")
	public PlcEntityList criaListaEntidadePlc() {
		if (this.entityListPlc==null) {
			this.entityListPlc = new PlcEntityList();
			this.newObjectList();
		}
		return this.entityListPlc;
	}	

}