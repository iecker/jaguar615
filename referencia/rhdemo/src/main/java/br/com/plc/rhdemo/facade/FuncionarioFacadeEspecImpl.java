/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package br.com.plc.rhdemo.facade;

import br.com.plc.rhdemo.entity.FuncionarioEntity;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcFacade;
import com.powerlogic.jcompany.model.annotation.PlcTransactional;

@SPlcFacade
public class FuncionarioFacadeEspecImpl implements IFuncionarioFacade {

	@Override
	@PlcTransactional
	public boolean verificaFuncionario(FuncionarioEntity entidade) {
		System.out.println(entidade);
		return true;
	}

}
