/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.rest.controllers;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerName;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerQualifier;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcController;

/**
 * Atende a /service/treeview.<caso_de_uso>
 */
@SPlcController
@QPlcControllerName("treeview")
@QPlcControllerQualifier("*")
public class PlcBaseTreeviewController<E, I> extends PlcBaseGridController<E, I> {

	private HttpServletRequest request;

	private Long idPai;

	public HttpServletRequest getRequest() {
		return request;
	}

	public Long getIdPai() {
		return idPai;
	}

	@Inject
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Inject
	public void setIdPai(@QueryParam("nodeid") Long idPai) {
		this.idPai = idPai;
	}

	@Override
	public void retrieveCollection() {

		try {
			retrieveCollectionBefore();
	
			if (getConfigAggregation() != null) {
	
				List<E> resultado = null;

				getContext().setApiQuerySel("queryTreeView");
				
				resultado = (List<E>) getFacade().findListTreeView(getContext(), getEntityType(), getIdPai(), getEntityType(), 0);

				setTotal(Long.valueOf(resultado.size()));
				
				setEntityCollection(resultado);
			}
	
			retrieveCollectionAfter();
		} catch (Exception e) {
			throw new PlcException(e);
		}
	}
}
