/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.template;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;

/**
 * Localiza
 * @author Adolfo Jr.
 */
@SPlcUtil
@QPlcDefault
public class PlcRiaJavaScriptLocator implements PlcTemplateLocator {

	private static final String TEMPLATE_EXT = ".js";

	private static final String TEMPLATE_PATH = "/res-plc/javascript/un/";

	private ServletContext servletContext;

	public void setServletContext(ServletContext servletContext){
		this.servletContext = servletContext;
	}

	public PlcTemplateSource getTemplateSource(String templateName)  { // Tenta recuperar o arquivo absoluto.
		PlcTemplateSource template = getTemplateFile(templateName);
		if (template == null) { // O arquivo pode estar empacotado em um WRA, tenta pegar o recurso.
			template = getTemplateResource(templateName);
			if (template == null) {
				throw new PlcException(new FileNotFoundException("template (" + templateName + ") não foi encontrado."));
			}
		}
		return template;
	}

	protected PlcFileTemplateSource getTemplateFile(String templateName) {
		String realPath = servletContext.getRealPath(TEMPLATE_PATH);
		if (realPath != null) {
			File templateFile = new File(realPath, templateName.concat(TEMPLATE_EXT));
			if (templateFile.exists()) {
				return new PlcFileTemplateSource(templateName, templateFile);
			}
		}
		return null;
	}

	protected PlcStringTemplateSource getTemplateResource(String templateName)  {
		String resourceName = TEMPLATE_PATH + templateName + TEMPLATE_EXT;
		InputStream inputStream = servletContext.getResourceAsStream(resourceName);
		if (inputStream != null) {
			try {
				return new PlcStringTemplateSource(templateName, IOUtils.toString(inputStream));
			} catch (IOException e) {
				throw new PlcException(e);
			} finally {
				IOUtils.closeQuietly(inputStream);
			}
		}
		return null;
	}
}
