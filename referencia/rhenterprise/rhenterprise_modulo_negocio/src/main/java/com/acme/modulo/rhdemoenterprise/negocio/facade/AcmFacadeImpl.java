/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/
package com.acme.modulo.rhdemoenterprise.negocio.facade;

import java.util.List;

import javax.inject.Inject;

import com.acme.modulo.rhdemoenterprise.negocio.entity.DadosProfional;
import com.acme.modulo.rhdemoenterprise.negocio.model.AcmRepository;
import com.powerlogic.jcompany.facade.PlcFacadeImpl;

public class AcmFacadeImpl extends PlcFacadeImpl  implements IAcmFacade {

	@Inject
	private AcmRepository mediaSalarial;
	
	public String retornaCategoriaSalario(List<DadosProfional> listaProfissional) {
		
		return mediaSalarial.retornaCategoriaSalario(listaProfissional);
	}
}
