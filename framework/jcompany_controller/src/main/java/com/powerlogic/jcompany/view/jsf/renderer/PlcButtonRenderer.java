/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.

		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */

package com.powerlogic.jcompany.view.jsf.renderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.MethodBinding;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.AttachedObjects;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.component.UIXComponentBase;
import org.apache.myfaces.trinidad.component.core.nav.CoreCommandButton;
import org.apache.myfaces.trinidad.context.RenderingContext;
import org.apache.myfaces.trinidadinternal.agent.TrinidadAgent;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.AccessKeyUtils;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.AutoSubmitUtils;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.CommandButtonRenderer;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.SkinSelectors;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.XhtmlConstants;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.XhtmlUtils;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.config.application.PlcConfigLookAndFeel.FormLookAndFeel;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.controller.appinfo.PlcAppInfoUtil;
import com.powerlogic.jcompany.controller.appinfo.PlcAppMBInfo;
import com.powerlogic.jcompany.controller.cache.PlcCacheSessionVO;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcButton;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcMsgComponentsUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcRendererUtil;

/**
 * Especialização do renderer base CommandButtonRenderer para permitir IoC e DI nos renderes JSF/Trinidad. 
 * 
 */
public class PlcButtonRenderer extends CommandButtonRenderer {

	protected static final Logger	logVisao		= Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	// Definindo o tipo do renderer
	static public final String		RENDERER_TYPE	= "com.powerlogic.jsf.Botao";

	/**
	 * IoC do jcompany. 
	 * Função sobrescrita para acrescentar os códigos javascript existente nos botões.
	 */
	@Override
	protected void encodeAll(FacesContext context, RenderingContext arc, UIComponent component, FacesBean bean) throws IOException {

		PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcMsgComponentsUtil msgComponentsUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgComponentsUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		HttpServletRequest request = contextUtil.getRequest();

		String label 		= (String) bean.getProperty(CoreCommandButton.TEXT_KEY);
		String eventoBotao 	= (String) bean.getProperty(PlcButton.EVENTO_KEY);

		//Necessário estender apenas para incluir as figuras no estilo jQuery entre div's
		this.encodeAllFacelets(context, arc, component, bean);

		if (eventoBotao != null && StringUtils.isNotBlank(eventoBotao)) {
			ResponseWriter writer = context.getResponseWriter();
			writer.append("\n<script id=\"avaliar:" + component.getClientId(context) + "\" type=\"text/javascript\">\n");

			String hotkey = buscaHotKey(componentUtil, request, eventoBotao);

			ajustaComportamentoBotao(bean, eventoBotao, hotkey, writer);

			writer.append("</script>\n");

		}

		String testeExibe = (String) request.getAttribute("testeExibe");

		if (!StringUtils.isBlank(testeExibe) && testeExibe.equals("N")) {

			bean.setProperty(UIXComponentBase.RENDERED_KEY, false);
		}

		String parameter = request.getParameter("jsecurityCaptura");

		if ("S".equals(parameter)) {
			ResponseWriter writer = context.getResponseWriter();
			if (StringUtils.isNotBlank(label) && StringUtils.isNotBlank(eventoBotao))
				writer.append("<!-- TITULO_BOTAO_ACAO:").append(label).append("_").append(eventoBotao).append(" -->");
		}

		try {

			String defaultRiaParameters = getDefaultRiaParameters(context, component, bean);
			String defaultRia = getDefaultRia(context, component, bean);
			String customRia = getCustomRia(context, component, bean);
			PlcRendererUtil.includeRiaTemplates(component.getClientId(context), defaultRiaParameters, defaultRia, customRia, context.getResponseWriter());

		} catch (Exception e) {
			e.printStackTrace();
		}

		msgComponentsUtil.printMessageError(component);

	}



