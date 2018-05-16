/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.renderer;

import java.io.IOException;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.context.RenderingContext;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.OutputLabelRenderer;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcNamedLiteral;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcDomainLookupUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcTitle;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcMsgComponentsUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcRendererUtil;

/**
 * Especialização do renderer base OutputLabelRenderer para permitir IoC e DI nos renderes JSF/Trinidad. 
 * 
 */
public class PlcTitleRenderer extends OutputLabelRenderer {

	protected static final Logger	logVisao		= Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	// Definindo o tipo do renderer
	static public final String		RENDERER_TYPE	= "com.powerlogic.jsf.Titulo";

	/**
	 *  IoC do jcompany. Cria um span clicável, parecido com um link, para tornar rotulos possíveis registradores de ordenacao
	 */
	@Override
	protected void encodeAll(FacesContext context, RenderingContext arc, UIComponent component, FacesBean bean) throws IOException {


		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcMsgComponentsUtil msgComponentsUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgComponentsUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		if (bean.getProperty(PlcTitle.PROP_ORDENACAO) != null && !StringUtils.isBlank((String) bean.getProperty(PlcTitle.PROP_ORDENACAO))) {

			String propOrdenacao = (String) bean.getProperty(PlcTitle.PROP_ORDENACAO);
			String alias = (String) bean.getProperty(PlcTitle.ALIAS);
			String ordem = (String) bean.getProperty(PlcTitle.ORDEM);
			ResponseWriter writer = context.getResponseWriter();

			alias = (StringUtils.isNotBlank(alias) ? alias : "");
			ordem = (StringUtils.isNotBlank(ordem) ? ordem : "0");

			// Javascripts de apoio
			writer.startElement("script", component);
			writer.append("if(!getImagem(\"asc\"))	images[images.length] = new regImg(\"asc\", \"" + contextUtil.getRequest().getContextPath() + "/res-plc/midia/triup.gif\",\"Ascendente\");" + "if(!getImagem(\"desc\")) images[images.length] = new regImg(\"desc\", \"" + contextUtil.getRequest().getContextPath() + "/res-plc/midia/tridown.gif\",\"Descendente\");" + "if(!getImagem(\"\")) images[images.length] = new regImg(\"\", \"" + contextUtil.getRequest().getContextPath() + "/res-plc/midia/1x1_black.gif\", \"\");" + "ordem[" + ordem + "] = \"" + alias + "." + propOrdenacao + "\";");
			writer.endElement("script");
			// A tag em si
			writer.startElement("span", component);
			writer.writeAttribute("id", "linkOrdenacaoPlc", null);
			writer.writeAttribute("class", "plc-label-ordenacao", null);
			writer.writeAttribute("onclick", "atualizaCriterio('corpo:formulario:ordenacaoPlc', '" + alias + "." + propOrdenacao + "'+getCriterioOrdenacao('" + alias + "." + propOrdenacao + "'), montaCriterioNovo('" + alias + "." + propOrdenacao + "', '" + alias + "'), ','); substituirImagems('" + alias + "." + propOrdenacao + "')", null);

			String estilo = (String) bean.getProperty(PlcTitle.STYLE_CLASS_KEY);
			if (!StringUtils.isBlank(estilo))
				writer.writeAttribute("style", estilo, null);
			else
				//FIXME - Passar para StyleSheet
				writer.writeAttribute("style", "text-decoration: underline;cursor:pointer", null);
		}

		String imagem = (String) bean.getProperty(PlcTitle.CAMINHO_IMAGEM_KEY);
		// Código para inclusão da lixeira, caso em modo de exclusão
		if (StringUtils.isNotBlank(imagem)) {
			ResponseWriter writer = context.getResponseWriter();
			writer.write("<img alt =\"\" src=\"");
			writer.write(imagem.toString());
			writer.write("\"/>");
		}

		super.encodeAll(context, arc, component, bean);

		if (bean.getProperty(PlcTitle.PROP_ORDENACAO) != null && !StringUtils.isBlank((String) bean.getProperty(PlcTitle.PROP_ORDENACAO))) {
			PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);

			ResponseWriter writer = context.getResponseWriter();
			String propOrdenacao = (String) bean.getProperty(PlcTitle.PROP_ORDENACAO);
			String alias = (String) bean.getProperty(PlcTitle.ALIAS);

			alias = (StringUtils.isNotBlank(alias) ? alias : "");

			writer.endElement("span");

			HttpServletRequest request = contextUtil.getRequest();

			// Exibe imagens
			writer.startElement("img", component);
			writer.writeAttribute("id", "IMAGEM_" + alias + "." + propOrdenacao, null);
			writer.writeAttribute("src", request.getContextPath() + "/res-plc/midia/1x1.gif", null);
			writer.writeAttribute("border", "0", null);

			String path = request.getContextPath() + "/res-plc/midia/1x1.gif') >= 0)";
			String ordenacao = elUtil.evaluateExpressionGet("#{" + PlcConstants.PlcJsfConstantes.PLC_CONTROLE_CONVERSACAO + ".ordenacaoPlc}", Object.class) + "','" + alias + "." + propOrdenacao + "', ',')}";

			writer.writeAttribute("onload", "if (this.src.indexOf('" + path + "{mantemEstadoCriterio('" + ordenacao, null);

			//FIXME- I18N
			writer.writeAttribute("alt", "Critério de Ordenação", null);
			writer.endElement("img");

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

	@Override
	protected String getConvertedString(FacesContext context, UIComponent component, FacesBean bean) {

		PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcMsgComponentsUtil msgComponentsUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgComponentsUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		try {

			Object valorProperty = getValue(component, bean);

			// Se valor for um PlcEntityInstance, busca entidade em lookup. Se existir, chama o método toString da entidade
			if (valorProperty != null && metamodelUtil.isEntityClass(valorProperty.getClass())) {

				Object voAtual = valorProperty;
				PlcDomainLookupUtil dominiosLookup = PlcCDIUtil.getInstance().getInstanceByType(PlcDomainLookupUtil.class, new PlcNamedLiteral(PlcConstants.PlcJsfConstantes.PLC_DOMINIOS));
				List listaDominio = dominiosLookup.getDominio(voAtual.getClass().getSimpleName());

				PlcEntityInstance voAtualInstance = metamodelUtil.createEntityInstance(voAtual);

				if (listaDominio != null && !listaDominio.isEmpty()) {
					for (Object dom : listaDominio) {
						Object dominio = dom;
						PlcEntityInstance dominioInstance = metamodelUtil.createEntityInstance(dominio);
						if (dominioInstance.getIdAux().equals(voAtualInstance.getIdAux())) {
							return dominio.toString();
						}
					}
				}
			}

			if (!Boolean.TRUE.equals(bean.getProperty(PlcTitle.ENUM_I18N)) 
					&& (valorProperty == null || (valorProperty != null && !valorProperty.getClass().isEnum())))
				return super.getConvertedString(context, component, bean);

			String valorAtual = super.getConvertedString(context, component, bean);

			String property = (String) bean.getProperty(PlcTitle.PROPRIEDADE_KEY);
			if (StringUtils.isNotBlank(property)) {
				valorAtual = property + '.' + valorAtual;
			} else if (valorProperty.getClass().isEnum()) {
				valorAtual = StringUtils.uncapitalize(valorProperty.getClass().getSimpleName()) + '.' + valorAtual;
			}

			return componentUtil.createLocalizedMessage(bean, valorAtual);

		} catch (Exception e) {
			msgComponentsUtil.createMsgError(bean, component, null, e);
			return null;
		}
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

		return (String) bean.getProperty(PlcTitle.RIA_USA);
	}

}
