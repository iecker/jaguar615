/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.model;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcSpecific;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.model.util.PlcIocModelUtil;

/**
 * Ancestral com métodos comuns para o PlcBaseRepository. Importante: esta é uma classe Stateless (ApplicationScoped), portanto não pode
 * conter variáveis de instância
 */
public abstract class PlcBaseParentRepository {

	@Inject
	protected transient Logger log;
	
 	/**
 	 * Classe util para tratamento de ioc na camada de modelo.
 	 */
	@Inject @QPlcDefault 
 	protected PlcIocModelUtil iocModelUtil;	
	
	@Inject @QPlcDefault 
	protected PlcReflectionUtil reflectionUtil;

	@Inject @QPlcDefault 
	protected PlcMetamodelUtil metamodelUtil;
	
	@Inject	@QPlcSpecific
	protected PlcBaseCRUDSRepository baseCRUDSRepository;
	
	
}
