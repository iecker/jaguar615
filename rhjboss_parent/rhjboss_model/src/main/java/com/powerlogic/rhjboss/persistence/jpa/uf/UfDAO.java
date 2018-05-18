package com.powerlogic.rhjboss.persistence.jpa.uf;

import javax.ejb.Stateless;

import com.powerlogic.jcompany.commons.annotation.PlcAggregationDAOIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcDataAccessObjectEjb;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryService;
import com.powerlogic.rhjboss.entity.UfEntity;
import com.powerlogic.rhjboss.persistence.jpa.AppJpaDAO;

/**
 * Classe de Persistência gerada pelo assistente
 */

@PlcAggregationDAOIoC(UfEntity.class)
@PlcQueryService
@SPlcDataAccessObjectEjb
@Stateless
public class UfDAO extends AppJpaDAO  {

	
}
