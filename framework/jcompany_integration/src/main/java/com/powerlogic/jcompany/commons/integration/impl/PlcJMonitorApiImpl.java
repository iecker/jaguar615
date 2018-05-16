/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.integration.impl;

import java.util.List;

import com.powerlogic.jcompany.commons.integration.IPlcJMonitorApi;
import com.powerlogic.jcompany.commons.integration.jmonitor.PlcClientDTO;

public class PlcJMonitorApiImpl implements IPlcJMonitorApi {

	@Override
	public boolean inicia(String ignorar, boolean enviarQueryString,
			String enderecoJMS, String enderecoSMTP, String emailRemetente,
			String emailFatal, String emailError, String modoExecucao,
			boolean indPseudoProducao, String nomeAplicacao,
			String siglaAplicacao, String uriHTMLBaseEmail,
			String nomeServidorAplicacao, String ipServidorAplicacao) {
		return true;
	}

	@Override
	public void iniciaMonitoriaAtivaAplicacao() {
	
	}

	@Override
	public void log(String tipo, NIVEL_LOG nivelLog, String mensagem) {
		
	}

	@Override
	public boolean logRequisicao(String requestURI, String contextPath,
			String queryString, String acao, String evento,
			PlcClientDTO clienteDTO, List listaMsgErro, Object entidade,
			List args) {
		return false;
	}

	@Override
	public void logSessao(String idSessao, CICLO_VIDA cicloVida) {
		
	}

}
