/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcFile;

/**
 * jCompany. Servlet para download de Imagem dinâmicas
 */
public class PlcDownloadImageServlet extends HttpServlet {

	private static final Logger logControle = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_CONTROLE);


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PlcDownloadImageServlet() {  }

	/**
	 * Pega relatório JasperPrint da sessão da variável "relatorioPlc", devolve
	 * e remove da sessão.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {

		Logger log = Logger.getLogger(getClass().getCanonicalName());
		Logger logF = Logger.getLogger("fluxo");

		logF.debug("####################Entrou no doGet do Servlet de Download");

		PlcFile arqVO;

		if (request.getSession().getAttribute(PlcConstants.PlcJsfConstantes.PLC_IMAGEM_DOWNLOAD) != null) {
			log.debug( "Achou relatorio na sessao");
			OutputStream out = response.getOutputStream();

			try {
				arqVO 			= (PlcFile) request.getSession().getAttribute(PlcConstants.PlcJsfConstantes.PLC_IMAGEM_DOWNLOAD);
				String mime 	= arqVO.getType() != null ? arqVO.getType().trim(): null;
				byte[] document = (byte[]) arqVO.getBinaryContent().getBinaryContent();
				//response.reset();
				if (mime == null || mime.equals("")) {
					mime = "application/octet-strStream";
					response.setContentType(mime);
				} else
					response.setContentType(mime);

				response.setHeader("Content-Disposition","attachment; filename=" + arqVO.getNome());
				response.setContentLength(document.length);
				out.write(document, 0, document.length);

				request.getSession().removeAttribute(PlcConstants.PlcJsfConstantes.PLC_IMAGEM_DOWNLOAD);

			} catch (IOException e) {
				log.error( "Erro fazendo download do relatorio jasper.", e);
			} finally {
				// Último comando deve ser o close
				out.flush();
				out.close();
			}
		} else {
			log.warn( "Nao encontrou o Arquivo na sessao!");
		}
	}

}