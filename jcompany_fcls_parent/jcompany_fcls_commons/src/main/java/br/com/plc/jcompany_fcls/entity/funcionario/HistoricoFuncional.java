/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.funcionario;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
import javax.persistence.Version;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import org.hibernate.annotations.ForeignKey;

import br.com.plc.jcompany_fcls.entity.departamento.Departamento;
import br.com.plc.jcompany_fcls.entity.departamento.DepartamentoEntity;
import br.com.plc.jcompany_fcls.entity.uf.Uf;
import br.com.plc.jcompany_fcls.entity.uf.UfEntity;

import com.powerlogic.jcompany.config.domain.PlcReference;
import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompany.domain.type.PlcYesNo;



@MappedSuperclass

public abstract class HistoricoFuncional  implements Serializable {



	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_HISTORICO_FUNCIONAL")
	@Column (name = "ID_HISTORICO_FUNCIONAL")
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
	@ManyToOne (targetEntity = FuncionarioEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_HF_FUNCIONARIO")
	@JoinColumn (name = "ID_FUNCIONARIO")
	private Funcionario funcionario;

	@RequiredIf(valueOf="id",is=RequiredIfType.not_empty)
	@NotNull(groups=PlcEntityList.class)
	@Size(max=40)
	@Column (name = "DESCRICAO")
	private String descricao;
	
	@RequiredIf(valueOf="descricao",is=RequiredIfType.not_empty)
	@NotNull(groups=PlcEntityList.class)
	@Column (name = "DATA")
	private Date data;

	@NotNull(groups=PlcEntityList.class)
	@Digits(integer=18,fraction=2)
	@RequiredIf(valueOf="descricao",is=RequiredIfType.not_empty)
	@Column (name = "SALARIO")
	private BigDecimal salario;

	@NotNull(groups=PlcEntityList.class)
	@RequiredIf(valueOf="descricao",is=RequiredIfType.not_empty)
	@ManyToOne(targetEntity = DepartamentoEntity.class, fetch = FetchType.EAGER)
	@ForeignKey(name = "FK_HF_DEPARTAMENTO")
	@JoinColumn(name = "ID_DEPARTAMENTO")
	private Departamento departamento;

	@NotNull(groups=PlcEntityList.class)
	@RequiredIf(valueOf="descricao",is=RequiredIfType.not_empty)
	@Enumerated(EnumType.STRING)
	@Column(name = "SEXO")
	private Sexo sexo;

	@NotNull(groups=PlcEntityList.class)
	@RequiredIf(valueOf="descricao",is=RequiredIfType.not_empty)
	@Enumerated(EnumType.STRING)
	@Column(name = "TEM_CURSO_SUPERIOR")
	private PlcYesNo temCursoSuperior;
	
	@NotNull(groups=PlcEntityList.class)
	@RequiredIf(valueOf="descricao",is=RequiredIfType.not_empty)
	@ManyToOne (targetEntity = UfEntity.class, fetch = FetchType.LAZY)
	@JoinColumn (name = "ID_UF")
	private Uf uf;
	
	
	
	
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

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}


	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public PlcYesNo getTemCursoSuperior() {
		return temCursoSuperior;
	}

	public void setTemCursoSuperior(PlcYesNo temCursoSuperior) {
		this.temCursoSuperior = temCursoSuperior;
	}

	public Uf getUf() {
		return uf;
	}

	public void setUf(Uf uf) {
		this.uf = uf;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}
}
