/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.util;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBaseUserProfileDTO;
import com.powerlogic.jcompany.commons.PlcBaseUserProfileVO;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.facade.IPlcFacade;
import com.powerlogic.jcompany.commons.integration.IPlcJMonitorApi;
import com.powerlogic.jcompany.commons.integration.IPlcJMonitorApi.CICLO_VIDA;
import com.powerlogic.jcompany.commons.integration.IPlcJSecurityApi;
import com.powerlogic.jcompany.commons.integration.jmonitor.PlcClientDTO;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.adm.PlcUserOnlineUtil;

/**
 * jCompany. Serviço. Abstrata. Ancestral para classe que deve
 * encapsular todas as regras de "Profiling" de usuários, ou seja, que irá
 * recuperar e registrar as informações do usuário autenticado na sessão.
 */
@SPlcUtil
@QPlcDefault
abstract public class PlcBaseUserProfileUtil implements Serializable {

	@Inject
    protected transient Logger log;
    
    @Inject
	transient IPlcJMonitorApi jMonitorApi;

	@Inject 
	transient IPlcJSecurityApi jSecurityApi;	
    
    @Inject @QPlcDefault
	PlcConfigUtil configUtil;
    
    @Inject @QPlcDefault
    PlcIocControllerFacadeUtil plcIocControllerFacadeUtil;
    
    @Inject @QPlcDefault
    PlcExceptionHandlerUtil plcExceptionHandlerUtil;
    
    @Inject @QPlcDefault
    PlcCookieUtil cookieUtil;
    
    public PlcBaseUserProfileUtil() {
    	
    }
    
    /**
 	 * Facade jCompany p/ camada de modelo
 	 */
 	protected IPlcFacade iPlcFacade = null;
 	

    /**
     * Retorna a Interface do Serviço de Persistência armazenado no escopo
     * da aplicação
     * 
     * @since jCompany 3.0
     */
    protected IPlcFacade getServiceFacade()  {
    	return plcIocControllerFacadeUtil.getFacade();
    }


    /**
     * Registra dados do perfil do usuário logado. Chamado para criar o VO
     * de profile do usuário.
     * 
     * @param request
     * @param response
     */
    public void registryUserProfile(HttpServletRequest request, HttpServletResponse response) {

		try {
			
		    if (request.getUserPrincipal() != null) { 
		
				HttpSession sess = request.getSession();
		
				if (sess.getAttribute(PlcConstants.USER_INFO_KEY) == null || 
						!((PlcBaseUserProfileVO)sess.getAttribute(PlcConstants.USER_INFO_KEY)).getLogin().equals(request.getUserPrincipal().getName())  ) {
		
				    log.debug( "Vai recuperar perfil de usuarios e disponibilizar na sessao");
		
				    PlcBaseUserProfileVO plcUsuVO = PlcCDIUtil.getInstance().getInstanceByType(PlcBaseUserProfileVO.class, QPlcDefaultLiteral.INSTANCE);
		
				    plcUsuVO = registryGenericProfile(request, response, plcUsuVO);
		
				    log.info("###### Registrando profile para o Usuário.: " + plcUsuVO.getLogin());
				    
				    sess.setAttribute(PlcConstants.USER_INFO_KEY, plcUsuVO);
		
				    log.debug( "Colocou o VO de perfil de usuario na sessao com chave USER_INFO");
				    
				} 
		    
		    }
		    
		} catch (Exception e) {
		    log.error( "Erro interceptado no request processor:" + e, e);
		}
		
    }

