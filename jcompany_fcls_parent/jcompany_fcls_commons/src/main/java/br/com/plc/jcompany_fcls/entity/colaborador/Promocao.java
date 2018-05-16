/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.colaborador;

import java.io.Serializable;
import java.math.BigDecimal;
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

import com.powerlogic.jcompany.config.domain.PlcReference;
import com.powerlogic.jcompany.controller.jsf.PlcEntityList;



@MappedSuperclass

public abstract class Promocao  implements Serializable {

	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_PROMOCAO")
	@Column (name = "ID_PROMOCAO")
	private Long id;
	
	@NotNull(groups=PlcEntityList.class)
	@Version
	@Column (name = "VERSAO")
	private Integer versao= new Integer(0);
	
	@NotNull(groups=PlcEntityList.class)
	@Column (name = "DATA_ULT_ALTERACAO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUltAlteracao = new Date();
	
	@NotNull(groups=PlcEntityList.class)
	@Column (name = "USUARIO_ULT_ALTERACAO", nullable=false)
	private String usuarioUltAlteracao = "";
	
	@NotNull(groups=PlcEntityList.class)
	@SuppressWarnings("unchecked")
	@ManyToOne (targetEntity = ColaboradorEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_PROMOCAO_COLABORADOR")
	@JoinColumn (name = "ID_COLAB")
	private Colaborador colaborador;
	
	@NotNull(groups=PlcEntityList.class)
	@RequiredIf(valueOf="id",is=RequiredIfType.not_empty)
	@Column (name = "DAT_PROMOCAO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataPromocao;
	
	@NotNull(groups=PlcEntityList.class)
	@Digits(integer=18,fraction=2)
	@RequiredIf(valueOf="dataPromocao",is=RequiredIfType.not_empty)
	@Column (name = "SALARIO")
	private BigDecimal salario;

	@Transient
	private String indExcPlc = "N";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id=id;
	}

	public Date getDataPromocao() {
		return dataPromocao;
	}

	public void setDataPromocao(Date dataPromocao) {
		this.dataPromocao=dataPromocao;
	}

	public BigDecimal getSalario() {
		return salario;
	}

	public void setSalario(BigDecimal salario) {
		this.salario=salario;
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
