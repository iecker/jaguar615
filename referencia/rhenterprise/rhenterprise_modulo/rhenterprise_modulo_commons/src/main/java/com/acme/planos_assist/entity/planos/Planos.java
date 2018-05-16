package com.acme.planos_assist.entity.planos;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.acme.planos_assist.dominio.TipoPlanos;
import com.acme.planos_assist.entity.PlaBaseEntity;


@MappedSuperclass
public abstract class Planos extends PlaBaseEntity {

	
	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_PLANOS")
	@Column (nullable=false, length=5)
	private Long id;

	
	
	@Column (nullable=false, length=50)
	private String nome;
	
	@Enumerated(EnumType.STRING)
	@Column (nullable=false, length=1)
	private TipoPlanos tipoPlanos;

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

	public TipoPlanos getTipoPlanos() {
		return tipoPlanos;
	}

	public void setTipoPlanos(TipoPlanos tipoPlanos) {
		this.tipoPlanos=tipoPlanos;
	}

}
