/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.jcompanyqa.mdt;

import com.powerlogic.jcompany.qa.PlcWebTestCase;
import com.powerlogic.jcompany.qa.PlcWebTestCasoDeUso;

public class ProfissionalWebTest extends PlcWebTestCase { 

		PlcWebTestCasoDeUso casoDeUso = new PlcWebTestCasoDeUso("profissional.xml");

		public void testCasoDeUsoWebTest() {
			casoDeUso.setListenerPreencherCampo(this);
			casoDeUso.executar();			
			
		}

		@Override
		public void aposPreencherCampo(String nomeCampo, String valorCampo, String tipoCampo) {
			
			if (nomeCampo.endsWith(".departamento"))
				esperar(2000);
			
			super.aposPreencherCampo(nomeCampo, valorCampo, tipoCampo);
		}
		
}