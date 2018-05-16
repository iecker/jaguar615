/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/
package com.empresa.rhtutorial.entity.funcionario;

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
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.apache.myfaces.extensions.validator.baseval.annotation.Required;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.Email;

import com.empresa.rhtutorial.entity.AppBaseEntity;
import com.empresa.rhtutorial.entity.Endereco;
import com.empresa.rhtutorial.entity.UnidadeOrganizacional;
import com.empresa.rhtutorial.entity.UnidadeOrganizacionalEntity;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.PlcDateUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.config.domain.PlcFileAttach;
import com.powerlogic.jcompany.domain.validation.PlcValCpf;
import com.powerlogic.jcompany.domain.validation.PlcValDuplicity;
import com.powerlogic.jcompany.domain.validation.PlcValGroupArgument;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;

/**
 * FuncionÃ¡rio
 */
@MappedSuperclass
public abstract class Funcionario extends AppBaseEntity {

	public static final int MAIORIDADE = 18;

	public static final BigDecimal SALARIO_MINIMO_SUPERIOR = new BigDecimal(1000);
	
	@Id 
 	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_FUNCIONARIO")
	@Column(nullable=false,length=5)
	private Long id;
	
	@Min(value=1,groups=PlcValGroupArgument.class,message="Informe ao menos uma inicial no nome")
	@NotNull 
	@Size(max = 40) 
	@Column
	private String nome;
	
	@Past
	@NotNull @Column(length=11) @Temporal(TemporalType.DATE)
	private Date dataNascimento;
	
	//@Index(name="I_CPF")
	@PlcValCpf
	@NotNull 
	@Size(max = 11) 
	@Column
	private String cpf;
	
	@Email 
	@Size(max = 60) @Column
	private String email;
	
	@Enumerated(EnumType.STRING)
	@NotNull @Column(length=1)
	private EstadoCivil estadoCivil;
	
	@Enumerated(EnumType.STRING)
	@NotNull @Column(length=1)
	private Sexo sexo;
	
	@NotNull @Column(length=1)
	private Boolean temCursoSuperior = false;
	
	@ManyToOne (targetEntity = UnidadeOrganizacionalEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_FUNCIONARIO_UNIDADEORGANIZACIONAL")
	@JoinColumn
	private UnidadeOrganizacional unidadeOrganizacional;
	
	@Size(max = 255) @Column
	private String observacao;
	
	@Embedded
	@Valid
	private Endereco enderecoResidencial;
	
    @OneToMany (targetEntity = CurriculoEntity.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @OrderBy("id")
    @ForeignKey(name="FK_CURRICULO")
    @PlcValMultiplicity(min=2, max=4, referenceProperty="nome",	message="{funcionario.multiplicidade.curriculo}")
    @PlcFileAttach(extension={"txt","doc","pdf"},  multiple=true, maximumLength=2048)
    @Valid
    private List<CurriculoEntity> curriculo;

    @OneToOne (targetEntity = Foto.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL)    
    @ForeignKey(name="FK_FOTO")
    @JoinColumn(name="ID_FOTO")
    // Exemplo de teste de obrigatoriedade
    // @NotNull(message="{funcionario.obrigatoriedade.foto}")
    @PlcFileAttach(extension={"gif", "jpg", "png"},image=true,showImageHeight=120,showImageWidth=90)
    private Foto foto;
	
	@NotNull
	@Size(max = 1) @Column
	private String sitHistoricoPlc="A";
	
	@OneToMany (targetEntity = com.empresa.rhtutorial.entity.funcionario.HistoricoProfissionalEntity.class, 
			fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="funcionario")
	@ForeignKey(name="FK_HISTORICOPROFISSIONAL_FUNCIONARIO")
 	@PlcValMultiplicity(min=1,referenceProperty="descricao",message="{funcionario.multiplicidade.historicoProfissional}")
	@PlcValDuplicity(property="descricao")
	@Valid
	private List<HistoricoProfissional> historicoProfissional;
	
	@OneToMany (targetEntity = com.empresa.rhtutorial.entity.funcionario.DependenteEntity.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="funcionario")
	@ForeignKey(name="FK_DEPENDENTE_FUNCIONARIO")
	@PlcValMultiplicity(max=2,referenceProperty="nome",message="{funcionario.multiplicidade.max.dependente}")
	@PlcValDuplicity(property="nome")
	@Valid
	private List<Dependente> dependente;
	
	@Transient
	PlcDateUtil dateUtil ;
	
	/**
	 * TODO ImplementaÃ§Ã£o somente de exemplo, com margem de erro (desconsidera ano bissexto, etc.)
	 * @return true Se funcionÃ¡rio for maior de idade
	 */
	@AssertTrue(message="{funcionario.valida.maioridade}")
	public boolean isMaiorDeIdade() {
		if (dateUtil==null)
			dateUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcDateUtil.class, QPlcDefaultLiteral.INSTANCE);
		float numDiasVida = dateUtil.daysBetweenDates(this.dataNascimento,new Date());
		return numDiasVida / 365 > MAIORIDADE;	
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

	public String getSitHistoricoPlc() {
		return sitHistoricoPlc;
	}

	public void setSitHistoricoPlc(String sitHistoricoPlc) {
		this.sitHistoricoPlc=sitHistoricoPlc;
	}

	public Foto getFoto() {
		return foto;
	}

	public void setFoto(Foto foto) {
		this.foto = foto;
	}

	public List<CurriculoEntity> getCurriculo() {
		return curriculo;
	}

	public void setCurriculo(List<CurriculoEntity> curriculo) {
		this.curriculo = curriculo;
	}


}