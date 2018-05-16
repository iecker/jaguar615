package com.empresa.rhtutorial2.entity;



import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;

import com.powerlogic.jcompany.config.domain.PlcReference;
import com.powerlogic.jcompany.domain.validation.PlcValExactSize;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;
import com.powerlogic.jcompany.domain.validation.PlcValSimpleFormat;
import com.powerlogic.jcompany.domain.validation.PlcValSimpleFormat.SimpleFormat;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import java.util.List;
import javax.validation.Valid;
import javax.persistence.CascadeType;
import org.hibernate.annotations.ForeignKey;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import com.powerlogic.jcompany.domain.validation.PlcValDuplicity;
@MappedSuperclass
public abstract class Uf extends AppBaseEntity {

	
	@OneToMany (targetEntity = com.empresa.rhtutorial2.entity.MunicipioEntity.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="uf")
	@ForeignKey(name="FK_MUNICIPIO_UF")
	@PlcValDuplicity(property="nome")
	@PlcValMultiplicity(referenceProperty="nome",  message="{jcompany.aplicacao.mestredetalhe.multiplicidade.MunicipioEntity}")
	@Valid
	private List<Municipio> municipio;


	
	@Id 
 	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_UF")
	private Long id;
	
	@NotNull(groups=PlcValGroupEntityList.class)
	@RequiredIf(valueOf="id",is=RequiredIfType.not_empty)
	@Size(max = 40)
	@PlcReference(testDuplicity=true)
	private String nome;
	
	@PlcValSimpleFormat(format=SimpleFormat.UPPER_CASE)
	@PlcValExactSize(size=2)
	@NotNull(groups=PlcValGroupEntityList.class)
	@RequiredIf(valueOf="nome",is=RequiredIfType.not_empty)
	@Size(max = 2)
	private String sigla;
	
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
