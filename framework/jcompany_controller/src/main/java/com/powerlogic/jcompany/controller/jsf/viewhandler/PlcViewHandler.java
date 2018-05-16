/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.jsf.viewhandler;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregationPOJO;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;

/**
 * Especializa um ViewHandler de forma a manter-se compatível com as implementações
 * da camada de visão atuais (Facelets)
 * 
 * @author george
 *
 */
public class PlcViewHandler extends ViewHandlerWrapper {

	private ViewHandler delegate;
	
	public PlcViewHandler(ViewHandler delegate) {
		this.delegate = delegate;
	}
	
	@Override
	public ViewHandler getWrapped() {
		return delegate;
	}
	

	/**
	 * TODO - Revisar
	 */
	@Override
	public String getActionURL(FacesContext context, String viewId) {
		
		if (viewId.contains("#{")){
			
			Pattern pattern = Pattern.compile("#\\{(.*)\\}");
			Matcher matcher = pattern.matcher(viewId);
			
			while (matcher.find()){
				String expression = matcher.group();
				PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
				Object valorExpression = elUtil.evaluateExpressionGet(expression, Object.class);
				viewId = viewId.replace(expression, valorExpression != null?valorExpression.toString():"");
												
			}
			
		}
		
		String actionURL = super.getActionURL(context, viewId);
		
		if (actionURL.contains("#{")){
			
			Pattern pattern = Pattern.compile("#\\{(.*)\\}");
			Matcher matcher = pattern.matcher(actionURL);
			
			while (matcher.find()){
				PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
				String expression 		= matcher.group();
				Object valorExpression 	= elUtil.evaluateExpressionGet(expression, Object.class);
				actionURL				= actionURL.replace(expression, valorExpression != null?valorExpression.toString():"");
												
			}
		
		}
		
		Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
		
		String modoJanelaPlc = requestParameterMap.get("modoJanelaPlc");
		
		if (StringUtils.isNotBlank(modoJanelaPlc) && !"modal".equals(modoJanelaPlc)){
			actionURL = actionURL.contains("?") ? actionURL + "&modoJanelaPlc=" + modoJanelaPlc : actionURL + "?modoJanelaPlc=" + modoJanelaPlc; 
		}
		
		
		
		String fwPlc = requestParameterMap.get("fwPlc");
		
		if (StringUtils.isNotBlank(fwPlc)){
			try {
				
				PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);
				PlcURLUtil urlUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcURLUtil.class, QPlcDefaultLiteral.INSTANCE);
				PlcConfigUtil configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);
				PlcConfigAggregationPOJO _configAcao = configUtil.getConfigAggregation(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest()));
				if ( _configAcao.pattern().formPattern()==FormPattern.Sel ||  _configAcao.pattern().formPattern()==FormPattern.Con){
					Object acaoNovo = context.getExternalContext().getRequestMap().get(PlcJsfConstantes.ACAO.PLC_IND_ACAO_NOVO);
					if (PlcConstants.SIM.equals(acaoNovo)){
						String inicio = actionURL.substring(0, actionURL.indexOf("/f/t"));
						String ultimo = "";
						if (actionURL.contains("?")){
							ultimo = actionURL.substring(actionURL.indexOf("?"), actionURL.length());
						}
						actionURL = inicio + "/f/t/" + fwPlc + ultimo;
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}

		return actionURL;
	}
}
