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
@Table(name="FUNCIONARIO")
@SequenceGenerator(name="SE_FUNCIONARIO", sequenceName="SE_FUNCIONARIO")
@Access(AccessType.FIELD)

@NamedQueries({
	@NamedQuery(name="FuncionarioEntity.querySel3", query="select id as id, nome as nome, dataNascimento as dataNascimento from FuncionarioEntity order by nome asc"),
	@NamedQuery(name="FuncionarioEntity.querySel2", query="select id as id, nome as nome, dataNascimento as dataNascimento, cpf as cpf from FuncionarioEntity order by nome asc"),
	@NamedQuery(name="FuncionarioEntity.queryMan", query="from FuncionarioEntity"),
	@NamedQuery(name="FuncionarioEntity.querySel", 
			query="select id as id, nome as nome, temCursoSuperior as temCursoSuperior from FuncionarioEntity order by nome asc"),
	@NamedQuery(name="FuncionarioEntity.querySelLookup", 
			query="select id as id, nome as nome from FuncionarioEntity where id = ? order by id asc")
})
public class FuncionarioEntity extends Funcionario {

	private static final long serialVersionUID = 1L;
 	
    /*
     * Construtor padrão
     */
    public FuncionarioEntity() {
    }
	@Override
	public String toString() {
		return getNome();
	}

	
	@Transient
	private String departamentoLookup;
	@Transient
	private String departamentoId;
	public String getDepartamentoLookup() {
		if (departamentoLookup!=null) return departamentoLookup;
		if (getDepartamento()!=null&& getDepartamento().getNome()!=null) return getDepartamento().getNome();
		return departamentoLookup;
	}

	public void setDepartamentoLookup(String departamentoLookup) {
		this.departamentoLookup = departamentoLookup;
	}

	public String getDepartamentoId() {
		if (departamentoId!=null) return departamentoId;
		if (getDepartamento()!=null && getDepartamento().getId()!=null)
			return getDepartamento().getId()+"";
		return departamentoId;
	}

	public void setDepartamentoId(String departamentoId) {
		if (!StringUtils.isBlank(departamentoId) &&  StringUtils.isNumeric(departamentoId)){
			DepartamentoEntity d = new DepartamentoEntity();
			d.setId(new Long(departamentoId));
			setDepartamento(d);
		}
		this.departamentoId = departamentoId;
	}
}
