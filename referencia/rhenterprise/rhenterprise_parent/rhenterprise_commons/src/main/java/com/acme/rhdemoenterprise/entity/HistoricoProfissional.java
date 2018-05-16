/*  																													
	    				   jCompany Developer Suite																		
			    		Copyright (C) 2008  Powerlogic																	
	 																													
	    Este programa Ã© licenciado com todos os seus cÃ³digos fontes. VocÃª pode modificÃ¡-los e							
	    utilizÃ¡-los livremente, inclusive distribuÃ­-los para terceiros quando fizerem parte de algum aplicativo 		
	    sendo cedido, segundo os termos de licenciamento gerenciado de cÃ³digo aberto da Powerlogic, definidos			
	    na licenÃ§a 'Powerlogic Open-Source Licence 2.0 (POSL 2.0)'.    													
	  																													
	    A Powerlogic garante o acerto de erros eventualmente encontrados neste cÃ³digo, para os clientes licenciados, 	
	    desde que todos os cÃ³digos do programa sejam mantidos intactos, sem modificaÃ§Ãµes por parte do licenciado. 		
	 																													
	    VocÃª deve ter recebido uma cÃ³pia da licenÃ§a POSL 2.0 juntamente com este programa.								
	    Se nÃ£o recebeu, veja em <http://www.powerlogic.com.br/licencas/posl20/>.										
	 																													
	    Contatos: plc@powerlogic.com.br - www.powerlogic.com.br 														
																														
 */ 
package com.acme.rhdemoenterprise.entity;



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
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import org.hibernate.annotations.ForeignKey;

import com.powerlogic.jcompany.config.domain.PlcReference;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;
/**
 * HistÃ³rico Profissional
 */
@MappedSuperclass
public abstract class HistoricoProfissional extends AppBaseEntity {
	
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_HISTORICO_PROFISSIONAL")	
	private Long id;
	
	@ManyToOne (targetEntity = FuncionarioEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_HISTORICOPROFISSIONAL_FUNCIONARIO")
	@JoinColumn (nullable=false)
	private Funcionario funcionario;
	
	@NotNull(groups=PlcValGroupEntityList.class)
	@Column 
	@Size(max = 50)
	@PlcReference(testDuplicity=true)
	private String descricao;
	
	@NotNull(groups=PlcValGroupEntityList.class)
	@Column 
	@Temporal(TemporalType.TIMESTAMP)
	@RequiredIf(valueOf="descricao",is=RequiredIfType.not_empty)
	private java.util.Date dataInicio;
	
	@NotNull(groups=PlcValGroupEntityList.class)
	@Column 
	@Digits(integer=11, fraction=2)
	@RequiredIf(valueOf="descricao",is=RequiredIfType.not_empty)
	private java.math.BigDecimal salario;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id=id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao=descricao;
	}

	public java.util.Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(java.util.Date dataInicio) {
		this.dataInicio=dataInicio;
	}

	public java.math.BigDecimal getSalario() {
		return salario;
	}

	public void setSalario(java.math.BigDecimal salario) {
		this.salario=salario;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario=funcionario;
	}

}