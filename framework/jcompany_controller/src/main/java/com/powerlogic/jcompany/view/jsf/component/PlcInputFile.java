/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.EditableValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.PropertyKey;
import org.apache.myfaces.trinidad.component.core.input.CoreInputFile;
import org.apache.myfaces.trinidad.model.UploadedFile;

import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcFile;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.PlcFileUtil;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregationPOJO;
import com.powerlogic.jcompany.config.domain.PlcFileAttach;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcViewJsfUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcMsgUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;

/**
 * Especialização do componente base CoreInputFile para permitir IoC e DI nos componentes JSF/Trinidad. 
 * 
 */
public class PlcInputFile extends CoreInputFile implements IPlcComponent {

	/*
	 * Define o Tipo a Família para o componente 
	 */
	static public final String COMPONENT_FAMILY = "com.powerlogic.jsf.componente.entrada";

	static public final String COMPONENT_TYPE = "com.powerlogic.jsf.componente.PlcArquivo";

	public static final FacesBean.Type TYPE = new FacesBean.Type(CoreInputFile.TYPE);

	/*
	 * Registra novas propriedades para o componente 
	 */
	static public final PropertyKey VALUE_ARQUIVO_KEY = TYPE.registerKey("valueArquivo", String.class);

	static public final String REQUIRED_MESSAGE_ID = "errors.required";

	static public final PropertyKey COMPONENTE_DESPREZAR_KEY = TYPE.registerKey("componenteDesprezar", EditableValueHolder.class);

	static public final PropertyKey BUNDLE = TYPE.registerKey("bundle", String.class);

	static public final PropertyKey CHAVE_I18N_DETALHE_KEY = TYPE.registerKey("chaveI18nDetalhe", String.class);

	static public final PropertyKey RENDERED_EXPRESSION = TYPE.registerKey("renderedExpression", String.class);

	static public final PropertyKey RIA_USA = TYPE.registerKey("riaUsa", String.class);

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
			//getNameFile() == null && 
			if ((!isValid() || componentUtil.avoidValidationByDespiseField(this.getFacesBean(), COMPONENTE_DESPREZAR_KEY, getClientId(context)))) {

				return;
			}
			// If our value is empty, only check the required property
			if (isEmpty(newValue)) {
				//não faz nada, a validação é feita via Hibernate Validation
				if (isRequired()) {
					//componentUtil.handleRequiredComponent(context, this);
				}
			} else {
				super.validateValue(context, newValue);
			}
		}
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
	
	public void validate(FacesContext context) {

		PlcMsgUtil msgUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		if (context == null)
			throw new NullPointerException();

		// Submitted value == null means "the component was not submitted
		// at all";  validation should not continue

		Object submittedValue = getSubmittedValue();
		if (submittedValue == null)
			return;

		// From the SPEC:
		// If the javax.faces.INTERPRET_EMPTY_STRING_SUBMITTED_VALUES_AS_NULL context parameter value is
		// true (ignoring case), and getSubmittedValue() returns a zero-length String call
		// setSubmittedValue(null) and continue processing using null as the current submitted value
		//
		// TODO: -> SPEC ISSUE (matzew)  setSubmittedValue(null) is wrong, so we do not follow the spec here...
		if (shouldInterpretEmptyStringSubmittedValuesAsNull(context) && _isEmptyString(submittedValue)) {
			submittedValue = null;
		}

		Object newValue = null;
		try {
			newValue = getConvertedValue(context, submittedValue);
		} catch (ConverterException ce) {
			setValid(false);
		}

		validateValue(context, newValue);

		// If our value is valid, store the new value, erase the
		// "submitted" value, and emit a ValueChangeEvent if appropriate
		if (isValid()) {
			Object previous = getValue();
			setSubmittedValue(null);
			if (compareValues(previous, newValue)) {
				setValue(newValue);
				queueEvent(new ValueChangeEvent(this, previous, newValue));
			}
		}
	}

	/**
	 * <p>Return <code>true</code> if the value is an empty <code>String</code>.</p>
	 */
	private boolean _isEmptyString(Object value) {

		return ((value instanceof String) && (((String) value).length() == 0));
	}
	
	@Override
	public void encodeBegin(FacesContext context) throws IOException {

		//verificando se o componente está disabled.
		setDisabled(isDisabled() || PlcCDIUtil.getInstance().getInstanceByType(PlcViewJsfUtil.class, QPlcDefaultLiteral.INSTANCE).isReadOnly());

		addLabel();
		
		super.encodeBegin(context);
	}
	
	protected String getNameFileAttach() {
		
		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		String name = null;
		
		name = contextUtil.getRequestParameter("nameFile");
		
		if (StringUtils.isEmpty(name) ) {
			name = "";
		}
		return name;
	}
	
	public void addLabel() {

		PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		componentUtil.addLabel(this);
	}
}
