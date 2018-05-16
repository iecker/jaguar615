package com.acme.rhdemoenterprise.model;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.acme.modulo.rhdemoenterprise.negocio.entity.DadosProfional;
import com.acme.modulo.rhdemoenterprise.negocio.facade.IAcmFacade;
import com.acme.rhdemoenterprise.entity.FuncionarioEntity;
import com.acme.rhdemoenterprise.entity.HistoricoProfissional;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationDAOIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcRepository;
import com.powerlogic.jcompany.model.PlcBaseRepository;

@SPlcRepository
@PlcAggregationDAOIoC(value=FuncionarioEntity.class)
public class FuncionarioRepository extends PlcBaseRepository {
	
	@Inject
	IAcmFacade mediaSalarial;
	
	@Override
	public Object insert(PlcBaseContextVO context,Object entidade)  throws PlcException, Exception  {
		
		FuncionarioEntity funcionario = (FuncionarioEntity) entidade;
		
		incluirCategoriaFuncionario(funcionario);
		
		return super.insert(context, funcionario);
	}
	
	@Override
	public Object update(PlcBaseContextVO context, Object entidade) {
		
		FuncionarioEntity funcionario = (FuncionarioEntity) entidade;
		
		incluirCategoriaFuncionario(funcionario);
		
		return super.update(context, funcionario);
	}

	private void incluirCategoriaFuncionario(Object entidade) {
		
		FuncionarioEntity funcionario = (FuncionarioEntity) entidade;
		DadosProfional salarioProfisional = new DadosProfional();
		List<DadosProfional> listaProfissional = new ArrayList<DadosProfional>();
		
		if(funcionario.getHistoricoProfissional() != null){
			for (HistoricoProfissional historico : funcionario.getHistoricoProfissional()) {
				salarioProfisional.setSalario(historico.getSalario());
				listaProfissional.add(salarioProfisional);
			}
			
			String categoriaSalarial = mediaSalarial.retornaCategoriaSalario(listaProfissional);
			funcionario.setCategoriaSalarial(categoriaSalarial);
		}
		
	}
	
}
