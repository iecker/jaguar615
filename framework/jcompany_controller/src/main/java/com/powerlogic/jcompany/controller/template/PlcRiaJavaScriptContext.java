/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.template;

import org.apache.commons.lang.StringUtils;

public class PlcRiaJavaScriptContext extends PlcTemplateContext {

	public PlcRiaJavaScriptContext(String templateName) {
		super(templateName);
	}

	/**
	 * Espera uma string de parametros no formato {@code "param1=valor1,...,paramN=valorN"}
	 * Faz o parser dos parametros é injeta no contexto.
	 * @param parameters String de parametros.
	 */
	public void putParameters(String parameters) {
		final String paramSpliter = "\\s*,\\s*";
		final String nameAndValueSpliter = "\\s*=\\s*";
		if (!StringUtils.isEmpty(parameters)) {
			String[] splitedParameter = parameters.trim().split(paramSpliter);
			for (String parameter : splitedParameter) {
				String[] nameAndValue = parameter.split(nameAndValueSpliter);
				if (nameAndValue.length == 2) {
					put(nameAndValue[0], unquote(nameAndValue[1]));
				}
			}
		}
	}

	private String unquote(String value) {
		if (value != null && value.length() > 1) {
			int lastIndex = value.length() - 1;
			if (value.charAt(0) == '\'' && value.charAt(lastIndex) == '\'') {
				return value.substring(1, lastIndex);
			}
			if (value.charAt(0) == '\"' && value.charAt(lastIndex) == '\"') {
				return value.substring(1, lastIndex);
			}
		}
		return value;
	}
}
