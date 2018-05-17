package com.empresa.rhtutorial2.entity.funcionario.pendencia;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;

import com.empresa.rhtutorial2.entity.funcionario.Funcionario;
import com.empresa.rhtutorial2.entity.funcionario.FuncionarioEntity;


@MappedSuperclass
@NamedQueries({
	@NamedQuery(name="Pendencia.querySel", query="select id as id from Pendencia order by id asc")
})
public class Pendencia  implements Serializable {

	
	@Id 
 	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_PENDENCIA")
	private Long id;
	
	@Version
	@NotNull
	@Digits(integer=5, fraction=0)
	@Column(name="VERSAO")	
	private Integer versao = new Integer(0);

	
	@ManyToOne (targetEntity = FuncionarioEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_PENDENCIA_FUNCIONARIO")
	@NotNull
	private Funcionario funcionario;
	
	@NotNull
	@Size(max = 50)
	private String descricao;
	
	@NotNull
	@Digits(integer=11, fraction=2)
	private BigDecimal valor;	
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

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor=valor;
	}

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao=versao;
	}

}
