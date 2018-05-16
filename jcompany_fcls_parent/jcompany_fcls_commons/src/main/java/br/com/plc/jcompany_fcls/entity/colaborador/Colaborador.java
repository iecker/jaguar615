/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.colaborador;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.validator.constraints.Email;

import br.com.plc.jcompany_fcls.entity.funcionario.Sexo;

import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompany.domain.validation.PlcValCpf;
import com.powerlogic.jcompany.domain.validation.PlcValDuplicity;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;


@MappedSuperclass

public abstract class Colaborador  implements Serializable {

	@SuppressWarnings("unchecked")
	@OneToMany (targetEntity = PromocaoEntity.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="colaborador")
	@ForeignKey(name="FK_PROMOCAO_COLAB")
	//TODO JPA / Validator: org.hibernate.validator.Valid
	@PlcValDuplicity(property="dataPromocao")
	@PlcValMultiplicity(referenceProperty="dataPromocao")
	@OrderBy("id")
	private List<Promocao> promocao;

	@SuppressWarnings("unchecked")
	@OneToMany (targetEntity = SetorEntity.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="colaborador")
	@ForeignKey(name="FK_SETOR_COLAB")
	//TODO JPA / Validator: org.hibernate.validator.Valid
	@PlcValDuplicity(property="nome")
	@PlcValMultiplicity(referenceProperty="nome")
	@OrderBy("id")
	private List<Setor> setor;
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_COLAB")
	@Column (name = "ID_COLAB")
	private Long id;
	
	@Version
	@NotNull
	@Column (name = "VERSAO")
	@Digits(integer=5, fraction=0)
	private Integer versao = new Integer(0);
	
	@NotNull(groups=PlcEntityList.class)
	@Column (name = "DATA_ULT_ALTERACAO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUltAlteracao = new Date();
	
	@NotNull(groups=PlcEntityList.class)
	@Column (name = "USUARIO_ULT_ALTERACAO")
	private String usuarioUltAlteracao = "";

	@NotNull(groups=PlcEntityList.class)
	@Size(max=40)
	@Column (name = "NOM")
	private String nome;
	
	@NotNull(groups=PlcEntityList.class)
	@Size(max=15)
	@PlcValCpf
	@Column (name = "CPF")
	private String cpf;
	
	@NotNull(groups=PlcEntityList.class)
	@Column (name = "DAT_CAD")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro;
	
	@NotNull(groups=PlcEntityList.class)
	@Size(max=20)
	@Column (name = "EMAIL")
	@Email(message="{validator.email}")
	private String email;
	
	@NotNull(groups=PlcEntityList.class)
	@Size(max=40)
	@Column (name = "RESP_CAD")
	private String responsavelCadastro;
	
	@NotNull(groups=PlcEntityList.class)
	@Enumerated(EnumType.STRING)
	@Column (name = "ESTDO_CIVIL")
	private EstadoCivil estadoCivil;
	
	@Enumerated(EnumType.STRING)
	@Column (name = "SEXO", nullable=false, length=1)
	private Sexo sexo;
	
	@Transient
	private String indExcPlc = "N";
	
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

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro=dataCadastro;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email=email;
	}

	public String getResponsavelCadastro() {
		return responsavelCadastro;
	}

	public void setResponsavelCadastro(String responsavelCadastro) {
		this.responsavelCadastro=responsavelCadastro;
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

	public List<Setor> getSetor() {
		return setor;
	}

	public void setSetor(List<Setor> setor) {
		this.setor=setor;
	}

	public List<Promocao> getPromocao() {
		return promocao;
	}

	public void setPromocao(List<Promocao> promocao) {
		this.promocao=promocao;
	}

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

}
