package com.empresa.rhtutorial.controller.jsf.unidadeOrganizacional;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.empresa.rhtutorial.controller.jsf.AppMB;
import com.empresa.rhtutorial.entity.UnidadeOrganizacionalEntity;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigBehavior;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.config.collaboration.PlcConfigNestedCombo;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@PlcConfigAggregation(
		entity = com.empresa.rhtutorial.entity.UnidadeOrganizacionalEntity.class,
		components = {@com.powerlogic.jcompany.config.aggregation.PlcConfigComponent
			(clazz=com.empresa.rhtutorial.entity.Endereco.class,property="endereco")}
	)

@PlcConfigForm (formPattern=FormPattern.Man,
	formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/unidadeOrganizacional"),
	behavior = @PlcConfigBehavior(useTreeView=true),
	nestedCombo=@PlcConfigNestedCombo(origemProp="endereco.uf",destinyProp="endereco.municipio")
)

@SPlcMB
@PlcUriIoC("unidadeOrganizacional")
@PlcHandleException
public class UnidadeOrganizacionalMB extends AppMB  {

	private static final long serialVersionUID = 1L;

	@Produces  @Named("unidadeOrganizacional")
	public UnidadeOrganizacionalEntity criaEntidadePlc() {

        if (this.entityPlc==null) {
              this.entityPlc = new UnidadeOrganizacionalEntity();
              this.newEntity();
        }

        return (UnidadeOrganizacionalEntity)this.entityPlc;
        
	}	
}
