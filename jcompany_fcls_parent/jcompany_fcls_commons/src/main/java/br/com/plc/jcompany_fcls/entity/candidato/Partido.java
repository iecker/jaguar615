/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.candidato;


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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import org.hibernate.annotations.ForeignKey;

import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompany.domain.validation.PlcValDuplicity;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;

@MappedSuperclass
public class Partido  implements Serializable {

	@SuppressWarnings("unchecked")
	@OneToMany (targetEntity = OutrosCandidatosEntity.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="partido")
	@ForeignKey(name="FK_OUTROSCANDIDATOS_PARTIDO")
	//TODO JPA / Validator: @org.hibernate.validator.Valid
	@PlcValDuplicity(property="nome")
	@PlcValMultiplicity(referenceProperty="nome")
	private List<OutrosCandidatos> outrosCandidatos;
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_PARTIDO")
	@Column (name = "ID_PARTIDO")
	private Long id;
	
	@Version
	@NotNull(groups=PlcEntityList.class)
	@Column (name = "VERSAO")
	private int versao;
	
	@NotNull(groups=PlcEntityList.class)
	@Column (name = "DATA_ULT_ALTERACAO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUltAlteracao = new Date();
	
	@NotNull(groups=PlcEntityList.class)
	@Column (name = "USUARIO_ULT_ALTERACAO")
	private String usuarioUltAlteracao = "";
	
	@SuppressWarnings("unchecked")
	@ManyToOne (targetEntity = CandidatoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_PARTIDO_CANDIDATO")
	@JoinColumn (name = "ID_CANDIDATO", nullable=false)
	private Candidato candidato;
	
	@Transient
	private String indExcPlc = "N";
	
	@NotNull(groups=PlcEntityList.class)
	@RequiredIf(valueOf="id",is=RequiredIfType.not_empty)
	@Size(max=40)
	@Column (name = "NOME")
	private String nome;
	
	@NotNull(groups=PlcEntityList.class)
	@RequiredIf(valueOf="nome",is=RequiredIfType.not_empty)
	@Size(max=40)
	@Column (name = "FORCA")
	private String forca;


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

	public String getForca() {
		return forca;
	}

	public void setForca(String forca) {
		this.forca=forca;
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

	public List<OutrosCandidatos> getOutrosCandidatos() {
		return outrosCandidatos;
	}

	public void setOutrosCandidatos(List<OutrosCandidatos> outrosCandidatos) {
		this.outrosCandidatos=outrosCandidatos;
	}

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

}
