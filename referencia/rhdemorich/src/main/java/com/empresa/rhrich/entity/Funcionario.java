package com.empresa.rhrich.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.apache.myfaces.extensions.validator.crossval.annotation.NotEquals;
import org.hibernate.annotations.ForeignKey;

import com.powerlogic.jcompany.domain.validation.PlcValCpf;
import com.powerlogic.jcompany.domain.validation.PlcValDuplicity;
import com.powerlogic.jcompany.domain.validation.PlcValGroupArgument;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;

@MappedSuperclass
public class Funcionario  implements Serializable {

	@PlcValDuplicity(property="descricao")
	@PlcValMultiplicity(min=1,referenceProperty="descricao")
	@OneToMany (targetEntity = com.empresa.rhrich.entity.HistoricoFuncionalEntity.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="funcionario")
	@ForeignKey(name="FK_HISTORICOFUNCIONAL_FUNCIONARIO")
	@Valid
	private List<HistoricoFuncional> historicoFuncional;

	@PlcValMultiplicity(max=2,referenceProperty="nome")
	@OneToMany (targetEntity = com.empresa.rhrich.entity.DependenteEntity.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="funcionario")
	@ForeignKey(name="FK_DEPENDENTE_FUNCIONARIO")
	@Valid
	private List<Dependente> dependente;
	
	@Id 
 	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_FUNCIONARIO")
	@Column(nullable=false,length=5)
	private Long id;
	
	@Version
	@NotNull
	@Column(length=5)
	private int versao;
	
	@NotNull
	@Column(length=11)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUltAlteracao = new Date();
	
	@NotNull
	@Size(max = 5)
	@Column
	private String usuarioUltAlteracao = "";

	
	
	@NotNull
	@Size(max = 30)
	@Column
	private String nome;
	
	@Past
	@NotNull
	@Column(length=11)
	@Temporal(TemporalType.DATE)
	private Date dataNascimento;
	
	@PlcValCpf
	@NotNull
	@Size(max = 11)
	@Column
	private String cpf;
	
	@Min(value=1,groups=PlcValGroupArgument.class)
	@NotNull
	@Column(length=5)
	private Boolean temCursoSuperior;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(length=1)
	private Sexo sexo;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(length=1)
	private EstadoCivil estadoCivil;
	
	@ManyToOne (targetEntity = DepartamentoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_FUNCIONARIO_DEPARTAMENTO")
	@JoinColumn
	private Departamento departamento;
	
	@NotNull
	@Size(max = 255)
	@Column
	private String observacao;
	
	@Embedded
	@Valid
	private Endereco endereco;

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

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento=dataNascimento;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf=cpf;
	}

	public Boolean getTemCursoSuperior() {
		return temCursoSuperior;
	}

	public void setTemCursoSuperior(Boolean temCursoSuperior) {
		this.temCursoSuperior=temCursoSuperior;
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

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento=departamento;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao=observacao;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco=endereco;
	}

	public int getVersao() {
		return versao;
	}

	public void setVersao(int versao) {
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

	public List<Dependente> getDependente() {
		return dependente;
	}

	public void setDependente(List<Dependente> dependente) {
		this.dependente=dependente;
	}

	public List<HistoricoFuncional> getHistoricoFuncional() {
		return historicoFuncional;
	}

	public void setHistoricoFuncional(List<HistoricoFuncional> historicoFuncional) {
		this.historicoFuncional=historicoFuncional;
	}

}
