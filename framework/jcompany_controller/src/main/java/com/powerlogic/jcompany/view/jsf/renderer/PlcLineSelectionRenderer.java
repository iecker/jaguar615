/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.renderer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.component.html.HtmlRowLayout;
import org.apache.myfaces.trinidad.context.RenderingContext;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.XhtmlRenderer;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.annotation.PlcPrimaryKey;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcNamedLiteral;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.controller.jsf.PlcBaseMB;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcEntityUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcLineSelection;
import com.powerlogic.jcompany.view.jsf.util.PlcMsgComponentsUtil;

/**
 * Especialização do renderer base XhtmlRenderer para permitir IoC e DI nos renderes JSF/Trinidad. 
 * @author Moies Paula, Pedro Henrique Neves

 */
public class PlcLineSelectionRenderer extends XhtmlRenderer {

	Logger									log				= Logger.getLogger(PlcLineSelectionRenderer.class.getCanonicalName());

	protected static final Logger			logVisao		= Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	protected static final SimpleDateFormat	formatter		= new SimpleDateFormat("dd/MM/yyyy");

	static {
		formatter.setLenient(false);
	}

	// Definindo o tipo do renderer
	static public final String				RENDERER_TYPE	= "com.powerlogic.jsf.LinhaSelecao";

	public PlcLineSelectionRenderer() {

		super(PlcLineSelection.TYPE);
	}

	@Override
	protected void encodeBegin(FacesContext context, RenderingContext arc, UIComponent component, FacesBean bean) throws IOException {

		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("tr", component);
		renderId(context, component);
		if (bean.getProperty(PlcLineSelection.STYLE_CLASS_KEY) != null)
			writer.writeAttribute("class", bean.getProperty(PlcLineSelection.STYLE_CLASS_KEY).toString(), null);
		if (bean.getProperty(HtmlRowLayout.INLINE_STYLE_KEY) != null)
			writer.writeAttribute("style", bean.getProperty(HtmlRowLayout.INLINE_STYLE_KEY).toString(), null);
		renderEventHandlers(context, component, bean);

	}

	@Override
	protected void encodeEnd(FacesContext context, RenderingContext arc, UIComponent component, FacesBean bean) throws IOException {

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcMsgComponentsUtil msgComponentsUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgComponentsUtil.class, QPlcDefaultLiteral.INSTANCE);

		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("tr");

		try {
			PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
			Long id = elUtil.evaluateExpressionGet("#{plcEntidade"+"id"+"}", Long.class);
			if (id != null && id > 0) {

				boolean isPopup = "popup".equalsIgnoreCase(contextUtil.getRequest().getParameter("modoJanelaPlc"));
				boolean isModal = "modal".equalsIgnoreCase(contextUtil.getRequest().getParameter("modoJanelaPlc"));

				if (!(isPopup || isModal)) {

					String url = getRedirectUrl(bean);

					writer.append("<script id=\"avaliar:" + this.getClientId(context, component) + "\" type=\"text/javascript\">\n");
					writer.append("getRootDocument().location = '").append(url).append("';\n");
					writer.append("</script>\n");
				}
			}
		} catch (Exception e) {
			msgComponentsUtil.createMsgError(bean, component, null, e);
		}

