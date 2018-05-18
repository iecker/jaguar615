package com.empresa.rhavancado.controller.jsf.funcionario;

import java.math.BigDecimal;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.empresa.cargosalario.facade.ICargoService;
import com.empresa.rhavancado.controller.jsf.AppMB;
import com.empresa.rhavancado.entity.FuncionarioEntity;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcConversationControl;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.controller.jsf.util.PlcCreateContextUtil;

@PlcConfigAggregation(entity = com.empresa.rhavancado.entity.FuncionarioEntity.class

)
@PlcConfigForm(

formPattern = FormPattern.Man, formLayout = @PlcConfigFormLayout(dirBase = "/WEB-INF/fcls")

)
/**
 * Classe de Controle gerada pelo assistente
 */
@SPlcMB
@PlcUriIoC("funcionario")
@PlcHandleException
public class FuncionarioMB extends AppMB {

	private static final long serialVersionUID = 1L;

	@Inject
	protected ICargoService cargoService;

	@Inject
	@Named(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_CONVERSACAO)
	protected PlcConversationControl plcControleConversacao;

	@Inject
	@QPlcDefault
	protected PlcCreateContextUtil contextMontaUtil;

	/**
	 * Entidade da ação injetado pela CDI
	 */
	@Produces
	@Named("funcionario")
	public FuncionarioEntity createEntityPlc() {

		if (this.entityPlc == null) {
			this.entityPlc = new FuncionarioEntity();
			this.newEntity();
			PlcBaseContextVO context = contextMontaUtil
					.createContextParam(plcControleConversacao);
			BigDecimal valor = cargoService.salarioPorCargo(context,
					((FuncionarioEntity) this.entityPlc).getCodigoCargo());
			((FuncionarioEntity) this.entityPlc).setValorSalario(valor);
		}

		return (FuncionarioEntity) this.entityPlc;
	}

}
