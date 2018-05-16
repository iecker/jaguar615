/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.view.jsf.tag;

import javax.el.ValueExpression;

import org.apache.myfaces.trinidad.bean.FacesBean;

import com.powerlogic.jcompany.view.jsf.component.PlcArea;
import com.powerlogic.jcompany.view.jsf.renderer.PlcAreaRenderer;

/**
 * Especialização da tag base PlcTextoTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza um campo com várias linhas e colunas para entrada de dados.!
 * @Exemplo <plcf:area id="obs" value="#{plcEntidade.obs}" ajudaChave="ajuda.obs"  tamanho="80" numLinhas="3" />!
 * @Tag area!
 */
public class PlcAreaTag extends PlcTextTag {

	/**
	 * Recupera qual é o componente associado a esta tag
	 */
	@Override
	public String getComponentType() {
		return PlcArea.COMPONENT_TYPE;
	}
	
	/**
	 *  Recupera qual é o renderer associado a esta tag
	 */
	@Override
	public String getRendererType() {
		return PlcAreaRenderer.RENDERER_TYPE;
	}

	@Override
	protected void setProperties(FacesBean bean) {

		super.tamanho = super.colunas;
		super.setProperties(bean);
		
	}

	public void setNumLinhas(ValueExpression numLinhas) {
		super.setRows(numLinhas);
	}

}
