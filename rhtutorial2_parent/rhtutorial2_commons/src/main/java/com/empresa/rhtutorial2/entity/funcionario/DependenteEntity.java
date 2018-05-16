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
@Table(name="DEPENDENTE")
@SequenceGenerator(name="SE_DEPENDENTE", sequenceName="SE_DEPENDENTE")
@Access(AccessType.FIELD)


@NamedQueries({
	@NamedQuery(name="DependenteEntity.querySelLookup", query="select id as id, nome as nome from DependenteEntity where id = ? order by id asc")
})
public class DependenteEntity extends Dependente {

	private static final long serialVersionUID = 1L;
 	
    /*
     * Construtor padrao
     */
    public DependenteEntity() {
    }
	@Override
	public String toString() {
		return getNome();
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
