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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.PropertyKey;
import org.apache.myfaces.trinidad.component.UIXComponentBase;
import org.apache.myfaces.trinidad.component.core.input.CoreSelectOneChoice;
import org.apache.myfaces.trinidad.component.core.nav.CoreCommandButton;

import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.PlcEntityCommonsUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcNamedLiteral;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregationPOJO;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.controller.jsf.PlcBaseMB;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcDomainLookupUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcViewJsfUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcI18nUtil;
import com.powerlogic.jcompany.controller.util.PlcMsgUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;
import com.powerlogic.jcompany.domain.validation.PlcMessage.Cor;
import com.powerlogic.jcompany.view.jsf.renderer.PlcButtonRenderer;
import com.powerlogic.jcompany.view.jsf.renderer.PlcDynamicComboRenderer;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcMsgComponentsUtil;

/**
 * Especialização do componente base CoreSelectOneChoice para permitir IoC e DI nos componentes JSF/Trinidad. 
 * 
 */
public class PlcDynamicCombo extends CoreSelectOneChoice implements IPlcComponent {

	protected static final Logger		log							= Logger.getLogger(PlcBaseMB.class.getCanonicalName());

	/*
	 * Define o Tipo a Família para o componente 
	 */
	static public final String			COMPONENT_FAMILY			= "com.powerlogic.jsf.componente.entrada";

	static public final String			COMPONENT_TYPE				= "com.powerlogic.jsf.componente.PlcComboDinamico";

	static public final FacesBean.Type	TYPE						= new FacesBean.Type(CoreSelectOneChoice.TYPE);

	/*
	 * Registra novas propriedades para o componente 
	 */
	static public final PropertyKey		DOMINIO_KEY					= TYPE.registerKey("dominio", Collection.class);

	static public final PropertyKey		PROPRIEDADE_KEY				= TYPE.registerKey("propriedade", String.class);

	static public final PropertyKey		NAVEGACAO_PARA_CAMPOS_KEY	= TYPE.registerKey("navegacaoParaCampos", String.class);

	static public final PropertyKey		COMBO_ANINHADO_KEY			= TYPE.registerKey("ComboAninhado", Boolean.class);

	static public final PropertyKey		BOTAO_ATUALIZA_KEY			= TYPE.registerKey("botaoAtualiza", BotaoAtualizaTipo.class);

	static public final PropertyKey		PROP_ROTULO_KEY				= TYPE.registerKey("propRotulo", String.class);

	static public final PropertyKey		COMPONENTE_DESPREZAR_KEY	= TYPE.registerKey("componenteDesprezar", EditableValueHolder.class);

	static public final PropertyKey		CHAVE_I18N_DETALHE_KEY		= TYPE.registerKey("chaveI18nDetalhe", String.class);

	static public final PropertyKey		BUNDLE						= TYPE.registerKey("bundle", String.class);

	static public final PropertyKey		RENDERED_EXPRESSION			= TYPE.registerKey("renderedExpression", String.class);

	static public final PropertyKey		RIA_USA						= TYPE.registerKey("riaUsa", String.class);

	static public final PropertyKey		SKIP_CACHE_CONTROLE						= TYPE.registerKey("skipCacheControl", String.class);

	{
		//Registra o tipo do componente
		TYPE.lockAndRegister(COMPONENT_TYPE, PlcDynamicComboRenderer.RENDERER_TYPE);
	}

	public PlcDynamicCombo() {
		/**
		 * Se o Combo dinâmmico for recriado na postagem é porque seu conteúdo foi alterado. 
		 * Assim, emitimos um exceção para interromper o fluxo e não permitir que inserções incorretas sejam realizadas. 
		 */

	}


	public static enum BotaoAtualizaTipo {
		ATUALIZA, ATUALIZA_CACHE
	}

	private PlcButton	botaoAtualiza;

	@Override
	public String getFamily() {

		return COMPONENT_FAMILY;
	}

	@Override
	protected FacesBean.Type getBeanType() {

		return TYPE;
	}

