/*  																													
	    				   jCompany Developer Suite																		
			    		Copyright (C) 2008  Powerlogic																	
	 																													
	    Este programa é licenciado com todos os seus códigos fontes. Você pode modificá-los e							
	    utilizá-los livremente, inclusive distribuí-los para terceiros quando fizerem parte de algum aplicativo 		
	    sendo cedido, segundo os termos de licenciamento gerenciado de código aberto da Powerlogic, definidos			
	    na licença 'Powerlogic Open-Source Licence 2.0 (POSL 2.0)'.    													
	  																													
	    A Powerlogic garante o acerto de erros eventualmente encontrados neste código, para os clientes licenciados, 	
	    desde que todos os códigos do programa sejam mantidos intactos, sem modificações por parte do licenciado. 		
	 																													
	    Você deve ter recebido uma cópia da licença POSL 2.0 juntamente com este programa.								
	    Se não recebeu, veja em <http://www.powerlogic.com.br/licencas/posl20/>.										
	 																													
	    Contatos: plc@powerlogic.com.br - www.powerlogic.com.br 														
																														
 */ 
package ###NOME_PACOTE###.controller.listener;

import javax.servlet.http.HttpSessionEvent;

import org.apache.log4j.Logger;

import com.empresa.controller.listener.EmpHttpSessionListener;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.controller.cache.PlcCacheSessionVO;

/**
 * rhdemofcls. Classe que implementa o DP "Listener" para Sessão
 */
public class AppHttpSessionListener extends EmpHttpSessionListener {
	/**
	 * rhdemofcls: Realiza procedimentos no momento de encerramento de cada Sessão 
	 */
    @Override
	public void aoEncerrarSessao (HttpSessionEvent event) {
		
		log.debug( "Aplicacao: Encerrando Sessao");
	}
	
	/**
	 *  rhdemofcls: Realiza procedimentos no momento de inicialização de cada Sessão 
	 */
    @Override
	public void aoInicializarSessao (HttpSessionEvent event,PlcCacheSessionVO plcSessao) {
						
		Logger log = Logger.getLogger(this.getClass().getCanonicalName());
		
		log.debug( "Aplicacao: Iniciando Sessao");
		
		// Coloca CSS default
		// JSF - Coloca informacoes de leiaute na sessao
		event.getSession().setAttribute(PlcConstants.SESSION_CACHE_KEY,plcSessao);
		
		// Auxiliar provisorio para manter compatibilidade
		event.getSession().setAttribute("contextPathPlc","./../");

	}
}
