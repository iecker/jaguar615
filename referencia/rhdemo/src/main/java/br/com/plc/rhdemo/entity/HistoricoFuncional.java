/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package br.com.plc.rhdemo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import org.hibernate.annotations.ForeignKey;

import com.powerlogic.jcompany.config.domain.PlcReference;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;



@MappedSuperclass

public abstract class HistoricoFuncional  implements Serializable {


	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_HISTORICO_FUNCIONAL")
	@Column (name = "ID_HISTORICO_FUNCIONAL", nullable=false, length=5)
	private Long id;

	@Version
	@NotNull
	@Column (name = "VERSAO")
	@Digits(integer=5, fraction=0)
	private int versao;

	@NotNull
	@Column (name = "DATA_ULT_ALTERACAO")
	private Date dataUltAlteracao = new Date();

	@NotNull
	@Size(max=150)
	@Column (name = "USUARIO_ULT_ALTERACAO")
	private String usuarioUltAlteracao = "";

	@NotNull
	@ManyToOne (targetEntity = FuncionarioEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_HISTORICOFUNCIONAL_FUNCIONARIO")
	@JoinColumn 
	private Funcionario funcionario;

	@NotNull(groups=PlcValGroupEntityList.class) 
	@Column
	@Size(max = 40)
	private String descricao;
	
	@Past
    @RequiredIf(valueOf="descricao",is=RequiredIfType.not_empty) 
	@NotNull(groups=PlcValGroupEntityList.class) 
	@Column(length=11)
	@Temporal(TemporalType.DATE)	 
	private Date dataInicio;

    public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	@RequiredIf(valueOf="descricao",is=RequiredIfType.not_empty) 
    @NotNull(groups=PlcValGroupEntityList.class)
    @Digits(integer=11, fraction=2)
	@Column
	private BigDecimal salario;

	@Transient
	private String indExcPlc = "N";	


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

	public BigDecimal getSalario() {
		return salario;
	}

	public void setSalario(BigDecimal salario) {
		this.salario=salario;
	}

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
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

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario=funcionario;
	}

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

	protected static BigDecimal recuperaMaiorSalario(List<HistoricoFuncional>historicoFuncionais){

		BigDecimal maiorSalario= new BigDecimal(0);

		for (Iterator<HistoricoFuncional> iter=historicoFuncionais.iterator();iter.hasNext();){
			HistoricoFuncional historicoFuncional= iter.next();  
			if (!("S".equals(historicoFuncional.getIndExcPlc()))&&historicoFuncional.getSalario() !=null &&
					historicoFuncional.getSalario().compareTo(maiorSalario)>0)
				maiorSalario=historicoFuncional.getSalario();
		}
		return maiorSalario;
	}

}
