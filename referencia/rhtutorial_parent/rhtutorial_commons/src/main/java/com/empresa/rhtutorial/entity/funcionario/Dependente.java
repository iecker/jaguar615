/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/ 
package com.empresa.rhtutorial.entity.funcionario;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import org.hibernate.annotations.ForeignKey;

import com.empresa.rhtutorial.entity.AppBaseEntity;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;

/**
 * Dependente
 */
@MappedSuperclass
public abstract class Dependente extends AppBaseEntity {
	
	@Id 
 	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_DEPENDENTE")
	@Column(nullable=false,length=5)
	private Long id;

	@NotNull(groups=PlcValGroupEntityList.class) 
	@Size(max = 40)
	@Column
//	@PlcReference(testDuplicity=true)
	private String nome;
	
    @RequiredIf(valueOf="nome",is=RequiredIfType.not_empty) 
	@Enumerated(EnumType.STRING)
	@NotNull(groups=PlcValGroupEntityList.class) 
	@Column(length=1)
	private Sexo sexo;
	
	@ManyToOne (targetEntity = FuncionarioEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_DEPENDENTE_FUNCIONARIO")
	@NotNull
	@JoinColumn
	private Funcionario funcionario;

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

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo=sexo;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario=funcionario;
	}

}