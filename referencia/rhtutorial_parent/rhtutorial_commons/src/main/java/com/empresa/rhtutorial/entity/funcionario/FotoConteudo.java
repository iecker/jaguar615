/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/
package com.empresa.rhtutorial.entity.funcionario;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import com.powerlogic.jcompany.commons.file.PlcBaseMapFileContent;

/**
 * Arquivo de conteÃºdos de fotos
 */
@SuppressWarnings("serial")
@SPlcEntity
@Entity
@Table(name="FOTO_CONTEUDO")
@SequenceGenerator(name="SE_ARQUIVO_CONTEUDO_PLC",sequenceName="SE_FOTO_CONTEUDO_PLC")
public class FotoConteudo extends PlcBaseMapFileContent {

}
