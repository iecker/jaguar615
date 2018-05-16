package com.powerlogic.jcompany.controller.appinfo;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;


/**
 * 
 * @author Roberto Badaró
 * @version $Id: PlcJSecurityAppInfoProvider.java 15802 2009-04-15 14:02:56Z moises_paula $
 */
public class PlcAppInfoProvider {

	/**
	 * Escreve no response um xml com as informações da aplicação.
	 * 
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	
	public static void informaRecursos(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		PlcAppInfoUtil appInfoUtil  = PlcCDIUtil.getInstance().getInstanceByType(PlcAppInfoUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		PlcAppInfo info = appInfoUtil.getAppInfo();
		
		StringBuilder bf = new StringBuilder("<app-info>\n");
		
		if (info != null) {
			bf.append("<aplicacao sigla=\"")
			  .append(info.getApplicationAcronym())
			  .append("\" nome=\"")
			  .append(info.getApplicationName())
			  .append("\"/>\n")
			  .append("<recursos>\n");

			List<PlcAppMBInfo> l = info.getActions();
			if (l != null && !l.isEmpty()) {
				int t = l.size();
				for (int i=0; i<t; i++) {
					PlcAppMBInfo act = l.get(i);
					bf.append("<recurso nome=\"")
					  .append(act.getFullPath())
					  .append("\"");
					
					if (act.getDescription() != null) {
						bf.append(" descricao=\"")
						  .append(act.getDescription().trim().replace("\"", "'"))
						  .append("\"");
					}
					bf.append("/>\n");
				}
			}
			
			bf.append("</recursos>\n");
			
			List<String> filterDefs = info.getFilterDefs();
			if (filterDefs != null && !filterDefs.isEmpty()){
				bf.append("<filterDefinitions>\n");
				
				for (String filterDef : filterDefs) {
					bf.append(filterDef).append("\n");
				}
				
				bf.append("</filterDefinitions>\n");
			}
			
		}
		
		bf.append("</app-info>");
		
		// Guarda o charsetEmUso para reconfiguracao apos reset!
		String characterEncodingEmUso = response.getCharacterEncoding();
		response.reset();
		response.setCharacterEncoding(characterEncodingEmUso);
		response.setContentType("text/html");
		response.getWriter().print(bf.toString());
	}
}
