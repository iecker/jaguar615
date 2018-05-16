package com.empresa.rhtutorial2.controller.jsf.funcionario;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.empresa.rhtutorial2.controller.jsf.AppMB;
import com.empresa.rhtutorial2.entity.funcionario.Funcionario;
import com.empresa.rhtutorial2.entity.funcionario.FuncionarioEntity;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@PlcConfigAggregation(
		entity = com.empresa.rhtutorial2.entity.funcionario.FuncionarioEntity.class


		,components = {@com.powerlogic.jcompany.config.aggregation.PlcConfigComponent(clazz=com.empresa.rhtutorial2.entity.Endereco.class, property="enderecoResidencial", separate=true)}
		,details = { 		@com.powerlogic.jcompany.config.aggregation.PlcConfigDetail(clazz = com.empresa.rhtutorial2.entity.funcionario.DependenteEntity.class,
								collectionName = "dependente", numNew = 4,onDemand = true)
			
			,
		@com.powerlogic.jcompany.config.aggregation.PlcConfigDetail(clazz = com.empresa.rhtutorial2.entity.funcionario.HistoricoProfissionalEntity.class,
								collectionName = "historicoProfissional", numNew = 4,onDemand = false)
			

		}
	)
	



@PlcConfigForm (
	
	formPattern=FormPattern.Mdt,
	formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/funcionario")
	
	
)


/**
 * Classe de Controle gerada pelo assistente
 */
 
@SPlcMB
@PlcUriIoC("funcionario")
@PlcHandleException
public class FuncionarioMB extends AppMB  {

	private static final long serialVersionUID = 1L;
	
	
     		
	/**
	* Entidade da ação injetado pela CDI
	*/
	@Produces  @Named("funcionario")
	public FuncionarioEntity createEntityPlc() {
        if (this.entityPlc==null) {
              this.entityPlc = new FuncionarioEntity();
              this.newEntity();
        }
        return (FuncionarioEntity)this.entityPlc;     	
	}
	
	@Override
	public String cloneEntity() {

		Funcionario f = (Funcionario) this.entityPlc;

		// Fazer limpeza de salários.

		super.cloneEntity();

		return null;
	}
	
	@Override
	public void handleButtonsAccordingFormPattern() {

		super.handleButtonsAccordingFormPattern();
		if (contextUtil.getRequest().isUserInRole("visitante") ||
	          	contextUtil.getRequest().getUserPrincipal()
	                                 .getName().equals("admin2")) {
			visaoJsfUtil.hideAction("jcompany_evt_gravar");
	              	//visaoJsfUtil.hideAction(PlcConstants.ACAO.EVT_GRAVAR);
			//contextUtil.getRequest().setAttribute( PlcConstants.ACAO.EXIBE_BT_GRAVAR, PlcConstants.NAO_EXIBIR);
		}
	}
		
}
