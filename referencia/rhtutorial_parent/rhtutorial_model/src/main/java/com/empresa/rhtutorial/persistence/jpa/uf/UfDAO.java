package com.empresa.rhtutorial.persistence.jpa.uf;

import com.empresa.rhtutorial.entity.UfEntity;
import com.empresa.rhtutorial.persistence.jpa.AppJpaDAO;


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
