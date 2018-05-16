/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.file;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.powerlogic.jcompany.commons.PlcFileContent;

/**
 * Ancestral para Conteúdo de Arquivo Anexado. Manter binários em estruturas separadas evita problemas com "download invisível"
 * e isola estes  volumes em tabelas separadas.
 */
@MappedSuperclass
public abstract class PlcBaseMapFileContent extends PlcFileContent {

	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_ARQUIVO_CONTEUDO_PLC")
	@Column (name = "ID")
	public Long getId() {
		return id;
	}
	
	@Column(name = "CONTEUDO_BINARIO", length = Integer.MAX_VALUE)
	public byte[] getBinaryContent() {
	return binaryContent;
	}
}
