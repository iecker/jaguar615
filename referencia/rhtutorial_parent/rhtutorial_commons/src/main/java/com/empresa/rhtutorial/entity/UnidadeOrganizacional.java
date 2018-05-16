package com.empresa.rhtutorial.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;

@MappedSuperclass
public abstract class UnidadeOrganizacional extends AppBaseEntity {

	@Id 
 	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_UNIDADE_ORGANIZACIONAL")
	@Column(nullable=false,length=5)
	private Long id;

	@NotNull
	@Size(max = 40)
	@Column
	private String nome;
	
	@Embedded
	@Valid
	private Endereco endereco;
	
	@ManyToOne (targetEntity = UnidadeOrganizacionalEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_UNIDADEORGANIZACIONAL_UNIDADEPAI")
	@JoinColumn
	private UnidadeOrganizacional unidadePai;
	
	/**
	 * @return true se o pai for diferente do filho
	 */
	@AssertTrue(message="{validator.unidaderecursiva}")
	public boolean isUnidadeRecursiva() {
	   
		if(getUnidadePai() == null) {
			return true;
		} else {
			if(getUnidadePai().getId() == null || (getUnidadePai().getId() != null && !getUnidadePai().getId().equals(getId()))) {
				return true;
			} else {
				return false;
			}
		}
	}
	   
	
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

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco=endereco;
	}

	public UnidadeOrganizacional getUnidadePai() {
		return unidadePai;
	}

	public void setUnidadePai(UnidadeOrganizacional unidadePai) {
		this.unidadePai=unidadePai;
	}

}
