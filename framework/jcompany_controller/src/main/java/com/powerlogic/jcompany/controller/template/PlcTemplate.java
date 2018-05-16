/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.template;

import java.io.Writer;

/**
 * @author Adolfo Jr.
 */
public class PlcTemplate {

	private String templateName;

	private PlcTemplateLocator templateLocator;

	private PlcTemplateProcessor templateProcessor;

	private PlcTemplateSource templateSource;

	public PlcTemplate(String templateName, PlcTemplateLocator templateLocator, PlcTemplateProcessor templateProcessor) {
		this.templateName = templateName;
		this.templateLocator = templateLocator;
		this.templateProcessor = templateProcessor;
	}

	public String getTemplateName() {
		return templateName;
	}

	protected PlcTemplateLocator getTemplateLocator() {
		return templateLocator;
	}

	protected PlcTemplateProcessor getTemplateProcessor() {
		return templateProcessor;
	}

	public void include(PlcTemplateContext context, Writer writer)  {
		getTemplateProcessor().include(getTemplateSource(), context, writer);
	}

	protected PlcTemplateSource getTemplateSource()  {
		if (templateSource == null) {
			templateSource = getTemplateLocator().getTemplateSource(templateName);
		}
		return templateSource;
	}
}
