/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.controller.jsf;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import br.com.plc.jcompany_fcls.entity.cliente.GrupoEntity;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@PlcConfigAggregation(
		entity = br.com.plc.jcompany_fcls.entity.cliente.GrupoEntity.class
	)

@PlcConfigForm(
		formPattern= FormPattern.Mdt,
		formLayout = @PlcConfigFormLayout(
							dirBase = "/WEB-INF/fcls/grupo")
//		,
//		selecao = @PlcConfigSelecao(
//					navegador = @PlcConfigNavegador(numPorPagina=20, dinamicoTipo=com.powerlogic.jcompany.config.colaboracao.PlcConfigNavegador.DinamicoTipo.DINAMICO_PAGINA),				argumentos = {
//							@PlcConfigArgumento(propriedade="id", operador=Operador.IGUAL_A, formato=Formato.LONG),
//							@PlcConfigArgumento(propriedade="nome", operador=Operador.LIKE_PERC_FINAL, formato=Formato.STRING),
//							@PlcConfigArgumento(propriedade="inativo", operador=Operador.IGUAL_A, formato=Formato.STRING)
//				})

	)

@SPlcMB
@PlcUriIoC("grupo")
@PlcHandleException
@ConversationScoped
public class GrupoMB extends AppMB {
	
	/**
	 * Entidade da aÃ§Ã£o injetado pela CDI
	 */
	@Produces  @Named("grupo") 
	public GrupoEntity criaentityPlc() {
		if (this.entityPlc==null) {
			this.entityPlc = new GrupoEntity();
			this.newEntity();
		}
		return (GrupoEntity)this.entityPlc;
	}

	
	
}
