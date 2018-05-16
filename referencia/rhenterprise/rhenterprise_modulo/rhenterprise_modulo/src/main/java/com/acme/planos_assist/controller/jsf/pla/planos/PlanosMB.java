package com.acme.planos_assist.controller.jsf.pla.planos;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.acme.planos_assist.controller.jsf.PlaMB;
import com.acme.planos_assist.entity.planos.PlanosEntity;
import com.powerlogic.jcompany.commons.annotation.PlcDbFactory;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;


/*	Para alterar o comportamento padrÃ£o do jCompany, descomente esse trecho do cÃ³digo e altere a configuraÃ§Ã£o conforme desejar. 

import com.powerlogic.jcompany.config.aggregation.PlcConfigAgregacao;


@PlcConfigAgregacao(
		entidade = com.acme.planos_assist.entity.planos.PlanosEntity.class
	)

*/


@PlcConfigForm (
	formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/pla/planos")
)


/**
 * Classe de Controle gerada pelo assistente
 */
 
@PlcDbFactory(nome="pla")
 
 
@SPlcMB
@PlcUriIoC("pla/planos")
@PlcHandleException
public class PlanosMB extends PlaMB  {

	private static final long serialVersionUID = 1L;
	
	
     		
	/**
	* Entidade da aÃ§Ã£o injetado pela CDI
	*/
	@Produces  @Named("planos")
	public PlanosEntity criaEntidadePlc() {

        if (this.entityPlc==null) {
              this.entityPlc = new PlanosEntity();
              this.newEntity();
        }

        return (PlanosEntity)this.entityPlc;
        
	}	
}
