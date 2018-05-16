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

import br.com.plc.jcompany_fcls.entity.usuario.UsuarioEntity;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigBehavior;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.controller.jsf.PlcEntityList;


@PlcConfigForm(formPattern=FormPattern.Man,
			behavior = @PlcConfigBehavior(batchInput=false)
	)


@SPlcMB
@PlcUriIoC("usuario")
@ConversationScoped
public class UsuarioMB extends AppMB {

	@Inject
	protected transient Logger log;
	
	@Inject 
	protected UsuarioSaveMB usuarioSaveAction;
	
	/**
	 * Entidade da aÃ§Ã£o injetado pela CDI
	 */
	@Produces  @Named("usuario") 
	public UsuarioEntity criaentityPlc() {
		if (this.entityPlc==null) {
			this.entityPlc = new UsuarioEntity();
			this.newEntity();
		}
		return (UsuarioEntity)this.entityPlc;
	}
 
	
	/**
	 * Lista de entidade da aÃ§Ã£o injetado pela CDI
	*/
	@Produces  @Named("usuarioLista")
	public PlcEntityList criaentityListPlc() {
		if (this.entityListPlc==null) {
			this.entityListPlc = new PlcEntityList();
			this.newObjectList();
		}
		return this.entityListPlc;
	}
	
	@Override
	public String save() {
		return usuarioSaveAction.save(entityPlc);
	}

}
