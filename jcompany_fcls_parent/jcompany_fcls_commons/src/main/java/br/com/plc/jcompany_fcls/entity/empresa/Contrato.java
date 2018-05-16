/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.empresa;


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

public abstract class Contrato  implements Serializable {

	@Transient
	private String indExcPlc = "N";	

	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_CONTRATO")
	@Column (name = "ID_CONTRATO")
	private Long id;
	
	@Version
	@NotNull
	@Column (name = "VERSAO")
	@Digits(integer=5, fraction=0)
	private Integer versao = new Integer(0);
	
	@NotNull
	@Column (name = "DATA_ULT_ALTERACAO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUltAlteracao = new Date();
	
	@NotNull
	@Size(max=20)
	@Column (name = "USUARIO_ULT_ALTERACAO")
	private String usuarioUltAlteracao = "";
	
	@NotNull
	@SuppressWarnings("unchecked")
	@ManyToOne (targetEntity = ParceirosEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_CONTRATO_PARCEIROS")
	@JoinColumn (name = "ID_PARCEIROS")
	private Parceiros parceiros;

	@PlcReference
	@RequiredIf(valueOf="id",is=RequiredIfType.not_empty)
	@NotNull(groups=PlcEntityList.class)
	@Size(max=10)
	@Column (name = "NUMERO_CONTRATO")
	private String numeroContrato;
	
	@RequiredIf(valueOf="numeroContrato",is=RequiredIfType.not_empty)
	@NotNull(groups=PlcEntityList.class)
	@Size(max=40)
	@Column (name = "ESCOPO_CONTRATO")
	private String escopoContrato;

    /*
     * Construtor sem parâmetros.
     */
    public Contrato() {
    }



	public String getEscopoContrato() {
		return escopoContrato;
	}

	public void setEscopoContrato(String escopoContrato) {
		this.escopoContrato=escopoContrato;
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

	public Parceiros getParceiros() {
		return parceiros;
	}

	public void setParceiros(Parceiros parceiros) {
		this.parceiros=parceiros;
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getNumeroContrato() {
		return numeroContrato;
	}



	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}



	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}



	public String getIndExcPlc() {
		return indExcPlc;
	}

}
