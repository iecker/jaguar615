/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/ 
package com.empresa.rhtutorial2.entity.funcionario;

import com.empresa.rhtutorial2.entity.AppBaseEntity;
import java.util.Date;
import javax.validation.constraints.Size;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import org.hibernate.annotations.ForeignKey;
import javax.persistence.TemporalType;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import javax.validation.constraints.NotNull;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;
import javax.persistence.EnumType;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import javax.persistence.GeneratedValue;
import javax.persistence.Temporal;
import javax.persistence.FetchType;
import javax.validation.constraints.Digits;
import javax.persistence.Enumerated;
import javax.persistence.Version;
import javax.persistence.Id;

/**
 * Dependente
 */
@MappedSuperclass

public abstract class Dependente extends AppBaseEntity {

	
	@Id 
 	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_DEPENDENTE")
	private Long id;
	
	@NotNull(groups=PlcValGroupEntityList.class)
	@RequiredIf(valueOf="id",is=RequiredIfType.not_empty)
	@Size(max = 60)
	private String nome;
	
	@Enumerated(EnumType.STRING)
	@NotNull(groups=PlcValGroupEntityList.class)
	@RequiredIf(valueOf="nome",is=RequiredIfType.not_empty)
	@Column(length=1)
	private Sexo sexo;
	
	@ManyToOne (targetEntity = FuncionarioEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_DEPENDENTE_FUNCIONARIO")
	@NotNull(groups=PlcValGroupEntityList.class)
	@RequiredIf(valueOf="nome",is=RequiredIfType.not_empty)
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