/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.profissional;

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
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.Email;

import br.com.plc.jcompany_fcls.entity.colaborador.EstadoCivil;
import br.com.plc.jcompany_fcls.entity.departamento.Departamento;
import br.com.plc.jcompany_fcls.entity.departamento.DepartamentoEntity;
import br.com.plc.jcompany_fcls.entity.departamento.Endereco;
import br.com.plc.jcompany_fcls.entity.funcionario.Sexo;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.PlcDateUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.config.domain.PlcFileAttach;
import com.powerlogic.jcompany.config.domain.PlcNotCloneable;
import com.powerlogic.jcompany.domain.type.PlcYesNo;
import com.powerlogic.jcompany.domain.validation.PlcValCpf;
import com.powerlogic.jcompany.domain.validation.PlcValDuplicity;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;


@MappedSuperclass

public abstract class Profissional  implements Serializable {

	@Size(max=1)
	@Column (name = "SIT_HISTORICO_PLC")
	private String sitHistoricoPlc="A";
	
	@SuppressWarnings("unchecked")
	@OneToMany (targetEntity = DependenteProfissionalEntity.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="profissional")
	@ForeignKey(name="FK_DEPENDENTEPROFISSIONAL_PROFISSIONAL")
	@PlcValMultiplicity(referenceProperty="nome")
	@PlcValDuplicity(property="nome")
	private List<DependenteProfissional> dependenteProfissional;
	
	@SuppressWarnings("unchecked")
	@OneToMany (targetEntity = HistoricoProfissionalEntity.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="profissional")
	@ForeignKey(name="FK_HISTORICOPROFISSIONAL_PROFISSIONAL")
	@PlcValMultiplicity(referenceProperty="descricao")
	@PlcValDuplicity(property="descricao")
	private List<HistoricoProfissional> historicoProfissional;
	

	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_PROFISSIONAL")
	@Column (name = "ID_PROFISSIONAL")
	private Long id;
	
	public static final int MAIORIDADE = 18;
	public static final BigDecimal SALARIO_MINIMO_SUPERIOR = new BigDecimal(1000);
	
	@Version
	@NotNull
	@Column (name = "VERSAO")
	@Digits(integer=5, fraction=0)
	private Integer versao = new Integer(0);
	
	@Column (name = "DATA_ULT_ALTERACAO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUltAlteracao = new Date();
	
	@Column (name = "USUARIO_ULT_ALTERACAO")
	private String usuarioUltAlteracao = "";

	@NotNull
	@Size(max=40)
	@Column (name = "NOME")
	private String nome;
	
	@NotNull
	@Past
	@Column (name = "DATA_NASCIMENTO")
	@Temporal(TemporalType.DATE)
	private Date dataNascimento;
	
	@PlcNotCloneable
	@NotNull
	@Size(max=11)
	@PlcValCpf
	@Column (name = "CPF")
	@Index(name="I_CPF")
	private String cpf;
	
	@Size(max=60)
	@Email
	@Column (name = "EMAIL")
	private String email;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column (name = "ESTADO_CIVIL")
	private EstadoCivil estadoCivil;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column (name = "SEXO")
	private Sexo sexo;
	
	@Enumerated(EnumType.STRING)
	@Column (name = "TEM_CURSO_SUPERIOR")
	private PlcYesNo temCursoSuperior;
	
	@SuppressWarnings("unchecked")
	@ManyToOne (targetEntity = DepartamentoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_PROFISSIONAL_DEPARTAMENTO")
	@JoinColumn (name = "ID_DEPARTAMENTO")
	private Departamento departamento;
	
	@Size(max=255)
	@Column (name = "DESCRICAO", length=255)
	private String descricao;
	
	@Embedded
	private Endereco endereco;
	
	@OneToOne (targetEntity = FotoEntity.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@ForeignKey(name="FK_PROFISSIONAL_FOTO")
	@JoinColumn(name="ID_FOTO")
	@PlcFileAttach(extension={"gif", "jpg", "png"},image=true,showImageHeight=120,showImageWidth=90)
	private FotoEntity foto;
	
	public FotoEntity getFoto() {
		return foto;
	}

	public void setFoto(FotoEntity foto) {
		this.foto = foto;
	}
	
	/**
	 * TODO Testar ano bissexto
	 * @return true se for maior de idade conforme constante MAIORIDADE (com margem de erro)
	 */
	@AssertTrue(message="{validator.maioridade}")
	public boolean isMaioridade() {
		PlcDateUtil dateUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcDateUtil.class, QPlcDefaultLiteral.INSTANCE);
		float numDiasVida = dateUtil.daysBetweenDates(this.dataNascimento, new Date());	
		return (numDiasVida/365) > MAIORIDADE;
	}
	
	/**
	 * @return true se FuncionÃ¡rio nÃ£o tiver curso superior ou se tiver 
	 *  e seu salÃ¡rio for maior que SAL_MINIMO_SUPERIOR
	 */
	@AssertTrue(message="{validator.salariocursosuperior}")
	public boolean isSalarioCursoSuperior() {
		
		if(PlcYesNo.S == this.temCursoSuperior) {
			if(HistoricoProfissional.recuperaMaiorSalario(this.historicoProfissional).compareTo(SALARIO_MINIMO_SUPERIOR)>0) {
				return true;
			} else {
				return false;
			}
		} else {
			if(HistoricoProfissional.recuperaMaiorSalario(this.historicoProfissional).compareTo(SALARIO_MINIMO_SUPERIOR)>0) {
				return false;
			} else {
				return true;
			}
		}
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

	public PlcYesNo getTemCursoSuperior() {
		return temCursoSuperior;
	}

	public void setTemCursoSuperior(PlcYesNo temCursoSuperior) {
		this.temCursoSuperior=temCursoSuperior;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento=departamento;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao=descricao;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco=endereco;
	}

	//public Foto getFoto() {
		//return foto;
	//}

	//public void setFoto(Foto foto) {
		//this.foto=foto;
	//}

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

	public List<DependenteProfissional> getDependenteProfissional() {
		return dependenteProfissional;
	}

	public void setDependenteProfissional(List<DependenteProfissional> dependenteProfissional) {
		this.dependenteProfissional=dependenteProfissional;
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

}
