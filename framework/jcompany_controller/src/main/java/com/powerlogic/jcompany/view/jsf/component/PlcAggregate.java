/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.

		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */

package com.powerlogic.jcompany.view.jsf.component;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.render.Renderer;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.PropertyKey;
import org.apache.myfaces.trinidad.component.UIXIterator;
import org.apache.myfaces.trinidad.component.UIXValue;
import org.apache.myfaces.trinidad.component.core.input.CoreInputText;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.comparator.PlcComparatorNaturalKey;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcEntityUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcViewJsfUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;
import com.powerlogic.jcompany.view.jsf.renderer.PlcAggregateRenderer;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcMsgComponentsUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;

/**
 * Especialização do componente base CoreInputText para permitir IoC e DI nos componentes JSF/Trinidad. 
 * @author Pedro Henrique Neves
 */
public class PlcAggregate extends CoreInputText implements IPlcComponent {

	private final String				IND_LOOKUP							= "lookup_";

	protected static final Logger		log									= Logger.getLogger(PlcAggregate.class.getCanonicalName());

	/*
	 * Define o Tipo a Família para o componente 
	 */
	public static final String			COMPONENT_TYPE						= "com.powerlogic.jsf.componente.PlcVinculado";

	public static final String			COMPONENT_FAMILY					= "com.powerlogic.jsf.componente.popup";

	public static final FacesBean.Type	TYPE								= new FacesBean.Type(CoreInputText.TYPE);

	/*
	 * Registra novas propriedades para o componente 
	 */
	static public final PropertyKey		ALT_KEY								= TYPE.registerKey("alt", String.class);

	static public final PropertyKey		LARG_KEY							= TYPE.registerKey("larg", String.class);

	static public final PropertyKey		POSX_KEY							= TYPE.registerKey("posx", String.class);

	static public final PropertyKey		POSY_KEY							= TYPE.registerKey("posy", String.class);

	static public final PropertyKey		AJAX_ID_UNICO_KEY					= TYPE.registerKey("ajaxIdUnico", String.class);

	/* atributos da selecao */
	static public final PropertyKey		BASE_ACTION_KEY						= TYPE.registerKey("baseActionSel", String.class);

	static public final PropertyKey		ACTION_SEL_KEY						= TYPE.registerKey("actionSel", String.class);

	static public final PropertyKey		EVENTO_KEY							= TYPE.registerKey("evento", String.class);

	static public final PropertyKey		PARAMETRO_KEY						= TYPE.registerKey("parametro", ValueExpression.class);

	static public final PropertyKey		PROPRIEDADE_KEY						= TYPE.registerKey("propriedade", String.class);

	static public final PropertyKey		PROPSEL_POP_KEY						= TYPE.registerKey("propSelPop", String.class);

	static public final PropertyKey		PROPSSEL_POP_KEY					= TYPE.registerKey("propsSelPop", String.class);

	static public final PropertyKey		LOOKUP_TAMANHO_KEY					= TYPE.registerKey("lookupTamanho", String.class);

	static public final PropertyKey		ID_TAMANHO_KEY						= TYPE.registerKey("idTamanho", String.class);

	static public final PropertyKey		LOOKUP_VALUE_KEY					= TYPE.registerKey(PlcJsfConstantes.PROPRIEDADES.LOOKUP_VALUE_KEY, String.class);

	static public final PropertyKey		COLUNAS_KEY							= TYPE.registerKey("colunas", String.class);

	static public final PropertyKey		ID_EXIBE_KEY						= TYPE.registerKey("idExibe", String.class);

	static public final PropertyKey		ID_SOMENTE_LEITURA_KEY				= TYPE.registerKey("idSomenteLeitura", String.class);

	static public final PropertyKey		OBRIGATORIO_KEY						= TYPE.registerKey("obrigatorio", String.class);

	static public final PropertyKey		AUTO_RECUPERACAO_CLASSE_KEY			= TYPE.registerKey(PlcJsfConstantes.PROPRIEDADES.AUTO_RECUPERACAO_CLASSE_KEY, String.class);

