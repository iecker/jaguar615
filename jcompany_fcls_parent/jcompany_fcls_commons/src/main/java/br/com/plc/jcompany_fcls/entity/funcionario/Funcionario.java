/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.funcionario;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;

import br.com.plc.jcompany_fcls.entity.departamento.Departamento;
import br.com.plc.jcompany_fcls.entity.departamento.DepartamentoEntity;
import br.com.plc.jcompany_fcls.entity.departamento.Endereco;

import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompany.domain.type.PlcYesNo;
import com.powerlogic.jcompany.domain.validation.PlcValDuplicity;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;

@MappedSuperclass

public abstract class Funcionario implements Serializable {

	private static final BigDecimal SALARIO_MINIMO_SUPERIOR = new BigDecimal(1000);

	@OneToMany(targetEntity = HistoricoFuncionalEntity.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "funcionario")
	@ForeignKey(name = "FK_HISTORICOFUNCIONAL_FUNCIONARIO")
	@PlcValMultiplicity(referenceProperty="descricao")
	@PlcValDuplicity(property="descricao")
	private List<HistoricoFuncional> historicoFuncional;

	@OneToMany(targetEntity = DependenteEntity.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "funcionario")
	@ForeignKey(name = "FK_DEPENDENTE_FUNCIONARIO")
	@PlcValMultiplicity(min = 2, max = 4, message = "{jcompany.aplicacao.mestredetalhe.cardinalidade.DependenteEntity}", referenceProperty="nome")
	@PlcValDuplicity(property="nome")
	private List<Dependente> dependente;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_FUNCIONARIO")
	@Column(name = "ID_FUNCIONARIO")
	private Long id;

	@Version
	@NotNull
	@Column (name = "VERSAO")
	@Digits(integer=5, fraction=0)
	private int versao;

	@NotNull
	@Column(name = "DATA_ULT_ALTERACAO", nullable = false, length = 11)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUltAlteracao = new Date();
	
	@NotNull
	@Size(max=150)
	@Column(name = "USUARIO_ULT_ALTERACAO", nullable = false)
	private String usuarioUltAlteracao = "";
	
	@NotNull
	@Size(max=40)
	@Column(name = "NOME")
	private String nome;

	@Column(name = "DATA_NASCIMENTO")
	@Temporal(TemporalType.DATE)
	private Date dataNascimento;

	@NotNull
	@ManyToOne(targetEntity = DepartamentoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_FUNCIONARIO_DEPARTAMENTO")
	@JoinColumn(name = "ID_DEPARTAMENTO")
	private Departamento departamento;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "SEXO")
	private Sexo sexo;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "TEM_CURSO_SUPERIOR")
	private PlcYesNo temCursoSuperior;

	@NotNull
	@Size(max=255)
	@Column(name = "OBSERVACAO")
	private String observacao;

	@Transient
	private String indExcPlc = "N";

	@Embedded
	@NotNull
	@Valid
	private Endereco endereco;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public PlcYesNo getTemCursoSuperior() {
		return temCursoSuperior;
	}

	public void setTemCursoSuperior(PlcYesNo temCursoSuperior) {
		this.temCursoSuperior = temCursoSuperior;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public int getVersao() {
		return versao;
	}

	public void setVersao(int versao) {
		this.versao = versao;
	}

	public Date getDataUltAlteracao() {
		return dataUltAlteracao;
	}

	public void setDataUltAlteracao(Date dataUltAlteracao) {
		this.dataUltAlteracao = dataUltAlteracao;
	}

	public String getUsuarioUltAlteracao() {
		return usuarioUltAlteracao;
	}

	public void setUsuarioUltAlteracao(String usuarioUltAlteracao) {
		this.usuarioUltAlteracao = usuarioUltAlteracao;
	}

	public List<HistoricoFuncional> getHistoricoFuncional() {
		return historicoFuncional;
	}

	public void setHistoricoFuncional(List<HistoricoFuncional> historicoFuncional) {
		this.historicoFuncional = historicoFuncional;
	}

	public List<Dependente> getDependente() {
		return dependente;
	}

	public void setDependente(List<Dependente> dependente) {
		this.dependente = dependente;
	}

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

	public boolean validaSalarioCursoSuperior() {
		return (PlcYesNo.S == this.temCursoSuperior) || (PlcYesNo.N == this.temCursoSuperior && HistoricoFuncional.recuperaMaiorSalario(this.historicoFuncional).compareTo(SALARIO_MINIMO_SUPERIOR) >= 1000);
	}

}
