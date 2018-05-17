package com.empresa.rhtutorial2.entity.funcionario;


import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

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
	@NamedQuery(name="FuncionarioEntity.queryMan", query="from FuncionarioEntity"),
	@NamedQuery(name="FuncionarioEntity.querySel", query="select id as id, nome as nome, cpf as cpf, estadoCivil as estadoCivil from FuncionarioEntity order by nome asc"),
	@NamedQuery(name="FuncionarioEntity.querySelLookup", query="select id as id, nome as nome from FuncionarioEntity where id = ? order by id asc"),
	@NamedQuery(name="FuncionarioEntity.naoDeveExistirCpfDuplicado", query="select count(*) from FuncionarioEntity where cpf = :cpf")
})
@Audited
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
