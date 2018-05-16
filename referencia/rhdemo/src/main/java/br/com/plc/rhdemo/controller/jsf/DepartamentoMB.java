/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package br.com.plc.rhdemo.controller.jsf;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import br.com.plc.rhdemo.entity.DepartamentoEntity;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@SPlcMB
@PlcUriIoC("departamento")
@PlcHandleException
public class DepartamentoMB extends AppMB {

	private static final long serialVersionUID = 1L;

	/**
	 * Entidade da aÃ§Ã£o injetado pela CDI
	 */
	@Produces  @Named("departamento") 
	public DepartamentoEntity createEntityPlc() {
		if (this.entityPlc==null) {
			this.entityPlc = new DepartamentoEntity();
			this.newEntity();
		}
		return (DepartamentoEntity)this.entityPlc;
	}


}
