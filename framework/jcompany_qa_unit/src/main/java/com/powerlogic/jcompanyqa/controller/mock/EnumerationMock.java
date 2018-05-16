/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompanyqa.controller.mock;


import java.util.Enumeration;
import java.util.Iterator;


/**
 * <p>General purpose <code>Enumeration</code> wrapper around an
 * <code>Iterator</code> specified to our controller.</p>
 *
 * @since jCompany 2.7.3
 */

public class EnumerationMock implements Enumeration {


	/**
	 * @since jCompany 2.7.3
	 */
    public EnumerationMock(Iterator iterator) {
        this.iterator = iterator;
    }


    protected Iterator iterator;


	/**
	 * @since jCompany 2.7.3
	 */
    public boolean hasMoreElements() {
        return (iterator.hasNext());
    }


	/**
	 * @since jCompany 2.7.3
	 */
    public Object nextElement() {
        return (iterator.next());
    }


}
