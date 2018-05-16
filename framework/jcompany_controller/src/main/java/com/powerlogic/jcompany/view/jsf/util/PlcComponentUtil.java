/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.view.jsf.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.extensions.validator.util.ReflectionUtils;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.PropertyKey;
import org.apache.myfaces.trinidad.bean.ValueExpressionValueBinding;
import org.apache.myfaces.trinidad.component.UIXComponent;
import org.apache.myfaces.trinidad.component.UIXComponentBase;
import org.apache.myfaces.trinidad.component.UIXEditableValue;
import org.apache.myfaces.trinidad.component.UIXForm;
import org.apache.myfaces.trinidad.component.UIXIterator;
import org.apache.myfaces.trinidad.component.UIXSelectInput;
import org.apache.myfaces.trinidad.component.UIXValue;
import org.apache.myfaces.trinidad.component.core.input.CoreInputText;
import org.apache.myfaces.trinidad.util.LabeledFacesMessage;

import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregationPOJO;
import com.powerlogic.jcompany.config.aggregation.PlcConfigDetail;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcI18nUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;
import com.powerlogic.jcompany.domain.validation.PlcValSimpleFormat.SimpleFormat;
import com.powerlogic.jcompany.view.jsf.component.IPlcComponent;


/**
 * @since jCompany 5.0 Classe para auxiliar funcionamento dos Componentes JSF/trinidad do JCompany 
 * @author Bruno Grossi
 * @author Pedro Henrique
 * @author Moises
 * @author Igor Guimarães
 */
@SPlcUtil
@QPlcDefault
public class PlcComponentUtil {

	@Inject
	protected transient Logger log;

	static public final FacesBean.Type TYPE = new FacesBean.Type(UIXSelectInput.TYPE);
	
	static public final PropertyKey LABEL_NULL_KEY = TYPE.registerKey("labelNull", Boolean.class, Boolean.FALSE);	
	
	/**
	 * Padrão regex para recuperar a útlima propriedade da expressão (EL). O grupo1 será a propriedade.
	 */
	private final Pattern patternUltimaPropriedade = Pattern.compile("#\\{[^}]+\\.([\\w\\_\\-]+)\\s*\\}");

	private final Pattern patternPartesExpressao = Pattern.compile("([^\\.\\#\\{\\}]+)");
	

	/**
	 * Serviço injetado e gerenciado pelo CDI
	 */
	@Inject @QPlcDefault 
	protected PlcContextUtil contextUtil;
	
	@Inject @QPlcDefault 
	protected PlcConfigUtil configUtil;
	
	@Inject @QPlcDefault 
	protected PlcURLUtil urlUtil;

	@Inject @QPlcDefault 
	protected PlcComponentUtil componentUtil;

	@Inject @QPlcDefault 
	protected PlcComponentPropertiesUtil componentPropertiesUtil;
	
	@Inject @QPlcDefault 
	protected PlcELUtil elUtil;
	
	@Inject @QPlcDefault
	protected PlcMetamodelUtil metamodelUtil;
	
	
 	public PlcComponentUtil() { 
 		
 	}

