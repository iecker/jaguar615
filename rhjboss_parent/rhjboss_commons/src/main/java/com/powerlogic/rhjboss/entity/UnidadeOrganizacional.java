package com.powerlogic.rhjboss.entity;

 

import java.io.Serializable;
import java.util.Date;
import javax.persistence.ManyToOne;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import javax.persistence.TemporalType;
import javax.persistence.Id;
import javax.persistence.Embedded;
import javax.persistence.Version;
import javax.persistence.MappedSuperclass;
import javax.persistence.GenerationType;
import org.hibernate.annotations.ForeignKey;
import javax.persistence.Temporal;
import javax.validation.constraints.Size;
import javax.validation.constraints.Digits;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

@MappedSuperclass
public class UnidadeOrganizacional  implements Serializable {

	@Id 
 	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_UNIDADE_ORGANIZACIONAL")
	private Long id;
	
	@Version
	@NotNull
	@Digits(integer=5, fraction=0)
	@Column(name="VERSAO")	
	private Integer versao = new Integer(0);
	
	@NotNull
	@Column(name="DATA_ULT_ALTERACAO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUltAlteracao = new Date();
	
	@NotNull
	@Size(max = 150)
	@Column(name = "USUARIO_ULT_ALTERACAO") 
	private String usuarioUltAlteracao = "";

	@NotNull
	@Size(max = 50)
	private String nome;
	
	@ManyToOne (targetEntity = UnidadeOrganizacionalEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_UNIDADEORGANIZACIONAL_UNIDADEPAI")
	private UnidadeOrganizacional unidadePai;
	
	@Embedded
	@NotNull
	@Valid
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

	public UnidadeOrganizacional getUnidadePai() {
		return unidadePai;
	}

	public void setUnidadePai(UnidadeOrganizacional unidadePai) {
		this.unidadePai=unidadePai;
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

}
