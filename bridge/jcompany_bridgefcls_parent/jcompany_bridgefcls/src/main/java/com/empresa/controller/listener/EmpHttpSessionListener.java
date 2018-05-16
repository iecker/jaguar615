/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.empresa.controller.listener;

import javax.servlet.http.HttpSessionEvent;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.controller.cache.PlcCacheSessionVO;
import com.powerlogic.jcompany.controller.listener.PlcHttpSessionListener;

/**
 * Bridge. Classe que implementa o DP "Listener" para Sessão
 */
abstract public class EmpHttpSessionListener extends PlcHttpSessionListener {
	/**
	 * vitoria: Realiza procedimentos no momento de encerramento de cada Sessão
	 */
	@Override
	public void aoEncerrarSessao(HttpSessionEvent event) {

		log.info("Aplicacao: Encerrando Sessao");
	}

	/**
	 * vitoria: Realiza procedimentos no momento de inicialização de cada Sessão
	 */
	@Override
	public void aoInicializarSessao(HttpSessionEvent event, PlcCacheSessionVO plcSessao) {

		Logger log = Logger.getLogger(this.getClass().getCanonicalName());

		log.debug( "Aplicacao: Iniciando Sessao");
		
		// Coloca Javascript do jcompany
		event.getSession().setAttribute(PlcConstants.JAVASCRIPT.JAVASCRIPT_JCOMPANY,"/res/javascript/plc.geral.js");
		// Coloca Javascript complementar da aplicação
		event.getSession().setAttribute(PlcConstants.JAVASCRIPT.JAVASCRIPT_ESPECIFICO,"/res/javascript/app.geral.js");
		// Coloca CSS default
		event.getSession().setAttribute(PlcConstants.GUI.PELE.CSS_ESPECIFICO,"/res/css/" + plcSessao.getPele() +"/PlcPele.css");
			
		// JSF - Coloca informacoes de leiaute na sessao
		plcSessao.setIndLayoutReduzido("N");
		event.getSession().setAttribute(PlcConstants.SESSION_CACHE_KEY,plcSessao);
		
		// Auxiliar provisorio para manter compatibilidade
		event.getSession().setAttribute("contextPathPlc","./../");
		
	}
}
