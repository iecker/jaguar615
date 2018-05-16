/*
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.preferencia;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.powerlogic.jcompany.controller.jsf.PlcEntityList;

/**
 * @author igor.guimaraes
 *
 */
@MappedSuperclass

public class PreferenciaAplicacao  implements Serializable {

	
	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_PREFERENCIA_APLICACAO")
	@Column (name = "ID_PREFERENCIA_APLICACAO")
	private Long id;
	
	@NotNull(groups=PlcEntityList.class)
	@Version
	@Column (name = "VERSAO")
	private int versao;
	
	@NotNull(groups=PlcEntityList.class)
	@Column (name = "DATA_ULT_ALTERACAO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUltAlteracao = new Date();
	
	@NotNull(groups=PlcEntityList.class)
	@Column (name = "USUARIO_ULT_ALTERACAO")
	private String usuarioUltAlteracao = "";

	@NotNull(groups=PlcEntityList.class)
	@Digits(integer=5,fraction=0)
	@Column (name = "TIME_OUT")
	private Integer timeOut;
	
	@NotNull(groups=PlcEntityList.class)
	@Digits(integer=10,fraction=0)
	@Column (name = "NUM_MAX_TENTATIVAS_LOGIN")
	private Integer numMaxTentativasLogin;
	
	@NotNull(groups=PlcEntityList.class)
	@Size(max=500)
	@Column (name = "MENSAGEM_BOAS_VINDAS")
	private String mensagemBoasVindas;
	
	@NotNull(groups=PlcEntityList.class)
	@Size(max=100)
	@Column (name = "SERVIDOR_EMAIL")
	private String servidorEmail;
	
	@NotNull(groups=PlcEntityList.class)
	@Column (name = "DATA_EXPIRACAO_SENHA")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataExpiracaoSenha;
	
	@Transient
	private String indExcPlc = "N";		
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id=id;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut=timeOut;
	}

	public int getNumMaxTentativasLogin() {
		return numMaxTentativasLogin;
	}

	public void setNumMaxTentativasLogin(int numMaxTentativasLogin) {
		this.numMaxTentativasLogin=numMaxTentativasLogin;
	}

	public String getMensagemBoasVindas() {
		return mensagemBoasVindas;
	}

	public void setMensagemBoasVindas(String mensagemBoasVindas) {
		this.mensagemBoasVindas=mensagemBoasVindas;
	}

	public String getServidorEmail() {
		return servidorEmail;
	}

	public void setServidorEmail(String servidorEmail) {
		this.servidorEmail=servidorEmail;
	}

	public Date getDataExpiracaoSenha() {
		return dataExpiracaoSenha;
	}

	public void setDataExpiracaoSenha(Date dataExpiracaoSenha) {
		this.dataExpiracaoSenha=dataExpiracaoSenha;
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

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

}
