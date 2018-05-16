/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompanyqa.controller.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.controller.util.PlcI18nUtil;

public class PlcI18nUtilMock extends PlcI18nUtil {

	public String mountLocalizedMessage(String nomeBundle, Locale locale, String key, Object[] args) {

		try {

			if (key.startsWith("#"))
				return key.substring(1);

			ResourceBundle messages = ResourceBundle.getBundle(nomeBundle, locale);

			String msgTratada = messages.getString(key);

			if (args != null) {
				for (int i = 0; i < args.length; i++) {

					if (args[i] != null)
						msgTratada = StringUtils.replaceOnce(msgTratada, "{" + i + "}", args[i].toString());

				}
			}

			//return msgTratada;
			return key;

		} catch (MissingResourceException e) {
			// Deve voltar null para facilitar busca para vários Bundles
			// pelo chamador
			return null;
		}

	}
	
	
	@Override
	public String mountLocalizedMessage(String nomeBundle, String key, String[] parametros) {
		return key;
	}
	


}