	private List<UIComponent> children = null;
	/**
	 * Adicionando a lista de Componentes "Dominio"  para  o ComboBox
	 * 
	 * Observação: Caso o combo seja aninhado(atualizar um campo conforme valor do outro),
	 * a lista será buscada do scopo de conversação
	 * 
	 */
	@Override
	public List<UIComponent> getChildren() {

		if (getProperty(SKIP_CACHE_CONTROLE)==null || !Boolean.parseBoolean((String)getProperty(SKIP_CACHE_CONTROLE))) {
			
			verificaListaComboAlterada();

			if (children!=null)
				return children;
		}	
			
		List<UIComponent> childrenList = null;//(List<UIComponent>) requestMap.get(reqId);
		if (childrenList == null) {
			//Essa chamada é necessária, para que o botao seja criado,
			//se ainda nao foi, e adicionado ao super.children.
			getBotaoAtualiza();

			childrenList = super.getChildren();

			UISelectItems items = null;
			//Busca o UISelectItems e atualiza a lista
			for (UIComponent child : childrenList) {
				if (child instanceof UISelectItems) {
					items = (UISelectItems) child;
					break;
				}
			}
			if (items == null) {
				items = new UISelectItems();
				childrenList.add(items);
			}

			List<SelectItem> lista = getListaElementos();
			if (lista != null && !lista.isEmpty()) {
				items.setValue(lista);
			} else {
				items.setValue(new ArrayList<SelectItem>());
			}

		}

		children = childrenList; 

		/**
		 * Armazena lista de elementos na sessão com viewState + proporiedade
		 */		
		if (FacesContext.getCurrentInstance().getViewRoot()!=null && FacesContext.getCurrentInstance().getViewRoot().getAttributes().get(this.getProperty(PROPRIEDADE_KEY))==null )
			FacesContext.getCurrentInstance().getViewRoot().getAttributes().put(this.getProperty(PROPRIEDADE_KEY).toString(), children);

		return childrenList;
	}

