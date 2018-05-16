/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.rest.conversors.jaxb.atom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;

/**
 * Encoder e decoder de datas para os padrões rfc822 e rfc3339.
 * @see http://tools.ietf.org/html/rfc822
 * @see http://tools.ietf.org/html/rfc3339
 */
@SPlcUtil
@QPlcDefault
public class PlcConvertRFCUtil {

	private static SimpleDateFormat sdf822 = new SimpleDateFormat("EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'Z", Locale.US);
	
	private static SimpleDateFormat sdf3339 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'-03:00'");
	
	public PlcConvertRFCUtil() {
		
	}

	// Formata utilizando a data atual
	public static String encode822() {
		return sdf822.format(new Date()).toString();
	}

	// Formata utilizando a data do parâmetro
	public static String encode822(Date data) {
		return sdf822.format(data).toString();
	}

	// Formata utilizando a data atual
	public static String encode3339() {
		return sdf3339.format(new Date()).toString();
	}

	// Formata utilizando a data do parâmetro
	public static String encode3339(Date data) {
		return sdf3339.format(data).toString();
	}
	
	// Formata utilizando a data do parâmetro
	public static Date decode3339(String data) {
		Date ret = null;
		try {
			ret = sdf3339.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ret;
	}

}
