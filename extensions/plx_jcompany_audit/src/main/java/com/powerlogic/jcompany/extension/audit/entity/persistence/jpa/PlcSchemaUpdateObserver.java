package com.powerlogic.jcompany.extension.audit.entity.persistence.jpa;

import javax.enterprise.event.Observes;

import org.hibernate.cfg.Configuration;
import org.hibernate.envers.configuration.AuditConfiguration;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.model.bindingtype.PlcCreateConfigurationAfter;

public class PlcSchemaUpdateObserver {

	
	/**
	 * Metodo responsavel por configurar a geração de DDL com Envers...
	 * @param Configuration hbmcfg
	 * @throws PlcException
	 */
	public void configureSchemaWithEnvers(@Observes @PlcCreateConfigurationAfter Configuration hbmcfg) throws PlcException {

		hbmcfg.buildMappings();
		
		AuditConfiguration.getFor(hbmcfg);

	}
}
