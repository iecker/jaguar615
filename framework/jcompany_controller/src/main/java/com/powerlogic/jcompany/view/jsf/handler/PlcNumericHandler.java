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

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.view.jsf.adapter.PlcNumericAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcNumeric;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;

/**
 * Especialização da tag base PlcTextoTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza um campo com um linha para entrada de dados no formato monetário.!
 * @Exemplo <plcf:numerico value="#{plcEntidade.salarioMaximo}"  ajudaChave="ajuda.salarioMaximo" />!
 * @Tag numerico!
 */
public class PlcNumericHandler extends PlcTextHandler {

	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);
	
	private TagAttribute numCasas;
	
	public PlcNumericHandler(ComponentConfig config) {
		
		super(config);
		
		numCasas = getAttribute("numCasas");
		
	}

	
	@Override
	public void setAttributes(FaceletContext ctx, Object instance) {
		
		super.setAttributes(ctx, instance);
	
		FacesBean bean = ((PlcNumeric)instance).getFacesBean();
		
		ValueExpression _numCasas = PlcTagUtil.getValueExpression(this.numCasas, ctx, String.class);
		
		PlcNumericAdapter.getInstance().adapter(bean, _numCasas);
		
	}
	
	@Override
	public void applyNextHandler(FaceletContext ctx, UIComponent c) throws IOException, FacesException, ELException {
		// forçando a regeração do clientBehaviour.
		c.setParent(null);
		super.applyNextHandler(ctx, c);
	}
	
}
