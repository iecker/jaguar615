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

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidadinternal.facelets.TrinidadComponentHandler;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.view.jsf.adapter.PlcLineAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcLine;

/**
 * Especialização da tag base HtmlRowLayoutTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza corpo entre linhas HTML.!
 * @Exemplo <plcf:linha>conteudo</plcf:linha>!
 * @Tag linha!
 */
public class PlcLineHandler extends TrinidadComponentHandler{

	public PlcLineHandler(ComponentConfig config) {
		super(config);
	}

	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);


	@Override
	public void setAttributes(FaceletContext ctx, Object instance) {

		super.setAttributes(ctx, instance);

		FacesBean bean = ((PlcLine)instance).getFacesBean();

		PlcLineAdapter.getInstance().adapter(bean);

	}
	
	@Override
	public void applyNextHandler(FaceletContext ctx, UIComponent c) throws IOException, FacesException, ELException {
		// forçando a regeração do clientBehaviour.
		c.setParent(null);
		super.applyNextHandler(ctx, c);
	}

}

