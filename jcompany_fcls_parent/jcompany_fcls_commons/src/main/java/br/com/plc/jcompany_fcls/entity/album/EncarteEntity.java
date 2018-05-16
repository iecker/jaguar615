/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.album;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.powerlogic.jcompany.commons.PlcFileContent;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import com.powerlogic.jcompany.commons.file.PlcBaseMapFile;

@SPlcEntity
@Entity
@javax.persistence.Table(name="ARQUIVO_ENCARTE")
@SequenceGenerator(name="SE_ARQUIVO_PLC", sequenceName="SE_ARQUIVO_PLC")
@SuppressWarnings("serial")
public class EncarteEntity extends PlcBaseMapFile {

	@OneToOne(targetEntity=EncarteConteudoEntity.class, fetch=FetchType.LAZY)
	@Cascade({CascadeType.ALL})
    public PlcFileContent getBinaryContent() {
    	return( conteudo );
    }	
	
}
