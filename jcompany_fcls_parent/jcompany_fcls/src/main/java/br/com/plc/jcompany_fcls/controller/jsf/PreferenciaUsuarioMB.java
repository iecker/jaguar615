/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
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

import br.com.plc.jcompany_fcls.entity.preferencia.PreferenciaUsuarioEntity;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;


@PlcConfigForm(formPattern=FormPattern.Usu,
		formLayout = @PlcConfigFormLayout(
							dirBase = "/WEB-INF/fcls/preferencia")
	)						



@PlcUriIoC("preferenciausu")	
@PlcHandleException
@SPlcMB
@ConversationScoped
public class PreferenciaUsuarioMB extends AppMB {

	@Inject
	protected transient Logger log;

	/**
	 * Entidade da aÃ§Ã£o injetado pela CDI
	 */
	@Produces  @Named("preferenciausu") 
	public PreferenciaUsuarioEntity criaentityPlc() {
		
		if (this.entityPlc==null) {
			this.entityPlc = new PreferenciaUsuarioEntity();
			this.newEntity();
		}
		return (PreferenciaUsuarioEntity)this.entityPlc;
	}
}
	

