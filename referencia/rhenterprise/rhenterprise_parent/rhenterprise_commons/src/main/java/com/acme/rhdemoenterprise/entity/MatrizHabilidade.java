package com.acme.rhdemoenterprise.entity;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;

import com.powerlogic.jcompany.extension.manytomanymatrix.entity.OpcaoMatrix;


@MappedSuperclass
public abstract class MatrizHabilidade extends AppBaseEntity {

	
	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_MATRIZ_HABILIDADE")
	@Column (nullable=false, length=5)
	private Long id;

	
	@ManyToOne (targetEntity = FuncionarioEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_MATRIZHABILIDADE_FUNCIONARIO")
	@JoinColumn (nullable=false)
	private Funcionario funcionario;
	
	@ManyToOne (targetEntity = HabilidadeEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_MATRIZHABILIDADE_HABILIDADE")
	@JoinColumn (nullable=false)
	private Habilidade habilidade;
	
	@Transient
	private OpcaoMatrix opcaoMatrix = OpcaoMatrix.TODOS;
	
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

	public Habilidade getHabilidade() {
		return habilidade;
	}

	public void setHabilidade(Habilidade habilidade) {
		this.habilidade=habilidade;
	}
	
	public OpcaoMatrix getOpcaoMatrix() {
		return opcaoMatrix;
	}

	public void setOpcaoMatrix(OpcaoMatrix opcaoMatrix) {
		this.opcaoMatrix = opcaoMatrix;
	}

}
