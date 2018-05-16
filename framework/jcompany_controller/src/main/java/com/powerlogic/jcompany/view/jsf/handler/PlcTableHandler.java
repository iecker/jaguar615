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
import com.powerlogic.jcompany.view.jsf.adapter.PlcTableAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcTable;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;

/**
 * Especialização da tag base HtmlTableLayoutTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza tags HTML de uma tabela que envolve os componentes de um tela.!
 * @Exemplo <plcf:tabela tituloChave="consultafuncionariocon.titulo">!
 * @Tag tabela!
 */
public class PlcTableHandler extends TrinidadComponentHandler{
	
	private TagAttribute classeCSS;
	private TagAttribute tituloChave;
	private TagAttribute titulo;
	private TagAttribute fragmento;
	private TagAttribute classeTituloCSS;
	protected TagAttribute bundle;
	protected TagAttribute aoSair;
	protected TagAttribute usaFieldset;
	
	public PlcTableHandler(ComponentConfig config) {
		super(config);
		
		classeCSS = getAttribute("classeCSS");
		tituloChave = getAttribute("tituloChave");
		titulo = getAttribute("titulo");
		fragmento = getAttribute("fragmento");
		classeTituloCSS = getAttribute("classeTituloCSS");
		bundle = getAttribute("bundle");
		aoSair = getAttribute("aoSair");
		usaFieldset = getAttribute("usaFieldset");
		
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

		String _classeCSS = (String)PlcTagUtil.getValue(classeCSS, ctx);;
		String _tituloChave = (String)PlcTagUtil.getValue(tituloChave, ctx);
		String _titulo = (String)PlcTagUtil.getValue(titulo, ctx);
		String _fragmento = (String)PlcTagUtil.getValue(fragmento, ctx);
		String _classeTituloCSS = (String)PlcTagUtil.getValue(classeTituloCSS, ctx);
		// Arquivo de Resources. Se não foi informado o padrão é ApplicationResources
		String _bundle = (String)PlcTagUtil.getValue(bundle, ctx);
		String _aoSair = (String)PlcTagUtil.getValue(aoSair, ctx);
		String _usaFieldset = (String)PlcTagUtil.getValue(usaFieldset, ctx);
		
		FacesBean bean = ((PlcTable)instance).getFacesBean();
		
		PlcTableAdapter.getInstance().adapter(bean, _classeCSS, _tituloChave, _titulo, _fragmento, _classeTituloCSS, _bundle, _aoSair, _usaFieldset);

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
