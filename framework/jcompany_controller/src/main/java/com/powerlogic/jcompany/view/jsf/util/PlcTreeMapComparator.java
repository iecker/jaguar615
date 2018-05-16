/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.util;

import java.io.Serializable;
import java.util.Comparator;

class PlcTreeMapComparator implements Comparator<String>, Serializable  {

	private static final long serialVersionUID = 7854766820159603525L;

	public int compare(String o1, String o2) {
		if (o1.equals(o2))
			return 0;
		return +1;
	}
	
}