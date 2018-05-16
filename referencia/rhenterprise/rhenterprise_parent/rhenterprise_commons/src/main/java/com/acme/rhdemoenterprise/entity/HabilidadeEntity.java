package com.acme.rhdemoenterprise.entity;


import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.powerlogic.jcompany.commons.annotation.PlcEntityTreeView;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="HABILIDADE")
@SequenceGenerator(name="SE_HABILIDADE", sequenceName="SE_HABILIDADE")
@Access(AccessType.FIELD)

@PlcEntityTreeView(classeLookup=true)

@NamedQueries({
	@NamedQuery(name="HabilidadeEntity.querySel", query="select id as id, descricao as descricao from HabilidadeEntity order by id asc"),
	@NamedQuery(name="HabilidadeEntity.querySelLookup", query="select id as id, descricao as descricao from HabilidadeEntity where id = ? order by id asc"),
	@NamedQuery(name="HabilidadeEntity.querySelMarcados", query="select obj.id as id, obj.descricao as descricao from HabilidadeEntity obj where obj in (select obj2.habilidade from MatrizHabilidadeEntity obj2) order by obj.id asc"),
	@NamedQuery(name="HabilidadeEntity.querySelDesmarcados", query="select obj.id as id, obj.descricao as descricao from HabilidadeEntity obj where obj not in (select obj2.habilidade from MatrizHabilidadeEntity obj2) order by obj.id asc")	
})
public class HabilidadeEntity extends Habilidade {
 	
    /*
     * Construtor padrão
     */
    public HabilidadeEntity() {
    }
	@Override
	public String toString() {
		return getDescricao();
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
