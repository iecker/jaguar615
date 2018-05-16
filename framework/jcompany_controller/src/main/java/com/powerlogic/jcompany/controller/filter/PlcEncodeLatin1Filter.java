/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.filter;
/**
 * jCompany. Filtro para Login
 *    Realiza lógicas de profiling após a autenticação do usuário.
 */
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcConstants;

/**
 * jCompany 3.0 Registra o enconding "latin1" no request, para as URLs
 * mapeadas no filtro. O default do jCompany é utilizar "utf-8".
 * @since jCompany 3.0
*/
public class PlcEncodeLatin1Filter   implements Filter {
	
	protected static final Logger log = Logger.getLogger(PlcEncodeLatin1Filter.class.getCanonicalName());
	protected static final Logger logProfiling = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_QA_PROFILING);
	
	//private FilterConfig filterConfig = null;
	private ServletContext servletContext = null;
	
    public PlcEncodeLatin1Filter() {

    }

    public void init( FilterConfig config )
            throws ServletException {
		
		this.servletContext = config.getServletContext();
		
    }

    public void destroy(){
        /** @todo: implement this function */
    }

	/**
	 * Este filtro se fez necessário no Tomcat 5 para resolver problemas de acentuação nos POSTs de
	 * dados.
	 */
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain filterChain )
            throws IOException, ServletException 
    {

		request.setCharacterEncoding("latin1");
	   	
		filterChain.doFilter(request,response);

    }
	
	protected ServletContext getServletContext () {
		return this.servletContext;
	}
		
}

