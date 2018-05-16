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
@Table(name="PREFERENCIA_USUARIO")
@SequenceGenerator(name="SE_PREFERENCIA_USUARIO", sequenceName="SE_PREFERENCIA_USUARIO")
@Access(AccessType.FIELD)


@NamedQueries({
	@NamedQuery(name="PreferenciaUsuarioEntity.querySelLookup", query="select id as id, nomeCompleto as nomeCompleto from PreferenciaUsuarioEntity where id = ? order by id asc")
})
public class PreferenciaUsuarioEntity extends PreferenciaUsuario {

	private static final long serialVersionUID = 1L;
 	
    /*
     * Construtor padrao
     */
    public PreferenciaUsuarioEntity() {
    }
	@Override
	public String toString() {
		return getNomeCompleto();
	}

}
