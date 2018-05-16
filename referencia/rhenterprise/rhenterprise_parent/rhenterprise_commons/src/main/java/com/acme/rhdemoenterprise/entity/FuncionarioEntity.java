package com.acme.rhdemoenterprise.entity;


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


@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="FuncionarioEntity.queryMan", query="from FuncionarioEntity"),
	@NamedQuery(name="FuncionarioEntity.querySel", query="select obj.id as id, obj.nome as nome, obj.dataNascimento as dataNascimento, obj.cpf as cpf, obj.estadoCivil as estadoCivil, obj.enderecoResidencial.logradouro as enderecoResidencial_logradouro, obj.categoriaSalarial as categoriaSalarial,  obj0.id as unidadeOrganizacional_id, obj0.nome as unidadeOrganizacional_nome , obj1.id as enderecoResidencial_uf_id, obj1.nome as enderecoResidencial_uf_nome, obj2.salario as ultimoSalario, obj2.dataInicio as dataUltimoEmprego from FuncionarioEntity obj left outer join obj.unidadeOrganizacional as obj0 left outer join obj.enderecoResidencial.uf as obj1 left outer join obj.historicoProfissional as obj2 order by obj.nome asc"),
	@NamedQuery(name="FuncionarioEntity.querySelLookup", query="select id as id, nome as nome from FuncionarioEntity where id = ? order by id asc"),
	@NamedQuery(name="FuncionarioEntity.querySelMarcados", query="select obj.id as id, obj.nome as nome from FuncionarioEntity obj where obj in (select obj2.funcionario from MatrizHabilidadeEntity obj2) order by obj.id asc"),
	@NamedQuery(name="FuncionarioEntity.querySelDesmarcados", query="select obj.id as id, obj.nome as nome from FuncionarioEntity obj where obj not in (select obj2.funcionario from MatrizHabilidadeEntity obj2) order by obj.id asc")	
})
public class FuncionarioEntity extends Funcionario {
 	
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
