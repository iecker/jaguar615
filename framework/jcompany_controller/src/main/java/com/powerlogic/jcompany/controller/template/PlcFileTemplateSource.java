/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.template;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * @author Adolfo Jr.
 */
public class PlcFileTemplateSource extends PlcTemplateSource {

	private File file;

	private long lastModified;

	private String source;

	public PlcFileTemplateSource(String name, File file) {
		super(name);
		this.file = file;
	}

	public CharSequence getContent() throws IOException {
		if (!isValidSource()) {
			lastModified = this.file.lastModified();
			source = FileUtils.readFileToString(file, null);
		}
		return source;
	}

	public boolean isValidSource() {
		if (source == null) {
			return false;
		}
		// verifica ultima modificação.
		if (file.lastModified() > lastModified) {
			return false;
		}
		return true;
	}
}
