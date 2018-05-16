/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.test.masterdetail;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

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
import javax.validation.constraints.Min;

import org.hibernate.annotations.ForeignKey;

import com.powerlogic.jcompany.config.domain.PlcReference;


/**
 * Histórico Profissional
 */
@MappedSuperclass


public abstract class ProfissionalHistory extends AppBaseEntity {

	
	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_HISTORICO_PROFISSIONAL")
	@Column (name = "ID_HISTORICO_PROFISSIONAL", nullable=false, length=5)
	private Long id;
	
	@SuppressWarnings("unchecked")
	@ManyToOne (targetEntity = EmployeeEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_HISTORICOPROFISSIONAL_FUNCIONARIO")
	@JoinColumn (name = "ID_FUNCIONARIO", nullable=false)
	private Employee funcionario;
	
	@Column (name = "DESCRICAO", nullable=false, length=40)
	@PlcReference
	private String descricao;
	
	@Column (name = "DATA_INICIO", nullable=false, length=11)
	@Temporal(TemporalType.DATE)
	private java.util.Date dataInicio;
	
	@Min(value=0)
	@Column (name = "SALARIO", nullable=false, length=11, precision=11, scale=2)
	private java.math.BigDecimal salario;
	
	/**
	 * @param historicosProfissionais relação de históricos
	 * @return Devolve o maior salário válido
	 */
	protected static BigDecimal recuperaMaiorSalario(List<ProfissionalHistory> historicosProfissionais) {
		
		BigDecimal maiorSalario = new BigDecimal(0);
		
		if (historicosProfissionais != null) {
			for (Iterator iterator = historicosProfissionais.iterator(); iterator.hasNext();) {
				ProfissionalHistoryEntity historicoProfissional = (ProfissionalHistoryEntity) iterator.next();
				if (!"S".equals(historicoProfissional.getIndExcPlc()) && historicoProfissional.getSalario()!=null &&
						historicoProfissional.getSalario().compareTo(maiorSalario)>0)
					maiorSalario=historicoProfissional.getSalario();
			}
		}
		return maiorSalario;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id=id;
	}

	public Employee getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Employee funcionario) {
		this.funcionario=funcionario;
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

}