/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompanyqa.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.domain.validation.PlcMessage;
import com.powerlogic.jcompanyqa.PlcBaseTestCase;
import com.powerlogic.jcompanyqa.commons.mock.PlcBaseContextMock;
import com.powerlogic.jcompanyqa.controller.mock.HttpServletRequestMock;
import com.powerlogic.jcompanyqa.controller.mock.HttpServletResponseMock;
import com.powerlogic.jcompanyqa.controller.mock.HttpSessionMock;
import com.powerlogic.jcompanyqa.controller.mock.ServletContextMock;

/**
 * jCompany 2.7. Classe base para testes de classes DAO
 * 
 * @since jCompany 2.7.3
 */
public class PlcBaseMBTestCase extends PlcBaseTestCase {

    private ServletContextMock servletContext = new ServletContextMock();

    protected HttpServletRequestMock requestMock = new HttpServletRequestMock(servletContext);

    protected HttpServletResponseMock responseMock = new HttpServletResponseMock();

    protected PlcBaseContextVO contextMock = new PlcBaseContextMock();

    protected HttpSession sessionMock = new HttpSessionMock(servletContext);

    {
	requestMock.setLocale(new Locale("", ""));
    }

    @Override
    protected void setUp() throws Exception { 
    	super.setUp();
    }

    /**
         * jCompany 2.7. Verifica se existe mensagem no request, no padrão do
         * Struts.
         * 
         * @since jCompany 2.7.3
         * @return Se houver, devolve true, senão devolve false
         */
    protected boolean existeMensagemNoRequest(HttpServletRequestMock requestMock, String msg) {

	String[] cores = new String[] { 
				PlcMessage.Cor.msgAzulPlc.toString(), 
				PlcMessage.Cor.msgVerdePlc.toString(), 
				PlcMessage.Cor.msgVermelhoPlc.toString(), 
				PlcMessage.Cor.msgAmareloPlc.toString()
			};

	boolean retorno = false;

	for (String cor : cores) {
	    List l = (List) requestMock.getAttribute(cor);
	    if ( l != null) {
		for (int i = 0; i < l.size(); i++) {
		    PlcMessage mensagem = (PlcMessage)l.get(i);
		    if (mensagem.getMensagem().equals(msg))
			return true;
		}
	    }
		
	}
	return retorno;

	/*
         * 
         * ActionMessages messages; if
         * (requestMock.getAttribute(Globals.MESSAGE_KEY) == null) return false;
         * 
         * messages = (ActionMessages) requestMock
         * .getAttribute(Globals.MESSAGE_KEY);
         * 
         * Iterator mensagens = messages.get(); while (mensagens.hasNext()) {
         * ActionMessage am = (ActionMessage) mensagens.next(); if
         * (am.getKey().equals(msg)) return true; }
         */

    }

    /**
         * jCompany 2.7 Reinicializa o Mock para Request
         * 
         * @since jCompany 2.7.3
         */
    protected void limpaRequest() {
	requestMock = new HttpServletRequestMock(servletContext);
    }

    /**
         * jCompany 2.7 Reinicializa o Mock para Response
         * 
         * @since jCompany 2.7.3
         */
    protected void limpaResponse() {
	responseMock = new HttpServletResponseMock();
    }


}
