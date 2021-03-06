package com.empresa.rhavancado.entity;


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
@Table(name="UNIDADE_ORGANIZACIONAL")
@SequenceGenerator(name="SE_UNIDADE_ORGANIZACIONAL", sequenceName="SE_UNIDADE_ORGANIZACIONAL")
@Access(AccessType.FIELD)


@NamedQueries({
	@NamedQuery(name="UnidadeOrganizacionalEntity.querySelLookup", query="select id as id, nome as nome from UnidadeOrganizacionalEntity where id = ? order by id asc")
})
public class UnidadeOrganizacionalEntity extends UnidadeOrganizacional {

	private static final long serialVersionUID = 1L;
 	
    /*
     * Construtor padrao
     */
    public UnidadeOrganizacionalEntity() {
    }
	@Override
	public String toString() {
		return getNome();
	}

}