	/**
	 * Foi necessário estender este método apenas para incluir as figuras no estilo jQuery entre div's.
	 * O que foi modificado está entre os comentários <!-- INICIO MODIFICACAO --> e <!-- FIM MODIFICACAO -->
	 */
	protected void encodeAllFacelets(FacesContext context, RenderingContext arc, UIComponent component, FacesBean bean) throws IOException {

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		String clientId = getClientId(context, component);
		if (canSkipRendering(arc, clientId))
			return;

		if (getPartialSubmit(component, bean)) {
			AutoSubmitUtils.writeDependencies(context, arc);
		}

		// Make sure we don't have anything to save
		assert (arc.getCurrentClientId() == null);
		arc.setCurrentClientId(clientId);

		ResponseWriter rw = context.getResponseWriter();
		String icon = getIcon(component, bean);

		//if icon is set, render as an image element within a link element
		//since "buttons" html element is not supported and "input" element of
		//type=image does not support "onClick" JS handler.
		if ((icon != null) && !_supportsOnClickOnImgInput(arc)) {
			if (!getDisabled(component, bean)) {
				rw.startElement(XhtmlConstants.LINK_ELEMENT, component);
				renderEncodedActionURI(context, XhtmlConstants.HREF_ATTRIBUTE, "#");
				rw.writeAttribute(XhtmlConstants.ONCLICK_ATTRIBUTE, getOnclick(component, bean), null);
				rw.startElement("img", component);
				renderAllAttributes(context, arc, component, bean);
				renderEncodedResourceURI(context, "src", icon);
				rw.endElement("img");
				rw.endElement(XhtmlConstants.LINK_ELEMENT);
			} else {
				//If disabled attribute is set on PDAs for commandButtons set as icon,
				//render a static image
				rw.startElement("img", component);
				renderAllAttributes(context, arc, component, bean);
				renderEncodedResourceURI(context, "src", icon);
				rw.endElement("img");
			}
		} else {
			boolean useButtonTag = useButtonTags(arc);
			String element = useButtonTag ? "button" : "input";
			rw.startElement(element, component);
			renderId(context, component);

			// Write the text and access key
			String text = getText(component, bean);

			if (useButtonTag)
				rw.writeAttribute("type", getButtonType(), null);
			else if (icon != null)
				rw.writeAttribute("type", "image", null);
			else
				rw.writeAttribute("type", getInputType(), null);

			if (getDisabled(component, bean)) {
				rw.writeAttribute("disabled", Boolean.TRUE, "disabled");
				// Skip over event attributes when disabled
				renderStyleAttributes(context, arc, component, bean);
			} else {
				renderAllAttributes(context, arc, component, bean);
			}

			char accessKey;
			if (supportsAccessKeys(arc)) {
				accessKey = getAccessKey(component, bean);
				if (accessKey != CHAR_UNDEFINED) {
					rw.writeAttribute("accesskey", Character.valueOf(accessKey), "accessKey");
				}
			} else {
				accessKey = CHAR_UNDEFINED;
			}
			if (useButtonTag) {
				//<!-- INICIO MODIFICACAO -->
				rw.write("<table><tr>");
				if (icon != null) {
					PlcCacheSessionVO c = (PlcCacheSessionVO) contextUtil.getRequest().getSession().getAttribute(PlcConstants.SESSION_CACHE_KEY);
					if (c == null || !FormLookAndFeel.ONLY_TEXT.toString().equals(c.getFormAcaoExibeTexto())) {
						rw.write("<td>");
						rw.write("<div class=\"plc-corpo-acao-i\"><span class=\"" + getStyleImage(((PlcButton) component).getAction(), bean) + "\"></span></div>");
						rw.write("</td>");
					}
				}

				// Inclui título entre div
				if (StringUtils.isNotEmpty(text)) {
					rw.write("<td>");
					rw.write("<div class=\"plc-corpo-acao-t\">");
					AccessKeyUtils.renderAccessKeyText(context, text, accessKey, SkinSelectors.AF_ACCESSKEY_STYLE_CLASS);
					rw.write("</div>");
					rw.write("</td>");
				}
				rw.write("</tr></table>");
				//<!-- FIM MODIFICACAO -->
			} else {
				if (icon != null) {
					renderEncodedResourceURI(context, "src", icon);
				}

				// For Non-JavaScript browsers, encode the name attribute with the 
				// parameter name and value thus it would enable the browsers to 
				// include the name of this element in its payLoad if it submits the
				// page.

				else if (!supportsScripting(arc)) {
					rw.writeAttribute("name", XhtmlUtils.getEncodedParameter(XhtmlConstants.SOURCE_PARAM) + clientId, null);

					rw.writeAttribute("value", text, "text");
				} else {
					rw.writeAttribute("value", text, "text");
					;
				}
			}

			rw.endElement(element);
		}
		arc.setCurrentClientId(null);
	}

