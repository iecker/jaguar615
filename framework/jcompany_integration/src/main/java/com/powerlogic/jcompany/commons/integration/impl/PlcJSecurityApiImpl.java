/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */


package com.powerlogic.jcompany.commons.integration.impl;

import java.io.Serializable;
import java.util.Calendar;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.powerlogic.jcompany.commons.PlcBaseUserProfileDTO;
import com.powerlogic.jcompany.commons.integration.IPlcJSecurityApi;

public class PlcJSecurityApiImpl implements IPlcJSecurityApi {

	@Override
	public void iniciaSeguranca() {

	}

	@Override
	public PlcBaseUserProfileDTO carregaProfileJSecurity(HttpServletRequest request, HttpServletResponse response, PlcBaseUserProfileDTO usuario) {

		return null;
	}

	@Override
	public Serializable carregaProfileCompletoJSecurity(HttpServletRequest request, HttpServletResponse response, String login, String[] siglaAplicacoes) {

		return null;
	}

	@Override
	public void configuraVisao(HttpServletRequest request) {

	}

	@Override
	public void efetivaSeguranca(ServletRequest request, ServletResponse response) {

	}

	@Override
	public boolean isAcessoNegado(HttpServletRequest request, PlcBaseUserProfileDTO usuario) {

		return false;
	}

	@Override
	public boolean isAcessoNegado(String recurso, PlcBaseUserProfileDTO usuario) {

		return false;
	}

	@Override
	public boolean isUsaJSecurity() {

		return false;
	}

	@Override
	public Serializable recuperaUsuarioJSecurity(HttpServletRequest request, HttpServletResponse response, PlcBaseUserProfileDTO usuario) {

		return null;
	}

	@Override
	public void registraRecursos(ServletContext servletContext) {

	}

	@Override
	public boolean validaHorario(Calendar instance, Object horario) {

		return false;
	}

	@Override
	public boolean verificaHorarioDeAcessoNegado(PlcBaseUserProfileDTO usuario) {

		return false;
	}

}
