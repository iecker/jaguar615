package com.empresa.rhtutorial.entity;



import javax.persistence.ManyToOne;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import javax.persistence.GenerationType;
import javax.persistence.MappedSuperclass;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import com.powerlogic.jcompany.config.domain.PlcReference;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;

import org.hibernate.annotations.ForeignKey;
import javax.persistence.GeneratedValue;
@MappedSuperclass
public class Municipio extends AppBaseEntity {

	@Id 
 	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_MUNICIPIO")
	@Column(nullable=false,length=5)
	private Long id;
	
	@ManyToOne (targetEntity = UfEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_MUNICIPIO_UF")
	@NotNull
	@JoinColumn
	private Uf uf;

	@Size(max = 40)
	@NotNull(groups=PlcValGroupEntityList.class) 
	@Column(length=40)
	private String nome;
	
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

	public Uf getUf() {
		return uf;
	}

	public void setUf(Uf uf) {
		this.uf=uf;
	}

}
