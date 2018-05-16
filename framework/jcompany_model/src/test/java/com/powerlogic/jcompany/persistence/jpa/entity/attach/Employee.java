/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.persistence.jpa.entity.attach;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.ForeignKey;

import com.powerlogic.jcompany.config.domain.PlcFileAttach;



/**
 * Funcionário
 */
@MappedSuperclass

public abstract class Employee implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_FUNCIONARIO")
	@Column (name = "ID_FUNCIONARIO", nullable=false, length=5)
	private Long id;

	@Column (name = "NOME", nullable=false, length=40)
	private String nome;
	
	@ManyToOne(targetEntity=ImageEntity.class,fetch=FetchType.LAZY)
	@ForeignKey(name="FK_FUNCIONARIO_FOTO")
	@JoinColumn(name="ID_FOTO",nullable=true)
    @PlcFileAttach(extension={"txt","doc","pdf"})
	private ImageEntity arquivoAnexado;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setArquivoAnexado(ImageEntity arquivoAnexado) {
		this.arquivoAnexado = arquivoAnexado;
	}

	public ImageEntity getArquivoAnexado() {
		return arquivoAnexado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}
	

}