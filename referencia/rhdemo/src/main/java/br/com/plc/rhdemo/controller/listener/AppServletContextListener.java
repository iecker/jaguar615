/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package br.com.plc.rhdemo.controller.listener;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.controller.listener.PlcServletContextListener;

/**
 * Classe destinada a programaï¿½ï¿½es em tempo de inicializaï¿½ï¿½o  da aplicaï¿½ï¿½o
 */
public class AppServletContextListener extends PlcServletContextListener {
	
	protected static final Logger log = Logger.getLogger(AppServletContextListener.class.getCanonicalName());

	@Override
	public void cdAoEncerrarAplicacao(ServletContextEvent event)
			throws PlcException {
		log.log(Level.INFO, "Encerrando a Aplicacao");

	}

	@Override
	public void ciAoInicializarAplicacao(ServletContextEvent event)
			throws PlcException {
		log.log(Level.INFO, "Tratamento da Aplicacao: Inicializando a Aplicacao");
	}
	
}
