/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.PropertyKey;
import org.apache.myfaces.trinidad.component.UIXComponentBase;
import org.apache.myfaces.trinidad.component.UIXIterator;
import org.apache.myfaces.trinidad.component.core.input.CoreInputDate;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcNamedLiteral;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcConversationControl;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcViewJsfUtil;
import com.powerlogic.jcompany.view.jsf.renderer.PlcDateRenderer;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;

/**
 * Especialização do componente base CoreInputDate para permitir IoC e DI nos componentes JSF/Trinidad. 
 * 
 */
public class PlcDate extends CoreInputDate implements IPlcComponent {

	protected static final Logger			logVisao					= Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	protected static final Logger			log							= Logger.getLogger(PlcDate.class.getCanonicalName());

	/*
	 * Define o Tipo a Família para o componente 
	 */
	static public final String				COMPONENT_FAMILY			= "com.powerlogic.jsf.componente.entrada";

	static public final String				COMPONENT_TYPE				= "com.powerlogic.jsf.componente.PlcData";

	static public final FacesBean.Type		TYPE						= new FacesBean.Type(CoreInputDate.TYPE);

	/*
	 * Registra novas propriedades para o componente 
	 */
	static public final PropertyKey			PROPRIEDADE_KEY				= TYPE.registerKey("propriedade", String.class);

	static public final PropertyKey			MSG_ERRO					= TYPE.registerKey("msgErro", String.class);

	static public final PropertyKey			COMPONENTE_DESPREZAR_KEY	= TYPE.registerKey("componenteDesprezar", EditableValueHolder.class);

	static public final PropertyKey			CHAVE_I18N_DETALHE_KEY		= TYPE.registerKey("chaveI18nDetalhe", String.class);

	static public final PropertyKey			BUNDLE						= TYPE.registerKey("bundle", String.class);

	static public final PropertyKey			RENDERED_EXPRESSION			= TYPE.registerKey("renderedExpression", String.class);

	static public final PropertyKey			RIA_USA						= TYPE.registerKey("riaUsa", String.class);

	static {
		//Registra o tipo do componente
		TYPE.lockAndRegister(COMPONENT_TYPE, PlcDateRenderer.RENDERER_TYPE);
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

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		//Evitando lixo de valor da tela
		if (getValue() == null && getSubmittedValue() != null) 
			setSubmittedValue(null);
		
		//verificando se esta disabled.
		setDisabled(isDisabled()||PlcCDIUtil.getInstance().getInstanceByType(PlcViewJsfUtil.class, QPlcDefaultLiteral.INSTANCE).isReadOnly());

		try {
			String propriedade = (String) getProperty(PlcDate.PROPRIEDADE_KEY);

			PlcConversationControl controleConversasao = PlcCDIUtil.getInstance().getInstanceByType(PlcConversationControl.class, new PlcNamedLiteral(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_CONVERSACAO));
			Boolean chaveNaturalPreenchido = PlcTagUtil.checkNaturalKeyFilled(getFacesBean(), propriedade);
			if (propriedade != null && propriedade.startsWith("idNatural.")) {
				String modoPlc = controleConversasao.getModoPlc();
				Boolean ehInclusao = "inclusaoPlc".equals(modoPlc);
				Boolean disabled = false;

				if (ehInclusao) {
					disabled = false;
				} else if (chaveNaturalPreenchido) {
					disabled = true;
				}

				setDisabled(disabled);
			}

			// Registra nome detalhe se estiver neste contexto
			String detalhe = componentUtil.getCurrentDetail();

			if (detalhe == null) {
				// Caso não achou detalhe, verifica pelo id da tag
				detalhe = componentUtil.findDetail(context, this);
			}

			componentUtil.checkSecurityDetail(getFacesBean(), detalhe, context);
			componentUtil.checkSecurity(getFacesBean(), UIXComponentBase.RENDERED_KEY, detalhe, propriedade);

			super.encodeBegin(context);

			Map mapaItens = (Map) contextUtil.getRequestAttribute(PlcJsfConstantes.PLC_ITENS_STATUS);

			if (getProperty(ONKEYUP_KEY) == null || mapaItens != null) {
				setOnkeyup("return validaCaracter(this, event, 'D');");
			}

			addLabel();
			
		} catch (PlcException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Método reescrito para alterar a validação padrão dos componentes do trinidad. 
	 * Em caso do obrigatório redefine mensagens de obrigatoriedade para o componente, 
	 * pois o trinidad busca as mensagens em arquivos de resources internos, e o padrão 
	 * jcompany e buscar nos bundles da aplicação.
	 * 
	 * Também verifica se é para validar ou não determinado componente.
	 */
	@Override
	protected void validateValue(FacesContext context, Object newValue) {

		PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		ServletContext servletContext = contextUtil.getApplicationContext();

		String client = servletContext.getInitParameter("org.apache.myfaces.trinidad.CLIENT_STATE_METHOD");

		EditableValueHolder componenteDesprezar = (EditableValueHolder) this.getFacesBean().getProperty(COMPONENTE_DESPREZAR_KEY);

		if (componenteDesprezar == this) {

			Object detalhe = null;
			UIComponent parent = ((UIComponent) componenteDesprezar).getParent();

			while (parent != null) {

				if (parent instanceof UIXIterator) {
					String var = ((UIXIterator) parent).getVar();
					PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
					detalhe = elUtil.evaluateExpressionGet("#{" + var + "}", Object.class);
					break;
				}

				parent = parent.getParent();
			}

			if (detalhe != null) {
				PlcEntityInstance detalheInstance = metamodelUtil.createEntityInstance(detalhe);
				// evita falha na validação caso já exista o detalhe.
				if (detalheInstance.getId() != null) {
					setRequired(true);
				}
			}
		}

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

	/**
	 * Foi necessária dar override no método para que seja setado um label obrigatório na mensagem de validação.
	 * 
	 */
	@Override
	public void validate(FacesContext context) {
	
		PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);
		boolean updated = componentUtil.setLabel(this);
		
		super.validate(context);
		
		if (updated) {
			componentUtil.unsetLabel(this);
		}
		
		if (getSubmittedValue() != null) {
			
			String value = (String) getSubmittedValue();
			if(StringUtils.isNotEmpty(value)) {
				setValue(getConvertedValue(context, getSubmittedValue()));
			}
		}	
	}

	public void addLabel() {

		PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		componentUtil.addLabel(this);
	}
}
