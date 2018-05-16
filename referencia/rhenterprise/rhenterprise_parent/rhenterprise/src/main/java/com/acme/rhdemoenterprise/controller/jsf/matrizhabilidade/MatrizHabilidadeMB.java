package com.acme.rhdemoenterprise.controller.jsf.matrizhabilidade;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.acme.rhdemoenterprise.controller.jsf.AppMB;
import com.acme.rhdemoenterprise.entity.MatrizHabilidadeEntity;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.extension.manytomanymatrix.metadata.PlcConfigManyToManyMatrix;

@PlcConfigForm(
		formPattern = FormPattern.Plx
)


@PlcConfigAggregation(
		
		entity = com.acme.rhdemoenterprise.entity.MatrizHabilidadeEntity.class
)

@PlcConfigManyToManyMatrix(classeAssociativa = com.acme.rhdemoenterprise.entity.MatrizHabilidadeEntity.class , 
		classeEntidade1 =com.acme.rhdemoenterprise.entity.FuncionarioEntity.class , 
		classeEntidade2 = com.acme.rhdemoenterprise.entity.HabilidadeEntity.class, 
		propriedadeEntidade1 = "funcionario", 
		propriedadeEntidade2 = "habilidade"
)


/**
 * Classe de Controle gerada pelo assistente
 */
@SPlcMB
@PlcUriIoC("matrizhabilidade")
@PlcHandleException
public class MatrizHabilidadeMB extends AppMB  {

	private static final long serialVersionUID = 1L;
     		
	/**
	* Entidade da aÃ§Ã£o injetado pela CDI
	*/
	@Produces  @Named("matrizhabilidade")
	public MatrizHabilidadeEntity criaEntidadePlc() {

        if (this.entityPlc==null) {
              this.entityPlc = new MatrizHabilidadeEntity();
              this.newEntity();
        }

        return (MatrizHabilidadeEntity)this.entityPlc;
        
	}	
}
