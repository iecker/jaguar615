package com.acme.planos_assist.entity.planos;


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
@Table(name="PLANOS")
@SequenceGenerator(name="SE_PLANOS", sequenceName="SE_PLANOS")
@Access(AccessType.FIELD)


@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="PlanosEntity.queryMan", query="from PlanosEntity"),
	@NamedQuery(name="PlanosEntity.querySel2", query="select id as id, nome as nome, tipoPlanos as tipoPlanos from PlanosEntity order by nome asc"),
	@NamedQuery(name="PlanosEntity.querySel", query="select id as id, nome as nome, tipoPlanos as tipoPlanos from PlanosEntity order by nome asc"),
	@NamedQuery(name="PlanosEntity.querySelLookup", query="select id as id, nome as nome from PlanosEntity where id = ? order by id asc")
})
public class PlanosEntity extends Planos {
 	
    /*
     * Construtor padrÃ£o
     */
    public PlanosEntity() {
    }
	@Override
	public String toString() {
		return getNome();
	}

}
