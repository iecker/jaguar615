/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.jcompanyqa.tabular;

import com.powerlogic.jcompany.qa.PlcWebTestCase;
import com.powerlogic.jcompany.qa.PlcWebTestTabular;

public class UnidadeFederativaWebTest extends PlcWebTestCase{
	
	PlcWebTestTabular casoDeUso = new PlcWebTestTabular("uf.xml");
	
	
	public void testUnidadeFederativaWebTest(){
		
		casoDeUso.executar();
		
	}
	

	
}
