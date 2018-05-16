/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.powerlogic.jcompanyqa.controller.mock.ServletContextMock;



//  StrutsTestCase - a JUnit extension for testing Struts actions
//  within the context of the ActionServlet.
//  Copyright (C) 2002 Deryl Seale
//
//  This library is free software; you can redistribute it and/or
//  modify it under the terms of the Apache Software License as
//  published by the Apache Software Foundation; either version 1.1
//  of the License, or (at your option) any later version.
//
//  This library is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  Apache Software Foundation Licens for more details.
//
//  You may view the full text here: http://www.apache.org/LICENSE.txt

@RequestScoped
public class HttpServletRequestMock implements HttpServletRequest {

	private Hashtable attributes = new Hashtable();
	private Hashtable parameters = new Hashtable();
	
    private HttpSession session;
    private ServletContext context;
    
	@Override
	public String getAuthType() {

		return null;
	}

	@Override
	public String getContextPath() {

		return null;
	}

	@Override
	public Cookie[] getCookies() {

		return null;
	}

	@Override
	public long getDateHeader(String name) {

		return 0;
	}

	@Override
	public String getHeader(String name) {

		return null;
	}

	@Override
	public Enumeration getHeaderNames() {

		return null;
	}

	@Override
	public Enumeration getHeaders(String name) {

		return null;
	}

	@Override
	public int getIntHeader(String name) {

		return 0;
	}

	@Override
	public String getMethod() {

		return null;
	}

	@Override
	public String getPathInfo() {

		return null;
	}

	@Override
	public String getPathTranslated() {

		return null;
	}

	@Override
	public String getQueryString() {

		return null;
	}

	@Override
	public String getRemoteUser() {

		return null;
	}

	@Override
	public String getRequestURI() {

		return null;
	}

	@Override
	public StringBuffer getRequestURL() {

		return null;
	}

	@Override
	public String getRequestedSessionId() {

		return null;
	}

	@Override
	public String getServletPath() {

		return null;
	}

	@Override
	public HttpSession getSession() {
		return getSession(true);
	}

	@Override
	public HttpSession getSession(boolean create) {
		if (context==null)
			context = new ServletContextMock();
        if ((session == null) && (create))
            this.session = new HttpSessionMock(context);
        else if ((session != null) && (!((HttpSessionMock) session).isValid()) && (create))
            this.session = new HttpSessionMock(context);
        if ((session != null) && (((HttpSessionMock) session).isValid()))
            return this.session;
        else
            return null;
	}

	@Override
	public Principal getUserPrincipal() {

		return null;
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {

		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {

		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {

		return false;
	}

	@Override
	public boolean isRequestedSessionIdValid() {

		return false;
	}

	@Override
	public boolean isUserInRole(String role) {

		return false;
	}


	@Override
	public Enumeration getAttributeNames() {

		return null;
	}

	@Override
	public String getCharacterEncoding() {

		return null;
	}

	@Override
	public int getContentLength() {

		return 0;
	}

	@Override
	public String getContentType() {

		return null;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {

		return null;
	}

	@Override
	public String getLocalAddr() {

		return null;
	}

	@Override
	public String getLocalName() {

		return null;
	}

	@Override
	public int getLocalPort() {

		return 0;
	}

	@Override
	public Locale getLocale() {
		return Locale.getDefault();
	}

	@Override
	public Enumeration getLocales() {
		
		return null;
	}

    public String getParameter( String s )
    {
        if (s == null)
            return null;

        Object param = parameters.get(s);
        if( null == param )
            return null;
        if( param.getClass().isArray() )
            return ((String[]) param)[0];
        return (String)param;
    }

	@Override
    public Map getParameterMap() {
        return this.parameters;
    }

    public Enumeration getParameterNames()
    {
        return parameters.keys();
    }

	@Override
	public String[] getParameterValues(String name) {

		return null;
	}

	@Override
	public String getProtocol() {

		return null;
	}

	@Override
	public BufferedReader getReader() throws IOException {

		return null;
	}

	@Override
	public String getRealPath(String path) {

		return null;
	}

	@Override
	public String getRemoteAddr() {

		return null;
	}

	@Override
	public String getRemoteHost() {

		return null;
	}

	@Override
	public int getRemotePort() {

		return 0;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {

		return null;
	}

	@Override
	public String getScheme() {

		return null;
	}

	@Override
	public String getServerName() {

		return null;
	}

	@Override
	public int getServerPort() {

		return 0;
	}

	@Override
	public boolean isSecure() {

		return false;
	}

	@Override
	public void removeAttribute(String name) {

		
	}


	@Override
	public void setCharacterEncoding(String env)
			throws UnsupportedEncodingException {

		
	}


    /**
    *
    * Returns the value of the named attribute as an <code>Object</code>,
    * or <code>null</code> if no attribute of the given name exists.
    *
    * <p> Attributes can be set two ways.  The servlet container may set
    * attributes to make available custom information about a request.
    * For example, for requests made using HTTPS, the attribute
    * <code>javax.servlet.request.X509Certificate</code> can be used to
    * retrieve information on the certificate of the client.  Attributes
    * can also be set programatically using
    * {@link ServletRequest#setAttribute}.  This allows information to be
    * embedded into a request before a {@link RequestDispatcher} call.
    *
    * <p>Attribute names should follow the same conventions as package
    * names. This specification reserves names matching <code>java.*</code>,
    * <code>javax.*</code>, and <code>sun.*</code>.
    *
    * @param s	a <code>String</code> specifying the name of
    *			the attribute
    *
    * @return		an <code>Object</code> containing the value
    *			of the attribute, or <code>null</code> if
    *			the attribute does not exist
    *
    */
   public Object getAttribute(String s)
   {
       return attributes.get(s);
   }
   
   /**
   *
   * Stores an attribute in this request.
   * Attributes are reset between requests.  This method is most
   * often used in conjunction with {@link RequestDispatcher}.
   *
   * <p>Attribute names should follow the same conventions as
   * package names. Names beginning with <code>java.*</code>,
   * <code>javax.*</code>, and <code>com.sun.*</code>, are
   * reserved for use by Sun Microsystems.
   *<br> If the value passed in is null, the effect is the same as
   * calling {@link #removeAttribute}.
   *
   *
   *
   * @param name			a <code>String</code> specifying
   *					the name of the attribute
   *
   * @param o				the <code>Object</code> to be stored
   *
   */
  public void setAttribute(String name, Object o)
  {
      if (o == null)
          attributes.remove(name);
      else
          attributes.put(name, o);
  }

  public void setParameter(String name, String o)
  {
      if (o == null)
    	  parameters.remove(name);
      else
    	  parameters.put(name, o);
  }
  
  
}
