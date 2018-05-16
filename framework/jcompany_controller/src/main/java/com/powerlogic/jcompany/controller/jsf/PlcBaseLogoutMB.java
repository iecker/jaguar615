/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.jsf;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes.NAVEGACAO;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.bindingtype.PlcLogoutBefore;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;

@QPlcDefault
public class PlcBaseLogoutMB extends PlcBaseParentMB implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	protected transient Logger log;

	@Inject @QPlcDefault 
	protected PlcContextUtil contextUtil;
	
	/**
	 * Método para desconexão do usuário.
	 */
	public String logout() throws Exception {

		HttpServletRequest request = contextUtil.getRequest();
		HttpServletResponse response = contextUtil.getResponse();

		if (request.getAttribute(PlcConstants.ACAO.EVENTO) == null)
			request.setAttribute(PlcConstants.ACAO.EVENTO, PlcConstants.ACAO.EVT_DESCONECTAR);

		if (log.isDebugEnabled())
			log.debug( "############ Vai desconectar a sessao do usuario" + request.getUserPrincipal());

		if (request.getAttribute(PlcConstants.WORKFLOW.IND_ACAO_ORIGINAL) == null)
			request.setAttribute(PlcConstants.WORKFLOW.IND_ACAO_ORIGINAL, PlcConstants.EVENTOS.DESCONECTA);

		if (!logoutBefore()) {
			return defaultNavigationFlow;
		}
		
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if(cookie != null && cookie.getName() != null &&  cookie.getName().equals("JSESSIONID")) {
				cookie = null;
			}
		}
		
		request.getSession().removeAttribute(PlcConstants.USER_INFO_KEY); 

		request.getSession().invalidate(); 
		
		return NAVEGACAO.LOGOUT; 
	}

	
	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * jCompany. Operação para que descendentes ou extensions executem programação antes da desconexão (encerramento da sessão) 
	 * de usuários a partir do botão de desconexão.
	 */
	protected boolean logoutBefore()  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcLogoutBefore>(){});
		return true;	
	}
	
}
