package com.empresa.rhtutorial2.entity;


import java.io.Serializable;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import javax.validation.constraints.Size;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.ForeignKey;
import javax.persistence.FetchType;
import javax.validation.constraints.NotNull;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.Access;

@Embeddable
@Access(AccessType.FIELD)

@NamedQueries({
	@NamedQuery(name="Endereco.querySelLookup", query="select logradouro as logradouro from Endereco where id = ? order by logradouro asc")
})
public class Endereco  implements Serializable {

	
	@NotNull
	@Size(max = 60)
	private String logradouro;
	
	@NotNull
	@Size(max = 5)
	private String numero;
	
	@NotNull
	@Size(max = 30)
	private String bairro;
	
	@NotNull
	@Size(max = 8)
	private String cep;
	
	@ManyToOne (targetEntity = UfEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_ENDERECO_UF")
	@NotNull
	private Uf uf;
	public String getLogradouro() {
		return logradouro;
	}
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public Uf getUf() {
		return uf;
	}
	public void setUf(Uf uf) {
		this.uf = uf;
	}
	
	public Endereco() {
	}
	@Override
	public String toString() {
		return getLogradouro();
	}

}
