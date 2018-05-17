package com.empresa.rhtutorial2.controller.jsf.funcionario;

import java.math.BigDecimal;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.empresa.rhtutorial2.controller.jsf.AppMB;
import com.empresa.rhtutorial2.entity.funcionario.Funcionario;
import com.empresa.rhtutorial2.entity.funcionario.FuncionarioEntity;
import com.empresa.rhtutorial2.entity.funcionario.HistoricoProfissional;
import com.empresa.rhtutorial2.entity.funcionario.HistoricoProfissionalEntity;
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
	private BigDecimal mediaSalarial = new BigDecimal(0);
	
	public BigDecimal getMediaSalarial() {
		return mediaSalarial;
	}
 
	public void setMediaSalarial(BigDecimal mediaSalarial) {
		this.mediaSalarial = mediaSalarial;
	}	
     		
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
	
	public String eventoCalculaMediaSalario(){
		setMediaSalarial(new BigDecimal(0));
		FuncionarioEntity func = (FuncionarioEntity)this.entityPlc;
		if (func.getHistoricoProfissional() != null) {
			HistoricoProfissionalEntity hist;
			BigDecimal somaSalario = new BigDecimal(0);
			int qtd = 0;
			for (HistoricoProfissional historico : func.getHistoricoProfissional()) {
				hist = (HistoricoProfissionalEntity)historico;
				if(!"S".equals(hist.getIndExcPlc()) && hist.getSalario()!= null){
					somaSalario = somaSalario.add(hist.getSalario());
					qtd++;
				}
			}
			if(qtd!=0){
				setMediaSalarial(somaSalario.divide(new BigDecimal(qtd)));
			}
		}
		//msgUtil.msg("Média salarial calculada! Verifique a aba Histórico Profissional!", PlcMessageCor.msgAzulPlc.toString());
		return "";
	}	
		
}
