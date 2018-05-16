/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.commons.util.cdi;

import java.util.Locale;

import ch.qos.cal10n.IMessageConveyor;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;

@SPlcUtil
@QPlcDefault
public class PlcMessageConveyorFactory {

	public static final String PREFIXO = "JCP";

	public IMessageConveyor getDefaultMessageConveyer(String subsystem) {
		return new PlcMessageConveyor(Locale.getDefault(), subsystem);
	}
	
	public IMessageConveyor getDefaultMessageConveyer(Locale locale, String subsystem) {
		return new PlcMessageConveyor(locale, subsystem);
	}
 
}
