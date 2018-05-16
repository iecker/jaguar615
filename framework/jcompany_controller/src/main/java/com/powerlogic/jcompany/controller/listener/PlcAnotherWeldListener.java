/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.listener;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.servlet.PlcServletContextProducer;
import com.powerlogic.jcompany.controller.servlet.PlcServletUtil;

/**
 * Listener para adicionar request e servletContext no contexto do CDI
 * Utilizável para servidores de aplicação que não sejam Tomcat.
 * 
 * @author baldini
 *
 */
public class PlcAnotherWeldListener implements ServletContextListener, ServletRequestListener {

	@Inject
	protected transient Logger log;
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		//event.getServletContext().setAttribute("org.jboss.resteasy.spi.ResteasyDeployment", new PlcResteasyDeployment(event.getServletContext()));
		//super.contextInitialized(event);
		try {
			PlcCDIUtil.getInstance().getInstanceByType(PlcServletContextProducer.class, QPlcDefaultLiteral.INSTANCE).setServletContext(event.getServletContext());
		} catch (Exception e) {
			log.warn( "Erro generico no contextInitialized: " + e.getMessage());
		}	
	}


	@Override
	public void requestInitialized(ServletRequestEvent sre) {
		// Configura encoding
		try {
			sre.getServletRequest().setCharacterEncoding("UTF-8");
			//super.requestInitialized(sre);
			PlcCDIUtil.getInstance().getInstanceByType(PlcServletUtil.class, QPlcDefaultLiteral.INSTANCE).setRequest(sre.getServletRequest());
		} catch (UnsupportedEncodingException ex) {
			log.warn( "Erro ao forçar encode UTF-8 no no request: " + ex.getMessage());
		} catch (ClassCastException ex) {
			log.warn( "Erro no requestInitialized: " + ex.getMessage());
		} catch (Exception e) {
			// Log null ocorre em momento de login do WAS8
			if (log!=null) {
				log.warn( "Erro generico no requestInitialized: " + e.getMessage());
			}
		}
	}


	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void requestDestroyed(ServletRequestEvent sre) {
		// TODO Auto-generated method stub
		
	}


}