	/**
	 * Modifica a propriedade informada em uma EL, utilizando o PLC_MANAGED_BEAN_KEY.
	 * Se já for uma expressão, não altera nada.
	 * Se tive no escopo de requisição um managed bean, inclui na expressão.
	 * Se não, apenas coloca a propriedade como expressão. 
	 * @param propriedade
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String transformProperty(String propriedade, boolean includeManagedBeanIfExpression) {
		return transformProperty(PlcJsfConstantes.PLC_MANAGED_BEAN_KEY, propriedade, includeManagedBeanIfExpression);
	}
	
	public String transformProperty(String managedBeanKey, String propriedade, boolean includeManagedBeanIfExpression) {

		boolean expression = elUtil.isExpression(propriedade);
		
		//ManagedBean só será processado se não for expressão ou tiver que incluir.
		String managedBean = !expression || includeManagedBeanIfExpression ? (String) contextUtil.getRequestAttribute(managedBeanKey) : null;
		
		if (expression) {
			if (includeManagedBeanIfExpression && managedBean!=null && managedBean.trim().length()>0) {
				int indIni = propriedade.indexOf("#{");
				
				return propriedade.substring(0, indIni+2) + managedBean + '.' + propriedade.substring(indIni+2);
			}
			return propriedade;
		}
		
		if (managedBean!=null && managedBean.contains(".")){
			propriedade = managedBean+"."+propriedade;
		}
		
		if (managedBean!=null && managedBean.trim().length()>0 && managedBean.indexOf(".")==-1) {
			return "#{"+managedBean+"."+propriedade+"}";
		}
		
		return "#{"+propriedade+"}";
	}
	
	/**
	 * Retorna um valor para um evento [onkeyup ou onkeydown] considerando o formato.
	 * @param evento  Evento a ser considerado	
	 * @param format, Formato de entrada de dados para o campo, podendo ser um dentre  os seguintes: 
	 * [numerico|alfanumerico|ALFANUMERICO|alfabetico|ALFABETICO] 
	 * -> numerico		: Aceita somente números. 
	 * -> alfanumerico	: Aceita letras minúsculas e numeros. 
	 * -> ALFANUMERICO	: Aceita letras maiúsculas e numeros. 
	 * -> alfabetico	: Aceita letras minúsculas. 
	 * -> ALFABETICO	: Aceita letras maiúsculas.
	 *  
	 * @return Valor para o evento, geralmente uma função javaScript 
	 */
	public String  getScriptFormat(FacesBean bean, PropertyKey evento, String format){
		
		final String CONVERTE_MAIUSCULA = "converteMaiuscula(event, this);"; 
		final String CONVERTE_MINUSCULA = "converteMinuscula(event, this);";
		final String VALIDA_CARACTER = "return validaCaracter(this, event, 'V');";
		final String VALIDA_ALFABETICO = "return validaCaracter(this, event, 'L');";
		
		String value = "";
		if (CoreInputText.ONKEYUP_KEY.equals(evento)){  // onkeyup
			
			String onKeyUp = StringUtils.isNotBlank((String)bean.getProperty(evento))?(String)bean.getProperty(evento):"";

			if (onKeyUp.toString().contains(CONVERTE_MAIUSCULA) || onKeyUp.toString().contains(CONVERTE_MINUSCULA) || 
					onKeyUp.toString().contains(VALIDA_CARACTER) )
				return onKeyUp;
			
			if ("ALFABETICO".equals(format) || "ALFANUMERICO".equals(format)  || "MAIUSCULO".equals(format) || SimpleFormat.UPPER_CASE.toString().equals(format))
				value = onKeyUp.concat(";").concat(CONVERTE_MAIUSCULA);

			if ("alfabetico".equals(format) || "alfanumerico".equals(format)  || "MINUSCULO".equals(format) || SimpleFormat.LOWER_CASE.toString().equals(format))
				value = onKeyUp.concat(";").concat(CONVERTE_MINUSCULA);

			if ("numerico".equals(format) || "NUMERICO".equals(format))
				value = onKeyUp.concat(";").concat(VALIDA_CARACTER);

		}
		else{
			if (CoreInputText.ONKEYDOWN_KEY.equals(evento) || CoreInputText.ONKEYPRESS_KEY.equals(evento)){  // onkeydown
				
				if ("numerico".equalsIgnoreCase(format) || SimpleFormat.NUMBER.toString().equals(format))
					value = VALIDA_CARACTER ;

				if ("alfabetico".equalsIgnoreCase(format))
					value = VALIDA_ALFABETICO;
			}
		}
		
		return value;
	}
	
	/**
	 * Busca do request o nome da coleção do detalhe atual.
	 * @return nome do detalhe, ou nulo se não tiver sido definido.
	 */
	public String getCurrentDetail() {
		Object ret = contextUtil.getRequestAttribute(PlcJsfConstantes.PLC_COLECAO_DETALHE_NOME);
		return ret==null||StringUtils.isEmpty(ret.toString()) ? null : ret.toString();
	}

	/**
	 * Busca do request o nome da coleção do sub detalhe atual.
	 * @return nome do detalhe, ou nulo se não tiver sido definido.
	 */
	public String getCurrentSubDetail() {
		Object ret = contextUtil.getRequestAttribute(PlcJsfConstantes.PLC_COLECAO_SUB_DETALHE_NOME);
		return ret==null||StringUtils.isEmpty(ret.toString()) ? null : ret.toString();
	}
	
