/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompanyqa.commons.util;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Testes para camada Comuns (Helper)
 * @since jCompany 2.7.3
 */

public class PlcCommonsUtilTestSuite{

	/**
	 * @since jCompany 2.7.3
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite("Testes Gerais do jCompany");
		suite.addTestSuite(PlcDateUtilTest.class);
		suite.addTestSuite(PlcVOUtilTest.class);
		return suite;
	}
}
