/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.view.jsf.tag;

import javax.el.ValueExpression;

import org.apache.myfaces.trinidad.bean.FacesBean;

import com.powerlogic.jcompany.view.jsf.adapter.PlcNumericAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcNumeric;
import com.powerlogic.jcompany.view.jsf.renderer.PlcNumericRenderer;

/**
 * Especialização da tag base PlcTextoTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza um campo com um linha para entrada de dados no formato monetário.!
 * @Exemplo <plcf:numerico value="#{plcEntidade.salarioMaximo}"  ajudaChave="ajuda.salarioMaximo" />!
 * @Tag numerico!
 */
public class PlcNumericTag extends PlcTextTag {

	/*
	 * Propriedades ja mapeadas
	 */
	private ValueExpression numCasas;
	
	/**
	 * Recupera qual é o componente associado a esta tag
	 */
	@Override
	public String getComponentType() {
		return PlcNumeric.COMPONENT_TYPE;
	}
	
	/**
	 *  Recupera qual é o renderer associado a esta tag
	 */
	@Override
	public String getRendererType() {
		return PlcNumericRenderer.RENDERER_TYPE;
	}
	
	
	/**
	 *  Registrando valores para propriedades específicas da tag
	 */
	@Override
	protected void setProperties(FacesBean bean){

		// Propriedades Trinidad
		super.setProperties(bean);
							
		PlcNumericAdapter.getInstance().adapter(bean, numCasas);
		
	}

	public void setNumCasas(ValueExpression numCasas) {
		this.numCasas = numCasas;
	}
	
}
