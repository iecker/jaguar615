/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.jsf;

import java.io.Serializable;

import javax.inject.Inject;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcNamedLiteral;
import com.powerlogic.jcompany.controller.injectionpoint.HttpParam;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcRequestControl;

public class PlcBaseParentMB implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * Entidade corrente: Como o MB é escopo e conversação, mantém principais parametros como instancia, para implementações descendentes
	 */
	protected Object entityPlc;	
	/**
	 * Lista de entidades correntes: Como o MB é escopo e conversação, mantém principais parametros como instancia, para implementações descendentes
	 */
	protected PlcEntityList entityListPlc;
	/**
	 * Este flag pode ser utilizado como controle em Extensions, para indicar para o fluxo principal padrão prosseguir ou não.
	 */
	protected boolean defaultProcessFlow = true;
	/**
	 * Este flag pode ser utilizado como controle em Extensions, para indicar alterações programáticas no fluxo de navegação. 
	 */
	protected String defaultNavigationFlow = null;
	
	/**
	 * Parâmetro de request padrão para OID, gerenciado pelo CDI.
	 */
	@Inject	@HttpParam("id")
	protected String id;
	
	public boolean isDefaultProcessFlow() {
		return defaultProcessFlow;
	}

	public void setDefaultProcessFlow(boolean defaultProcessFlow) {
		this.defaultProcessFlow = defaultProcessFlow;
	}

	public String getDefaultNavigationFlow() {
		return defaultNavigationFlow;
	}

	public void setDefaultNavigationFlow(String defaultNavigationFlow) {
		this.defaultNavigationFlow = defaultNavigationFlow;
	}

	protected PlcRequestControl getPlcRequestControl() {
		return PlcCDIUtil.getInstance().getInstanceByType(PlcRequestControl.class, new PlcNamedLiteral(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_REQUISICAO));
	}

	public Object getEntity() {
		return entityPlc;
	}

	public void setEntity(Object entityPlc) {
		this.entityPlc = entityPlc;
	}

	public String getKeyPlc() {
		return id;
	}

	public void setKeyPlc(String id) {
		this.id = id;
	}

	public PlcEntityList getLogicaItensPlc() {
		return entityListPlc;
	}

	public void setLogicaItensPlc(PlcEntityList entityListPlc) {
		this.entityListPlc = entityListPlc;
	}
	
}
