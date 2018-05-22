package com.powerlogic.rhavancado.persistence.jpa.funcionario;

import com.powerlogic.rhavancado.persistence.jpa.AppJpaDAO;
import com.powerlogic.rhavancado.entity.funcionario.FuncionarioEntity;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryParameter;
import com.powerlogic.rhavancado.entity.EstadoCivil;
import java.util.Date;

import java.util.List;

import com.powerlogic.jcompany.persistence.jpa.PlcQuery;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryLineAmount;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryOrderBy;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryFirstLine;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationDAOIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcDataAccessObject;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryService;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
/**
 * Classe de Persistência gerada pelo assistente
 */

@PlcAggregationDAOIoC(FuncionarioEntity.class)
@SPlcDataAccessObject
@PlcQueryService
public class FuncionarioDAO extends AppJpaDAO  {

	@PlcQuery("querySel")
	public native List<FuncionarioEntity> findList(
			PlcBaseContextVO context,
			@PlcQueryOrderBy String dynamicOrderByPlc,
			@PlcQueryFirstLine Integer primeiraLinhaPlc, 
			@PlcQueryLineAmount Integer numeroLinhasPlc,		   
			
			@PlcQueryParameter(name="id", expression="id = :id") Long id,
			@PlcQueryParameter(name="nome", expression="nome like :nome || '%' ") String nome,
			@PlcQueryParameter(name="estadoCivil", expression="estadoCivil = :estadoCivil") EstadoCivil estadoCivil,
			@PlcQueryParameter(name="dataNascimento", expression="dataNascimento >= :dataNascimento  ") Date dataNascimento
	);

	@PlcQuery("querySel")
	public native Long findCount(
			PlcBaseContextVO context,
			
			@PlcQueryParameter(name="id", expression="id = :id") Long id,
			@PlcQueryParameter(name="nome", expression="nome like :nome || '%' ") String nome,
			@PlcQueryParameter(name="estadoCivil", expression="estadoCivil = :estadoCivil") EstadoCivil estadoCivil,
			@PlcQueryParameter(name="dataNascimento", expression="dataNascimento >= :dataNascimento  ") Date dataNascimento
	);
	
}
