/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package br.com.plc.rhdemo.controller.jsf;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.plc.rhdemo.entity.FuncionarioEntity;
import br.com.plc.rhdemo.facade.IFuncionarioFacade;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@SPlcMB
@PlcUriIoC("funcionario")
@PlcHandleException
public class FuncionarioMB extends AppMB {

	@Inject
	IFuncionarioFacade funcionarioFacade;
	
	private static final long serialVersionUID = 1L;

 
	
	@Produces  @Named("funcionario") 
	public FuncionarioEntity createEntityPlc() {
		if (this.entityPlc==null) {
			this.entityPlc = new FuncionarioEntity();
			this.newEntity();
		}
		return (FuncionarioEntity)this.entityPlc;
	}
	

	public String verifica() {
		funcionarioFacade.verificaFuncionario((FuncionarioEntity)this.entityPlc);
		return null;
	}
	
}
