/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/
package com.acme.modulo.rhdemoenterprise.negocio.facade;

import java.util.List;

import com.acme.modulo.rhdemoenterprise.negocio.entity.DadosProfional;
import com.powerlogic.jcompany.commons.facade.IPlcFacade;

public interface IAcmFacade extends IPlcFacade {

	public String retornaCategoriaSalario(List<DadosProfional> listaProfissional);
	
}
