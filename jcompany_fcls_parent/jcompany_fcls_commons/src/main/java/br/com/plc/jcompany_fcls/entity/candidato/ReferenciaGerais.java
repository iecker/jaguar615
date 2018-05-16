/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.candidato;


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

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import org.hibernate.annotations.ForeignKey;

import com.powerlogic.jcompany.controller.jsf.PlcEntityList;

@MappedSuperclass
public class ReferenciaGerais  implements Serializable {

	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_REFERENCIA_GERAIS")
	@Column (name = "ID_REFERENCIA_GERAIS")
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
	
	@SuppressWarnings("unchecked")
	@ManyToOne (targetEntity = CandidatoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_REFERENCIAGERAIS_CANDIDATO")
	@JoinColumn (name = "ID_CANDIDATO", nullable=false)
	private Candidato candidato;
	
	@Transient
	private String indExcPlc = "N";	
	
	@NotNull(groups=PlcEntityList.class)
	@RequiredIf(valueOf="id",is=RequiredIfType.not_empty)
	@Column (name = "INDICACAO_PARTIDARIA", nullable=false, length=40)
	private String indicacaoPartidaria;
	
	@NotNull(groups=PlcEntityList.class)
	@RequiredIf(valueOf="indicacaoPartidaria",is=RequiredIfType.not_empty)
	@Column (name = "VOTOS", nullable=false, length=50)
	private String votos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id=id;
	}

	public String getIndicacaoPartidaria() {
		return indicacaoPartidaria;
	}

	public void setIndicacaoPartidaria(String indicacaoPartidaria) {
		this.indicacaoPartidaria=indicacaoPartidaria;
	}

	public String getVotos() {
		return votos;
	}

	public void setVotos(String votos) {
		this.votos=votos;
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

	public Candidato getCandidato() {
		return candidato;
	}

	public void setCandidato(Candidato candidato) {
		this.candidato=candidato;
	}

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

}