	private void verificaListaComboAlterada() {
		if (FacesContext.getCurrentInstance().isPostback() && FacesContext.getCurrentInstance().getViewRoot()!=null) {
			List<UIComponent> childrenArmazenado = (List<UIComponent>)FacesContext.getCurrentInstance().getViewRoot().getAttributes().get(this.getProperty(PROPRIEDADE_KEY));
			if (!FacesContext.getCurrentInstance().getRenderResponse() && childrenArmazenado!=null && children!=null && !equalsListaElementos(childrenArmazenado, children)) {
				String propriedade = getProperty(PlcDynamicCombo.PROPRIEDADE_KEY).toString();

				FacesContext.getCurrentInstance().renderResponse();
				PlcI18nUtil i18nUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcI18nUtil.class, QPlcDefaultLiteral.INSTANCE);
				PlcMsgUtil msgUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgUtil.class, QPlcDefaultLiteral.INSTANCE);
				msgUtil.msg("{jcompany.erro.combodinamico.desatualizado}", new Object[]{i18nUtil.getMessage("ApplicationResources", "label."+propriedade)}, Cor.msgVermelhoPlc.toString());
			}
		}
	}

	private boolean equalsListaElementos(List<UIComponent> list1, List<UIComponent> list2) {

		PlcEntityCommonsUtil entityCommonsUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcEntityCommonsUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		
		if (list1==null || list2==null)
			return false;

		if (list1.size()!=list2.size())
			return false;

		if (list1.size()>0) {
			List<SelectItem> l1 = (List<SelectItem>)((UISelectItems)list1.get(0)).getValue();
			List<SelectItem> l2 = (List<SelectItem>)((UISelectItems)list2.get(0)).getValue();

			if (l1.size()!=l2.size())
				return false;

			for (int i = 0; i <l1.size(); i++) {
				SelectItem s1 = l1.get(i);
				SelectItem s2 = l2.get(i);
				if (!entityCommonsUtil.equalsPlc(s1.getValue(), s2.getValue()))
					return false;

			}
		}
		return true;
	}

	/**
	 * Busca a lista de {@link SelectItem} para o combo.
	 * @return uma lista de SelectItem ou nulo se a lista for vazia.
	 */
	@SuppressWarnings("unchecked")
	protected List<SelectItem> getListaElementos() {

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcConfigUtil configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcURLUtil urlUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcURLUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcMsgComponentsUtil msgComponentsUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgComponentsUtil.class, QPlcDefaultLiteral.INSTANCE);

		List lista = null;

		if (!isComboAninhado() && !FacesContext.getCurrentInstance().getResponseComplete()) {
			/*
			 * O combo não é aninhado ou seja busca sua lista das classes de Lookup da aplicação  
			 */
			ValueExpression dominio = getFacesBean().getValueExpression(PlcDynamicCombo.DOMINIO_KEY);
			if (dominio != null)
				lista = (List) dominio.getValue(FacesContext.getCurrentInstance().getELContext());

		} else {

			/*
			 * É um combo aninhado, por isso sua lista vai ser recuperada do escopo de conversação.
			 * A lista é atualizada no scopo de conversação quando altera o combo mestre ou quando edita o registro. 
			 */
			try {
				PlcDomainLookupUtil dominioLookupUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcDomainLookupUtil.class, new PlcNamedLiteral(PlcJsfConstantes.PLC_DOMINIOS));
				PlcConfigAggregationPOJO _configAcao = configUtil.getConfigAggregation(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest()));
				String nomeDominio = (String) getFacesBean().getProperty(PlcDynamicCombo.PROPRIEDADE_KEY);

				Map mapaItens = (Map) contextUtil.getRequestAttribute(PlcJsfConstantes.PLC_ITENS_STATUS);
				/*
				 * Verificando se o componente comboDinamico aninhado é de um detalhe, subdetalhe ou tabular
				 * Se o mapaItens não estiver null é porque é de uma destas opções, se estiver porque é do mestre ou de uma crud ..
				 */
				if (mapaItens != null) {
					nomeDominio = mapaItens.get("colecaoNome") + "[" + mapaItens.get("index") + "]" + nomeDominio;
				} else {

					if (_configAcao != null && (_configAcao.pattern().formPattern() == FormPattern.Smd || _configAcao.pattern().formPattern() == FormPattern.Sel || _configAcao.pattern().formPattern() == FormPattern.Con)) {
						nomeDominio = nomeDominio.substring(0, nomeDominio.indexOf(".valor"));
					}
				}

				lista = dominioLookupUtil.getNestedComboDomain(_configAcao.entity(), nomeDominio);

			} catch (Exception e) {
				log.info( e.getMessage(), e);
				msgComponentsUtil.createMsgError(getFacesBean(), this, null, e);
				//return listaComponentes; Não é necessário, se der exceção, lista estará vazia, e volta a listaComponentes no final do método
			}

		}

		PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);
		//A lista não é vazia nem nula.
		if (lista != null && !lista.isEmpty() && metamodelUtil.isEntityClass(lista.get(0).getClass())) {
			
			//cria instancia do propertyUtilsBean para evitar diversas chamadas a metodo syncronized
			PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
			
			//Converte a lista de PlcEntityInstance em lista de SelectItem
			try {
				String propRotulo = (String) getFacesBean().getProperty(PlcDynamicCombo.PROP_ROTULO_KEY);
				List<SelectItem> listaSelectItem = new ArrayList<SelectItem>();

				Iterator<Object> i = lista.iterator();

				while (i.hasNext()) {
					Object vo = i.next();
					SelectItem item = new SelectItem();
					item.setValue(vo);

					if (!StringUtils.isBlank(propRotulo))
						item.setLabel((String) propertyUtilsBean.getProperty(vo, propRotulo));
					else
						item.setLabel(vo.toString());

					listaSelectItem.add(item);

				}

				lista = listaSelectItem;
			} catch (Exception e) {
				msgComponentsUtil.createMsgError(getFacesBean(), this, "#Erro: Verifique o propRotulo do comboDinamico", e);
				//Se deu algum erro, não retorna nada.
				lista = null;
			}
		}

		return lista;
	}

	/**
	 * Retorna, e cria, se necessário, o botão de atualização.
	 * @return componente do botão de atualização, ou null, se não for para gerar.
	 */
	public PlcButton getBotaoAtualiza() {

		if (this.botaoAtualiza == null) {
			BotaoAtualizaTipo tipo = (BotaoAtualizaTipo) getFacesBean().getProperty(PlcDynamicCombo.BOTAO_ATUALIZA_KEY);
			if (tipo != null) {
				PlcButton botao = new PlcButton();
				botao.setRendererType(PlcButtonRenderer.RENDERER_TYPE);
				botao.setTransient(true);
				PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
				if (tipo == BotaoAtualizaTipo.ATUALIZA_CACHE) {//Se não for atualizar cache, só dá um reload na página. Nao precisa de uma action.
					botao.setActionExpression(elUtil.createMethodExpression("#{PlcBaseMB.atualizaClassesLookupDoBanco}", null, null));
				}
				FacesBean bean = botao.getFacesBean();
				bean.setProperty(PlcButton.TEXT_KEY, tipo == BotaoAtualizaTipo.ATUALIZA_CACHE ? "AC" : "A");
				bean.setProperty(PlcButton.EVENTO_KEY, tipo == BotaoAtualizaTipo.ATUALIZA_CACHE ? "REFRESH_CACHE" : "REFRESH");
				bean.setProperty(PlcButton.VALIDA_FORM, Boolean.FALSE);
				bean.setProperty(PlcButton.LIMPA_FORM, Boolean.FALSE);
				bean.setProperty(PlcButton.IMMEDIATE_KEY, Boolean.FALSE);
				bean.setValueExpression(PlcButton.PARTIAL_SUBMIT_KEY, elUtil.createValueExpression("#{requestScope.ajaxUsa}", Boolean.class));
				bean.setProperty(CoreCommandButton.ID_KEY, "AC");
				bean.setProperty(CoreCommandButton.STYLE_CLASS_KEY, "botao");
				this.botaoAtualiza = botao;
				super.getChildren().add(botao);
			}
		}
		return this.botaoAtualiza;
	}

	/**
	 * Implementado segurança na tag. Utilizado para verificar se o campo informado irá ser rederizado ou não
	 */

	@Override
	public void encodeBegin(FacesContext context) throws IOException {

		PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);
		//verificando se esta disabled.
		setDisabled(isDisabled()||PlcCDIUtil.getInstance().getInstanceByType(PlcViewJsfUtil.class, QPlcDefaultLiteral.INSTANCE).isReadOnly());
		try {

			String propriedade = (String) getProperty(PlcDynamicCombo.PROPRIEDADE_KEY);
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

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getChildCount() {

		return this.getChildren().size();
	}

	/**
	 * Retorna se o combo é aninhado ou não. "lista é atualizada conforme valor de outro combo".
	 */
	private Boolean isComboAninhado() {

		if (getFacesBean().getProperty(COMBO_ANINHADO_KEY) == null)
			return false;
		else
			return (Boolean) getFacesBean().getProperty(COMBO_ANINHADO_KEY);
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

		PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		if (!isValid() || componentUtil.avoidValidationByDespiseField(this.getFacesBean(), COMPONENTE_DESPREZAR_KEY, getClientId(context))) {
			return;
		}

		// If our value is empty, only check the required property
		if (isEmpty(newValue)) {
			if (isRequired()) {
				componentUtil.handleRequiredComponent(context, this);
			}
		}

	}

	/**
	 * Este método foi reescrito para considerar que o valor do Combo pode ser Enum e não 
	 * somente String igual o Trinidad considera.
	 */
	protected boolean isEmpty(Object value) {

		if (value == null)
			return true;
		return StringUtils.isBlank(value.toString());
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

	/**
	 * Analisando o tipo do valor para fazer a conversão de valor adequada
	 */
	@Override
	public Object getLocalValue() {

		PlcConfigAggregationPOJO _configAcao;

		PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcConfigUtil configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcURLUtil urlUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcURLUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcMsgComponentsUtil msgComponentsUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgComponentsUtil.class, QPlcDefaultLiteral.INSTANCE);

		try {
			_configAcao = configUtil.getConfigAggregation(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest()));
			if (_configAcao.pattern().formPattern() == FormPattern.Smd || _configAcao.pattern().formPattern() == FormPattern.Sel || _configAcao.pattern().formPattern() == FormPattern.Con || _configAcao.pattern().formPattern() == FormPattern.Rel || _configAcao.pattern().formPattern() == FormPattern.Ctb) {
				Object localValue = super.getLocalValue();
				if (localValue != null && metamodelUtil.isEntityClass(localValue.getClass())) {
					Object entidade = super.getLocalValue();
					PlcEntityInstance entidadeInstance = metamodelUtil.createEntityInstance(entidade);
					if (super.getLocalValue() != null && entidadeInstance.getId() != null) {
						if(_configAcao.pattern().formPattern()==FormPattern.Sel && localValue instanceof String) {
							return entidade.toString();
						} else {
							return entidade;
						}
					} else {
						if (super.getLocalValue() != null && entidadeInstance.getIdNaturalDinamico() != null)
							return entidadeInstance.getIdNaturalDinamico().toString();
					}
				} else {
					if (localValue instanceof Enum) {
						ValueExpression expression = getFacesBean().getValueExpression(VALUE_KEY);
						if (expression.getType(FacesContext.getCurrentInstance().getELContext()).isEnum())
							return localValue;
						else
							return localValue.toString();
					}
				}
			}
		} catch (PlcException e) {
			msgComponentsUtil.createMsgError(getFacesBean(), this, null, e);
		}
		return super.getLocalValue();
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
