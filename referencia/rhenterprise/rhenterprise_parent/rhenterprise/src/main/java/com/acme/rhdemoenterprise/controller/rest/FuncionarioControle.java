package com.acme.rhdemoenterprise.controller.rest;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.acme.rhdemoenterprise.entity.FuncionarioEntity;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.facade.IPlcFacade;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregationPOJO;
import com.powerlogic.jcompany.config.collaboration.PlcConfigCollaborationPOJO;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerName;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerQualifier;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcCurrent;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcDefaultValue;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcHttpParam;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcController;
import com.powerlogic.jcompany.controller.rest.controllers.PlcBaseGridController;
import com.powerlogic.jcompany.controller.util.PlcBeanPopulateUtil;


@SPlcController
@QPlcControllerName("grid")
@QPlcControllerQualifier("funcionario")
public class FuncionarioControle<E,I> extends PlcBaseGridController<E,I> {
	
	
	private Double totalSalario = 0.0;	
	
	@Override
	protected void retrieveCollectionAfter() {
		
		super.retrieveCollectionAfter();
		
		List<FuncionarioEntity> lista = (List<FuncionarioEntity>) getEntityCollection();
						
		for(FuncionarioEntity e : lista){

				totalSalario+=e.getUltimoSalario().doubleValue();			

		}		
	}

	public Double getTotalSalario() {
		return totalSalario;
	}

	public void setTotalSalario(Double totalSalario) {
		this.totalSalario = totalSalario;
	}	

	
	@Inject
	@Override
	public void setConfigAggregation(@QPlcCurrent PlcConfigAggregationPOJO configAgregacao) {
		super.setConfigAggregation(configAgregacao);
	}

	@Inject
	@Override
	public void setConfigCollaboration(@QPlcCurrent PlcConfigCollaborationPOJO configColaboracao) {
		super.setConfigCollaboration(configColaboracao);
	}
	
	@Inject
	public void setContext(@QPlcCurrent PlcBaseContextVO context) {
		super.setContext(context);
	}
	
	@Inject
	public void setFacade(@QPlcDefault IPlcFacade facade) {
		super.setFacade(facade);
	}

	public PlcBeanPopulateUtil getBeanPopulateUtil() {
		return beanPopulateUtil;
	}

	@Inject
	public void setBeanPopulateUtil(@QPlcDefault PlcBeanPopulateUtil beanPopulateUtil) {
		this.beanPopulateUtil = beanPopulateUtil;
	}
	
	@Inject
	public void setRequest(HttpServletRequest request) {
		super.setRequest(request);
	}

	@Inject
	public void setPage(@QPlcHttpParam("page") @QPlcDefaultValue("1") Integer page) {
		super.setPage(page);
	}

	@Inject
	public void setRows(@QPlcHttpParam("rows") @QPlcDefaultValue("10") Integer rows) {
		super.setRows( rows);
	}

	@Inject
	public void setOrderBy(@QPlcHttpParam("sidx") String orderBy) {
		super.setOrderBy( orderBy);
	}

	@Inject
	public void setOrder(@QPlcHttpParam("sord") String order) {
		super.setOrder(order);
	}
	
	
}
