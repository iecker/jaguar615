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
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.config.collaboration.PlcConfigTabular;
import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

//@PlcConfigAggregation(entity = br.com.plc.jcompany_fcls.entity.categoria.CategoriaEntity.class)

@PlcConfigForm(formPattern=FormPattern.Tab,
		tabular= @PlcConfigTabular(numNew=4),
		formLayout = @PlcConfigFormLayout(
							dirBase = "/WEB-INF/fcls/categoria")
	)						


@SPlcMB
@PlcUriIoC("categoria")
@PlcHandleException
public class CategoriaMB extends AppMB {

	private static final long serialVersionUID = 1L;

	
	/**
	 * Entidade da aÃ§Ã£o injetado pela CDI
	 */
	@Produces  
	@Named("categoriaLista") 
	public PlcEntityList criaentityListPlc() {
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
		log.info("CRIADO CATEGORIA ACTION !! "+objetoConversacao);
	}
	
	@PreDestroy
	public void destroy() {
		log.info("Action CategoriaAction destruida !");
	}



}
