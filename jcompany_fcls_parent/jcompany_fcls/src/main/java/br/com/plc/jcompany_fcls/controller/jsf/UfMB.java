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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.config.collaboration.PlcConfigTabular;
import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@PlcConfigForm(formPattern=FormPattern.Tab,
		tabular= @PlcConfigTabular(numNew=4),
		formLayout = @PlcConfigFormLayout(
							dirBase = "/WEB-INF/fcls/uf")
	)						


@SPlcMB
@PlcUriIoC("uf")
@PlcHandleException
public class UfMB extends AppMB {

	private static final long serialVersionUID = 1L;

	/**
	 * Entidade da aÃ§Ã£o injetado pela CDI
	 */
	@Produces  @Named("ufLista") 
	public PlcEntityList createEntityListPlc() {
		if (this.entityListPlc==null) {
			this.entityListPlc = new PlcEntityList();
			this.newObjectList();
		}
		return this.entityListPlc;
	}	
	
	
	
	@Inject
	private ConversationObj objetoConversacao;
	
	
	@PostConstruct
	public void criacao() {
		log.info("CRIADO UF ACTION !! "+objetoConversacao);
	}
	
	@PreDestroy
	public void destroy() {
		log.info("Action UFAction destruida !");
	}


}
