package com.empresa.rhtutorial.entity;


import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="MUNICIPIO")
@SequenceGenerator(name="SE_MUNICIPIO", sequenceName="SE_MUNICIPIO")
@Access(AccessType.FIELD)

@NamedQueries({
	@NamedQuery(name="MunicipioEntity.querySelLookup", query="select id as id, nome as nome from MunicipioEntity where id = ? order by id asc")
})
public class MunicipioEntity extends Municipio {

	private static final long serialVersionUID = 1L;
 	
    /*
     * Construtor padrÃ£o
     */
    public MunicipioEntity() {
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
