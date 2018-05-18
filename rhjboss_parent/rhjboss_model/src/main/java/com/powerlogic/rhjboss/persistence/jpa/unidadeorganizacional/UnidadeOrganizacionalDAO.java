package com.powerlogic.rhjboss.persistence.jpa.unidadeorganizacional;

import com.powerlogic.rhjboss.persistence.jpa.AppJpaDAO;
import com.powerlogic.rhjboss.entity.UnidadeOrganizacionalEntity;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryParameter;
import com.powerlogic.rhjboss.entity.UnidadeOrganizacional;

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
			
			@PlcQueryParameter(name="id", expression="obj.id = :id") Long id,
			@PlcQueryParameter(name="nome", expression="obj.nome like :nome || '%' ") String nome,
			@PlcQueryParameter(name="unidadePai", expression="obj1 = :unidadePai") UnidadeOrganizacional unidadePai
	);

	@PlcQuery("querySel")
	public native Long findCount(
			PlcBaseContextVO context,
			
			@PlcQueryParameter(name="id", expression="obj.id = :id") Long id,
			@PlcQueryParameter(name="nome", expression="obj.nome like :nome || '%' ") String nome,
			@PlcQueryParameter(name="unidadePai", expression="obj1 = :unidadePai") UnidadeOrganizacional unidadePai
	);
	
}