	/**
	 * Retorna se o componente deve ou não ser validado, ou seja está marcado ou não para validar
	 */
	public boolean avoidValidationTabular() {
		return Boolean.TRUE.equals(contextUtil.getRequestAttribute(PlcJsfConstantes.VALIDACAO.EVITA_VALIDACAO_TABULAR));
	}
	
	/**
	 * Busca o componente gerador do evento (baseado no atributo "source") a partir do componente
	 * indicado.
	 * @param rootComponent Componente raiz para a procura do componente.
	 * @return componente apontado como fonte do evento, ou null se nenhum for encontrado.
	 */
	public UIComponent getSourceComponent(UIComponent rootComponent) {
		
		String source = contextUtil.getRequestParameter("source");
		
		if (source==null || source.trim().length()==0) {
			source = contextUtil.getRequestParameter("javax.faces.source");
		}
		if (source==null || source.trim().length()==0) {
			return null;
		}
		
		UIComponent sourceComponent = findComponent(rootComponent, source);
		
		return sourceComponent;
	}
	
	/**
	 * Implementação da busca por componente na árvore, corrigindo problema com Iterator.
	 * @param base componente base
	 * @param id id do componente procurado.
	 * @return
	 */
	public UIComponent findComponent(UIComponent base, String id) {
		
		String[] segments = id.split(":");
		
		if (segments.length==1) {
			return base.findComponent(id);
		}
		
		UIComponent result = base;
		for (int i=0; i<segments.length; i++) {
			
			if (result != null && result.findComponent(segments[i]) != null) {
				result = result.findComponent(segments[i]);
			}
			
			if (result instanceof UIXIterator && i<segments.length-1 && NumberUtils.isNumber(segments[i+1])) {
				i++;
			}
		}
		return result;
	}
	
	/**
	 * Procura um componente ancestral que seja do tipo especificado.
	 * @param <T> tipo procurado.
	 * @param rootComponent componente alvo da procura
	 * @param tipoProcurado Classe do tipo procurado.
	 * @return ancestral do tipo indicado ou null se não encontrar.
	 */
	public <T extends UIComponent> T getParent(UIComponent rootComponent, Class<T> tipoProcurado) {
		
		UIComponent component = rootComponent;
		
		for (; component!=null && !tipoProcurado.isAssignableFrom(component.getClass()); component = component.getParent());
		
		return component==null ? null : tipoProcurado.cast(component);
	}
	
	/**
	 * Busca o componente de formulário, ancestral ao componente indicado.
	 * Procura por UIXForm e UIForm, respectivamente.
	 * @param component componente base
	 * @return componente de formulário
	 */
	public UIComponent getFormComponent(UIComponent component) {
		UIXForm form = getParent(component, UIXForm.class);
		return form!=null?form:getParent(component, UIForm.class);
	}
	
	
	/**
	 * Metodo que verifica se uma propriedade já foi definida em um bean,
	 * sem que pra isso resolva a propriedade (no caso de valueBindins).
	 * @param bean
	 * @param propertyKey
	 * @return true se a propriedade informada já foi definida.
	 */
	public boolean isValueDefined(FacesBean bean, PropertyKey propertyKey) {
		return bean.keySet().contains(propertyKey)||bean.bindingKeySet().contains(propertyKey);
	}
	
	/**
	 * Método que busca a expressão que define uma propriedade do bean.
	 * Se a propriedade não for definida por uma expressão, retorna null.
	 * @param bean
	 * @param propertyKey
	 * @return a string contendo a expressão que define a propriedade ou null caso não seja definido por value binding.
	 */
	public String getExpression(FacesBean bean, PropertyKey propertyKey) {
		if (isValueDefined(bean, propertyKey)) {
			ValueExpression valueExpr = bean.getValueExpression(propertyKey);
			String expressionString = valueExpr==null?null:valueExpr.getExpressionString();
			if (expressionString == null){
				
				ValueBinding valueBinding = bean.getValueBinding(propertyKey);
				
				if (valueBinding instanceof ValueExpressionValueBinding) {
					expressionString = ((ValueExpressionValueBinding)valueBinding).getValueExpression().getExpressionString();
				}
			}
			return expressionString;
		}
		return null;
	}
	
