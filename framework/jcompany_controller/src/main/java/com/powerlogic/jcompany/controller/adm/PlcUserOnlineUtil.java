/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.adm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionEvent;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.util.PlcCacheUtil;

/**
 * Mantém um HashMap de usuário on-line em caching em nível da aplicação para monitoria e visualizacao "quem está on-line"
 */
@SPlcUtil
@QPlcDefault
public class PlcUserOnlineUtil {
	
	@Inject
	protected transient Logger log;
	
    private Map usuariosOnLine = new ConcurrentHashMap();
    
	public PlcUserOnlineUtil() {	
		
	}
	
	 /**
	  * jCompany. Adiciona uma nova sessão ao caching
	  */
	public void addSession(HttpSessionEvent event) {
		 
		if (usuariosOnLine == null) {
			usuariosOnLine = new ConcurrentHashMap();
		}
		
		PlcUserOnlineVO plcUsu = new PlcUserOnlineVO();
		
		plcUsu.setIdSessao(event.getSession().getId());
		plcUsu.setDesde(new java.util.Date());
		plcUsu.setTimeoutUsuario(event.getSession().getMaxInactiveInterval()+"");
		 
		usuariosOnLine.put(event.getSession().getId(),plcUsu);
		
	}
	
	/**
	 * jCompany. Remove uma nova sessão ao caching
	 */
	public void removeSession(HttpSessionEvent event) {
		
		if (log.isDebugEnabled()) {
			log.debug( "####### Vai remover sessao: "+event.getSession().getId());
		}
		
		if (usuariosOnLine == null) 
		  	log.warn( "Sessao nula no momento do login. Nao foi possivel atualizar quem esta on-line corretamente!");
		else {
	
			if (log.isDebugEnabled())
				log.debug( "Total caching de sessoes antes: "+usuariosOnLine.size());
				
			usuariosOnLine.remove(event.getSession().getId());
			
			if (log.isDebugEnabled())
				log.debug( "Total caching de sessoes depois: "+usuariosOnLine.size());
				
		}	
		
	}
	
	/**
	  * jCompany. Altera uma sessão para usuário autenticado
	  */
	public void addAuthentication(HttpServletRequest request,String login) {
	
		PlcCacheUtil cacheUtil = (PlcCacheUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcCacheUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		if (usuariosOnLine == null) 
			log.warn( "Sessao nula no momento do login. Nao foi possivel atualizar quem esta on-line corretamente!");
		else {
					
			PlcUserOnlineVO plcUsu = (PlcUserOnlineVO) usuariosOnLine.get(request.getSession().getId());
				
			if (login != null && plcUsu != null) {
				plcUsu.setIP(request.getRemoteAddr());
				plcUsu.setLogin(login);
			}
		}
			
	}

	/**
	 * Adiciona informações de requisição. Chamado a cada request
	 */
	public void addInfoRequest(HttpServletRequest request) {
		
		PlcCacheUtil cacheUtil = (PlcCacheUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcCacheUtil.class, QPlcDefaultLiteral.INSTANCE);
				
		if (usuariosOnLine != null) {
			
			PlcUserOnlineVO plcUsu = (PlcUserOnlineVO) usuariosOnLine.get(request.getSession().getId());
			
			if (plcUsu != null){
				plcUsu.setUltRequisicao(new Date());					
			}
		}
	}

	public Map getUsuariosOnLine() {
		return usuariosOnLine;
	}

	public void setUsuariosOnLine(Map usuariosOnLine) {
		this.usuariosOnLine = usuariosOnLine;
	}	

	
}
