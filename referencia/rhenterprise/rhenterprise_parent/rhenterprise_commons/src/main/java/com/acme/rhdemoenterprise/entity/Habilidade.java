package com.acme.rhdemoenterprise.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GenerationType;
import javax.persistence.MappedSuperclass;
import com.powerlogic.jcompany.config.domain.PlcReference;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;

import javax.persistence.GeneratedValue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@MappedSuperclass
public class Habilidade extends AppBaseEntity {
	
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_HABILIDADE")	
	private Long id;

	@NotNull(groups=PlcValGroupEntityList.class)
	@Column 
	@Size(max = 40)
	@PlcReference(testDuplicity=true)
	private String descricao;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id=id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao=descricao;
	}

}
