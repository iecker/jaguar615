/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.rest.controllers;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;

import org.apache.commons.beanutils.PropertyUtils;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerName;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerQualifier;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcController;

/**
 * Atende a /service/autocomplete.<caso_de_uso>
 */
@SPlcController
@QPlcControllerName("autocomplete")
@QPlcControllerQualifier("*")
public class PlcBaseAutoCompleteController<E, I> extends PlcBaseDynamicController<E, I> {

	private String property;
	private Integer limit;

	@Inject
	private HttpServletRequest request;
	
	public String getProperty(){
		return property;
	}
	
	@Inject
	public void setProperty(@QueryParam("q") String propriedade, @QueryParam("limit") Integer limite) {
		this.property = propriedade;
		this.limit = limite;
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public void retrieveCollection() {
		try {
			// A entidade é utilizada como filtro.
			if (getEntity() == null) {
				E instancia = getEntityType().newInstance();
				String v = request.getParameterValues("q")[1];
				
				PropertyUtils.setProperty(instancia, property, v);
				
				setCollectionPageRecords(limit);
				
				setEntity(instancia);
			}
			// recupera coleção sem paginação
			super.retrieveCollection();
		} catch(PlcException plcE){
			throw plcE;	
		} catch (Exception e) {
			throw new PlcException("PlcBaseAutoComplete", "retrieveCollection", e, null, "");

		}
	}	

}
