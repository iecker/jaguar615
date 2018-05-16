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

import br.com.plc.jcompany_fcls.entity.departamento.DepartamentoEntity;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

/*
@PlcConfigBpm(
        processoDefinicao="processoPesquisaPowerlogic", 
        urlImagemProcesso="http://localhost:8080/activiti-cycle/proxy/activiti-rest-endpoint/content?connectorId=Activiti&artifactId=%2Froot-directory%3Bexamples%3Bpowerlogic%3BProcessoPesquisaPowerlogic.signavio.xml&contentRepresentationId=PNG"
)
*/
@PlcConfigAggregation(
		entity= br.com.plc.jcompany_fcls.entity.departamento.DepartamentoEntity.class
		,components = {@com.powerlogic.jcompany.config.aggregation.PlcConfigComponent(clazz=br.com.plc.jcompany_fcls.entity.departamento.Endereco.class, property="endereco")}
		)
@SPlcMB
@PlcUriIoC("departamento")
@PlcHandleException
public class DepartamentoMB extends AppMB {

	private static final long serialVersionUID = 1L;

	/**
	 * Entidade da aÃ§Ã£o injetado pela CDI
	 */
	@Produces  @Named("departamento") 
	public DepartamentoEntity criaEntityPlc() {
		if (this.entityPlc==null) {
			this.entityPlc = new DepartamentoEntity();
			this.newEntity();
		}
		return (DepartamentoEntity)this.entityPlc;
	}

	
	@Inject
	private ConversationObj objetoConversacao;

	@PostConstruct
	public void criacao() {
		log.info("CRIADO Departamento ACTION !! " + objetoConversacao);
		if (objetoConversacao != null) {
			log.info("Valor do objeto de conversacao: "+objetoConversacao.getValor());
		}
	}
	
	@PreDestroy
	public void destroy() {
		log.info("Action DepartamentoAction destruida");
	}

}
