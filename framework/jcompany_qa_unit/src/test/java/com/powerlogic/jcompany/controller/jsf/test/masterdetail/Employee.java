/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.test.masterdetail;

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
import javax.validation.constraints.Past;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.Email;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.PlcDateUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.domain.validation.PlcValCpf;
import com.powerlogic.jcompany.domain.validation.PlcValDuplicity;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;

/**
 * Funcionário
 */
@MappedSuperclass

public abstract class Employee extends AppBaseEntity {

	
	@Column (name = "SIT_HISTORICO_PLC", nullable=false, length=1)
	private String sitHistoricoPlc="A";

	@PlcValDuplicity(property="descricao")
	@PlcValMultiplicity(min=1,referenceProperty="descricao")
	@SuppressWarnings("unchecked")
	@OneToMany (targetEntity = ProfissionalHistoryEntity.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="funcionario")
	@ForeignKey(name="FK_HISTORICOPROFISSIONAL_FUNCIONARIO")
	private List<ProfissionalHistory> historicoProfissional;

	@PlcValDuplicity(property="nome")
	@PlcValMultiplicity(max=2,referenceProperty="nome")
	@SuppressWarnings("unchecked")
	@OneToMany (targetEntity = DependentEntity.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="funcionario")
	@ForeignKey(name="FK_DEPENDENTE_FUNCIONARIO")
	private List<Dependent> dependente;

	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_FUNCIONARIO")
	@Column (name = "ID_FUNCIONARIO", nullable=false, length=5)
	private Long id;

	public static final int MAIORIDADE = 18;
	public static final BigDecimal SALARIO_MINIMO_SUPERIOR = new BigDecimal(1000);

	@Column (name = "NOME", nullable=false, length=40)
	private String nome;
	
	@Past
	@Column (name = "DATA_NASCIMENTO", nullable=false, length=11)
	@Temporal(TemporalType.DATE)
	private java.util.Date dataNascimento;
	
	@PlcValCpf
	@Index(name="I_CPF")
	@Column (name = "CPF", nullable=false, length=11)
	private String cpf;
	
	@Email
	@Column (name = "EMAIL", length=60)
	private String email;
	
	@Enumerated(EnumType.STRING)
	@Column (name = "ESTADO_CIVIL", nullable=false, length=1)
	private MaritalStatus estadoCivil;
	
	@Enumerated(EnumType.STRING)
	@Column (name = "SEXO", nullable=false, length=1)
	private Sex sexo;
	
	@Column (name = "TEM_CURSO_SUPERIOR", nullable=false, length=1)
	private Boolean temCursoSuperior=null;
	
	@SuppressWarnings("unchecked")
	@ManyToOne (targetEntity = OrganizationalUnitEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_FUNCIONARIO_UNIDADEORGANIZACIONAL")
	@JoinColumn (name = "ID_UNIDADE_ORGANIZACIONAL")
	private OrganizationalUnit unidadeOrganizacional;
	
	@Column (name = "OBSERVACAO", length=255)
	private String observacao;
	
	@Embedded
	private Address enderecoResidencial;
	
	@ManyToOne(targetEntity=ImageEntity.class,fetch=FetchType.LAZY)
	@ForeignKey(name="FK_FUNCIONARIO_FOTO")
	@JoinColumn(name="ID_FOTO",nullable=true)
	private ImageEntity arquivoAnexado;
	
	public ImageEntity getArquivoAnexado() {
		return arquivoAnexado;
	}

	public void setArquivoAnexado(ImageEntity arquivoAnexado) {
		this.arquivoAnexado = arquivoAnexado;
	}


	
	public boolean validaMaioridade() {
		
		PlcDateUtil dateUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcDateUtil.class, QPlcDefaultLiteral.INSTANCE);
		float numDiasVida = dateUtil.daysBetweenDates(this.dataNascimento, new Date());	
		return (numDiasVida/365) > MAIORIDADE;
	}
	
	/**
	 * @return true se Funcionário não tiver curso superior ou se tiver 
	 *  e seu salário for maior que SAL_MINIMO_SUPERIOR
	 */
	public boolean validaSalarioCursoSuperior() {
		
		return !temCursoSuperior ||
				(temCursoSuperior 
				&& ProfissionalHistory.recuperaMaiorSalario(this.historicoProfissional)
					        .compareTo(SALARIO_MINIMO_SUPERIOR)>=0);
	}
	
	
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

	public MaritalStatus getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(MaritalStatus estadoCivil) {
		this.estadoCivil=estadoCivil;
	}

	public Sex getSexo() {
		return sexo;
	}

	public void setSexo(Sex sexo) {
		this.sexo=sexo;
	}

	public Boolean getTemCursoSuperior() {
		return temCursoSuperior;
	}

	public void setTemCursoSuperior(Boolean temCursoSuperior) {
		this.temCursoSuperior=temCursoSuperior;
	}

	public OrganizationalUnit getUnidadeOrganizacional() {
		return unidadeOrganizacional;
	}

	public void setUnidadeOrganizacional(OrganizationalUnit unidadeOrganizacional) {
		this.unidadeOrganizacional=unidadeOrganizacional;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao=observacao;
	}

	public Address getEnderecoResidencial() {
		return enderecoResidencial;
	}

	public void setEnderecoResidencial(Address enderecoResidencial) {
		this.enderecoResidencial=enderecoResidencial;
	}

	public List<Dependent> getDependente() {
		return dependente;
	}

	public void setDependente(List<Dependent> dependente) {
		this.dependente=dependente;
	}

	public List<ProfissionalHistory> getHistoricoProfissional() {
		return historicoProfissional;
	}

	public void setHistoricoProfissional(List<ProfissionalHistory> historicoProfissional) {
		this.historicoProfissional=historicoProfissional;
	}

	public String getSitHistoricoPlc() {
		return sitHistoricoPlc;
	}

	public void setSitHistoricoPlc(String sitHistoricoPlc) {
		this.sitHistoricoPlc=sitHistoricoPlc;
	}

}