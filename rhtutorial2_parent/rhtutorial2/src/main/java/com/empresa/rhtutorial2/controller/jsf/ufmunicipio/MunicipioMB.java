package com.empresa.rhtutorial2.controller.jsf.ufmunicipio;

import javax.enterprise.inject.Produces;
import javax.inject.Named;


import com.empresa.rhtutorial2.entity.UfEntity;
import com.empresa.rhtutorial2.controller.jsf.AppMB;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.config.collaboration.FormPattern;

import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm.ExclusionMode;



import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;

@PlcConfigAggregation(
		entity = com.empresa.rhtutorial2.entity.UfEntity.class

		,details = { 		@com.powerlogic.jcompany.config.aggregation.PlcConfigDetail(clazz = com.empresa.rhtutorial2.entity.MunicipioEntity.class,
								collectionName = "municipio", numNew = 4,onDemand = false)
			

		}
	)


@PlcConfigForm (
	
	formPattern=FormPattern.Mad,
	formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/ufmunicipio")
	
	
)


/**
 * Classe de Controle gerada pelo assistente
 */
 
@SPlcMB
@PlcUriIoC("ufmunicipio")
@PlcHandleException
public class MunicipioMB extends AppMB  {

	private static final long serialVersionUID = 1L;
	
	
     		
	/**
	 * Entidade da ação injetado pela CDI
	 */
	@Produces
	@Named("ufmunicipio")
	public UfEntity createEntityPlc() {
		if (this.entityPlc == null) {
			this.entityPlc = new UfEntity();
			this.newEntity();
		}
		return (UfEntity) this.entityPlc;
	}

	@Override
	public String save() {

		super.save();

		/*if (conversation != null) {
			conversation.end();
		}*/

		return "grava";

	}
		
}
