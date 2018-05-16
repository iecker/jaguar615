/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.webapp.ResourceServlet;

import com.powerlogic.jcompany.commons.PlcConstants;

@SuppressWarnings("serial")
public class PlcTrinidadResourceServlet extends ResourceServlet {

	private static final Logger logControle = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_CONTROLE);

	private static final int _BUFFER_SIZE = 2048;

	private boolean _debug;	


	/*
	 * Método adicionado para resolver exception 
	 * 
	 * java.util.MissingResourceException: 
	 * 				Can't find resource for bundle org.jboss.seam.core.SeamResourceBundle, key javax.faces.validator.LongRangeValidator.MINIMUM
	 * 
	 * Tentar encontrar melhor alternativa
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String resourcePath = getResourcePath(request);
		
		if (!resourcePath.contains("/jsLibs/resources/LocaleElements")){
			super.doGet(request, response);
			return;
		}
		
		URL url = Thread.currentThread().getContextClassLoader().getResource("META-INF"+ resourcePath.replace("2_0_0_beta_2", "")); 


		// Make sure the resource is available
		if (url == null)
		{
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		// Stream the resource contents to the servlet response
		URLConnection connection = url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(false);

		_setHeaders(connection, response);

		InputStream in = connection.getInputStream();
		OutputStream out = response.getOutputStream();
		byte[] buffer = new byte[_BUFFER_SIZE];

		try
		{
			_pipeBytes(in, out, buffer);
		}
		finally
		{
			try
			{
				in.close();
			}
			finally
			{
				out.close();
			}
		}


	}

	
	/*
	 * Método adicionado para resolver exception 
	 * 
	 * java.util.MissingResourceException: 
	 * 				Can't find resource for bundle org.jboss.seam.core.SeamResourceBundle, key javax.faces.validator.LongRangeValidator.MINIMUM
	 * 
	 * Tentar encontrar melhor alternativa
	 */
	
	@Override
	protected long getLastModified(HttpServletRequest request)
	{
		try
		{
			String resourcePath = getResourcePath(request);

			if (!resourcePath.contains("/jsLibs/resources/LocaleElements")){
				return super.getLastModified(request);				
			}
			
			URL url = Thread.currentThread().getContextClassLoader().getResource("META-INF"+ resourcePath.replace("2_0_0_beta_2", "")); 

			if (url == null)
				return super.getLastModified(request);

			URLConnection connection = url.openConnection();
			connection.setDoInput(false);
			connection.setDoOutput(false);

			long lastModified = connection.getLastModified();
			// Make sure the connection is closed
			try
			{
				InputStream is = connection.getInputStream();
				if (is != null)
					is.close();
			}
			catch (UnknownServiceException use)
			{
			}

			return lastModified;
		}
		catch (IOException e)
		{
			// Note: API problem with HttpServlet.getLastModified()
			//       should throw ServletException, IOException
			return super.getLastModified(request);
		}
	}


	/**
	 * Sets HTTP headers on the response which tell
	 * the browser to cache the resource indefinitely.
	 */
	private void _setHeaders(
			URLConnection       connection,
			HttpServletResponse response)
	{
		String contentType = connection.getContentType();
		if (contentType == null || "content/unknown".equals(contentType))
		{
			URL url = connection.getURL();
			String resourcePath = url.getPath();
			if(resourcePath.endsWith(".css"))
				contentType = "text/css";
			else
				contentType = getServletContext().getMimeType(resourcePath);
		}
		response.setContentType(contentType);

		int contentLength = connection.getContentLength();
		if (contentLength >= 0)
			response.setContentLength(contentLength);

		long lastModified = connection.getLastModified();
		if (lastModified >= 0)
			response.setDateHeader("Last-Modified", lastModified);

		// If we're not in debug mode, set cache headers
		if (!_debug)
		{
			// We set two headers: Cache-Control and Expires.
			// This combination lets browsers know that it is
			// okay to cache the resource indefinitely.

			// Set Cache-Control to "Public".
			response.setHeader("Cache-Control", "Public");

			// Set Expires to current time + one year.
			long currentTime = System.currentTimeMillis();

			response.setDateHeader("Expires", currentTime + ONE_YEAR_MILLIS);
		}
	}



	/**
	 * Reads the specified input stream into the provided byte array storage and
	 * writes it to the output stream.
	 */
	private static void _pipeBytes(
			InputStream in,
			OutputStream out,
			byte[] buffer
	) throws IOException
	{
		int length;

		while ((length = (in.read(buffer))) >= 0)
		{
			out.write(buffer, 0, length);
		}
	}	
}

