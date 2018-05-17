/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/
package com.empresa.rhtutorial2.entity.funcionario;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.envers.Audited;

import com.empresa.rhtutorial2.entity.AppBaseEntity;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;

/**
 * Histórico Profissional
 */
@MappedSuperclass

public abstract class HistoricoProfissional extends AppBaseEntity {

	
	@Id 
 	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_HISTORICO_PROFISSIONAL")
	private Long id;
	
	/*@Version
	@NotNull
	@Digits(integer=5, fraction=0)
	@Column(name="VERSAO")	
	private Integer versao = new Integer(0);*/
	
	/*@NotNull
	@Column(name="DATA_ULT_ALTERACAO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUltAlteracao = new Date();*/
	
	/*@NotNull
	@Size(max = 150)
	@Column(name = "USUARIO_ULT_ALTERACAO") 
	private String usuarioUltAlteracao = "";*/


	
	@ManyToOne (targetEntity = FuncionarioEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_HISTORICOPROFISSIONAL_FUNCIONARIO")
	@NotNull(groups=PlcValGroupEntityList.class)
	@RequiredIf(valueOf="descricao",is=RequiredIfType.not_empty)
	private Funcionario funcionario;
	
	@NotNull(groups=PlcValGroupEntityList.class)
	@RequiredIf(valueOf="id",is=RequiredIfType.not_empty)
	@Size(max = 5)
	private String descricao;
	
	@Past
	@NotNull(groups=PlcValGroupEntityList.class)
	@RequiredIf(valueOf="descricao",is=RequiredIfType.not_empty)
	@Column(length=11)
	@Temporal(TemporalType.DATE)
	private Date dataInicio;
	
	@Audited
	@Min(value=0)
	@NotNull(groups=PlcValGroupEntityList.class)
	@RequiredIf(valueOf="descricao",is=RequiredIfType.not_empty)
	@Digits(integer=8, fraction=2)
	private BigDecimal salario;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id=id;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario=funcionario;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao=descricao;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio=dataInicio;
	}

	public BigDecimal getSalario() {
		return salario;
	}

	public void setSalario(BigDecimal salario) {
		this.salario=salario;
	}

	/*public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao=versao;
	}*/

	/*public Date getDataUltAlteracao() {
		return dataUltAlteracao;
	}

	public void setDataUltAlteracao(Date dataUltAlteracao) {
		this.dataUltAlteracao=dataUltAlteracao;
	}*/

	/*public String getUsuarioUltAlteracao() {
		return usuarioUltAlteracao;
	}

	public void setUsuarioUltAlteracao(String usuarioUltAlteracao) {
		this.usuarioUltAlteracao=usuarioUltAlteracao;
	}*/

}