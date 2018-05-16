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
import javax.faces.component.EditableValueHolder;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.PropertyKey;
import org.apache.myfaces.trinidad.component.core.input.CoreInputHidden;

import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.view.jsf.renderer.PlcHiddenRenderer;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;

/**
 * Especialização do componente base CoreInputHidden para permitir IoC e DI nos componentes JSF/Trinidad. 
 * 
 */
public class PlcHidden extends CoreInputHidden {

	/*
	 * Define o Tipo a Família para o componente 
	 */
	static public final String			COMPONENT_FAMILY			= "com.powerlogic.jsf.componente.entrada";

	static public final String			COMPONENT_TYPE				= "com.powerlogic.jsf.componente.PlcEscondido";

	static public final FacesBean.Type	TYPE						= new FacesBean.Type(CoreInputHidden.TYPE);

	static public final PropertyKey		RENDERED_EXPRESSION			= TYPE.registerKey("renderedExpression", String.class);

	/*
	 * Registra novas propriedades para o componente 
	 */
	static public final PropertyKey		COMPONENTE_DESPREZAR_KEY	= TYPE.registerKey("componenteDesprezar", EditableValueHolder.class);

	static {
		//Registra o tipo do componente
		TYPE.lockAndRegister(COMPONENT_TYPE, PlcHiddenRenderer.RENDERER_TYPE);
	}

	public PlcHidden() {

		this(PlcHiddenRenderer.RENDERER_TYPE);
	}

	public PlcHidden(String rendererType) {

		super(rendererType);
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
	 * Método reescrito para alterar a validação padrão dos componentes do trinidad. 
	 * Em caso do obrigatório redefini mensagens de obrigatoriedade para o componente, 
	 * pois o trinidad busca as mensagens em arquivos de resources internos, e o padrão 
	 * jcompany e buscar nos bundles da aplicação.
	 * 
	 * Também verifica se é para validar ou não determinado componente.
	 */
	@Override
	protected void validateValue(FacesContext context, Object newValue) {

		PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		ServletContext servletContext = contextUtil.getApplicationContext();

		String client = servletContext.getInitParameter("org.apache.myfaces.trinidad.CLIENT_STATE_METHOD");

		if ("all".equals(client)) {
			if (!isValid() || !componentUtil.avoidValidationByDespiseField(this.getFacesBean(), COMPONENTE_DESPREZAR_KEY, getClientId(context))) {

				if (isEmpty(newValue)) {
					contextUtil.setRequestAttribute(PlcJsfConstantes.VALIDACAO.EVITA_VALIDACAO_TABULAR, true);
				} else {
					contextUtil.setRequestAttribute(PlcJsfConstantes.VALIDACAO.EVITA_VALIDACAO_TABULAR, false);
				}

				return;
			}

			if (componentUtil.avoidValidationTabular()) {
				return;
			} else {
				// If our value is empty, only check the required property
				if (isEmpty(newValue)) {
					if (isRequired()) {
						componentUtil.handleRequiredComponent(context, this);
					}
				} else {
					super.validateValue(context, newValue);
				}
			}
		} else {
			if (!isValid() || componentUtil.avoidValidationByDespiseField(this.getFacesBean(), COMPONENTE_DESPREZAR_KEY, getClientId(context))) {

				return;
			}
			// If our value is empty, only check the required property
			if (isEmpty(newValue)) {
				if (isRequired()) {
					componentUtil.handleRequiredComponent(context, this);
				}
			} else {
				super.validateValue(context, newValue);
			}
		}
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
