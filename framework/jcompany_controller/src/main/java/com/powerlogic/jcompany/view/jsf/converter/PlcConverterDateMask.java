/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.converter;

import java.text.SimpleDateFormat;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.PlcDateUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;

public class PlcConverterDateMask implements Converter {

	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		PlcDateUtil dateUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcDateUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		if (value != null && value.length() == 6 && !value.contains("/")) {
			value = value.substring(0,2) + "/" + value.substring(2,6);
		} else if (value != null && value.length() == 8 && !value.contains("/")) {
			value = value.substring(0,2) + "/" + value.substring(2,4)  + "/" + value.substring(4,8);
		}
		
		if (value != null && value.length() == 7) {
			value = "01/".concat(value);
		} else if (value != null && value.length() == 5) {
			value = "01/".concat(value.substring(0,2)).concat("20").concat(value.substring(3,2));
		}
		
		try{
			if (value != null && StringUtils.isNotEmpty(value)) {
				return dateUtil.stringToDate(value);
			} else {
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null){
			return (new SimpleDateFormat("MM/yyyy")).format(value);
		} else {
			return null;
		}
	}

}
