/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.test.masterdetail;

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

import org.hibernate.annotations.ForeignKey;

import com.powerlogic.jcompany.config.domain.PlcReference;


/**
 * Dependente
 */
@MappedSuperclass


public abstract class Dependent extends AppBaseEntity {

	
	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_DEPENDENTE")
	@Column (name = "ID_DEPENDENTE", nullable=false, length=5)
	private Long id;

	@Column (name = "NOME", nullable=false, length=40)
	@PlcReference
	private String nome;
	
	@Enumerated(EnumType.STRING)
	@Column (name = "SEXO", nullable=false, length=1)
	private Sex sexo;
	
	@SuppressWarnings("unchecked")
	@ManyToOne (targetEntity = EmployeeEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_DEPENDENTE_FUNCIONARIO")
	@JoinColumn (name = "ID_FUNCIONARIO", nullable=false)
	private Employee funcionario;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id=id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome=nome;
	}

	public Sex getSexo() {
		return sexo;
	}

	public void setSexo(Sex sexo) {
		this.sexo=sexo;
	}

	public Employee getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Employee funcionario) {
		this.funcionario=funcionario;
	}

}