package com.empresa.rhtutorial2.entity;


import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.SequenceGenerator;
import javax.persistence.AccessType;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import javax.persistence.Access;
import javax.persistence.Transient;
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
     * Construtor padrao
     */
    public MunicipioEntity() {
    }
	@Override
	public String toString() {
		return getNome();
	}

	@Transient
	private String indExcPlc = "N";	

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

}
