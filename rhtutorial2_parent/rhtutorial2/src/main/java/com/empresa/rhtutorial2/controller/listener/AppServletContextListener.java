/* Jaguar-jCompany Developer Suite. Powerlogic 2010-2014. Please read licensing information or contact Powerlogic 
 * for more information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br        */ 
package com.empresa.rhtutorial2.controller.listener;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;

import com.empresa.rhtutorial2.entity.ParametroGlobal;
import com.empresa.rhtutorial2.facade.IParametroGlobal;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.PlcIocFacadeUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.listener.PlcServletContextListener;

/**
 * Classe destinada a programações em tempo de inicialização  da aplicação
 */
public class AppServletContextListener extends PlcServletContextListener {
	
	protected static final Logger log = Logger.getLogger(AppServletContextListener.class.getCanonicalName());

	@Override
	public void cdAoEncerrarAplicacao(ServletContextEvent event)
			throws PlcException {
		log.info( "Encerrando a Aplicacao");

	}

	@Override
	public void ciAoInicializarAplicacao(ServletContextEvent event) throws PlcException {
		log.info( "Tratamento da Aplicacao: Inicializando a Aplicacao");

		// Instancia programaticamente pois o Listener não está
		// gerenciado pelo CDI (Tomcat 6 usa Servlet 2.5)
		PlcIocFacadeUtil facadeUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcIocFacadeUtil.class, QPlcDefaultLiteral.INSTANCE);
		//PlcIocFacadeUtil facadeUtil = new PlcIocFacadeUtil();

		// Pega facade criado pelo CDI programaticamente
		IParametroGlobal iParametroGlobal= (IParametroGlobal)
		 facadeUtil.getFacadeSpecific(IParametroGlobal.class);

		ParametroGlobal pg = iParametroGlobal.recuperaParametroGlobal();

		if (pg != null)
			event.getServletContext().setAttribute("parametroGlobal", pg);
	}
	
}
