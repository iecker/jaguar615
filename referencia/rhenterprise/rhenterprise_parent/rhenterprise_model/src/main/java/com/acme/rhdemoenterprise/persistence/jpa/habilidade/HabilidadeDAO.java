package com.acme.rhdemoenterprise.persistence.jpa.habilidade;

import java.util.List;

import com.acme.rhdemoenterprise.entity.HabilidadeEntity;
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

@PlcAggregationDAOIoC(HabilidadeEntity.class)
@SPlcDataAccessObject
@PlcQueryService
public class HabilidadeDAO extends AppJpaDAO  {

	@PlcQuery("querySel")
	public native List<HabilidadeEntity> findList(
			PlcBaseContextVO context,
			@PlcQueryOrderBy String dynamicOrderByPlc,
			@PlcQueryFirstLine Integer primeiraLinhaPlc, 
			@PlcQueryLineAmount Integer numeroLinhasPlc,
			
			@PlcQueryParameter(name="id", expression="id = :id") Long id
	);
	
	@PlcQuery("querySelMarcados")
	public native List<HabilidadeEntity> findList(
			PlcBaseContextVO context,
			@PlcQueryParameter(name="id", expression="id = :id") Long id
	);
	
	@PlcQuery("querySelDesmarcados")
	public native List<HabilidadeEntity> findList(
			PlcBaseContextVO context,
			@PlcQueryParameter(name="id", expression="id = :id") Long id,
			@PlcQueryParameter(name="descricao", expression="descricao = :descricao") String descricao
	);

	
}
