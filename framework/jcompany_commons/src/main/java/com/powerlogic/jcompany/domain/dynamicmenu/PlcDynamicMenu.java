/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.domain.dynamicmenu;


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
import javax.validation.constraints.NotNull;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;

import com.powerlogic.jcompany.config.domain.PlcReference;

@MappedSuperclass
public abstract class PlcDynamicMenu  implements Serializable{
 	
	@Transient
	private transient String indExcPlc = "N";	
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_PLC_MENU_DINAMICO")
	@Column (name = "ID_PLC_MENU_DINAMICO", nullable=false, length=5)
	private Long id;
	
	@Version
	@Column (name = "VERSAO", nullable=false, length=5)
	private int versao;
	
	@Column (name = "DATA_ULT_ALTERACAO", nullable=false, length=11)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUltAlteracao = new Date();
	
	@Column (name = "USUARIO_ULT_ALTERACAO", nullable=false)
	private String usuarioUltAlteracao = "";

	@RequiredIf(valueOf="id",is=RequiredIfType.not_empty)
	//@NotNull(groups=PlcEntityList.class)	
	@PlcReference(testDuplicity=true)
	@Column (name = "NOME", nullable=false, length=30)
	private String nome;
	
	//@NotNull(groups=PlcEntityList.class)
	@RequiredIf(valueOf="nome",is=RequiredIfType.not_empty)
	@Column (name = "URL", nullable=false, length=250)
	private String url;

	
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

	
	
}