	/**
	 * Render all the Javascript attributes.
	 * 
	 * Sobrescrito para implementar a funcionalidade de gerar onclick do botão com Get, caso as 
	 * configurações ou da aplicação ou do caso de uso estiver configurado para tal.
	 * 
	 */
	@Override
	protected void renderEventHandlers(FacesContext context, UIComponent component, FacesBean bean) throws IOException {

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		ResponseWriter rw = context.getResponseWriter();

		try {
			
			boolean isPopup = "popup".equalsIgnoreCase(contextUtil.getRequest().getParameter("modoJanelaPlc"));
			boolean isModal = "modal".equalsIgnoreCase(contextUtil.getRequest().getParameter("modoJanelaPlc"));
			String complementoPopup = "";
			
			if (isPopup || isModal) {
				complementoPopup = "modoJanelaPlc=popup";
			}

			// Renderiza requisição get
			MethodExpression valueExpression = (MethodExpression) bean.getProperty(PlcButton.ACTION_EXPRESSION_KEY);
			String expressionString = valueExpression.getExpressionString();

			if ("#{plcAction.open}".equals(expressionString)) {

				String requestURI = ((HttpServletRequest) context.getExternalContext().getRequest()).getRequestURL().toString();

				String sufixo = requestURI.substring(requestURI.length()-3);
				int idx = ArrayUtils.indexOf(PlcConfigUtil.SUFIXOS_URL, sufixo);
				String formPattern =  (idx != -1 ? PlcConfigUtil.NOMES_SUFIXOS_URL[idx] : null);
				String casoUso = requestURI.substring(requestURI.lastIndexOf("/") + 1,requestURI.length());
				String requestURISel;
				if(casoUso.contains(".xhtml")){
					casoUso = casoUso.substring(0, (casoUso.length() - 6));
				}
				if (formPattern == null) {
					if(requestURI.contains(".xhtml")){
						int pos = requestURI.indexOf(".xhtml");
						requestURISel = requestURI.substring(0, pos);
						PlcConfigUtil configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);
						formPattern = configUtil.getConfigAggregation(casoUso).pattern().formPattern().sufixoUrl();
					}else{
						requestURISel = requestURI;
						PlcConfigUtil configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);
						formPattern = configUtil.getConfigAggregation(casoUso).pattern().formPattern().sufixoUrl();
					}
				} else {
					requestURISel = requestURI.substring(0, requestURI.length() - 3);
				}

				if (StringUtils.equalsIgnoreCase(formPattern, FormPattern.Mad.sufixoUrl()) ||
						StringUtils.equalsIgnoreCase(formPattern, FormPattern.Man.sufixoUrl()) ||
						StringUtils.equalsIgnoreCase(formPattern, FormPattern.Mdt.sufixoUrl()) ||
						StringUtils.equalsIgnoreCase(formPattern, FormPattern.Mds.sufixoUrl()) ||
						StringUtils.equalsIgnoreCase(formPattern, FormPattern.Mas.sufixoUrl())) {

					String complFwplc = "";
					if ( ((HttpServletRequest) context.getExternalContext().getRequest()).getParameter("mfPlc")!=null )
						complFwplc = "&amp;mfPlc=" + ((HttpServletRequest) context.getExternalContext().getRequest()).getParameter("mfPlc");
					if ( ((HttpServletRequest) context.getExternalContext().getRequest()).getParameter("mcPlc")!=null )
						complFwplc = complFwplc+"&amp;mcPlc=" + ((HttpServletRequest) context.getExternalContext().getRequest()).getParameter("mcPlc");
					
					if (StringUtils.equalsIgnoreCase(formPattern, FormPattern.Mad.sufixoUrl()) ||
							StringUtils.equalsIgnoreCase(formPattern, FormPattern.Mas.sufixoUrl())) {
						requestURISel = requestURISel.concat(FormPattern.Smd.sufixoUrl().toLowerCase()) + "?fwPlc=" + casoUso + complFwplc;
					} else if (StringUtils.equalsIgnoreCase(formPattern, FormPattern.Mdt.sufixoUrl()) ||
							StringUtils.equalsIgnoreCase(formPattern, FormPattern.Mds.sufixoUrl())) {
						requestURISel = requestURISel.concat(FormPattern.Sel.sufixoUrl().toLowerCase()) + "?fwPlc=" + casoUso + complFwplc;
					} else {
						if(requestURI.contains(".xhtml")){
							requestURISel = requestURISel.concat(FormPattern.Sel.sufixoUrl().toLowerCase()+".xhtml");
						}else{
							requestURISel = requestURISel.concat(FormPattern.Sel.sufixoUrl().toLowerCase());
						}
					}

					if (StringUtils.isNotBlank(complementoPopup)) {
						requestURISel += (StringUtils.equalsIgnoreCase(formPattern, FormPattern.Mad.sufixoUrl()) ||
								StringUtils.equalsIgnoreCase(formPattern, FormPattern.Mdt.sufixoUrl()) ||
								StringUtils.equalsIgnoreCase(formPattern, FormPattern.Mds.sufixoUrl()) ||
								StringUtils.equalsIgnoreCase(formPattern, FormPattern.Mas.sufixoUrl()) ? "&" : "?") + complementoPopup;
					}	
					partialAjaxOnClick(rw, requestURISel);
				} else {
					rw.writeAttribute("onclick", getOnclick(component, bean), "onclick");
				}

			} else if ("#{plcAction.create}".equals(expressionString)) {
				// Neste caso, a selecao pode servir a vários padrões de casos de uso.
				// Para funcionar ok o GET deve ser enviado com argumento "?fwPlc=funcionariomdt" ou "?fwPlc=departamentoman"
				// senão informado "?fwPlc=", tenta descobrir a url de manutenção
				String requestURI = ((HttpServletRequest) context.getExternalContext().getRequest()).getRequestURL().toString();
				boolean temXhtml = false;
				if(requestURI.contains(".xhtml")){
					int pos = requestURI.indexOf(".xhtml");
					requestURI = requestURI.substring(0, pos);
					temXhtml = true;
				}
				if (requestURI.endsWith("sel")) {
					//Preferencia eh do fwPlc na URL
					String urlManutencao = ((HttpServletRequest) context.getExternalContext().getRequest()).getParameter("fwPlc");
					if (StringUtils.isBlank(urlManutencao)) {
						//fwPLC do botão
						urlManutencao = (String) bean.getProperty(PlcButton.FW_PLC);
						if (StringUtils.isBlank(urlManutencao)) {
							// Tenta descobrir qual a url de manutenção
							String nomeAction = (String)contextUtil.getRequestAttribute(PlcConstants.PlcJsfConstantes.URL_COM_BARRA);
							if (!StringUtils.isBlank(nomeAction)){
								//retira a "/" inicial e o sufixo
								nomeAction = nomeAction.substring(1,nomeAction.length() - 3);
							}
							if(temXhtml){
								int pos = requestURI.indexOf("/f/");
								urlManutencao = requestURI.substring(pos + 1, requestURI.length() - 3)+".xhtml";
							}else{
								urlManutencao = getUrlMaintenance(nomeAction);
							}
						}
					}
					//encontrou url de manutenção?
					if (!StringUtils.isBlank(urlManutencao)){
						//TODO: Fábio Mendes => Rever esta parte do código, partnner de URL
						if (!StringUtils.startsWithIgnoreCase(urlManutencao, "f/n/") && !urlManutencao.contains(".xhtml")){
							urlManutencao = "f/n/" + urlManutencao; 
						}
						urlManutencao = ((HttpServletRequest) context.getExternalContext().getRequest()).getContextPath()+"/" + urlManutencao;
						if (!StringUtils.isBlank(complementoPopup)) {
							urlManutencao = urlManutencao + "?" + complementoPopup;
						}
						logVisao.debug("Encontrou URL para manunteção "+urlManutencao);
						partialAjaxOnClick(rw, urlManutencao);
					}else{
						logVisao.debug("NÃO encontrou URL para manunteção, vai esconder visualmente com o botão");
						rw.writeAttribute("style", "display:none;", "style");
					}

				} else {
					rw.writeAttribute("onclick", getOnclick(component, bean), "onclick");
				}

			} else {
				rw.writeAttribute("onclick", getOnclick(component, bean), "onclick");
			}



		} catch (Exception e) {
			logVisao.debug( "Não foi possível determinar o tipo de renderização para o botão.");
			rw.writeAttribute("onclick", getOnclick(component, bean), "onclick");
		}

		//rw.writeAttribute("onclick", getOnclick(component, bean),  "onclick");
		rw.writeAttribute("ondblclick", getOndblclick(component, bean), "ondblclick");
		rw.writeAttribute("onkeydown", getOnkeydown(component, bean), "onkeydown");
		rw.writeAttribute("onkeyup", getOnkeyup(component, bean), "onkeyup");
		rw.writeAttribute("onkeypress", getOnkeypress(component, bean), "onkeypress");
		rw.writeAttribute("onmousedown", getOnmousedown(component, bean), "onmousedown");
		rw.writeAttribute("onmousemove", getOnmousemove(component, bean), "onmousemove");
		rw.writeAttribute("onmouseout", getOnmouseout(component, bean), "onmouseout");
		rw.writeAttribute("onmouseover", getOnmouseover(component, bean), "onmouseover");
		rw.writeAttribute("onmouseup", getOnmouseup(component, bean), "onmouseup");

	}


	/**
	 * Obtem a URL de manutenção a apartir do identificador do caso de uso
	 * @param nomeAction
	 * @return url de maanutençã para o caso de uso
	 */
	private String getUrlMaintenance(String identificadorCDU) {

		List<PlcAppMBInfo> listaAppMBInfo = PlcCDIUtil.getInstance().getInstanceByType(PlcAppInfoUtil.class, QPlcDefaultLiteral.INSTANCE).findAllUCWithURI(identificadorCDU);
		if (listaAppMBInfo != null && listaAppMBInfo.size() > 0) {

			for(PlcAppMBInfo info : listaAppMBInfo){
				for(String suffixMan:PlcConfigUtil.SUFIXOS_MAN_URL){
					if(info.getPath().endsWith(suffixMan)){
						return info.getPath();
					}
				}
			}
		}
		/*se nao encontrou nenhum com sufixo de manutencao, retorna o primeiro da lista.
		 * Isso para quando é um caso de uso diferente dos padroes do jCompany.
		 */
		return listaAppMBInfo.get(0).getPath();
	}



	private void partialAjaxOnClick(ResponseWriter rw, String requestURI) throws IOException {
		rw.writeAttribute("onclick", "document.location='" + requestURI + "'", "onclick");
	}

	protected String getStyleImage(MethodBinding action, FacesBean bean) {

		if (bean.getProperty(PlcButton.URL_ICONE_KEY) != null) {
			return bean.getProperty(PlcButton.URL_ICONE_KEY).toString();
		}

		return "ui-icon-gear";
	}

	/**
	 * Returns true if the agent supports the "onclick" JS Handler in an "input" 
	 * HTML element of type "image"
	 *
	 */
	static private boolean _supportsOnClickOnImgInput(RenderingContext arc) {

		Object cap = arc.getAgent().getCapabilities().get(TrinidadAgent.CAP_ONCLICK_IMG_INPUT);
		return !Boolean.FALSE.equals(cap);
	}

	/**
	 * @return Parâmetros default de todos os templates ria.
	 */
	protected String getDefaultRiaParameters(FacesContext context, UIComponent component, FacesBean bean) {

		return "id='" + component.getClientId(context) + "'";
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

		return (String) bean.getProperty(PlcButton.RIA_USA);
	}


	/**
	 * Ajusta os botoes pelo evento, adicionando um atalho específico ou validação
	 * @param bean
	 * @param eventoBotao
	 * @param writer
	 * @param _configAcao
	 * @throws IOException
	 */
	private void ajustaComportamentoBotao(FacesBean bean, String eventoBotao, String hotkey, ResponseWriter writer) throws IOException {

		if(eventoBotao.equals("jcompany.evt.gravar")) {
			javascriptValidaExclusaoGravacao(bean, writer);
		}

		if(eventoBotao.equals("jcompany.evt.excluir")) {
			javascriptValidaExclusao(bean, writer);
		}

		if(hotkey != null && StringUtils.isNotBlank(hotkey)) {
			if(eventoBotao.equals("jcompany.evt.desconectar")) {
				writer.append("plc.hotkey('" + hotkey + "', funcaoDesconectar);");
			} else if(eventoBotao.equals("jcompany.evt.home")) {
				writer.append("plc.hotkey('" + hotkey + "', funcaoHome);");
			} else {
				writer.append("plc.hotkey('" + hotkey + "', '"+ bean.getProperty(PlcButton.ID_KEY).toString() +"');");
			}
		}


	}

	/**
	 * Esse método escreve o JavaScript responsável por confirmar a excluão no botão Excluir.
	 * @param bean
	 * @param writer
	 * @throws IOException
	 */
	private void javascriptValidaExclusao(FacesBean bean, ResponseWriter writer) throws IOException {

		String botaoID = bean.getProperty(PlcButton.ID_KEY).toString();
		String alertaExcluir = bean.getProperty(PlcButton.ALERTA_EXCLUIR_KEY).toString();
		String alertaExcluirDetalhe = bean.getProperty(PlcButton.ALERTA_EXCLUIR_DETALHE_KEY).toString();

		writer.append("function execExclusao() { " + "return executarExclusao('"+botaoID+"', '"+""+alertaExcluir+"', '"+""+alertaExcluirDetalhe+"'); " +  "}");

	}

	/**
	 * Esse método escreve o JavaScript responsável por confirmar a excluão no botão Excluir.
	 * @param bean
	 * @param writer
	 * @throws IOException
	 */
	private void javascriptValidaExclusaoGravacao(FacesBean bean, ResponseWriter writer) throws IOException {

		String botaoID = bean.getProperty(PlcButton.ID_KEY).toString();
		String alertaExcluirDetalhe = bean.getProperty(PlcButton.ALERTA_EXCLUIR_DETALHE_KEY).toString();
		writer.append("function execExclusaoGravacao() { " + "return executarExclusaoGravacao('"+botaoID+"','"+alertaExcluirDetalhe+"');" +  "}");

	}
	/**
	 * 
	 * @param componentUtil
	 * @param request
	 * @param eventoBotao
	 * @return
	 */
	private String buscaHotKey(PlcComponentUtil componentUtil, HttpServletRequest request, String eventoBotao) {

		String atalho = componentUtil.getUtilI18n().mountLocalizedMessageAnyBundle(request, eventoBotao + ".hotkey", null);
		if(atalho != null && !atalho.startsWith("???")) {
			return atalho;
		} else {
			return null;
		}

	}
	
	/**
	 * Verifica se existe a necessidade de propagar a action do Ajax.
	 * @param component
	 * @param bean
	 * @return
	 */
	private boolean propagateActionAjaxBehavior(UIComponent component, FacesBean bean) {
		
		AttachedObjects attachedObjects = (AttachedObjects)((PlcButton)component).getFacesBean().getProperty(PlcButton.CLIENT_BEHAVIORS);
		
		return attachedObjects !=null && ((PlcButton) component).getActionAjaxBehavior()!=null && (attachedObjects.getAttachedObjectList("action")==null || attachedObjects.getAttachedObjectList("action").size()==0);
				
	}
}
