package com.empresa.rhavancado.entity;


import java.io.Serializable;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.validation.constraints.Size;
import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.MappedSuperclass;
import org.hibernate.annotations.ForeignKey;
import javax.persistence.FetchType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.persistence.Version;
import javax.persistence.Id;

@MappedSuperclass
public class Endereco  implements Serializable {

	
	@Id 
 	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_ENDERECO")
	private Long id;
	
	@Version
	@NotNull
	@Digits(integer=5, fraction=0)
	@Column(name="VERSAO")	
	private Integer versao = new Integer(0);


	
	@NotNull
	@Size(max = 5)
	private String logradouro;
	
	@NotNull
	@Size(max = 5)
	private String numero;
	
	@NotNull
	@Size(max = 5)
	private String bairro;
	
	@NotNull
	@Size(max = 5)
	private String cep;
	
	@ManyToOne (targetEntity = Uf.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_ENDERECO_UF")
	@NotNull
	private Uf uf;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id=id;
	}

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

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao=versao;
	}

}
