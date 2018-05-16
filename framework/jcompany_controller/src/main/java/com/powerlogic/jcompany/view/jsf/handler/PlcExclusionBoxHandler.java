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

import com.powerlogic.jcompany.view.jsf.adapter.PlcExclusionBoxAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcExclusionBox;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;

/**
 * Especialização da tag base PlcCaixaMarcacaoTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza um campo com opção de marcar e desmarcar para entrada de dados. Indica ou não a exclusão do registro.!
 * @Exemplo <plcf:caixaExclusao id="tipoFuncionario_indExcPlc" />!
 * @Tag caixaExclusao!
 */

public class PlcExclusionBoxHandler extends PlcSelectionBoxHandler {

	private TagAttribute valorChave;
	
	public PlcExclusionBoxHandler(ComponentConfig config) {
		
		super(config);

		valorChave = getAttribute("valorChave");
		
	}

	/*
	 * Propriedades nao mapeadas
	 */
	//private ValueExpression valorChave;
	
	@Override
	public void setAttributes(FaceletContext ctx, Object instance) {

		super.setAttributes(ctx, instance);
		
		FacesBean bean = ((PlcExclusionBox)instance).getFacesBean();
		
		ValueExpression _valorChave = PlcTagUtil.getValueExpression(valorChave, ctx, Object.class);
		
		PlcExclusionBoxAdapter.getInstance().adapter(bean, _valorChave);

	}
	
	/**
	 *  Registrando valores para propriedades específicas da tag
	 *//*
	@Override
	protected void setProperties(FacesBean bean) {
		
		

		this.setAjudaChave("ajuda.indExcPlc");

		super.setProperties(bean);
		
		
		
	   
	}*/
		
	@Override
	public void applyNextHandler(FaceletContext ctx, UIComponent c) throws IOException, FacesException, ELException {
		// forçando a regeração do clientBehaviour.
		c.setParent(null);
		super.applyNextHandler(ctx, c);
	}

	
}
