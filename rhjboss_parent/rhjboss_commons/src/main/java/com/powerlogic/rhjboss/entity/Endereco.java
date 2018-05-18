package com.powerlogic.rhjboss.entity;



import java.io.Serializable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.persistence.FetchType;
import org.hibernate.annotations.ForeignKey;
import javax.persistence.Access;
import javax.persistence.Embeddable;
import javax.persistence.AccessType;

@Embeddable
@Access(AccessType.FIELD)

@NamedQueries({
	@NamedQuery(name="Endereco.querySelLookup", query="select rua as rua from Endereco where id = ? order by rua asc")
})
public class Endereco  implements Serializable {
 
	@NotNull
	@Size(max = 50)
	private String rua;
	
	@NotNull
	@Size(max = 6)
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
	 
	public Endereco() {
	}
	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua=rua;
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

	@Override
	public String toString() {
		return getRua();
	}

}
