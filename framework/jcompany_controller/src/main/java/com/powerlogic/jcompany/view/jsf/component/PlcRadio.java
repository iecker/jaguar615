/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.PropertyKey;
import org.apache.myfaces.trinidad.component.UIXComponentBase;
import org.apache.myfaces.trinidad.component.UIXIterator;
import org.apache.myfaces.trinidad.component.core.input.CoreSelectOneRadio;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregationPOJO;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcViewJsfUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcI18nUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;
import com.powerlogic.jcompany.view.jsf.renderer.PlcRadioRenderer;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;

/**
 * Especialização do componente base CoreSelectOneRadio para permitir IoC e DI nos componentes JSF/Trinidad. 
 * 
 */
public class PlcRadio extends CoreSelectOneRadio implements IPlcComponent {

	protected static final Logger		logVisao					= Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	/*
	 * Define o Tipo a Família para o componente 
	 */
	static public final String			COMPONENT_FAMILY			= "com.powerlogic.jsf.componente.entrada";

	static public final String			COMPONENT_TYPE				= "com.powerlogic.jsf.componente.PlcRadio";

	static public final FacesBean.Type	TYPE						= new FacesBean.Type(CoreSelectOneRadio.TYPE);

	/*
	 * Registra novas propriedades para o componente 
	 */
	static public final PropertyKey		DOMINIO_KEY					= TYPE.registerKey("dominio", Collection.class);

	static public final PropertyKey		PROPRIEDADE_KEY				= TYPE.registerKey("propriedade", String.class);

	static public final PropertyKey		COMPONENTE_DESPREZAR_KEY	= TYPE.registerKey("componenteDesprezar", EditableValueHolder.class);

	static public final PropertyKey		CHAVE_I18N_DETALHE_KEY		= TYPE.registerKey("chaveI18nDetalhe", String.class);

	static public final PropertyKey		BUNDLE						= TYPE.registerKey("bundle", String.class);

	static public final PropertyKey		RENDERED_EXPRESSION			= TYPE.registerKey("renderedExpression", String.class);

	static public final PropertyKey		RIA_USA						= TYPE.registerKey("riaUsa", String.class);

	static {
		//Registra o tipo do componente
		TYPE.lockAndRegister(COMPONENT_TYPE, PlcRadioRenderer.RENDERER_TYPE);
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

		try {

			PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

			//verificando se esta disabled.
			setDisabled(isDisabled()||PlcCDIUtil.getInstance().getInstanceByType(PlcViewJsfUtil.class, QPlcDefaultLiteral.INSTANCE).isReadOnly());

			String propriedade = (String) getProperty(PlcRadio.PROPRIEDADE_KEY);
			// Registra nome detalhe se estiver neste contexto
			String detalhe = componentUtil.getCurrentDetail();

			if (detalhe == null) {
				// Caso não achou detalhe, verifica pelo id da tag
				detalhe = componentUtil.findDetail(context, this);
			}

			componentUtil.checkSecurityDetail(getFacesBean(), detalhe, context);
			componentUtil.checkSecurity(getFacesBean(), UIXComponentBase.RENDERED_KEY, detalhe, propriedade);

			addLabel();
			
			super.encodeBegin(context);

		} catch (PlcException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Recuperando a lista de conforme Classes de Dominio discreto
	 */
	@Override
	public List<UIComponent> getChildren() {
		
		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		PlcI18nUtil i18nUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcI18nUtil.class, QPlcDefaultLiteral.INSTANCE);

		List<UIComponent> listaComponentes = null;//super.getChildren();
		if (listaComponentes == null) {
			listaComponentes = new ArrayList<UIComponent>();
		}

		//String expression = 
		ValueExpression valueExpression = getFacesBean().getValueExpression(PlcRadio.DOMINIO_KEY);
		
		Collection<SelectItem> c = null;
		
		Collection listaRetorno = null;
		
		Collection<SelectItem> listaItem = new ArrayList<SelectItem>();
		
		if (valueExpression != null){		
			listaRetorno = (Collection) valueExpression.getValue(FacesContext.getCurrentInstance().getELContext());
		}
		
		for(SelectItem i: (Collection<SelectItem>) listaRetorno){
			
			SelectItem item = new SelectItem();
			item.setLabel(i.getLabel());
			item.setValue(i.getValue());
			listaItem.add(item);
			
		}
		
		if(listaItem != null){
							
			HttpServletRequest request = contextUtil.getRequest();
			
			for(SelectItem item: listaItem){
				
				String label = item.getLabel();
									
				label = i18nUtil.mountLocalizedMessageAnyBundle(request, label, null);						
									
				item.setLabel(label);
				
			}				
		
		}
		
		c = listaItem;

		if (c != null) {
			UISelectItems items = new UISelectItems();
			items.setValue(c);
			//items.setParent(this);
			listaComponentes.add(items);
		}

		return listaComponentes;
	}

	@Override
	public int getChildCount() {

		return super.getChildCount() + 1;
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
	public void validateValue(FacesContext context, Object newValue) {

		PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

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

		if (!isValid() || componentUtil.avoidValidationByDespiseField(this.getFacesBean(), COMPONENTE_DESPREZAR_KEY, getClientId(context)))
			return;

		// If our value is empty, only check the required property
		if (isEmpty(newValue)) {
			if (isRequired()) {
				componentUtil.handleRequiredComponent(context, this);
			}
		} else {
			super.validateValue(context, newValue);
		}
	}

	/**
	 * Analisando o tipo do valor para fazer a conversão de valor adequada
	 */
	@Override
	public Object getLocalValue() {

		PlcConfigAggregationPOJO _configDominio;

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcConfigUtil configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcURLUtil urlUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcURLUtil.class, QPlcDefaultLiteral.INSTANCE);

		try {
			_configDominio = configUtil.getConfigAggregation(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest()));
			if (_configDominio.pattern().formPattern() == FormPattern.Sel || _configDominio.pattern().formPattern() == FormPattern.Con || _configDominio.pattern().formPattern() == FormPattern.Rel) {
				Object localValue = super.getLocalValue();
				if (localValue != null && localValue.getClass().isEnum()) {
					Enum valor = (Enum) localValue;
					return valor.name();
				}
			}
		} catch (PlcException e) {
			e.printStackTrace();
		}
		return super.getLocalValue();
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

	@Override
	public void processDecodes(FacesContext context) {

		super.processDecodes(context);
		if (getSubmittedValue() == null) {
			setSubmittedValue("");
		}
	}
	
	public void addLabel() {

		PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		componentUtil.addLabel(this);
	}
}
