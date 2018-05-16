/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/  
package com.empresa.rhtutorial.entity.proventodesconto;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import org.hibernate.annotations.ForeignKey;

import com.empresa.rhtutorial.entity.AppBaseEntity;
import com.empresa.rhtutorial.entity.funcionario.Funcionario;
import com.empresa.rhtutorial.entity.funcionario.FuncionarioEntity;
import com.powerlogic.jcompany.config.domain.PlcReference;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;

/**
 * @author Powerlogic
 * @version 6.0
 */
@MappedSuperclass

@SuppressWarnings("serial")
public abstract class ProventoDesconto extends AppBaseEntity {

	
	@Id 
 	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_PROVENTO_DESCONTO")
	@Column(nullable=false,length=5)
	private Long id;

	private static final BigDecimal MAX_DIAS_TRABALHADOS = new BigDecimal(22.00);
	
	@Past
	@NotNull
	@Column(length=8)
	@Temporal(TemporalType.DATE)
	private Date anoMesReferencia;
	
	@NotNull(groups=PlcValGroupEntityList.class) 
	@Size(max = 40)
	@Column(length=40)
	@PlcReference
	private String descricao;
	
	@RequiredIf(valueOf="descricao",is=RequiredIfType.not_empty) 
	@Enumerated(EnumType.STRING)
	@NotNull(groups=PlcValGroupEntityList.class) 
	@Column(length=8)
	public NaturezaProventoDesconto naturezaProventoDesconto;
	
	@RequiredIf(valueOf="descricao",is=RequiredIfType.not_empty) 
	@NotNull(groups=PlcValGroupEntityList.class) 
	@Column(length=11, precision=11, scale=2)
	private BigDecimal valor;
	
	@ManyToOne (targetEntity = FuncionarioEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_PROVENTODESCONTO_FUNCIONARIO")
	@NotNull
	@JoinColumn
	private Funcionario funcionario;
	
	/**
	 * Indica que a rotina de cÃ¡lculo de folha estÃ¡ preenchendo dados desta entidade
	 */
	@Transient
	private transient boolean estaCalculandoFolha=false;
	
	public void setEstaCalculandoFolha(boolean estaCalculandoFolha) {
		this.estaCalculandoFolha = estaCalculandoFolha;
	}

	@AssertTrue(message="NÃ£o Ã© permitido informar valores para SalÃ¡rio e Imposto de Renda")
	public boolean isValidaNaturezaCalculada() {
		return (estaCalculandoFolha &&
				(NaturezaProventoDesconto.IR.equals(this.naturezaProventoDesconto) ||
				 NaturezaProventoDesconto.SALARIO.equals(this.naturezaProventoDesconto))
				 ) 
				 ||	
			   (!estaCalculandoFolha &&
				(NaturezaProventoDesconto.DIASTRAB.equals(this.naturezaProventoDesconto) ||
				 NaturezaProventoDesconto.PROVENTO.equals(this.naturezaProventoDesconto) ||
				 NaturezaProventoDesconto.DESCONTO.equals(this.naturezaProventoDesconto))
				 );	
	}
	
	@AssertTrue(message="O valor de dias trabalhados nÃ£o pode ser superior a 22,00")
	public boolean isValidoValorDiasTrabalhados() {
		if (NaturezaProventoDesconto.DIASTRAB.equals(this.naturezaProventoDesconto) &&
			MAX_DIAS_TRABALHADOS.compareTo(valor)<0)
			return false;
		else
			return true;	
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id=id;
	}

	public Date getAnoMesReferencia() {
		return anoMesReferencia;
	}

	public void setAnoMesReferencia(Date anoMesReferencia) {
		this.anoMesReferencia=anoMesReferencia;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao=descricao;
	}

	public NaturezaProventoDesconto getNaturezaProventoDesconto() {
		return naturezaProventoDesconto;
	}

	public void setNaturezaProventoDesconto(NaturezaProventoDesconto naturezaProventoDesconto) {
		this.naturezaProventoDesconto=naturezaProventoDesconto;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor=valor;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario=funcionario;
	}

}