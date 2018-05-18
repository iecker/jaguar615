package com.empresa.cargosalario.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.xpath.operations.String;

import com.powerlogic.jcompany.commons.annotation.PlcDbFactory;

@PlcDbFactory(nome = "default_module")
@Entity
@NamedQueries({ @NamedQuery(name = "CargoView.querySelByCodigo", query = "from CargoView where codigo = :codigo ") })
@Table(name = "cargo")
public class CargoView {

	@Id
	private Long id;

	private String codigo;

	private BigDecimal salario;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public java.math.BigDecimal getSalario() {
		return salario;
	}

	public void setSalario(java.math.BigDecimal salario) {
		this.salario = salario;
	}
}
