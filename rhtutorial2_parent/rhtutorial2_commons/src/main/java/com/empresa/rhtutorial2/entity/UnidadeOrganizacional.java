package com.empresa.rhtutorial2.entity;



import javax.validation.Valid;
import javax.persistence.GenerationType;
import javax.validation.constraints.Size;
import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.MappedSuperclass;
import org.hibernate.annotations.ForeignKey;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.validation.constraints.NotNull;
import javax.persistence.Id;
@MappedSuperclass
public abstract class UnidadeOrganizacional extends AppBaseEntity {

	
	@Id 
 	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_UNIDADE_ORGANIZACIONAL")
	private Long id;

	
	
	@NotNull
	@Size(max = 5)
	private String nome;
	
	@Embedded
	@NotNull
	@Valid
	private Endereco endereco;
	
	@ManyToOne (targetEntity = UnidadeOrganizacionalEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_UNIDADEORGANIZACIONAL_UNIDADEPAI")
	@NotNull
	private UnidadeOrganizacional unidadePai;
	
	
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
