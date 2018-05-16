/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.birtviewer.wrapper;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class PlcHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private String nomeFile;
    public PlcHttpServletRequestWrapper(HttpServletRequest request, String nomeFile) {
	super(request);
	this.nomeFile = nomeFile;
    }

    @Override
    public String getParameter(String parametro) {
	if (parametro.equals("__report"))
	    return nomeFile;
	else
	    return super.getParameter(parametro);
    }

    @Override
    public Map getParameterMap() {
	// TODO Auto-generated method stub
	return super.getParameterMap();
    }

    @Override
    public Enumeration getParameterNames() {
	// TODO Auto-generated method stub
	return super.getParameterNames();
    }

    @Override
    public String[] getParameterValues(String parametro) {
	if (parametro.equals("__report"))
	    return new String[]{nomeFile};
	else
	    return super.getParameterValues(parametro);
    }
    
}
