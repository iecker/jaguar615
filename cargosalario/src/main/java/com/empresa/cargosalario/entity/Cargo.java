package com.empresa.cargosalario.entity;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.persistence.TemporalType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.persistence.GenerationType;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Digits;
import javax.persistence.GeneratedValue;


@MappedSuperclass
public class Cargo  implements Serializable {

	@Id 
 	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_CARGO")
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
	@Size(max = 10)
	private String codigo;
	
	@NotNull
	@Size(max = 100)
	private String descricao;
	
	@NotNull
	@Digits(integer=8, fraction=2)
	private BigDecimal salario;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id=id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo=codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao=descricao;
	}

	public BigDecimal getSalario() {
		return salario;
	}

	public void setSalario(BigDecimal salario) {
		this.salario=salario;
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
