/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.view.jsf.tag;

import javax.el.ValueExpression;

import org.apache.myfaces.trinidad.bean.FacesBean;

import com.powerlogic.jcompany.view.jsf.adapter.PlcExclusionBoxAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcExclusionBox;
import com.powerlogic.jcompany.view.jsf.renderer.PlcSelectionBoxRenderer;

/**
 * Especialização da tag base PlcCaixaMarcacaoTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza um campo com opção de marcar e desmarcar para entrada de dados. Indica ou não a exclusão do registro.!
 * @Exemplo <plcf:caixaExclusao id="tipoFuncionario_indExcPlc" />!
 * @Tag caixaExclusao!
 */

public class PlcExclusionBoxTag extends PlcSelectionBoxTag {

	/*
	 * Propriedades nao mapeadas
	 */
	private ValueExpression valorChave;
	
	/**
	 * Recupera qual é o componente associado a esta tag
	 */	
	@Override
	public String getComponentType() {
		return PlcExclusionBox.COMPONENT_TYPE;
	}
	
	/**
	 *  Recupera qual é o renderer associado a esta tag
	 */
	@Override
	public String getRendererType() {
		return PlcSelectionBoxRenderer.RENDERER_TYPE;
	}
	
	/**
	 *  Registrando valores para propriedades específicas da tag
	 */
	@Override
	protected void setProperties(FacesBean bean) {
			

		this.setAjudaChave("ajuda.indExcPlc");

		super.setProperties(bean);
		
		PlcExclusionBoxAdapter.getInstance().adapter(bean, valorChave);

	}
	
	public void setValorChave(ValueExpression valorChave) {
		this.valorChave = valorChave;
	}

	
}