	static public final PropertyKey		AUTO_RECUPERACAO_PROPRIEDADE_KEY	= TYPE.registerKey(PlcJsfConstantes.PROPRIEDADES.AUTO_RECUPERACAO_PROPRIEDADE_KEY, String.class);

	static public final PropertyKey		AUTO_RECUPERACAO_PROPRIEDADE_VALUE	= TYPE.registerKey(PlcJsfConstantes.PROPRIEDADES.AUTO_RECUPERACAO_PROPRIEDADE_VALUE, String.class);

	static public final PropertyKey		AJUDA_KEY							= TYPE.registerKey("ajudaChave", String.class);

	static public final PropertyKey		COMPONENTE_DESPREZAR_KEY			= TYPE.registerKey("componenteDesprezar", EditableValueHolder.class);

	static public final PropertyKey		CHAVE_I18N_DETALHE_KEY				= TYPE.registerKey("chaveI18nDetalhe", String.class);

	static public final PropertyKey		BUNDLE								= TYPE.registerKey("bundle", String.class);

	static public final PropertyKey		PROPS_CHAVE_NATURAL_PLC				= TYPE.registerKey(PlcJsfConstantes.PROPRIEDADES.PROPS_CHAVE_NATURAL_PLC, TreeMap.class);

	static public final PropertyKey		LIMPA_PROPSSEL_POP_KEY				= TYPE.registerKey("limpaPropsSelPop", String.class);

	static public final PropertyKey		TITULO_BOTAO_SEL_POP_KEY			= TYPE.registerKey("tituloBotaoSelPop", String.class);

	static public final PropertyKey		TITULO_BOTAO_LIMPAR_KEY				= TYPE.registerKey("tituloBotaoLimpar", String.class);

	static public final PropertyKey		EXIBE_BOTAO_LIMPAR_KEY				= TYPE.registerKey("exibeBotaoLimpar", Boolean.class);

	static public final PropertyKey		EXIBE_BOTAO_MULTISEL				= TYPE.registerKey("exibeBotaoMultiSel", Boolean.class);

	static public final PropertyKey		BOTAO_MULTISEL_TITULO				= TYPE.registerKey("botaoMultiSelTitulo", String.class);

	static public final PropertyKey		RENDERED_EXPRESSION					= TYPE.registerKey("renderedExpression", String.class);

	static public final PropertyKey		RIA_USA								= TYPE.registerKey("riaUsa", String.class);

	static public final PropertyKey		MODAL								= TYPE.registerKey("modal", String.class);

	static public final PropertyKey		DELIMITADOR							= TYPE.registerKey("delimitador", String.class);

	static public final PropertyKey		SOMENTE_LEITURA						= TYPE.registerKey("somenteLeitura", String.class);

	static public final PropertyKey		TRANSACAO_UNICA						= TYPE.registerKey("transacaoUnica", String.class);
		
	static {
		//Registra o tipo do componente
		TYPE.lockAndRegister(COMPONENT_TYPE, null);
	}
	
	//cria instancia do propertyUtilsBean para evitar diversas chamadas a metodo syncronized
	private static PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();

	@Override
	public String getFamily() {

		return COMPONENT_FAMILY;
	}

	@Override
	protected FacesBean.Type getBeanType() {

		return TYPE;
	}

