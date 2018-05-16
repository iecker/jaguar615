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
import javax.faces.component.EditableValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.el.ValueBinding;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.PropertyKey;
import org.apache.myfaces.trinidad.bean.ValueExpressionValueBinding;
import org.apache.myfaces.trinidad.component.UIXComponentBase;
import org.apache.myfaces.trinidad.component.UIXSelectBoolean;
import org.apache.myfaces.trinidad.component.UIXValue;
import org.apache.myfaces.trinidad.component.core.input.CoreSelectBooleanCheckbox;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcViewJsfUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;
import com.powerlogic.jcompany.domain.type.PlcYesNo;
import com.powerlogic.jcompany.view.jsf.renderer.PlcSelectionBoxRenderer;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcMsgComponentsUtil;

/**
 * Especialização do componente base CoreSelectBooleanCheckbox para permitir IoC e DI nos componentes JSF/Trinidad. 
 * 
 */
public class PlcSelectionBox extends CoreSelectBooleanCheckbox implements IPlcComponent {

	protected static final Logger		logVisao					= Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	/*
	 * Define o Tipo a Família para o componente 
	 */
	static public final String			COMPONENT_FAMILY			= "com.powerlogic.jsf.componente.entrada";

	static public final String			COMPONENT_TYPE				= "com.powerlogic.jsf.componente.PlcCaixaMarcacao";

	static public final FacesBean.Type	TYPE						= new FacesBean.Type(CoreSelectBooleanCheckbox.TYPE);

	/*
	 * Registra novas propriedades para o componente 
	 */
	static public final PropertyKey		COMPONENTE_DESPREZAR_KEY	= TYPE.registerKey("componenteDesprezar", EditableValueHolder.class);

	static public final PropertyKey		CHAVE_I18N_DETALHE_KEY		= TYPE.registerKey("chaveI18nDetalhe", String.class);

	static public final PropertyKey		PROPRIEDADE_KEY				= TYPE.registerKey("propriedade", String.class);

	static public final PropertyKey		BUNDLE						= TYPE.registerKey("bundle", String.class);

	static public final PropertyKey		RENDERED_EXPRESSION			= TYPE.registerKey("renderedExpression", String.class);

	static public final PropertyKey 	VALOR_MARCADO 				= TYPE.registerKey("valorMarcado", String.class);
	
	static public final PropertyKey 	VALOR_DESMARCADO 			= TYPE.registerKey("valorDesmarcado", String.class);
	
	static public final PropertyKey		RIA_USA						= TYPE.registerKey("riaUsa", String.class);
	
	static public final PropertyKey		SOMENTE_LEITURA				= TYPE.registerKey("somenteLeitura", String.class);

