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
@Table(name="UNIDADE_ORGANIZACIONAL")
@SequenceGenerator(name="SE_UNIDADE_ORGANIZACIONAL", sequenceName="SE_UNIDADE_ORGANIZACIONAL")
@Access(AccessType.FIELD)


@NamedQueries({
	@NamedQuery(name="UnidadeOrganizacionalEntity.queryMan", query="from UnidadeOrganizacionalEntity"),
	@NamedQuery(name="UnidadeOrganizacionalEntity.querySel", query="select id as id, nome as nome from UnidadeOrganizacionalEntity order by nome asc"),
	@NamedQuery(name="UnidadeOrganizacionalEntity.querySelLookup", query="select id as id, nome as nome from UnidadeOrganizacionalEntity where id = ? order by id asc"),
	@NamedQuery(name="UnidadeOrganizacionalEntity.naoDeveExistirNomeDuplicado", query="select count(*) from UnidadeOrganizacionalEntity where nome = :nome")
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
