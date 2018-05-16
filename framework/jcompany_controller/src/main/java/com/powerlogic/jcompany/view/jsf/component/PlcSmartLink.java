/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.component;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.servlet.ServletContext;

import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.PropertyKey;
import org.apache.myfaces.trinidad.component.core.output.CoreOutputLabel;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;

/**
 * Especialização do componente base CoreOutputLabel para permitir IoC e DI nos componentes JSF/Trinidad. 
 */
public class PlcSmartLink extends CoreOutputLabel {

	static public final String			COMPONENT_FAMILY	= "com.powerlogic.jsf.componente.saida";

	static public final String			COMPONENT_TYPE		= "com.powerlogic.jsf.componente.PlcLinkInteligente";

	static public final String			RENDERER_TYPE		= "com.powerlogic.jsf.LinkInteligente";

	static public final FacesBean.Type	TYPE				= new FacesBean.Type(CoreOutputLabel.TYPE);

	/*
	 * Registra novas propriedades para o componente 
	 */
	static public final PropertyKey		BUNDLE_KEY			= TYPE.registerKey("bundle", String.class);

	static public final PropertyKey		LINK_KEY			= TYPE.registerKey("link", String.class);

	static public final PropertyKey		ALVO_KEY			= TYPE.registerKey("alvo", String.class);

	static public final PropertyKey		TITULO_KEY			= TYPE.registerKey("titulo", String.class);

	static public final PropertyKey		TITULO_CHAVE_KEY	= TYPE.registerKey("tituloChave", String.class);

	static public final PropertyKey		AJUDA_KEY			= TYPE.registerKey("ajuda", String.class);

	static public final PropertyKey		AJUDA_CHAVE_KEY		= TYPE.registerKey("ajudaChave", String.class);

	static public final PropertyKey		EXIBESE_KEY			= TYPE.registerKey("exibeSe", String.class);

	static public final PropertyKey		CLASSECSS_KEY		= TYPE.registerKey("classeCSS", String.class);

	static public final PropertyKey		RENDERED_EXPRESSION	= TYPE.registerKey("renderedExpression", String.class);

	static public final PropertyKey		RIA_USA				= TYPE.registerKey("riaUsa", String.class);

	@Override
	protected FacesBean.Type getBeanType() {

		return TYPE;
	}

	@Override
	public String getFamily() {

		return COMPONENT_FAMILY;
	}

	@Override
	public boolean isRendered() {

		String renderedExpression = (String) getProperty(RENDERED_EXPRESSION);

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

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
