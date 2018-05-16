package com.empresa.rhtutorial.entity;


import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;

import com.powerlogic.jcompany.domain.validation.PlcValExactSize;
import com.powerlogic.jcompany.domain.validation.PlcValSimpleFormat;
import com.powerlogic.jcompany.domain.validation.PlcValSimpleFormat.SimpleFormat;

@Embeddable
@Access(AccessType.FIELD)
@NamedQueries({
	@NamedQuery(name="Endereco.querySelLookup", query="select logradouro as logradouro from Endereco where id = ? order by logradouro asc")
})
public class Endereco  implements Serializable {

	@NotNull @Size(max = 40) @Column
	private String logradouro;
	
	@NotNull @Size(max = 15) @Column
	private String numero;
	
	@NotNull @Size(max = 30) @Column
	private String bairro;	
	
	@PlcValExactSize(size=8)
	@PlcValSimpleFormat(format=SimpleFormat.NUMBER)
	@Size(max = 8) @Column
	private String cep;
	
	@ManyToOne (targetEntity = UfEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_ENDERECO_UF")
	@NotNull @JoinColumn
	private Uf uf;
	
	@ManyToOne (targetEntity = MunicipioEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_ENDERECO_MUNICIPIO")
	@NotNull @JoinColumn
	private Municipio municipio;

	@Override
	public String toString() {
		return getLogradouro();
	}
	
	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public Endereco() { }
	
	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro=logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero=numero;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro=bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep=cep;
	}

	public Uf getUf() {
		return uf;
	}

	public void setUf(Uf uf) {
		this.uf=uf;
	}


}
