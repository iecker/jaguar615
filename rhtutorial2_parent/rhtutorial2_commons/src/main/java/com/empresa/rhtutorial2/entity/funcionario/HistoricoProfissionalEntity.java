package com.empresa.rhtutorial2.entity.funcionario;


import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.SequenceGenerator;
import javax.persistence.AccessType;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import javax.persistence.Access;
import javax.persistence.Transient;
/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="HISTORICO_PROFISSIONAL")
@SequenceGenerator(name="SE_HISTORICO_PROFISSIONAL", sequenceName="SE_HISTORICO_PROFISSIONAL")
@Access(AccessType.FIELD)


@NamedQueries({
	@NamedQuery(name="HistoricoProfissionalEntity.querySelLookup", query="select id as id, descricao as descricao from HistoricoProfissionalEntity where id = ? order by id asc")
})
public class HistoricoProfissionalEntity extends HistoricoProfissional {

	private static final long serialVersionUID = 1L;
 	
    /*
     * Construtor padrao
     */
    public HistoricoProfissionalEntity() {
    }
	@Override
	public String toString() {
		return getDescricao();
	}

	@Transient
	private String indExcPlc = "N";	

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

}
