/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.candidato;


import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;

/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="HISTORICO_POLITICO")
@SequenceGenerator(name="SE_HISTORICO_POLITICO", sequenceName="SE_HISTORICO_POLITICO")
//@AccessType("field")
@Access(AccessType.FIELD)




@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="HistoricoPoliticoEntity.querySelLookup", query="select id as id, cargoAnterior as cargoAnterior from HistoricoPoliticoEntity where id = ? order by id asc")
})
public class HistoricoPoliticoEntity extends HistoricoPolitico {
 	
    /*
     * Construtor padrÃ£o
     */
    public HistoricoPoliticoEntity() {
    }
 	@Override
	public String toString() {
		return getCargoAnterior();
	}

}
