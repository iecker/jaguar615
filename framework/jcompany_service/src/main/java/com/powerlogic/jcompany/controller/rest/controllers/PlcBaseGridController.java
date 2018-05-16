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
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.config.collaboration.PlcConfigSelection;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerName;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerQualifier;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcController;
import com.powerlogic.jcompany.controller.util.PlcBeanPopulateUtil;

/**
 * Atende a /service/grid.<caso_de_uso>
 */
@SPlcController
@QPlcControllerName("grid")
@QPlcControllerQualifier("*")
public class PlcBaseGridController<E, I> extends PlcBaseDynamicController<E, I> {

	@Inject
	@QPlcDefault
	protected PlcBeanPopulateUtil beanPopulateUtil;

	private HttpServletRequest request;

	private Integer page;

	private Integer rows;

	private String orderBy;

	private String order;

	private Long total;

	public HttpServletRequest getRequest() {
		return request;
	}

	public Integer getPage() {
		return page;
	}

	public Integer getRows() {
		return rows;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public String getOrder() {
		return order;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	@Inject
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Inject
	public void setPage(@QueryParam("page") @DefaultValue("1") Integer page) {
		this.page = page;
	}

	@Inject
	public void setRows(@QueryParam("rows") @DefaultValue("10") Integer rows) {
		this.rows = rows;
	}

	@Inject
	public void setOrderBy(@QueryParam("sidx") String orderBy) {
		this.orderBy = orderBy;
	}

	@Inject
	public void setOrder(@QueryParam("sord") String order) {
		this.order = order;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void retrieveCollection() {

		try {

			PlcConfigSelection configSelecao = getConfigCollaboration().selection();

			String querySel = null;
			if (configSelecao != null) {
				querySel = configSelecao.apiQuerySel();
			}

			PlcBaseContextVO context = getContext();

			context.setApiQuerySel(StringUtils.isNotBlank(querySel) ? querySel : null);

			String orderByDinamico = null;
			if (StringUtils.isNotEmpty(orderBy)) {
				orderByDinamico = orderBy + " " + StringUtils.defaultIfEmpty(order, "asc");
			}

			Object instancia = getEntityType().newInstance();

			this.beanPopulateUtil.transferMapToBean(request.getParameterMap(), instancia);
			
			this.retrieveCollectionBefore();

			this.setTotal(getFacade().findCount(context, instancia));

			List<E> lista = (List<E>) getFacade().findList(context, instancia, orderByDinamico, ((page - 1) * rows), (rows));
			
			while(lista.isEmpty() && page>1){
				page--;
				lista = (List<E>) getFacade().findList(context, instancia, orderByDinamico, ((page - 1) * rows), (rows));
			}

			this.setEntityCollection(lista);

			this.retrieveCollectionAfter();
			
		} catch (Exception e) {
			handleExceptions(e);
		}
	}
}
