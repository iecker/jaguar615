package com.powerlogic.rhavancado.entity.funcionario;


import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Access;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.AccessType;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import javax.persistence.Entity;
/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="FUNCIONARIO")
@SequenceGenerator(name="SE_FUNCIONARIO", sequenceName="SE_FUNCIONARIO")
@Access(AccessType.FIELD)


@NamedQueries({
	@NamedQuery(name="FuncionarioEntity.queryMan", query="from FuncionarioEntity"),
	@NamedQuery(name="FuncionarioEntity.querySel", query="select id as id, nome as nome, estadoCivil as estadoCivil, dataNascimento as dataNascimento from FuncionarioEntity order by nome asc"),
	@NamedQuery(name="FuncionarioEntity.querySelLookup", query="select id as id, nome as nome from FuncionarioEntity where id = ? order by id asc")
})
public class FuncionarioEntity extends Funcionario {

	private static final long serialVersionUID = 1L;
 	
    /*
     * Construtor padrÃ£o
     */
    public FuncionarioEntity() {
    }
	@Override
	public String toString() {
		return getNome();
	}

}
