package com.empresa.commons.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.powerlogic.jcompany.commons.annotation.PlcDbFactory;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;

@PlcDbFactory(nome = "bridge")
@SPlcEntity
@Entity
@Table(name = "PREFERENCIA_USUARIO")
@SequenceGenerator(name = "SE_PREFERENCIA_USUARIO", sequenceName = "SE_PREFERENCIA_USUARIO")
@Access(AccessType.FIELD)
@NamedQueries({
		@NamedQuery(name = "PreferenciaUsuario.querySelLookup", query = "select id as id, login as login from PreferenciaUsuario where id = ? order by id asc"),
		@NamedQuery(name = "PreferenciaUsuario.querySelByLogin", query = "from PreferenciaUsuario where login = :login ") })
public class PreferenciaUsuario implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_PREFERENCIA_USUARIO")
	private Long id;

	@Version
	@NotNull
	@Digits(integer = 5, fraction = 0)
	@Column(name = "VERSAO")
	private Integer versao = new Integer(0);

	@NotNull
	@Column(name = "DATA_ULT_ALTERACAO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUltAlteracao = new Date();

	@NotNull
	@Size(max = 150)
	@Column(name = "USUARIO_ULT_ALTERACAO")
	private String usuarioUltAlteracao = "";

	@NotNull
	@Size(max = 100)
	private String login;

	@NotNull
	@Size(max = 50)
	private String pele;

	@NotNull
	@Size(max = 50)
	private String leiaute;

	public PreferenciaUsuario() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPele() {
		return pele;
	}

	public void setPele(String pele) {
		this.pele = pele;
	}

	public String getLeiaute() {
		return leiaute;
	}

	public void setLeiaute(String leiaute) {
		this.leiaute = leiaute;
	}

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public Date getDataUltAlteracao() {
		return dataUltAlteracao;
	}

	public void setDataUltAlteracao(Date dataUltAlteracao) {
		this.dataUltAlteracao = dataUltAlteracao;
	}

	public String getUsuarioUltAlteracao() {
		return usuarioUltAlteracao;
	}

	public void setUsuarioUltAlteracao(String usuarioUltAlteracao) {
		this.usuarioUltAlteracao = usuarioUltAlteracao;
	}

	@Override
	public String toString() {
		return getLogin();
	}

}
