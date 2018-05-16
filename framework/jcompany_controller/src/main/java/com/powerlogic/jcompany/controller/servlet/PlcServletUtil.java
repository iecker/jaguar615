package com.powerlogic.jcompany.controller.servlet;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;

/**
 * Classe que disponibiliza dados do container Servlet.
 * Por exemplo: Armazena objetos como ServletContext, ServletRequest, ServletResponse, HttpSession.
 * e também disponibiliza todos no contexto do CDI.
 * @author Bruno Carneiro
 *
 */

@SPlcUtil
@RequestScoped
@QPlcDefault
public class PlcServletUtil {
	
	
	private ServletRequest request;
	private ServletResponse response;
	private HttpSession session;
	
	@Inject @QPlcDefault
	private PlcServletContextProducer servletContextProducer; 
	
	public ServletContext getServletContext() {
		return servletContextProducer.getServletContext();
	}
	
	
	public void setServletContext(ServletContext servletContext) {
		servletContextProducer.setServletContext(servletContext);
	}
	
	public void setRequest(ServletRequest request) {
		this.request = request;
		setSession(((HttpServletRequest)request).getSession());
	}
	
	
	@Produces @QPlcDefault @RequestScoped
	public HttpServletRequest getHttpRequest() {
		return (HttpServletRequest) request;
	}
	
	@Produces @QPlcDefault @RequestScoped
	public HttpServletResponse getHttpResponse() {
		return (HttpServletResponse) response;
	}
	
	
	
	public void setResponse(ServletResponse response) {
		this.response = response;
	}


	@Produces @QPlcDefault @SessionScoped
	public HttpSession getSession() {
		return session;
	}


	public void setSession(HttpSession session) {
		this.session = session;
	}
	
	
	

}


