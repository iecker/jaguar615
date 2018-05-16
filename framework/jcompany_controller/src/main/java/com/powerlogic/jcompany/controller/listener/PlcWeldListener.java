/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.listener;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpSessionEvent;

import org.apache.log4j.Logger;
import org.jboss.weld.environment.servlet.Listener;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.servlet.PlcServletContextProducer;
import com.powerlogic.jcompany.controller.servlet.PlcServletUtil;

public class PlcWeldListener extends Listener {

	@Inject
	protected transient Logger log;
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		//event.getServletContext().setAttribute("org.jboss.resteasy.spi.ResteasyDeployment", new PlcResteasyDeployment(event.getServletContext()));
		try {
			InitialContext.doLookup("java:comp/BeanManager");
		} catch (NamingException e) {
			super.contextInitialized(event);
		}
		PlcCDIUtil.getInstance().getInstanceByType(PlcServletContextProducer.class, QPlcDefaultLiteral.INSTANCE).setServletContext(event.getServletContext());
	}
	
	/**
	 * extensão feita para retirar o log de WELD-001303 na tela de login enquanto o Tomcat não resolve o problema.
	 */
	@Override
	public void sessionCreated(HttpSessionEvent se)
	{
		try {
			super.sessionCreated(se);
		} catch (Exception ex) {
			log.warn( "Tela de login causa exceção no Weld: "+ex.getMessage());
		}
	}

	@Override
	public void requestInitialized(ServletRequestEvent sre) {
		// Configura encoding
		try {
			sre.getServletRequest().setCharacterEncoding("UTF-8");
			super.requestInitialized(sre);
			PlcCDIUtil.getInstance().getInstanceByType(PlcServletUtil.class, QPlcDefaultLiteral.INSTANCE).setRequest(sre.getServletRequest());
		} catch (UnsupportedEncodingException ex) {
			log.warn( "Erro ao forçar encode UTF-8 no no request: " + ex.getMessage());
		} catch (ClassCastException ex) {
			log.warn( "Erro no requestInitialized: " + ex.getMessage());
		}
	}

}
