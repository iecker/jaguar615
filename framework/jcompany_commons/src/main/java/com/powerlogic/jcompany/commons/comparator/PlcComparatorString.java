/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.comparator;


import java.util.Comparator;

/**
 * jCompany 2.5.3. Comparator. Compara Strings dentro de coleção 
 */
public class PlcComparatorString implements Comparator {

    
    /**
     * @since jCompany 3.0
     *
     */
	public boolean equals( Object parameter1 )  {
        return false; 
    }

    
	 /**
     * @since jCompany 3.0
     *
     */
	public int compare(Object obj1, Object obj2) { 

		String s1 = (String) obj1; 

		String s2 = (String) obj2; 

		return s1.compareTo(s2); 

    }
}

