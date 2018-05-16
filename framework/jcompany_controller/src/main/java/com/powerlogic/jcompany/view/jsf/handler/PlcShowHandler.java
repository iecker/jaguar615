/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.handler;

import static com.powerlogic.jcompany.view.jsf.util.PlcTagUtil.getValue;

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
import com.powerlogic.jcompany.view.jsf.adapter.PlcShowAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcTitle;

/**
 * Especialização da tag base CoreOutputLabelTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza o texto referente ao título na tela.!
 * @Exemplo <plcf:titulo tituloChave="label.descricao"/>!
 * @Tag titulo!
 */
public class PlcShowHandler extends TrinidadComponentHandler {

	
	protected final TagAttribute chavePrimaria;
	// Arquivo de Resources. Se não foi informado o padrão é ApplicationResources
	protected final TagAttribute bundle;
	protected final TagAttribute classeCSS;
	protected final TagAttribute enumI18n;
	protected final TagAttribute riaUsa;
	
	private FaceletContext ctx;
	
	public PlcShowHandler(ComponentConfig config) {
		
		super(config);
		
		chavePrimaria = getAttribute("chavePrimaria");
		bundle = getAttribute("bundle");
		enumI18n = getAttribute("enumI18n");
		riaUsa = getAttribute("riaUsa");
		classeCSS = getAttribute("classeCSS");
		
	}

	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	public void setAttributes(FaceletContext ctx, Object instance) {
		
		this.ctx = ctx;
		
		super.setAttributes(ctx, instance);

		FacesBean bean = ((PlcTitle)instance).getFacesBean();
		
		String _chavePrimaria = (String)getValue(chavePrimaria, ctx);
		String _bundle = (String)getValue(bundle, ctx);
		String _enumI18n = (String)getValue(enumI18n, ctx);
		String _riaUsa = (String)getValue(riaUsa, ctx);
		String _classeCSS = (getValue(this.classeCSS,ctx) == null ? "plc-show" : ((String)getValue(this.classeCSS,ctx) + " plc-show"));
		
		PlcShowAdapter.getInstance().adapter(bean, _chavePrimaria, _bundle, _enumI18n, _riaUsa, _classeCSS);
		
	}
	
	@Override
	public void applyNextHandler(FaceletContext ctx, UIComponent c) throws IOException, FacesException, ELException {
		// forçando a regeração do clientBehaviour.
		c.setParent(null);
		super.applyNextHandler(ctx, c);
	}

}
