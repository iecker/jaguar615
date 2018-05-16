/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.empresa;

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
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import org.hibernate.annotations.ForeignKey;

import com.powerlogic.jcompany.config.domain.PlcReference;
import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;

@MappedSuperclass
public abstract class Parceiros  implements Serializable {

	@Transient
	private String indExcPlc = "N";
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_PARCEIROS")
	@Column (name = "ID_PARCEIROS")
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
	
	@PlcReference(testDuplicity=true)
	@NotNull(groups=PlcEntityList.class)
	@Size(max=40)
	@Column (name = "NOME_EMPRESA")
	@org.hibernate.annotations.OrderBy(clause="order by nomeEmpresa asc")
	private String nomeEmpresa;
	
	@NotNull(groups=PlcEntityList.class)
	@RequiredIf(valueOf="nomeEmpresa",is=RequiredIfType.not_empty)
	@Column (name = "DATA_PARCERIA")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataParceria;
	
	@NotNull
	@SuppressWarnings("unchecked")
	@ManyToOne (targetEntity = EmpresaEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_PARCEIROS_EMPRESA")
	@JoinColumn (name = "ID_EMPRESA")
	private Empresa empresa;
	
	@SuppressWarnings("unchecked")
	@OneToMany (targetEntity = ContratoEntity.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="parceiros")
	@ForeignKey(name="FK_CONTRATO_PARCEIROS")
	@PlcValMultiplicity(referenceProperty="numeroContrato")
	private List<Contrato> contrato;	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id=id;
	}

	public String getNomeEmpresa() {
		return nomeEmpresa;
	}

	public void setNomeEmpresa(String nomeEmpresa) {
		this.nomeEmpresa=nomeEmpresa;
	}

	public Date getDataParceria() {
		return dataParceria;
	}

	public void setDataParceria(Date dataParceria) {
		this.dataParceria=dataParceria;
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

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa=empresa;
	}

	public List<Contrato> getContrato() {
		return contrato;
	}

	public void setContrato(List<Contrato> contrato) {
		this.contrato=contrato;
	}

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

}
