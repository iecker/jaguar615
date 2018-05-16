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
 * Arquivo de conteÃºdos de currÃ­culos
 */
@SuppressWarnings("serial")
@SPlcEntity
@Entity
@Table(name="CURRICULO_CONTEUDO")
@SequenceGenerator(name="SE_ARQUIVO_CONTEUDO_PLC",sequenceName="SE_CURRICULO_CONTEUDO_PLC")
public class CurriculoConteudoEntity extends PlcBaseMapFileContent {

}

