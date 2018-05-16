/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package br.com.plc.rhdemo.controller.listener;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpSessionEvent;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.controller.cache.PlcCacheSessionVO;
import com.powerlogic.jcompany.controller.listener.PlcHttpSessionListener;

/**
 * rhdemofcls. Classe que implementa o DP "Listener" para Sessï¿½o
 */
public class AppHttpSessionListener extends PlcHttpSessionListener {
	/**
	 * rhdemofcls: Realiza procedimentos no momento de encerramento de cada Sessï¿½o 
	 */
    @Override
	public void aoEncerrarSessao (HttpSessionEvent event) {
		
    	log.debug( "Aplicacao: Encerrando Sessao");
	}
	
	/**
	 *  rhdemofcls: Realiza procedimentos no momento de inicializaï¿½ï¿½o de cada Sessï¿½o 
	 */
    @Override
	public void aoInicializarSessao (HttpSessionEvent event,PlcCacheSessionVO plcSessao) {
		
		Logger log = Logger.getLogger(this.getClass().getCanonicalName());
		
		log.debug( "Aplicacao: Iniciando Sessao");
		
		// Coloca CSS default
		// JSF - Coloca informacoes de leiaute na sessao
		plcSessao.setIndLayoutReduzido("N");
		event.getSession().setAttribute(PlcConstants.SESSION_CACHE_KEY,plcSessao);
		
		// Auxiliar provisorio para manter compatibilidade
		event.getSession().setAttribute("contextPathPlc","./../");

	}
}
