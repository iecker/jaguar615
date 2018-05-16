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


public abstract class PreferenciaUsuario  implements Serializable {

	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_PREFERENCIA_USUARIO")
	@Column (name = "ID_PREFERENCIA_USUARIO")
	private Long id;
	
	/**
	 * Login do usuário.
	 */
	@NotNull(groups=PlcEntityList.class)
	@Size(max=50)
	@Column (name="LOGIN")
	private String login;
	
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
	@Column (name = "USUARIO_ULT_ALTERACAO", nullable=false)
	private String usuarioUltAlteracao = "";
	
	@NotNull(groups=PlcEntityList.class)
	@Size(max=100)
	@Column (name = "CAMINHO_RAIZ")
	private String caminhoRaiz;
	
	@NotNull(groups=PlcEntityList.class)
	@Size(max=100)
	@Column (name = "PAGINA_INICIAL")
	private String paginaInicial;
	
	@NotNull(groups=PlcEntityList.class)
	@Digits(integer=10,fraction=0)
	@Column (name = "NUMERO_PAGINACAO")
	private Integer numeroPaginacao;
	
	@NotNull(groups=PlcEntityList.class)
	@Column (name = "DATA_ANIVERSARIO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAniversario;
	
	@Transient
	private String indExcPlc = "N";	
		
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id=id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getCaminhoRaiz() {
		return caminhoRaiz;
	}

	public void setCaminhoRaiz(String caminhoRaiz) {
		this.caminhoRaiz=caminhoRaiz;
	}

	public String getPaginaInicial() {
		return paginaInicial;
	}

	public void setPaginaInicial(String paginaInicial) {
		this.paginaInicial=paginaInicial;
	}

	public int getNumeroPaginacao() {
		return numeroPaginacao;
	}

	public void setNumeroPaginacao(int numeroPaginacao) {
		this.numeroPaginacao=numeroPaginacao;
	}

	public Date getDataAniversario() {
		return dataAniversario;
	}

	public void setDataAniversario(Date dataAniversario) {
		this.dataAniversario=dataAniversario;
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
