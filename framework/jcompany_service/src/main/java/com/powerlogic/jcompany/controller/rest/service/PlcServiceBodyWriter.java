/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */
package com.powerlogic.jcompany.controller.rest.service;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.powerlogic.jcompany.controller.rest.api.controller.IPlcController;
import com.powerlogic.jcompany.controller.rest.api.conversor.IPlcConversor;

/**
 * Classe que prove a transformação de dados de entrada e saída, delegando para
 * o {@link IPlcConversor} selecionado para tratar a requisição.
 * 
 * A camada de controle REST {@link PlcServiceResource}, sempre tem no retorno
 * de seus metodos uma intância de um {@link IPlcController}, que dispara a
 * conversão JAXRS para esses tipos atravês desta classe.
 * 
 * @see PlcServiceResource
 * @see IPlcController
 * @see IPlcConversor
 * 
 * @author Adolfo Jr.
 */
@Provider
@Produces("*/*")
public class PlcServiceBodyWriter extends PlcBaseService implements MessageBodyWriter<IPlcController<?, ?>> {

	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return IPlcController.class.isAssignableFrom(type);
	}

	public void writeTo(IPlcController<?, ?> object, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream outputStream) throws IOException, WebApplicationException {

		IPlcController<Object, Object> controller = getController();

		IPlcConversor<IPlcController<Object, Object>> conversor = getConversor();

		if (controller.getEntityCollection() != null) {
			conversor.writeEntityCollection(controller, outputStream);
		} else if (controller.getEntity() != null) {
			conversor.writeEntity(controller, outputStream);
		} else {
			conversor.writeMessages(outputStream);
		}
	}

	public long getSize(IPlcController<?, ?> resposta, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}
}
