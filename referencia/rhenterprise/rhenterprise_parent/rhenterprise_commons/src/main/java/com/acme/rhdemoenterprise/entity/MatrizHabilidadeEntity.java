package com.acme.rhdemoenterprise.entity;

/**
 * Classe Concreta gerada a partir do assistente
 */

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;

@SPlcEntity
@Entity
@Table(name="MATRIZ_HABILIDADE")
@SequenceGenerator(name="SE_MATRIZ_HABILIDADE", sequenceName="SE_MATRIZ_HABILIDADE")
@Access(AccessType.FIELD)

@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="MatrizHabilidadeEntity.querySel2", query="select obj.id as id, obj0.id as funcionario_id, obj0.nome as funcionario_nome, obj1.id as habilidade_id, obj1.descricao as habilidade_descricao  from MatrizHabilidadeEntity obj left outer join obj.funcionario as obj0 left outer join obj.habilidade as obj1 order by obj.id asc"),
	@NamedQuery(name="MatrizHabilidadeEntity.querySel", query="select obj.id as id, obj0.id as funcionario_id , obj1.id as habilidade_id  from MatrizHabilidadeEntity obj left outer join obj.funcionario as obj0 left outer join obj.habilidade as obj1 order by obj.id asc"),
	@NamedQuery(name="MatrizHabilidadeEntity.querySelLookup", query="select id as id, funcionario as funcionario from MatrizHabilidadeEntity where id = ? order by id asc")
})
public class MatrizHabilidadeEntity extends MatrizHabilidade {
 	
    /*
     * Construtor padrÃ£o
     */
    public MatrizHabilidadeEntity() {
    }

}
