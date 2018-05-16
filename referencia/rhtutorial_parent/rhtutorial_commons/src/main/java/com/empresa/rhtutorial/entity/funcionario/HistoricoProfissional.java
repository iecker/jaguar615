/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/
package com.empresa.rhtutorial.entity.funcionario;

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
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import org.hibernate.annotations.ForeignKey;

import com.empresa.rhtutorial.entity.AppBaseEntity;
import com.powerlogic.jcompany.config.domain.PlcReference;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;

/**
 * HistÃ³rico Profissional
 */
@MappedSuperclass
public abstract class HistoricoProfissional extends AppBaseEntity {

	private static int SAL_MIN_CURSO_SUPERIOR=1000;
	
	@Id 
 	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_HISTORICO_PROFISSIONAL")
	@Column(nullable=false,length=5)
	private Long id;
	
	@ManyToOne (targetEntity = FuncionarioEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_HISTORICOPROFISSIONAL_FUNCIONARIO")
	@NotNull
	@JoinColumn
	private Funcionario funcionario;
	
	@NotNull(groups=PlcValGroupEntityList.class) 
	@Size(max = 40)
	@Column
//	@PlcReference(testDuplicity=true)
	private String descricao;
	
	@Past
    @RequiredIf(valueOf="descricao",is=RequiredIfType.not_empty) 
	@NotNull(groups=PlcValGroupEntityList.class) 
	@Column(length=11)
	@Temporal(TemporalType.DATE)
	private Date dataInicio;
	
	@Min(value=0)
    @RequiredIf(valueOf="descricao",is=RequiredIfType.not_empty) 
	@NotNull(groups=PlcValGroupEntityList.class) 
	@Column(length=11, precision=11, scale=2)
	private BigDecimal salario;


	/**
	 * @return true Se funcionÃ¡rio nÃ£o tiver curso superior ou tiver e salario for maior que 1.000,000
	 */
	@AssertTrue(message="{funcionario.valida.salario}")
	@Transient
	public boolean isSalarioValido() {
		
		if (!this.funcionario.getTemCursoSuperior() || this.salario == null)
			return true;
		
		return this.funcionario.getTemCursoSuperior() && 
		     this.salario.compareTo(new BigDecimal(SAL_MIN_CURSO_SUPERIOR))>=0;
		
	}
	
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

}