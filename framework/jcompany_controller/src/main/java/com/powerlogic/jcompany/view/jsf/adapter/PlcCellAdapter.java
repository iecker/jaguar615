/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.adapter;

import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.component.html.HtmlCellFormat;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;


public class PlcCellAdapter {

	private static PlcCellAdapter INSTANCE = new PlcCellAdapter ();
	
	private PlcCellAdapter () {
		
	}

	public static PlcCellAdapter getInstance () {
		return INSTANCE;
	}

	public void adapter (FacesBean bean){
		
		PlcComponentUtil componenteUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		if (!componenteUtil.isValueDefined(bean, HtmlCellFormat.STYLE_CLASS_KEY)) {
			bean.setProperty(HtmlCellFormat.STYLE_CLASS_KEY, "celulaFormulario");
		}
		
	}
	
}
