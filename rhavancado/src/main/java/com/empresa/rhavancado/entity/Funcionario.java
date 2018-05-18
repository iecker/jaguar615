package com.empresa.rhavancado.entity;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;

@MappedSuperclass
@NamedQueries({
	@NamedQuery(name="Funcionario.queryMan", query="from Funcionario"),
	@NamedQuery(name="Funcionario.querySel", query="select id as id from Funcionario order by id asc")
})
public class Funcionario  implements Serializable {

	
	@Id 
 	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_FUNCIONARIO")
	private Long id;
	
	@Version
	@NotNull
	@Digits(integer=5, fraction=0)
	@Column(name="VERSAO")	
	private Integer versao = new Integer(0);


	
	@NotNull
	@Size(max = 5)
	private String nome;
	
	@NotNull
	@Size(max = 11)
	private String cpf;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(length=1)
	private Sexo sexo;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(length=1)
	private EstadoCivil estadoCivil;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataNascimento;
	
	@NotNull
	private boolean temCursoSuperior;
	
	@NotNull
	@Size(max = 5)
	private String codigoCargo;
	
	@NotNull
	@Size(max = 5)
	private String observacao;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public Sexo getSexo() {
		return sexo;
	}
	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}
	public EstadoCivil getEstadoCivil() {
		return estadoCivil;
	}
	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}
	public Date getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	public boolean isTemCursoSuperior() {
		return temCursoSuperior;
	}
	public void setTemCursoSuperior(boolean temCursoSuperior) {
		this.temCursoSuperior = temCursoSuperior;
	}
	public String getCodigoCargo() {
		return codigoCargo;
	}
	public void setCodigoCargo(String codigoCargo) {
		this.codigoCargo = codigoCargo;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id=id;
	}

	public boolean getTemCursoSuperior() {
		return temCursoSuperior;
	}

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao=versao;
	}

}
