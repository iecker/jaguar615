package com.empresa.rhtutorial2.entity.funcionario.pendencia;


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
@Table(name="PENDENCIA")
@SequenceGenerator(name="SE_PENDENCIA", sequenceName="SE_PENDENCIA")
@Access(AccessType.FIELD)


@NamedQueries({
	@NamedQuery(name="PendenciaEntity.querySelLookup", query="select id as id, descricao as descricao from PendenciaEntity where id = ? order by id asc"),
    @NamedQuery(name="PendenciaEntity.pendenciaPorFuncionario", query="from PendenciaEntity where funcionario = :funcionario")
})
public class PendenciaEntity extends Pendencia {

	private static final long serialVersionUID = 1L;
 	
    /*
     * Construtor padrao
     */
    public PendenciaEntity() {
    }
	@Override
	public String toString() {
		return getDescricao();
	}

}
