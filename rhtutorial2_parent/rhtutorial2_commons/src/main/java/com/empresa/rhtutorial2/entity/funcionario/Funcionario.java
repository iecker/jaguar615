/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */
package com.empresa.rhtutorial2.entity.funcionario;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
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
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Email;

import com.empresa.rhtutorial2.entity.AppBaseEntity;
import com.empresa.rhtutorial2.entity.Endereco;
import com.empresa.rhtutorial2.entity.UnidadeOrganizacional;
import com.empresa.rhtutorial2.entity.UnidadeOrganizacionalEntity;
import com.powerlogic.jcompany.domain.validation.PlcValCpf;
import com.powerlogic.jcompany.domain.validation.PlcValDuplicity;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;

/**
 * Funcionário
 */
@SuppressWarnings("serial")
@MappedSuperclass
public class Funcionario extends AppBaseEntity {

	@OneToMany(targetEntity = com.empresa.rhtutorial2.entity.funcionario.HistoricoProfissionalEntity.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "funcionario")
	@ForeignKey(name = "FK_HISTORICOPROFISSIONAL_FUNCIONARIO")
	@PlcValDuplicity(property = "descricao")
	@PlcValMultiplicity(min = 1, referenceProperty = "descricao", message = "{jcompany.aplicacao.mestredetalhe.multiplicidade.HistoricoProfissionalEntity}")
	@Valid
	private List<HistoricoProfissional> historicoProfissional;

	@OneToMany(targetEntity = com.empresa.rhtutorial2.entity.funcionario.DependenteEntity.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "funcionario")
	@ForeignKey(name = "FK_DEPENDENTE_FUNCIONARIO")
	@PlcValDuplicity(property = "nome")
	@PlcValMultiplicity(max = 2, referenceProperty = "nome", message = "{jcompany.aplicacao.mestredetalhe.multiplicidade.DependenteEntity}")
	@Valid
	private List<Dependente> dependente;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_FUNCIONARIO")
	private Long id;

	public static final int MAIORIDADE = 18;
	public static final BigDecimal SALARIO_MINIMO_SUPERIOR = new BigDecimal(
			1000);

	@NotNull
	@Size(max = 40)
	private String nome;

	@Past
	@NotNull
	@Temporal(TemporalType.DATE)
	private Date dataNascimento;

	@Audited
	@PlcValCpf
	@NotNull
	@Size(max = 14)
	private String cpf;

	@Email
	@Size(max = 60)
	private String email;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(length = 1)
	private EstadoCivil estadoCivil;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(length = 1)
	private Sexo sexo;
	private Boolean temCursoSuperior;

	@ManyToOne(targetEntity = UnidadeOrganizacionalEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_FUNCIONARIO_UNIDADEORGANIZACIONAL")
	private UnidadeOrganizacional unidadeOrganizacional;

	@Size(max = 255)
	private String observacao;

	@Embedded
	@NotNull
	@Valid
	private Endereco enderecoResidencial;

	// private Foto foto;
	// private List<Curriculo> curriculo;
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

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public EstadoCivil getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public Boolean getTemCursoSuperior() {
		return temCursoSuperior;
	}

	public void setTemCursoSuperior(Boolean temCursoSuperior) {
		this.temCursoSuperior = temCursoSuperior;
	}

	public UnidadeOrganizacional getUnidadeOrganizacional() {
		return unidadeOrganizacional;
	}

	public void setUnidadeOrganizacional(
			UnidadeOrganizacional unidadeOrganizacional) {
		this.unidadeOrganizacional = unidadeOrganizacional;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Endereco getEnderecoResidencial() {
		return enderecoResidencial;
	}

	public void setEnderecoResidencial(Endereco enderecoResidencial) {
		this.enderecoResidencial = enderecoResidencial;
	}

	public List<Dependente> getDependente() {
		return dependente;
	}

	public void setDependente(List<Dependente> dependente) {
		this.dependente = dependente;
	}

	public List<HistoricoProfissional> getHistoricoProfissional() {
		return historicoProfissional;
	}

	public void setHistoricoProfissional(
			List<HistoricoProfissional> historicoProfissional) {
		this.historicoProfissional = historicoProfissional;
	}

	@AssertTrue(message = "{trienamento.funcionario.assertiva.maior.idade}")
	public boolean getValidaMaiorIdade() {

		final long mileSegundosPorDia = 1000 * 60 * 60 * 24;

		long numeroDias = (new Date().getTime() - getDataNascimento().getTime())
				/ mileSegundosPorDia;

		return (numeroDias / 365) >= MAIORIDADE;
	}

	public BigDecimal obtemSalarioAtual() {
		// funcionário tem no minimo um histórico profissional
		BigDecimal salarioAtual = this.getHistoricoProfissional().get(0)
				.getSalario();
		Date dataInicio = this.getHistoricoProfissional().get(0)
				.getDataInicio();
		if (dataInicio != null) {
			for (Iterator iterator = this.getHistoricoProfissional().iterator(); iterator
					.hasNext();) {
				HistoricoProfissional hp = (HistoricoProfissional) iterator
						.next();
				if (hp.getDataInicio().getTime() - dataInicio.getTime() > 0) {
					salarioAtual = hp.getSalario();
					dataInicio = hp.getDataInicio();
				}
			}
		}
		return salarioAtual;
	}

	public int obtemTotalDependentes() {
		if (this.getDependente() != null)
			return this.getDependente().size();
		else
			return 0;
	}
}