package com.empresa.rhrich.entity;


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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;

import com.powerlogic.jcompany.domain.validation.PlcValSimpleFormat;
import com.powerlogic.jcompany.domain.validation.PlcValSimpleFormat.SimpleFormat;

@Embeddable
@Access(AccessType.FIELD)

@NamedQueries({
	@NamedQuery(name="Endereco.querySelLookup", query="select rua as rua from Endereco where id = ? order by rua asc")
})
public class Endereco  implements Serializable {
	
	@NotNull
	@Size(max = 30)
	@Column
	private String rua;
	
	@NotNull
	@Size(max = 30)
	@Column
	private String bairro;
	
	@NotNull
	@Size(max = 10)
	@Column
	private String numero;
	
	@PlcValSimpleFormat(format=SimpleFormat.NUMBER)
	@NotNull
	@Size(max = 8,min=8, message="CEP deve ter 8 números")
	@Column
	private String cep;
	
	@ManyToOne (targetEntity = UfEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_ENDERECO_UF")
	@NotNull
	@JoinColumn
	private Uf uf = new UfEntity();

	public Endereco() {
	}
	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua=rua;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro=bairro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero=numero;
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

	@Override
	public String toString() {
		return getRua();
	}

	@Transient
	private Long idUf;

	public Long getIdUf() {
		if (idUf!=null) return idUf;
		if (getUf()!=null){
			return getUf().getId();
		}
		return idUf;
	}
	public void setIdUf(Long idUf) {
		if (idUf!=null){
			UfEntity uf = new UfEntity();
				uf.setId(idUf);
				setUf(uf);		
		}
		this.idUf = idUf;
	}
}