	static {
		//Registra o tipo do componente
		TYPE.lockAndRegister(COMPONENT_TYPE, PlcSelectionBoxRenderer.RENDERER_TYPE);
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
	public void encodeBegin(FacesContext context) throws IOException {

		PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		try {
			
			PlcViewJsfUtil viewJsfUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcViewJsfUtil.class, QPlcDefaultLiteral.INSTANCE);
			//verificando se esta disabled.
			setDisabled(viewJsfUtil.isReadOnly()||viewJsfUtil.textMode());
			
			//Object valor = getFacesBean().getValueExpression(UIXValue.VALUE_KEY).getValue(getFacesContext().getELContext());
			Object valor = getFacesBean().getProperty(PlcSelectionBox.VALUE_KEY);
			
			if (!(valor instanceof Boolean)) {
				setBooleanProperty(UIXSelectBoolean.SELECTED_KEY, "S".equals(valor) || PlcYesNo.S.equals(valor) || "true".equals(valor) || (getFacesBean().getProperty(VALOR_MARCADO) != null && valor != null ? getFacesBean().getProperty(VALOR_MARCADO).toString().equalsIgnoreCase(valor.toString()):false));
			}

			String propriedade = (String) getProperty(PlcSelectionBox.PROPRIEDADE_KEY);
			// Registra nome detalhe se estiver neste contexto
			String detalhe = componentUtil.getCurrentDetail();

			if (detalhe == null) {
				// Caso não achou detalhe, verifica pelo id da tag
				detalhe = componentUtil.findDetail(context, this);
			}

			componentUtil.checkSecurityDetail(getFacesBean(), detalhe, context);
			componentUtil.checkSecurity(getFacesBean(), UIXComponentBase.RENDERED_KEY, detalhe, propriedade);

			super.encodeBegin(context);

		} catch (PlcException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Faz uma conversão do valor para PlcSimNao
	 * TODO - Verificar como fazer para tratar outros tipos: Usando valueBinding.getType(...) ou um Converter JSF.
	 */
	@Override
	public Object getLocalValue() {

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcConfigUtil configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcURLUtil urlUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcURLUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcMsgComponentsUtil msgComponentsUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgComponentsUtil.class, QPlcDefaultLiteral.INSTANCE);

		Object valor = super.getLocalValue(); //getFacesBean().getProperty(UIXValue.VALUE_KEY);

		ValueExpression valueExpr = getFacesBean().getValueExpression(UIXValue.VALUE_KEY);

		if (valor != null) {
			Class classe = valueExpr.getType(getFacesContext().getELContext());
			if (classe.getName().contains("String") && valor.getClass().getName().contains("Boolean")) {
				if (new Boolean(valor.toString()).booleanValue())
					return "S";
				else
					return "N";
			} else if (classe.getName().contains("PlcYesNo") && valor.getClass().getName().contains("Boolean")) {
				if (new Boolean(valor.toString()).booleanValue())
					return PlcYesNo.S;
				else
					return PlcYesNo.N;
			} else if (classe.getName().contains("String") && valor.getClass().getName().contains("PlcYesNo")) {
				return valor.toString();
			} else if (classe.getName().contains("PlcYesNo") && valor.getClass().getName().contains("String")) {
				if (valor.equals("S"))
					return PlcYesNo.S;
				else
					return PlcYesNo.N;
			} else
				return valor;
		}

		return super.getLocalValue();

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

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);
		
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

	/**
	 * Faz uma conversão do valor para PlcSimNao ou para String
	 * TODO - Verificar como fazer para tratar outros tipos: Usando valueBinding.getType(...) ou um Converter JSF.
	 */
	@Override
	protected Object getConvertedValue(FacesContext context, Object submittedValue) throws ConverterException {

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcConfigUtil configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcURLUtil urlUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcURLUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcMsgComponentsUtil msgComponentsUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgComponentsUtil.class, QPlcDefaultLiteral.INSTANCE);

		if (submittedValue == null) {
			return null;
		}

		Object valor = submittedValue;

		ValueExpression valueExpr = getFacesBean().getValueExpression(UIXValue.VALUE_KEY);

		if (valueExpr == null) {
			ValueBinding valueBinding = getFacesBean().getValueBinding(UIXValue.VALUE_KEY);
			if (valueBinding instanceof ValueExpressionValueBinding)
				valueExpr = ((ValueExpressionValueBinding) valueBinding).getValueExpression();
		}


		Class classe = valueExpr.getType(getFacesContext().getELContext());

		if (classe.getName().contains("String") && valor.getClass().getName().contains("Boolean")) {
			if (new Boolean(valor.toString()).booleanValue()){
				if(getFacesBean().getProperty(VALOR_MARCADO) != null){
					return getFacesBean().getProperty(VALOR_MARCADO);
				} else {
					return "S";
				}
				
			} else {
				
				if(getFacesBean().getProperty(VALOR_DESMARCADO) != null){
					return getFacesBean().getProperty(VALOR_DESMARCADO);
				} else {
					return "N";
				}
				
			}
		} else if (classe.getName().contains("PlcYesNo") && valor.getClass().getName().contains("Boolean")) {
			if (new Boolean(valor.toString()).booleanValue())
				return PlcYesNo.S;
			else
				return PlcYesNo.N;
		} else if (classe.getName().contains("String") && valor.getClass().getName().contains("PlcYesNo")) {
			return valor.toString();
		} else if (classe.getName().contains("PlcYesNo") && valor.getClass().getName().contains("String")) {
			if (valor.equals("S"))
				return PlcYesNo.S;
			else
				return PlcYesNo.N;
		} else
			return valor;

		//return super.getConvertedValue(context, submittedValue);
	}

	public void setPropertyComponenteDesprezar(EditableValueHolder componenteDesprezar) {

		this.setProperty(COMPONENTE_DESPREZAR_KEY, componenteDesprezar);
	}

	public void setPropertyChaveI18nDetalhe(String chaveI18nDetalhe) {

		this.setProperty(CHAVE_I18N_DETALHE_KEY, chaveI18nDetalhe);
	}

	public String getPropertyChaveI18nDetalhe() {

		return (String) this.getProperty(CHAVE_I18N_DETALHE_KEY);
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

	public void addLabel() {

		PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		componentUtil.addLabel(this);
	}
}
