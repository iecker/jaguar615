/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.album;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import com.powerlogic.jcompany.commons.file.PlcBaseMapFileContent;

@SPlcEntity
@Entity
@javax.persistence.Table(name="ARQUIVO_ENCARTE_CONTEUDO")
@SequenceGenerator(name="SE_ARQUIVO_CONTEUDO_PLC", sequenceName="SE_ARQUIVO_CONTEUDO_PLC")
@SuppressWarnings("serial")
public class EncarteConteudoEntity extends PlcBaseMapFileContent {

}
