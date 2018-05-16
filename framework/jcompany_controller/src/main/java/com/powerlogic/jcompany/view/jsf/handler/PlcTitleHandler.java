/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.handler;

import static com.powerlogic.jcompany.view.jsf.util.PlcTagUtil.getValue;
import static com.powerlogic.jcompany.view.jsf.util.PlcTagUtil.getValueExpression;

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
import org.apache.myfaces.trinidadinternal.facelets.TrinidadComponentHandler;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.view.jsf.adapter.PlcTitleAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcTitle;

/**
 * Especialização da tag base CoreOutputLabelTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza o texto referente ao título na tela.!
 * @Exemplo <plcf:titulo tituloChave="label.descricao"/>!
 * @Tag titulo!
 */
public class PlcTitleHandler extends TrinidadComponentHandler {

	private final TagAttribute titulo;
	private final TagAttribute colunas;
	private final TagAttribute classeCSS;
	private final TagAttribute tituloChave;
	private final TagAttribute bundle;
	private final TagAttribute alias;
	private final TagAttribute propOrdenacao;
	private final TagAttribute ordem;
	protected TagAttribute riaUsa;
	
	private FaceletContext ctx;
	
	public PlcTitleHandler(ComponentConfig config) {
		
		super(config);

		titulo = getAttribute("titulo");
		colunas   = getAttribute("colunas");
		classeCSS = getAttribute("classeCSS");
		tituloChave   = getAttribute("tituloChave");
		bundle = getAttribute("bundle");
		alias   = getAttribute("alias");
		propOrdenacao = getAttribute("propOrdenacao");
		ordem   = getAttribute("ordem");
		riaUsa = getAttribute("riaUsa");
		
	}

	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);
	
	public void setAttributes(FaceletContext ctx, Object instance) {

		this.ctx = ctx;
		
		super.setAttributes(ctx, instance);
		
		FacesBean bean = ((PlcTitle)instance).getFacesBean();
		
		ValueExpression _titulo = getValueExpression(this.titulo, ctx, String.class);
		
		String _colunas = (String)getValue(this.colunas,ctx);
		String _classeCSS = (getValue(this.classeCSS,ctx) == null ? "plc-title" : ((String)getValue(this.classeCSS,ctx) + " plc-title"));
		String _tituloChave = (String)getValue(this.tituloChave,ctx);
		String _bundle = (String)getValue(this.bundle,ctx);
		String _alias = (String)getValue(this.alias,ctx);
		String _propOrdenacao = (String)getValue(this.propOrdenacao,ctx);
		String _ordem = (String)getValue(this.ordem,ctx);
		String _riaUsa = (String)getValue(this.riaUsa,ctx);
				
		PlcTitleAdapter.getInstance().adapter(bean, _titulo, _colunas, _classeCSS, _tituloChave, _bundle, _alias, _propOrdenacao, _ordem, _riaUsa);
		
	}
	
		@Override
		public void applyNextHandler(FaceletContext ctx, UIComponent c) throws IOException, FacesException, ELException {
			// forçando a regeração do clientBehaviour.
			c.setParent(null);
			super.applyNextHandler(ctx, c);
		}
	
}