	/**
	 * Recupera o VO Corrente e Recupera o Objeto "PlcEntityInstance" do vinculado e se não for nulo
	 * recupera o valor para o componente vinculado .
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void encodeBegin(FacesContext context) throws IOException { 

		PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcReflectionUtil reflectionUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcReflectionUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcMsgComponentsUtil msgComponentsUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgComponentsUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		//verificando se esta disabled.
		setDisabled(isDisabled()||PlcCDIUtil.getInstance().getInstanceByType(PlcViewJsfUtil.class, QPlcDefaultLiteral.INSTANCE).isReadOnly());

		try {
			TreeMap<String, Object> propsChaveNatural = null;
			propsChaveNatural = (TreeMap<String, Object>) this.getProperty(PlcAggregate.PROPS_CHAVE_NATURAL_PLC);

			// Caso as propriedades chaves forem nulos, verifica na classe
			if (propsChaveNatural == null) {

				Object value = getValue();

				if (value != null && metamodelUtil.isEntityClass(value.getClass())) {
					propsChaveNatural = (TreeMap<String, Object>) PlcTagUtil.createNaturalKeyMap(value.getClass().getName());
					this.setProperty(PlcAggregate.PROPS_CHAVE_NATURAL_PLC, propsChaveNatural);
				} else if (value == null) {
					String nameClass = PlcTagUtil.getClassByField(getFacesBean());
					if (nameClass != null) {
						propsChaveNatural = (TreeMap<String, Object>) PlcTagUtil.createNaturalKeyMap(nameClass);
						this.setProperty(PlcAggregate.PROPS_CHAVE_NATURAL_PLC, propsChaveNatural);
					}
				}

			}

			//Recuperando a entidade corrente, não importa a Lógica {entityPlc ou itensPlc}
			Object entity = componentUtil.getManagedBean(getFacesBean());
			if (entity != null) {
				if (metamodelUtil.isEntityClass(entity.getClass())) {

					String propriedade = (String) this.getProperty(PlcAggregate.PROPRIEDADE_KEY);
					Object agregado = reflectionUtil.getEntityPropertyPlc(entity, propriedade);
					String id = applyRequestValues(context, agregado);

					if (agregado != null) {

						PlcEntityInstance agregadoInstance = metamodelUtil.createEntityInstance(agregado);

						// Se não for chave Natural
						if (propsChaveNatural == null || propsChaveNatural.isEmpty()) {
							if (NumberUtils.isNumber(id)) {
								agregadoInstance.setIdAux(id);
								this.setValue(agregadoInstance.getIdAux());

								String autoRecuperacaoPropriedade = (String) this.getProperty(PlcAggregate.AUTO_RECUPERACAO_PROPRIEDADE_KEY);
								if (StringUtils.isNotBlank(autoRecuperacaoPropriedade)) {
									Object prop = propertyUtilsBean.getProperty(agregado, autoRecuperacaoPropriedade);
									// Teste para tipo String
									if (prop instanceof String) {
										if (StringUtils.isNotBlank((String) prop))
											this.setProperty(PlcAggregate.AUTO_RECUPERACAO_PROPRIEDADE_VALUE, prop);
									} else if (prop != null)
										this.setProperty(PlcAggregate.AUTO_RECUPERACAO_PROPRIEDADE_VALUE, prop);
								}
							}
						} else {
							propsChaveNatural = atualizaPropsChaveNatural(agregado, propsChaveNatural);
							this.setProperty(PlcAggregate.PROPS_CHAVE_NATURAL_PLC, propsChaveNatural);
						}
					}
				}

			} else {
				this.setValue("");
				this.setProperty(PlcAggregate.LOOKUP_VALUE_KEY, "");
			}

		} catch (Exception e) {
			msgComponentsUtil.createMsgError(getFacesBean(), this, null, e);
		}
		
		addLabel();
		
		super.encodeBegin(context);
	}


	/**
	 * Atualiza o mapa de propriedades referênte à chaves naturais.
	 */
	protected TreeMap<String, Object> atualizaPropsChaveNatural(Object agregado, TreeMap<String, Object> propsChaveNatural) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcEntityInstance agregadoInstance = metamodelUtil.createEntityInstance(agregado);

		TreeMap<String, Object> newPropsChaveNatural = returnComparator();

		newPropsChaveNatural.putAll(propsChaveNatural);

		boolean firstField = true;

