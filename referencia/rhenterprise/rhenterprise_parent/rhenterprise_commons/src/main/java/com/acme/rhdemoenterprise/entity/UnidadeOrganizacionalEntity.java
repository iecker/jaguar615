package com.acme.rhdemoenterprise.entity;


import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.powerlogic.jcompany.commons.annotation.PlcEntityTreeView;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="UNIDADE_ORGANIZACIONAL")
@SequenceGenerator(name="SE_UNIDADE_ORGANIZACIONAL", sequenceName="SE_UNIDADE_ORGANIZACIONAL")
@Access(AccessType.FIELD)

@PlcEntityTreeView(recursividadeNomeProp="unidadePai")
@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="UnidadeOrganizacionalEntity.queryMan", query="from UnidadeOrganizacionalEntity"),
	@NamedQuery(name="UnidadeOrganizacionalEntity.querySel", query="select obj.id as id, obj.nome as nome, obj0.id as unidadePai_id, obj0.nome as unidadePai_nome , obj.endereco.logradouro as endereco_logradouro, obj.endereco.numero as endereco_numero from UnidadeOrganizacionalEntity obj left outer join obj.unidadePai as obj0 order by obj.nome asc"),
	@NamedQuery(name="UnidadeOrganizacionalEntity.queryTreeView", query="select obj.id as id, obj.nome as nome from UnidadeOrganizacionalEntity obj where obj.unidadePai is null order by obj.nome asc"),
	@NamedQuery(name="UnidadeOrganizacionalEntity.querySelLookup", query="select id as id, nome as nome from UnidadeOrganizacionalEntity where id = ? order by id asc"),
	@NamedQuery(name="UnidadeOrganizacionalEntity.naoDeveExistirNomeDuplicado", query="select count(*) from UnidadeOrganizacionalEntity where nome = :nome")
	
	
	/*
	 
	@NamedQuery(name="UnidadeOrganizacionalEntity.querySel", query="select obj.id as id, obj.nome as nome, obj.endereco.logradouro as endereco_logradouro, obj.endereco.numero as endereco_numero from UnidadeOrganizacionalEntity obj order by obj.nome asc"),
	@NamedQuery(name="UnidadeOrganizacionalEntity.queryTreeView", query="select obj.id as id, obj.nome as nome from UnidadeOrganizacionalEntity obj where obj.unidadePai is null order by obj.nome asc"), 
	@NamedQuery(name="UnidadeOrganizacionalEntity.querySelLookup", query="select id as id, nome as nome from UnidadeOrganizacionalEntity where id = ? order by id asc"),
	 
	 */
})
public class UnidadeOrganizacionalEntity extends UnidadeOrganizacional {
 	
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
