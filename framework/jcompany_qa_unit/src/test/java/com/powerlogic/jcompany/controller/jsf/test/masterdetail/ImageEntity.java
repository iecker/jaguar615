/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.test.masterdetail;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import com.powerlogic.jcompany.commons.file.PlcBaseMapFile;

/**
 * Arquivo de Foto de Funcionário
 */
@SPlcEntity
@SuppressWarnings("serial")
@Entity
@Table(name="ARQUIVO_FOTO")
@SequenceGenerator(name="SE_ARQUIVO_PLC",sequenceName="SE_ARQUIVO_PLC")
public class ImageEntity extends PlcBaseMapFile {

	
}