		for (String propriedadeCN : propsChaveNatural.keySet()) {

			Object value = null;
			if (agregadoInstance.getIdNaturalDinamico() != null) {
				
				Object property = null;

				if (propertyUtilsBean.isWriteable(agregadoInstance.getIdNaturalDinamico(), propriedadeCN)) {
					property = propertyUtilsBean.getProperty(agregadoInstance.getIdNaturalDinamico(), propriedadeCN);
				} else if (propertyUtilsBean.isWriteable(agregado, propriedadeCN)) {
					property = propertyUtilsBean.getProperty(agregado, propriedadeCN);
				}
				
				if (property != null) {
					value = property.toString();
				}
			} else {
				String idCampo = (firstField == true ? this.getClientId(FacesContext.getCurrentInstance()) : this.getClientId(FacesContext.getCurrentInstance()) + propriedadeCN);
				value = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(idCampo);
			}

			if (value != null) {

				if (value != null && value instanceof Date) {
					DateFormat dataFormat = DateFormat.getDateInstance(DateFormat.SHORT, contextUtil.getRequest().getLocale());
					newPropsChaveNatural.put(propriedadeCN, dataFormat.format(value));
				} else {
					newPropsChaveNatural.put(propriedadeCN, value);
				}
			}

			firstField = false;

		}
		return newPropsChaveNatural;
	}

	private TreeMap<String, Object> returnComparator() {

		TreeMap<String, Object> newPropsChaveNatural = new TreeMap<String, Object>(new PlcComparatorNaturalKey());
		return newPropsChaveNatural;
	}

	/**
	 * Recupera o valor escolhido para o agregado no componente e cria o Objeto do agregado na Entidade.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void processUpdates(FacesContext context) {

		PlcMsgComponentsUtil msgComponentsUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgComponentsUtil.class, QPlcDefaultLiteral.INSTANCE);

		try {
			setProperty(LOOKUP_VALUE_KEY, getLookupProperty());
			log.debug("processUpdates - Recupera o valor escolhido para o agregado no componente e cria o Objeto do agregado na Entidade.");
			recuperaAgregado(context);

		}catch (PlcException e) {
			//plcException necessario para o não tratamento de mensagens de componente
		}catch (Exception e) {
			msgComponentsUtil.createMsgError(getFacesBean(), this, null, e);
			super.processUpdates(context);
		}
	}

	protected void recuperaAgregado(FacesContext context) {

		PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcReflectionUtil reflectionUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcReflectionUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		Map<String, Object> propriedades = (Map<String, Object>) this.getProperty(PlcAggregate.PROPS_CHAVE_NATURAL_PLC);
		
		PlcEntityUtil entityUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcEntityUtil.class, QPlcDefaultLiteral.INSTANCE);
 
		PlcConfigUtil configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		log.debug("recuperaAgregado - Recuperando a entidade corrente, não importa a Lógica {entityPlc ou itensPlc}");
		Object entity = componentUtil.getManagedBean(getFacesBean());

		if (propriedades != null && !propriedades.isEmpty()) {
			// Se for chaveNatural, entra aqui
			if (entity != null && metamodelUtil.isEntityClass(entity.getClass())) {
				Object agregado = entityUtil.getAggregate(entity, (String) this.getProperty(PlcAggregate.PROPRIEDADE_KEY));

				String autoRecuperacaoPropriedadeKey = (String) this.getProperty(PlcAggregate.AUTO_RECUPERACAO_PROPRIEDADE_KEY);

				// Atualiza chaves
				propriedades = atualizaPropsChaveNaturalFromRequest(propriedades);

				Map propAutoRecuperacao = null;

				if (StringUtils.isNotBlank(autoRecuperacaoPropriedadeKey)) {
					propAutoRecuperacao = new HashMap<String, Object>();
					String idPropAutoRecuperacao = getClientId(context) + ":" + autoRecuperacaoPropriedadeKey;
					String valorPropAutoRecuperacao = contextUtil.getRequestParameter(idPropAutoRecuperacao);
					propAutoRecuperacao.put(autoRecuperacaoPropriedadeKey, valorPropAutoRecuperacao);
					this.setProperty(PlcAggregate.AUTO_RECUPERACAO_PROPRIEDADE_VALUE, valorPropAutoRecuperacao);
				}

				if (agregado == null) {
					entityUtil.createAggregateToEntity(entity, (String) this.getProperty(PlcAggregate.PROPRIEDADE_KEY), null, propriedades);
					agregado = entityUtil.getAggregate(entity, (String) this.getProperty(PlcAggregate.PROPRIEDADE_KEY));
				}

				Set<String> keys = propriedades.keySet();

				String clientId = getClientId(context);

				String indiceAutoRecuperacao = (String) contextUtil.getRequestAttribute(PlcJsfConstantes.ACAO.PLC_IND_AUTO_RECUPERACAO + clientId);
				boolean ehAutoRecuperacao = clientId.equals(indiceAutoRecuperacao);

				if (!ehAutoRecuperacao) {
					for (String props : keys) {
						Object valor = propriedades.get(props);
						if (StringUtils.isBlank((String) valor)) {
							agregado = null;
							break;
						}
					}
				}

				PlcEntityInstance agregadoInstance = metamodelUtil.createEntityInstance(agregado);

				if (ehAutoRecuperacao || (agregado != null && (StringUtils.isEmpty(agregadoInstance.getIdAux()) || (StringUtils.isEmpty(agregado.toString()) && getLookupProperty()==null)  ))) {
					
					// #110972# So faz a recuperação do vinculado neste momento se a propriedade transacaoUnica for diferente de S
					Boolean transacaoUnicaAplicacao =  true;
					Boolean transacaoUnicaCustom 	= null;
					
					if (getFacesBean().getProperty(PlcAggregate.TRANSACAO_UNICA) != null){
						transacaoUnicaCustom =  "S".equals(getFacesBean().getProperty(PlcAggregate.TRANSACAO_UNICA));
					}
					//FIXME - Rever caso da Autorecuperacao
					if (propAutoRecuperacao == null) {
						recuperaAgregado(propriedades, entityUtil, agregado, transacaoUnicaAplicacao, transacaoUnicaCustom);
					} else {
						recuperaAgregado(propAutoRecuperacao, entityUtil, agregado,  transacaoUnicaAplicacao, transacaoUnicaCustom);
					}
				}

				String propriedade = (String) this.getProperty(PlcAggregate.PROPRIEDADE_KEY);
				reflectionUtil.setEntityPropertyPlc(entity, propriedade, agregado);
				this.setValue(agregado);
			} 

		} else {
			// Se não for chave Natural, entra aqui
			if (entity != null && metamodelUtil.isEntityClass(entity.getClass())) {
				if (propriedades == null) {
					propriedades = new HashMap<String, Object>();
				}

				String autoRecuperacaoPropriedadeKey = (String) this.getProperty(PlcAggregate.AUTO_RECUPERACAO_PROPRIEDADE_KEY);

				if (StringUtils.isBlank(autoRecuperacaoPropriedadeKey)) {
					if (this.getValue() instanceof String && (NumberUtils.isNumber((String) this.getValue()))) {
						propriedades.put("id", new Long((String) this.getValue()));
					} else if (this.getValue() instanceof String && (((String)this.getValue()).isEmpty())) {
						//Limpando, setando a entidade para null
						reflectionUtil.setEntityPropertyPlc(entity, (String) this.getProperty(PlcAggregate.PROPRIEDADE_KEY), null);
						return;
					}
				} else {
					String idPropAutoRecuperacao = getClientId(context) + ":" + autoRecuperacaoPropriedadeKey;
					String valorPropAutoRecuperacao = contextUtil.getRequestParameter(idPropAutoRecuperacao);
					propriedades.put(autoRecuperacaoPropriedadeKey, valorPropAutoRecuperacao);
					this.setProperty(PlcAggregate.AUTO_RECUPERACAO_PROPRIEDADE_VALUE, valorPropAutoRecuperacao);
				}

				Object agregado = entityUtil.getAggregate(entity, (String) this.getProperty(PlcAggregate.PROPRIEDADE_KEY));
				
				if (agregado == null) {
					try {
						entityUtil.createAggregateToEntity(entity, (String) this.getProperty(PlcAggregate.PROPRIEDADE_KEY), "", propriedades);
						agregado = entityUtil.getAggregate(entity, (String) this.getProperty(PlcAggregate.PROPRIEDADE_KEY));
					} catch (Exception e) {
						ValueExpression actionVE =   (ValueExpression)this.getProperty(PlcAggregate.ACTION_SEL_KEY);
						String action = "";
						if (actionVE != null) {
							if (actionVE.isLiteralText()) {
								action = actionVE.getExpressionString();
							} else {
								action = (String) actionVE.getValue(context.getELContext());
							}
						}
						Class o = configUtil.getConfigAggregation(action).entity();
						try {
							agregado = o.newInstance();
						} catch (InstantiationException e1) {
							e1.printStackTrace();
						} catch (IllegalAccessException e1) {
							e1.printStackTrace();
						}
						PlcEntityInstance agregadoInstance = metamodelUtil.createEntityInstance(agregado);
						agregadoInstance.setIdAux(propriedades.get("id").toString());
						this.setValue(agregado);
					}
				}

				PlcEntityInstance agregadoInstance = metamodelUtil.createEntityInstance(agregado);
				
				if (propriedades.isEmpty() && agregadoInstance!=null && agregadoInstance.getIdAux()!=null) {
					propriedades.put("id", agregadoInstance.getIdAux());
				}
				
				if (verificaPropriedadesParaRecuperacao(propriedades, agregado, agregadoInstance)) {
					
					// #110972# So faz a recuperação do vinculado neste momento se a propriedade transacaoUnica for diferente de S
					String transacaoUnica = (String) getFacesBean().getProperty( 	PlcAggregate.TRANSACAO_UNICA);
					//FIXME - Rever caso da Autorecuperacao
					try {
						agregado = entityUtil.retrieveAggregateByProperties(propriedades, agregado);
						if (agregado != null) {
							log.info("recuperaAgregado - Verificando propriedade 'transacaoUnica' definida como "+ transacaoUnica +" no Vinculado: " + agregado.getClass());
							if (!StringUtils.equals("S", transacaoUnica)) { 
								propertyUtilsBean.copyProperties(agregado, agregado);
							} else {
								propertyUtilsBean.copyProperties(agregado, agregado);
							}
						}	
					} catch(PlcException plcE){
						throw plcE;								
					} catch (Exception e) {
						throw new PlcException(e);
					} 
				}

				String propriedade = (String) this.getProperty(PlcAggregate.PROPRIEDADE_KEY);
				reflectionUtil.setEntityPropertyPlc(entity, propriedade, agregado);

				agregadoInstance = metamodelUtil.createEntityInstance(agregado);
				if (agregadoInstance!=null) {
					this.setValue(agregadoInstance.getIdAux());
				} else {
					this.setValue(""); 
				}
				
				propriedades.clear();
			} 

		}
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
				// Evita falha na validação caso já exista o detalhe.
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

	@Override
	protected Object getConvertedValue(FacesContext context, Object submittedValue) throws ConverterException {

		ativaCrossValidation(context, submittedValue);
    	
		try {
			PlcEntityUtil entityUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcEntityUtil.class, QPlcDefaultLiteral.INSTANCE);

			PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);

			PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

			Object entity = componentUtil.getManagedBean(getFacesBean());

			Map<String, Object> propriedades = (Map<String, Object>) this.getProperty(PlcAggregate.PROPS_CHAVE_NATURAL_PLC);

			if (entity != null && metamodelUtil.isEntityClass(entity.getClass())) {
				if (propriedades != null && !propriedades.isEmpty()) {
					// chave natural
					propriedades = atualizaPropsChaveNaturalFromRequest(propriedades);
					submittedValue = entityUtil.createAggregateToEntity(entity, (String) this.getProperty(PlcAggregate.PROPRIEDADE_KEY), null, propriedades, true);
				} else {
					submittedValue = entityUtil.createAggregateToEntity(entity, (String) this.getProperty(PlcAggregate.PROPRIEDADE_KEY), (String) submittedValue, propriedades, true);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return submittedValue;
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
	 * IoC do jcompany. 
	 * Como o vinculado é um componente customizado com campos que não faz parte do componente original deve-se registrar os valores para estes campos conforme o request.
	 * Estes campos adicionais são criados no encodeAll do padrão IND_LOOKUP + clientId
	 * 
	 * Recuperando do request o valor do lookup referente ao agregado.
	 * O campo "Lookup" do agregado não é transferido para o componente, pois não é um component do trinidad [ é um input text do html ]. 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * 
	 */
	@SuppressWarnings("unchecked")
	protected String applyRequestValues(FacesContext context, Object agregado) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		PlcMetamodelUtil metamodelUtil 	= PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);
		PlcContextUtil contextUtil 		= PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);
		PlcURLUtil urlUtil 				= PlcCDIUtil.getInstance().getInstanceByType(PlcURLUtil.class, QPlcDefaultLiteral.INSTANCE);
		PlcConfigUtil configUtil 		= PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);

		String id 			= "";
		String url 			= urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
		FormPattern logica 	= configUtil.getConfigAggregation(url).pattern().formPattern();
		
		// Se for novo e excluir a ação deve-se limpar os componentes
		if (!eParaLimparComponente(contextUtil.getRequest())) {
			TreeMap<String, Object> propsChaveNatural = (TreeMap<String, Object>) this.getProperty(PlcAggregate.PROPS_CHAVE_NATURAL_PLC);

			if (agregado != null && metamodelUtil.isEntityClass(agregado.getClass()) && (propsChaveNatural == null || propsChaveNatural.isEmpty())) {
				PlcEntityInstance<?> valorInstance = metamodelUtil.createEntityInstance(agregado);
				id = valorInstance.getIdAux();
			} else if (agregado != null) {
				id = this.getClientId(context);
			}
			
			if (StringUtils.isNotBlank(id)) {
				 
				if (agregado != null && StringUtils.isNotBlank(agregado.toString())) {
					this.setProperty(PlcAggregate.LOOKUP_VALUE_KEY, agregado.toString());
				} else if(getLookupProperty() != null && StringUtils.isNotBlank(getLookupProperty().toString())){
					this.setProperty(PlcAggregate.LOOKUP_VALUE_KEY, getLookupProperty());
				} else {
					this.setProperty(PlcAggregate.LOOKUP_VALUE_KEY, "");
				}
			} else {
				this.setValue("");
				this.setProperty(PlcAggregate.LOOKUP_VALUE_KEY, "");
			}
			
			// Quando se usa outra propriedade para auto recuperacao
			String propAutoRecuperacaoValue = (String) contextUtil.getRequest().getAttribute(PlcJsfConstantes.PROPRIEDADES.AUTO_RECUPERACAO_PROPRIEDADE_VALUE + getClientId(context));
			String nomeProp = (String) this.getProperty(PlcAggregate.AUTO_RECUPERACAO_PROPRIEDADE_KEY);

			if (StringUtils.isBlank(propAutoRecuperacaoValue) && StringUtils.isNotBlank(nomeProp)) {
				if (agregado != null && metamodelUtil.isEntityClass(agregado.getClass()) && propertyUtilsBean.getProperty(agregado, nomeProp) !=null) {
					Object valorProp = propertyUtilsBean.getProperty(agregado, nomeProp);
					if (valorProp != null) {
						propAutoRecuperacaoValue = valorProp.toString();
					}
				} else {
					String idPropAutoRecuperacao = getClientId(context) + ":" + nomeProp;
					propAutoRecuperacaoValue = contextUtil.getRequestParameter(idPropAutoRecuperacao);
				}
			}

			if (StringUtils.isNotBlank(propAutoRecuperacaoValue)) {
				this.setProperty(PlcAggregate.AUTO_RECUPERACAO_PROPRIEDADE_VALUE, propAutoRecuperacaoValue);
			}

		} else if (eParaLimparComponente(contextUtil.getRequest()) && (FormPattern.Ctb.equals(logica)?false:true)){
			limpaComponente();
		}			

		return id;
	}

	/**
	 * Se a ação for novo e excluir deve-se limpar os componentes
	 */
	protected boolean eParaLimparComponente(HttpServletRequest request) {

		// Se a ação for novo e excluir deve-se limpar os componentes
		Object acaoNovo = request.getAttribute(PlcJsfConstantes.ACAO.PLC_IND_ACAO_NOVO);
		Object acaoExcluir = request.getAttribute(PlcJsfConstantes.ACAO.PLC_IND_ACAO_EXCLUIR);

		if (acaoExcluir != null && "S".equals(acaoExcluir.toString())) {
			acaoExcluir = FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(PlcConstants.WORKFLOW.EXCLUI_ENCERROU_OK);
		}	
		return ((acaoNovo != null && "S".equals(acaoNovo.toString())) || (acaoExcluir != null && "S".equals(acaoExcluir.toString())));

	}

	@SuppressWarnings("unchecked")
	protected void limpaComponente() {

		this.setProperty(PlcAggregate.VALUE_KEY, "");
		this.setProperty(PlcAggregate.LOOKUP_VALUE_KEY, "");
		this.setProperty(PlcAggregate.AUTO_RECUPERACAO_PROPRIEDADE_VALUE, "");
		TreeMap<String, Object> propsChaveNatural = (TreeMap<String, Object>) this.getProperty(PlcAggregate.PROPS_CHAVE_NATURAL_PLC);
		this.setProperty(PlcAggregate.PROPS_CHAVE_NATURAL_PLC, propsChaveNatural);

	}

	/**
	 * Atualiza o mapa de propriedades referênte à chaves naturais.
	 */
	protected Map<String, Object> atualizaPropsChaveNaturalFromRequest(Map<String, Object> propsChaveNatural) {

		PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		Map<String, String> mapaComponentes = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

		String expressao = componentUtil.getExpression(getFacesBean(), UIXValue.VALUE_KEY).replace("#{", "").replace("}", "");
		boolean ehAutoRecuperacao = mapaComponentes.get(this.getClientId(FacesContext.getCurrentInstance())) != null;

		if (expressao.contains(".idNatural.") && !ehAutoRecuperacao) {
			// Não faz nada, pois é id e não precisa de atualização
		} else if (propsChaveNatural != null && !propsChaveNatural.isEmpty()) {
			TreeMap<String, Object> newPropsChaveNatural = returnComparator();
			boolean firstField = true;
			String clientId = this.getClientId(FacesContext.getCurrentInstance());
			for (String propriedadeCN : propsChaveNatural.keySet()) {
				newPropsChaveNatural.put(propriedadeCN, mapaComponentes.get(firstField == true ? clientId : clientId + propriedadeCN));
				firstField = false;
			}
			propsChaveNatural = newPropsChaveNatural;
		}
		return propsChaveNatural;
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
		} else {
			super.setValueExpression(name, expression);
		}
	}

	
	public void addLabel() {

		PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		componentUtil.addLabel(this);
	}
	
	/**
	 * Ativando a "cross-validation". 
	 * Possibilitando o ativação/uso do RequiredIf disponibilizado pelo ExtVal.
	 * O Framework Extval possui integração nativa com os componentes Trinidad/RichFaces.
	 * PlcAggregate extende de CoreInputText, possibilitando utilizar/ativar a funcionalidade através dessa chamada.
	 * @param context
	 * @param submittedValue
	 */
	private void ativaCrossValidation(FacesContext context, Object submittedValue) {
		Renderer renderer = getRenderer(context);
	    if (renderer != null) {
	    	renderer.getConvertedValue(context, this, submittedValue);
	    }
	}
	
	private void recuperaAgregado(Map propriedades, PlcEntityUtil entityUtil, Object agregado, Boolean transacaoUnicaAplicacao, Boolean transacaoUnicaCustom) {
		if ((transacaoUnicaCustom == null && transacaoUnicaAplicacao) || (transacaoUnicaCustom != null && transacaoUnicaCustom)) {   
			agregado = entityUtil.createAggregateByProperties(propriedades, agregado);
		} else {
			agregado = entityUtil.retrieveAggregateByProperties(propriedades, agregado);
		}
	}

	private boolean verificaPropriedadesParaRecuperacao(Map<String, Object> propriedades, Object agregado, PlcEntityInstance agregadoInstance) {
		
		return agregado != null 
				&& agregadoInstance != null 
				&& (agregadoInstance.getIdAux() == null || (agregado.toString() == null 
				&& (getLookupProperty() == null || StringUtils.isEmpty((String) getLookupProperty()))) || !agregadoInstance.getIdAux().equals((this.getValue()))) 
				&& !propriedades.isEmpty();
	}
	
	/**
	 * @return o valor do lookup vindo do request.
	 */
	private Object getLookupProperty(){
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> mapaTrinidad = context.getExternalContext().getRequestParameterMap();
		String lookupclientId = IND_LOOKUP + this.getClientId(context);
		Object value = mapaTrinidad.get(lookupclientId);
		return value;
	}	
}
