/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.colaborador;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import org.hibernate.annotations.ForeignKey;

import com.powerlogic.jcompany.config.domain.PlcReference;
import com.powerlogic.jcompany.controller.jsf.PlcEntityList;


@MappedSuperclass

public abstract class Setor  implements Serializable {

	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_SETOR")
	@Column (name = "ID_SETOR")
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
	@SuppressWarnings("unchecked")
	@ManyToOne (targetEntity = ColaboradorEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_SETOR_COLABORADOR")
	@JoinColumn (name = "ID_COLAB")
	private Colaborador colaborador;
	
	@NotNull(groups=PlcEntityList.class)
	@Size(max=40)
	@RequiredIf(valueOf="id",is=RequiredIfType.not_empty)
	@Column (name = "NOM")
	private String nome;
	
	@NotNull(groups=PlcEntityList.class)
	@Size(max=5)
	@RequiredIf(valueOf="ramal",is=RequiredIfType.not_empty)
	@Column (name = "RMAL")
	private String ramal;
	
	@NotNull(groups=PlcEntityList.class)
	@Size(max=5)
	@RequiredIf(valueOf="ramal",is=RequiredIfType.not_empty)
	@Column (name = "SIG")
	private String sigla;

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

	public String getRamal() {
		return ramal;
	}

	public void setRamal(String ramal) {
		this.ramal=ramal;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla=sigla;
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

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador=colaborador;
	}

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

}
