/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;


public class PlcConverterDateMaskForArgument implements Converter {

	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		//FIXME - Pegar máscara
		if (value != null && value.length() == 7)
			value = "01/".concat(value);
		else if (value != null && value.length() == 5)
			value = "01/".concat(value.substring(0,2)).concat("20").concat(value.substring(3,2));

		return value;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null && value.toString().length() == 10)
			return value.toString().substring(3, value.toString().length());
		else if (value != null && (value.toString().length() == 7 || value.toString().length() == 5))
			return value.toString();
		else
			return null;
	}

}
