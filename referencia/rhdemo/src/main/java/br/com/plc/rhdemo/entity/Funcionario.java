/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package br.com.plc.rhdemo.entity;

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
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;

import com.powerlogic.jcompany.domain.type.PlcYesNo;
import com.powerlogic.jcompany.domain.validation.PlcValCpf;
import com.powerlogic.jcompany.domain.validation.PlcValDuplicity;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;


@MappedSuperclass

public abstract class Funcionario  implements Serializable {


	private static final BigDecimal SALARIO_MINIMO_SUPERIOR = new BigDecimal(1000);

	@OneToMany (targetEntity = HistoricoFuncionalEntity.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="funcionario")
	@ForeignKey(name="FK_HISTORICOFUNCIONAL_FUNCIONARIO")
	@PlcValDuplicity(property="descricao")
	@PlcValMultiplicity(min = 1, message = "{jcompany.aplicacao.mestredetalhe.cardinalidade.HistoricoFuncionalEntity}", referenceProperty="descricao")
	@Valid
	private List<HistoricoFuncional> historicoFuncional;
	
	@OneToMany (targetEntity = DependenteEntity.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="funcionario")
	@ForeignKey(name="FK_DEPENDENTE_FUNCIONARIO")
	@PlcValDuplicity(property="nome")
	@PlcValMultiplicity(max = 2, message = "{jcompany.aplicacao.mestredetalhe.cardinalidade.DependenteEntity}", referenceProperty="nome")
	@Valid
	private List<Dependente> dependente;

	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_FUNCIONARIO")
	@Column (name = "ID_FUNCIONARIO", nullable=false, length=5)
	private Long id;

	@Version
	@NotNull
	@Column (name = "VERSAO")
	@Digits(integer=5, fraction=0)
	private Integer versao = new Integer(0);

	@NotNull
	@Column (name = "DATA_ULT_ALTERACAO")
	private Date dataUltAlteracao = new Date();

	@NotNull
	@Size(max=150)
	@Column (name = "USUARIO_ULT_ALTERACAO")
	private String usuarioUltAlteracao = "";

	@NotNull
	@Size(max = 40) 
	@Column
	private String nome;
	
	@Past
	@NotNull
	@Column (length=11)
	@Temporal(TemporalType.DATE)
	private Date dataNascimento;
	
	@PlcValCpf
	@NotNull 
	@Size(max = 11) 
	@Column
	private String cpf;

	@NotNull
	@ManyToOne (targetEntity = DepartamentoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_FUNCIONARIO_DEPARTAMENTO")
	@JoinColumn
	private Departamento departamento;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column (length=1)
	private Sexo sexo;

	@Enumerated(EnumType.STRING)
	@Column (length=1)
	private PlcYesNo temCursoSuperior;

	@Size(max = 255) 
	@Column
	private String observacao;

	@Transient
	private transient String indExcPlc = "N";		

	@Embedded
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
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf=cpf;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento=dataNascimento;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento=departamento;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo=sexo;
	}

	public PlcYesNo getTemCursoSuperior() {
		return temCursoSuperior;
	}

	public void setTemCursoSuperior(PlcYesNo temCursoSuperior) {
		this.temCursoSuperior=temCursoSuperior;
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

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
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

	public List<HistoricoFuncional> getHistoricoFuncional() {
		return historicoFuncional;
	}

	public void setHistoricoFuncional(List<HistoricoFuncional> historicoFuncional) {
		this.historicoFuncional=historicoFuncional;
	}

	public List<Dependente> getDependente() {
		return dependente;
	}

	public void setDependente(List<Dependente> dependente) {
		this.dependente=dependente;
	}

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

	public boolean validaSalarioCursoSuperior(){
		return (PlcYesNo.S==this.temCursoSuperior)||
		(PlcYesNo.N==this.temCursoSuperior &&
				HistoricoFuncional.recuperaMaiorSalario(this.historicoFuncional).compareTo(SALARIO_MINIMO_SUPERIOR)>=1000);
	}
	
}
