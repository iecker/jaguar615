package com.empresa.rhavancado.entity;

import java.beans.Transient;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;

@SPlcEntity
@Entity
@Table(name="UF")
@SequenceGenerator(name="SE_UF", sequenceName="SE_UF")
//@Access(AccessType.FIELD)
public class Uf implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_UF")
	private Long id;

	@NotNull
	@Size(max = 30)
	private String nome;

	@NotNull
	@Size(max = 2)
	private String sigla;

	public Uf() {
	}
	public Long getId() {
	return id;
	}

	public void setId(Long id) {
	this.id=id;
	}

	public String getNome() {
	return nome;
	}

	public void setNome(String nome) {
	this.nome=nome;
	}

	public String getSigla() {
	return sigla;
	}

	public void setSigla(String sigla) {
	this.sigla=sigla;
	}

	@Override
	public String toString() {
	return getNome() + "-" + getSigla();
	}

	//@Transient
	private String indExcPlc = "N";

	public void setIndExcPlc(String indExcPlc) {
	this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
	return indExcPlc;
	}
		
}
