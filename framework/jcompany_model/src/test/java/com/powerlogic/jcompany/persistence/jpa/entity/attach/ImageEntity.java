/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.persistence.jpa.entity.attach;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.powerlogic.jcompany.commons.PlcFileContent;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import com.powerlogic.jcompany.commons.file.PlcBaseMapFile;

/**
 * Arquivo de Fotos
 */
@SPlcEntity
@Entity
@Table(name="TEST_ARQUIVO_FOTO")
@SequenceGenerator(name="SE_ARQUIVO_PLC",sequenceName="TEST_SE_ARQUIVO_PLC")
public class ImageEntity  extends PlcBaseMapFile{

	@OneToOne(targetEntity=ImageContentEntity.class, fetch=FetchType.LAZY)
	@Cascade({CascadeType.ALL})
    public PlcFileContent getBinaryContent() {
    	return( conteudo );
    }
	
}
