/* Jaguar-jCompany Developer Suite. Powerlogic 2010-2014. Please read licensing information or contact Powerlogic 
 * for more information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br        */ 
package com.empresa.rhtutorial.controller.listener;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.controller.listener.PlcServletContextListener;

/**
 * Classe destinada a programaÃ§Ãµes em tempo de inicializaÃ§Ã£o  da aplicaÃ§Ã£o
 */
public class AppServletContextListener extends PlcServletContextListener {
	
	protected static final Logger log = Logger.getLogger(AppServletContextListener.class.getCanonicalName());

	@Override
	public void cdAoEncerrarAplicacao(ServletContextEvent event)
			throws PlcException {
		log.info( "Encerrando a Aplicacao");

	}

	@Override
	public void ciAoInicializarAplicacao(ServletContextEvent event)
			throws PlcException {
		log.info( "Tratamento da Aplicacao: Inicializando a Aplicacao");
	}
	
}
