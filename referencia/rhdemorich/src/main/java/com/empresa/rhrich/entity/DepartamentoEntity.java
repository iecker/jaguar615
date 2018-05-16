package com.empresa.rhrich.entity;


import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="DEPARTAMENTO")
@SequenceGenerator(name="SE_DEPARTAMENTO", sequenceName="SE_DEPARTAMENTO")
@Access(AccessType.FIELD)

@NamedQueries({
	@NamedQuery(name="DepartamentoEntity.queryMan", query="from DepartamentoEntity"),
	@NamedQuery(name="DepartamentoEntity.querySel", query="select id as id, nome as nome, endereco.rua as endereco_rua, endereco.numero as endereco_numero, endereco.cep as endereco_cep from DepartamentoEntity order by nome asc"),
	@NamedQuery(name="DepartamentoEntity.querySelLookup", query="select id as id, nome as nome from DepartamentoEntity where id = ? order by id asc")
})
public class DepartamentoEntity extends Departamento {

	private static final long serialVersionUID = 1L;
 	
    /*
     * Construtor padrão
     */
    public DepartamentoEntity() {
    }
	@Override
	public String toString() {
		return getNome();
	}
	
 
	@Transient
	private String departamentoPaiLookup;
	@Transient
	private String departamentoPaiId;
	public String getDepartamentoPaiLookup() {
		if (departamentoPaiLookup!=null) return departamentoPaiLookup;
		if (getDepartamentoPai()!=null&& getDepartamentoPai().getNome()!=null) return getDepartamentoPai().getNome();
		return departamentoPaiLookup;
	}

	public void setDepartamentoPaiLookup(String departamentoPaiLookup) {
		this.departamentoPaiLookup = departamentoPaiLookup;
	}

	public String getDepartamentoPaiId() {
		if (departamentoPaiId!=null) return departamentoPaiId;
		if (getDepartamentoPai()!=null && getDepartamentoPai().getId()!=null)
			return getDepartamentoPai().getId()+"";
		return departamentoPaiId;
	}

	public void setDepartamentoPaiId(String departamentoPaiId) {
		if (!StringUtils.isBlank(departamentoPaiId) &&  StringUtils.isNumeric(departamentoPaiId)){
			DepartamentoEntity d = new DepartamentoEntity();
			d.setId(new Long(departamentoPaiId));
			setDepartamentoPai(d);
		}
		this.departamentoPaiId = departamentoPaiId;
	}

	
	
}
