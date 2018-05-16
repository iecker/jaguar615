/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package br.com.plc.jcompany_fcls.controller.jsf;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import br.com.plc.jcompany_fcls.entity.profissional.ProfissionalEntity;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;


@SPlcMB
@PlcUriIoC("profissional")	
@PlcHandleException
@ConversationScoped
public class ProfissionalMB extends AppMB {

	@Inject
	protected transient Logger log;
	
	/**
	 * Entidade da aÃ§Ã£o injetado pela CDI
	 */
	@Produces  @Named("profissional") 
	public ProfissionalEntity criaentityPlc() {
		if (this.entityPlc==null) {
			this.entityPlc = new ProfissionalEntity();
			this.newEntity();
		}
		return (ProfissionalEntity)this.entityPlc;
	}
 

}
