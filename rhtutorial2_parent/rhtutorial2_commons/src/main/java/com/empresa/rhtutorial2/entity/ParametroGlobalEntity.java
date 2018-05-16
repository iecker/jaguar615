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
@Table(name="PARAMETRO_GLOBAL")
@SequenceGenerator(name="SE_PARAMETRO_GLOBAL", sequenceName="SE_PARAMETRO_GLOBAL")
@Access(AccessType.FIELD)


@NamedQueries({
	@NamedQuery(name="ParametroGlobalEntity.querySelLookup", query="select id as id, mensagemHome as mensagemHome from ParametroGlobalEntity where id = ? order by id asc"),
    @NamedQuery(name="ParametroGlobalEntity.edita", query="from ParametroGlobalEntity")
})
public class ParametroGlobalEntity extends ParametroGlobal {

	private static final long serialVersionUID = 1L;
 	
    /*
     * Construtor padrao
     */
    public ParametroGlobalEntity() {
    }
	@Override
	public String toString() {
		return getMensagemHome();
	}

}
