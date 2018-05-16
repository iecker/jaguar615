package com.empresa.rhrich.entity;


import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Access;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.AccessType;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import javax.persistence.Entity;
import javax.persistence.Transient;
/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="HISTORICO_FUNCIONAL")
@SequenceGenerator(name="SE_HISTORICO_FUNCIONAL", sequenceName="SE_HISTORICO_FUNCIONAL")
@Access(AccessType.FIELD)


@NamedQueries({
	@NamedQuery(name="HistoricoFuncionalEntity.querySelLookup", query="select id as id, descricao as descricao from HistoricoFuncionalEntity where id = ? order by id asc")
})
public class HistoricoFuncionalEntity extends HistoricoFuncional {

	private static final long serialVersionUID = 1L;
 	
    /*
     * Construtor padrão
     */
    public HistoricoFuncionalEntity() {
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
