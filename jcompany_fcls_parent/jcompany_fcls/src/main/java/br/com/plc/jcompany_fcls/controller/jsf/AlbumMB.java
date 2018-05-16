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

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import br.com.plc.jcompany_fcls.entity.album.AlbumEntity;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@PlcUriIoC("album")
@PlcHandleException
@SPlcMB
public class AlbumMB extends AppMB {

	@Inject
	protected transient Logger log;
	
	/**
	 * Entidade da aÃ§Ã£o injetado pela CDI
	 */
	@Produces  @Named("album") 
	public AlbumEntity criaentityPlc() {
		if (this.entityPlc==null) {
			this.entityPlc = new AlbumEntity();
			this.newEntity();
		}
		return (AlbumEntity)this.entityPlc;
	}
}
