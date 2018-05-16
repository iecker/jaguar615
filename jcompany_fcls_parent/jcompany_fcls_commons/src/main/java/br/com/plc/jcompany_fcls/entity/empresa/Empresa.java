/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.empresa;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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

import com.powerlogic.jcompany.domain.validation.PlcValDuplicity;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;

@MappedSuperclass

public abstract class Empresa  implements Serializable {

	@Transient
	private String indExcPlc = "N";	
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_EMPRESA")
	@Column (name = "ID_EMPRESA")
	private Long id;
	
	@Version
	@NotNull
	@Column (name = "VERSAO")
	@Digits(integer=5, fraction=0)
	private Integer versao = new Integer(0);
	
	@NotNull
	@Column (name = "DATA_ULT_ALTERACAO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUltAlteracao = new Date();
	
	@NotNull
	@Column (name = "USUARIO_ULT_ALTERACAO")
	private String usuarioUltAlteracao = "";

	@Size(max=40)
	@NotNull
	@Column (name = "NOME")
	private String nome;
	
	@NotNull
	@Size(max=20)
	@Column (name = "CNPJ_BASICO")
	private String cnpjBasico;
	
	@NotNull
	@Digits(integer=8, fraction=2)
	@Column (name = "CAPITAL_SOCIAL")
	private BigDecimal capitalSocial;
	
	@NotNull
	@Column (name = "DATA_INICIO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataInicio;
	
	@SuppressWarnings("unchecked")
	@OneToMany (targetEntity = EnderecoEntity.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="empresa")
	@ForeignKey(name="FK_ENDERECO_EMPRESA")
	@OrderBy("logradouro")
	private List<Endereco> endereco;
	
	@SuppressWarnings("unchecked")
	@OneToMany (targetEntity = ParceirosEntity.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="empresa")
	@ForeignKey(name="FK_PARCEIROS_EMPRESA")
	@PlcValDuplicity(property="nomeEmpresa")
    @PlcValMultiplicity(min=0, max=4, referenceProperty="nomeEmpresa")
	private List<Parceiros> parceiros;
 
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

	public BigDecimal getCapitalSocial() {
		return capitalSocial;
	}

	public void setCapitalSocial(BigDecimal capitalSocial) {
		this.capitalSocial=capitalSocial;
	}
	
	public String getCnpjBasico() {
		return cnpjBasico;
	}

	public void setCnpjBasico(String cnpjBasico) {
		this.cnpjBasico=cnpjBasico;
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

	public List<Endereco> getEndereco() {
		return endereco;
	}

	public void setEndereco(List<Endereco> endereco) {
		this.endereco=endereco;
	}

	public List<Parceiros> getParceiros() {
		return parceiros;
	}

	public void setParceiros(List<Parceiros> parceiros) {
		this.parceiros=parceiros;
	}

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

}