	/**
	 * Busca a propriedade do bean que está em Value.
	 * Só deve ser utilizado a partir de uma tag JSP, e não de um Componente,
	 * pois utiliza atributo de requisição.
	 * Espera que o Bean tenha uma propriedade chamada "value", ou dará erro.
	 * A propriedade deve ser um ValueBinding, ou não retornará nada.
	 * Retorna eliminando argumentos e valor do inicio e fim, para caso de argumento.
	 * @param bean Bean que contém o valor.
	 * @return a propriedade procurada, ou null se value não é um ValueBinding.
	 */
	public String getValueProperty(FacesBean bean) {
		PropertyKey valueKey = bean.getType().findKey("value");
		String expressao = getExpression(bean, valueKey);
		if (expressao != null) {
			PropertyKey beanKey = bean.getType().findKey("plcManagedBean");
			// Tenta buscar do bean a propriedade managedBean
			String managedBeanName = beanKey != null && isValueDefined(bean, beanKey) ? (String) bean.getProperty(beanKey) : (String) contextUtil.getRequestAttribute(PlcJsfConstantes.PLC_MANAGED_BEAN_KEY);
			
			//Verifica se o MB esta na expressão, caso não estaja verifica se existe detalhe 
			if(expressao.indexOf(managedBeanName) < 0 && !expressao.contains("msg")){
				if(null != contextUtil.getRequestAttribute(PlcJsfConstantes.PLC_COLECAO_DETALHE_NOME) && contextUtil.getRequestAttribute(PlcJsfConstantes.PLC_COLECAO_DETALHE_NOME).toString() != null){
					managedBeanName = contextUtil.getRequestAttribute(PlcJsfConstantes.PLC_COLECAO_DETALHE_NOME).toString(); 
				}				
			}
			
			int pos1 = -1;
			// busca a posiçao do managedBean na expressao
			if (managedBeanName != null)
				pos1 = expressao.indexOf(managedBeanName);
			// se achou, acrescenta o tamanho do managedbean
			if (pos1 >= 0)
				pos1 += managedBeanName.length();

			// Se achou o managedBean, vai até o próximo ponto, para retirar
			// colchetes
			int pos2 = pos1 >= 0 ? expressao.indexOf('.', pos1) : expressao.lastIndexOf('.');
			// Se nao achou, procura o ultimo ponto.
			// se nao achou nem o managed bean nem um ponto, considera toda a
			// expressao.
			if (pos1 < 0 && pos2 < 0) {
				pos2 = expressao.indexOf('{');
			}

			String prop = expressao.substring((pos2 >= 0 ? pos2 : pos1) + 1, expressao.lastIndexOf('}')).trim();

			return prop;

		}
		return null;
	}
	
	/**
	 * Retorna Serviço de I18n
	 */
	public PlcI18nUtil getUtilI18n() {
		return (PlcI18nUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcI18nUtil.class, QPlcDefaultLiteral.INSTANCE);
	}
	
	/**
	 * Retorna o nome do detalhe a partir do id do componente
	 */
	public String findDetail(FacesContext context, UIComponent component) {

		String detalhe = null;
		// Caso não achou detalhe, verifica pelo id da tag
		String id = component.getClientId(context);

		if (StringUtils.isNotBlank(id)){

			Matcher matcher = Pattern.compile("corpo:formulario:(\\w+):(\\d+):(\\w+)").matcher(id);

			if (matcher.find()){
				detalhe = matcher.group(1);
			}
			
			matcher = Pattern.compile("corpo:formulario:(\\w+):(\\d+):(\\w+):(\\d+):(\\w+)").matcher(id);
			
			if (matcher.find()){
				detalhe = matcher.group(1) + "[]." + matcher.group(3);
			}
			
		}

		return detalhe;
	}
	
	
	@SuppressWarnings("unchecked")
	public void checkSecurity (FacesBean bean, PropertyKey key, String objetoIndexado, String chave){
		
		if(objetoIndexado != null && objetoIndexado.equals("plcLogicaItens")){
			objetoIndexado = null;
		}
		
		StringBuilder objetoIndexadoSeguranca = new StringBuilder();
		
		if (chave != null) {
			
			if(chave.contains("idNatural.")) {
				chave = chave.replace("idNatural.", "idNatural_");
			}
			
			objetoIndexadoSeguranca.append(objetoIndexado!=null?objetoIndexado:"").append(".").append(chave);
			
			HttpServletRequest request = contextUtil.getRequest();
			
			Map<String,Boolean> m = (Map<String,Boolean>) request.getAttribute(PlcConstants.GUI.MAPA_SEGURANCA);
			
			if (m != null && (m.containsKey(objetoIndexadoSeguranca.toString()) || m.containsKey(chave.toString() ))){
				bean.setProperty(key, false);
			}
			
			if (m != null && m.containsKey(objetoIndexadoSeguranca.toString()+"_" + PlcTagUtil.DISABLED)){
				key = bean.getType().findKey(PlcTagUtil.DISABLED);
				bean.setProperty(key, true);
			}
		}
		
		
	}
	
