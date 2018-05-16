/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.renderer;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.component.UIXComponentBase;
import org.apache.myfaces.trinidad.component.core.input.CoreInputText;
import org.apache.myfaces.trinidad.component.html.HtmlTableLayout;
import org.apache.myfaces.trinidad.context.RenderingContext;
import org.apache.myfaces.trinidad.context.RequestContext;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.InputTextRenderer;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.XhtmlUtils;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcNamedLiteral;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcConversationControl;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcViewJsfUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcAggregate;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcMsgComponentsUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcRendererUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;

/**
 * Especialização do renderer base InputTextRenderer para permitir IoC e DI nos renderes JSF/Trinidad. 
 * @author Pedro Henrique Neves
 * 
 */
public class PlcAggregateRenderer extends InputTextRenderer {

	protected static final Logger	logVisao									= Logger.getLogger(PlcAggregateRenderer.class.getCanonicalName());

	// Definindo o tipo do renderer
	static public final String		RENDERER_TYPE								= "com.powerlogic.jsf.Vinculado";

	private final String			_PANEL_FORM_LAYOUT_FORM_ITEM_KEY			= "org.apache.myfaces.trinidadinternal.PanelFormLayoutFormItem";

	/**
	 * Request map key for child renderers to inspect to see if their labels should be stacked above their fields as opposed to the side.
	 */
	protected static final String	PANEL_FORM_LAYOUT_LABELS_START_ALIGNED_KEY	= "oracle.adfinternal.PanelFormLayoutLabelsStartAligned";

