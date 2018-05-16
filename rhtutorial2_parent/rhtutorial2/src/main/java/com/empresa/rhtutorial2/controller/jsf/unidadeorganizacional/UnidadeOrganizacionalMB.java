package com.empresa.rhtutorial2.controller.jsf.unidadeorganizacional;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.empresa.rhtutorial2.controller.jsf.AppMB;
import com.empresa.rhtutorial2.entity.UnidadeOrganizacionalEntity;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcSpecific;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@PlcConfigAggregation(
		entity = com.empresa.rhtutorial2.entity.UnidadeOrganizacionalEntity.class
		

		,components = {@com.powerlogic.jcompany.config.aggregation.PlcConfigComponent(clazz=com.empresa.rhtutorial2.entity.Endereco.class, property="endereco", separate=true)}
	)
	


@PlcConfigForm (
	
	formPattern=FormPattern.Man,
	formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/unidadeorganizacional")
	
	
)


/**
 * Classe de Controle gerada pelo assistente
 */
 
@SPlcMB
@PlcUriIoC("unidadeorganizacional")
@QPlcSpecific(name="unidadeorganizacional")
@PlcHandleException
public class UnidadeOrganizacionalMB extends AppMB  {

	private static final long serialVersionUID = 1L;
	
	
     		
	/**
	* Entidade da ação injetado pela CDI
	*/
	@Produces  @Named("unidadeorganizacional")
	public UnidadeOrganizacionalEntity createEntityPlc() {
        if (this.entityPlc==null) {
              this.entityPlc = new UnidadeOrganizacionalEntity();
              this.newEntity();
        }
        return (UnidadeOrganizacionalEntity)this.entityPlc;     	
	}
	
	int i;
	@Override
	public String save() {
		i++;
		if (i==1) {
			// exceção inesperada não tratada
			int k = 2 / (i - 1);
			return null;
		} if (i==2){
			// exceção inesperada tratada
			try {
				int k = 2 / (i - 2);
				return null;
			} catch (Exception e) {
				throw new PlcException("UnidadeOrganizacionalMB", "save", e, log, "Meu complemento");
			}
		} else if (i==3) {
			// exceção controlada
			throw new PlcException("Esta é uma exceção controlada");
		} else if (i==4) {
			// exceção controlada i18n
			throw new PlcException("{chave.teste}");
		} else if (i==5) {
			// exceção controlada i18n chave errada
			throw new PlcException("{chave.faltando}");
		}
		return super.save();
	}
		
}
