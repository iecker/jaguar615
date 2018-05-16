/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompanyqa.persistence;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Testes para Persistencia
 * @since jCompany 2.7.3
 */

public class PlcPersistenceTestSuite{

	/**
	 * @since jCompany 2.7.3
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite("Testes para com.powerlogic.jcompany.persistencia.hibernate");
		//suite.addTestSuite(PlcPersistenciaUtilTest.class);
		return suite;
	}
}