	/**
	 * Verifica segurança para label 
	 * @param expressionString - Chave a ser verificada
	 * @return true ou false para esconder ou não esconder o label
	 */
	public Boolean checkSecurityLabel(String expressionString){
		
		if(expressionString != null){
			if(expressionString.contains("label")){
				String expressionStringLabel = expressionString.subSequence(expressionString.indexOf("['")+2, expressionString.indexOf("']")).toString();
				
				HttpServletRequest request = contextUtil.getRequest();
				
				Map<String,Boolean> m = (Map<String,Boolean>) request.getAttribute(PlcConstants.GUI.MAPA_SEGURANCA);
				
				if(m != null && m.containsKey(expressionStringLabel)){
					return true;
				}
			}						
		}
		
		return false;
	}
	
	public void checkSecurityDetail(FacesBean bean, String detalhe, FacesContext context)  {
		
		
		boolean isProtegido = false;

		if (StringUtils.isNotBlank(detalhe)){

			if (detalhe.contains("[]"))
				detalhe = detalhe.substring(0, detalhe.indexOf("[]"));

			HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();

			Map mapaSeguranca = (Map)request.getAttribute(PlcConstants.GUI.MAPA_SEGURANCA);
			PlcConfigDetail[] detalhes = configUtil.getConfigAggregation(urlUtil.resolveCollaborationNameFromUrl(request)).details();

			Class entidadeDetalhe = null;
			for (PlcConfigDetail plcConfigDetalhe : detalhes) {
				if (detalhe.equals(plcConfigDetalhe.collectionName()))
					entidadeDetalhe = plcConfigDetalhe.clazz();
			}

		}
		
		if (isProtegido) {
			bean.setProperty(bean.getType().findKey("disabled"), isProtegido);
		}
	}
	
	/**
	 * Evita a validação se o flag desprezar não foi informado.
	 * 
	 * Se o componente possui o componenteFlagDesprezar, ou seja é de lógica tabular ou detalhe,
	 * verifica o se o flag desprezar foi informado ou não. 
	 *  
	 */
	public boolean avoidValidationByDespiseField(FacesBean bean , PropertyKey keyComponenteDesprezar, String idComponent) {
		
		EditableValueHolder componenteDesprezar = (EditableValueHolder)bean.getProperty(keyComponenteDesprezar);

		// Se componenteDesprezar é nulo, ou seja componente de lógica diferente de Tabular e Detalhe,
		// tem que validar
		if ( componenteDesprezar == null ) {
			
			HttpServletRequest request = ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()); 
			String skipValidation = request.getParameter("skipValidation");
			
			if(Boolean.parseBoolean(skipValidation)) {
				return true;
				
			} else {
				return false;
			}
		}
		
		// Verificando o valor atual do componente de flag desprezar
		//Verifica se há algum parâmetro de request com o nome (client id) do componente, e se esse não é vazio.
		String clientId = ((UIComponent)componenteDesprezar).getClientId(FacesContext.getCurrentInstance());
		String value = contextUtil.getRequestParameter(clientId);

		if (StringUtils.isBlank(value)){
			clientId = idComponent.substring(0,idComponent.lastIndexOf(":") + 1) + ((UIComponent)componenteDesprezar).getId();
			value = contextUtil.getRequestParameter(clientId);
			
			Object detalhe = null;
			UIComponent parent = ((UIComponent)componenteDesprezar).getParent();

			while (parent != null) {
				
				if (parent instanceof UIXIterator){
					String var = ((UIXIterator)parent).getVar();
					detalhe = elUtil.evaluateExpressionGet("#{" + var + "}", Object.class);
					break;
				}
				
				parent = parent.getParent(); 
			}
			
			if (detalhe != null){
				PlcEntityInstance detalheInstance = metamodelUtil.createEntityInstance(detalhe);
				// evita falha na validação caso já exista o detalhe.
				if (detalheInstance.getId() != null){
					return false;
				}
			}
			
		}
		
