package com.acme.rhdemoenterprise.controller.jsf.unidadeOrganizacional;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.acme.rhdemoenterprise.controller.jsf.AppMB;
import com.acme.rhdemoenterprise.entity.UnidadeOrganizacionalEntity;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigBehavior;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;



/*	Para alterar o comportamento padrÃ£o do jCompany, descomente esse trecho do cÃ³digo e altere a configuraÃ§Ã£o conforme desejar. 

import com.powerlogic.jcompany.config.aggregation.PlcConfigAgregacao;


@PlcConfigAgregacao(
		entidade = com.acme.rhdemoenterprise.entity.UnidadeOrganizacionalEntity.class

	,componentes = {@com.powerlogic.jcompany.config.agregacao.PlcConfigComponente(classe=com.acme.rhdemoenterprise.entity.Endereco.class, propriedade="endereco")}
	)

*/

@PlcConfigForm(
		formPattern = FormPattern.Man,
		behavior = @PlcConfigBehavior(useTreeView=true)
)


/**
 * Classe de Controle gerada pelo assistente
 */
 
 
 
@SPlcMB
@PlcUriIoC("unidadeOrganizacional")
@PlcHandleException
public class UnidadeOrganizacionalMB extends AppMB  {

	private static final long serialVersionUID = 1L;
	
	
     		
	/**
	* Entidade da aÃ§Ã£o injetado pela CDI
	*/
	@Produces  @Named("unidadeOrganizacional")
	public UnidadeOrganizacionalEntity criaEntidadePlc() {

        if (this.entityPlc==null) {
              this.entityPlc = new UnidadeOrganizacionalEntity();
              this.newEntity();
        }

        return (UnidadeOrganizacionalEntity)this.entityPlc;
        
	}	
}
