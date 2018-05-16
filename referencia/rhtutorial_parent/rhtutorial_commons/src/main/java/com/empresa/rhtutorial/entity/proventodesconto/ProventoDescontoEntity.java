package com.empresa.rhtutorial.entity.proventodesconto;


import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.AssertTrue;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="PROVENTO_DESCONTO")
@SequenceGenerator(name="SE_PROVENTO_DESCONTO", sequenceName="SE_PROVENTO_DESCONTO")
@Access(AccessType.FIELD)


@NamedQueries({
	@NamedQuery(name="ProventoDescontoEntity.queryMan", query="from ProventoDescontoEntity"),
	@NamedQuery(name="ProventoDescontoEntity.querySelLookup", query="select id as id, descricao as descricao from ProventoDescontoEntity where id = ? order by id asc")
})
public class ProventoDescontoEntity extends ProventoDesconto {

	private static final long serialVersionUID = 1L;
 	
	@SuppressWarnings("deprecation")
	@AssertTrue(message="Erro inesperado. O mÃªs/ano nÃ£o foi enviado com formato correto (dia deve ser 01)")
	public boolean isValidaDataTemSomenteMesAno() {
		return getAnoMesReferencia() != null && getAnoMesReferencia().getDate()==1;
	}
	
    /*
     * Construtor padrÃ£o
     */
    public ProventoDescontoEntity() {
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
