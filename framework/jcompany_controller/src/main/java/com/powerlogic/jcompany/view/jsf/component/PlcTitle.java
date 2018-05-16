/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.component;

import java.io.IOException;
import java.util.Map;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.PropertyKey;
import org.apache.myfaces.trinidad.component.UIXComponentBase;
import org.apache.myfaces.trinidad.component.core.output.CoreOutputLabel;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.view.jsf.renderer.PlcTitleRenderer;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;

/**
 * Especialização do componente base CoreOutputLabel para permitir IoC e DI nos componentes JSF/Trinidad. 
 * 
 */
public class PlcTitle extends CoreOutputLabel {

	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	/*
	 * Define o Tipo a Família para o componente 
	 */
	static public final String			COMPONENT_FAMILY	= "com.powerlogic.jsf.componente.saida";

	static public final String			COMPONENT_TYPE		= "com.powerlogic.jsf.componente.PlcTitulo";

	static public final FacesBean.Type	TYPE				= new FacesBean.Type(CoreOutputLabel.TYPE);

	/*
	 * Registra novas propriedades para o componente 
	 */
	static public final PropertyKey		TITULO_CHAVE_KEY	= TYPE.registerKey("tituloChave", String.class);

	static public final PropertyKey		PROP_ORDENACAO		= TYPE.registerKey("propOrdenacao", String.class);

	static public final PropertyKey		ALIAS				= TYPE.registerKey("alias", String.class);

	static public final PropertyKey		CAMINHO_IMAGEM_KEY	= TYPE.registerKey("caminhoImagem", String.class);

	static public final PropertyKey		ORDEM				= TYPE.registerKey("ordem", String.class);

	static public final PropertyKey		BUNDLE				= TYPE.registerKey("bundle", String.class);

	static public final PropertyKey		PROPRIEDADE_KEY		= TYPE.registerKey("propriedade", String.class);

	static public final PropertyKey		ENUM_I18N			= TYPE.registerKey("enumI18n", Boolean.class);

	static public final PropertyKey		RENDERED_EXPRESSION	= TYPE.registerKey("renderedExpression", String.class);

	static public final PropertyKey		RIA_USA				= TYPE.registerKey("riaUsa", String.class);
	
	static public final PropertyKey		CLASSECSS_KEY		= TYPE.registerKey("classeCSS", String.class);

	static {
		//Registra o tipo do componente
		TYPE.lockAndRegister(COMPONENT_TYPE, PlcTitleRenderer.RENDERER_TYPE);
	}

	@Override
	public String getFamily() {

		return COMPONENT_FAMILY;
	}

	@Override
	protected FacesBean.Type getBeanType() {

		return TYPE;
	}

	/**
	 * Implementado segurança na tag. Utilizado para verificar se o campo informado irá ser rederizado ou não
	 */
	@Override
	public void encodeAll(FacesContext context) throws IOException {

		// Tratamento especial para quando é exibida uma entidade, mas as propriedades lookups estão vazias		

		PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		String tituloChave = (String) getProperty(PlcTitle.TITULO_CHAVE_KEY);

		componentUtil.checkSecurity(getFacesBean(), UIXComponentBase.RENDERED_KEY, null, tituloChave);

		super.encodeAll(context);

	}

	@Override
	public void encodeBegin(FacesContext context) throws IOException {

		String propriedade = (String) getProperty(PlcTitle.TITULO_CHAVE_KEY);

		if (propriedade != null && propriedade.contains("idNatural."))
			propriedade = propriedade.replace("idNatural.", "idNatural_");

		Map<String, Boolean> m = (Map<String, Boolean>) context.getExternalContext().getRequestMap().get(PlcConstants.GUI.MAPA_SEGURANCA);

		if (m != null && m.containsKey(propriedade)) {
			getFacesBean().setProperty(UIXComponentBase.RENDERED_KEY, false);
		}

		super.encodeBegin(context);

	}

	@Override
	public boolean isRendered() {

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		String renderedExpression = (String) getProperty(RENDERED_EXPRESSION);

		ServletContext servletContext = contextUtil.getApplicationContext();
		String client = servletContext.getInitParameter("org.apache.myfaces.trinidad.CLIENT_STATE_METHOD");

		if (renderedExpression != null && "all".equals(client)) {
			PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
			this.setRendered(elUtil.evaluateExpressionGet(renderedExpression, Boolean.class));
		}

		return super.isRendered();
	}

	@Override
	public void setValueExpression(String name, ValueExpression expression) {

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		ServletContext servletContext = contextUtil.getApplicationContext();

		String client = servletContext.getInitParameter("org.apache.myfaces.trinidad.CLIENT_STATE_METHOD");
		
		//Verifica segurança para label 
		if(expression.getExpressionString()!=null){
			
			PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);	
			
			Boolean notLabel = componentUtil.checkSecurityLabel(expression.getExpressionString());
			
			if(notLabel){
				expression = null;
			}
		}			
		
		if ("rendered".equals(name) && expression != null && "all".equals(client)) {

			String expressionString = expression.getExpressionString();

			if (expression.isLiteralText()) {
				try {
					this.setRendered(Boolean.valueOf(expressionString).booleanValue());
				} catch (ELException e) {
					throw new FacesException(e);
				}
			} else {
				PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
				this.setRendered(elUtil.evaluateExpressionGet(expressionString, Boolean.class));
				this.setProperty(RENDERED_EXPRESSION, expressionString);
			}
		} else
			super.setValueExpression(name, expression);
	}

}
