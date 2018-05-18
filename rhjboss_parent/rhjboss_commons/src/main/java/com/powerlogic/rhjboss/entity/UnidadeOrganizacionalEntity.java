package com.powerlogic.rhjboss.entity;


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
@Table(name="UNIDADE_ORGANIZACIONAL")
@SequenceGenerator(name="SE_UNIDADE_ORGANIZACIONAL", sequenceName="SE_UNIDADE_ORGANIZACIONAL")
@Access(AccessType.FIELD)


@NamedQueries({
	@NamedQuery(name="UnidadeOrganizacionalEntity.queryMan", query="from UnidadeOrganizacionalEntity"),
	@NamedQuery(name="UnidadeOrganizacionalEntity.querySel", query="select obj.id as id, obj.nome as nome, obj1.id as unidadePai_id , obj1.nome as unidadePai_nome from UnidadeOrganizacionalEntity obj left outer join obj.unidadePai as obj1 order by obj.nome asc"),
	@NamedQuery(name="UnidadeOrganizacionalEntity.querySelLookup", query="select id as id, nome as nome from UnidadeOrganizacionalEntity where id = ? order by id asc")
})
public class UnidadeOrganizacionalEntity extends UnidadeOrganizacional {

	private static final long serialVersionUID = 1L;
 	
    /*
     * Construtor padrÃ£o
     */
    public UnidadeOrganizacionalEntity() {
    }
	@Override
	public String toString() {
		return getNome();
	}

}
