package com.powerlogic.jcompany.controller.servlet;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.servlet.ServletContext;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;

@ApplicationScoped
@QPlcDefault
public class PlcServletContextProducer{
	
	private ServletContext servletContext;
	
	@Produces @QPlcDefault
	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	
}