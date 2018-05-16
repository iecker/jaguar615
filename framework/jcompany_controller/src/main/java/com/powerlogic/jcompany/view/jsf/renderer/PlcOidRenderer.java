/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.renderer;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.context.RenderingContext;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcViewJsfUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcOid;
import com.powerlogic.jcompany.view.jsf.util.PlcMsgComponentsUtil;
/**
 * Especialização do renderer base PlcTextoRenderer para permitir IoC e DI nos renderes JSF/Trinidad. 
 * 
 */
public class PlcOidRenderer extends PlcTextRenderer {

	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);
	// Definindo o tipo do renderer
	static public final String RENDERER_TYPE 	= "com.powerlogic.jsf.Oid";
	
	/**
	 * IoC do jcompany. Função implementada para acrescentar o código javascript existente na tag area.tag
	 * 
	 * function autoRecuperacao(ajaxUsa) {
	 * 	var oid = get('id');
	 * 	if (typeof ajaxUsa != 'undefined' && ajaxUsa == 'S') {
	 * 		plcAjax.setFuncaoAposAjax("configAutoRecuperacaoOID()");
	 * 		plcAjax.ajaxSubmit('GET','',document.location.pathname+'?evento=Editar&id='+oid+'&limpa=n');
	 * 	} else
	 * 	    submeteUrl(document.location.pathname+'?evento=Editar&id='+oid+'&limpa=n');
	 * }
	 *
	 * function configAutoRecuperacaoOID(){
	 * 	if(getElementoPorId('TOPO_CH_PLC'))
	 * 		getElementoPorId('TOPO_CH_PLC').innerHTML = get('id');
	 * 	setFocus('id', true)
	 * }
	 */
	@Override
	protected void encodeAll(FacesContext context, RenderingContext arc, UIComponent component, FacesBean bean) throws IOException {
		
		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcMsgComponentsUtil msgComponentsUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgComponentsUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		try {

			if (PlcCDIUtil.getInstance().getInstanceByType(PlcViewJsfUtil.class, QPlcDefaultLiteral.INSTANCE).textMode()) {
				new PlcTitleRenderer().encodeAll(context, arc, component, bean);
				return;
			}

			ResponseWriter writer = context.getResponseWriter();

			Boolean exibeSe = bean.getProperty(PlcOid.EXIBE_SE_KEY_OID) != null ? ((Boolean)bean.getProperty(PlcOid.EXIBE_SE_KEY_OID)).booleanValue() : true;
			if (exibeSe){

				super.encodeAll(context, arc, component, bean);
				String autoRecupera = (String)bean.getProperty(PlcOid.AUTO_RECUPERACAO_KEY);



				if (autoRecupera != null && !autoRecupera.equals("N")){

					StringBuilder function1 = new StringBuilder();
					
					function1.append("\nfunction autoRecuperacao(ajaxUsa) {\n");
					function1.append("		var oid = get('" + component.getClientId(context) + "');\n");
					function1.append("		if (typeof oid != 'undefined' && oid != null){\n");
					function1.append("				submeteUrl(document.location.pathname+'?id='+oid);\n");
					function1.append("		}\n");
					function1.append("}\n");
					

					writer.startElement("script",component);
					writer.writeAttribute("id", "avaliar:"+component.getClientId(context), null);
					writer.writeAttribute("type", "text/javascript", null);
					writer.write(function1.toString());
					writer.endElement("script");
				}
			}

			if (contextUtil.getSessionAttribute(PlcConstants.QA.MODO_TESTE)!=null&& 
					!contextUtil.getRequest().getSession().getServletContext().getInitParameter(PlcConstants.CONTEXTPARAM.INI_MODO_EXECUCAO).equalsIgnoreCase("P")) {

				PlcConfigUtil configUtil =  PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);
				PlcURLUtil urlUtil =  PlcCDIUtil.getInstance().getInstanceByType(PlcURLUtil.class, QPlcDefaultLiteral.INSTANCE);

				String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());				
				FormPattern logica = configUtil.getConfigAggregation(url).pattern().formPattern();

				String exp = component.getClientId(context);

				if ( logica == FormPattern.Man || logica == FormPattern.Mdt || logica == FormPattern.Mds){
					if ( StringUtils.countMatches(exp, ":") == 2 ){ // É um mestre

						writer.write("<!--COD=" + bean.getProperty(PlcOid.VALUE_KEY) + "-->");

					}else if ( StringUtils.countMatches(exp, ":") == 4 ){ // É um detalhe

						writer.write("<!--COD_DET_" + getNomeDetalhe(exp) + "_" + getIndiceDetalhe(exp) + "=" + bean.getProperty(PlcOid.VALUE_KEY) + "-->");

					}

				}else if ( logica == FormPattern.Tab ){
					writer.write("<!--COD_"+getIndiceDetalhe(exp) +"="+bean.getProperty(PlcOid.VALUE_KEY)+"-->");
				}
			}

		} catch (Exception e) {
			msgComponentsUtil.createMsgError(bean, component, null, e);
		}
		
	    msgComponentsUtil.printMessageError(component);
	
	}
	/**
	 * Retorna o nome dos detalhes baseado na expressão informada
	 */
	private String getNomeDetalhe(String exp){
		
		Pattern pattern = Pattern.compile("corpo:formulario:(\\w*):(\\d*):(\\w*)");
		Matcher matcher = pattern.matcher(exp);
		
		String meio = null;
		
		if (matcher.find()){
			meio = matcher.group(1);
		}
		return meio;
		
	}
	
	/**
	 * Retorna o índice dos detalhes baseado na expressão informada
	 */
	private int getIndiceDetalhe(String exp){

		String indice = null;

		if (!exp.startsWith("corpo:formulario:")){

			int primeiro = exp.indexOf(":");
			int segundo = exp.substring(primeiro + 1).indexOf(":") + primeiro;
			int terceiro = exp.lastIndexOf(":");

			indice = exp.substring( segundo + 2 ,terceiro);
		} else {
			// corpo:formulario:(\w*):(\d*):(\w*)
			
			Pattern pattern = Pattern.compile("corpo:formulario:(\\w*):(\\d*):(\\w*)");
			Matcher matcher = pattern.matcher(exp);
			
			if (matcher.find()){
				indice = matcher.group(2);
			}

		}

		return Integer.parseInt(indice);

	}
}
