package com.empresa.rhavancado.entity;


import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
	@NamedQuery(name="FuncionarioEntity.querySelLookup", query="select id as id, nome as nome from FuncionarioEntity where id = ? order by id asc")
})
public class FuncionarioEntity extends Funcionario {

	private static final long serialVersionUID = 1L;
 	
    /*
     * Construtor padrao
     */
    public FuncionarioEntity() {
    }
	@Override
	public String toString() {
		return getNome();
	}

}
