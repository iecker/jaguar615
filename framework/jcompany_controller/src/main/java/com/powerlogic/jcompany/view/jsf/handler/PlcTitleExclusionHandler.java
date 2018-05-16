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

import com.powerlogic.jcompany.view.jsf.adapter.PlcTitleExclusionAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcTitle;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;

/**
 * Especialização da tag base PlcTituloTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza uma coluna de título para caixas de exclusão.!
 * @Exemplo <plcf:tituloExclusao/>!
 * @Tag tituloExclusao!
 */
public class PlcTitleExclusionHandler extends PlcTitleHandler{
	
	private TagAttribute caminhoImagem;
	
	public PlcTitleExclusionHandler(ComponentConfig config) {
		super(config);
		
		caminhoImagem = getAttribute("caminhoImagem");
		
	}

	
	@Override
	public void setAttributes(FaceletContext ctx, Object instance) {
		
		super.setAttributes(ctx, instance);
		
		String _caminhoImagem = (String)PlcTagUtil.getValue(caminhoImagem, ctx);
		
		FacesBean bean = ((PlcTitle)instance).getFacesBean();
		
		PlcTitleExclusionAdapter.getInstance().adapter(bean, _caminhoImagem);
		
	}
	
	@Override
	public void applyNextHandler(FaceletContext ctx, UIComponent c) throws IOException, FacesException, ELException {
		// forçando a regeração do clientBehaviour.
		c.setParent(null);
		super.applyNextHandler(ctx, c);
	}
		
}
