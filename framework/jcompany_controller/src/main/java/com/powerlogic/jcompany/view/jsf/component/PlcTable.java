/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.component;

import java.io.IOException;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.PropertyKey;
import org.apache.myfaces.trinidad.component.html.HtmlTableLayout;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.view.jsf.renderer.PlcTableRenderer;

/**
 * Especialização do componente base HtmlTableLayout para permitir IoC e DI nos componentes JSF/Trinidad. 
 * 
 */
public class PlcTable extends HtmlTableLayout {

	protected static final Logger		logVisao				= Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	protected static final Logger		log						= Logger.getLogger(PlcTable.class.getCanonicalName());

	/*
	 * Define o Tipo a Família para o componente 
	 */
	static public final String			COMPONENT_FAMILY		= "com.powerlogic.jsf.componente.tabela";

	static public final String			COMPONENT_TYPE			= "com.powerlogic.jsf.componente.PlcTabela";

	static public final FacesBean.Type	TYPE					= new FacesBean.Type(HtmlTableLayout.TYPE);

	/*
	 * Registra novas propriedades para o componente 
	 */

	static public final PropertyKey		TITULO_KEY				= TYPE.registerKey("titulo", String.class);

	static public final PropertyKey		FRAGMENTO_KEY			= TYPE.registerKey("fragmento", String.class);

	static public final PropertyKey		CLASSE_TITULO_CSS_KEY	= TYPE.registerKey("classeTituloCSS", String.class);

	static public final PropertyKey		BUNDLE					= TYPE.registerKey("bundle", String.class);

	static public final PropertyKey		RENDERED_EXPRESSION		= TYPE.registerKey("renderedExpression", String.class);

	public static final PropertyKey		USA_FIELDSET			= TYPE.registerKey("usaFieldset", String.class);

	static {
		//Registra o tipo do componente
		TYPE.lockAndRegister(COMPONENT_TYPE, PlcTableRenderer.RENDERER_TYPE);
	}

	public PlcTable() {

		super(PlcTableRenderer.RENDERER_TYPE);
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
	 * Renderiza título da tabelas se for declarado, e não for layout de tabFolder ou assistente apresentando detalhes.
	 */
	@Override
	public void encodeBegin(FacesContext context) throws IOException {

		super.encodeBegin(context);

	}

	/**
	 * Renderiza título da tabelas se for declarado, e não for layout de tabFolder ou assistente apresentando detalhes.
	 */
	@Override
	public void encodeEnd(FacesContext context) throws IOException {

		ResponseWriter writer = context.getResponseWriter();

		super.encodeEnd(context);

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
