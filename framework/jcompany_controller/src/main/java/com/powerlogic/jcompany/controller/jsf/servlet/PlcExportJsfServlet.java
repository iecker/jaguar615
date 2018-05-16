/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.config.collaboration.PlcConfigExport;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcExportUtil;
/**
 * Recupera da conversação a lista de registros a ser Exportada.
 * Delega exportação de dados para o Serviço de Exportação.
 * Verifica se foi configurado a exportação dos registros,
 * se verdadeiro coloca no request a lista de Entidades e os campos configurados.
 */

public class PlcExportJsfServlet extends HttpServlet {
	Logger log = Logger.getLogger(getClass().getCanonicalName());
	
	private static final long serialVersionUID = 1L;

	/**
	 * Serviço injetado e gerenciado pelo CDI
	 */
	@Inject @QPlcDefault 
	protected PlcConfigUtil configUtil;
	
	@Inject @QPlcDefault 
	protected PlcExportUtil exportacaoUtil;

	@Override
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String action = request.getParameter("action");
			PlcConfigExport configExportacao = null;
			if (configUtil.getConfigCollaboration(action).selection()!=null)
				configExportacao = configUtil.getConfigCollaboration(action).selection().export();

			if ( configExportacao != null ){
				String conversationIdPlc 				= (String)request.getParameter(PlcJsfConstantes.CONVERSATION_ID_PLC);
//				PlcBaseLogicaArgumento logicaArgumento 	= conversationUtil.getObjectInConversation(conversationIdPlc, PlcJsfConstantes.PLC_LOGICA_ITENS, request);
//				
//				if ( logicaArgumento != null ){
//					String campos = getCampos(configExportacao);
//					request.setAttribute(PlcConstantes.LOGICAPADRAO.CONSULTA.EXPORTACAO.EXPORTACAO_CAMPOS, campos);
//					request.setAttribute(PlcConstantes.LOGICAPADRAO.CONSULTA.EXPORTACAO.EXPORTACAO_ITENS_PLC,logicaArgumento.getItensPlc());
//					exportacaoUtil.exporta(request, response);
//				}
//				else
//					log.info( "Não encontrou PLC_LOGICA_ITENS na conversação: " +  conversationIdPlc);

			}
			else
				log.info( "Não encontrou anotação de Exportação '@PlcConfigExportacao' para o caso de uso: " +  action);
		} catch (Exception e) {

			e.printStackTrace();
		}


	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	
	protected String getCampos(PlcConfigExport configExportacao ){
		
		String campos = "";
		int qtdCampos = configExportacao.fields().length;
		for(int i=0; i < qtdCampos; i++){
			if (i == 0)
				campos = campos + configExportacao.fields()[i];
			else
				campos = campos + "," + configExportacao.fields()[i];
		}
		return campos;
	}

}
