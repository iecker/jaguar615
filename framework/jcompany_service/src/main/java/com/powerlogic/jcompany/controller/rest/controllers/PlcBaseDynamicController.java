/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */
package com.powerlogic.jcompany.controller.rest.controllers;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregationPOJO;
import com.powerlogic.jcompany.config.collaboration.PlcConfigCollaborationPOJO;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerName;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcCurrent;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcController;
import com.powerlogic.jcompany.controller.util.PlcBeanPopulateUtil;

/**
 * Atende a /service/<casos_de_uso>
 */
@SPlcController
@QPlcControllerName("*")
public class PlcBaseDynamicController<E, I> extends PlcBaseEntityController<E, I> {

	@Inject
	@QPlcCurrent
	private PlcConfigAggregationPOJO configAggregation;

	@Inject
	@QPlcCurrent
	private PlcConfigCollaborationPOJO configCollaboration;

	@Inject
	@QPlcDefault
	protected PlcBeanPopulateUtil beanPopulateUtil;

	@Inject
	private HttpServletRequest request;

	public PlcConfigAggregationPOJO getConfigAggregation() {
		return configAggregation;
	}

	@Inject
	public void setConfigAggregation(@QPlcCurrent PlcConfigAggregationPOJO configAgregacao) {
		this.configAggregation = configAgregacao;
	}

	public PlcConfigCollaborationPOJO getConfigCollaboration() {
		return configCollaboration;
	}

	@Inject
	public void setConfigCollaboration(@QPlcCurrent PlcConfigCollaborationPOJO configColaboracao) {
		this.configCollaboration = configColaboracao;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<E> getEntityType() {
		if (configAggregation != null) {
			return configAggregation.entity();
		} else {
			throw new PlcException("Configuração não encontrada.");
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void retrieveCollection() {
		try {
			// A entidade é utilizada como filtro.
			if (getEntity() == null) {
				E instancia = getEntityType().newInstance();
				beanPopulateUtil.transferMapToBean(request.getParameterMap(), instancia);
				setEntity(instancia);
			}
			// recupera coleção sem paginação
			super.retrieveCollection();
		} catch(PlcException plcE){
			throw plcE;	
		} catch (Exception e) {
			throw new PlcException("PlcBaseDynamicContoller", "retrieveCollection", e, null, "");
		}
	}
}
