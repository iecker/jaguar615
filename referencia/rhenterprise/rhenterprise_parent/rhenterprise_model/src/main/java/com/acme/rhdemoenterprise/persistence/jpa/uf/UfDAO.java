package com.acme.rhdemoenterprise.persistence.jpa.uf;

import com.acme.rhdemoenterprise.entity.UfEntity;
import com.acme.rhdemoenterprise.persistence.jpa.AppJpaDAO;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationDAOIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcDataAccessObject;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryService;

/**
 * Classe de PersistÃªncia gerada pelo assistente
 */

@PlcAggregationDAOIoC(UfEntity.class)
@SPlcDataAccessObject
@PlcQueryService
public class UfDAO extends AppJpaDAO  {

	
}
