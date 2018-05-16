/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.test.masterdetail;


import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.ForeignKey;



@MappedSuperclass

public abstract class OrganizationalUnit extends AppBaseEntity {

	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_UNIDADE_ORGANIZACIONAL")
	@Column (name = "ID_UNIDADE_ORGANIZACIONAL", nullable=false, length=5)
	private Long id;
	
	@Column (name = "NOME", nullable=false, length=40)
	private String nome;
	
	@Embedded
	private Address endereco;
	
	@SuppressWarnings("unchecked")
	@ManyToOne (targetEntity = OrganizationalUnitEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_UNIDADEORGANIZACIONAL_UNIDADEPAI")
	@JoinColumn(name="ID_PAI")
	private OrganizationalUnit unidadePai;
	
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

	public Address getEndereco() {
		return endereco;
	}

	public void setEndereco(Address endereco) {
		this.endereco=endereco;
	}

	public OrganizationalUnit getUnidadePai() {
		return unidadePai;
	}

	public void setUnidadePai(OrganizationalUnit unidadePai) {
		this.unidadePai=unidadePai;
	}

}
