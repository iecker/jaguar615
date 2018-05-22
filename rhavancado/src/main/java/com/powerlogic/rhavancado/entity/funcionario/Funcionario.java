package com.powerlogic.rhavancado.entity.funcionario;
import java.util.Date;

import com.powerlogic.rhavancado.entity.EstadoCivil;
import com.powerlogic.rhavancado.entity.Sexo;
import java.io.Serializable;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.persistence.Enumerated;
import javax.persistence.TemporalType;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.persistence.MappedSuperclass;
import javax.persistence.GenerationType;
import javax.persistence.EnumType;
import javax.persistence.Temporal;
import javax.validation.constraints.Size;
import javax.validation.constraints.Digits;
import javax.persistence.GeneratedValue;


@MappedSuperclass
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
	@Column(name="DATA_ULT_ALTERACAO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUltAlteracao = new Date();
	
	@NotNull
	@Size(max = 150)
	@Column(name = "USUARIO_ULT_ALTERACAO") 
	private String usuarioUltAlteracao = "";
	
	@NotNull
	@Size(max = 50)
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
	@Column(length=4)
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
	@Size(max = 1024)
	private String observacao;

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

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf=cpf;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo=sexo;
	}

	public EstadoCivil getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil=estadoCivil;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento=dataNascimento;
	}

	public boolean getTemCursoSuperior() {
		return temCursoSuperior;
	}

	public void setTemCursoSuperior(boolean temCursoSuperior) {
		this.temCursoSuperior=temCursoSuperior;
	}

	public String getCodigoCargo() {
		return codigoCargo;
	}

	public void setCodigoCargo(String codigoCargo) {
		this.codigoCargo=codigoCargo;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao=observacao;
	}

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao=versao;
	}

	public Date getDataUltAlteracao() {
		return dataUltAlteracao;
	}

	public void setDataUltAlteracao(Date dataUltAlteracao) {
		this.dataUltAlteracao=dataUltAlteracao;
	}

	public String getUsuarioUltAlteracao() {
		return usuarioUltAlteracao;
	}

	public void setUsuarioUltAlteracao(String usuarioUltAlteracao) {
		this.usuarioUltAlteracao=usuarioUltAlteracao;
	}

}