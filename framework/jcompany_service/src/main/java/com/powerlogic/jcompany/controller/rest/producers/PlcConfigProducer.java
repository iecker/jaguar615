/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */
package com.powerlogic.jcompany.controller.rest.producers;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregationPOJO;
import com.powerlogic.jcompany.config.collaboration.PlcConfigCollaborationPOJO;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerName;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerQualifier;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcCurrent;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;

@RequestScoped
public class PlcConfigProducer {

	@Inject
	protected transient Logger log;

	@Inject
	@QPlcDefault
	protected PlcConfigUtil configUtil;

	protected PlcConfigAggregationPOJO configAggregation = null;

	protected PlcConfigCollaborationPOJO configCollaboration = null;

	@Produces
	@QPlcCurrent
	@RequestScoped
	public PlcConfigAggregationPOJO getPlcConfigAgregacaoAtual(@QPlcControllerName String controllerName, @QPlcControllerQualifier String controlleQualifier) {
		if (configAggregation == null) {
			configAggregation = configUtil.getConfigAggregation(getConfigName(controllerName, controlleQualifier));
		}
		return configAggregation;
	}

	@Produces
	@QPlcCurrent
	@RequestScoped
	public PlcConfigCollaborationPOJO getPlcConfigGrupoControleAtual(@QPlcControllerName String controllerName, @QPlcControllerQualifier String controlleQualifier) {
		if (configCollaboration == null) {
			configCollaboration = configUtil.getConfigCollaboration(getConfigName(controllerName, controlleQualifier));
		}
		return configCollaboration;
	}

	protected String getConfigName(String controllerName, String controlleQualifier) {
		String configName = StringUtils.defaultIfEmpty(controlleQualifier, controllerName);
		// trata nome de modulo.
		return configName.replace('.', '/').replace('_', '/');
	}
}
