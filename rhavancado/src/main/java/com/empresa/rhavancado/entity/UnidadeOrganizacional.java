package com.empresa.rhavancado.entity;


import java.io.Serializable;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.validation.constraints.Size;
import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.MappedSuperclass;
import org.hibernate.annotations.ForeignKey;
import javax.persistence.FetchType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.persistence.Version;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;

@MappedSuperclass
@NamedQueries({
	@NamedQuery(name="UnidadeOrganizacional.queryMan", query="from UnidadeOrganizacional"),
	@NamedQuery(name="UnidadeOrganizacional.querySel", query="select id as id from UnidadeOrganizacional order by id asc")
})
public class UnidadeOrganizacional  implements Serializable {

	
	@Id 
 	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_UNIDADE_ORGANIZACIONAL")
	private Long id;
	
	@Version
	@NotNull
	@Digits(integer=5, fraction=0)
	@Column(name="VERSAO")	
	private Integer versao = new Integer(0);

	
	@NotNull
	@Size(max = 5)
	private String nome;
	
	@ManyToOne (targetEntity = UnidadeOrganizacionalEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_UNIDADEORGANIZACIONAL_UNIDADEPAI")
	@NotNull
	private UnidadeOrganizacional unidadePai;
	
	@ManyToOne (targetEntity = Endereco.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_UNIDADEORGANIZACIONAL_ENDERECO")
	@NotNull
	private Endereco endereco;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public UnidadeOrganizacional getUnidadePai() {
		return unidadePai;
	}
	public void setUnidadePai(UnidadeOrganizacional unidadePai) {
		this.unidadePai = unidadePai;
	}
	public Endereco getEndereco() {
		return endereco;
	}
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id=id;
	}

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao=versao;
	}

}
