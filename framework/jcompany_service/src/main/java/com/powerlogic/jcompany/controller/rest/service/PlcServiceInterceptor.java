package com.powerlogic.jcompany.controller.rest.service;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;

import org.apache.commons.lang.StringUtils;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;

import com.powerlogic.jcompany.controller.rest.producers.PlcJAXRSProducer;
import com.powerlogic.jcompany.controller.rest.producers.PlcServiceProducer;

/**
 * @author Adolfo Jr.
 */
@Provider
@ServerInterceptor
public class PlcServiceInterceptor extends PlcBaseService implements PreProcessInterceptor {
	
	@Context
	private HttpHeaders httpHeaders;
	@Context
	private UriInfo uriInfo;
	@Context
	private HttpServletRequest servletRequest;
	@Context
	private ServletConfig servletConfig;
	@Context
	private ServletContext servletContext;
	@Context
	private SecurityContext securityContext;
	@Context
	private Providers providers;
	
	@Override
	public ServerResponse preProcess(HttpRequest request, ResourceMethod method) throws Failure, WebApplicationException {
		// Inicializa integração JAXRS-CDI!
		PlcJAXRSProducer jaxrsProducer = getJAXRSProducer();
		// configura as injeções de JAXRS.
		configJAXRS(jaxrsProducer);
		// Inicializa Configuração do Contexto do Service.
		PlcServiceProducer serviceProducer = getServiceProducer();
		// Checa se o metodo REST utiliza o Service
		if (StringUtils.isNotEmpty(serviceProducer.getControllerPath())) {
			// Verifica se foi encontrado um controller que atende á requisição.
			if (serviceProducer.getController() == null) {
				return ServerResponse.copyIfNotServerResponse(Response.status(Status.NOT_FOUND).build());
			}
			// Verifica se foi encontrado um conversor que atende á requisição.
			if (serviceProducer.getConversor() == null) {
				return ServerResponse.copyIfNotServerResponse(Response.status(Status.NOT_ACCEPTABLE).build());
			}
		}
		return null;
	}

	protected void configJAXRS(PlcJAXRSProducer jaxrsProducer) {
		jaxrsProducer.setServletRequest(servletRequest);
		jaxrsProducer.setServletConfig(servletConfig);
		jaxrsProducer.setServletContext(servletContext);
		jaxrsProducer.setSecurityContext(securityContext);
		jaxrsProducer.setHttpHeaders(httpHeaders);
		jaxrsProducer.setUriInfo(uriInfo);
		jaxrsProducer.setProviders(providers);
	}
}
