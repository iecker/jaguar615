/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.cliente;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;

import com.powerlogic.jcompany.controller.jsf.PlcEntityList;

import br.com.plc.jcompany_fcls.entity.AppBaseEntity;




@MappedSuperclass

public abstract class Grupo extends AppBaseEntity {

	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_GRUPO")
	@Column (name = "ID_GRUPO")
	private Long id;

	@NotNull(groups=PlcEntityList.class)
	@Size(max=32)
	@RequiredIf(valueOf="id",is=RequiredIfType.not_empty)
	@Column (name = "NOME")
	private String nome;
	
	@NotNull(groups=PlcEntityList.class)
	@RequiredIf(valueOf="nome",is=RequiredIfType.not_empty)
	@Column (name = "INATIVO")
	private Boolean inativo;

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

	public Boolean getInativo() {
		return inativo;
	}
	
	public void setInativo(Boolean inativo) {
		this.inativo = inativo;
	}

}
