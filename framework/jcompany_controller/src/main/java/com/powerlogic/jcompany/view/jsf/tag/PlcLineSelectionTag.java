/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.tag;

import javax.el.ValueExpression;

import org.apache.myfaces.trinidad.bean.FacesBean;

import com.powerlogic.jcompany.view.jsf.adapter.PlcLineSelectionAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcLineSelection;
import com.powerlogic.jcompany.view.jsf.renderer.PlcLineSelectionRenderer;

/**
 * Especialização da tag base PlcLinhaTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza tags html para início de uma linha de uma tabela com estilo de linha de seleção (com link).!
 * @Exemplo <plcf:linhaSelecao linkEdicao="#{plcLogicaItens.itensPlc.linkEdicaoPlc}" propAgregada="cargoAtual">!
 * @Tag linhaSelecao!
 */
public class PlcLineSelectionTag extends PlcLineTag {
	
	private String action;
	private ValueExpression linkEdicao;
	private ValueExpression linkEdicaoCT;
	private ValueExpression propAgregada;
	private ValueExpression propAgregadaCampo;
	private ValueExpression redirectModal;
	private ValueExpression titleModal;

	/**
	 * Recupera qual é o componente associado a esta tag
	 */
	@Override
	public String getComponentType() {
		return PlcLineSelection.COMPONENT_TYPE;
	}

	/**
	 *  Recupera qual é o renderer associado a esta tag
	 */
	@Override
	public String getRendererType() {
		return PlcLineSelectionRenderer.RENDERER_TYPE;
	}

	/**
	 *  Registrando valores para propriedades específicas da tag
	 */
	@Override
	protected void setProperties(FacesBean bean) {
		// Genericos Trinidad
		
		//Integer i = Integer.valueOf((String)PlcContextUtil.getInstance().getRequestAttribute(PlcJsfConstantes.PLC_ITENS_LINHA));
		
		//bean.setProperty(PlcLinhaSelecao.ID_KEY, "linhaSel_" + i.toString());
		
		//this.setId("linhaSel_" + i.toString());
		
		super.setProperties(bean);
		
		PlcLineSelectionAdapter.getInstance().adapter(bean, action, linkEdicao, linkEdicaoCT, propAgregada, propAgregadaCampo, redirectModal, titleModal);
		
		
	}
	
	


	public void setAction(String action) {
		this.action = action;
	}

	public void setLinkEdicao(ValueExpression linkEdicao) {
		this.linkEdicao = linkEdicao;
	}

	public void setLinkEdicaoCT(ValueExpression linkEdicaoCT) {
		this.linkEdicaoCT = linkEdicaoCT;
	}

	public void setPropAgregada(ValueExpression propAgregada) {
		this.propAgregada = propAgregada;
	}

	public void setPropAgregadaCampo(ValueExpression propAgregadaCampo) {
		this.propAgregadaCampo = propAgregadaCampo;
	}

	public ValueExpression getRedirectModal() {
		return redirectModal;
	}

	public void setRedirectModal(ValueExpression redirectModal) {
		this.redirectModal = redirectModal;
	}

	public ValueExpression getTitleModal() {
		return titleModal;
	}

	public void setTitleModal(ValueExpression titleModal) {
		this.titleModal = titleModal;
	}

}