		return StringUtils.isBlank((String)value);
	}
	
	/**
	 * Em caso do obrigatório redefine mensagens de obrigatoriedade para o componente, 
	 * pois o trinidad busca as mensagens em arquivos de resources internos, e o padrão 
	 * jcompany e buscar nos bundles da aplicação.
	 * 
	 */
	public void handleRequiredComponent(FacesContext context, UIXEditableValue componente) {
		FacesMessage message = getRequiredFacesMessage(context,componente);
		context.addMessage(componente.getClientId(context), message);
		componente.setValid(false);

	}
	
	/**
	 * Redefinindo Mensagens de obrigatoriedade para componente, pois o trinidad busca as
	 * mensagens em arquivos de resources internos, e o padrão jcompany e buscar nos bundles da
	 * aplicação 
	 */
	public FacesMessage getRequiredFacesMessage(FacesContext context, UIXComponentBase componente) {
		
		Object propriedade 	= componente.getAttributes().get("propriedade");
		
		Object label 				= getLabel(componente);

		if (label == null) {
			label = getUtilI18n().mountLocalizedMessageAnyBundle(contextUtil.getRequest(),"label."+propriedade, new String[]{});
		}
		
		String msgErro	= getUtilI18n().getMessageCal10n(contextUtil.getRequest(), PlcBeanMessages.ERRORS_REQUIRED, new String[]{(String)label});

		try {
			
			msgErro = completeMessageWithLineAndCollection(msgErro, componente);
			
		} catch (Exception e) {
			log.error( e.getMessage());
		}
		
		return new LabeledFacesMessage(FacesMessage.SEVERITY_ERROR, msgErro, (String)propriedade, "");
	}
	
	/**
	 * Complementa uma mensagem de validação para um componente com a linha e nome da coleção a que pertencem.
	 * @param mensagem, Mensagem original
	 * @return  Retorna a mensagem alterada
	 */
	public String completeMessageWithLineAndCollection(String mensagem, UIComponent componente){
		
		/* Se for um componente de uma coleção, na mensagem de obrigagoriedade deve ser
		 * adicionado a linha do componente e a o nome do detalhe, se for detalhe.
		 */ 
		PlcConfigAggregationPOJO configAcao = configUtil.getConfigAggregation(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest()));
		
		try {

			Map status = (Map) contextUtil.getRequestAttribute(PlcJsfConstantes.PLC_ITENS_STATUS);
		 
			if ( status != null && isValidPattern(configAcao)) {

				// Adicionando na mensagem número da linha na coleção de detalhe ou tabular
				Integer[] linha = (Integer[])status.get("indexCompleto");
				StringBuilder numLinha= new StringBuilder();
				if (linha != null) {
					for (Integer numberLine : linha) {
						if (numLinha.length() > 0) {
							numLinha.append('.');
						}
						numLinha.append(numberLine + 1);
					}
				}
				
				mensagem = getMessageWithLineNumber(mensagem, componente, numLinha.toString());
			} 
			// Retorna a mensagem montada ou a original
			return mensagem;

		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcComponentUtil", "completeMessageWithLineAndCollection" , e, log, null);
		}
	}
	
	/**
	 * Busca o nome da ultima propriedade a partir do ValueExpression.
	 * O nome será o ultimo atributo, depois do ultimo ponto (.).
	 * @param valueExpression expressão se onde será induzido o nome da coleção.
	 * @return nome da propriedade, ou nulo se não achar.
	 */
	public String getLastPropertyName(ValueExpression valueExpression) {
		
		String colecaoNome = null;
		
		if (valueExpression!=null) {
			String expressionString = valueExpression.getExpressionString();
			Matcher matcher = patternUltimaPropriedade.matcher(expressionString);
			if (matcher.find()) {
				//Tem um detalhe
				colecaoNome = matcher.group(1);
			}
		}
		return colecaoNome;
	}
	
	public String[] getExpressionTokens(String expressao) {
		if (expressao.contains("#") || expressao.contains(".")) {
			ArrayList<String> lista = new ArrayList<String>();
			Matcher m = patternPartesExpressao.matcher(expressao);
			while (m.find()) {
				lista.add(m.group());
			}
			return lista.toArray(new String[lista.size()]);
		}
		return new String[]{expressao};
	}
	
	/**
	 * Verifica a igualdade entre duas {@link ValueExpression}s.
	 * @param v1
	 * @param v2
	 * @return true se as duas expressões são equivalentes.
	 */
	public boolean equalsValueExpression(ValueExpression v1, ValueExpression v2) {
		String[] expressaoTokens = getExpressionTokens(v1.getExpressionString());
		String[] expressaoTokens2 = getExpressionTokens(v2.getExpressionString());
		
		return Arrays.equals(expressaoTokens, expressaoTokens2);
	}
	
	/**
	 * Recupera O ManagedBean através da expressão
	 */
	public Object getManagedBean(FacesBean bean ) {
		
		String valor = componentUtil.getExpression(bean, UIXValue.VALUE_KEY);
		
		if ( valor == null) {
			return null;
		}
		
		String expressao 	= valor.replace("#{", "").replace("}", "");
		if (expressao.indexOf(".")>-1) {
			String managedBean 	= expressao.substring(0, expressao.indexOf("."));
		//	Recuperando o managedBean. Atráves da expressão não importa a Lógica {entityPlc ou itensPlc}
			return elUtil.evaluateExpressionGet("#{"+managedBean+"}", Object.class);
		} else
			return elUtil.evaluateExpressionGet("#{plcAction.entityPlc}", Object.class);

	}
	
	/**
	 * Método auxiliar para montagem de mensagem localizada I18n de forma simplificada para componentes.
	 * Recebe apenas o FacesBean, a chave e os parâmetros, facilitando a chamada ao {@link PlcI18nUtil}.
	 * @param bean bean do componente atual. Se contiver uma chave "bundle", esse bundle será usado para o localizador.
	 * @param key chave no bundle.
	 * @param parametros parâmetros a serem passados para o valor do bundle.
	 * @return Valor convertido se acordo com o bundle.
	 * @author Bruno Grossi
	 * @see PlcI18nUtil#mountLocalizedMessage(String, java.util.Locale, String, String[])
	 */
	public String createLocalizedMessage(FacesBean bean, String key, Object ... parametros) {
		
		String propertyBundle = componentPropertiesUtil.getPropertyBundle(bean);
		
		HttpServletRequest request = contextUtil.getRequest();
		
		return getUtilI18n().mountLocalizedMessageAnyBundle(request, propertyBundle, key, parametros==null ? new String[]{} : parametros);
		
	}
	
	
	/**
	 * Imprime uma árvore de componentes, a partir do componente informado.
	 * Método utilizado apenas para debug da aplicação. 
	 * Não deve ser utilizado em produção.
	 * @throws IOException 
	 */
	public void printComponentTree(OutputStream out, UIComponent component) throws IOException {
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(out));
		printComponentTree(bufferedWriter, component, 0);
		bufferedWriter.flush();
	}
	

	public <BeanMessage extends Enum<?>> String getMessage(BeanMessage chave, Object[] args) {
		PlcI18nUtil i18nUtilI = componentUtil.getUtilI18n();
		HttpServletRequest request = contextUtil.getRequest();
		String mensagem = i18nUtilI.getMessageCal10n(request, chave, args);
		return mensagem;
	}
	
	public boolean setLabel(UIComponent component) {

		FacesBean facesBean = ((UIXComponent)component).getFacesBean();

		PropertyKey LABEL_KEY = facesBean.getType().findKey("label");

		if (facesBean.getProperty(LABEL_KEY)==null) {

			Object customMessageDetail = facesBean.getProperty(facesBean.getType().findKey("requiredMessageDetail"));
			if (customMessageDetail==null) 
				customMessageDetail = facesBean.getProperty(facesBean.getType().findKey("propriedade"));
			try {
				PlcI18nUtil plcI18nService = componentUtil.getUtilI18n();
				facesBean.setProperty(LABEL_KEY, plcI18nService.mountLocalizedMessageAnyBundle(contextUtil.getRequest(),"label."+customMessageDetail, new String[]{}));
			} catch (Exception e) {
				log.warn( "PlcData.validate: "+e);
			}

			return true;
		}
		return false;
	}
	
	public void addLabel(UIComponent component) {
		
		FacesBean facesBean = ((UIXComponent)component).getFacesBean();

		PropertyKey LABEL_KEY = facesBean.getType().findKey("label");
		
		PropertyKey PROPRIEDADE_KEY = facesBean.getType().findKey("propriedade");
		
		// Caso não tenha label, adicionao a chave
		if(facesBean.getProperty(LABEL_KEY) == null) {
			if(facesBean.getProperty(PROPRIEDADE_KEY) != null) {
				String label = (String) facesBean.getProperty(PROPRIEDADE_KEY);
				if(StringUtils.isNotBlank(label)) {
					if(label.contains(".")) {
						label = label.substring(label.lastIndexOf(".") + 1, label.length());
					}
					label = label.substring(0,1).toUpperCase() + label.substring(1, label.length());
				}
				facesBean.setProperty(LABEL_KEY, label );
			}
		}
	}

	public void unsetLabel(UIComponent component) {
		FacesBean facesBean = ((UIXComponent)component).getFacesBean();
		PropertyKey LABEL_KEY = facesBean.getType().findKey("label");
		facesBean.setProperty(LABEL_KEY, null);
	}

	private String getMessageWithLineNumber(String mensagem, UIComponent componente, String numLinha) {
		
		String chaveI18nDetalhe = "";
		String msgLinha			= 	getUtilI18n().getMessageCal10n(contextUtil.getRequest(), PlcBeanMessages.JCOMPANY_ERROR_LINE_N, new String[]{numLinha.toString()});
		PlcConfigAggregationPOJO configAcaoCorrente =  configUtil.getConfigAggregation(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest()));
		FormPattern logica 			= configAcaoCorrente.pattern().formPattern();
		// Se for metre-detalhe/subdetalhe ou mantém detalhe/subdetalhe adiciona o nome da coleção
		if ( (logica.equals(FormPattern.Mad)) || (logica.equals(FormPattern.Mas)) || (logica.equals(FormPattern.Mdt))|| (logica.equals(FormPattern.Mds)) ){
			// Adicionando na mensagem o nome da coleção, no padrão jcompany [ nome do detalhe ]
			chaveI18nDetalhe = ((IPlcComponent)componente).getPropertyChaveI18nDetalhe();
			if (chaveI18nDetalhe != null){
				chaveI18nDetalhe 	=  getUtilI18n().mountLocalizedMessageAnyBundle(contextUtil.getRequest(), chaveI18nDetalhe, new String[]{});
				chaveI18nDetalhe 	= " [ " + chaveI18nDetalhe + " ]";
			}
			else {
				// Se não tiver detalhe não adiciona na mensagem
				chaveI18nDetalhe = "";
			}
		}
		// monta a mensagem
		if(mensagem.endsWith(".")) {
			mensagem = mensagem.substring(0, mensagem.length() - 1);
		}
		mensagem = mensagem + " " + msgLinha + chaveI18nDetalhe;
		return mensagem;
	}

	private boolean isValidPattern(PlcConfigAggregationPOJO configAcao) {
		return ((configAcao.pattern().formPattern() == FormPattern.Tab) ||
				(configAcao.pattern().formPattern() == FormPattern.Mad) ||
				(configAcao.pattern().formPattern() == FormPattern.Mas) ||
				(configAcao.pattern().formPattern() == FormPattern.Mdt) ||
				(configAcao.pattern().formPattern() == FormPattern.Mds));
	}
	

	// We currently use 'label' for the validation failed message
	private Object getLabel(UIXComponentBase componente) {
		return componente.getFacesBean().getProperty(componente.getFacesBean().getType().findKey("label"));
	}
	
	private void printComponentTree(BufferedWriter out, UIComponent component, int nivel) throws IOException {
		for(int i=0; i<=nivel; i++) {
			out.write('-');
		}
		out.write(component.toString());
		out.newLine();
		
		if (component.getChildCount()>0) {
			List<UIComponent> children = component.getChildren();
			for (UIComponent child : children) {
				printComponentTree(out, child, nivel+1);
			}
		}
		
	}	
}