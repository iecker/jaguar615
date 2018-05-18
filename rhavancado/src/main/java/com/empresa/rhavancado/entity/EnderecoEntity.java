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
@Table(name="ENDERECO")
@SequenceGenerator(name="SE_ENDERECO", sequenceName="SE_ENDERECO")
@Access(AccessType.FIELD)


@NamedQueries({
	@NamedQuery(name="EnderecoEntity.querySelLookup", query="select id as id, logradouro as logradouro from EnderecoEntity where id = ? order by id asc")
})
public class EnderecoEntity extends Endereco {

	private static final long serialVersionUID = 1L;
 	
    /*
     * Construtor padrao
     */
    public EnderecoEntity() {
    }
	@Override
	public String toString() {
		return getLogradouro();
	}

}
