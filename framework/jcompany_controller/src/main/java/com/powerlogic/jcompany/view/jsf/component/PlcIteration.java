/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.component;

import java.io.IOException;

import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;

public class PlcIteration extends HtmlDataTable   {
	
	
	@Override
	public void setId(String id) {
		 
		super.setId(getVar());
	}
	
	@Override
	public void encodeBegin(FacesContext context) throws IOException {

		String styleClass = getStyleClass();
		if (styleClass==null || styleClass.equals("")) {
			styleClass = "plc-table-tabsel delimitador tabelaFormulario";
			setStyleClass(styleClass);
		}
		
		String columnClasses = getColumnClasses();
		if (columnClasses==null || columnClasses.equals("")) {
			columnClasses = "celulaFormulario";
			super.setColumnClasses(columnClasses);
		}
		
		String headerClass = getHeaderClass();
		if (headerClass==null || headerClass.equals("")) {
			headerClass = "celulaFormulario";
			setHeaderClass(headerClass);
		}
		
		super.encodeBegin(context);
	}
	

	
}
