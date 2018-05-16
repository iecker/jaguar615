/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */
package com.powerlogic.jcompany.controller.rest.service;

import java.io.ByteArrayOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.rest.api.controller.IPlcController;
import com.powerlogic.jcompany.controller.rest.api.conversor.IPlcConversor;
import com.powerlogic.jcompany.controller.util.PlcExceptionHandlerUtil;
import com.powerlogic.jcompany.controller.util.PlcMsgUtil;
import com.powerlogic.jcompany.domain.validation.PlcMessage;
import com.powerlogic.jcompany.domain.validation.PlcMessage.Cor;

/**
 * @author savio
 */
@Provider
public class PlcServiceExceptionHandler extends PlcBaseService implements ExceptionMapper<Throwable> {

	
	private Logger log = Logger.getLogger(this.getClass().getCanonicalName());

	public Response toResponse(Throwable throwable) {
		PlcExceptionHandlerUtil exceptionHandlerUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcExceptionHandlerUtil.class, QPlcDefaultLiteral.INSTANCE);
		HttpServletRequest request = PlcCDIUtil.getInstance().getInstanceByType(HttpServletRequest.class);
		
		ResponseBuilder responseBuilder = Response.ok();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		IPlcController<Object, Object> controller = getController();
		IPlcConversor<IPlcController<Object,Object>> conversor = getConversor();
		
		// A Trata a Exceção para mensagem mais apropriada e exceção raiz, se for o caso.
		PlcException excecaoTratada = exceptionHandlerUtil.interpretExcecao(throwable);

		if (throwable instanceof PlcException) {
			excecaoTratada = (PlcException) throwable;
		}
		else{
			excecaoTratada=new PlcException(throwable);
		}
			
			
		/* 
		 * Se for exeção externa (nao controlada pela aplicação) entao coloca ainda o Stack Trace 
		 * em request para permitir exibição de detalhes para usuários 
		 */
		logStackTrace(excecaoTratada, request, exceptionHandlerUtil);

		/*
		 * Envio de Log
		 */
		exceptionHandlerUtil.handleExceptions(request, excecaoTratada);
		
		PlcMsgUtil msgUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgUtil.class, QPlcDefaultLiteral.INSTANCE);
		msgUtil.msg(excecaoTratada.getMessage(), Cor.msgVermelhoPlc.toString());
		if (conversor!=null)
			conversor.writeException(controller, outputStream, new PlcMessage(excecaoTratada.getMessage(), Cor.msgVermelhoPlc.toString()));
		return responseBuilder.entity(outputStream).build();
	}
	
	/**
	 * Se for exeção externa (nao controlada pela aplicação) entao coloca ainda o Stack Trace em request para permitir
	 * exibição de detalhes para usuários
	 * @param request
	 * @param excecaoTratada Exceçao tratada podendo ser no padrao (PlcException com excecao original na causaRaiz) ou fora dele (qualquer excecao)
	 */
	protected void logStackTrace(PlcException excecaoTratada, HttpServletRequest request, PlcExceptionHandlerUtil exceptionHandlerUtil) {

		log.debug( "########## Entrou em executeDisponibilizaStackTrace");
		try {
			Throwable t = excecaoTratada;
			if (PlcException.class.isAssignableFrom(excecaoTratada.getClass())) {
				if (((PlcException) excecaoTratada).getCausaRaiz() != null) {
					t = ((PlcException) excecaoTratada).getCausaRaiz();
					// somente disponibiliza stack trace se for exceção externa
					request.setAttribute(PlcConstants.ERRO.STACK_TRACE_MSG, exceptionHandlerUtil.stackTraceToString(t, true));
				}
			}

		} catch (Exception e) {
			log.fatal( "Erro ao tentar disponibilizar stack trace no request " + e, e);
		}
	}
}
