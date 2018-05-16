package com.acme.rhdemoenterprise.entity;


import javax.persistence.NamedQueries;

import javax.persistence.NamedQuery;
import com.powerlogic.jcompany.commons.annotation.PlcEntityTreeView;
import javax.persistence.Access;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.AccessType;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import javax.persistence.Entity;
import javax.persistence.Transient;
/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="UF")
@SequenceGenerator(name="SE_UF", sequenceName="SE_UF")
@Access(AccessType.FIELD)

@PlcEntityTreeView(classeLookup=true)

//@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="UfEntity.queryMan", query="from UfEntity"),
	@NamedQuery(name="UfEntity.querySelLookup", query="select id as id, nome as nome from UfEntity where id = ? order by id asc")
})
public class UfEntity extends Uf {
 	
    /*
     * Construtor padrão
     */
    public UfEntity() {
    }
	@Override
	public String toString() {
		return getNome();
	}

	@Transient
	private transient String indExcPlc = "N";	

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

}
