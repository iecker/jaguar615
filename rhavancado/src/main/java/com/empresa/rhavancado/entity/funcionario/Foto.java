/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/
package com.empresa.rhavancado.entity.funcionario;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.powerlogic.jcompany.commons.PlcFileContent;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import com.powerlogic.jcompany.commons.file.PlcBaseMapFile;

/**
 * Arquivo de Fotos
 */
@SuppressWarnings("serial")
@SPlcEntity
@Entity
@Table(name="FOTO")
@SequenceGenerator(name="SE_ARQUIVO_PLC",sequenceName="SE_FOTO_PLC")
@NamedQuery(name="Foto.querySelLookup",query="select id as id, nome as nome from Foto where id = ?")
public class Foto extends PlcBaseMapFile {

	@OneToOne(targetEntity=FotoConteudo.class, fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	public PlcFileContent getBinaryContent() {
    	return( conteudo );
	}
}
