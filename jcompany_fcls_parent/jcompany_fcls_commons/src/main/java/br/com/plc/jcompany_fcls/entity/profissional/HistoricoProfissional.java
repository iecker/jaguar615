/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.profissional;

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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import org.hibernate.annotations.ForeignKey;

import br.com.plc.jcompany_fcls.entity.departamento.Departamento;
import br.com.plc.jcompany_fcls.entity.departamento.DepartamentoEntity;

import com.powerlogic.jcompany.controller.jsf.PlcEntityList;



@MappedSuperclass

public abstract class HistoricoProfissional  implements Serializable {

	@Transient
	private String indExcPlc = "N";	
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_HISTORICO_PROFISSIONAL")
	@Column (name = "ID_HISTORICO_PROFISSIONAL")
	private Long id;
	
	@Version
	@NotNull
	@Column (name = "VERSAO")
	@Digits(integer=5, fraction=0)
	private Integer versao = new Integer(0);
	
	@NotNull(groups=PlcEntityList.class)
	@Column (name = "DATA_ULT_ALTERACAO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUltAlteracao = new Date();
	
	@NotNull(groups=PlcEntityList.class)
	@Column (name = "USUARIO_ULT_ALTERACAO")
	private String usuarioUltAlteracao = "";
	
	@NotNull(groups=PlcEntityList.class)
	@SuppressWarnings("unchecked")
	@ManyToOne (targetEntity = ProfissionalEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_HP_PROF")
	@JoinColumn (name = "ID_PROFISSIONAL")
	private Profissional profissional;

	@ManyToOne(targetEntity = DepartamentoEntity.class, fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_DEPARTAMENTO")
	@NotNull(groups=PlcEntityList.class)
	@RequiredIf(valueOf="nome",is=RequiredIfType.not_empty)
	@ForeignKey(name="FK_HP_DEPART")
	private Departamento departamento;
	
	@NotNull(groups=PlcEntityList.class)
	@Size(max=40)
	@RequiredIf(valueOf="id",is=RequiredIfType.not_empty)
	@Column (name = "DESCRICAO")
	private String descricao;
	
	
	@Past
	@NotNull(groups=PlcEntityList.class)
	@RequiredIf(valueOf="descricao",is=RequiredIfType.not_empty)
	@Column (name = "DATA_INICIO")
	@Temporal(TemporalType.DATE)
	private Date dataInicio;
	
	@NotNull(groups=PlcEntityList.class)
	@RequiredIf(valueOf="descricao",is=RequiredIfType.not_empty)
	@Min(value=0)
	@Digits(integer=11, fraction=2)
	@Column (name = "SALARIO")
	private BigDecimal salario;
	
	/**
	 * @param historicosProfissionais relaÃ§Ã£o de histÃ³ricos
	 * @return Devolve o maior salÃ¡rio vÃ¡lido
	 */
	
	protected static BigDecimal recuperaMaiorSalario(List<HistoricoProfissional>historicoProfissionais){

		BigDecimal maiorSalario= new BigDecimal(0);

		for (Iterator<HistoricoProfissional> iter=historicoProfissionais.iterator();iter.hasNext();){
			HistoricoProfissional historicoProfissional= iter.next();  
			if (!("S".equals(historicoProfissional.getIndExcPlc()))&&historicoProfissional.getSalario() !=null &&
					historicoProfissional.getSalario().compareTo(maiorSalario)>0)
				maiorSalario=historicoProfissional.getSalario();
		}
		return maiorSalario;
	}
	
	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}
	
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

	public Profissional getProfissional() {
		return profissional;
	}

	public void setProfissional(Profissional profissional) {
		this.profissional=profissional;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

}
