/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.commons.metamodel;

import java.util.Collection;
import java.util.Set;

import javax.enterprise.event.Observes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.powerlogic.jcompany.commons.config.metamodel.PlcBusinessObject;
import com.powerlogic.jcompany.commons.config.metamodel.PlcDataAccessObject;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntity;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.metamodel.bootstrap.PlcMetamodelUtilBootstrapAfter;
import com.powerlogic.jcompany.commons.metamodel.bootstrap.PlcMetamodelUtilBootstrapBefore;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;

public class PlcMetamodelUtilInfo {

	private final Logger log = LoggerFactory.getLogger(PlcMetamodelUtilInfo.class);

	public void process(@Observes PlcMetamodelUtilBootstrapBefore evento, @QPlcDefault PlcMetamodelUtil metamodel) {
		log.debug( "PlcMetamodelUtilBootstrapAntes...");

	}
	
	public void process(@Observes PlcMetamodelUtilBootstrapAfter evento, @QPlcDefault PlcMetamodelUtil metamodel) {
		log.debug( "PlcMetamodelUtilBootstrapApos...");

		dumpPlcEntities(metamodel);
		
		dumpPlcBusinessObjects(metamodel);
		
		dumpPlcDataAccessObjects(metamodel);
		
	}
	
	private void dumpPlcEntities(PlcMetamodelUtil metamodel) {
		Collection<PlcEntity<?>> entidades = metamodel.getEntities();
		
		log.debug( "-- PlcEntities (total {}) -- ", entidades.size());		
		for (PlcEntity<?> entidade : entidades) {
			log.debug( "  PlcEntity ({}) ", entidade.getEntityClass());
			log.debug( "    idAtributo                    : {}", entidade.getIdAtributo());
			log.debug( "    isIdNatural                   : {}", entidade.isIdNatural());
			log.debug( "    rowIdAtributo                 : {}", entidade.getRowIdAtributo());
			log.debug( "    versaoAtributo                : {}", entidade.getVersaoAtributo());
			log.debug( "    hashCodePlc                   : {}", entidade.getHashCodePlcAtributo());
			log.debug( "    dataUltimaAlteracaoAtributo   : {}", entidade.getDataUltimaAlteracaoAtributo());
			log.debug( "    usuarioUltimaAlteracaoAtributo: {}", entidade.getUsuarioUltimaAlteracaoAtributo());
			log.debug( "    indExcPlcAtributo             : {}", entidade.getIndExcPlcAtributo());			
			log.debug( "    nomesAtributos                : {}", entidade.getNomesAtributos());			
			log.debug( "    BusinessObject                : {}", entidade.getBusinessObject().getBusinessObjectClass());
			log.debug( "    DataAccessObject              : {}", entidade.getDataAccessObject().getDataAcessObjectClass());
			log.debug( "                                      ");
		}
		log.debug( "-- /PlcEntities (total {}) -- ", entidades.size());
	}


	private void dumpPlcBusinessObjects(PlcMetamodelUtil metamodel) {
		Collection<PlcBusinessObject<?>> businessObjects = metamodel.getBusinessObjects();
		
		log.debug( "-- PlcBusinessObjects (total {}) -- ", businessObjects.size());		
		for (PlcBusinessObject<?> businessObject : businessObjects) {
			log.debug( " PlcBusinessObject({})", businessObject.getBusinessObjectClass());
			log.debug( "   PlcEntidades antendidas (total {}): ", businessObject.getEntidades()!=null?businessObject.getEntidades().size():0);	
			Set<? extends PlcEntity<?>> businessObjectEntidades = businessObject.getEntidades();
			if (businessObjectEntidades!=null)
				for (PlcEntity<?> facadeEntidade : businessObjectEntidades) {
					log.debug( "     {}", facadeEntidade.getEntityClass());			
				}
		}
		log.debug( "-- /PlcBusinessObjects (total {}) -- ", businessObjects.size());				
	}

	private void dumpPlcDataAccessObjects(PlcMetamodelUtil metamodel) {
		Collection<PlcDataAccessObject<?>> dataAccessObjects = metamodel.getDataAccessObjects();

		log.debug( "-- DataAccessObjects (total {}) -- ", dataAccessObjects.size());				
		for (PlcDataAccessObject<?> dataAccessObject : dataAccessObjects) {
			log.debug( " PlcDataAccessObject({}) ", dataAccessObject.getDataAcessObjectClass());
			log.debug( "   PlcEntidades antendidas (total {}): ", dataAccessObject.getEntidades()!=null?dataAccessObject.getEntidades().size():0);		
			Set<? extends PlcEntity<?>> dataAccessObjectEntidades = dataAccessObject.getEntidades();
			if (dataAccessObjectEntidades!=null)
				for (PlcEntity<?> dataAccessObjectEntidade : dataAccessObjectEntidades) {
					log.debug( "     {}", dataAccessObjectEntidade.getEntityClass());	
				}
		}
		log.debug( "-- /DataAccessObjects (total {}) -- ", dataAccessObjects.size());				
	}
}