		msgComponentsUtil.printMessageError(component);

	}

	/**
	 * Devolve a instancia do backing-bean corrente
	 */
	protected PlcBaseMB getAction() {

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		String backingBean = (String) contextUtil.getRequestAttribute(PlcConstants.PlcJsfConstantes.PLC_ACTION_KEY);

		Object action = PlcCDIUtil.getInstance().getInstanceByType(Object.class, new PlcNamedLiteral(backingBean));

		if (action != null && action instanceof PlcBaseMB) {
			return (PlcBaseMB) action;
		} else {
			return null;
		}

	}

	/**
	 * Definindo a ação que será executada ao clicar em uma linha do resultado da seleção
	 */
	@Override
	protected String getOnclick(UIComponent component, FacesBean bean) {

		PlcEntityUtil entityUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcEntityUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcConfigUtil configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);

		try {

			//cria instancia do propertyUtilsBean para evitar diversas chamadas a metodo syncronized
			PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
			
			String indMultiSelPlc = contextUtil.getRequestParameter("indMultiSelPlc");
			StringBuilder sb = new StringBuilder();

			Object itemExpression = bean.getProperty(PlcLineSelection.ITEM_EXPRESSION_KEY);
			if (StringUtils.isBlank(indMultiSelPlc)) {

				boolean isPopup = "popup".equalsIgnoreCase(contextUtil.getRequest().getParameter("modoJanelaPlc"));
				boolean isModal = "modal".equalsIgnoreCase(contextUtil.getRequest().getParameter("modoJanelaPlc"));
				String delimitador = StringUtils.defaultIfEmpty(contextUtil.getRequest().getParameter("delimPropsPlc"), ",");

				if (isPopup || isModal) {

					Object item = getValor(itemExpression);

					String propAgregada = (String) getValor(bean.getProperty(PlcLineSelection.PROP_AGREGADA_KEY));
					String propAgregadaCampo = (String) getValor(bean.getProperty(PlcLineSelection.PROP_AGREGADA_CAMPO_KEY));
					PlcPrimaryKey chavePrimaria = (PlcPrimaryKey) item.getClass().getAnnotation(PlcPrimaryKey.class);
					Set<String> propsChaveNatural = new TreeSet<String>(new Comparator<String>() {

						public int compare(String o1, String o2) {

							if (o1.equals(o2))
								return 0;
							return +1;
						}

					});
					if (chavePrimaria != null) {
						for (String propriedadeCN : chavePrimaria.propriedades()) {
							propsChaveNatural.add(propriedadeCN);
						}
					}

					if (isPopup)
						sb.append("devolveSelecao('");
					else
						sb.append("devolveSelecaoModal('");

					if (chavePrimaria == null) {
						if (propertyUtilsBean.getProperty(item, "id") != null) {
							sb.append(propAgregada).append('#').append(StringEscapeUtils.escapeJavaScript(ObjectUtils.toString(propertyUtilsBean.getProperty(item, "id"))));
						} else {
							sb.append(propAgregada).append('#').append(StringEscapeUtils.escapeJavaScript(ObjectUtils.toString(propertyUtilsBean.getProperty(item, "rowId"))));
						}
					} else {
						boolean firstField = true;
						for (String propriedadeCN : propsChaveNatural) {
							if (!firstField) {
								sb.append(delimitador);
							}

							PlcEntityInstance itemInstance = metamodelUtil.createEntityInstance(item);

							Object value = propertyUtilsBean.getProperty(itemInstance.getIdNaturalDinamico(), propriedadeCN);
							if (value != null && value instanceof Date) {
								sb.append(firstField == true ? propAgregada : propriedadeCN).append('#').append(StringEscapeUtils.escapeJavaScript(formatter.format(value)));
							} else {
								sb.append(firstField == true ? propAgregada : propriedadeCN).append('#').append(StringEscapeUtils.escapeJavaScript(ObjectUtils.toString(value)));
							}
							firstField = false;
						}

					}

					sb.append(delimitador);
					sb.append(propAgregada).append("Lookup#").append(StringEscapeUtils.escapeJavaScript(ObjectUtils.toString(item)));
					// Verificando se está buscando outras propriedades
					if (StringUtils.isNotEmpty(propAgregadaCampo)) {
						if (propAgregadaCampo.contains("#")) {
							String[] propriedades = propAgregadaCampo.split(",");
							Object[] propAgregadaCampoValue = new Object[propriedades.length];
							for (int i = 0; i < propriedades.length; i++) {
								String[] valores = propriedades[i].split("#");
								if (valores.length > 1) {
									Object propriedade = propertyUtilsBean.getProperty(item, valores[1]);

									propriedade = entityUtil.resolveField(propriedade);

									propAgregadaCampoValue[i] = new Object[] { valores[0], propriedade };
								} else {
									propAgregadaCampoValue[i] = new Object[] { valores[0], "" };
								}
							}
							for (Object propriedadeAgregada : propAgregadaCampoValue) {
								Object[] obj = (Object[]) propriedadeAgregada;
								sb.append(delimitador);
								sb.append(obj[0]);
								sb.append('#');
								sb.append(StringEscapeUtils.escapeJavaScript(ObjectUtils.toString(obj[1])));
							}
						} else {
							Object propAgregadaCampoValue = null;
							propAgregadaCampoValue = propertyUtilsBean.getProperty(item, propAgregadaCampo);
							sb.append(delimitador);
							sb.append(propAgregada);
							sb.append('_');
							sb.append(propAgregadaCampo);
							sb.append('#');
							sb.append(StringEscapeUtils.escapeJavaScript(ObjectUtils.toString(propAgregadaCampoValue)));
						}
					}
				} else {
					sb.append("document.location=('");
					sb.append(getRedirectUrl(bean));
				}

				sb.append("');"); 

			} else {
				ValueExpression propAgregadaExpression = (ValueExpression) bean.getProperty(PlcLineSelection.PROP_AGREGADA_KEY);
				String propAgregada = null;
				if (propAgregadaExpression != null) {
					if (propAgregadaExpression.isLiteralText()) {
						propAgregada = propAgregadaExpression.getExpressionString();
					} else {
						propAgregada = (String) propAgregadaExpression.getValue(FacesContext.getCurrentInstance().getELContext());
					}
				}

				Object item = null;
				if (itemExpression instanceof ValueExpression) {
					item = ((ValueExpression) itemExpression).getValue(FacesContext.getCurrentInstance().getELContext());
				} else {
					item = itemExpression;
				}

				sb.append("guardaSelecaoMultiJSF(\"");
				sb.append(propAgregada).append('#').append(propertyUtilsBean.getProperty(item, "id"));
				sb.append(",").append(propAgregada).append("Lookup#").append(item.toString().replace("'", "").replace("\"", ""));
				sb.append("\", this);");
			}

			return sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
			log.info( e.getMessage(), e);
			return null;
		}
	}

	protected Object getValor(Object objeto) {

		if (objeto instanceof ValueExpression)
			return ((ValueExpression) objeto).getValue(FacesContext.getCurrentInstance().getELContext());
		else
			return objeto;

	}

	/**
	 * Define a url para a qual será redirecionado ao clicar num elemento da pesquisa.
	 * Também usado para auto-recuperação.
	 *  
	 */
	protected String getRedirectUrl(FacesBean bean)  {

		PlcURLUtil urlUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcURLUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcConfigUtil configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		String action = (String) bean.getProperty(PlcLineSelection.ACTION_KEY);

		HttpServletRequest request = contextUtil.getRequest();
		if (StringUtils.isBlank(action)) {
			try {
				// Caso esteja vazio, pega o nome da action através da url
				action = (String) urlUtil.resolveCollaborationNameFromUrl(request);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (action.endsWith("sel")) {
			action = action.substring(0, action.lastIndexOf("sel")) + "man";
		}

		if (action.equals("#")) {
			action = "";
		}

		if (action.startsWith("/")) {
			action = action.substring(1);
		}

		//Define os parametros
		String linkEdicao = (String) bean.getProperty(PlcLineSelection.LINK_EDICAO_CT_KEY);
		if (StringUtils.isBlank(linkEdicao)) {
			Object le = bean.getProperty(PlcLineSelection.LINK_EDICAO_KEY);
			if (le instanceof ValueExpression) {
				linkEdicao = (String) ((ValueExpression) le).getValue(FacesContext.getCurrentInstance().getELContext());
			} else {
				linkEdicao = (String) le;
			}
		}

		StringBuilder parametros = new StringBuilder(StringUtils.isBlank(linkEdicao) ? "" : linkEdicao);

		if ("popup".equalsIgnoreCase(request.getParameter("modoJanelaPlc")))
			parametros.append("&modoJanelaPlc=popup");

		// Perpetua o flag de modo visualizacao
		if (contextUtil.getSessionAttribute("mfPlc") != null) {
			parametros.append("&mfPlc=").append(contextUtil.getSessionAttribute("mfPlc"));
		}

		// Perpetua o flag de form em modo somente texto 
		// Perpetua o flag de modo visualizacao
		if (contextUtil.getSessionAttribute("mcPlc") != null) {
			parametros.append("&mcPlc=").append(contextUtil.getSessionAttribute("mcPlc"));
		}

		//Se os parametros começarem com ? ou &, retira isso
		if (parametros.length() > 0 && parametros.charAt(0) == '&') {
			int length = "&".length();
			//Se começa com '&'
			if (parametros.length() >= length && "&".equals(parametros.substring(0, length))) {
				parametros.delete(0, length);
			} else { //Se não, tira só o '&'
				parametros.deleteCharAt(0);
			}
		}

		if (!action.equals("") && parametros.indexOf("id=") < 0 && parametros.indexOf("evento=") < 0) {
			parametros.insert(0, "evento=y&");
		}

		action = action + (parametros.charAt(0) != '?' && !action.equals("") ? "?" : "") + parametros;

		if (action.startsWith("../"))
			action = action.substring(3, action.length());

		String siglaMod = urlUtil.resolveModuleAcronymFromCollaboration(request);
		boolean incluiSigla = StringUtils.isNotBlank(siglaMod) && !action.startsWith(siglaMod + "/");
		PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
		action = elUtil.evaluateExpressionGet("#{request.contextPath}", String.class) + "/f/n/" + (incluiSigla ? siglaMod + "/" : "") + action;


		return action;
	}
}
