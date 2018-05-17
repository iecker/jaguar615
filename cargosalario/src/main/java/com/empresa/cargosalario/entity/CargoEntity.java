package com.empresa.cargosalario.entity;


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
@Table(name="CARGO")
@SequenceGenerator(name="SE_CARGO", sequenceName="SE_CARGO")
@Access(AccessType.FIELD)


@NamedQueries({
	@NamedQuery(name="CargoEntity.queryMan", query="from CargoEntity"),
	@NamedQuery(name="CargoEntity.querySel", query="select id as id, codigo as codigo, descricao as descricao, salario as salario from CargoEntity order by codigo asc"),
	@NamedQuery(name="CargoEntity.querySelLookup", query="select id as id, codigo as codigo from CargoEntity where id = ? order by id asc")
})
public class CargoEntity extends Cargo {

	private static final long serialVersionUID = 1L;
 	
    /*
     * Construtor padrÃ£o
     */
    public CargoEntity() {
    }
	@Override
	public String toString() {
		return getCodigo();
	}

}
