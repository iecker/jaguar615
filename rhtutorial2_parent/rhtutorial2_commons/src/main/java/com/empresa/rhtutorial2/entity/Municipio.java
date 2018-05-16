package com.empresa.rhtutorial2.entity;



import javax.persistence.GenerationType;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import javax.persistence.GeneratedValue;
import javax.persistence.MappedSuperclass;
import org.hibernate.annotations.ForeignKey;
import javax.persistence.FetchType;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import javax.validation.constraints.NotNull;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;
import javax.persistence.Id;
@MappedSuperclass
public class Municipio extends AppBaseEntity {

	
	@Id 
 	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_MUNICIPIO")
	private Long id;


	
	@ManyToOne (targetEntity = UfEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_MUNICIPIO_UF")
	@NotNull(groups=PlcValGroupEntityList.class)
	@RequiredIf(valueOf="nome",is=RequiredIfType.not_empty)
	private Uf uf;
	
	@NotNull(groups=PlcValGroupEntityList.class)
	@RequiredIf(valueOf="id",is=RequiredIfType.not_empty)
	@Size(max = 30)
	private String nome;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id=id;
	}

	public Uf getUf() {
		return uf;
	}

	public void setUf(Uf uf) {
		this.uf=uf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome=nome;
	}

}
