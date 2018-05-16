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
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;

/**
 * 
 * Especialização da tag base UIXIteratorTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Implementação de iteração para uma coleção.!
 * @Exemplo <plcf:iteracao value="#{plcLogicaItens.itensPlc}">!
 * @Tag iteracao!
 */
public class PlcIterationHandler extends ComponentHandler {

	public PlcIterationHandler(ComponentConfig config) {
		super(config);
	}
	
	@Override
	public void applyNextHandler(FaceletContext ctx, UIComponent c) throws IOException, FacesException, ELException {
		// forçando a regeração do clientBehaviour.
		c.setParent(null);
		super.applyNextHandler(ctx, c);
	}

}
