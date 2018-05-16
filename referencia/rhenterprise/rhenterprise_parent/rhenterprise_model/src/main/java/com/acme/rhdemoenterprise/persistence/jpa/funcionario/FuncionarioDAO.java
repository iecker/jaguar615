package com.acme.rhdemoenterprise.persistence.jpa.funcionario;

import java.util.List;

import com.acme.rhdemoenterprise.entity.EstadoCivil;
import com.acme.rhdemoenterprise.entity.FuncionarioEntity;
import com.acme.rhdemoenterprise.entity.UnidadeOrganizacional;
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
			
			@PlcQueryParameter(name="id", expression="obj.id = :id ") Long id,
			@PlcQueryParameter(name="nome", expression="obj.nome like '%' || :nome ") String nome,
			@PlcQueryParameter(name="dataNascimento", expression="obj.dataNascimento >= :dataNascimento  ") java.util.Date dataNascimento,
			@PlcQueryParameter(name="cpf", expression="obj.cpf = :cpf") String cpf,
			@PlcQueryParameter(name="estadoCivil", expression="obj.estadoCivil = :estadoCivil") EstadoCivil estadoCivil,
			@PlcQueryParameter(name ="categoriaSalarial", expression=" obj.categoriaSalarial= :categoriaSalarial") String categoriaSalarial,
			@PlcQueryParameter(name="unidadeOrganizacional", expression="obj0.unidadeOrganizacional = :unidadeOrganizacional") UnidadeOrganizacional unidadeOrganizacional,
			@PlcQueryParameter(name="enderecoResidencial_logradouro", expression="enderecoResidencial.logradouro like '%' || :enderecoResidencial_logradouro ") String enderecoResidencial_logradouro
	);

	@PlcQuery("querySel")
	public native Long findCount(
			PlcBaseContextVO context,
			@PlcQueryParameter(name="id", expression="obj.id = :id ") Long id,
			@PlcQueryParameter(name="nome", expression="obj.nome like '%' || :nome ") String nome,
			@PlcQueryParameter(name="dataNascimento", expression="obj.dataNascimento >= :dataNascimento  ") java.util.Date dataNascimento,
			@PlcQueryParameter(name="cpf", expression="obj.cpf = :cpf") String cpf,
			@PlcQueryParameter(name="estadoCivil", expression="obj.estadoCivil = :estadoCivil") EstadoCivil estadoCivil,
			@PlcQueryParameter(name ="categoriaSalarial", expression=" obj.categoriaSalarial= :categoriaSalarial") String categoriaSalarial,
			@PlcQueryParameter(name="unidadeOrganizacional", expression="obj0.unidadeOrganizacional = :unidadeOrganizacional") UnidadeOrganizacional unidadeOrganizacional,
			@PlcQueryParameter(name="enderecoResidencial_logradouro", expression="enderecoResidencial.logradouro like '%' || :enderecoResidencial_logradouro ") String enderecoResidencial_logradouro
	);
	
	
	@PlcQuery("querySelMarcados")
	public native List<FuncionarioEntity> findList(
			PlcBaseContextVO context,
			@PlcQueryParameter(name="id", expression="id = :id") Long id,
			@PlcQueryParameter(name="nome", expression="upper(nome) like '%' || upper(:nome) || '%'") String nome
	);
	
	@PlcQuery("querySelDesmarcados")
	public native List<FuncionarioEntity> findList(
			PlcBaseContextVO context,
			@PlcQueryParameter(name="id", expression="id = :id") Long id
			
	);
	
}
