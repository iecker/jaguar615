/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcFile;

/**
 * jCompany. Servlet para download de relatório dinâmicos utilizando
 * JasperReports. Este servlet é acesso via redirects globais, pegando o
 * relatório criado pela engine Jasper da sessão e removendo-o da sessão em
 * seguida.
 * 
 * @since jCompany 1.5
 * @author alvim, roberto badaró (refactoring para incluir a opção de download
 *         de xls)
 */
public class PlcDownloadServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7687373660464970961L;
	
	@Inject
	protected transient Logger log;
	
	/**
     * Mime types que o download servlet trabalha.<br>
     * A chave é o valor de voArg.getTipo e o conteúdo o mime type correspondente.
     */
    private Map mimeType = new HashMap();
    {
        // Popula os mime types. Esse código é executado quando a classe é instanciada.
        mimeType.put("P", "application/pdf");
        mimeType.put("H", "application/html");
        mimeType.put("X", "application/html");
        mimeType.put("E", "application/xls");
        mimeType.put("C", "application/xls"); // CSV
    }

    public PlcDownloadServlet() {  }

	 /**
     * Pega relatório JasperPrint da sessão da variável "relatorioPlc", devolve
     * e remove da sessão.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Logger logF = Logger.getLogger("fluxo");
        
        logF.debug( "####################Entrou no doGet do Servlet de Download");

        PlcFile arqVO;

        byte report[] = {};

        if (request.getSession().getAttribute("relatorioPlc") != null) {
            log.debug( "Achou relatorio na sessao");
            OutputStream out = response.getOutputStream();

            try {
                arqVO = (PlcFile) request.getSession().getAttribute("relatorioPlc");
                report = (byte[]) arqVO.getObjectAux();

                String formato = arqVO.getType();

                if (log.isDebugEnabled())
                    log.debug( "Achou relatorio com formato=" + formato
                            + " e vai devolver como octet");

                if (!formato.equals("L")) {
                    String contentType = (String) mimeType.get(formato);

                    if (contentType != null) {
                        log.debug( "Formato escolhido foi " + contentType);

                        response.reset();
//                        response.setHeader("Pragma", "No-cache");
//                        response.setHeader("Cache-Control", "no-cache,no-store,max-age=0");
//                        response.setDateHeader("Expires", 1);
                        response.setBufferSize(1024 * 1024);
                        response.setContentLength(report.length);
                        response.setContentType(contentType);

                        out.write(report, 0, report.length);
                    }
                }

                request.getSession().removeAttribute("relatorioPlc");

                log.debug( "Removeu relatorio da sessao");

            } catch (IOException e) {
                log.error( "Erro fazendo download do relatorio jasper.", e);
            } finally {
                // Último comando deve ser o close
                out.flush();
                out.close();
            }
        } else {
            log.warn( "Nao encontrou o relatorio na sessao!");
        }
    }

}