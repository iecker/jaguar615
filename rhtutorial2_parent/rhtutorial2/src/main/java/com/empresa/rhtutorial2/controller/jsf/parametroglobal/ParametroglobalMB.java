package com.empresa.rhtutorial2.controller.jsf.parametroglobal;

import javax.enterprise.inject.Produces;
import javax.inject.Named;


import com.empresa.rhtutorial2.entity.ParametroGlobal;
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
		entity= com.empresa.rhtutorial2.entity.ParametroGlobalEntity.class
	)


@PlcConfigForm (
	
	formPattern=FormPattern.Apl,
	formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls")
	
	
)


/**
 * Classe de Controle gerada pelo assistente
 */
 
@SPlcMB
@PlcUriIoC("parametroglobal")
@PlcHandleException
public class ParametroglobalMB extends AppMB  {

	private static final long serialVersionUID = 1L;
	
	
     		
	/**
	* Entidade da ação injetado pela CDI
	*/
	@Produces  @Named("parametroglobal")
	public ParametroGlobal createEntityPlc() {
        if (this.entityPlc==null) {
              this.entityPlc = new ParametroGlobal();
              this.newEntity();
        }
        return (ParametroGlobal)this.entityPlc;     	
	}
	
	@Override
	public String save() {
		super.save();
		contextUtil.setApplicationAttribute ("parametroGlobal", this.entityPlc);
		return null;
	}
		
}
