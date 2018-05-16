/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.handler;

import java.io.IOException;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;

import org.apache.myfaces.trinidad.bean.FacesBean;

import com.powerlogic.jcompany.view.jsf.component.PlcArea;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;

/**
 * Especialização da tag base PlcTextoTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza um campo com várias linhas e colunas para entrada de dados.!
 * @Exemplo <plcf:area id="obs" value="#{plcEntidade.obs}" ajudaChave="ajuda.obs"  tamanho="80" numLinhas="3" />!
 * @Tag area!
 */
public class PlcAreaHandler extends PlcTextHandler {

	private TagAttribute numLinhas;
	
	public PlcAreaHandler(ComponentConfig config) {
		super(config);
		
		numLinhas = getAttribute("numLinhas");
		
	}
	
	@Override
	public void setAttributes(FaceletContext ctx, Object instance) {
		
		super.tamanho = super.colunas;
		super.setAttributes(ctx, instance);
		
		FacesBean bean = ((PlcArea)instance).getFacesBean();
		
		String _numLinhas = (String)PlcTagUtil.getValue(this.numLinhas, ctx);
		if (_numLinhas != null)
			bean.setProperty(PlcArea.ROWS_KEY, new Long(_numLinhas));

	}
	
	@Override
	public void applyNextHandler(FaceletContext ctx, UIComponent c) throws IOException, FacesException, ELException {
		// forçando a regeração do clientBehaviour.
		c.setParent(null);
		super.applyNextHandler(ctx, c);
	}

}
