package com.empresa.rhrich.jcompanyqa;

import com.powerlogic.jcompany.qa.PlcWebTestCase;
import com.powerlogic.jcompany.qa.PlcWebTestTabular;

public class UfWebTest extends PlcWebTestCase {

	PlcWebTestTabular casoDeUso = new PlcWebTestTabular("uf.xml");
	
	public void testUfWebTest() {
		
		
		casoDeUso.executar();
		
	}

}
