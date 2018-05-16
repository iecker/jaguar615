/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.renderer;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.context.RenderingContext;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.OutputLabelRenderer;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcSmartLink;
import com.powerlogic.jcompany.view.jsf.component.PlcText;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcMsgComponentsUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcRendererUtil;

/**
 * Especialização do renderer base OutputLabelRenderer para permitir IoC e DI nos renderes JSF/Trinidad. 
 */
public class PlcSmartLinkRenderer extends OutputLabelRenderer {

	protected static final Logger	logVisao		= Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	// Definindo o tipo do renderer
	static public final String		RENDERER_TYPE	= "com.powerlogic.jsf.LinkInteligente";

	/**
	 *  IoC do jcompany. Cria um  link, para facilitar hiperlinks entre logicas
	 *  <a href="[link]" alt='Clique para opções de impressão'>[tituloChave]</a>
	 */
	@Override
	protected void encodeAll(FacesContext context, RenderingContext arc, UIComponent component, FacesBean bean) throws IOException {

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcMsgComponentsUtil msgComponentsUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgComponentsUtil.class, QPlcDefaultLiteral.INSTANCE);

		boolean exibeSe = false;
		if (bean.getProperty(PlcSmartLink.EXIBESE_KEY) != null) {
			exibeSe = (Boolean) bean.getProperty(PlcSmartLink.EXIBESE_KEY);
		}

		if (exibeSe) {
			String link = bean.getProperty(PlcSmartLink.LINK_KEY).toString();
			String titulo = ">";
			String ajuda = "Clique no link para navegar";
			String classeCSS = "";
			if (bean.getProperty(PlcSmartLink.TITULO_CHAVE_KEY) != null) {
				String tituloChave = (String) bean.getProperty(PlcSmartLink.TITULO_CHAVE_KEY);
				titulo = componentUtil.createLocalizedMessage(bean, tituloChave);
			} else if (bean.getProperty(PlcSmartLink.TITULO_KEY) != null)
				titulo = (String) bean.getProperty(PlcSmartLink.TITULO_KEY);
			if (bean.getProperty(PlcSmartLink.AJUDA_CHAVE_KEY) != null) {
				String ajudaChave = (String) bean.getProperty(PlcSmartLink.AJUDA_CHAVE_KEY);
				ajuda = componentUtil.createLocalizedMessage(bean, ajudaChave);
			} else if (bean.getProperty(PlcSmartLink.AJUDA_KEY) != null) {
				ajuda = (String) bean.getProperty(PlcSmartLink.AJUDA_KEY);
			} else if (bean.getProperty(PlcSmartLink.CLASSECSS_KEY) != null)
				classeCSS = (String) bean.getProperty(PlcSmartLink.CLASSECSS_KEY);

			String alvo = (String) bean.getProperty(PlcSmartLink.ALVO_KEY);

			ResponseWriter writer = context.getResponseWriter();

			if (StringUtils.isBlank(alvo)) {
				writer.startElement("a", component);
				writer.writeAttribute("id", component.getClientId(context), null);
				writer.writeAttribute("onkeydown", "selecionaLinkInteligentePorTecla(event,this); return false;", null);
				writer.writeAttribute("class", classeCSS, null);
				writer.writeAttribute("href", link, null);
				writer.writeAttribute("title", ajuda, null);
				writer.write(titulo);
				writer.endElement("a");
			} else {
				//<a id="LINK_INTELIGENTE" href="#" onclick="janela('${action}.do?evento=${evento}${valorChave}${expressao}','','','LINK_INTELIGENTE'); return false;">
				if (link.contains("?")) {
					link = link + "&modoJanelaPlc=popup";
				} else {
					link = link + "?modoJanelaPlc=popup";
				}

				writer.startElement("a", component);
				writer.writeAttribute("id", component.getClientId(context), null);
				writer.writeAttribute("href", "#", null);
				writer.writeAttribute("class", classeCSS, null);
				writer.writeAttribute("onclick", "janela('" + link + "','','','LINK_INTELIGENTE'); return false;", null);
				writer.writeAttribute("title", ajuda, null);
				writer.write(titulo);
				writer.endElement("a");
			}
		}

		try {
			if (!"S".equals(contextUtil.getRequest().getAttribute(PlcConstants.VISUALIZA_DOCUMENTO_PLC) + "")) {
				String defaultRiaParameters = getDefaultRiaParameters(context, component, bean);
				String defaultRia = getDefaultRia(context, component, bean);
				String customRia = getCustomRia(context, component, bean);
				PlcRendererUtil.includeRiaTemplates(component.getClientId(context), defaultRiaParameters, defaultRia, customRia, context.getResponseWriter());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		msgComponentsUtil.printMessageError(component);

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

		return (String) bean.getProperty(PlcText.RIA_USA);
	}

}
