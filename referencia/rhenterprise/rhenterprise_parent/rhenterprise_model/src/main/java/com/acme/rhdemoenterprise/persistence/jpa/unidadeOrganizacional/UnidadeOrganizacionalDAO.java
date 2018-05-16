package com.acme.rhdemoenterprise.persistence.jpa.unidadeOrganizacional;

import java.util.List;

import com.acme.rhdemoenterprise.entity.UnidadeOrganizacional;
import com.acme.rhdemoenterprise.entity.UnidadeOrganizacionalEntity;
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

@PlcAggregationDAOIoC(UnidadeOrganizacionalEntity.class)
@SPlcDataAccessObject
@PlcQueryService
public class UnidadeOrganizacionalDAO extends AppJpaDAO  {

	public native List<UnidadeOrganizacionalEntity> findList(
			PlcBaseContextVO context,
			@PlcQueryOrderBy String dynamicOrderByPlc,
			@PlcQueryFirstLine Integer primeiraLinhaPlc, 
			@PlcQueryLineAmount Integer numeroLinhasPlc,		   
			
			@PlcQueryParameter(name="nome", expression="obj.nome like '%' || :nome ") String nome,
			@PlcQueryParameter(name="unidadePai", expression="obj0.unidadePai = :unidadePai") UnidadeOrganizacional unidadePai
	);

	@PlcQuery("querySel")
	public native Long findCount(
			PlcBaseContextVO context,
			@PlcQueryParameter(name="nome", expression="obj.nome like '%' || :nome ") String nome,
			@PlcQueryParameter(name="unidadePai", expression="obj0.unidadePai = :unidadePai") UnidadeOrganizacional unidadePai
	);
	
}
