/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.util;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.trinidad.bean.FacesBean;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;

@SPlcUtil
@QPlcDefault
public class PlcMsgComponentsUtil {

	public final static String TOKEN_ERROR = "erroComponete";
	
	/**
	 * Serviço injetado e gerenciado pelo CDI
	 */
	@Inject @QPlcDefault 
	protected PlcComponentUtil componentUtil;
	
 	public PlcMsgComponentsUtil() { 
 		
 	}
 	
 	public void createMsgError (FacesBean bean, UIComponent component, String idMensagem, Exception e){
 		
 		Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
 		String clientId = component.getClientId(FacesContext.getCurrentInstance());
 		
 		String message = StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : e.toString();
 		
 		if (StringUtils.isNotBlank(idMensagem)){
 			
 			if (idMensagem.startsWith("#"))
 				message = idMensagem + " Exceção:"+ message;
 			else if (bean != null){
 				String mensagemLoc = componentUtil.createLocalizedMessage(bean, idMensagem, new Object []{});
				message = mensagemLoc + message;
 			}
 		}
 		
 		e.printStackTrace();
 		
 		requestMap.put(TOKEN_ERROR + clientId, message);
 		
 	}
 	
 	public void printMessageError (UIComponent component){
 		try {		
 			
 			FacesContext context = FacesContext.getCurrentInstance();
 			String clientId = component.getClientId(context);
 			String message = (String)context.getExternalContext().getRequestMap().get(TOKEN_ERROR + clientId);

 			if (message != null){
 				ResponseWriter writer = context.getResponseWriter();
 				writer.startElement("span", component);
 				writer.write(message);
 				writer.endElement("span");
 			}
 			
 		} catch (Exception e) {
 			// Se der erro, apenas imprime o stacktrace
 		}
 	}
 	
}
