/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.empresa.controller.listener;


import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.controller.listener.PlcServletContextListener;


/**
 * {@inheritDoc}
 */
public abstract class EmpServletContextListener extends PlcServletContextListener {

	protected static final Logger log = Logger.getLogger(EmpServletContextListener.class.getCanonicalName());

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void cdAoEncerrarAplicacao(ServletContextEvent event) throws PlcException {
		log.info("Encerrando a Aplicacao");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void ciAoInicializarAplicacao(ServletContextEvent event) throws PlcException {

		log.info("Tratamento da Aplicacao: Inicializando a Aplicacao");

		try {

		} catch (PlcException plcE) {
			erroMsg(event.getServletContext(), "Erro inesperado em ciAoInicializarAplicacao", plcE.getCausaRaiz());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
	}

}
