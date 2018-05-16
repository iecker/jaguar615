/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.birtviewer.filter;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.powerlogic.birtviewer.wrapper.PlcHttpServletRequestWrapper;

public class PlcBirtViewerSegFilter implements Filter {

	@SuppressWarnings("unused")
	private ServletContext servletContext = null;

	public void init(FilterConfig config) throws ServletException {

		this.servletContext = config.getServletContext();

	}

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
	ServletException {

		out("PlcBirtViewerSegFilter: doFilter");
		String report = request.getParameter("__report");
		
		if (report != null) {
			if (!verificaSegurancaRelatorio((HttpServletRequest) request, report)) {
				((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN);
				response.flushBuffer();
				return;
			}
		}

		try {
			HttpServletRequest requestWrapper = trataReportEmWar((HttpServletRequest) request);
			filterChain.doFilter(requestWrapper, response);

		} catch (Exception e) {
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			response.flushBuffer();
			return;
		}
	}

	private HttpServletRequest trataReportEmWar(HttpServletRequest request) throws Exception {

		String raiz = servletContext.getRealPath("");
		String file = request.getParameter("__report");
		String caminho1 = file;
		String caminho2 = raiz + "/" + file;
		
		if (!(new File(caminho1)).exists() && !(new File(caminho2)).exists()) {

			String app = StringUtils.substringBetween(file, "../", "/");

			if (app != null) {

				ServletContext appSC = servletContext.getContext("/" + app);
				if (appSC != null) {

					URL fileURL = appSC.getResource(StringUtils.substringAfter(file, "../" + app));
					if (fileURL != null) {
						String nomeFile = "rels/" + StringUtils.substringAfter(file, "../");
						FileUtils.copyURLToFile(fileURL, new File(raiz + "/" + nomeFile));
						return new PlcHttpServletRequestWrapper(request, nomeFile);
					}
				}
			} 	
			throw new Exception("Arquivo de relatório não encontrado");
		}
		return request;

	}

	private boolean verificaSegurancaRelatorio(HttpServletRequest request, String report) {

		if (!report.startsWith("../")) {

			if (report.startsWith("./"))
				report = report.substring(2);
			else if (report.startsWith("/"))
				report = report.substring(1);

			String[] args = StringUtils.split(report, "/");

			String rel = args[args.length - 1];
			String dir = null;
			if (args.length > 1)
				dir = args[args.length - 2];

			out(rel);
			out(dir);

			if (dir!=null && dir.equals("rel")) {
				out("PlcBirtViewerSegFilter: Liberado");
				return true;
			}

			String role = null;

			if (rel != null)
				role = StringUtils.substringBefore(rel, ".rptdesign").toUpperCase();

			if (role != null) {
				out("PlcBirtViewerSegFilter: role1:" + role + " " + request.isUserInRole(role));
				if (request.isUserInRole(role))
					return true;
				role = null;
			}

			if (dir != null)
				role = dir.toUpperCase();

			if (role != null) {
				out("PlcBirtViewerSegFilter: role2:" + role + " " + request.isUserInRole(role));

				boolean ecpReports = (request.getParameter("ecpreports") != null && request.getParameter("ecpreports").equalsIgnoreCase("s"));
				if(ecpReports && "TEMP".equals(role)) {
					role = "ECR_VIEW";
				}
				
				if (request.isUserInRole(role))
					return true;
			}
			
		} else {

			String[] args = StringUtils.split(report, "/");

			// se for menor que 3 não possui ../app/rel
			if (args.length < 3)
				return true;

			String app = args[1];
			String rel = args[args.length - 1];
			String dir = null;

			// se for maior que 3 possui ../app/dir/relatorio
			if (args.length > 3)
				dir = args[args.length - 2];

			// diretorio liberado - ../app/rel/relatorio
			if (args.length == 4 && args[2].equals("rel")) {
				out("PlcBirtViewerSegFilter: Liberado");
				return true;
			}

			String role = null;

			if (app != null && rel != null)
				role = app.toUpperCase() + "_" + StringUtils.substringBefore(rel, ".rptdesign").toUpperCase();

			if (role != null) {
				out("PlcBirtViewerSegFilter: role1:" + role + " " + request.isUserInRole(role));
				if (request.isUserInRole(role))
					return true;
				role = null;
			}

			if (app != null && dir != null)
				role = app.toUpperCase() + "_" + dir.toUpperCase();

			if (role != null) {
				out("PlcBirtViewerSegFilter: role2:" + role + " " + request.isUserInRole(role));
				if (request.isUserInRole(role))
					return true;
			}

		}
		return false;
	}

	private void out(String saida) {
//		System.out.println(saida);	
	}
	
}
