package com.empresa.rhtutorial.persistence.jpa.unidadeOrganizacional;

import com.empresa.rhtutorial.persistence.jpa.AppJpaDAO;
import com.empresa.rhtutorial.entity.UnidadeOrganizacionalEntity;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryParameter;

import java.util.List;

import com.powerlogic.jcompany.persistence.jpa.PlcQuery;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryLineAmount;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryOrderBy;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryFirstLine;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationDAOIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcDataAccessObject;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryService;

/**
 * Classe de PersistÃªncia gerada pelo assistente
 */

@PlcAggregationDAOIoC(UnidadeOrganizacionalEntity.class)
@SPlcDataAccessObject
@PlcQueryService
public class UnidadeOrganizacionalDAO extends AppJpaDAO  {

	@PlcQuery("querySel")
	public native List<UnidadeOrganizacionalEntity> findList(
			PlcBaseContextVO context,
			@PlcQueryOrderBy String dynamicOrderByPlc,
			@PlcQueryFirstLine Integer primeiraLinhaPlc, 
			@PlcQueryLineAmount Integer numeroLinhasPlc,		   
			
			@PlcQueryParameter(name="nome", expression="nome like :nome || '%' ") String nome,
			@PlcQueryParameter(name="endereco_logradouro", 
					expression="endereco.logradouro like :endereco_logradouro || '%' ") String endereco_logradouro
	);

	@PlcQuery("querySel")
	public native Long findCount(
			PlcBaseContextVO context,
			@PlcQueryParameter(name="nome", expression="nome like :nome || '%' ") String nome,
			@PlcQueryParameter(name="endereco_logradouro", 
					expression="endereco.logradouro like :endereco_logradouro || '%' ") String endereco_logradouro
	);
	
}
