/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.test.masterdetail;


import javax.persistence.Access;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="HISTORICO_PROFISSIONAL")
@SequenceGenerator(name="SE_HISTORICO_PROFISSIONAL", sequenceName="SE_HISTORICO_PROFISSIONAL")
@Access(javax.persistence.AccessType.FIELD)
@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="ProfissionalHistoryEntity.querySelLookup", query="select new ProfissionalHistoryEntity (id, descricao) from ProfissionalHistoryEntity where id = ? order by id asc")
})
public class ProfissionalHistoryEntity extends ProfissionalHistory {

	@Transient
	private transient int hashCodePlc = 0;	

    /*
     * Construtor padrão
     */
    public ProfissionalHistoryEntity() {
    }
	public ProfissionalHistoryEntity(Long id, String descricao) {
		this.setId(id);
		this.setDescricao(descricao);
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
