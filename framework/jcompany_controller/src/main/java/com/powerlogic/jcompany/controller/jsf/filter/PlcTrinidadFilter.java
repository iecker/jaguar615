/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.filter;

import java.io.IOException;
import java.util.logging.Level;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.context.RenderingContext;
import org.apache.myfaces.trinidad.logging.TrinidadLogger;
import org.apache.myfaces.trinidad.webapp.TrinidadFilter;
import org.apache.myfaces.trinidadinternal.application.ViewHandlerImpl;
import org.apache.myfaces.trinidadinternal.context.RequestContextImpl;
import org.apache.myfaces.trinidadinternal.renderkit.core.CoreRenderKit;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.util.PlcMsgUtil;
import com.powerlogic.jcompany.domain.validation.PlcMessage;

/**
 * Mero adaptador para manter isolamento e IoC na camada de arquitetura
 */
public class PlcTrinidadFilter extends TrinidadFilter implements Filter {

	private static final Logger logControle = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_CONTROLE);
	
	PlcMsgUtil msgUtil;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		try {
			super.doFilter(request, response, chain);
		} catch (ServletException e) {
			logControle.error(e.getMessage());
			msgUtil.msg(e.getMessage()!=null?e.getMessage():e.toString(), PlcMessage.Cor.msgVermelhoPlc.toString());
			RequestDispatcher rq = request.getRequestDispatcher("/");
			rq.forward(request, response);
			//((HttpServletResponse)response).sendRedirect(((HttpServletRequest)request).getContextPath());
		}	
		
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		super.init(filterConfig);
		// Adicionado para eliminar logs desnecessarios do Trinidad.
		TrinidadLogger.createTrinidadLogger(CoreRenderKit.class).setLevel(Level.OFF);
		TrinidadLogger.createTrinidadLogger(RenderingContext.class).setLevel(Level.OFF);
		TrinidadLogger.createTrinidadLogger(ViewHandlerImpl.class).setLevel(Level.OFF);
		TrinidadLogger.createTrinidadLogger(RequestContextImpl.class).setLevel(Level.OFF);
		
		msgUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		
	}

	
}
