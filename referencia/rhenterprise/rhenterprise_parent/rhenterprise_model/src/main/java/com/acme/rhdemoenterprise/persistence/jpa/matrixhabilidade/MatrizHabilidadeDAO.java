package com.acme.rhdemoenterprise.persistence.jpa.matrixhabilidade;

import java.util.List;

import com.acme.rhdemoenterprise.entity.Funcionario;
import com.acme.rhdemoenterprise.entity.Habilidade;
import com.acme.rhdemoenterprise.entity.MatrizHabilidadeEntity;
import com.acme.rhdemoenterprise.persistence.jpa.AppJpaDAO;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationDAOIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcDataAccessObject;
import com.powerlogic.jcompany.persistence.jpa.PlcQuery;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryFirstLine;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryLineAmount;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryOrderBy;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryParameter;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryService;

/**
 * Classe de PersistÃªncia gerada pelo assistente
 */

@PlcAggregationDAOIoC(MatrizHabilidadeEntity.class)
@SPlcDataAccessObject
@PlcQueryService
public class MatrizHabilidadeDAO extends AppJpaDAO  {

	@PlcQuery("querySel2")
	public native List<MatrizHabilidadeEntity> findList(
			PlcBaseContextVO context,
			@PlcQueryParameter(name="funcionario", expression="obj.funcionario = :funcionario") Funcionario funcionario,
			@PlcQueryParameter(name="habilidade", expression="obj.habilidade = :habilidade") Habilidade habilidade,
			@PlcQueryOrderBy String dynamicOrderByPlc,
			@PlcQueryFirstLine Integer primeiraLinhaPlc, 
			@PlcQueryLineAmount Integer numeroLinhasPlc
	);

	@PlcQuery("querySel2")
	public native Long findCount(
			PlcBaseContextVO context,
			@PlcQueryParameter(name="funcionario", expression="obj.funcionario = :funcionario") Funcionario funcionario,
			@PlcQueryParameter(name="habilidade", expression="obj.habilidade = :habilidade") Habilidade habilidade
	);
	
	
	
}