	/**
	 * IoC do jcompany. Adicioanando componentes HTML antes e depois de renderizar o inpuText, para formar o 
	 * componente agregado
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected void encodeAll(FacesContext context, RenderingContext arc, UIComponent component, FacesBean bean) throws IOException {

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcMsgComponentsUtil msgComponentsUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgComponentsUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcConfigUtil configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);

		try {

			if (PlcCDIUtil.getInstance().getInstanceByType(PlcViewJsfUtil.class, QPlcDefaultLiteral.INSTANCE).textMode()) {
				new PlcTitleRenderer().encodeAll(context, arc, component, bean);
				return;
			}
			
			
			PlcConversationControl controleConversasao = PlcCDIUtil.getInstance().getInstanceByType(PlcConversationControl.class, new PlcNamedLiteral(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_CONVERSACAO));

			TreeMap<String, Object> propsChaveNatural = (TreeMap<String, Object>) bean.getProperty(PlcAggregate.PROPS_CHAVE_NATURAL_PLC);
			String autoRecuperacaoPropriedade = (String) bean.getProperty(PlcAggregate.AUTO_RECUPERACAO_PROPRIEDADE_KEY);
			String autoRecuperacaoClasse = (String) bean.getProperty(PlcAggregate.AUTO_RECUPERACAO_CLASSE_KEY);

			String detalhe = componentUtil.getCurrentDetail();
			String propriedade = (String) bean.getProperty(PlcAggregate.PROPRIEDADE_KEY);
			String idTamanho = (String) bean.getProperty(PlcAggregate.ID_TAMANHO_KEY);

			StringBuilder objetoIndexadoSeguranca = new StringBuilder();

			if(StringUtils.isNotBlank(detalhe)) {
				objetoIndexadoSeguranca.append(detalhe).append(".");
			}
			objetoIndexadoSeguranca.append(propriedade);
			HttpServletRequest request = contextUtil.getRequest();

			Map<String, Boolean> m = (Map<String, Boolean>) request.getAttribute(PlcConstants.GUI.MAPA_SEGURANCA);

			String modoPlc = controleConversasao.getModoPlc();

			if ((m != null && m.containsKey(objetoIndexadoSeguranca.toString()))) {
				bean.setProperty(UIXComponentBase.RENDERED_KEY, false);
				return;
			} else {

				boolean needsPanelFormLayout = inicioComponent(context, bean, component);

				ResponseWriter writer = context.getResponseWriter();

				String clientId = component.getClientId(context);

				String complementoNome = "vinculado-";
				if (bean.getProperty(PlcAggregate.PROPSEL_POP_KEY)!=null && ((ValueExpression)bean.getProperty(PlcAggregate.PROPSEL_POP_KEY)).getValue(context.getELContext())!=null) {
					complementoNome = complementoNome + ((ValueExpression)bean.getProperty(PlcAggregate.PROPSEL_POP_KEY)).getValue(context.getELContext())+"-";	
				}
				writer.writeAttribute("id", complementoNome + clientId, null);

				registraPartialSubmit(context, component);

				if (!needsPanelFormLayout) {

					String styleClass = (String) bean.getProperty(CoreInputText.STYLE_CLASS_KEY);
					// Estilos fixos retiram a flexibilidade!
					if (styleClass != null)
						writer.writeAttribute("class", bean.getProperty(CoreInputText.STYLE_CLASS_KEY).toString(), null);
					if (bean.getProperty(CoreInputText.INLINE_STYLE_KEY) != null)
						writer.writeAttribute("style", bean.getProperty(CoreInputText.INLINE_STYLE_KEY).toString(), null);
					writer.writeAttribute("onkeydown", "selecionaPorTecla(event,this);", null);

					String obrigatorio = (String) bean.getProperty(PlcAggregate.OBRIGATORIO_KEY);
					boolean required = (Boolean) bean.getProperty(PlcAggregate.REQUIRED_KEY);
					//if (obrigatorio != null || required ) {
					if ("S".equals(obrigatorio) || required) {
						writer.writeAttribute("class", (styleClass != null ? styleClass + " " : "") + "p_AFRequired", null);
					}
					//}

				}

				// input para código
				String idSomenteLeitura = (String) bean.getProperty(PlcAggregate.ID_SOMENTE_LEITURA_KEY);
				String idExibe = (String) bean.getProperty(PlcAggregate.ID_EXIBE_KEY);
				String somenteLeitura = (String) bean.getProperty(PlcAggregate.SOMENTE_LEITURA);
				if ("S".equals(somenteLeitura)) {
					idSomenteLeitura = "S";
				}

				//Recuperando a entidade corrente, não importa a Lógica {entityPlc ou itensPlc}
				if (StringUtils.isNotBlank(autoRecuperacaoPropriedade)) {
					// Para quando for campo diferente de id

					if (propsChaveNatural != null && !propsChaveNatural.isEmpty()) {

						imprimeChaves(context, component, bean, propsChaveNatural, autoRecuperacaoClasse, propriedade, idTamanho, writer, idSomenteLeitura, "N");

					} else {

						writer.startElement("input", component);
						writer.writeAttribute("type", "hidden", null);
						writer.writeAttribute("id", clientId, null);
						writer.writeAttribute("name", clientId, null);
						writer.writeAttribute("value", bean.getProperty(PlcAggregate.VALUE_KEY), null);
						writer.writeAttribute("title", bean.getProperty(PlcAggregate.SHORT_DESC_KEY), null);
						//writer.writeAttribute("onchange", "autoRecuperacaoVinculado('"+component.getClientId(context)+"');return false;", null);
						writer.writeAttribute("onkeydown", "selecionaPorTecla(event,this);", null);
						//if (idSomenteLeitura.equals("S"))
						//writer.writeAttribute("readonly", "true", null);
						writer.writeAttribute("size", "5", null);
						writer.endElement("input");

					}

					boolean limparValor = StringUtils.isBlank((String) bean.getProperty(PlcAggregate.LOOKUP_VALUE_KEY));

					writer.startElement("input", component);
					writer.writeAttribute("type", "text", null);
					// Layout N-UP
					if (needsPanelFormLayout)
						writer.writeAttribute("class", "af_inputText_content", null);

					writer.writeAttribute("id", clientId + ":" + autoRecuperacaoPropriedade, null);
					writer.writeAttribute("name", clientId + ":" + autoRecuperacaoPropriedade, null);
					writer.writeAttribute("value", limparValor == false ? bean.getProperty(PlcAggregate.AUTO_RECUPERACAO_PROPRIEDADE_VALUE) : "", null);
					if(isDisabled(component, bean)){
						writer.writeAttribute("disabled", "disabled", null);
					}
					writer.writeAttribute("title", bean.getProperty(PlcAggregate.SHORT_DESC_KEY), null);
					writer.writeAttribute("onchange", "autoRecuperacaoVinculado('" + clientId + "', \"" + clientId + ":" + autoRecuperacaoPropriedade + "\");return false;", null);
					writer.writeAttribute("onkeydown", "selecionaPorTecla(event,this);", null);
					Boolean disabled = PlcCDIUtil.getInstance().getInstanceByType(PlcViewJsfUtil.class, QPlcDefaultLiteral.INSTANCE).isReadOnly();
					Boolean chaveNaturalPreenchido = PlcTagUtil.checkNaturalKeyFilled(bean, propriedade);
					if (propriedade.startsWith("idNatural.")) {
						Boolean ehInclusao = "inclusaoPlc".equals(modoPlc);
						if (ehInclusao) {
							disabled = false;
						} else if (chaveNaturalPreenchido) {
							disabled = true;
						}
					}
					writer.writeAttribute("readonly", disabled, null);

					//if (idSomenteLeitura.equals("S"))
					//writer.writeAttribute("readonly", "true", null);
					writer.writeAttribute("size", idTamanho, null);
					writer.endElement("input");

				} else if (propsChaveNatural == null || propsChaveNatural.keySet().isEmpty()) {

					writer.startElement("input", component);
					writer.writeAttribute("type", idExibe.equalsIgnoreCase("S") ? "text" : "hidden", null);

					// Layout N-UP
					if (needsPanelFormLayout)
						writer.writeAttribute("class", "af_inputText_content", null);

					writer.writeAttribute("id", clientId, null);
					writer.writeAttribute("name", clientId, null);
					writer.writeAttribute("value", bean.getProperty(PlcAggregate.VALUE_KEY), null);
					writer.writeAttribute("title", bean.getProperty(PlcAggregate.SHORT_DESC_KEY), null);
					writer.writeAttribute("onchange", "autoRecuperacaoVinculado('" + clientId + "', null);return false;", null);
					writer.writeAttribute("onkeydown", "selecionaPorTecla(event,this);", null);
					if (StringUtils.isBlank(autoRecuperacaoClasse))
						writer.writeAttribute("readonly", PlcTagUtil.checkNaturalKeyFilled(bean, propriedade), null);
					if (isDisabled(component, bean))
						writer.writeAttribute("disabled", "disabled", null);
					
					if (idSomenteLeitura.equals("S")||isDisabled(component, bean))
						writer.writeAttribute("readonly", "true", null);
					writer.writeAttribute("size", idTamanho, null);
					writer.endElement("input");

				} else {
					// Se não for autoRecuperação o valor deve ser limpado caso não tenha descrição. Ex: Limpar dos argumentos	
					imprimeChaves(context, component, bean, propsChaveNatural, autoRecuperacaoClasse, propriedade, idTamanho, writer, idSomenteLeitura, idExibe);
				}

				String defaultRiaParameters = getDefaultRiaParameters(context, component, bean);
				String defaultRia = getDefaultRia(context, component, bean);
				String customRia = getCustomRia(context, component, bean);
				boolean usaUnobtrusiveRia = PlcRendererUtil.includeRiaTemplates(clientId, defaultRiaParameters, defaultRia, customRia, context.getResponseWriter());

				//input para descrição
				writer.startElement("input", component);
				writer.writeAttribute("type", "text", null);

				// Layout N-UP
				if (needsPanelFormLayout)
					writer.writeAttribute("class", "af_inputText_content", null);

				writer.writeAttribute("id", "lookup_" + clientId, null);
				writer.writeAttribute("name", "lookup_" + clientId, null);
				Object valorLookup = bean.getProperty(PlcAggregate.LOOKUP_VALUE_KEY);
				writer.writeAttribute("value", valorLookup, null);
				writer.writeAttribute("title", bean.getProperty(PlcAggregate.SHORT_DESC_KEY), null);
				if (isDisabled(component, bean))
					writer.writeAttribute("disabled", "disabled", null);
				
				if (!usaUnobtrusiveRia) {
					writer.writeAttribute("readonly", "false", null);
				}

				writer.writeAttribute("size", bean.getProperty(PlcAggregate.LOOKUP_TAMANHO_KEY), null);

				if (!usaUnobtrusiveRia && !"S".equals(somenteLeitura)) {
					writer.writeAttribute("onkeydown", "selecionaPorTecla(event,this);", null);
				}

				writer.endElement("input");

				// Não renderiza botoes em modo de visualização de documento
				if ((!("S".equals(request.getAttribute(PlcConstants.VISUALIZA_DOCUMENTO_PLC) + "")) && !"S".equals(somenteLeitura))&& !isDisabled(component, bean)) {
					writer.startElement("span", component);
					writer.writeAttribute("id", clientId + "Sel", null);

					String contextPath = contextUtil.getRequest().getContextPath();
					String baseAction = bean.getProperty(PlcAggregate.BASE_ACTION_KEY) == null ? "" : bean.getProperty(PlcAggregate.BASE_ACTION_KEY).toString();
					ValueExpression actionVE =   (ValueExpression)bean.getProperty(PlcAggregate.ACTION_SEL_KEY);
					String action = "";
					if (actionVE != null) {
						if (actionVE.isLiteralText()) {
							action = actionVE.getExpressionString();
						} else {
							action = (String) actionVE.getValue(context.getELContext());
						}
					}
					//if (StringUtils.isNotBlank(baseAction))
					//	action 			= baseAction + "/" + action;
					String evento = bean.getProperty(PlcAggregate.EVENTO_KEY) == null ? "" : bean.getProperty(PlcAggregate.EVENTO_KEY).toString();
					String parametros = "";
					ValueExpression parametroKey = (ValueExpression) bean.getProperty(PlcAggregate.PARAMETRO_KEY);
					if (parametroKey != null) {
						if (parametroKey.isLiteralText()) {
							parametros = parametroKey.getExpressionString();
						} else {
							parametros = (String) parametroKey.getValue(context.getELContext());
						}
					}
					String separador = "";
					String larg = bean.getProperty(PlcAggregate.LARG_KEY) == null ? "0" : bean.getProperty(PlcAggregate.LARG_KEY).toString();
					String alt = bean.getProperty(PlcAggregate.ALT_KEY) == null ? "0" : bean.getProperty(PlcAggregate.ALT_KEY).toString();
					String posx = bean.getProperty(PlcAggregate.POSX_KEY) == null ? "0" : bean.getProperty(PlcAggregate.POSX_KEY).toString();
					String posy = bean.getProperty(PlcAggregate.POSY_KEY) == null ? "0" : bean.getProperty(PlcAggregate.POSY_KEY).toString();
					ValueExpression propSelPopVE = (ValueExpression)bean.getProperty(PlcAggregate.PROPSEL_POP_KEY);
					String propSelPop = "";
					if (propSelPopVE != null) {
						if (propSelPopVE.isLiteralText()) {
							propSelPop = propSelPopVE.getExpressionString();
						} else {
							propSelPop = (String) propSelPopVE.getValue(context.getELContext());
						}
					}
					ValueExpression propsSelPopVE = (ValueExpression)bean.getProperty(PlcAggregate.PROPSSEL_POP_KEY);
					String propsSelPop = "";
					if (propsSelPopVE != null) {
						if (propsSelPopVE.isLiteralText()) {
							propsSelPop = propsSelPopVE.getExpressionString();
						} else {
							propsSelPop = (String) propsSelPopVE.getValue(context.getELContext());
						}
					}
					String limpaPropsSelPop = bean.getProperty(PlcAggregate.LIMPA_PROPSSEL_POP_KEY) == null ? "" : bean.getProperty(PlcAggregate.LIMPA_PROPSSEL_POP_KEY).toString();
					String tituloBotaoSelPop = bean.getProperty(PlcAggregate.TITULO_BOTAO_SEL_POP_KEY) == null ? "" : bean.getProperty(PlcAggregate.TITULO_BOTAO_SEL_POP_KEY).toString();
					String tituloBotaoLimpar = bean.getProperty(PlcAggregate.TITULO_BOTAO_LIMPAR_KEY) == null ? "" : bean.getProperty(PlcAggregate.TITULO_BOTAO_LIMPAR_KEY).toString();

					if (propSelPop.startsWith("argumentos.")) {
						propSelPop = propSelPop.substring(11, propSelPop.length());
					}

					if (propSelPop.endsWith(".valor")) {
						propSelPop = propSelPop.substring(0, propSelPop.length() - 6);
					}

					if (propsSelPop != null) {
						if (!propsSelPop.startsWith(",")) {
							propsSelPop = ",".concat(propsSelPop);
						} 
						String containerID = component.getContainerClientId(context);
						containerID = containerID.substring(0, containerID.lastIndexOf(':') + 1);
						propsSelPop = propsSelPop.replaceAll(",", "," + containerID);

					}

					String propAutoRecuperacao = "";

					if (StringUtils.isNotBlank(autoRecuperacaoPropriedade))
						propAutoRecuperacao = "," + clientId + ":" + autoRecuperacaoPropriedade + "#" + autoRecuperacaoPropriedade;

					String propriedades = "";
					String propriedadePrincipal = "";

					if (propsChaveNatural == null || propsChaveNatural.keySet().isEmpty()) {
						propriedadePrincipal = clientId + "#" + propSelPop + ",lookup_" + clientId + "#" + propSelPop + "Lookup";
						propriedades = "'" + propriedadePrincipal + propsSelPop + propAutoRecuperacao + "'";
					} else {

						String props = "";
						boolean firstField = true;
						for (String propriedadeCN : propsChaveNatural.keySet()) {
							if (firstField) {
								props = clientId + "#" + propriedadeCN;
							} else {
								props += "," + clientId + propriedadeCN + "#" + propriedadeCN;
							}
							firstField = false;
						}

						propriedadePrincipal = props + ",lookup_" + clientId + "#" + propSelPop + "Lookup";
						propriedades = "'" + propriedadePrincipal + propsSelPop + (StringUtils.isNotBlank(propAutoRecuperacao) ? propAutoRecuperacao : "") + "'";
					}
					if (StringUtils.isNotBlank(evento)) {
						parametros = "evento=" + evento + parametros;
					}
					String alvo = action.replaceAll("/", "_");

					String delimitador = StringUtils.defaultIfEmpty((String) bean.getProperty(PlcAggregate.DELIMITADOR), ",");
					String modal = (String) bean.getProperty(PlcAggregate.MODAL);
					String janelaSelecao = "Popup";
					if (modal != null && modal.equalsIgnoreCase("S"))
						janelaSelecao = "Modal";

					String onclick = "";

					//Homologar esta URL também para outras possiveis URLs facelets de modo que o componente nao fique restrito
					onclick = "selecao" + janelaSelecao + "('" + (StringUtils.isNotBlank(baseAction) ? baseAction : contextPath) + "/f/n/" + action + "?" + parametros + "&modoJanelaPlc=" + janelaSelecao.toLowerCase() + "&delimPropsPlc=" + delimitador + "'," + (!"".equals(propriedades) ? propriedades + "," : "") + "'" + separador + "','" + larg + "','" + alt + "','" + posx + "','" + posy + "','" + alvo + "','" + delimitador + "'" + "); plc.componenteFoco = document.getElementById('lookup_" + clientId + "'); return false;";


					writer.writeAttribute("onclick", onclick, null);
					writer.writeAttribute("onkeydown", "selecionaPorTecla(event,this);", null);
					writer.writeAttribute("title", tituloBotaoSelPop, null);
					writer.writeAttribute("class", "ui-state-default plc-botao-agregado bt", null);

					// SPAN de Icone!
					writer.startElement("span", null);
					writer.writeAttribute("class", "ico iRecupera", null);
					writer.endElement("span");

					if (StringUtils.isNotEmpty(tituloBotaoSelPop)) {
						writer.writeText(tituloBotaoSelPop, null);
					}

					writer.endElement("span");

					String[] arrayPropriedades = ("S".equals(limpaPropsSelPop)) ? propriedades.replaceAll("'", "").split(",") : propriedadePrincipal.split(",");
					String campos = "";

					for (String prop : arrayPropriedades) {
						String[] arrayPropriedade = prop.split("#");
						if (campos == "") {
							campos = arrayPropriedade[0];
						} else {
							campos += "," + arrayPropriedade[0];
						}
					}

					boolean exibeBotaoLimpar = bean.getProperty(PlcAggregate.EXIBE_BOTAO_LIMPAR_KEY) == null ? false : Boolean.parseBoolean(bean.getProperty(PlcAggregate.EXIBE_BOTAO_LIMPAR_KEY).toString());
					if (exibeBotaoLimpar) {
						writer.startElement("span", component);
						writer.writeAttribute("id", clientId + "Limpar", null);
						writer.writeAttribute("onclick", "limparVinculado(this, '" + campos + "')", null);
						writer.writeAttribute("class", "ui-state-default ui-corner-all plc-botao-agregado bt", null);
						if (!StringUtils.isNotBlank((String) valorLookup)) {
							writer.writeAttribute("style", "display:none", null);
						}
						writer.writeAttribute("title", tituloBotaoLimpar, null);
						writer.writeText(tituloBotaoLimpar, null);
						writer.endElement("span");
					}

					boolean selecaoMultipla = false;
					if ((Boolean) bean.getProperty(PlcAggregate.EXIBE_BOTAO_MULTISEL) != null)
						selecaoMultipla = (Boolean) bean.getProperty(PlcAggregate.EXIBE_BOTAO_MULTISEL);

					String uriSel = request.getRequestURI();
					String uri = uriSel.substring(0, uriSel.lastIndexOf("/") + 1);
					
					if (selecaoMultipla) {

						String tituloMulti = (String) bean.getProperty(PlcAggregate.BOTAO_MULTISEL_TITULO);

						String nomeDetalhe = clientId.split(":")[2];	
						parametros = (StringUtils.isNotEmpty(parametros) ? parametros + "&" : parametros) + PlcConstants.PlcJsfConstantes.PLC_COLECAO_DETALHE_NOME + "=" + nomeDetalhe;
						
						String onclickMulti = "selecaoModalMulti('" + uri + action + "?" + parametros + "&modoJanelaPlc=modal" + "&indMultiSelPlc=S" + "&delimPropsPlc=" + delimitador + "'," + (!"".equals(propriedades) ? propriedades + "," : "") + "'" + separador + "','" + larg + "','" + alt + "','" + posx + "','" + posy + "','" + alvo + "'" + "); return false;";
						writer.startElement("span", component);
						writer.writeAttribute("id", clientId + "SelecaoMultipla", null);
						writer.writeAttribute("onclick", onclickMulti, null);
						writer.writeAttribute("class", "ui-state-default ui-corner-all plc-botao-agregado bt", null);
						writer.writeAttribute("title", tituloMulti, null);
						writer.writeText(tituloMulti, null);
						writer.endElement("span");
					}

				}

				boolean inPanelFormLayout = __isInPanelFormLayout(context, component);
				if (!inPanelFormLayout) {
					//<label class="p_OraHiddenLabel" for="corpo:formulario:descricao">Descrição</label>
					String label = (String) bean.getProperty(PlcAggregate.LABEL_KEY);
					if (StringUtils.isNotEmpty(label)) {
						writer.startElement("label", component);
						writer.writeAttribute("class", "p_OraHiddenLabel", null);
						writer.writeAttribute("for", getClientId(context, component), null);
						writer.writeText(label, null);
						writer.endElement("label");
					}
				}

				fimComponent(context, bean, component);
			}

		} catch (Exception e) {
			msgComponentsUtil.createMsgError(bean, component, null, e);
		}

		msgComponentsUtil.printMessageError(component);

	}

	private void registraPartialSubmit(FacesContext context, UIComponent component) throws IOException {

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		String clientId = component.getClientId(context);

		String partialVinculado = contextUtil.getRequest().getParameter(PlcJsfConstantes.PLC_PARTIAL_VINCULADO);

		if (clientId.equals(partialVinculado)) {
			RequestContext.getCurrentInstance().addPartialTarget(component);
		}
	}

	private void imprimeChaves(FacesContext context, UIComponent component, FacesBean bean, TreeMap<String, Object> propsChaveNatural, String autoRecuperacaoClasse, String propriedade, String idTamanho, ResponseWriter writer, String idSomenteLeitura, String idExibe) throws IOException {

		boolean limparValor = StringUtils.isBlank((String) bean.getProperty(PlcAggregate.LOOKUP_VALUE_KEY));

		boolean needsPanelFormLayout = __isInPanelFormLayout(context, component);

		boolean firstField = true;
		for (Iterator i = propsChaveNatural.keySet().iterator(); i.hasNext();) {
			String propriedadeCN = (String) i.next();
			writer.startElement("input", component);
			writer.writeAttribute("type", idExibe.equalsIgnoreCase("S") ? "text" : "hidden", null);

			// Layout N-UP
			if (needsPanelFormLayout)
				writer.writeAttribute("class", "af_inputText_content", null);

			// O primeiro campo da chave natural deve ser somente o clientId para fazer o binding value do componente
			writer.writeAttribute("id", firstField == true ? component.getClientId(context) : component.getClientId(context) + propriedadeCN, null);
			writer.writeAttribute("name", firstField == true ? component.getClientId(context) : component.getClientId(context) + propriedadeCN, null);
			writer.writeAttribute("value", limparValor == false ? propsChaveNatural.get(propriedadeCN) : "", null);
			writer.writeAttribute("title", bean.getProperty(PlcAggregate.SHORT_DESC_KEY), null);

			bean.setProperty(PlcAggregate.VALUE_KEY, firstField == true && limparValor == false ? propsChaveNatural.get(propriedadeCN) : "");

			if (StringUtils.isBlank(autoRecuperacaoClasse))
				writer.writeAttribute("readonly", PlcTagUtil.checkNaturalKeyFilled(bean, propriedade), null);

			writer.writeAttribute("onkeydown", "selecionaPorTecla(event,this);", null);
			// Se for o Último campo referente às chaves coloca o onchange para fazer a auto recuperação
			if (!i.hasNext())
				writer.writeAttribute("onchange", "autoRecuperacaoVinculado('" + component.getClientId(context) + "');return false;", null);
			if (idSomenteLeitura.equals("S"))
				writer.writeAttribute("readonly", "false", null);
			writer.writeAttribute("size", idTamanho, null);
			writer.endElement("input");
			firstField = false;
		}
	}

	private boolean __isInPanelFormLayout(FacesContext context, UIComponent component) {

		Map requestMap = context.getExternalContext().getRequestMap();
		Object formItem = requestMap.get(_PANEL_FORM_LAYOUT_FORM_ITEM_KEY);
		return component.equals(formItem);
	}

	private boolean inicioComponent(FacesContext context, FacesBean bean, UIComponent component) throws IOException {

		boolean needsPanelFormLayout = __isInPanelFormLayout(context, component);
		ResponseWriter rw = context.getResponseWriter();
		boolean isSimple = getSimple(component, bean);

		if (!isSimple && needsPanelFormLayout) {

			boolean isLabelStartAligned = _isLabelStartAligned(context, needsPanelFormLayout);

			boolean needsTableTag = !isLabelStartAligned || (!needsPanelFormLayout && _needsTableTag(component));

			if (needsTableTag) {
				rw.startElement("table", component);
			}

			// Geração do tr
			rw.startElement("tr", component);
			//rw.writeAttribute("style", "float: left;", null);
			rw.writeAttribute("class", "af_inputText p_AFRequired", null);
			rw.writeAttribute("id", component.getClientId(context) + "__xc_", null);

			// Geração do td para label
			rw.startElement("td", component);
			rw.writeAttribute("nowrap", "", null);
			rw.writeAttribute("class", "af_inputText_label af_panelFormLayout_label-cell", null);
			rw.startElement("label", component);
			//<label for="corpo:formulario:nomeFuncionario6">
			rw.writeAttribute("for", component.getClientId(context), null);
			String label = getLabel(component, bean);

			Boolean isRequired = (Boolean) bean.getProperty(PlcAggregate.REQUIRED_KEY);
			if (isRequired != null && isRequired.booleanValue()) {
				label = "* " + label;
			}

			rw.write(label);
			rw.endElement("label");
			//rw.write(getLabel(bean));
			rw.endElement("td");

			// Geração da parte de inputs
			rw.startElement("td", component);
			rw.writeAttribute("valign", "top", null);
			rw.writeAttribute("nowrap", "", null);
			rw.writeAttribute("class", "af_panelFormLayout_content-cell", null);

		} else {
			rw.startElement("span", component);
		}
		return needsPanelFormLayout;
	}

	private void fimComponent(FacesContext context, FacesBean bean, UIComponent component) throws IOException {

		boolean needsPanelFormLayout = __isInPanelFormLayout(context, component);
		ResponseWriter rw = context.getResponseWriter();
		boolean isSimple = getSimple(component, bean);

		if (!isSimple && needsPanelFormLayout) {

			boolean isLabelStartAligned = _isLabelStartAligned(context, needsPanelFormLayout);

			boolean needsTableTag = !isLabelStartAligned || (!needsPanelFormLayout && _needsTableTag(component));

			rw.endElement("td");
			//rw.endElement("td");
			rw.endElement("tr");

			if (needsTableTag) {
				rw.endElement("table");
			}
		} else {
			rw.endElement("span");
		}
	}

	private boolean _needsTableTag(UIComponent component) {

		// Find the first content-generating parent
		UIComponent parent = XhtmlUtils.getStructuralParent(component);
		if (parent == null)
			return true;

		// =-=AEW We should review this code.
		// Either the parent should mark down that it rendered
		// a table, or we should lean on the ResponseWriter
		// to tell us if a table had been used.

		// Hardcoding some packages 'cause I need this code to change!
		String family = parent.getFamily();
		if (HtmlTableLayout.COMPONENT_FAMILY.equals(family)) {
			return false;
		}

		return true;
	}

	/**
	  * Retrieves whether the labels should be rendered above fields as opposed to
	  * the side of the fields.
	  * @param context the Faces context
	  * @param needsPanelFormLayout true if using a panelFormLayout
	  * @return true if labels are stacked above fields
	  */
	private boolean _isLabelStartAligned(FacesContext context, boolean needsPanelFormLayout) {

		if (needsPanelFormLayout) {
			Map requestMap = context.getExternalContext().getRequestMap();
			Object labelsStartAligned = requestMap.get(PANEL_FORM_LAYOUT_LABELS_START_ALIGNED_KEY);
			return Boolean.TRUE.equals(labelsStartAligned);
		}
		return true;
	}

	/**
	 * @return Parâmetros default de todos os templates ria.
	 */
	protected String getDefaultRiaParameters(FacesContext context, UIComponent component, FacesBean bean) {

		return "id='" + component.getClientId(context) + "',idLookup='" + "lookup_" + component.getClientId(context) + "'";
	}

	/**
	 * @return Templates RIA default do componente.
	 */
	protected String getDefaultRia(FacesContext context, UIComponent component, FacesBean bean) {

		return null;
	}

	/**
	 * @return Templates RIA customizado.
	 */
	protected String getCustomRia(FacesContext context, UIComponent component, FacesBean bean) {

		ValueExpression riaExpression = (ValueExpression) bean.getProperty(PlcAggregate.RIA_USA);
		if (riaExpression != null) {
			if (riaExpression.isLiteralText())
				return riaExpression.getExpressionString();
			else
				return (String) riaExpression.getValue(context.getELContext());
		}

		return null;
	}

}
