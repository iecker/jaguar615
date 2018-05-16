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

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidadinternal.facelets.TrinidadComponentHandler;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.view.jsf.component.PlcPanelFormLayout;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;

/**
 * Especialização da tag base HtmlTableLayoutTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza tags HTML de uma tabela que envolve os componentes de um tela.!
 * @Exemplo <plcf:tabela tituloChave="consultafuncionariocon.titulo">!
 * @Tag tabela!
 */
public class PlcPanelFormLayoutHandler extends TrinidadComponentHandler{
	
	protected TagAttribute aoSair;
	
	public PlcPanelFormLayoutHandler(ComponentConfig config) {
		super(config);
		
		aoSair = getAttribute("aoSair");
		
	}

	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	/*private String classeCSS;
	private String tituloChave;
	private String titulo;
	private String fragmento;
	private String classeTituloCSS;
	// Arquivo de Resources. Se não foi informado o padrão é ApplicationResources
	protected String bundle;
	protected String aoSair;*/

	@Override
	public void setAttributes(FaceletContext ctx, Object instance) {

		super.setAttributes(ctx, instance);
		
		String _aoSair = (String)PlcTagUtil.getValue(aoSair, ctx);
		FacesBean bean = ((PlcPanelFormLayout)instance).getFacesBean();
		bean.setProperty(PlcPanelFormLayout.AOSAIR_KEY, _aoSair);


	}
	
	
	/**
	 *  Registrando valores para propriedades específicas da tag
	 */
	/*@Override
	protected void setProperties(FacesBean bean) {
		
		

		// Trinidad
		super.setProperties(bean);
		
		
		
	   


	}*/

	@Override
	public void applyNextHandler(FaceletContext ctx, UIComponent c) throws IOException, FacesException, ELException {
		// forçando a regeração do clientBehaviour.
		c.setParent(null);
		super.applyNextHandler(ctx, c);
	}

}
