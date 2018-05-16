package com.empresa.rhtutorial.entity;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import org.hibernate.annotations.ForeignKey;

import com.powerlogic.jcompany.config.domain.PlcReference;
import com.powerlogic.jcompany.domain.validation.PlcValDuplicity;
import com.powerlogic.jcompany.domain.validation.PlcValExactSize;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;
import com.powerlogic.jcompany.domain.validation.PlcValSimpleFormat;
import com.powerlogic.jcompany.domain.validation.PlcValSimpleFormat.SimpleFormat;

@MappedSuperclass
public abstract class Uf extends AppBaseEntity {

	@Id 
 	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_UF")
	@Column(nullable=false,length=5)
	private Long id;

    @NotNull(groups=PlcValGroupEntityList.class) 
	@Size(max = 30)
	@Column
	@PlcReference(testDuplicity=true)
	private String nome;
	
	@PlcValSimpleFormat(format=SimpleFormat.UPPER_CASE)
	@PlcValExactSize(size=2)
    @RequiredIf(valueOf="nome",is=RequiredIfType.not_empty) 
	@NotNull(groups=PlcValGroupEntityList.class) 
	@Size(max = 2)
	@Column
	private String sigla;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id=id;
	}
	
	@OneToMany (targetEntity = com.empresa.rhtutorial.entity.MunicipioEntity.class, 
			fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="uf")
	@ForeignKey(name="FK_MUNICIPIO_UF")
	@PlcValDuplicity(property="nome")
	private List<Municipio> municipio;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome=nome;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla=sigla;
	}

	public List<Municipio> getMunicipio() {
		return municipio;
	}

	public void setMunicipio(List<Municipio> municipio) {
		this.municipio=municipio;
	}

}
