package com.empresa.rhtutorial2.model;

import java.math.BigDecimal;

import javax.inject.Inject;

import com.empresa.rhtutorial2.entity.funcionario.Funcionario;
import com.empresa.rhtutorial2.entity.funcionario.FuncionarioEntity;
import com.empresa.rhtutorial2.entity.funcionario.pendencia.RegraSalarioMinimoPorDependente;
import com.empresa.rhtutorial2.persistence.jpa.funcionario.FuncionarioDAO;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcRepository;
import com.powerlogic.jcompany.model.PlcBaseRepository;

@SPlcRepository
@PlcAggregationIoC(clazz = FuncionarioEntity.class)
public class FuncionarioRepository extends PlcBaseRepository {

	@Inject
	RegraSalarioMinimoPorDependente controlarSalarioMinimoPorDependente;

	@Inject
	FuncionarioDAO funcionarioDAO;

	@Override
	public Object insert(PlcBaseContextVO context, Object entidade)
			throws PlcException, Exception {

		Funcionario funcionario = (Funcionario) super.insert(context, entidade);

		BigDecimal valorPendencia = controlarSalarioMinimoPorDependente
				.calculaPendencia(funcionario.obtemSalarioAtual(),
						funcionario.obtemTotalDependentes());

		if (valorPendencia.longValue() < 0)
			funcionarioDAO.criaPendencia(context, funcionario,
					"SALARIO_MIN_POR_DEPENDENTE", valorPendencia);

		return funcionario;
	}

	@Override
	public Object update (PlcBaseContextVO context, Object entidade) {

		Funcionario funcionario = (Funcionario) super.update(context, entidade);

		BigDecimal valorPendencia = controlarSalarioMinimoPorDependente
				.calculaPendencia(funcionario.obtemSalarioAtual(),
						funcionario.obtemTotalDependentes());

		if (valorPendencia.longValue() < 0) {
			funcionarioDAO.atualizaPendencia(context, funcionario, "SALARIO_MIN_POR_DEPENDENTE", valorPendencia);
		} else {
			funcionarioDAO.excluiPendencia(context, funcionario);
		}
		return funcionario;
	}
	
	@Override
	public void delete(PlcBaseContextVO context, Object entidade) {
		super.delete(context, entidade);
		funcionarioDAO.excluiPendencia(context,(Funcionario)entidade);
	}

}
