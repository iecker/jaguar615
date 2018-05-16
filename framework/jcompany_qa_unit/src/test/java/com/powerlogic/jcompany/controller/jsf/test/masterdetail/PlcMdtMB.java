/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.test.masterdetail;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.aggregation.PlcConfigComponent;
import com.powerlogic.jcompany.config.aggregation.PlcConfigDetail;
import com.powerlogic.jcompany.controller.jsf.PlcBaseMB;
import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@PlcConfigAggregation(
		entity = EmployeeEntity.class,

	components = {@PlcConfigComponent(clazz=Address.class, property="enderecoResidencial")},
		details = {
			@PlcConfigDetail(clazz = DependentEntity.class,
								collectionName = "dependente", numNew = 4, onDemand = true),
			@PlcConfigDetail(clazz = ProfissionalHistoryEntity.class,
								collectionName = "historicoProfissional", numNew = 4,onDemand = true)
		}//, fileAttach = @PlcFileAttach(clazz="com.powerlogic.jcompany.controller.jsf.test.masterdetail.FotoEntity", collectionName="fotos",multiple=false)
	)


@SPlcMB
@PlcUriIoC("mestre")
@PlcHandleException
public class PlcMdtMB extends PlcBaseMB {

	/**
	 * Entidade da ação injetado pela CDI
	 */
	@Produces  @Named("funcionario") 
	public EmployeeEntity criaEntidadePlc() {
		if (this.entityPlc==null) {
			this.entityPlc = new EmployeeEntity();
			this.newEntity();
		}
		return (EmployeeEntity)this.entityPlc;
	}


	/**
	 * Entidade da ação injetado pela CDI
	 */
	@Produces  @Named("funcionarioLista") 
	public PlcEntityList criaLogicaItensPlc() {
		if (this.entityListPlc==null) {
			this.entityListPlc = new PlcEntityList();
			this.newObjectList();
		}
		return this.entityListPlc;
	}		
}
