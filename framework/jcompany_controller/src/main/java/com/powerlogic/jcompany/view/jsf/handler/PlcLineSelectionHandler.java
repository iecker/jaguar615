/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.handler;

import java.io.IOException;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;

import org.apache.myfaces.trinidad.bean.FacesBean;

import com.powerlogic.jcompany.view.jsf.adapter.PlcLineSelectionAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcLineSelection;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;

/**
 * Especialização da tag base PlcLinhaTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza tags html para início de uma linha de uma tabela com estilo de linha de seleção (com link).!
 * @Exemplo <plcf:linhaSelecao linkEdicao="#{plcLogicaItens.itensPlc.linkEdicaoPlc}" propAgregada="cargoAtual">!
 * @Tag linhaSelecao!
 */
public class PlcLineSelectionHandler extends PlcLineHandler {
	
	private TagAttribute action;
	private TagAttribute linkEdicao;
	private TagAttribute linkEdicaoCT;
	private TagAttribute propAgregada;
	private TagAttribute propAgregadaCampo;
	private TagAttribute redirectModal;
	private TagAttribute titleModal;
	
	public PlcLineSelectionHandler(ComponentConfig config) {
		super(config);
		
		action = getAttribute("action");
		linkEdicao = getAttribute("linkEdicao");
		linkEdicaoCT = getAttribute("linkEdicaoCT");
		propAgregada = getAttribute("propAgregada");
		propAgregadaCampo = getAttribute("propAgregadaCampo");
		redirectModal = getAttribute("redirectModal");
		titleModal = getAttribute("titleModal");
		
		
	}

	/*private String action;
	private ValueExpression linkEdicao;
	private ValueExpression linkEdicaoCT;
	private ValueExpression propAgregada;
	private ValueExpression propAgregadaCampo;
	private ValueExpression redirectModal;
	private ValueExpression titleModal;*/

	@Override
	public void setAttributes(FaceletContext ctx, Object instance) {
		
		super.setAttributes(ctx, instance);

		FacesBean bean = ((PlcLineSelection)instance).getFacesBean();

		String _action = (String)PlcTagUtil.getValue(this.action, ctx);
		ValueExpression _linkEdicao = PlcTagUtil.getValueExpression(this.linkEdicao, ctx, String.class);
		ValueExpression _linkEdicaoCT= PlcTagUtil.getValueExpression(this.linkEdicaoCT, ctx, String.class);
		ValueExpression _propAgregada= PlcTagUtil.getValueExpression(this.propAgregada, ctx, String.class);
		ValueExpression _propAgregadaCampo= PlcTagUtil.getValueExpression(this.propAgregadaCampo, ctx, String.class);
		ValueExpression _redirectModal= PlcTagUtil.getValueExpression(this.redirectModal, ctx, String.class);
		ValueExpression _titleModal= PlcTagUtil.getValueExpression(this.titleModal, ctx, String.class);

		PlcLineSelectionAdapter.getInstance().adapter(bean, _action, _linkEdicao, _linkEdicaoCT, _propAgregada, _propAgregadaCampo, _redirectModal, _titleModal);
		
		
	}
	
	@Override
	public void applyNextHandler(FaceletContext ctx, UIComponent c) throws IOException, FacesException, ELException {
		// forçando a regeração do clientBehaviour.
		c.setParent(null);
		super.applyNextHandler(ctx, c);
	}

}
