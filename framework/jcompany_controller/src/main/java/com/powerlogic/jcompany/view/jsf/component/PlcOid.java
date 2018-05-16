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
import javax.servlet.ServletContext;

import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.PropertyKey;
import org.apache.myfaces.trinidad.component.UIXValue;
import org.apache.myfaces.trinidad.component.core.input.CoreInputText;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.view.jsf.renderer.PlcOidRenderer;

/**
 * Especialização do componente base PlcTexto para permitir IoC e DI nos componentes JSF/Trinidad. 
 * 
 */
public class PlcOid extends PlcText {

	/*
	 * Define o Tipo a Família para o componente 
	 */
	static public final String			COMPONENT_FAMILY		= "com.powerlogic.jsf.componente.entrada";

	static public final String			COMPONENT_TYPE			= "com.powerlogic.jsf.componente.PlcOid";

	static public final FacesBean.Type	TYPE					= new FacesBean.Type(PlcText.TYPE);

	static public final PropertyKey		AUTO_RECUPERACAO_KEY	= TYPE.registerKey("autoRecuperacao", String.class);

	static public final PropertyKey		EXIBE_SE_KEY_OID		= TYPE.registerKey("exibeSeOid", Boolean.class);

	static {
		//Registra o tipo do componente
		TYPE.lockAndRegister(COMPONENT_TYPE, PlcOidRenderer.RENDERER_TYPE);
	}

	@Override
	protected FacesBean.Type getBeanType() {

		return TYPE;
	}

	@Override
	public String getFamily() {

		return COMPONENT_FAMILY;
	}

	/**
	 * Implementado segurança na tag. Utilizado para verificar se o campo informado irá ser rederizado ou não
	 */
	@Override
	public void encodeBegin(FacesContext context) throws IOException {

		String autoRecuperacao = (String) getFacesBean().getProperty(PlcOid.AUTO_RECUPERACAO_KEY);

		if (autoRecuperacao != null && autoRecuperacao.equals("N")) {
			if (getFacesBean().getProperty(UIXValue.VALUE_KEY) != null) {
				setBooleanProperty(CoreInputText.READ_ONLY_KEY, false);
			} else {
				setBooleanProperty(CoreInputText.READ_ONLY_KEY, true);
			}
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
