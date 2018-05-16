package com.empresa.rhtutorial.persistence.jpa.funcionario;

import java.util.Date;
import java.util.List;

import com.empresa.rhtutorial.entity.UnidadeOrganizacional;
import com.empresa.rhtutorial.entity.funcionario.EstadoCivil;
import com.empresa.rhtutorial.entity.funcionario.FuncionarioEntity;
import com.empresa.rhtutorial.persistence.jpa.AppJpaDAO;
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
			
			@PlcQueryParameter(name="nome", expression="obj.nome like :nome || '%' ") String nome,
			@PlcQueryParameter(name="dataNascimentoMin", expression="obj.dataNascimento <= :dataNascimentoMin  ") Date dataNascimentoMin,
			@PlcQueryParameter(name="dataNascimentoMax", expression="obj.dataNascimento >= :dataNascimentoMax  ") Date dataNascimentoMax,	
			@PlcQueryParameter(name="cpf", expression="obj.cpf like :cpf || '%' ") String cpf,
			@PlcQueryParameter(name="estadoCivil", expression="obj.estadoCivil = :estadoCivil") EstadoCivil estadoCivil,
			@PlcQueryParameter(name="unidadeOrganizacional", expression="obj1 = :unidadeOrganizacional") UnidadeOrganizacional unidadeOrganizacional,
			@PlcQueryParameter(name="enderecoResidencial_logradouro", expression="obj.enderecoResidencial.logradouro like :enderecoResidencial_logradouro || '%' ") String enderecoResidencial_logradouro
	);

	@PlcQuery("querySel")
	public native Long findCount(
			PlcBaseContextVO context,
			@PlcQueryParameter(name="nome", expression="obj.nome like :nome || '%' ") String nome,
			@PlcQueryParameter(name="dataNascimentoMin", expression="obj.dataNascimento <= :dataNascimentoMin  ") Date dataNascimentoMin,
			@PlcQueryParameter(name="dataNascimentoMax", expression="obj.dataNascimento >= :dataNascimentoMax  ") Date dataNascimentoMax,	
			@PlcQueryParameter(name="cpf", expression="obj.cpf like :cpf || '%' ") String cpf,
			@PlcQueryParameter(name="estadoCivil", expression="obj.estadoCivil = :estadoCivil") EstadoCivil estadoCivil,
			@PlcQueryParameter(name="unidadeOrganizacional", expression="obj1 = :unidadeOrganizacional") UnidadeOrganizacional unidadeOrganizacional,
			@PlcQueryParameter(name="enderecoResidencial_logradouro", expression="obj.enderecoResidencial.logradouro like :enderecoResidencial_logradouro || '%' ") String enderecoResidencial_logradouro
	);
	
}
