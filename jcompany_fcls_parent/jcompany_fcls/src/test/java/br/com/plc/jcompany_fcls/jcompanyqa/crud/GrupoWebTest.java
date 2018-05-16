/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.jcompanyqa.crud;

import com.powerlogic.jcompany.qa.PlcWebTestCase;
import com.powerlogic.jcompany.qa.PlcWebTestCrud;

public class GrupoWebTest extends PlcWebTestCase { 

		PlcWebTestCrud casoDeUso = new PlcWebTestCrud("grupo.xml");

		public void testCrudWebTest() {
			casoDeUso.executar();
		}

}