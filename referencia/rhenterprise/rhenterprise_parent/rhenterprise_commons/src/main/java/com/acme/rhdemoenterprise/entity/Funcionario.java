/*  																													
	    				   jCompany Developer Suite																		
			    		Copyright (C) 2008  Powerlogic																	
	 																													
	    Este programa Ã© licenciado com todos os seus cÃ³digos fontes. VocÃª pode modificÃ¡-los e							
	    utilizÃ¡-los livremente, inclusive distribuÃ­-los para terceiros quando fizerem parte de algum aplicativo 		
	    sendo cedido, segundo os termos de licenciamento gerenciado de cÃ³digo aberto da Powerlogic, definidos			
	    na licenÃ§a 'Powerlogic Open-Source Licence 2.0 (POSL 2.0)'.    													
	  																													
	    A Powerlogic garante o acerto de erros eventualmente encontrados neste cÃ³digo, para os clientes licenciados, 	
	    desde que todos os cÃ³digos do programa sejam mantidos intactos, sem modificaÃ§Ãµes por parte do licenciado. 		
	 																													
	    VocÃª deve ter recebido uma cÃ³pia da licenÃ§a POSL 2.0 juntamente com este programa.								
	    Se nÃ£o recebeu, veja em <http://www.powerlogic.com.br/licencas/posl20/>.										
	 																													
	    Contatos: plc@powerlogic.com.br - www.powerlogic.com.br 														
																														
 */ 
package com.acme.rhdemoenterprise.entity;

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
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.validator.constraints.Email;

import com.powerlogic.jcompany.domain.validation.PlcValCpf;
import com.powerlogic.jcompany.domain.validation.PlcValDuplicity;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;


/**
 * FuncionÃ¡rio
 */
@MappedSuperclass

public abstract class Funcionario extends AppBaseEntity {
	
	@OneToMany (targetEntity = com.acme.rhdemoenterprise.entity.HistoricoProfissionalEntity.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="funcionario")
	@PlcValMultiplicity(min = 1, referenceProperty="descricao", message = "{jcompany.aplicacao.mestredetalhe.multiplicidade.HistoricoProfissionalEntity}")
	@PlcValDuplicity(property="descricao")
	@ForeignKey(name="FK_HISTORICOPROFISSIONAL_FUNCIONARIO")	
	@Valid
	private List<HistoricoProfissional> historicoProfissional;

	@OneToMany (targetEntity = com.acme.rhdemoenterprise.entity.DependenteEntity.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="funcionario")
	@PlcValMultiplicity(min = 2, max = 4, referenceProperty="nome", message = "{jcompany.aplicacao.mestredetalhe.multiplicidade.DependenteEntity}")
	@PlcValDuplicity(property="nome")
	@ForeignKey(name="FK_DEPENDENTE_FUNCIONARIO")
	@Valid
	private List<Dependente> dependente;
	
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_FUNCIONARIO")	
	private Long id;

	public static final int MAIORIDADE = 18;
	public static final BigDecimal SALARIO_MINIMO_SUPERIOR = new BigDecimal(1000);
	
	@Column 	
	@Size(max = 50)
	@NotNull
	private String nome;
	
	@Column	
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date dataNascimento;
	
	@PlcValCpf
	@Column 
	@Size(max = 14)
	@NotNull
	private String cpf;
	
	@Email
	@Column 
	@Size(max = 40)
	private String email;
	
	@Enumerated(EnumType.STRING)
	@Column(length=1)
	@NotNull
	private EstadoCivil estadoCivil;
	
	@Enumerated(EnumType.STRING)	
	@Column(length=1)
	@NotNull
	private Sexo sexo;
	
	@Column
	@NotNull
	private Boolean temCursoSuperior;
	
	@ManyToOne (targetEntity = UnidadeOrganizacionalEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_FUNCIONARIO_UNIDADEORGANIZACIONAL")
	@JoinColumn (nullable=true)	
	private UnidadeOrganizacional unidadeOrganizacional;
	
	@Column 
	@Size(max = 255)	
	private String observacao;
	
	@Column 
	@Size(max = 1)
	private String categoriaSalarial;
	
	@Embedded
	@Valid
	private Endereco enderecoResidencial;
	
	private transient BigDecimal ultimoSalario;
	private transient Date dataUltimoEmprego;

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

	public java.util.Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(java.util.Date dataNascimento) {
		this.dataNascimento=dataNascimento;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf=cpf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email=email;
	}

	public EstadoCivil getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil=estadoCivil;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo=sexo;
	}

	public Boolean getTemCursoSuperior() {
		return temCursoSuperior;
	}

	public void setTemCursoSuperior(Boolean temCursoSuperior) {
		this.temCursoSuperior=temCursoSuperior;
	}

	public UnidadeOrganizacional getUnidadeOrganizacional() {
		return unidadeOrganizacional;
	}

	public void setUnidadeOrganizacional(UnidadeOrganizacional unidadeOrganizacional) {
		this.unidadeOrganizacional=unidadeOrganizacional;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao=observacao;
	}

	public Endereco getEnderecoResidencial() {
		return enderecoResidencial;
	}

	public void setEnderecoResidencial(Endereco enderecoResidencial) {
		this.enderecoResidencial=enderecoResidencial;
	}

	public List<Dependente> getDependente() {
		return dependente;
	}

	public void setDependente(List<Dependente> dependente) {
		this.dependente=dependente;
	}

	public List<HistoricoProfissional> getHistoricoProfissional() {
		return historicoProfissional;
	}

	public void setHistoricoProfissional(List<HistoricoProfissional> historicoProfissional) {
		this.historicoProfissional=historicoProfissional;
	}

	public String getCategoriaSalarial() {
		return categoriaSalarial;
	}

	public void setCategoriaSalarial(String categoriaSalarial) {
		this.categoriaSalarial = categoriaSalarial;
	}

	public BigDecimal getUltimoSalario() {
		return ultimoSalario;
	}

	public void setUltimoSalario(BigDecimal ultimoSalario) {
		this.ultimoSalario = ultimoSalario;
	}

	public Date getDataUltimoEmprego() {
		return dataUltimoEmprego;
	}

	public void setDataUltimoEmprego(Date dataUltimoEmprego) {
		this.dataUltimoEmprego = dataUltimoEmprego;
	}
	
	

}