/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.jcompanyqa.mdt;

import com.powerlogic.jcompany.qa.PlcWebTestCase;
import com.powerlogic.jcompany.qa.PlcWebTestCasoDeUso;

public class ClienteGrupoWebTest extends PlcWebTestCase { 

		PlcWebTestCasoDeUso casoDeUso = new PlcWebTestCasoDeUso("clienteGrupo.xml");

		public void testCasoDeUsoWebTest() {
			casoDeUso.executar();
		}

}