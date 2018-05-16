/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.integration;

import java.io.Serializable;
import java.util.Calendar;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.powerlogic.jcompany.commons.PlcBaseUserProfileDTO;

/**
 * Interface para o helper de controle de segurança do jSecurity.
 * 
 * @since jCompany 3.0
 * 
 * @author Roberto Badaró
 */
public interface IPlcJSecurityApi {

	/**
	 * Inicializa os dados relativos a segurança.
	 * Chamado na Classe PlcServletContextListener - contextInitialized.
	 */
	public void iniciaSeguranca();

	/**
	 * Método disponibilizado para tratar todas as requisições.
	 * @param request
	 * @param response
	 */
	public void efetivaSeguranca(ServletRequest request, ServletResponse response);
		
	/**
	 * A aplicação está configurada para utilizar o JSecurity?
	 * 
	 * @return
	 */
	public boolean isUsaJSecurity();
	
	/**
	 * Registra os recursos da aplicação para utilização no serviço de cadastro automático online a partir da interface web do jSecurity.
	 * 
	 * @param servletContext
	 */
	public void registraRecursos(ServletContext servletContext);	

	/**
	 * Configura o serviço de visão para mostrar/ocultar botões e campos.
	 *
	 * @param visao PlcVisaoUtil
	 * @param request
	 * 
	 */
	public void configuraVisao(HttpServletRequest request) ;

	/**
	 * Carrega e configura perfil do usuário.
	 * 
	 * @param request
	 * @param response
	 * @param plcPerfilVO
	 * @param plc
	 * @return
	 * @throws Exception
	 */
	public PlcBaseUserProfileDTO carregaProfileJSecurity(HttpServletRequest request, HttpServletResponse response, PlcBaseUserProfileDTO usuario);
	
	/**
	 * Carrega e configura perfil do usuário.
	 * 
	 * @param request
	 * @param response
	 * @param plcPerfilVO
	 * @param plc
	 * @return
	 * @throws Exception
	 */
	public Serializable carregaProfileCompletoJSecurity(HttpServletRequest request, HttpServletResponse response, String login, String[] siglaAplicacoes);
	
	/**
	 * Recupera o cadastro parcial do usuário. Não traz a senha.
	 * 
	 * @param request
	 * @param response
	 * @param plcPerfilVO
	 * @param plc
	 * @return
	 * @throws Exception
	 */
	public Serializable recuperaUsuarioJSecurity(HttpServletRequest request, HttpServletResponse response, PlcBaseUserProfileDTO usuario) ;
	
	/**
	 * Verifica se há negação de acesso ao recurso para o perfil informado.
	 * 
	 * @param request
	 * @param perfil
	 * @return
	 * @throws Exception
	 */
	public boolean isAcessoNegado(HttpServletRequest request, PlcBaseUserProfileDTO usuario) ;

	/**
	 * Verifica se há negação de acesso ao recurso para o perfil informado.
	 * 
	 * @param recurso
	 * @param perfil
	 * @return
	 * @throws Exception
	 */
	public boolean isAcessoNegado(String recurso, PlcBaseUserProfileDTO usuario);
	
	/**
	 * Verifica se há negação de acesso ao recurso para o perfil informado relativo ao cadastro de Horario.
	 * 
	 * @param recurso
	 * @param perfil
	 * @return
	 * @throws Exception
	 */
	public boolean verificaHorarioDeAcessoNegado(PlcBaseUserProfileDTO usuario); 
	
	/**
	 * Valida se o horario do usuario e valido.
	 * 
	 * @param recurso
	 * @param perfil
	 * @return
	 * @throws Exception
	 */
	public boolean validaHorario(Calendar instance, Object horario);
	
}
