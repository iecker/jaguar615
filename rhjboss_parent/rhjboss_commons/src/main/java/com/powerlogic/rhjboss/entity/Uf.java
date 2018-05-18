package com.powerlogic.rhjboss.entity;

 

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.persistence.TemporalType;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.persistence.MappedSuperclass;
import javax.persistence.GenerationType;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import javax.persistence.Temporal;
import javax.validation.constraints.Size;
import javax.validation.constraints.Digits;
import com.powerlogic.jcompany.config.domain.PlcReference;
import javax.persistence.GeneratedValue;

@MappedSuperclass
public class Uf  implements Serializable {
	
	@Id 
 	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_UF")
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

	@NotNull(groups=PlcValGroupEntityList.class)
	@RequiredIf(valueOf="id",is=RequiredIfType.not_empty)
	@Size(max = 50)
	@PlcReference(testDuplicity=true)
	private String nome;
	
	@NotNull(groups=PlcValGroupEntityList.class)
	@RequiredIf(valueOf="nome",is=RequiredIfType.not_empty)
	@Size(max = 2)
	private String sigla;
	 
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

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla=sigla;
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
