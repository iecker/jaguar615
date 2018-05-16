package com.acme.planos_assist.persistence.jpa.pla.planos;

import java.util.List;

import com.acme.planos_assist.dominio.TipoPlanos;
import com.acme.planos_assist.entity.planos.PlanosEntity;
import com.acme.planos_assist.persistence.jpa.PlaJpaDAO;
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

@PlcAggregationDAOIoC(PlanosEntity.class)
@SPlcDataAccessObject
@PlcQueryService
public class PlanosDAO extends PlaJpaDAO  {

	@PlcQuery("querySel2")
	public native List<PlanosEntity> findList(
			PlcBaseContextVO context,
			@PlcQueryOrderBy String dynamicOrderByPlc,
			@PlcQueryFirstLine Integer primeiraLinhaPlc, 
			@PlcQueryLineAmount Integer numeroLinhasPlc,		   
			
			@PlcQueryParameter(name="nome", expression="upper(nome) like '%' || upper(:nome) || '%'") String nome,
			@PlcQueryParameter(name="tipoPlanos", expression="tipoPlanos = :tipoPlanos") TipoPlanos tipoPlanos
	);

	@PlcQuery("querySel2")
	public native Long findCount(
			PlcBaseContextVO context,
			@PlcQueryParameter(name="nome", expression="upper(nome) like '%' || upper(:nome) || '%'") String nome,
			@PlcQueryParameter(name="tipoPlanos", expression="tipoPlanos = :tipoPlanos") TipoPlanos tipoPlanos
	);
	
}
