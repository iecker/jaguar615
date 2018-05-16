/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.util;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.template.PlcRiaJavaScriptUtil;

/**
 * Classe que auxiliar para os renderes  
 *
 */

@QPlcDefault
public class PlcRendererUtil {

	protected static final Logger	logVisao		= Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	/**
	 * Faz o processamento e inclusão de um template unobtrusive de javascript.
	 * @param id ID do componente.
	 * @param defaultRiaParameters
	 * @param defaultRia
	 * @param customRia
	 * @param writer writer onde serão escrito o resultado do processamento do template.
	 * @return 
	 * @throws Exception Caso ocorra algum erro durante a inclusão do template.
	 * @see PlcRiaJavaScriptUtil
	 */
	public static boolean includeRiaTemplates(String id, String defaultRiaParameters, String defaultRia, String customRia, Writer writer) throws Exception {

		if ("N".equalsIgnoreCase(customRia)) {
			return false;
		}

		if (StringUtils.isNotBlank(customRia)) {
			includeRiaTemplate(id, defaultRiaParameters, customRia, writer);
			return true;
		} else	if (StringUtils.isNotBlank(defaultRia)) {
			includeRiaTemplate(id, defaultRiaParameters, defaultRia, writer);
			return true;
		} else
			return false;
	}

	private static void includeRiaTemplate(String id, String defaultRiaParameters, String defaultRia, Writer writer) throws IOException {

		PlcRiaJavaScriptUtil riaJavaScriptUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcRiaJavaScriptUtil.class, QPlcDefaultLiteral.INSTANCE);

		writer.write("\n<script type=\"text/javascript\" id=\"avaliar:" + id + ":" + System.nanoTime() + "\">\n");

		riaJavaScriptUtil.include(defaultRiaParameters, defaultRia, writer);

		writer.write("\n</script>\n");
	}
}
