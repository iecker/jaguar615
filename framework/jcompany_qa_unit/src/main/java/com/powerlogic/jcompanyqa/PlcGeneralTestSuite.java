/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompanyqa;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.powerlogic.jcompanyqa.commons.util.PlcCommonsUtilTestSuite;
import com.powerlogic.jcompanyqa.model.PlcModelTest;
import com.powerlogic.jcompanyqa.persistence.PlcPersistenceTestSuite;

/**
 * jCompany 2.7.3. Suíte de Testes Gerais do jCompany
 * @since jCompany 2.7.3
*/

public class PlcGeneralTestSuite {

	/**
	 * @since jCompany 2.7.3
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite("Testes Gerais do jCompany");
		
		//Testa o projeto Comuns
		suite.addTest(PlcCommonsUtilTestSuite.suite());
		
		//Testa o projeto Modelo
		suite.addTestSuite(PlcModelTest.class);
		
		//Testa a persistencia no BD.
		suite.addTest(PlcPersistenceTestSuite.suite());
		return suite;
	}

}