	/**
	 * jCompany. Realiza procedimentos após a autenticação do usuario, como
	 * registro de informações do login, alteração do time-out (se desejado)
	 * e mensagem para objeto específico do usuario complementar o perfil do
	 * usuário com filtros de segurança, alteração do "quem está on-line" e
	 * outras informações específicas pertinentes ao relacionamento entre o
	 * usuario específico e a aplicação.
	 */
    public PlcBaseUserProfileVO registryGenericProfile(HttpServletRequest request, HttpServletResponse response, PlcBaseUserProfileVO plcPerfilVO) throws Exception {

    	log.debug( "########## Entrou em registro de perfil generico de usuarios");

    	// jCompany Monitor
    	jMonitorApi.logSessao(request.getSession().getId(),CICLO_VIDA.INICIO);
 
    	try {
    		
    		//cria instancia do propertyUtilsBean para evitar diversas chamadas a metodo syncronized
    		PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();

    		if (request.getRemoteAddr() != null)
    			plcPerfilVO.setIp(request.getRemoteAddr());

    		plcPerfilVO.setLogin(request.getUserPrincipal().getName());

    		Principal user = request.getUserPrincipal();
    		if (user != null) {

    			List<String> roles = null;
    			try {
    				Object property = propertyUtilsBean.getProperty(user, "roles");
    				if (property instanceof Iterator){
    					roles = new ArrayList ();
    					for (Iterator iterator = (Iterator)property; iterator.hasNext();) {
    						Object object = (Object) iterator.next();
    						String rolename = (String)propertyUtilsBean.getProperty(object, "rolename");
    						if (rolename!=null)
    							roles.add(rolename);
    					}

    				} else if (property instanceof List){
    					roles = (List) property;
    				} else if (property instanceof String[]){ // JOSSO
    					roles = new ArrayList<String>();
    					String[] _roles = (String[]) property;
    					for (String rolename : _roles) {
    						if (rolename!=null)
    							roles.add(rolename);
    					}    					
    				}
    			} catch (Exception e) {
    				log.info( "Classe com.powerlogic.jsecurity.jaas.User encontrada e não possui propriedade roles: " + e.getMessage());
    				roles = null;
    			}
    			plcPerfilVO.setRoles(roles);
    		}

    		// Carrega profile do usuário, quando jSecurity ligado.
    		if (jSecurityApi.isUsaJSecurity()) {
    			PlcBaseUserProfileDTO dto = new PlcBaseUserProfileDTO();
    			propertyUtilsBean.copyProperties(dto, plcPerfilVO);
    			dto  = jSecurityApi.carregaProfileJSecurity(request, response, dto);
    			propertyUtilsBean.copyProperties(plcPerfilVO, dto);    			
    		}

    		plcPerfilVO = registrySpecificProfile(request, response, plcPerfilVO);

    		// Configura timeout da sessão.
    		// Preferência para o valor informado no perfil, senão, do
    		// web.xml.
    		Long timeout = plcPerfilVO.getTimeout();
    		if (timeout != null && timeout.longValue() > 0) {
    			request.getSession().setMaxInactiveInterval(timeout.intValue());
    		}

    		// Atualiza quem está on-line
    		PlcUserOnlineUtil usuarioOnlineUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcUserOnlineUtil.class, QPlcDefaultLiteral.INSTANCE);
    		usuarioOnlineUtil.addAuthentication(request, plcPerfilVO.getLogin());
    		plcPerfilVO.setProfileLoaded(true);
    		
    		registryGenericProfile(request);
    		


    	} catch (Exception e) {
    		log.fatal( "Erro ao tentar inicializar perfil de usuario: " + e, e);
    		plcPerfilVO.setProfileLoaded(false);
    		e.printStackTrace();
    		request.setAttribute("erro599", plcExceptionHandlerUtil.stackTraceToString(e, true));
    		response.sendError(599, e.toString());
    		response.flushBuffer();
    	}

    	return plcPerfilVO;
    }

    private void registryGenericProfile(HttpServletRequest request) {

		PlcClientDTO clienteDTO = new PlcClientDTO();
		
		if (request.getUserPrincipal() != null) {
			clienteDTO.setLogin(request.getUserPrincipal().getName());
		} else if (request.getRemoteUser() != null) {
			clienteDTO.setLogin(request.getRemoteUser());
		}
		 
		clienteDTO.setIp(request.getRemoteAddr());
		clienteDTO.setHost(request.getRemoteHost());
		clienteDTO.setUserAgent(request.getHeader("user-agent"));
		clienteDTO.setResolucao(recuperaResolucaoVideo(request));
		
		request.getSession().setAttribute(PlcConstants.BROWSER_INFO_KEY, clienteDTO);
		
	}
    
	 /**
     * Recupera a resolução do vídeo do cliente. Procura primeiro na sessão, senão encontrar, procura nos cookies.
     * 
     * @param request
     */
    private String recuperaResolucaoVideo(HttpServletRequest request) {
    	
    	String res = null;
    	
    	if(request.isRequestedSessionIdValid()){
    		res = (String) request.getSession().getAttribute("resolucaoPlc");
    	}
        
        if (res == null) {
        	res = cookieUtil.getCookieValue(request, "resolucaoPlc");
        }
        if (log.isDebugEnabled()) {
            log.debug( "Achou resolucao = "+res);
        }
        return res;
    }


	/**
	 * jCompany. Deve ser implementado no descendente para fazer regras de
	 * recuperação e registro na sessão, no PlcBaseUsuarioPerfilVO, das
	 * informações de autenticação (login) e profiling (nome, email, etc.)
	 * do usuário.
	 */
    abstract public PlcBaseUserProfileVO registrySpecificProfile(HttpServletRequest request, HttpServletResponse response, PlcBaseUserProfileVO plcPerfilVO) throws Exception;

}