/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.empresa;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import org.hibernate.annotations.ForeignKey;

import br.com.plc.jcompany_fcls.entity.uf.Uf;
import br.com.plc.jcompany_fcls.entity.uf.UfEntity;

import com.powerlogic.jcompany.config.domain.PlcReference;
import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;



@MappedSuperclass

public abstract class Endereco  implements Serializable {


	@Transient
	private String indExcPlc = "N";	
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_ENDERECO")
	@Column (name = "ID_ENDERECO")
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
	@Size(max=20)
	@Column (name = "USUARIO_ULT_ALTERACAO")
	private String usuarioUltAlteracao = "";
	
	@RequiredIf(valueOf="logradouro",is=RequiredIfType.not_empty)	
	@NotNull(groups=PlcEntityList.class)
	@Size(max=40)
	@Column (name = "BAIRRO")
	private String bairro;
	
	@RequiredIf(valueOf="logradouro",is=RequiredIfType.not_empty)	
	@NotNull(groups=PlcEntityList.class)
	@Size(max=15)
	@Column (name = "CEP")
	private String cep;
	
	@PlcReference
	@NotNull(groups=PlcEntityList.class)
	@Size(max=40)
	@Column (name = "LOGRADOURO")
	private String logradouro;
	
	@RequiredIf(valueOf="logradouro",is=RequiredIfType.not_empty)	
	@NotNull(groups=PlcEntityList.class)
	@Min(0)
	@Max(999999)
	@Column (name = "NUMERO")
	private Integer numero;
	
	
	@NotNull(groups=PlcEntityList.class)
	@RequiredIf(valueOf="logradouro",is=RequiredIfType.not_empty)	
	@ManyToOne (targetEntity = UfEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_ENDERECO_UF")	
	@JoinColumn (name = "ID_UF")
	private Uf uf;

	@NotNull
	@ManyToOne (targetEntity = EmpresaEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_ENDERECO_EMPRESA")
	@JoinColumn (name = "ID_EMPRESA")
	private Empresa empresa;

	@OneToMany (targetEntity = ComplementoEntity.class, fetch = FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="endereco")
	@ForeignKey(name="FK_COMPLEMENTO_ENDERECO")
	@OrderBy("dadosComplementares")
	@PlcValMultiplicity(referenceProperty="dadosComplementares")
	private List<Complemento> complemento;
		
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id=id;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro=bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep=cep;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro=logradouro;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero=numero;
	}

	public Uf getUf() {
		return uf;
	}

	public void setUf(Uf uf) {
		this.uf=uf;
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

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa=empresa;
	}

	public List<Complemento> getComplemento() {
		return complemento;
	}

	public void setComplemento(List<Complemento> complemento) {
		this.complemento=complemento;
	}

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

}
