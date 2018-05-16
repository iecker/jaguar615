/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.util;

import java.security.Principal;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBaseUserProfileVO;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregationPOJO;

/**
 * jCompany 5.0 Base para composição do 'contexto' para envio de informações
 * do cliente web para a camada modelo em POJO. É extendido para utilização diferenciada
 * no Struts, JSF ou, mais recentemente, diretamente na camada modelo (@see PlcContextModeloMontaHelper).
 * 
 * @since jCompany 5.0
 */
@SPlcUtil
@QPlcDefault
public abstract class PlcBaseContextUtil {
	
	@Inject
	protected transient Logger log;
	
	/**
	 * Serviço injetado e gerenciado pelo CDI
	 */
	@Inject @QPlcDefault 
	protected PlcAnnotationUtil annotationUtil;

	public PlcBaseContextUtil() { 
		
	}
 	

	/**
     * Monta fabrica default do VO em função de anotação
	 * 
	 * @param context
	 * @param valueObjectClass
	 * @param persistenciaServiceManagers
	 * @param configAcao
	 * @param siglaModulos
	 * 
	 * 
     * @since jCompany 5.5
	 */
	public void createDbFactory(PlcBaseContextVO context, Class valueObjectClass, PlcConfigAggregationPOJO configAcao, String[] siglaModulos)  {
		log.debug( "########## Entrou em montaFabrica");
		
		String nome = annotationUtil.getDbFactoryName(valueObjectClass);
		context.setDbFactory(nome);
		context.setEntityClassPlc(valueObjectClass);
	
	}

	
	/**
	 * 
	 * @param context
	 * @param evitaTrocaId
	 * @param voPerfilUsuario
	 * @param userPrincipal
	 * @param ipUsuario
	 * 
	 * 
     * @since jCompany 5.5
	 */
	public void createProfileRequest(PlcBaseContextVO context, PlcBaseUserProfileVO voPerfilUsuario, Principal userPrincipal, String ipUsuario)  {
		log.debug( "############# Entrou em montaProfile");
		
		/* Parametros da Sessão */
		// Profiling (Perfil do Usuario)
		if (voPerfilUsuario != null) {
			context.setUserProfile(voPerfilUsuario);
		}
		else if (userPrincipal != null) {
			PlcBaseUserProfileVO perfilUsuario = new PlcBaseUserProfileVO();  			
			perfilUsuario.setLogin(userPrincipal.getName());
  			perfilUsuario.setIp(ipUsuario);
  	  		context.setUserProfile(perfilUsuario);
		}
	}

	/**
	 * Monta acao original no context, para uso em lógicas de controle e na camada modelo.
	 * Obs: montar acão no request não é mais escopo desta classe. Deve ser feita por clientes desta classe.
	 * 
	 * @param context
	 * @param evento
	 * 
	 * 
	 * @since jCompany 5.5
	 */
	public void createOriginalAction(PlcBaseContextVO context, String evento)  {
		// Registra ação original clicada pelo usuário (nome dos métodos. Ex: aprova, reprova, pesquisa, etc..		
		if (context!= null) {
			context.setOriginalAction(evento);
		}		
	}


}
