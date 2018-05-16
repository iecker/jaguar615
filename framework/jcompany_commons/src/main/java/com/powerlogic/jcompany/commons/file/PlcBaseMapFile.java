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
import javax.persistence.Version;

import com.powerlogic.jcompany.commons.PlcFile;

/**
 * Ancestral para Arquivos Anexados. 
 */
@MappedSuperclass
public abstract class PlcBaseMapFile extends PlcFile {

	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_ARQUIVO_PLC") @Column (name = "ID")
	public Long getId() {
		return id;
	}
	@Version @Column (name = "VERSAO")
	public int getVersao() {
		return super.getVersao();
	}
	@Column (name = "DAT_SEGURANCA")
	public java.util.Date getDataUltAlteracao() {
		return super.getDataUltAlteracao();
	}
	@Column (name = "USU_SEGURANCA")
	public String getUsuarioUltAlteracao() {
		return super.getUsuarioUltAlteracao();
	}
	
	@Column (name = "NOME")
	public String getNome() {
		return super.getNome();
	}
	
	@Column (name = "TIPO")
    public String getType() {
    	return tipo;
    }

	@Column (name = "TAMANHO")
    public Integer getLength() {
    	return tamanho;
    }
}
