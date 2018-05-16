/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.jsf.interceptors;

import java.util.Set;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.util.PlcExceptionHandlerUtil;
import com.powerlogic.jcompany.controller.util.PlcMsgUtil;
import com.powerlogic.jcompany.domain.validation.PlcMessage;

/**
 * Tratador de Exceção para os ManagedBeans JSF.
 * Joga para a tela a exceção, no padrão jcompany.
 * É utilizada pelo {@link PlcExceptionInterceptor}.
 * @author Bruno Grossi, Igor Guimarães
 *
 */
@SPlcUtil
@QPlcDefault
public class PlcExceptionHandlerJSF {

	protected static Logger					log			= Logger.getLogger(PlcExceptionHandlerJSF.class.getCanonicalName());

	@Inject @QPlcDefault 
	protected PlcExceptionHandlerUtil exceptionHandlerUtil;
	
	@Inject @QPlcDefault 
	protected PlcContextUtil contextUtil;
	

	/**
	 * @since jComapny 5.0
	 * Desolve o request a partir do PplcContextHelper
	 */
	protected HttpServletRequest getRequest() {

		try {
			return contextUtil.getRequest();
		} catch (Exception e) {
			log.fatal( "Erro nao tratavel no tratamento de excecao. Erro: " + e, e);
			return null;
		}
	}

	/**
	 * jCompany 3.0 Método disparado para inicio de tratamento de exceções.
	 */
	public void handle(Exception excecaoInvolucro) throws ServletException {

		if (log.isDebugEnabled()) {
			log.debug( "########## jCompany: Entrou no tratamento de erro com erro " + excecaoInvolucro);
		}

		// A Trata a Exceção para mensagem mais apropriada e exceção raiz, se for o caso.
		PlcException excecaoTratada = interpretExcecao(excecaoInvolucro);

		if (excecaoTratada == null) {
			excecaoTratada = (PlcException) excecaoInvolucro;
		}

		/* 
		 * Se for exeção externa (nao controlada pela aplicação) entao coloca ainda o Stack Trace 
		 * em request para permitir exibição de detalhes para usuários 
		 */
		disponibilizaStackTrace(excecaoTratada);

		HttpServletRequest request = getRequest();
		/*
		 * Envio de Log
		 */
		exceptionHandlerUtil.handleExceptions(request, excecaoTratada);

		try {
			PlcMsgUtil msgUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgUtil.class, QPlcDefaultLiteral.INSTANCE);

			if (PlcBeanMessages.JCOMPANY_ERROR_BEAN_VALIDATION.toString().equals(excecaoTratada.getMessageError())) {
				msgUtil.availableInvariantMessages((Set<ConstraintViolation<Object>>) excecaoTratada.getObjectError(), null);
			} else {
				msgUtil.createMessageVariant(excecaoTratada, null, PlcMessage.Cor.msgVermelhoPlc.toString());
			}	

		} catch (Exception e) {
			log.info( "Exception tratada:", e);
		}

	}

	/**
	 * @since jCompany 5.0
	 * Se for exeção externa (nao controlada pela aplicação) entao coloca ainda o Stack Trace em request para permitir
	 * exibição de detalhes para usuários
	 * @param request
	 * @param excecaoTratada Exceçao tratada podendo ser no padrao (PlcException com excecao original na causaRaiz) ou fora dele (qualquer excecao)
	 */
	protected void disponibilizaStackTrace(PlcException excecaoTratada) {
		
		log.debug( "########## Entrou em executeDisponibilizaStackTrace");
		try {
			Throwable t = excecaoTratada;
			if (!PlcException.class.isAssignableFrom(excecaoTratada.getClass())) {
				if (((PlcException) excecaoTratada).getCausaRaiz() != null) {
					t = ((PlcException) excecaoTratada).getCausaRaiz();
					// somente disponibiliza stack trace se for exceção externa
					getRequest().setAttribute(PlcConstants.ERRO.STACK_TRACE_MSG, exceptionHandlerUtil.stackTraceToString(t, true));
				}
			}

		} catch (Exception e) {
			log.fatal( "Erro ao tentar disponibilizar stack trace no request " + e, e);
		}
	}

	/**
	 * @since jCompany 5.0
	 * Recebe um objeto do tipo Exception e chama tratamento, conforme seu tipo
	 * Delega a interpretação para outra classe desacoplada da Struts
	 * @return Exceção contendo mensagem e exceção original alteradas em conformidade com o tratamento
	 */
	protected PlcException interpretExcecao(Throwable ex) {

		return exceptionHandlerUtil.interpretExcecao(ex);
	}

}
