package com.empresa.rhtutorial2.entity;


import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.SequenceGenerator;
import javax.persistence.AccessType;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import javax.persistence.Access;
/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="UF")
@SequenceGenerator(name="SE_UF", sequenceName="SE_UF")
@Access(AccessType.FIELD)


@NamedQueries({
	@NamedQuery(name="UfEntity.queryMan", query="from UfEntity"),
	@NamedQuery(name="UfEntity.querySelLookup", query="select id as id, nome as nome from UfEntity where id = ? order by id asc")
})
public class UfEntity extends Uf {

	private static final long serialVersionUID = 1L;
 	
    /*
     * Construtor padrao
     */
    public UfEntity() {
    }
	@Override
	public String toString() {
		return getNome();
	}

}
