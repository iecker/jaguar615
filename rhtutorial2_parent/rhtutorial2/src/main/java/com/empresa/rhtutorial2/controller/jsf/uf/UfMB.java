package com.empresa.rhtutorial2.controller.jsf.uf;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.empresa.rhtutorial2.commons.AppUserProfileVO;
import com.empresa.rhtutorial2.controller.jsf.AppMB;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.config.collaboration.PlcConfigTabular;
import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
	
@PlcConfigAggregation(entity = com.empresa.rhtutorial2.entity.UfEntity.class)

@PlcConfigForm(
	
	formPattern=FormPattern.Tab,
	tabular = @PlcConfigTabular(numNew = 4), formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/uf")
	)





/**
 * Classe de Controle gerada pelo assistente
 */
 
@SPlcMB
@PlcUriIoC("uf")	
@PlcHandleException
public class UfMB extends AppMB  {

	private static final long serialVersionUID = 1L;

	

	/**
	 * Entidade da ação injetado pela CDI
	*/
	@Produces  @Named("ufLista") 
	public PlcEntityList createEntityListPlc() {
		if (this.entityListPlc==null) {
			this.entityListPlc = new PlcEntityList();
			this.newObjectList();
		}
		return this.entityListPlc;
	}	
	
	
	@Override
	public void handleButtonsAccordingFormPattern() {
	    super.handleButtonsAccordingFormPattern();
	    AppUserProfileVO userProfileVO = (AppUserProfileVO) contextUtil.getRequest().getSession().getAttribute(PlcConstants.USER_INFO_KEY);
	    if (!userProfileVO.isUserInRole("VERSIGLA")) {
	        visaoJsfUtil.hideWithLabels("sigla");
	    }
	}
}